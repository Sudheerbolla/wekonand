package com.weekend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.model.QBUser;
import com.weekend.base.WeekendApplication;
import com.weekend.fragments.ChatFragment;
import com.weekend.fragments.ChatListFragment;
import com.weekend.models.AdminUsersModel;
import com.weekend.qbutils.ChatServiceUtil;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.tv_title)
    CustomTextView tvTitle;
    @Bind(R.id.progress_chat)
    ProgressBar progressBar;
    private String bookingChatMessage;
    private DbHelper localDbHelper;
    private String bubbleDialogId = "";
    private boolean showChatsList, isFromNotification, isFromBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        getBundleData();
        initComponents();
        localDbHelper = new DbHelper(this);
    }

    public DbHelper getDBHelper() {
        if (localDbHelper == null)
            localDbHelper = new DbHelper(this);
        return localDbHelper;
    }

    private void getBundleData() {
        if (getIntent().hasExtra("booking_details"))
            bookingChatMessage = getIntent().getStringExtra("booking_details");
        if (getIntent().hasExtra("showChatsList"))
            showChatsList = getIntent().getBooleanExtra("showChatsList", false);
        if (getIntent().hasExtra("isFromNotification"))
            isFromNotification = getIntent().getBooleanExtra("isFromNotification", false);
        if (getIntent().hasExtra("isFromBubble"))
            isFromBubble = getIntent().getBooleanExtra("isFromBubble", false);
        if (getIntent().hasExtra("dialogId"))
            bubbleDialogId = getIntent().getStringExtra("dialogId");
    }

    private void initComponents() {
        getAdminUsersList();
        if (isFromBubble && !TextUtils.isEmpty(bubbleDialogId)) {
            progressBar.setVisibility(View.GONE);
            replaceFragment(ChatFragment.newInstance(getDBHelper().getChatDialog(bubbleDialogId), false, ""), false);
            return;
        }
        if (StaticUtil.isCurrentUserADmin() || showChatsList || isFromNotification) {
            progressBar.setVisibility(View.GONE);
            replaceFragment(ChatListFragment.newInstance(), false);
        } else
            startNewHelpChat();
    }

    private void getAdminUsersList() {
        if (WeekendApplication.adminUsersModelArrayList.isEmpty()) {
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

                }
            });
        }
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
                new DbHelper(ChatActivity.this).addChatDialog(dialog);
                progressBar.setVisibility(View.GONE);
                StaticUtil.sendSystemMessageAboutCreatingDialog(QBChatService.getInstance().getSystemMessagesManager(), dialog);
                replaceFragment(ChatFragment.newInstance(dialog, true, bookingChatMessage), false);
            }

            @Override
            public void onError(QBResponseException e) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick({R.id.iv_navbar_back, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_navbar_back:
                StaticUtil.hideSoftKeyboard(this);
                onBackPressed();
                break;
            case R.id.iv_close:
                StaticUtil.hideSoftKeyboard(this);
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.chatContainer);
        if (fragment instanceof ChatFragment) {
            ChatFragment chatFragment = (ChatFragment) fragment;
            QBChatDialog qbChatDialog = chatFragment.getCurrentDialog();
            if (qbChatDialog != null) {
                deleteThisDialog(qbChatDialog);
            }
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                super.onBackPressed();
            } else {
                replaceFragment(ChatListFragment.newInstance(), false);
            }
        } else
            super.onBackPressed();
    }

    private void deleteThisDialog(final QBChatDialog dialog) {
        ChatServiceUtil.getInstance().deleteDialog(dialog, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                StaticUtil.sendSystemMessageAboutDeletingDialog(QBChatService.getInstance().getSystemMessagesManager(), dialog);
                new DbHelper(ChatActivity.this).deleteChatDialog(dialog.getDialogId());
                new DbHelper(ChatActivity.this).deleteChatHistory(dialog.getDialogId());
                sendLocalBroadcastt();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void sendLocalBroadcastt() {
        Intent broadcastIntent = new Intent(StaticUtil.DIALOG_DELETED);
        broadcastIntent.putExtra("is_local_notification", true);
        sendBroadcast(broadcastIntent);
    }

    public void setTopBarTitle(String heading) {
        tvTitle.setText(heading);
    }

    public void replaceFragment(final Fragment fragment, final boolean needToAddBackStack) {
        StaticUtil.hideSoftKeyboard(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chatContainer, fragment);

        if (needToAddBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ChatListFragment.getInstance().onActivityResult(requestCode, resultCode, data);
    }

}
