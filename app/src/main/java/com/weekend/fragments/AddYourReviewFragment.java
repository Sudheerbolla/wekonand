package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.models.CustomerOrderRatingModel;
import com.weekend.models.MyBookingsModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 19/7/16.
 */
public class AddYourReviewFragment extends BaseFragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener, IParser<WSResponse> {

    public static final String TAG = AddYourReviewFragment.class.getSimpleName();
    private static final String BUNDLE_MY_BOOKING_ITEM = "my_booking_item";
    @Bind(R.id.rb_review)
    RatingBar rbReview;
    @Bind(R.id.edt_enter_review)
    CustomEditText edtEnterReview;
    @Bind(R.id.tv_submit_review)
    CustomTextView tvSubmitReview;
    private View rootView;
    private int rating;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private String orderId, propertyId;
    private MyBookingsModel.Datum myBookingItem;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddYourReviewFragment.
     */
    public static AddYourReviewFragment newInstance(MyBookingsModel.Datum myBookingItem) {
        AddYourReviewFragment fragment = new AddYourReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_MY_BOOKING_ITEM, myBookingItem);
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
                rootView = inflater.inflate(R.layout.fragment_add_your_review, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.add_your_review), true, false, true, false, false, false, false, false,
                    false, false, false, true);
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
        super.onResume();
        WeekendApplication.selectedFragment = TAG;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        rbReview.setOnRatingBarChangeListener(this);
        setOrderRatingData();
    }

    private void getBundleData() {
        if (getArguments() != null) {
            myBookingItem = (MyBookingsModel.Datum) getArguments().getSerializable(BUNDLE_MY_BOOKING_ITEM);
            orderId = myBookingItem.getOrderId();
            propertyId = myBookingItem.getPropertyId();
        }
    }

    private void setOrderRatingData() {
        if (!TextUtils.isEmpty(myBookingItem.getReviewComment())) {
            edtEnterReview.setText(myBookingItem.getReviewComment());
        }
        if (!TextUtils.isEmpty(myBookingItem.getRating())) {
            rbReview.setRating(Float.valueOf(myBookingItem.getRating()));
        }
        if (myBookingItem.getAllowRating().equalsIgnoreCase("No")) {
            rbReview.setEnabled(false);
            edtEnterReview.setEnabled(false);
        }
    }

    @Override
    @OnClick({R.id.tv_submit_review})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit_review:
                if (myBookingItem.getAllowRating().equalsIgnoreCase("Yes")) {
                    onSubmitReviewEvent();
                } else {
                    CommonUtil.showSnackbar(rootView, getString(R.string.your_review_is_successfully_accepted_by_owner_you_can_not_update_this_review));
                    activity.popBack(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        this.rating = Math.round(rating);
    }

    private void onSubmitReviewEvent() {
        StaticUtil.hideSoftKeyboard(activity);
        if (TextUtils.isEmpty(edtEnterReview.getText().toString())) {
            CommonUtil.showSnackbar(rootView, getString(R.string.please_enter_your_review));
            edtEnterReview.requestFocus();
        } else if (rating <= 0) {
            CommonUtil.showSnackbar(rootView, getString(R.string.please_select_rating));
        } else {
            requestOrderRating(true, String.valueOf(rating), edtEnterReview.getText().toString());
        }
    }


    private void requestOrderRating(boolean showDefaultProgress, String rating, String review) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTY_ID, propertyId);
        params.put(WSUtils.KEY_RATING, rating);
        params.put(WSUtils.KEY_ORDER_ID, orderId);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_REVIEW, review);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_ORDER_RATING);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_ORDER_RATING, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_CUSTOMER_ORDER_RATING:
                    parseOrderRating((CustomerOrderRatingModel) response);
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

    private void parseOrderRating(CustomerOrderRatingModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                myBookingItem.setRating(String.valueOf(rating));
                myBookingItem.setReviewComment(edtEnterReview.getText().toString().trim());
                activity.onBackPressed();
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }
}
