package com.weekend.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBEvent;
import com.quickblox.messages.model.QBNotificationType;
import com.quickblox.users.model.QBUser;
import com.weekend.ChatActivity;
import com.weekend.R;
import com.weekend.adapters.AdminsAdapter;
import com.weekend.adapters.ChatMessagesListAdapter;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IChatMessageSelectListener;
import com.weekend.models.AdminUsersModel;
import com.weekend.qbutils.ChatServiceUtil;
import com.weekend.qbutils.dbUtils.TableMessages;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.MarshmallowPermissions;
import com.weekend.utils.StaticUtil;
import com.weekend.utils.imageUtils.ScalingUtilities;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.CustomTextView;

import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ChatFragment extends Fragment implements View.OnClickListener, IChatMessageSelectListener, MarshmallowPermissions.PermissionCallbacks {

    private String TAG = "chat";
    public static final String EXTRA_DIALOG = "dialog";
    public static final String EXTRA_USER = "user";
    public static final String SHOW_ADMINS = "showAdmins";
    private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
    public static final String PROPERTY_SENDER_NAME = "sender_name";

    private CustomEditText edtMessage;
    private ImageButton imgSend, imageAttachment;
    private NestedScrollView chatContainer;
    private CustomRecyclerView chatRecyclerView;
    private RecyclerView rvAdmins;
    private CustomTextView txtCustomMessage;
    private View rootView;
    private ProgressBar progressBar;

    private ChatMessagesListAdapter chatMessagesListAdapter;
    private QBChatDialog dialog;

    private ArrayList<QBChatMessage> qbChatMessageArrayList, dbList;
    private ArrayList<AdminUsersModel> adminUsersModelArrayList;
    private HashMap<File, QBAttachment> fileQBAttachmentHashMap;

    private Uri IMAGE_CAPTURE_URI;
    private boolean needToDeleteDialog = true;

    private static final int REQUEST_IMAGE_CAPTURE = 10001, REQUEST_IMAGE_GALLERY = 10002;
    private Bitmap profilePicBitmap;

    private Dialog alertDialog;
    private boolean showAdmins, camera, isLocal = false;
    private LinearLayout layoutShowAdmins;
    private String bookingChatMessage, imagePath;

    public ChatActivity chatActivity;

    private View.OnClickListener cameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkCameraPermissions()) {
                openCamera();
            }
        }
    };

    private View.OnClickListener galleryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickOnGallery();
        }
    };

    private View.OnClickListener closeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (alertDialog != null)
                alertDialog.dismiss();
        }
    };

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("ChatFragment", "broadcast r onReceive");
            if (intent.getAction().equalsIgnoreCase(StaticUtil.CHAT_BROADCAST)) {
                refreshList();
            } else if (intent.getAction().equalsIgnoreCase(StaticUtil.DIALOG_DELETED)) {
                getActivity().onBackPressed();
            } else if (intent.getAction().equalsIgnoreCase(StaticUtil.DIALOG_UPDATE)) {
                if (intent.getStringExtra("dialog_id").equalsIgnoreCase(dialog.getDialogId()))
                    updateDialog();
            } else if (intent.getAction().equalsIgnoreCase(StaticUtil.NEWMESSAGE_RECEIVE)) {
                if (intent.getStringExtra("dialog_id").equalsIgnoreCase(dialog.getDialogId()))
                    if (intent.hasExtra("chatmessage") && intent.getSerializableExtra("chatmessage") != null) {
                        QBChatMessage incomingChatMessage = (QBChatMessage) intent.getSerializableExtra("chatmessage");
                        addNewIncomingMessageToTheList(incomingChatMessage);
                    } else if (intent.hasExtra("ismedia") && intent.getBooleanExtra("ismedia", false)) {
                        QBChatMessage incomingChatMessage = chatActivity.getDBHelper().getLastChatMessage(dialog.getDialogId());
                        addNewIncomingMessageToTheList(incomingChatMessage);
                    } else {
                        refreshList();
                    }
            }
        }
    };

    private void updateDialog() {
        dialog = chatActivity.getDBHelper().getChatDialog(dialog.getDialogId());
        if (chatMessagesListAdapter != null) chatMessagesListAdapter.updateChatDialog(dialog);
        joinChatAndGetMessages();
        chatActivity.setTopBarTitle(dialog.getName());
    }

    public static ChatFragment newInstance(QBChatDialog qbChatDialog, boolean showAdmins, String bookingChatMessage) {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DIALOG, qbChatDialog);
        bundle.putSerializable("bookingChatMessage", bookingChatMessage);
        bundle.putBoolean(SHOW_ADMINS, showAdmins);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void addNewIncomingMessageToTheList(QBChatMessage incomingChatMessage) {
        if (!qbChatMessageArrayList.contains(incomingChatMessage)) {
            qbChatMessageArrayList.add(incomingChatMessage);
            dbList.add(incomingChatMessage);
            chatMessagesListAdapter.notifyDataSetChanged();
            scrollDown();
        }
    }

    private void refreshList() {
        qbChatMessageArrayList.clear();
        dbList.clear();
        dbList.addAll(getChatMessagesFromDB());
        qbChatMessageArrayList.addAll(dbList);
        chatMessagesListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        scrollDown();
    }

    private ArrayList<QBChatMessage> getChatMessagesFromDB() {
        return chatActivity.getDBHelper().getChatMessagesArrayList(dialog);
    }

    @Override
    public void onAttach(Context activity) {
        this.chatActivity = (ChatActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_conversation, container, false);
        initComponents();
        getBundleData();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("ChatFragment Register", StaticUtil.CHAT_BROADCAST);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StaticUtil.NEWMESSAGE_RECEIVE);
        intentFilter.addAction(StaticUtil.DIALOG_BROADCAST);
        intentFilter.addAction(StaticUtil.CHAT_BROADCAST);
        intentFilter.addAction(StaticUtil.DIALOG_DELETED);
        intentFilter.addAction(StaticUtil.DIALOG_UPDATE);
        chatActivity.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        if (dialog != null)
            LocalStorage.getInstance(chatActivity).putString("dialogId", dialog.getDialogId());
    }

    @Override
    public void onStop() {
        super.onStop();
        chatActivity.unregisterReceiver(broadcastReceiver);
    }

    private void getTextFromQB() {
        String textToDisplay = "";
        if (adminUsersModelArrayList != null && adminUsersModelArrayList.size() > 0) {
            try {
                textToDisplay = new JSONObject(adminUsersModelArrayList.get(0).getQbUser().getCustomData()).getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(textToDisplay)) {
            txtCustomMessage.setText(textToDisplay);
        }
    }

    private void getBundleData() {
        if (getArguments().containsKey(EXTRA_DIALOG)) {
            dialog = (QBChatDialog) getArguments().getSerializable(EXTRA_DIALOG);
        }
        adminUsersModelArrayList = new ArrayList<>();
        if (!WeekendApplication.adminUsersModelArrayList.isEmpty())
            adminUsersModelArrayList.addAll(WeekendApplication.adminUsersModelArrayList);
        if (getArguments().containsKey(SHOW_ADMINS)) {
            showAdmins = getArguments().getBoolean(SHOW_ADMINS, true);
        }

        if (showAdmins) {
            getTextFromQB();
            setAdminsAdapter();
            layoutShowAdmins.setVisibility(View.VISIBLE);
        } else layoutShowAdmins.setVisibility(View.GONE);

        if (dialog != null) {
            chatActivity.setTopBarTitle(dialog.getName());
            if (chatMessagesListAdapter != null) chatMessagesListAdapter.updateChatDialog(dialog);
            joinChatAndGetMessages();
        } else {
            if (WeekendApplication.adminUsersModelArrayList.isEmpty()) {
                ChatServiceUtil.getInstance().getAdminUsers(new QBEntityCallback<ArrayList<QBUser>>() {
                    @Override
                    public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                        if (!qbUsers.isEmpty()) {
                            for (int i = 0; i < qbUsers.size(); i++) {
                                WeekendApplication.adminUsersModelArrayList.add(new AdminUsersModel(qbUsers.get(i)));
                            }
                            createNewEmptyDialog();
                        }
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            } else createNewEmptyDialog();

        }
        if (getArguments().containsKey("bookingChatMessage")) {
            bookingChatMessage = getArguments().getString("bookingChatMessage");
        }

    }

    private void setAdminsAdapter() {
        rvAdmins.setLayoutManager(new LinearLayoutManager(chatActivity, LinearLayoutManager.HORIZONTAL, false));
        AdminsAdapter adminsAdapter = new AdminsAdapter(chatActivity, adminUsersModelArrayList);
        rvAdmins.setAdapter(adminsAdapter);
    }

    private void joinChatAndGetMessages() {
        if (dialog != null) {
            LocalStorage.getInstance(chatActivity).putString("dialogId", dialog.getDialogId());
            dialog.initForChat(dialog.getDialogId(), QBDialogType.GROUP, QBChatService.getInstance());
            if (dialog.isJoined()) {
                refreshList();
                edtMessage.setEnabled(true);
                chatActivity.setTopBarTitle(dialog.getName());
                if (!TextUtils.isEmpty(bookingChatMessage)) sendChatMessage(bookingChatMessage);
            } else ChatServiceUtil.getInstance().join(dialog, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    refreshList();
                    edtMessage.setEnabled(true);
                    chatActivity.setTopBarTitle(dialog.getName());
                    if (!TextUtils.isEmpty(bookingChatMessage)) sendChatMessage(bookingChatMessage);
                }

                @Override
                public void onError(QBResponseException e) {
                    edtMessage.setEnabled(false);
                }
            });
        }
    }

    private void createNewEmptyDialog() {
        progressBar.setVisibility(View.VISIBLE);
        ChatServiceUtil.getInstance().createDialogWithSelectedUsers(WeekendApplication.adminUsersModelArrayList, new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(final QBChatDialog dialog, Bundle args) {
                if (chatMessagesListAdapter != null)
                    chatMessagesListAdapter.updateChatDialog(dialog);
                chatActivity.getDBHelper().addChatDialog(dialog);
                progressBar.setVisibility(View.GONE);
                ChatFragment.this.dialog = dialog;
                joinChatAndGetMessages();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initComponents() {
        qbChatMessageArrayList = new ArrayList<>();
        dbList = new ArrayList<>();
        chatRecyclerView = (CustomRecyclerView) rootView.findViewById(R.id.rv_chat_messages);
        rvAdmins = (RecyclerView) rootView.findViewById(R.id.rvAdmins);

        ViewCompat.setNestedScrollingEnabled(chatRecyclerView, false);

        edtMessage = (CustomEditText) rootView.findViewById(R.id.edit_chat_message);
        txtCustomMessage = (CustomTextView) rootView.findViewById(R.id.txtCustomMessage);
        imgSend = (ImageButton) rootView.findViewById(R.id.button_chat_send);
        imageAttachment = (ImageButton) rootView.findViewById(R.id.button_chat_attachment);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_chat);
        progressBar.setVisibility(View.VISIBLE);
        layoutShowAdmins = (LinearLayout) rootView.findViewById(R.id.layoutShowAdmins);
        chatContainer = (NestedScrollView) rootView.findViewById(R.id.chatContainer);
        setChatAdapter();

        setListeners();
    }

    @MarshmallowPermissions.AfterPermissionGranted(MarshmallowPermissions.RUNTIME_CAMERA_PERMISSION)
    private boolean checkCameraPermissions() {
        boolean hasPermission;
        String[] perms = {Manifest.permission.CAMERA/*,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE*/};
        if (MarshmallowPermissions.hasPermissions(chatActivity, perms)) {
            // Have permissions, do the thing!
            hasPermission = true;
        } else {
            // Ask for both permissions
            MarshmallowPermissions.requestPermissions(this, getString(R.string.rationale_camera), MarshmallowPermissions.RUNTIME_CAMERA_PERMISSION, perms);
            hasPermission = false;
        }
        return hasPermission;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(chatActivity.getPackageManager()) != null) {
            IMAGE_CAPTURE_URI = StaticUtil.getOutputMediaFileUri();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_CAPTURE_URI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void clickOnGallery() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
        } catch (Exception e) {
            e.printStackTrace();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MarshmallowPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == MarshmallowPermissions.RUNTIME_CAMERA_PERMISSION) {
            if (camera) {
                openCamera();
            } else {
                clickOnGallery();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied permissions and checked NEVER ASK AGAIN.
        // This will display a dialog directing them to enable the permission in app settings.
        MarshmallowPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.rationale_ask_again), R.string.action_settings, R.string.action_cancel, perms);
        if (requestCode == MarshmallowPermissions.RUNTIME_CAMERA_PERMISSION) {
            if (alertDialog.isShowing())
                alertDialog.dismiss();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bitmap bitmap1 = StaticUtil.getImageFromCamera(chatActivity, IMAGE_CAPTURE_URI);
                File cameraFile = StaticUtil.bitmapToFile(bitmap1);
                sendImageAttachment(cameraFile);
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                try {
                    if (data != null) {
                        Uri _uri = data.getData();
                        if (_uri != null) {
                            Cursor cursor = chatActivity.getContentResolver().query(_uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                try {
                                    imagePath = cursor.getString(0);
                                    profilePicBitmap = StaticUtil.getResizeImage(chatActivity, StaticUtil.PROFILE_IMAGE_SIZE,
                                            StaticUtil.PROFILE_IMAGE_SIZE, ScalingUtilities.ScalingLogic.CROP, true, imagePath, _uri);
                                    File file = new File(String.valueOf(StaticUtil.bitmapToFile(profilePicBitmap)));
                                    sendImageAttachment(file);
                                } catch (Exception e) {
                                    e.getStackTrace();
                                }
                                cursor.close();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showImagePickerDialog(boolean isFullScreen, QBAttachment qbAttachment) {
        try {
            TextView txtCamera, txtGallery, txtCancel, txtClose;
            LinearLayout linImagePicker;
            RelativeLayout relFullScreenImage;
            ImageView imgViewFullScreen;
            final ProgressBar progressBar;

            alertDialog = new Dialog(chatActivity, R.style.AlertDialogCustom);
            alertDialog.setCancelable(false);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.image_picker_dialog);

            txtCamera = (TextView) alertDialog.findViewById(R.id.txtCamera);
            txtGallery = (TextView) alertDialog.findViewById(R.id.txtGallery);
            txtCancel = (TextView) alertDialog.findViewById(R.id.txtCancel);

            txtClose = (TextView) alertDialog.findViewById(R.id.txtClose);
            linImagePicker = (LinearLayout) alertDialog.findViewById(R.id.linImagePicker);
            relFullScreenImage = (RelativeLayout) alertDialog.findViewById(R.id.relFullScreenImage);
            imgViewFullScreen = (ImageView) alertDialog.findViewById(R.id.imgViewFullScreen);
            progressBar = (ProgressBar) alertDialog.findViewById(R.id.progressBar);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = alertDialog.getWindow();
            window.getAttributes().windowAnimations = R.style.AlertDialogCustom;
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);

            if (isFullScreen) {
                progressBar.setVisibility(View.VISIBLE);
                linImagePicker.setVisibility(View.GONE);
                relFullScreenImage.setVisibility(View.VISIBLE);
                txtClose.setOnClickListener(closeClickListener);
                if (qbAttachment != null) {
                    if (!TextUtils.isEmpty(qbAttachment.getUrl()))
                        Glide.with(this).load(qbAttachment.getUrl()).listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(imgViewFullScreen);
                    progressBar.setVisibility(View.GONE);
                } else {
                    alertDialog.dismiss();
                }
            } else {
                linImagePicker.setVisibility(View.VISIBLE);
                relFullScreenImage.setVisibility(View.GONE);

                txtCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (cameraClick != null) {
                            camera = true;
                            cameraClick.onClick(v);
                        }
                    }
                });

                txtGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        if (galleryClick != null) {
                            camera = false;
                            galleryClick.onClick(v);
                        }
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }

            alertDialog.setCancelable(true);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setChatAdapter() {
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(chatActivity));
        chatMessagesListAdapter = new ChatMessagesListAdapter(chatActivity, dialog, qbChatMessageArrayList, this);
        chatRecyclerView.setAdapter(chatMessagesListAdapter);
        scrollDown();
    }

    private void setListeners() {
        imageAttachment.setOnClickListener(this);
        imgSend.setOnClickListener(this);
        edtMessage.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edtMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    actionSendClicked();
                    return true;
                }
                return false;
            }
        });
    }

    private void actionSendClicked() {
        String messageText = edtMessage.getText().toString();
        if (TextUtils.isEmpty(messageText)) {
            return;
        }
        sendChatMessage(messageText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_chat_attachment:
                showImagePickerDialog(false, null);
                break;
            case R.id.button_chat_send:
                actionSendClicked();
                break;
            default:
                break;
        }
    }

    private void maskImage(File cameraFile) {
        fileQBAttachmentHashMap = new HashMap<>();
        QBChatMessage chatMessage = new QBChatMessage();

        chatMessage.setSaveToHistory(true);
        chatMessage.setMarkable(true);
        chatMessage.setSenderId(LocalStorage.getInstance(chatActivity).getInt(Constants.QB_OCCUPANT_ID, 0));
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(new Date().getTime() / 1000);
        QBAttachment attachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
        attachment.setUrl(cameraFile.getAbsolutePath());
        fileQBAttachmentHashMap.put(cameraFile, attachment);
        chatMessage.addAttachment(attachment);
        qbChatMessageArrayList.add(chatMessage);
        dbList.add(chatMessage);
        chatMessagesListAdapter.notifyDataSetChanged();
        scrollDown();
        isLocal = true;
    }

    private void sendImageAttachment(final File cameraFile) {
        maskImage(cameraFile);
        QBContent.uploadFileTask(cameraFile, true, null).performAsync(new QBEntityCallback<QBFile>() {
            @Override
            public void onSuccess(QBFile qbFile, Bundle bundle) {
                QBAttachment attachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
                attachment.setId(qbFile.getId().toString());
                attachment.setUrl(qbFile.getPublicUrl());
                sendImage(attachment);
            }

            @Override
            public void onError(QBResponseException e) {
                qbChatMessageArrayList.remove(qbChatMessageArrayList.size() - 1);
                chatMessagesListAdapter.notifyDataSetChanged();
            }

        });
    }

    private void sendImage(final QBAttachment attachment) {
        isLocal = true;
        try {
            if (dialog != null && dialog.isJoined()) {
                final QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setSaveToHistory(true);
                chatMessage.setMarkable(true);
                chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
                chatMessage.setDateSent(new Date().getTime() / 1000);
                chatMessage.setSenderId(LocalStorage.getInstance(chatActivity).getInt(Constants.QB_OCCUPANT_ID, 0));
                chatMessage.addAttachment(attachment);
                sendMessage(chatMessage);
                showMessage(chatMessage);
            } else {
                CommonUtil.showSnackbar(rootView, "something went wrong.please try again later");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(final QBChatMessage chatMessage) {
        needToDeleteDialog = false;
        try {
            dialog.sendMessage(chatMessage);
        } catch (SmackException e) {
            Log.e(TAG, "failed to send a message", e);
        }
        if (StaticUtil.isCurrentUserADmin())
            if (TextUtils.isEmpty(dialog.getPhoto()) || dialog.getPhoto().equalsIgnoreCase("null")) {
                dialog.setName(WeekendApplication.loggedInQBUser.getFullName());
                String url = StaticUtil.getImageUrl(WeekendApplication.loggedInQBUser.getCustomData());
                if (!TextUtils.isEmpty(url))
                    dialog.setPhoto(url);
                ChatServiceUtil.getInstance().updateChatDialogNameAndImage(dialog, new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        dialog = qbChatDialog;
                        chatActivity.getDBHelper().updateChatDialog(qbChatDialog);
                        chatActivity.setTopBarTitle(qbChatDialog.getName());
                        StaticUtil.sendSystemMessageAboutUpdatingDialog(QBChatService.getInstance().getSystemMessagesManager(), qbChatDialog);
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }
    }

    private void sendChatMessage(final String messageText) {
        if (dialog != null && dialog.isJoined()) {
            final QBChatMessage chatMessage = new QBChatMessage();
            chatMessage.setBody(messageText);
            chatMessage.setSaveToHistory(true);
            chatMessage.setMarkable(true);
            chatMessage.setSenderId(LocalStorage.getInstance(chatActivity).getInt(Constants.QB_OCCUPANT_ID, 0));
            chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
            chatMessage.setDateSent(new Date().getTime() / 1000);
            sendMessage(chatMessage);
            edtMessage.setText("");
            showMessage(chatMessage);
        } else {
            CommonUtil.showSnackbar(rootView, "something went wrong.please try again later");
        }
    }

    private boolean isExistingMessage(QBChatMessage qbChatMessage) {
        boolean isExistingMessage = false;
        for (QBChatMessage mess : qbChatMessageArrayList) {
            if (mess.getId().equalsIgnoreCase(qbChatMessage.getId())) {
                isExistingMessage = true;
                break;
            }
        }
        return isExistingMessage;
    }

    public void showMessage(QBChatMessage chatMessageModel) {
        if (!StaticUtil.hasAttachments(chatMessageModel)) {
            qbChatMessageArrayList.add(chatMessageModel);
            dbList.add(chatMessageModel);
        }
//        if (StaticUtil.hasAttachments(chatMessageModel)) {
//            if (!isLocal) {
//                qbChatMessageArrayList.add(chatMessageModel);
//                dbList.add(chatMessageModel);
//            } else {
//                isLocal = false;
//                updateArray(chatMessageModel);
//            }
//        } else {
//            qbChatMessageArrayList.add(chatMessageModel);
//            dbList.add(chatMessageModel);
//        }

        if (chatActivity != null)
            chatActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (chatMessagesListAdapter != null)
                        chatMessagesListAdapter.notifyDataSetChanged();
                    scrollDown();
                }
            });
    }

    public QBChatDialog getCurrentDialog() {
        if (dialog != null && dbList.size() == 0 && needToDeleteDialog) {
            return dialog;
        }
        return null;
    }

    private void updateArray(QBChatMessage chatMessageModel) {
        for (int i = qbChatMessageArrayList.size() - 1; i >= 0; i--) {
            QBChatMessage qbChatMessage = qbChatMessageArrayList.get(i);
            if (qbChatMessage.getId().equalsIgnoreCase(chatMessageModel.getId())) {
                qbChatMessage.setProperty(TableMessages.z_PK, String.valueOf(chatMessageModel.getProperty(TableMessages.z_PK)));
            }
        }
    }

    private void scrollDown() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chatContainer.fullScroll(View.FOCUS_DOWN);
                chatRecyclerView.scrollToPosition(chatMessagesListAdapter.getItemCount() - 1);
            }
        }, 150);
