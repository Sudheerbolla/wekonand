package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.weekend.R;
import com.weekend.adapters.ReviewsAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.gcm.PushNotificationModel;
import com.weekend.models.PropertyReviewListModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
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
public class ReviewsFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse>, Paginate.Callbacks {

    public static final String TAG = ReviewsFragment.class.getSimpleName();
    private static final String BUNDLE_PROPERTY_ID = "property_id";
    private static final String BUNDLE_PUSH_NOTIFICATION = "push_notification";
    @Bind(R.id.tv_total_reviews)
    CustomTextView tvTotalReviews;
    @Bind(R.id.v_separator)
    View vSeparator;
    @Bind(R.id.tv_rating)
    CustomTextView tvRating;
    @Bind(R.id.rl_reviews)
    RelativeLayout rlReviews;
    @Bind(R.id.tv_rate_property)
    CustomTextView tvRateProperty;
    @Bind(R.id.rv_reviews)
    CustomRecyclerView rvReviews;
    @Bind(R.id.tv_no_result)
    CustomTextView tvNoResult;
    private View rootView;
    private ReviewsAdapter reviewsAdapter;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private List<PropertyReviewListModel.PropertyReview> propertyReviewList;
    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1;
    private String propertyId;
    private PushNotificationModel pushNotificationModel;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param propertyid
     * @return A new instance of fragment ReviewsFragment.
     */
    public static ReviewsFragment newInstance(String propertyid) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PROPERTY_ID, propertyid);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ReviewsFragment newInstance(PushNotificationModel pushNotificationModel) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_PUSH_NOTIFICATION, pushNotificationModel);
        fragment.setArguments(bundle);
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
                rootView = inflater.inflate(R.layout.fragment_reviews, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.reviews), true, false, true, false, false, false, false,
                    false, false, false, false,true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initialize();
                }
            }, Constants.FRAGMENT_TRANSACTION_DELAY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        if (getArguments()!=null && getArguments().containsKey(BUNDLE_PUSH_NOTIFICATION)) {
            WeekendApplication.selectedFragment = TAG+ "-PushNotification";
        }else{
            WeekendApplication.selectedFragment = TAG;
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        propertyReviewList = new ArrayList<>();
        setAdapter();
        requestPropertyReviewListing(true);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            if (getArguments().containsKey(BUNDLE_PROPERTY_ID)) {
                propertyId = getArguments().getString(BUNDLE_PROPERTY_ID);
            }
            if (getArguments().containsKey(BUNDLE_PUSH_NOTIFICATION)) {
                pushNotificationModel = (PushNotificationModel) getArguments().getSerializable(BUNDLE_PUSH_NOTIFICATION);
                propertyId = pushNotificationModel.propertyId;
            }
        }
    }

    @Override
    @OnClick({R.id.tv_rate_property})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_rate_property:
                onRatePropertyEvent();
                break;
            default:
                break;
        }
    }

    private void onRatePropertyEvent() {
        if (!TextUtils.isEmpty(LocalStorage.getInstance(activity).getString(Constants.SP_KEY_USER_ID, ""))) {
            activity.replaceFragment(MyBookingsFragment.newInstance(), true, true, false, false);
        } else {
            activity.manageGuestUserAction(getString(R.string.please_login_to_rate_this_property));
        }
    }

    private void setAdapter() {
        reviewsAdapter = new ReviewsAdapter(activity, propertyReviewList);
        //rvReviews.setHasFixedSize(true);
        //LinearLayoutManager weekdayLayout = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvReviews.setLayoutManager(new LinearLayoutManager(activity));
        rvReviews.setAdapter(reviewsAdapter);
        rvReviews.setListPagination(this);
    }

    //Recycler view pagination
    @Override
    public void onLoadMore() {
        isLoading = true;
        requestPropertyReviewListing(false);
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

    private void requestPropertyReviewListing(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_ID, propertyId);
        params.put(WSUtils.KEY_PAGE_INDEX, pageIndex + "");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_PROPERTY_REVIEW);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_PROPERTY_REVIEW, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_PROPERTY_REVIEW:
                    parsePropertyReview((PropertyReviewListModel) response);
                    break;
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

    private void parsePropertyReview(PropertyReviewListModel response) {
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

                if (pageIndex == 1 && propertyReviewList != null) {
                    propertyReviewList.clear();
                }

                isLoading = false;

                if (response.getData() != null && response.getData().getPropertyReviews().size() > 0) {
                    propertyReviewList.addAll(response.getData().getPropertyReviews());
                    tvNoResult.setVisibility(View.GONE);
                    rvReviews.setVisibility(View.VISIBLE);
                } else {
                    tvNoResult.setVisibility(View.VISIBLE);
                    rvReviews.setVisibility(View.GONE);
                }
                if (response.getData().getPropertyAverageRating() != null && response.getData().getPropertyAverageRating().size() > 0) {
                    rlReviews.setVisibility(View.VISIBLE);
                    tvRating.setText(response.getData().getPropertyAverageRating().get(0).getAvgRating());
                    tvTotalReviews.setText(getString(R.string.reviews) + " " + response.getData().getPropertyAverageRating().get(0).getTotalReviews());
                }
            } else {
                tvNoResult.setVisibility(View.VISIBLE);
                rvReviews.setVisibility(View.GONE);
            }

            if (reviewsAdapter != null) {
                reviewsAdapter.notifyDataSetChanged();
            }
        }
    }
}
