package com.weekend.qbutils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.users.model.QBUser;
import com.weekend.ChatActivity;
import com.weekend.HomeActivity;
import com.weekend.R;
import com.weekend.base.WeekendApplication;
import com.weekend.models.AdminUsersModel;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.Constants;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CircleImageView;
import com.weekend.views.CustomTextView;
import com.weekend.views.bubbles.BubbleLayout;
import com.weekend.views.bubbles.BubblesManager;
import com.weekend.views.bubbles.OnInitializedCallback;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;

public class ChatService extends Service implements QBChatDialogMessageListener, QBSystemMessageListener {

    private BackgroundBinder backgroundBinder = new BackgroundBinder();
    private DbHelper localDbHelper;
    private static boolean isRunning = false;
    private Context mContext;
    private ChatServiceUtil chatServiceUtil;
    private ArrayList<QBChatDialog> chatDialogsList;
    private int count = 0, mode, bubbleXCoord = 0, bubbleYCoord = 0;
    private BubblesManager bubblesManager;
    private ArrayList<BubbleLayout> bubblesArrayList;
    private Intent notificationIntent;
    private ConnectionListener connectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection xmppConnection) {

        }

        @Override
        public void authenticated(XMPPConnection xmppConnection, boolean b) {

        }

        @Override
        public void connectionClosed() {
            loginToQuickBlox();
        }

        @Override
        public void connectionClosedOnError(Exception e) {
            loginToQuickBlox();
        }

        @Override
        public void reconnectionSuccessful() {

        }

        @Override
        public void reconnectingIn(int i) {

        }

        @Override
        public void reconnectionFailed(Exception e) {
            loginToQuickBlox();
        }
    };

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        localDbHelper = new DbHelper(getApplicationContext());
        chatServiceUtil = ChatServiceUtil.getInstance();
        chatDialogsList = new ArrayList<>();
    }

    private void getQBSignInSignUpMode() {
        if (!TextUtils.isEmpty(LocalStorage.getInstance(mContext).getString(Constants.QB_EMAIL_ID, ""))) {
            mode = Constants.QB_SIGNIN_EMAIL;
        } else if (!TextUtils.isEmpty(LocalStorage.getInstance(mContext).getString(Constants.QB_LOGIN, ""))) {
            mode = Constants.QB_SIGNIN_LOGIN;
        } else {
            mode = Constants.QB_SIGNUP;
        }
    }

    private void sendLocalBroadcast(String broadcast) {
        Intent broadcastIntent = new Intent(broadcast);
        sendBroadcast(broadcastIntent);
    }

    private void sendUpdateDialogLocalBroadcast(String dialogId) {
        Intent broadcastIntent = new Intent(StaticUtil.DIALOG_UPDATE);
        broadcastIntent.putExtra("dialog_id", dialogId);
        sendBroadcast(broadcastIntent);
    }

    private void sendNewMessageLocalBroadcast(QBChatMessage qbChatMessage) {
        Intent broadcastIntent = new Intent(StaticUtil.NEWMESSAGE_RECEIVE);
        broadcastIntent.putExtra("message", qbChatMessage.getBody());
        broadcastIntent.putExtra("dialog_id", qbChatMessage.getDialogId());
        broadcastIntent.putExtra("date_sent", qbChatMessage.getDateSent());
//        broadcastIntent.putExtra("occupant_id", String.valueOf(qbChatMessage.getSenderId()));
        broadcastIntent.putExtra("is_local_notification", true);
        if (!StaticUtil.hasAttachments(qbChatMessage))
            broadcastIntent.putExtra("chatmessage", qbChatMessage);
        else broadcastIntent.putExtra("ismedia", true);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        count = 0;
        setConnectionListener();
        loginToQuickBlox();
        return START_STICKY;
    }

    private void loginWithLoginId() {
        final QBUser qbUser = new QBUser();
        qbUser.setLogin(LocalStorage.getInstance(mContext).getString(Constants.QB_LOGIN, ""));
        qbUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
        ChatServiceUtil.getInstance().createSignInSession(qbUser, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.e("onSuccess", "loginWithLoginId onSuccess");
                onSuccessfulLoginSignUp();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("onError", "loginWithLoginId onError");
            }
        });
    }

    private void onSuccessfulLoginSignUp() {
        initializeBubblesManager();
        saveDetailsToLocalStorage();
        syncChatDialogs(0);
        getAdminUsers();
        subscribeToPush();
//        if (!QBChatService.getInstance().isLoggedIn()) {
//            ChatServiceUtil.getInstance().loginToChat(WeekendApplication.loggedInQBUser, new QBEntityCallback<Void>() {
//                @Override
//                public void onSuccess(Void aVoid, Bundle bundle) {
//                    WeekendApplication.loggedInQBUser = ChatServiceUtil.getInstance().getCurrentUser();
//                    initializeBubblesManager();
//                    saveDetailsToLocalStorage();
//                    setListener();
//                    syncChatDialogs();
//                    subscribeToPush();
//                    getAdminUsers();
//                }
//
//                @Override
//                public void onError(QBResponseException e) {
//                    Log.e("error", e.getLocalizedMessage());
//                }
//            });
//        } else {
//            WeekendApplication.loggedInQBUser = ChatServiceUtil.getInstance().getCurrentUser();
//            initializeBubblesManager();
//            saveDetailsToLocalStorage();
//            setListener();
//            syncChatDialogs();
//            subscribeToPush();
//            getAdminUsers();
//        }
    }

    private void subscribeToPush() {
        final TelephonyManager mTelephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = mTelephony.getDeviceId() != null ? mTelephony.getDeviceId() : Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        QBSubscription subscription = new QBSubscription();
        subscription.setNotificationChannel(QBNotificationChannel.GCM);
        subscription.setDeviceUdid(deviceId);
        subscription.setRegistrationID(LocalStorage.getInstance(this).getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        subscription.setEnvironment(QBEnvironment.PRODUCTION);
        QBPushNotifications.createSubscription(subscription).performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {
                Log.e("sudheer", "QBPushNotifications onSuccess");
            }

            @Override
            public void onError(QBResponseException error) {
                Log.e("sudheer", "QBPushNotifications onError " + error.toString());
            }
        });
    }

    private void setListener() {
        if (QBChatService.getInstance() != null) {
            QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
            if (incomingMessagesManager != null)
                incomingMessagesManager.addDialogMessageListener(this);
            QBSystemMessagesManager systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
            if (systemMessagesManager != null)
                systemMessagesManager.addSystemMessageListener(this);
        }
    }

    private void setConnectionListener() {
        chatServiceUtil.removeConnectionListener(connectionListener);
        chatServiceUtil.addConnectionListener(connectionListener);
    }

    private void saveDetailsToLocalStorage() {
        LocalStorage.getInstance(mContext).putString(Constants.QB_LOGIN, WeekendApplication.loggedInQBUser.getLogin());
        LocalStorage.getInstance(mContext).putInt(Constants.QB_OCCUPANT_ID, WeekendApplication.loggedInQBUser.getId());

    }

    private void getAdminUsers() {
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
        ChatServiceUtil.getInstance().getAdminUsers(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                if (!qbUsers.isEmpty()) {
                    for (int i = 0; i < qbUsers.size(); i++) {
                        WeekendApplication.adminUsersModelArrayList.add(new AdminUsersModel(qbUsers.get(i)));
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("error", "adminusers" + e.getLocalizedMessage());
            }
        });
//            }
//        });
    }

    private void initializeBubblesManager() {
        bubbleXCoord = StaticUtil.dpToPx(getApplicationContext(), Constants.SCREEN_WIDTH) / 2;
        bubbleYCoord = StaticUtil.dpToPx(getApplicationContext(), Constants.SCREEN_HEIGHT) - 200;

        bubblesArrayList = new ArrayList<>();
        bubblesManager = new BubblesManager.Builder(getApplicationContext())
                .setTrashLayout(R.layout.bubble_trash_layout)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
//                        addNewBubble();
                    }
                }).build();
        bubblesManager.initialize();

    }

    private void addNewBubble() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.bubble_layout, null);
        CustomTextView txtMessage = (CustomTextView) bubbleView.findViewById(R.id.txtMessage);
        CustomTextView txtMessageCount = (CustomTextView) bubbleView.findViewById(R.id.txtMessageCount);
        CircleImageView imgChatHead = (CircleImageView) bubbleView.findViewById(R.id.imgChatHead);
        txtMessage.setText("message");
        txtMessageCount.setText(count + "");
        txtMessageCount.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        String path = "";