//        chatContainer.fullScroll(View.FOCUS_DOWN);
//        chatRecyclerView.scrollToPosition(chatMessagesListAdapter.getItemCount() - 1);
    }

    @Override
    public void onClick(View view, QBChatMessage qBChatMessage, int position) {
        if (StaticUtil.hasAttachments(qBChatMessage)) {
            Collection<QBAttachment> attachmentCollection = qBChatMessage.getAttachments();
            for (QBAttachment qbAttachment : attachmentCollection) {
                showImagePickerDialog(true, qbAttachment);
            }
        }
    }

    // @Unused TODO: 26/12/16 send push manually

    private void sendPushNotificationToUsers(String message) {
        QBEvent event = new QBEvent();
        StringifyArrayList<Integer> usersIds = new StringifyArrayList<>();
//        usersIds.add(opponentId);
        event.setUserIds(usersIds);
        event.setEnvironment(QBEnvironment.PRODUCTION);
        event.setNotificationType(QBNotificationType.PUSH);

        if (TextUtils.isEmpty(message)) {
            JSONObject data = new JSONObject();
            try {
//                data.put("message", SharedPreferenceUtil.getPreference(chatActivity, SharedPreferenceUtil.QB_FIRST_NAME, "") +
//                        SharedPreferenceUtil.getPreference(chatActivity, SharedPreferenceUtil.QB_LAST_NAME, "") + " : Image received");
                data.put("type", "Chat");
                data.put("content_type", "media");
                data.put("name", dialog.getName());
                data.put("dialog_id", dialog.getDialogId());
//                data.put("occupant_id", opponentId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            event.setMessage(data.toString());
        } else {
            JSONObject data = new JSONObject();
            try {
                data.put("message", dialog.getName() + " : " + message);
                data.put("type", "Chat");
                data.put("content_type", "plainText");
                data.put("name", dialog.getName());
                data.put("dialog_id", dialog.getDialogId());
//                data.put("occupant_id", opponentId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            event.setMessage(data.toString());
        }
        QBPushNotifications.createEvent(event).performAsync(new QBEntityCallback<QBEvent>() {
            @Override
            public void onSuccess(QBEvent qbEvent, Bundle args) {
                Log.e("Notification", "Chat Notification sent");
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.e("Notification", "Chat Notification error : " + errors);
            }
        });
    }

}
