package com.weekend.qbutils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.weekend.base.WeekendApplication;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.Constants;
import com.weekend.utils.StaticUtil;

import java.util.ArrayList;

public class UserLoginAndUpdateService extends Service {

    private String tag = "UserLoginAndUpdateService";
    private BackgroundBinder backgroundBinder = new BackgroundBinder();
    //    private QBUser loggedInQBUser;
    int count = 0;
    private DbHelper localDbHelper;
    private static boolean isRunning = false;
    private Context mContext;
    ChatServiceUtil chatServiceUtil;
    private LocalStorage localStorage;

    public static boolean isRunning() {
        return isRunning;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        localDbHelper = new DbHelper(getApplicationContext());
        localStorage = LocalStorage.getInstance(mContext);
        chatServiceUtil = ChatServiceUtil.getInstance();
    }

    private void sendLocalBroadcast(QBChatMessage qbChatMessage) {
        Log.e("ChatService", "sendLocalBroadcast");
        Intent broadcastIntent = new Intent(StaticUtil.NEWMESSAGE_RECEIVE);
        broadcastIntent.putExtra("message", qbChatMessage.getBody());
//        broadcastIntent.putExtra("attachment", qbChatMessage.getAttachments());
        broadcastIntent.putExtra("type", "Chat");
        broadcastIntent.putExtra("dialog_id", qbChatMessage.getDialogId());
        broadcastIntent.putExtra("date_sent", qbChatMessage.getDateSent());
        broadcastIntent.putExtra("occupant_id", String.valueOf(qbChatMessage.getSenderId()));
        broadcastIntent.putExtra("is_local_notification", true);

        sendBroadcast(broadcastIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        count = 0;

        if (!TextUtils.isEmpty(localStorage.getString(Constants.QB_EMAIL_ID, ""))) {
            final QBUser newUser = new QBUser();
            newUser.setEmail(localStorage.getString(Constants.QB_EMAIL_ID, ""));
            newUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
            ChatServiceUtil.getInstance().checkIfUserExistsAlreadyWithEmail(newUser, new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    Log.e("onSuccess", "User Exists with email already");
                    if (QBChatService.getInstance().isLoggedIn()) {
                        addNewUserInExistingDialogs(qbUser);
                    }
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("onError", "User Doesn't Exists with email already");
                    updateCurrentUserWithThisUser(newUser);
                }
            });
        }

        return START_STICKY;
    }

    private void updateCurrentUserWithThisUser(QBUser newUser) {
        QBUser newQbUser = WeekendApplication.getInstance().loggedInQBUser;
        if (newQbUser != null) {
            if (newUser != null && !TextUtils.isEmpty(newUser.getEmail()))
                newQbUser.setEmail(newUser.getEmail());
            newQbUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
            newQbUser.setOldPassword(StaticUtil.getWeekendCustomerPasswordQb());
            newQbUser.setId(localStorage.getInt(Constants.QB_OCCUPANT_ID, 0));
            newQbUser.setFullName(localStorage.getString(Constants.SP_KEY_FIRST_NAME, "") + localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
            QBUsers.updateUser(newQbUser).performAsync(new QBEntityCallback<QBUser>() {
                @Override
                public void onSuccess(QBUser qbUser, Bundle bundle) {
                    Log.e("onSuccess", "updateUserDetails onSuccess");
                    closeDbAndReStartChatService();
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.e("onError", "updateUserDetails onError");
                }
            });
        }
    }

    private void closeDbAndReStartChatService() {
        localDbHelper.closeDb();
        mContext.stopService(new Intent(mContext, ChatService.class));
        mContext.startService(new Intent(mContext, ChatService.class));
    }

    private void addNewUserInExistingDialogs(final QBUser newUser) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ArrayList<QBChatDialog> chatDialogArrayList = localDbHelper.getChatDialogs();
                if (!chatDialogArrayList.isEmpty()) {
                    ChatServiceUtil.getInstance().updateChatDialogs(chatDialogArrayList, newUser, new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                            Log.e("onSuccess", "updateChatDialog onSuccess");
                            ChatServiceUtil.getInstance().logout(new QBEntityCallback<Void>() {
                                @Override
                                public void onSuccess(Void o, Bundle bundle) {
                                    removeSubscription();
                                    closeDbAndReStartChatService();
                                }

                                @Override
                                public void onError(QBResponseException e) {

                                }
                            });
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e("onError", "updateChatDialog onError");
                            removeSubscription();
                            closeDbAndReStartChatService();
                        }
                    });
                } else {
                    closeDbAndReStartChatService();
                }
            }
        });
    }

    private void removeSubscription() {
        QBPushNotifications.getSubscriptions().performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {
                for (QBSubscription subscription : subscriptions) {
                    if (subscription.getDevice().getId().equals(localStorage.getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""))) {
                        QBPushNotifications.deleteSubscription(subscription.getId()).performAsync(new QBEntityCallback<Void>() {

                            @Override
                            public void onSuccess(Void aVoid, Bundle bundle) {

                            }

                            @Override
                            public void onError(QBResponseException errors) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });
    }

    public class BackgroundBinder extends Binder {
        public UserLoginAndUpdateService getService() {
            return UserLoginAndUpdateService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return backgroundBinder;
    }

}