//        if (senderId != WeekendApplication.loggedInQBUser.getId()) {
//            path = getProfilePic(senderId);
//        }
//        if (!TextUtils.isEmpty(path))
//            Glide.with(mContext).load(path).into(imgChatHead);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                bubblesArrayList.remove(bubble);
            }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Intent chatIntent = new Intent(mContext, ChatActivity.class);
                chatIntent.putExtra("isFromBubble", true);
//                chatIntent.putExtra("dialogId", dialogId);
                startActivity(chatIntent);
            }
        });
        bubbleView.setShouldStickToWall(true);
        bubblesArrayList.add(bubbleView);
        bubblesManager.addBubble(bubbleView, 0, 0);
    }

    private void addNewBubble(final String dialogId, int senderId, String message, int count) {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.bubble_layout, null);
        bubbleView.setTag(senderId);
        CustomTextView txtMessage = (CustomTextView) bubbleView.findViewById(R.id.txtMessage);
        CustomTextView txtMessageCount = (CustomTextView) bubbleView.findViewById(R.id.txtMessageCount);
        CircleImageView imgChatHead = (CircleImageView) bubbleView.findViewById(R.id.imgChatHead);
        txtMessage.setText(message);
        txtMessageCount.setText(count + "");
        txtMessageCount.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
        String path = "";
        if (senderId != WeekendApplication.loggedInQBUser.getId() && !WeekendApplication.adminUsersModelArrayList.isEmpty()) {
            path = StaticUtil.getProfilePic(senderId, WeekendApplication.adminUsersModelArrayList);
        }
        if (!TextUtils.isEmpty(path))
            Glide.with(mContext).load(path).into(imgChatHead);

        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) {
                bubblesArrayList.remove(bubble);
            }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {
            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Intent chatIntent = new Intent(mContext, ChatActivity.class);
                chatIntent.putExtra("isFromBubble", true);
                chatIntent.putExtra("dialogId", dialogId);
                startActivity(chatIntent);
            }
        });
        bubbleView.setShouldStickToWall(true);
        bubblesArrayList.add(bubbleView);
        bubblesManager.addBubble(bubbleView, bubbleXCoord, bubbleYCoord);
    }

    private void removeBubble(BubbleLayout bubbleLayout) {
        bubblesManager.removeBubble(bubbleLayout);
        bubblesArrayList.remove(bubbleLayout);
    }

    private boolean needToAddNewBubble(QBChatDialog qbChatDialog, QBChatMessage qbChatMessage, int count) {
        if (qbChatMessage.getSenderId().equals(WeekendApplication.loggedInQBUser.getId())) {
            return false;
        }
        for (int i = 0; i < bubblesArrayList.size(); i++) {
            if (qbChatMessage.getSenderId() == (int) bubblesArrayList.get(i).getTag()) {
                removeBubble(bubblesArrayList.get(i));
                addNewBubble(qbChatMessage.getDialogId(), qbChatMessage.getSenderId(), TextUtils.isEmpty(qbChatMessage.getBody()) ? "Attachment" : qbChatMessage.getBody(), count);
                createChatNotification(qbChatDialog, qbChatMessage);
                return false;
            }
        }
        return true;
    }

    private void loginWithEmail() {
        final QBUser qbUser = new QBUser();
        qbUser.setEmail(LocalStorage.getInstance(mContext).getString(Constants.QB_EMAIL_ID, ""));
        qbUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
        ChatServiceUtil.getInstance().createSignInWithEmailSession(qbUser, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.e("onSuccess", "loginWithEmail onSuccess");
                onSuccessfulLoginSignUp();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("onError", "loginWithEmail onError");
            }
        });
    }

    private void signupQBUsingRandomLogin() {
        final QBUser qbUser = new QBUser();
        qbUser.setLogin(StaticUtil.generateRandomLoginQb());
        qbUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
        ChatServiceUtil.getInstance().signUpSignInCheck(qbUser, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                Log.e("onSuccess", "signupQBUsingRandomLogin onSuccess");
                isRunning = true;
                onSuccessfulLoginSignUp();
            }


            @Override
            public void onError(QBResponseException e) {
                Log.e("onError", "signupQBUsingRandomLogin onError");
                if (count <= 5) {
                    signupQBUsingRandomLogin();
                    ++count;
                }
                e.printStackTrace();
            }
        });
    }

    private void performAppropriateQBSignInSignUp() {
        getQBSignInSignUpMode();
        switch (mode) {
            case Constants.QB_SIGNIN_EMAIL:
                loginWithEmail();
                break;
            case Constants.QB_SIGNIN_LOGIN:
                loginWithLoginId();
                break;
            case Constants.QB_SIGNUP:
                signupQBUsingRandomLogin();
                break;
            default:
                break;
        }
    }

    private void loginToQuickBlox() {
        if (!QBChatService.getInstance().isLoggedIn()) {
            performAppropriateQBSignInSignUp();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.e("sudheer", "already logged in syncChatDialogs");
                    onSuccessfulLoginSignUp();
                }
            });
        }
    }

    private void syncChatDialogs(final int skipPagination) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setListener();
                try {
                    ChatServiceUtil.getInstance().getDialogs(skipPagination, new QBEntityCallback<ArrayList<QBChatDialog>>() {
                        @Override
                        public void onSuccess(final ArrayList<QBChatDialog> chatDialogArrayList, Bundle bundle) {
                            if (!chatDialogArrayList.isEmpty()) {
                                localDbHelper.addChatDialogs(chatDialogArrayList);
                                chatDialogsList.addAll(chatDialogArrayList);
                                sendLocalBroadcast(StaticUtil.DIALOG_BROADCAST);
                                syncChatDialogs(skipPagination + StaticUtil.PAGINATION_AND_LIMIT_QB_MESSAGES);
                            } else {
                                for (QBChatDialog qbChatDialog : chatDialogsList) {
                                    loadChatHistory(qbChatDialog, 0);
                                }
                                sendLocalBroadcast(StaticUtil.DIALOG_BROADCAST);
                            }
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e("onError", "getDialogs " + e.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadChatHistory(final QBChatDialog qbChatDialog, final int skipPagination) {
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setLimit(StaticUtil.PAGINATION_AND_LIMIT_QB_MESSAGES);
        customObjectRequestBuilder.setSkip(skipPagination);
        customObjectRequestBuilder.sortAsc(ChatServiceUtil.CHAT_HISTORY_ITEMS_SORT_FIELD);
        QBRestChatService.getDialogMessages(qbChatDialog, customObjectRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
                synchronized (ChatService.this) {
                    if (messages.size() > 0) {
                        localDbHelper.addChatMessages(messages);
                        sendLocalBroadcast(StaticUtil.CHAT_BROADCAST);
                        loadChatHistory(qbChatDialog, skipPagination + StaticUtil.PAGINATION_AND_LIMIT_QB_MESSAGES);
                    } else {
                        sendLocalBroadcast(StaticUtil.CHAT_BROADCAST);
                    }
                }
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.e("loadChatHistory", "onError : " + errors.getErrors());
            }
        });

    }

    private void processIncomingMessage(String dialogId, final QBChatMessage qbChatMessage) {
        QBChatDialog tempChatDialog = new QBChatDialog();
        tempChatDialog.setDialogId(dialogId);
        if (chatDialogsList.size() <= 0) {
            QBRestChatService.getChatDialogById(dialogId).performAsync(new QBEntityCallback<QBChatDialog>() {
                @Override
                public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                    localDbHelper.addChatDialog(qbChatDialog);
                    localDbHelper.addChatMessage(qbChatMessage);
                    qbChatDialog.initForChat(qbChatDialog.getDialogId(), QBDialogType.GROUP, QBChatService.getInstance());
                    if (!qbChatDialog.isJoined()) ChatServiceUtil.getInstance().join(qbChatDialog);
                    chatDialogsList.add(qbChatDialog);
                    sendLocalBroadcast(StaticUtil.DIALOG_CREATED);
                    if (!qbChatMessage.getSenderId().equals(WeekendApplication.loggedInQBUser.getId())) {
                        addNewBubble(qbChatDialog.getDialogId(), qbChatMessage.getSenderId(), TextUtils.isEmpty(qbChatMessage.getBody()) ? "Attachment" : qbChatMessage.getBody(), count);
                        createChatNotification(qbChatDialog, qbChatMessage);
                    }
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        } else {
            if (!chatDialogsList.contains(tempChatDialog)) {
                QBRestChatService.getChatDialogById(dialogId).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        localDbHelper.addChatDialog(qbChatDialog);
                        localDbHelper.addChatMessage(qbChatMessage);
                        qbChatDialog.initForChat(qbChatDialog.getDialogId(), QBDialogType.GROUP, QBChatService.getInstance());
                        if (!qbChatDialog.isJoined())
                            ChatServiceUtil.getInstance().join(qbChatDialog);
                        chatDialogsList.add(qbChatDialog);
                        sendLocalBroadcast(StaticUtil.DIALOG_CREATED);
                        if (!qbChatMessage.getSenderId().equals(WeekendApplication.loggedInQBUser.getId())) {
                            addNewBubble(qbChatDialog.getDialogId(), qbChatMessage.getSenderId(), TextUtils.isEmpty(qbChatMessage.getBody()) ? "Attachment" : qbChatMessage.getBody(), count);
                            createChatNotification(qbChatDialog, qbChatMessage);
                        }
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            } else {
                localDbHelper.addChatMessage(qbChatMessage);
                localDbHelper.updateLastMessageInDialog(qbChatMessage.getDialogId(), qbChatMessage.getBody(), qbChatMessage.getDateSent());
                sendNewMessageLocalBroadcast(qbChatMessage);
                ChatServiceUtil.getInstance().getDialogById(dialogId, new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        int count = 0;
                        if (qbChatDialog.getUnreadMessageCount() != null)
                            count = qbChatDialog.getUnreadMessageCount();
                        localDbHelper.updateUnreadMessageCount(qbChatDialog.getDialogId(), count);
                        if (needToAddNewBubble(qbChatDialog, qbChatMessage, count)) {
                            addNewBubble(qbChatDialog.getDialogId(), qbChatMessage.getSenderId(), TextUtils.isEmpty(qbChatMessage.getBody()) ? "Attachment" : qbChatMessage.getBody(), count);
                            createChatNotification(qbChatDialog, qbChatMessage);
                        }
                        sendNewMessageLocalBroadcast(qbChatMessage);
                    }

                    @Override
                    public void onError(QBResponseException e) {

                    }
                });
            }
        }
    }

    private void createChatNotification(QBChatDialog qbChatDialog, QBChatMessage qbChatMessage) {
        if (notificationIntent == null) notificationIntent = new Intent(this, HomeActivity.class);
        Bundle notificatioBundel = new Bundle();
        notificatioBundel.putString("message", qbChatMessage.getBody());
        notificatioBundel.putString("google.message_id", qbChatMessage.getId());
        notificatioBundel.putBoolean("isFromChat", true);
        notificationIntent.putExtras(notificatioBundel);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int id = Integer.parseInt(qbChatMessage.getId());
        String message = TextUtils.isEmpty(qbChatMessage.getBody()) ? "Attachment" : qbChatMessage.getBody();
        PendingIntent contentIntent = PendingIntent.getActivity(this, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_app_launcher);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_app_launcher);
        builder.setLargeIcon(image);
        builder.setStyle(new android.support.v7.app.NotificationCompat.BigTextStyle().bigText(message));
        builder.setColor(getResources().getColor(R.color.colorPrimary));
        builder.setContentIntent(contentIntent).setAutoCancel(true).build();
        builder.setContentTitle(qbChatDialog.getName());
        builder.setContentText(message);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    @Override
    public void processMessage(String dialogId, final QBChatMessage qbChatMessage, Integer integer) {
        processIncomingMessage(dialogId, qbChatMessage);
    }

    @Override
    public void processError(String dialogId, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
        Log.e("processError", "incomingMessagesManager");
    }

    @Override
    public void processMessage(QBChatMessage qbChatMessage) {
        try {
            if (qbChatMessage != null) {
                String dialogId = qbChatMessage.getDialogId();
                if (qbChatMessage.getProperties().containsKey(StaticUtil.PROPERTY_NOTIFICATION_TYPE) &&
                        ((String) qbChatMessage.getProperty(StaticUtil.PROPERTY_NOTIFICATION_TYPE)).equalsIgnoreCase(StaticUtil.DELETING_DIALOG)) {
                    localDbHelper.deleteChatDialog(dialogId);
                    localDbHelper.deleteChatHistory(dialogId);
                    sendLocalBroadcast(StaticUtil.DIALOG_DELETED);
                    QBChatDialog qbChatDialog = new QBChatDialog();
                    qbChatDialog.setDialogId(dialogId);
                    chatDialogsList.remove(qbChatDialog);
                } else if (qbChatMessage.getProperties().containsKey(StaticUtil.PROPERTY_NOTIFICATION_TYPE) &&
                        ((String) qbChatMessage.getProperty(StaticUtil.PROPERTY_NOTIFICATION_TYPE)).equalsIgnoreCase(StaticUtil.UPDATING_DIALOG)) {
                    QBRestChatService.getChatDialogById(dialogId).performAsync(new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                            localDbHelper.updateChatDialog(qbChatDialog);
                            sendUpdateDialogLocalBroadcast(qbChatDialog.getDialogId());
                        }

                        @Override
                        public void onError(QBResponseException e) {

                        }
                    });
                } else {
                    processIncomingMessage(dialogId, qbChatMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processError(QBChatException e, QBChatMessage qbChatMessage) {
        Log.e("processError", "systemMessagesManager");
    }

    public class BackgroundBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return backgroundBinder;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        ChatServiceUtil.getInstance().logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void o, Bundle bundle) {
            }

            @Override
            public void onError(QBResponseException e) {
            }
        });
        super.onDestroy();
    }

}
