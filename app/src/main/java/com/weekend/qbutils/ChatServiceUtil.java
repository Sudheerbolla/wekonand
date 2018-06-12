package com.weekend.qbutils;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBDialogRequestBuilder;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBProgressCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.helper.StringifyArrayList;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.QbEntityCallbackTwoTypeWrapper;
import com.weekend.models.AdminUsersModel;
import com.weekend.utils.StaticUtil;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ChatServiceUtil {

    public static final String APP_ID = "69013";
    public static final String AUTH_KEY = "Zk93mhQMjx2Qcc2";
    public static final String AUTH_SECRET = "6JWybQPRPU7gCH5";
    public static final String ACCOUNT_KEY = "rBArrzxhTz2-mk1rBFPj";

    public static final String CHAT_HISTORY_ITEMS_SORT_FIELD = "date_sent";

    private QBChatService chatService;
    private static ChatServiceUtil instance;

    private static final String TAG = ChatServiceUtil.class.getSimpleName();

    //    private static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 60;
    private static final int AUTO_PRESENCE_INTERVAL_IN_SECONDS = 0;

    public static synchronized ChatServiceUtil getInstance() {
        if (instance == null) {
            instance = new ChatServiceUtil();
        }
        return instance;
    }

    public static void init(Application application) {
        QBSettings.getInstance().init(application, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    private ChatServiceUtil() {
        chatService = QBChatService.getInstance();
        QBChatService.setDebugEnabled(true);
//        QBChatService.setDefaultConnectionTimeout(QBChatService.getDefaultConnectionTimeout() * 2);
        chatService.setReconnectionAllowed(true);
        QBChatService.setDefaultPacketReplyTimeout(10000);
        QBChatService.setDefaultAutoSendPresenceInterval(AUTO_PRESENCE_INTERVAL_IN_SECONDS);
        chatService.setUseStreamManagement(true);

        removeConnectionListener(chatConnectionListener);
        addConnectionListener(chatConnectionListener);

        QBChatService.ConfigurationBuilder configurationBuilder = new QBChatService.ConfigurationBuilder();
        configurationBuilder.setKeepAlive(true).setSocketTimeout(120).setAutojoinEnabled(true);
        QBChatService.setConfigurationBuilder(configurationBuilder);

    }

    public void addConnectionListener(ConnectionListener listener) {
        chatService.addConnectionListener(listener);
    }

    public void removeConnectionListener(ConnectionListener listener) {
        chatService.removeConnectionListener(listener);
    }

    public void createSignInSession(final QBUser qbUser, final QBEntityCallback<Void> callback) {
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle args) {
                qbUser.setId(session.getUserId());
                signIn(qbUser, callback);
            }

            @Override
            public void onError(QBResponseException errors) {
                android.util.Log.e("onError : ", errors.toString());
                if (errors.getHttpStatusCode() == 401) {
                    createSession(qbUser, callback, false);
                } else callback.onError(errors);
            }
        });
    }

    public void createSignInWithEmailSession(final QBUser qbUser, final QBEntityCallback<Void> callback) {
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle args) {
                qbUser.setId(session.getUserId());
                signInWithEmail(qbUser, callback);
            }

            @Override
            public void onError(QBResponseException errors) {
                android.util.Log.e("onError : ", errors.toString());
                if (errors.getHttpStatusCode() == 401) {
                    createSession(qbUser, callback, false);
                } else callback.onError(errors);
            }

        });
    }

    public void checkIfUserExistsAlreadyWithEmail(final QBUser qbUser, final QBEntityCallback<QBUser> callback) {
        QBUsers.signInByEmail(qbUser.getEmail(), qbUser.getPassword()).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                android.util.Log.e("onSuccess : ", "onSuccess");
                callback.onSuccess(qbUser, bundle);
            }

            @Override
            public void onError(QBResponseException error) {
                android.util.Log.e("onError : ", error.toString());
                callback.onError(error);
            }
        });

    }

    private void signInWithEmail(final QBUser qbUser, final QBEntityCallback<Void> callback) {
        QBUsers.signInByEmail(qbUser.getEmail(), qbUser.getPassword()).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
//                WeekendApplication.loggedInQBUser = qbUser;
//                callback.onSuccess(null, bundle);
                loginToChat(qbUser, callback);
            }

            @Override
            public void onError(QBResponseException error) {
                android.util.Log.e("onError : ", error.toString());
                callback.onError(error);
            }
        });
    }

    public void createSession(final QBUser qbUser, final QBEntityCallback<Void> callback, boolean withUser) {
        if (withUser) {
            QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession session, Bundle args) {
                    qbUser.setId(session.getUserId());
                    signUpToChat(qbUser, callback);
                }

                @Override
                public void onError(QBResponseException errors) {
                    android.util.Log.e("onError : ", errors.toString());
                    if (errors.getHttpStatusCode() == 401) {
                        createSession(qbUser, callback, false);
                    } else callback.onError(errors);
                }

            });
        } else {
            QBAuth.createSession().performAsync(new QBEntityCallback<QBSession>() {
                @Override
                public void onSuccess(QBSession session, Bundle args) {
                    qbUser.setId(session.getUserId());
                    signUpToChat(qbUser, callback);
                }

                @Override
                public void onError(QBResponseException errors) {
                    android.util.Log.e("onError : ", errors.toString());
                    callback.onError(errors);
                }

            });
        }
    }

    public void signUpSignInCheck(final QBUser user, final QBEntityCallback<Void> callback) {
        createSession(user, callback, true);
    }

    public void logout(final QBEntityCallback<Void> qbEntityCallback) {
        chatService.logout(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                qbEntityCallback.onSuccess(result, bundle);
            }

            @Override
            public void onError(QBResponseException list) {
                qbEntityCallback.onError(list);
            }
        });
    }

    public void signUpToChat(final QBUser qbUser, final QBEntityCallback<Void> callback) {
        android.util.Log.e("onSuccess : ", "onSuccess");
        QBUsers.signUpSignInTask(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(final QBUser qbUser, Bundle bundle) {
//                WeekendApplication.loggedInQBUser = qbUser;
                loginToChat(qbUser, callback);
//                callback.onSuccess(null, bundle);
            }

            @Override
            public void onError(QBResponseException error) {
                android.util.Log.e("onError : ", error.toString());
                if (error.getHttpStatusCode() == 422) {
                    signIn(qbUser, callback);
                } else callback.onError(error);
            }
        });
    }

    public void loginToChat(final QBUser qbUser, final QBEntityCallback<Void> callback) {
        if (TextUtils.isEmpty(qbUser.getPassword())) {
            qbUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
        }
        try {
            if (!QBChatService.getInstance().isLoggedIn()) {
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {
                        Log.e("onSuccess : ", "onSuccess");
                        WeekendApplication.loggedInQBUser = getCurrentUser();
                        callback.onSuccess(null, bundle);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("onError : ", e.toString());
                        if (!QBChatService.getInstance().isLoggedIn()) callback.onError(e);
                    }
                });
            } else {
                WeekendApplication.loggedInQBUser = getCurrentUser();
                callback.onSuccess(null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", e.toString());
        }

    }

    private void signIn(final QBUser qbUser, final QBEntityCallback<Void> callback) {
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(final QBUser qbUser, Bundle bundle) {
                loginToChat(qbUser, callback);
//                WeekendApplication.loggedInQBUser = qbUser;
//                callback.onSuccess(null, bundle);
            }

            @Override
            public void onError(QBResponseException error) {
                android.util.Log.e("onError : ", error.toString());
                callback.onError(error);
            }
        });
    }

    public void getDialogs(int skipDialogRecords, final QBEntityCallback<ArrayList<QBChatDialog>> callback) {
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setSkip(skipDialogRecords);
        customObjectRequestBuilder.setLimit(StaticUtil.PAGINATION_AND_LIMIT_QB_MESSAGES);
//        customObjectRequestBuilder.sortDesc(CHAT_HISTORY_ITEMS_SORT_FIELD);
        QBRestChatService.getChatDialogs(QBDialogType.GROUP, customObjectRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(final ArrayList<QBChatDialog> dialogs, Bundle args) {
                callback.onSuccess(dialogs, null);
            }

            @Override
            public void onError(QBResponseException errors) {
                callback.onError(errors);
            }

        });
    }

    public void getDialogById(String dialogId, final QBEntityCallback<QBChatDialog> callback) {
        QBRestChatService.getChatDialogById(dialogId).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                callback.onSuccess(qbChatDialog, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }

    public QBUser getCurrentUser() {
        return QBChatService.getInstance().getUser();
    }

    public void join(QBChatDialog chatDialog, final QBEntityCallback<Void> callback) {
        DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(0);
        chatDialog.join(history, callback);
    }

    public void join(QBChatDialog chatDialog) {
        DiscussionHistory history = new DiscussionHistory();
        history.setMaxStanzas(0);
        try {
            chatDialog.join(history);
        } catch (XMPPException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }

    public void leaveChatDialog(QBChatDialog chatDialog) throws XMPPException, SmackException.NotConnectedException {
        chatDialog.leave();
    }

    public void createDialogWithSelectedUsers(final ArrayList<AdminUsersModel> users, final QBEntityCallback<QBChatDialog> callback) {
        if (users.contains(new AdminUsersModel(WeekendApplication.loggedInQBUser)))
            users.remove(new AdminUsersModel(WeekendApplication.loggedInQBUser));
        final ArrayList<QBUser> qbUserArrayList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            qbUserArrayList.add(users.get(i).getQbUser());
        }
        QBChatDialog qbChatDialog = DialogUtils.buildDialog(qbUserArrayList.toArray(new QBUser[qbUserArrayList.size()]));
        QBRestChatService.createChatDialog(qbChatDialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                callback.onSuccess(qbChatDialog, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }

    public void updateChatDialogNew(final QBChatDialog qbDialog, final QBUser newUser, final QBEntityCallback<QBChatDialog> callback) {
        QBDialogRequestBuilder qbRequestBuilder = new QBDialogRequestBuilder();
        qbRequestBuilder.addUsers(newUser.getId());
        QBRestChatService.updateGroupChatDialog(qbDialog, qbRequestBuilder).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                callback.onSuccess(qbChatDialog, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }

    public void updateChatDialogNameAndImage(final QBChatDialog qbDialog, final QBEntityCallback<QBChatDialog> callback) {
        QBDialogRequestBuilder qbRequestBuilder = new QBDialogRequestBuilder();
        QBRestChatService.updateGroupChatDialog(qbDialog, qbRequestBuilder).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                callback.onSuccess(qbChatDialog, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }

    public void updateChatDialogs(ArrayList<QBChatDialog> allChatDialogs, final QBUser newUser, final QBEntityCallback<QBChatDialog> callback) {
        final int size = allChatDialogs.size();
        for (int i = 0; i < size; i++) {
            QBChatDialog qbChatDialog = allChatDialogs.get(i);
            QBDialogRequestBuilder qbRequestBuilder = new QBDialogRequestBuilder();
            qbRequestBuilder.addUsers(newUser.getId());
            final int finalI = i;
            QBRestChatService.updateGroupChatDialog(qbChatDialog, qbRequestBuilder).performAsync(new QBEntityCallback<QBChatDialog>() {
                @Override
                public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                    if (finalI == (size - 1))
                        callback.onSuccess(qbChatDialog, bundle);
                }

                @Override
                public void onError(QBResponseException e) {
                    callback.onError(e);
                }
            });
        }
    }

    public void getAdminUsers(final QBEntityCallback<ArrayList<QBUser>> callback) {
        List<String> tags = new ArrayList<>();
        tags.add(StaticUtil.ADMIN_USERS_TAG);
        QBUsers.getUsersByTags(tags, new QBPagedRequestBuilder()).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle bundle) {
                Log.e("onSuccess", "getUsersByTags onSuccess");
                callback.onSuccess(users, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("onError", "getUsersByTags onError");
                callback.onError(e);
            }
        });
    }

    public void loadFileAsAttachment(File file, QBEntityCallback<QBAttachment> callback) {
        loadFileAsAttachment(file, callback, null);
    }

    public void loadFileAsAttachment(File file, final QBEntityCallback<QBAttachment> callback, QBProgressCallback progressCallback) {
        QBContent.uploadFileTask(file, true, null, progressCallback).performAsync(
                new QbEntityCallbackTwoTypeWrapper<QBFile, QBAttachment>(callback) {
                    @Override
                    public void onSuccess(QBFile qbFile, Bundle bundle) {
                        QBAttachment attachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
                        attachment.setId(qbFile.getId().toString());
                        attachment.setUrl(qbFile.getPublicUrl());
                        callback.onSuccess(attachment, bundle);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        callback.onError(e);
                    }
                });
    }

    public void loadChatHistory(QBChatDialog dialog, int skipPagination, final QBEntityCallback<ArrayList<QBChatMessage>> callback) {
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setSkip(skipPagination);
        messageGetBuilder.setLimit(StaticUtil.PAGINATION_AND_LIMIT_QB_MESSAGES);
        messageGetBuilder.sortDesc(CHAT_HISTORY_ITEMS_SORT_FIELD);
        messageGetBuilder.markAsRead(false);

        QBRestChatService.getDialogMessages(dialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                callback.onSuccess(qbChatMessages, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }

    private void getUsersFromMessages(final ArrayList<QBChatMessage> messages, final Set<Integer> userIds, final QBEntityCallback<ArrayList<QBChatMessage>> callback) {
        QBPagedRequestBuilder requestBuilder = new QBPagedRequestBuilder(userIds.size(), 1);
        QBUsers.getUsersByIDs(userIds, requestBuilder).performAsync(new QbEntityCallbackTwoTypeWrapper<ArrayList<QBUser>, ArrayList<QBChatMessage>>(callback) {
            @Override
            public void onSuccess(ArrayList<QBUser> users, Bundle params) {
                callback.onSuccess(messages, params);
            }
        });
    }

    public void deleteDialogs(Collection<QBChatDialog> dialogs, final QBEntityCallback<ArrayList<String>> callback) {
        StringifyArrayList<String> dialogsIds = new StringifyArrayList<>();
        for (QBChatDialog dialog : dialogs) {
            dialogsIds.add(dialog.getDialogId());
        }
        QBRestChatService.deleteDialogs(dialogsIds, false, null).performAsync(callback);
    }

    public void deleteDialog(QBChatDialog qbDialog, final QBEntityCallback<Void> callback) {
        QBRestChatService.deleteDialog(qbDialog.getDialogId(), true).performAsync(new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                callback.onSuccess(aVoid, bundle);
            }

            @Override
            public void onError(QBResponseException e) {
                callback.onError(e);
            }
        });
    }

    public Integer getOpponentIDForPrivateDialog(QBChatDialog dialog) {
        Integer opponentID = -1;
        if (dialog.getOccupants() != null && dialog.getOccupants().size() > 0)
            for (Integer userID : dialog.getOccupants()) {
                if (getCurrentUser() != null && getCurrentUser().getId() != null && !userID.equals(getCurrentUser().getId())) {
                    opponentID = userID;
                    break;
                }
            }
        return opponentID;
    }

    private boolean isPresent;

    public boolean isMessagePresentInDialog(QBChatDialog qbChatDialog, final QBChatMessage qbChatMessage) {
        isPresent = false;
        QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
        customObjectRequestBuilder.setLimit(25);
        customObjectRequestBuilder.sortAsc("date_sent");
        QBRestChatService.getDialogMessages(qbChatDialog, customObjectRequestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
            @Override
            public void onSuccess(ArrayList<QBChatMessage> arrayList, Bundle bundle) {
                isPresent = arrayList.contains(qbChatMessage);
            }

            @Override
            public void onError(QBResponseException e) {
                isPresent = false;
            }
        });
        return isPresent;
    }

    ConnectionListener chatConnectionListener = new ConnectionListener() {
        @Override
        public void connected(XMPPConnection connection) {
            Log.i(TAG, "connected");
        }

        @Override
        public void authenticated(XMPPConnection connection, boolean authenticated) {
            Log.i(TAG, "authenticated");
        }

        @Override
        public void connectionClosed() {
            Log.i(TAG, "connectionClosed");
        }

        @Override
        public void connectionClosedOnError(final Exception e) {
            Log.i(TAG, "connectionClosedOnError: " + e.getLocalizedMessage());
        }

        @Override
        public void reconnectingIn(final int seconds) {
            if (seconds % 5 == 0) {
                Log.i(TAG, "reconnectingIn: " + seconds);
            }
        }

        @Override
        public void reconnectionSuccessful() {
            Log.i(TAG, "reconnectionSuccessful");
        }

        @Override
        public void reconnectionFailed(final Exception error) {
            Log.i(TAG, "reconnectionFailed: " + error.getLocalizedMessage());
        }
    };

}
