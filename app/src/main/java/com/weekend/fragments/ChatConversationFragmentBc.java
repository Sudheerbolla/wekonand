/*
package com.weekend.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.model.QBUser;
import com.weekend.R;
import com.weekend.adapters.AttachmentPreviewAdapter;
import com.weekend.adapters.ChatMessagesListAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.OnImagePickedListener;
import com.weekend.interfaces.PaginationHistoryListener;
import com.weekend.interfaces.QbChatDialogMessageListenerImp;
import com.weekend.qbutils.ChatServiceUtil;
import com.weekend.qbutils.QbUsersHolder;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.utils.Constants;
import com.weekend.utils.ImagePickHelper;
import com.weekend.utils.VerboseQbChatConnectionListener;
import com.weekend.views.AttachmentPreviewAdapterView;
import com.weekend.views.CustomRecyclerView;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class ChatConversationFragmentBc extends BaseFragment implements OnImagePickedListener {
    private static final String TAG = ChatConversationFragmentBc.class.getSimpleName();
    private static final int REQUEST_CODE_ATTACHMENT = 721;
    private static final int REQUEST_CODE_SELECT_PEOPLE = 752;

    private static final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";

    public static final String EXTRA_DIALOG_ID = "dialogId";
    public static final String SHOW_ADMINS = "showAdmins";
    private static ChatConversationFragmentBc fragment;

    private ProgressBar progressBar;
    private CustomRecyclerView messagesListView;
    private EditText messageEditText;

    private LinearLayout attachmentPreviewContainerLayout;
    private Snackbar snackbar;

    private ChatMessagesListAdapter chatAdapter;
    private AttachmentPreviewAdapter attachmentPreviewAdapter;
    private ConnectionListener chatConnectionListener;

    private QBChatDialog qbChatDialog;
    private ArrayList<QBChatMessage> unShownMessages;
    private int skipPagination = 0;
    private ChatMessageListener chatMessageListener;
    private boolean showAdmins;
    private LinearLayout layoutShowAdmins;
    private DbHelper localDbHelper;
    private View rootView;

    public static ChatConversationFragmentBc newInstance(QBChatDialog qbChatDialog, boolean showAdmins) {
        fragment = new ChatConversationFragmentBc();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DIALOG_ID, qbChatDialog);
        bundle.putBoolean(SHOW_ADMINS, showAdmins);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ChatConversationFragmentBc getInstance() {
        if (fragment != null)
            return fragment;
        else return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
    }

    private void getBundleData() {
        if (getArguments() != null) {
            qbChatDialog = (QBChatDialog) getArguments().getSerializable(EXTRA_DIALOG_ID);
            showAdmins = getArguments().getBoolean(SHOW_ADMINS, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_chat_conversation, null);
            }
            ButterKnife.bind(this, rootView);
            fragment = this;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.v(TAG, "deserialized dialog = " + qbChatDialog);
                    checkLogin();
                }
            }, Constants.FRAGMENT_TRANSACTION_DELAY);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void checkLogin() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            ChatServiceUtil.getInstance().loginToChat(WeekendApplication.getInstance().loggedInQBUser, new QBEntityCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid, Bundle bundle) {
                    Log.e("ChatConversation", "onSuccess");
                    initiateChat();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("ChatConversation", e.toString());
                }
            });
        } else {
            initiateChat();
        }
    }

    private void initiateChat() {
        qbChatDialog.initForChat(QBChatService.getInstance());
        localDbHelper = new DbHelper(getActivity());
        chatMessageListener = new ChatMessageListener();
        qbChatDialog.addMessageListener(chatMessageListener);
        initChatConnectionListener();
        initViews();
        initChat();
    }

    @Override
    public void onResume() {
        super.onResume();
        ChatServiceUtil.getInstance().addConnectionListener(chatConnectionListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        ChatServiceUtil.getInstance().removeConnectionListener(chatConnectionListener);
    }

    private void sendDialogId() {
        Intent result = new Intent();
        result.putExtra(EXTRA_DIALOG_ID, qbChatDialog.getDialogId());
        getActivity().setResult(RESULT_OK, result);
    }

    @Override
    public void onImagePicked(int requestCode, File file) {
        switch (requestCode) {
            case REQUEST_CODE_ATTACHMENT:
                attachmentPreviewAdapter.add(file);
                break;
        }
    }

    @Override
    public void onImagePickError(int requestCode, Exception e) {
        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImagePickClosed(int requestCode) {
        // ignore
    }

    public void onSendChatClick(View view) {
//        if (layoutShowAdmins.getVisibility() == View.VISIBLE)
//            layoutShowAdmins.setVisibility(View.GONE);
        int totalAttachmentsCount = attachmentPreviewAdapter.getCount();
        Collection<QBAttachment> uploadedAttachments = attachmentPreviewAdapter.getUploadedAttachments();
        if (!uploadedAttachments.isEmpty()) {
            if (uploadedAttachments.size() == totalAttachmentsCount) {
                for (QBAttachment attachment : uploadedAttachments) {
                    sendChatMessage(null, attachment);
                }
            } else {
                Toaster.shortToast("chat_wait_for_attachments_to_upload");
            }
        }

        String text = messageEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(text)) {
            sendChatMessage(text, null);
        }
    }

    public void onAttachmentsClick(View view) {
        new ImagePickHelper().pickAnImage(this, REQUEST_CODE_ATTACHMENT);
    }

    public void showMessage(QBChatMessage message) {
        if (chatAdapter != null) {
            chatAdapter.addRecentMessage(message);
            messagesListView.scrollToPosition(0);
        } else {
            if (unShownMessages == null) {
                unShownMessages = new ArrayList<>();
            }
            unShownMessages.add(message);
        }
    }

    private void initViews() {
        messagesListView = (CustomRecyclerView) getActivity().findViewById(R.id.rv_chat_messages);
        messageEditText = (EditText) getActivity().findViewById(R.id.edit_chat_message);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_chat);
        attachmentPreviewContainerLayout = (LinearLayout) getActivity().findViewById(R.id.layout_attachment_preview_container);
        layoutShowAdmins = (LinearLayout) getActivity().findViewById(R.id.layoutShowAdmins);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        messagesListView.setLayoutManager(linearLayoutManager);
        ((ImageButton) getActivity().findViewById(R.id.button_chat_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendChatClick(v);
            }
        });
        ((ImageButton) getActivity().findViewById(R.id.button_chat_attachment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAttachmentsClick(v);
            }
        });

        if (showAdmins) layoutShowAdmins.setVisibility(View.VISIBLE);
        else layoutShowAdmins.setVisibility(View.GONE);

        attachmentPreviewAdapter = new AttachmentPreviewAdapter(getActivity(),
                new AttachmentPreviewAdapter.OnAttachmentCountChangedListener() {
                    @Override
                    public void onAttachmentCountChanged(int count) {
                        attachmentPreviewContainerLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
                    }
                },
                new AttachmentPreviewAdapter.OnAttachmentUploadErrorListener() {
                    @Override
                    public void onAttachmentUploadError(QBResponseException e) {
//                        showErrorSnackbar(0, e, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                onAttachmentsClick(v);
//                            }
//                        });
                    }
                });
        AttachmentPreviewAdapterView previewAdapterView = (AttachmentPreviewAdapterView) getActivity().findViewById(R.id.adapter_view_attachment_preview);
        previewAdapterView.setAdapter(attachmentPreviewAdapter);
    }

    private void sendChatMessage(String text, QBAttachment attachment) {
        if (layoutShowAdmins.getVisibility() == View.VISIBLE)
            layoutShowAdmins.setVisibility(View.GONE);
        QBChatMessage chatMessage = new QBChatMessage();
        if (attachment != null) {
            chatMessage.addAttachment(attachment);
        } else {
            chatMessage.setBody(text);
        }
        chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
        chatMessage.setDateSent(System.currentTimeMillis() / 1000);
        chatMessage.setMarkable(true);

        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType()) && !qbChatDialog.isJoined()) {
            Toaster.shortToast("You're still joining a group chat, please wait a bit");
            return;
        }

        try {
            qbChatDialog.sendMessage(chatMessage);

            if (QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
                showMessage(chatMessage);
            }

            if (attachment != null) {
                attachmentPreviewAdapter.remove(attachment);
            } else {
                messageEditText.setText("");
            }
        } catch (SmackException.NotConnectedException e) {
            Log.w(TAG, e);
            Toaster.shortToast("Can't send a message, You are not connected to chat");
        }
    }

    private void initChat() {
        switch (qbChatDialog.getType()) {
            case GROUP:
            case PUBLIC_GROUP:
                joinGroupChat();
                break;

            case PRIVATE:
                loadDialogUsers();
                break;

            default:
                Toaster.shortToast(String.format("%s %s", "chat_unsupported_type", qbChatDialog.getType().name()));
                getActivity().onBackPressed();
                break;
        }
    }

    private void joinGroupChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatServiceUtil.getInstance().join(qbChatDialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle b) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }
                loadDialogUsers();
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void leaveGroupDialog() {
        try {
            ChatServiceUtil.getInstance().leaveChatDialog(qbChatDialog);
        } catch (XMPPException | SmackException.NotConnectedException e) {
            Log.w(TAG, e);
        }
    }

    private void releaseChat() {
        qbChatDialog.removeMessageListrener(chatMessageListener);
        if (!QBDialogType.PRIVATE.equals(qbChatDialog.getType())) {
            leaveGroupDialog();
        }
    }

    private void loadDialogUsers() {
        ChatServiceUtil.getInstance().getUsersFromDialog(qbChatDialog, new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                setChatNameToActionBar();
                loadChatHistory();
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });
    }

    private void setChatNameToActionBar() {
        String chatName = getDialogName(qbChatDialog);
        activity.setTopbar(chatName, true, false, true, false, false, false, false, false,
                false, false, false, true);
    }

    private String getDialogName(QBChatDialog dialog) {
        if (dialog.getType().equals(QBDialogType.GROUP)) {
            return dialog.getName();
        } else {
            Integer opponentId = dialog.getRecipientId();
            QBUser user = QbUsersHolder.getInstance().getUserById(opponentId);
            if (user != null) {
                return TextUtils.isEmpty(user.getFullName()) ? user.getLogin() : user.getFullName();
            } else {
                return dialog.getName();
            }
        }
    }

    private void loadChatHistory() {
        ChatServiceUtil.getInstance().loadChatHistory(qbChatDialog, skipPagination, new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                // The newest messages should be in the end of list,
                // so we need to reverse list to show messages in the right order
//                if (messages.size() <= 0 || showAdmins) {
//                    layoutShowAdmins.setVisibility(View.VISIBLE);
//                } else {
//                    layoutShowAdmins.setVisibility(View.GONE);
//                }
//                Collections.reverse(messages);
                if (chatAdapter == null) {
                    chatAdapter = new ChatMessagesListAdapter(getActivity(), qbChatDialog, messages);
                    chatAdapter.setPaginationHistoryListener(new PaginationHistoryListener() {
                        @Override
                        public void downloadMore() {
                            loadChatHistory();
                        }
                    });
                    chatAdapter.setOnItemInfoExpandedListener(new ChatMessagesListAdapter.OnItemInfoExpandedListener() {
                        @Override
                        public void onItemInfoExpanded(final int position) {
                            if (isLastItem(position)) {
                                // HACK need to allow info textview visibility change so posting it via handler
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        messagesListView.setSelection(position);
//                                    }
//                                });
                            } else {
                                messagesListView.smoothScrollToPosition(position);
                            }
                        }

                        private boolean isLastItem(int position) {
                            return position == chatAdapter.getItemCount() - 1;
                        }
                    });
                    if (unShownMessages != null && !unShownMessages.isEmpty()) {
                        List<QBChatMessage> chatList = chatAdapter.getList();
                        for (QBChatMessage message : unShownMessages) {
                            if (!chatList.contains(message)) {
                                chatAdapter.add(message);
                            }
                        }
                    }
                    messagesListView.setAdapter(chatAdapter);
                } else {
                    chatAdapter.addList(messages);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
                skipPagination -= ChatServiceUtil.CHAT_HISTORY_ITEMS_PER_PAGE;
            }
        });
        skipPagination += ChatServiceUtil.CHAT_HISTORY_ITEMS_PER_PAGE;
    }

    private void initChatConnectionListener() {
        chatConnectionListener = new VerboseQbChatConnectionListener(messagesListView) {
            @Override
            public void reconnectionSuccessful() {
                super.reconnectionSuccessful();
                skipPagination = 0;
                switch (qbChatDialog.getType()) {
                    case GROUP:
                        chatAdapter = null;
                        // Join active room if we're in Group Chat
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joinGroupChat();
                            }
                        });
                        break;
                }
            }
        };
    }

    public class ChatMessageListener extends QbChatDialogMessageListenerImp implements QBChatDialogMessageListener {
        @Override
        public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
            showMessage(qbChatMessage);
        }

        @Override
        public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

        }
    }
}
*/
