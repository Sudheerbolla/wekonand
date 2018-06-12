package com.weekend.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.weekend.ChatActivity;
import com.weekend.R;
import com.weekend.adapters.ChatListAdapter;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IOnChatDialogClick;
import com.weekend.models.AdminUsersModel;
import com.weekend.qbutils.ChatServiceUtil;
import com.weekend.utils.DividerItemDecoration;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.Paginate;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatListFragment extends Fragment implements Paginate.Callbacks, IOnChatDialogClick {

    public static final String TAG = ChatListFragment.class.getSimpleName();
    public static final String EXTRA_DIALOG_ID = "dialogId";
    private static ChatListFragment instance;

    @Bind(R.id.rv_chat_list)
    CustomRecyclerView rvList;
    @Bind(R.id.tvNoResult)
    TextView tvNoResult;
    @Bind(R.id.fab_dialogs_new_chat)
    FloatingActionButton fab_dialogs_new_chat;
    @Bind(R.id.progress_bar_chat_list)
    ProgressBar progressBar;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private View rootView;
    private ChatListAdapter chatListAdapter;

    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1;
    private ArrayList<QBChatDialog> qbChatDialogArrayList;
    private ArrayList<QBChatDialog> dbList;
    //Chat implementation
    private QBUser currentUser;
    private ChatActivity chatActivity;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            android.util.Log.e("ChatListFragment", "broadcastReceiver onReceive");
            if (intent.getAction().equalsIgnoreCase(StaticUtil.CHAT_BROADCAST) || intent.getAction().equalsIgnoreCase(StaticUtil.DIALOG_DELETED) ||
                    intent.getAction().equalsIgnoreCase(StaticUtil.DIALOG_CREATED) || intent.getAction().equalsIgnoreCase(StaticUtil.DIALOG_BROADCAST)) {
                getChatsListFromDB();
            } else if (intent.getAction().equalsIgnoreCase(StaticUtil.NEWMESSAGE_RECEIVE) ||
                    intent.getAction().equalsIgnoreCase(StaticUtil.DIALOG_UPDATE)) {
                updateParticularItem(intent.getStringExtra("dialog_id"));
            }
        }
    };

    public ChatListFragment() {

    }

    public static ChatListFragment newInstance() {
        ChatListFragment fragment = new ChatListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ChatListFragment getInstance() {
        return instance;
    }

    public static ChatListFragment newInstance(Bundle bundle) {
        ChatListFragment fragment = new ChatListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatActivity = (ChatActivity) getActivity();
        chatActivity.setTopBarTitle("Weekend Customer Support");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_chat_list, null);
                instance = this;
            }
            ButterKnife.bind(this, rootView);
            initialize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initialize() {
        progressBar.setVisibility(View.VISIBLE);
        currentUser = WeekendApplication.loggedInQBUser;
        WeekendApplication.selectedFragment = TAG;

        qbChatDialogArrayList = new ArrayList<>();
        dbList = new ArrayList<>();

        setChatsListAdapter();
        getChatsListFromDB();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fab_dialogs_new_chat != null)
                    fab_dialogs_new_chat.setVisibility(StaticUtil.isCurrentUserADmin() ? View.GONE : View.VISIBLE);
            }
        }, 3000);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getChatsListFromDB();
                if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        chatActivity = (ChatActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        android.util.Log.e("ChatListFragment", "register");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StaticUtil.NEWMESSAGE_RECEIVE);
        intentFilter.addAction(StaticUtil.DIALOG_BROADCAST);
        intentFilter.addAction(StaticUtil.CHAT_BROADCAST);
        intentFilter.addAction(StaticUtil.DIALOG_DELETED);
        intentFilter.addAction(StaticUtil.DIALOG_UPDATE);
        intentFilter.addAction(StaticUtil.DIALOG_CREATED);
        chatActivity.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        android.util.Log.e("ChatListFragment", "unregister");
        chatActivity.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getChatsListFromDB();
        chatActivity.setTopBarTitle("Weekend Customer Support");
    }

    private void updateParticularItem(String dialogID) {
//                if (intent.hasExtra("chatmessage") && intent.getSerializableExtra("chatmessage") != null) {
//                    QBChatMessage incomingChatMessage = (QBChatMessage) intent.getSerializableExtra("chatmessage");
//                    updateParticularItem(dialogID);
//                } else {
//                    getChatsListFromDB();
//                }
        int positionToUpdate = getPositionInList(dialogID);
        if (positionToUpdate != -1) {
            QBChatDialog qbChatDialog = chatActivity.getDBHelper().getChatDialog(dialogID);
            if (qbChatDialog != null) qbChatDialogArrayList.set(positionToUpdate, qbChatDialog);
            if (positionToUpdate == 0) {
                chatListAdapter.notifyItemChanged(0);
            } else {
                qbChatDialogArrayList.remove(positionToUpdate);
                qbChatDialogArrayList.add(0, qbChatDialog);
//                getChatsListFromDB();
                chatListAdapter.notifyDataSetChanged();
            }
        } else {
            getChatsListFromDB();
        }
    }

    private int getPositionInList(String dialogID) {
        for (int i = 0; i < qbChatDialogArrayList.size(); i++) {
            if (qbChatDialogArrayList.get(i).getDialogId().equalsIgnoreCase(dialogID)) {
                return i;
            }
        }
        return -1;
    }

    private void getChatsListFromDB() {
        dbList.clear();
        qbChatDialogArrayList.clear();
        dbList.addAll(chatActivity.getDBHelper().getChatDialogs());
        if (dbList.size() > 0) {
            qbChatDialogArrayList.addAll(dbList);
            rvList.scrollToPosition(0);
            rvList.setVisibility(View.VISIBLE);
            tvNoResult.setVisibility(View.GONE);
        } else {
            rvList.setVisibility(View.GONE);
            pageIndex = 1;
            tvNoResult.setVisibility(View.VISIBLE);
        }
        if (chatListAdapter != null)
            chatListAdapter.notifyDataSetChanged();
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    private void setChatsListAdapter() {
        rvList.setLayoutManager(new LinearLayoutManager(chatActivity));
        rvList.addItemDecoration(new DividerItemDecoration(chatActivity, DividerItemDecoration.VERTICAL_LIST));
        chatListAdapter = new ChatListAdapter(chatActivity, qbChatDialogArrayList, this);
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
        /*if next page not available means we loaded all items so return true */
        return true;
    }

    @Override
    public void onItemClick(int position, QBChatDialog qbChatDialog) {
        chatActivity.replaceFragment(ChatFragment.newInstance(qbChatDialog, false, ""), true);
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
        if (WeekendApplication.adminUsersModelArrayList.isEmpty()) {
            ChatServiceUtil.getInstance().getAdminUsers(new QBEntityCallback<ArrayList<QBUser>>() {
                @Override
                public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                    if (!qbUsers.isEmpty()) {
                        for (int i = 0; i < qbUsers.size(); i++) {
                            WeekendApplication.adminUsersModelArrayList.add(new AdminUsersModel(qbUsers.get(i)));
                        }
                        createDialog();
                    }
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });
        } else createDialog();
    }

    private void createDialog() {
        ChatServiceUtil.getInstance().createDialogWithSelectedUsers(WeekendApplication.adminUsersModelArrayList, new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog dialog, Bundle args) {
                        chatActivity.getDBHelper().addChatDialog(dialog);
                        progressBar.setVisibility(View.GONE);
                        StaticUtil.sendSystemMessageAboutCreatingDialog(QBChatService.getInstance().getSystemMessagesManager(), dialog);
                        chatActivity.replaceFragment(ChatFragment.newInstance(dialog, true, ""), true);
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
        );
    }

}

