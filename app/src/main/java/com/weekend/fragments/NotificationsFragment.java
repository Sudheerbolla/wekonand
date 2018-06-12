package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weekend.R;
import com.weekend.adapters.NotificationAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.interfaces.INotificationDeleteClick;
import com.weekend.models.CustomerNotificationDeleteAllModel;
import com.weekend.models.CustomerNotificationDeleteModel;
import com.weekend.models.CustomerNotificationListModel;
import com.weekend.models.PushNotificationChangeStatusModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PopupUtil;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.CustomTextView;
import com.weekend.views.Paginate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 19/7/16.
 */
public class NotificationsFragment extends BaseFragment implements View.OnClickListener, INotificationDeleteClick, Paginate.Callbacks, IParser<WSResponse> {

    public static final String TAG = NotificationsFragment.class.getSimpleName();
    @Bind(R.id.ll_notification)
    LinearLayout llNotification;
    @Bind(R.id.tv_no_result)
    CustomTextView tvNoResult;
    @Bind(R.id.iv_notification_on_off)
    ImageView ivNotificationOnOff;
    @Bind(R.id.rv_notifications)
    CustomRecyclerView rvNotifications;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private List<CustomerNotificationListModel.Datum> notificationList;
    private NotificationAdapter notificationAdapter;
    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1, deletePos;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationsFragment.
     */
    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_notifications, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.notifications), true, false, true, false, false, false, false, false,
                    false, false, false,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        application.selectedFragment = TAG;
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        llNotification.setVisibility(View.GONE);
        notificationList = new ArrayList<>();
        ivNotificationOnOff.setSelected(localStorage.getBoolean(Constants.SP_KEY_PUSH_NOTIFICATION, false));
        LinearLayoutManager mLinearLayoutManger = new LinearLayoutManager(activity);
        rvNotifications.setLayoutManager(mLinearLayoutManger);
        setAdapter();
        rvNotifications.setListPagination(this);
        requestForCustomerNotificationList(true);
    }

    private void getBundleData() {
        if (getArguments() != null) {
        }
    }

    @Override
    @OnClick({R.id.iv_notification_on_off, R.id.tv_clear_all})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_notification_on_off:
                onNotificationOnOffEvent();
                break;
            case R.id.tv_clear_all:
                onClearAllEvent();
                break;

            default:
                break;
        }
    }

    private void onNotificationOnOffEvent() {
        requestForPushNotificationChangeStatus(ivNotificationOnOff.isSelected() ? "0" : "1");
    }

    private void onClearAllEvent() {
        PopupUtil.showAlert(activity, getString(R.string.are_you_sure_you_want_to_clear_all_notifications), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForCustomerNotificationDeleteAll();
            }
        });
    }

    private void setAdapter() {
        notificationAdapter = new NotificationAdapter(notificationList, this);
        rvNotifications.setAdapter(notificationAdapter);
    }

    @Override
    public void onDeleteClick(int position) {
        deletePos = position;
        PopupUtil.showAlert(activity, getString(R.string.are_you_sure_you_want_to_delete_this_notifications), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestForCustomerNotificationDelete(notificationList.get(deletePos).getNotificationId());
            }
        });
    }

    //Recycler view pagination
    @Override
    public void onLoadMore() {
        isLoading = true;
        requestForCustomerNotificationList(false);
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

    private void requestForCustomerNotificationList(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_PAGE_INDEX, String.valueOf(pageIndex));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_NOTIFICATION_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_NOTIFICATION_LIST, this);
    }

    private void requestForCustomerNotificationDeleteAll() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_NOTIFICATION_DELETE_ALL);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_NOTIFICATION_DELETE_ALL, this);
    }

    private void requestForCustomerNotificationDelete(String notificationId) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_NOTIFICATION_ID, notificationId);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_NOTIFICATION_DELETE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_NOTIFICATION_DELETE, this);
    }

    private void requestForPushNotificationChangeStatus(String action) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_ACTION, action);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_PUSH_NOTIFICATION_CHANGE_STATUS);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_PUSH_NOTIFICATION_CHANGE_STATUS, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_CUSTOMER_NOTIFICATION_LIST:
                    parseCustomerNotificationList((CustomerNotificationListModel) response);
                    break;
                case WSUtils.REQ_CUSTOMER_NOTIFICATION_DELETE:
                    parseCustomerNotificationDelete((CustomerNotificationDeleteModel) response);
                    break;
                case WSUtils.REQ_CUSTOMER_NOTIFICATION_DELETE_ALL:
                    parseCustomerNotificationDeleteAll((CustomerNotificationDeleteAllModel) response);
                    break;
                case WSUtils.REQ_PUSH_NOTIFICATION_CHANGE_STATUS:
                    parsePushNotificationChangeStatus((PushNotificationChangeStatusModel) response);
                    break;
            }
        }
    }


    private void parsePushNotificationChangeStatus(PushNotificationChangeStatusModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                ivNotificationOnOff.setSelected(!ivNotificationOnOff.isSelected());
                localStorage.putBoolean(Constants.SP_KEY_PUSH_NOTIFICATION, ivNotificationOnOff.isSelected());
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void parseCustomerNotificationDeleteAll(CustomerNotificationDeleteAllModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                notificationList.clear();
                notificationAdapter.notifyDataSetChanged();
                if (notificationList.size() <= 0) {
                    llNotification.setVisibility(View.GONE);
                    tvNoResult.setVisibility(View.VISIBLE);
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void parseCustomerNotificationDelete(CustomerNotificationDeleteModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                notificationList.remove(notificationList.get(deletePos));
                notificationAdapter.notifyDataSetChanged();
                if (notificationList.size() <= 0) {
                    llNotification.setVisibility(View.GONE);
                    tvNoResult.setVisibility(View.VISIBLE);
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void parseCustomerNotificationList(CustomerNotificationListModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (response.getSettings().getNextPage().equalsIgnoreCase("1")) {
                    if (response.getData() != null) {
                        isNextPageAvailable = true;
                        pageIndex = pageIndex + 1;
                    }
                } else {
                    isNextPageAvailable = false;
                }

                if (pageIndex == 1 && notificationList != null) {
                    notificationList.clear();
                }

                isLoading = false;

                if (response.getData() != null && response.getData().size() > 0) {
                    notificationList.addAll(response.getData());
                    tvNoResult.setVisibility(View.GONE);
                    llNotification.setVisibility(View.VISIBLE);
                    rvNotifications.setVisibility(View.VISIBLE);
                } else {
                    tvNoResult.setVisibility(View.VISIBLE);
                    llNotification.setVisibility(View.GONE);
                    rvNotifications.setVisibility(View.GONE);
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                tvNoResult.setVisibility(View.VISIBLE);
                llNotification.setVisibility(View.GONE);
                rvNotifications.setVisibility(View.GONE);
            }

            if (notificationAdapter != null) {
                notificationAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void errorResponse(int requestCode, Throwable t) {
        activity.showHideProgress(false);
    }

    @Override
    public void noInternetConnection(int requestCode) {
        CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
        activity.showHideProgress(false);
    }
}
