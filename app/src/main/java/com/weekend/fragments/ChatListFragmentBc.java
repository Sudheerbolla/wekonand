/*
package com.weekend.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.sample.core.gcm.GooglePlayServicesHelper;
import com.quickblox.sample.core.utils.constant.GcmConsts;
import com.quickblox.users.model.QBUser;
import com.weekend.ChatActivity;
import com.weekend.R;
import com.weekend.adapters.ChatListAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IOnChatDialogClick;
import com.weekend.interfaces.QbChatDialogMessageListenerImp;
import com.weekend.qbutils.ChatServiceUtil;
import com.weekend.qbutils.DialogsManager;
import com.weekend.qbutils.QbDialogHolder;
import com.weekend.qbutils.QbEntityCallbackImpl;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.Log;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.Paginate;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class ChatListFragmentBc extends BaseFragment implements Paginate.Callbacks, IOnChatDialogClick,
        DialogsManager.ManagingDialogsCallbacks {
    public static final String TAG = ChatListFragmentBc.class.getSimpleName();
    public static final String EXTRA_DIALOG_ID = "dialogId";

    @Bind(R.id.rv_chat_list)
    CustomRecyclerView rvList;
    @Bind(R.id.tvNoResult)
    TextView tvNoResult;
    @Bind(R.id.fab_dialogs_new_chat)
    FloatingActionButton fab_dialogs_new_chat;
    @Bind(R.id.progress_bar_chat_list)
    ProgressBar progressBar;

    private View rootView;
    private LocalStorage localStorage;
    private DbHelper localDbHelper;
    private ChatListAdapter chatListAdapter;
    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1;
    private ArrayList<QBChatDialog> qbChatDialogs;

    //Chat implementation
    private ActionMode currentActionMode;
    private QBRequestGetBuilder requestBuilder;
    private Menu menu;
    private int skipRecords = 0;
    private boolean isProcessingResultInProgress;

    private BroadcastReceiver pushBroadcastReceiver;
    private GooglePlayServicesHelper googlePlayServicesHelper;
    private QBChatDialogMessageListener allDialogsMessagesListener;
    private SystemMessagesListener systemMessagesListener;
    private QBSystemMessagesManager systemMessagesManager;
    private QBIncomingMessagesManager incomingMessagesManager;
    private DialogsManager dialogsManager;
    private QBUser currentUser;
    private ChatActivity activity;

    public ChatListFragmentBc() {

    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListViewFragment.
     *//*

    public static ChatListFragmentBc newInstance() {
        ChatListFragmentBc fragment = new ChatListFragmentBc();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListViewFragment.
     *//*

    public static ChatListFragmentBc newInstance(Bundle bundle) {
        ChatListFragmentBc fragment = new ChatListFragmentBc();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ChatActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_chat_list, null);
            }
            ButterKnife.bind(this, rootView);
//            activity.setTopbar("Chats", true, false, false,
//                    false, false, false, true,
//                    false, false, false, false, true);
            initialize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initialize() {
        googlePlayServicesHelper = new GooglePlayServicesHelper();

        pushBroadcastReceiver = new PushBroadcastReceiver();

        allDialogsMessagesListener = new AllDialogsMessageListener();
        systemMessagesListener = new SystemMessagesListener();

        dialogsManager = new DialogsManager();
        currentUser = WeekendApplication.getInstance().loggedInQBUser;
        requestBuilder = new QBRequestGetBuilder();

        localStorage = LocalStorage.getInstance(activity);
        localDbHelper = new DbHelper(activity);
        WeekendApplication.selectedFragment = TAG;

//        activity.updateUI(!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, "")));
        qbChatDialogs = new ArrayList<>();
        rvList.setLayoutManager(new LinearLayoutManager(activity));
        setTheAdapter();
        getChaletLists();
        registerQbChatListeners();
        if (QbDialogHolder.getInstance().getDialogs().size() > 0) {
            loadDialogsFromQb(true, true);
        } else {
            loadDialogsFromQb(false, true);
        }
    }

    private static final int REQUEST_SELECT_PEOPLE = 174;
    private static final int REQUEST_DIALOG_ID_FOR_UPDATE = 165;

    @Override
    public void onResume() {
        super.onResume();
        googlePlayServicesHelper.checkPlayServicesAvailable(activity);

        LocalBroadcastManager.getInstance(activity).registerReceiver(pushBroadcastReceiver,
                new IntentFilter(GcmConsts.ACTION_NEW_GCM_EVENT));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(pushBroadcastReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterQbChatListeners();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            isProcessingResultInProgress = true;
            if (requestCode == REQUEST_DIALOG_ID_FOR_UPDATE) {
                if (data != null) {
                    String dialogId = data.getStringExtra(ChatListFragmentBc.EXTRA_DIALOG_ID);
                    loadUpdatedDialog(dialogId);
                } else {
                    isProcessingResultInProgress = false;
                    updateDialogsList();
                }
            }
        } else {
            updateDialogsAdapter();
        }
    }

    private boolean isPrivateDialogExist(ArrayList<QBUser> allSelectedUsers) {
        ArrayList<QBUser> selectedUsers = new ArrayList<>();
        selectedUsers.addAll(allSelectedUsers);
        selectedUsers.remove(WeekendApplication.getInstance().loggedInQBUser);
        return selectedUsers.size() == 1 && QbDialogHolder.getInstance().hasPrivateDialogWithUser(selectedUsers.get(0));
    }

    private void loadUpdatedDialog(String dialogId) {
        ChatServiceUtil.getInstance().getDialogById(dialogId, new QbEntityCallbackImpl<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog result, Bundle bundle) {
                isProcessingResultInProgress = false;
                QbDialogHolder.getInstance().addDialog(result);
                updateDialogsAdapter();
            }

            @Override
            public void onError(QBResponseException e) {
                isProcessingResultInProgress = false;
            }
        });
    }

    private void updateDialogsList() {
        requestBuilder.setSkip(skipRecords = 0);
        loadDialogsFromQb(true, true);
    }

    private void registerQbChatListeners() {
        incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        systemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();

        if (incomingMessagesManager != null) {
            incomingMessagesManager.addDialogMessageListener(allDialogsMessagesListener != null
                    ? allDialogsMessagesListener : new AllDialogsMessageListener());
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.addSystemMessageListener(systemMessagesListener != null
                    ? systemMessagesListener : new SystemMessagesListener());
        }

        dialogsManager.addManagingDialogsCallbackListener(this);
    }

    private void unregisterQbChatListeners() {
        if (incomingMessagesManager != null) {
            incomingMessagesManager.removeDialogMessageListrener(allDialogsMessagesListener);
        }

        if (systemMessagesManager != null) {
            systemMessagesManager.removeSystemMessageListener(systemMessagesListener);
        }

        dialogsManager.removeManagingDialogsCallbackListener(this);
    }

    private void loadDialogsFromQb(final boolean silentUpdate, final boolean clearDialogHolder) {
        isProcessingResultInProgress = true;
        if (!silentUpdate) {
            progressBar.setVisibility(View.VISIBLE);
        }

        try {
            ChatServiceUtil.getInstance().getDialogs(new QBEntityCallback<ArrayList<QBChatDialog>>() {
                @Override
                public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                    isProcessingResultInProgress = false;
                    progressBar.setVisibility(View.GONE);

                    if (clearDialogHolder) {
                        QbDialogHolder.getInstance().clear();
                    }
                    QbDialogHolder.getInstance().addDialogs(dialogs);
                    updateDialogsAdapter();
                }

                @Override
                public void onError(QBResponseException e) {
                    isProcessingResultInProgress = false;
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
    }

    private void updateDialogsAdapter() {
//        chatListAdapter.updateList(new ArrayList<>(QbDialogHolder.getInstance().getDialogs().values()));
    }

    @Override
    public void onDialogCreated(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onDialogUpdated(String chatDialog) {
        updateDialogsAdapter();
    }

    @Override
    public void onNewDialogLoaded(QBChatDialog chatDialog) {
        updateDialogsAdapter();
    }

    private class PushBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra(GcmConsts.EXTRA_GCM_MESSAGE);
            android.util.Log.v(TAG, "Received broadcast " + intent.getAction() + " with data: " + message);
            requestBuilder.setSkip(skipRecords = 0);
            loadDialogsFromQb(true, true);
        }
    }

    private class SystemMessagesListener implements QBSystemMessageListener {
        @Override
        public void processMessage(final QBChatMessage qbChatMessage) {
            dialogsManager.onSystemMessageReceived(qbChatMessage);
        }

        @Override
        public void processError(QBChatException e, QBChatMessage qbChatMessage) {

        }
    }

    private class AllDialogsMessageListener extends QbChatDialogMessageListenerImp {
        @Override
        public void processMessage(final String dialogId, final QBChatMessage qbChatMessage, Integer senderId) {
            if (!senderId.equals(WeekendApplication.getInstance().loggedInQBUser.getId())) {
                dialogsManager.onGlobalMessageReceived(dialogId, qbChatMessage);
            }
        }
    }


    private void getChaletLists() {
        qbChatDialogs.addAll(localDbHelper.getChatDialogs());
        if (qbChatDialogs != null && qbChatDialogs.size() > 0) {
//            chatListAdapter.addAll(qbChatDialogs);
            rvList.scrollToPosition(0);
            tvNoResult.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);
        } else {
            tvNoResult.setText("No Chats found.");
            pageIndex = 1;
            rvList.setVisibility(View.GONE);
        }

    }

    private void setTheAdapter() {
        if (qbChatDialogs == null) {
            qbChatDialogs = new ArrayList<>();
        }
        chatListAdapter = new ChatListAdapter(activity, qbChatDialogs, (IOnChatDialogClick) this);
        rvList.setAdapter(chatListAdapter);
    }

    //Recycler view pagination
    @Override
    public void onLoadMore() {
        isLoading = true;
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        if (isNextPageAvailable) {
            return false;
        }
        */
/*if next page not available means we loaded all items so return true *//*

        return true;
    }

    @Override
    public void onItemClick(int position, QBChatDialog qbChatDialog) {
        Toast.makeText(getActivity(), "Position:" + position + " Chat:" + qbChatDialog.getName(), Toast.LENGTH_LONG).show();
        activity.replaceFragment(ChatConversationFragment.newInstance(qbChatDialog, false), true, true, false);
    }

    @Override
    public void onItemLongPres(int position, QBChatDialog qbChatDialog) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fab_dialogs_new_chat)
    public void onViewClicked() {
        startNewHelpChat();
    }

    private void startNewHelpChat() {
        progressBar.setVisibility(View.VISIBLE);
        ChatServiceUtil.getInstance().getAdminUsers(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                createDialog(qbUsers, true);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void createDialog(final ArrayList<QBUser> selectedUsers, final boolean showAdmins) {
        ChatServiceUtil.getInstance().createDialogWithSelectedUsers(selectedUsers,
                new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        progressBar.setVisibility(View.GONE);
                        activity.replaceFragment(ChatConversationFragment.newInstance(dialog, showAdmins), true,
                                true, false);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );
    }
}

*/
