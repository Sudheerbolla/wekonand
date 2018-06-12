package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.gcm.PushNotificationModel;
import com.weekend.interfaces.ICancelBookingClick;
import com.weekend.models.CustomerBookingsDetailsModel;
import com.weekend.models.CustomerCancelBookingModel;
import com.weekend.models.MyBookingsModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.DateUtil;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomTextView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 20/7/16.
 */
public class SoccerFieldBookingDetailsFragment extends BaseFragment implements View.OnClickListener, ICancelBookingClick, IParser<WSResponse> {
    public static final String TAG = SoccerFieldBookingDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_BOOKING_ITEM = "booking_item";
    private static final String BUNDLE_PUSH_NOTIFICATION = "push_notification";
    @Bind(R.id.tv_booking_id)
    CustomTextView tvBookingId;
    @Bind(R.id.tv_status)
    CustomTextView tvStatus;
    @Bind(R.id.tv_booked_on)
    CustomTextView tvBookedOn;
    @Bind(R.id.tv_booked_for)
    CustomTextView tvBookedFor;
    @Bind(R.id.tv_type)
    CustomTextView tvType;
    @Bind(R.id.ll_type)
    LinearLayout llType;
    @Bind(R.id.tv_date)
    CustomTextView tvDate;
    @Bind(R.id.tv_time)
    CustomTextView tvTime;
    @Bind(R.id.tv_title)
    CustomTextView tvTitle;
    @Bind(R.id.tv_soccer_field_name)
    CustomTextView tvSoccerFieldName;
    @Bind(R.id.tv_address)
    CustomTextView tvAddress;
    @Bind(R.id.tv_owner_name)
    CustomTextView tvOwnerName;
    @Bind(R.id.tv_owner_contact_number)
    CustomTextView tvOwnerContactNumber;
    @Bind(R.id.tv_owner_email)
    CustomTextView tvOwnerEmail;
    @Bind(R.id.tv_name)
    CustomTextView tvName;
    @Bind(R.id.tv_national_id)
    CustomTextView tvNationalId;
    @Bind(R.id.tv_phone_number)
    CustomTextView tvPhoneNumber;
    @Bind(R.id.tv_email_address)
    CustomTextView tvEmailAddress;
    @Bind(R.id.tv_desc_for_booking)
    CustomTextView tvDescForBooking;
    @Bind(R.id.tv_total_price)
    CustomTextView tvTotalPrice;
    @Bind(R.id.tv_total_price_day)
    CustomTextView tvTotalPriceDay;
    @Bind(R.id.tv_down_payment_price)
    CustomTextView tvDownPaymentPrice;
    @Bind(R.id.tv_service_charge_price)
    CustomTextView tvServiceChargePrice;
    @Bind(R.id.tv_amount_paid)
    CustomTextView tvAmountPaid;
    @Bind(R.id.tv_remaining_amount_to_pay_price)
    CustomTextView tvRemainingAmountToPayPrice;
    @Bind(R.id.tv_cancellation_type)
    CustomTextView tvCancellationType;
    @Bind(R.id.tv_cancellation_description)
    CustomTextView tvCancellationDescription;
    @Bind(R.id.tv_refund_policy_description)
    CustomTextView tvRefundPolicyDescription;
    @Bind(R.id.tv_cancellation_booking_hint)
    CustomTextView tvCancellationBookingHint;
    @Bind(R.id.tv_cancel_booking)
    CustomTextView tvCancelBooking;
    @Bind(R.id.tv_canceled_on)
    CustomTextView tvCanceledOn;
    @Bind(R.id.ll_cancellation)
    LinearLayout llCancellation;
    @Bind(R.id.tv_cancellation_reason)
    CustomTextView tvCancellationReason;
    @Bind(R.id.tv_discount_price)
    CustomTextView tvDiscountPrice;
    @Bind(R.id.tv_discount)
    CustomTextView tvDiscount;
    @Bind(R.id.rl_discount)
    RelativeLayout rlDiscount;
    @Bind(R.id.fl_blank)
    FrameLayout flBlank;
    @Bind(R.id.tv_terms_conditions)
    CustomTextView tvTermsConditions;
    @Bind(R.id.tv_insurance_amount_price)
    CustomTextView tvInsuranceAmountPrice;

    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private List<CustomerBookingsDetailsModel.Datum> bookingsDetailsModel;
    private String cancelReason;
    private MyBookingsModel.Datum myBookingItem;
    private String token, orderId, userID;
    private PushNotificationModel pushNotificationModel;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChaletBookingDetailsFragment.
     */
    public static SoccerFieldBookingDetailsFragment newInstance(MyBookingsModel.Datum booking_item) {
        SoccerFieldBookingDetailsFragment fragment = new SoccerFieldBookingDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_BOOKING_ITEM, booking_item);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SoccerFieldBookingDetailsFragment newInstance(PushNotificationModel notificationBundle) {
        SoccerFieldBookingDetailsFragment fragment = new SoccerFieldBookingDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_PUSH_NOTIFICATION, notificationBundle);
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
                rootView = inflater.inflate(R.layout.fragment_soccer_field_booking_details, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
//            activity.setTopbar(getString(R.string.booking_details), true, false, true, true, true, false, false, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myBookingItem != null && myBookingItem.getAllowRating().equalsIgnoreCase("Yes")) {
            WeekendApplication.selectedFragment = TAG;
            activity.setTopbar(getString(R.string.booking_details), true, false, true, true, true, false, false, false, false,
                    false, false, true);
        } else {
            WeekendApplication.selectedFragment = TAG + "-PushNotification";
            activity.setTopbar(getString(R.string.booking_details), true, false, true, true, false, false, false, false,
                    false, false, false, true);
        }
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        requestCustomerBookingDetails(true);
        setListeners();
    }

    private void setListeners() {
        activity.ivNavbarShare.setOnClickListener(this);
        activity.ivNavbarRating.setOnClickListener(this);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            if (getArguments().containsKey(BUNDLE_BOOKING_ITEM)) {
                myBookingItem = (MyBookingsModel.Datum) getArguments().getSerializable(BUNDLE_BOOKING_ITEM);
                orderId = myBookingItem.getOrderId();
            }
            if (getArguments().containsKey(BUNDLE_PUSH_NOTIFICATION)) {
                pushNotificationModel = (PushNotificationModel) getArguments().getSerializable(BUNDLE_PUSH_NOTIFICATION);
                orderId = pushNotificationModel.orderId;
            }
        }
    }

    @Override
    @OnClick({R.id.tv_cancel_booking, R.id.tv_cancellation_reason, R.id.tv_terms_conditions})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel_booking:
                onCancelBookingEvent();
                break;
            case R.id.tv_cancellation_reason:
                PopupUtil.showRefundPolicy(activity, getString(R.string.cancellation_reason), "", "", cancelReason, "", false, null);
                break;
            case R.id.iv_navbar_rating:
                activity.replaceFragment(AddYourReviewFragment.newInstance(myBookingItem), true, true, false, false);
                break;
            case R.id.iv_navbar_share:
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                //String shareText = getString(R.string.soccer_field_name)+": " + (myBookingItem.getPropertyTitle() + "").trim();
                String shareText = getString(R.string.thank_you_for_using_the_weekend_app) + "\n"
                        + getString(R.string.name) + ": " + (myBookingItem.getPropertyTitle() + "").trim();
                if (!TextUtils.isEmpty(myBookingItem.getFullAddress())) {
                    shareText += "\n" + getString(R.string.address) + ": " + myBookingItem.getFullAddress().trim();
                } *//*else if (!TextUtils.isEmpty(myBookingItem.getAddress())) {
                    shareText += "\n" + getString(R.string.address) + ": " + myBookingItem.getAddress().trim();
                } else {
                    shareText += "\n" + getString(R.string.address) + ": " + myBookingItem.getCity().trim();
                }*//*
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));*/

                share();
                break;
            case R.id.tv_terms_conditions:
                PopupUtil.showStaticPage(activity, getString(R.string.terms_conditions), bookingsDetailsModel.get(0).getTermsConditions());
                break;
            default:
                break;
        }
    }

    private void share() {
        String shareText = "<b>" + getString(R.string.thank_you_for_using_the_weekend_app) + "</b>&nbsp;" + "<br/>"
                + "<b>" + getString(R.string.name) + ": " + "</b>&nbsp;" + (myBookingItem.getPropertyTitle() + "").trim();
        if (!TextUtils.isEmpty(myBookingItem.getFullAddress())) {
            shareText += "<br/>" + "<b>" + getString(R.string.address) + ": " + "</b>&nbsp;" + myBookingItem.getFullAddress().trim();
        }
        StaticUtil.sharingIntent(activity, shareText);
    }

    private void onCancelBookingEvent() {
        PopupUtil.showBookingCancellation(activity, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_CUSTOMER_BOOKING_DETAIL:
                    parseCustomerBookingDetails((CustomerBookingsDetailsModel) response);
                    break;
                case WSUtils.REQ_CUSTOMER_CANCEL_BOOKING:
                    parseCustomerCancelBooking((CustomerCancelBookingModel) response);
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

    private void requestCustomerBookingDetails(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }

        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_ORDER_ID, orderId);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_BOOKING_DETAIL);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_BOOKING_DETAIL, this);
    }

    private void requestCustomerCancelBooking(boolean showDefaultProgress, String reason) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_ORDER_ID, orderId);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_REASON, reason);
        params.put(WSUtils.KEY_CANCEL_BY, WSUtils.Customer);
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_CANCEL_BOOKING);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_CANCEL_BOOKING, this);
    }


    private void parseCustomerBookingDetails(CustomerBookingsDetailsModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                flBlank.setVisibility(View.GONE);
                bookingsDetailsModel = response.getData();
                setBookingDetailData();
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void parseCustomerCancelBooking(CustomerCancelBookingModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                tvStatus.setText(R.string.cancelled);
                llCancellation.setVisibility(View.VISIBLE);
                tvCanceledOn.setText(DateUtil.getCurrentDate());
                tvCancellationReason.setVisibility(View.VISIBLE);
                tvCancelBooking.setVisibility(View.GONE);
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void setBookingDetailData() {
        CustomerBookingsDetailsModel.Datum bookingDetails = bookingsDetailsModel.get(0);
        if (bookingDetails != null) {
            tvBookingId.setText(" " + bookingDetails.getBookingId());
            tvStatus.setText(" " + bookingDetails.getBookingStatus());
            tvBookedOn.setText(" " + bookingDetails.getBookedDate());
            if (bookingDetails.getBookingStatusCheck().equalsIgnoreCase(WSUtils.Cancelled)) {
                llCancellation.setVisibility(View.VISIBLE);
                tvCanceledOn.setText(bookingDetails.getCancelDate());
                tvCancellationReason.setVisibility(View.VISIBLE);
            }
            if (bookingDetails.getCancelFlag().equalsIgnoreCase("Yes")) {
                tvCancelBooking.setVisibility(View.VISIBLE);
                tvCancellationBookingHint.setVisibility(View.GONE);
            } else {
                //"No"
                tvCancelBooking.setVisibility(View.GONE);
                if (TextUtils.isEmpty(bookingDetails.getCancelDate()) || bookingDetails.getCancelDate().equalsIgnoreCase("---")) {
                    tvCancellationBookingHint.setVisibility(View.VISIBLE);
                } else {
                    tvCancellationBookingHint.setVisibility(View.GONE);
                }
            }
            tvType.setText(" " + bookingDetails.getPropertyType());

            String time = bookingDetails.getBookingFromToTimes();
            if (!TextUtils.isEmpty(time)) {
                SpannableString sTime = new SpannableString("\u202D" + time);
                if (time.contains(getString(R.string.to_small))) {
                    sTime = new SpannableString(("\u202D" + time.toString().replace(getString(R.string.to_small), "-")).trim());
                } else if (time.contains("to")) {
                    sTime = new SpannableString(("\u202D" + time.toString().replace("to", "-")).trim());
                }
                tvTime.setText(sTime);
            }

            tvDate.setText(bookingDetails.getBookingDates());
            tvTitle.setText(bookingDetails.getPropertyTitle());
            tvSoccerFieldName.setText(bookingDetails.getChaletName());
            tvAddress.setText(CommonUtil.insertComma(bookingDetails.getCountryName(), bookingDetails.getCity(), bookingDetails.getNeighbourhood()));
            tvOwnerName.setText(" " + bookingDetails.getOwnerName());
            tvOwnerContactNumber.setText(" \u202D" + bookingDetails.getOwnerMobileNo());
            tvOwnerEmail.setText(" " + bookingDetails.getOwnerEmail());
            tvName.setText(bookingDetails.getBookingName());
            if (!TextUtils.isEmpty(bookingDetails.getNationalId())) {
                tvNationalId.setVisibility(View.VISIBLE);
                tvNationalId.setText(bookingDetails.getNationalId());
            } else {
                tvNationalId.setVisibility(View.GONE);
            }
            tvPhoneNumber.setText("\u202D" + bookingDetails.getBookingPhone());
            tvEmailAddress.setText(bookingDetails.getBookingEmail());
            if (TextUtils.isEmpty(bookingDetails.getBookingComment())) {
                tvDescForBooking.setVisibility(View.GONE);
            } else {
                tvDescForBooking.setVisibility(View.VISIBLE);
                tvDescForBooking.setText(bookingDetails.getBookingComment());
            }
            tvTotalPriceDay.setText(getString(R.string.total_price) + " (" + bookingDetails.getBookingDay() + ")");
            tvTotalPrice.setText(bookingDetails.getTotalPrice());
            tvDownPaymentPrice.setText(bookingDetails.getDownPayment());
            tvServiceChargePrice.setText(bookingDetails.getServiceCharge());
            if (!TextUtils.isEmpty(bookingDetails.getDiscountCode())) {
                rlDiscount.setVisibility(View.VISIBLE);
                tvDiscount.setText(getString(R.string.discount_code) + " (" + bookingDetails.getDiscountCode() + ")");
                tvDiscountPrice.setText(bookingDetails.getDiscountPrice());
            } else {
                rlDiscount.setVisibility(View.GONE);
            }
            tvAmountPaid.setText(bookingDetails.getAmountPaid());
            tvRemainingAmountToPayPrice.setText(bookingDetails.getRemainingAmountToOwner());
            tvCancellationType.setText(" " + bookingDetails.getCancellationPolicyType());
            String cancellationDescription = getString(R.string.cancellation_before) + " " + bookingDetails.getCancelBeforeTime() + " " + bookingDetails.getCancelBeforeType() + " " + getString(R.string.of_check_in_time);
            tvCancellationDescription.setText(cancellationDescription);
            if (!TextUtils.isEmpty(bookingDetails.getRefulndPolicy())) {
                tvRefundPolicyDescription.setText(bookingDetails.getRefulndPolicy());
                tvRefundPolicyDescription.setVisibility(View.VISIBLE);
            } else {
                tvRefundPolicyDescription.setVisibility(View.GONE);
            }
            cancelReason = bookingDetails.getCancelReason();
            tvInsuranceAmountPrice.setText(bookingDetails.getInsuranceAmount());
        }
    }

    @Override
    public void onCancelBooking(String reason) {
        cancelReason = reason;
        requestCustomerCancelBooking(true, reason);
    }
}
