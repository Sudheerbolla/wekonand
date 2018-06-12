package com.weekend.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.weekend.ChatActivity;
import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.calendar.DateObject;
import com.weekend.interfaces.WalletDataRefreshCallback;
import com.weekend.models.BookPropertyByCustomerModel;
import com.weekend.models.CustomerBookingConfirmationModel;
import com.weekend.models.DiscountCouponApplyModel;
import com.weekend.models.GetPropertyPriceModel;
import com.weekend.models.ManageSchedulesModel;
import com.weekend.models.PropertyDetailModel;
import com.weekend.payfort.IPaymentRequestCallBack;
import com.weekend.payfort.PayFortData;
import com.weekend.payfort.PayFortPayment;
import com.weekend.payfort.SadadPayFortPayment;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.DateUtil;
import com.weekend.utils.PhoneUtil;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Double.parseDouble;

/**
 * Created by Hymavathi.kadali on 20/7/16.
 */
public class ConfirmBookingFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse>, IPaymentRequestCallBack, WalletDataRefreshCallback {
    public static final String TAG = ConfirmBookingFragment.class.getSimpleName();
    private static final String BUNDLE_PROPERTY_TYPE_ID = "property_type_id";
    private static final String BUNDLE_PROPERTY_DETAILS = "property_details";
    private static final String BUNDLE_CHALET_DETAILS = "chalet_details";
    private static final String BUNDLE_BOOKING_NAME = "booking_name";
    private static final String BUNDLE_BOOKING_DATE = "booking_date";
    private static final String BUNDLE_BOOKING_SLOT = "booking_slot";
    @Bind(R.id.tv_booked_for)
    CustomTextView tvBookedFor;
    @Bind(R.id.tv_title)
    CustomTextView tvTitle;
    @Bind(R.id.tv_date)
    CustomTextView tvDate;
    @Bind(R.id.tv_name)
    CustomTextView tvName;
    @Bind(R.id.tv_address)
    CustomTextView tvAddress;
    @Bind(R.id.edt_booking_name)
    CustomEditText edtBookingName;
    @Bind(R.id.edt_booking_email_address)
    CustomEditText edtBookingEmailAddress;
    @Bind(R.id.edt_mobile_number)
    CustomEditText edtMobileNumber;
    @Bind(R.id.edt_national_id_number)
    CustomEditText edtNationalIdNumber;
    @Bind(R.id.edt_brief_note)
    CustomEditText edtBriefNote;
    @Bind(R.id.tv_total_price_day)
    CustomTextView tvTotalPriceDay;
    @Bind(R.id.tv_insurance_amount_price)
    CustomTextView tvInsuranceAmountPrice;
    @Bind(R.id.tv_total_price)
    CustomTextView tvTotalPrice;
    @Bind(R.id.tv_down_payment_price)
    CustomTextView tvDownPaymentPrice;
    @Bind(R.id.rl_service_charge)
    RelativeLayout rlServiceCharge;
    @Bind(R.id.tv_service_charge_price)
    CustomTextView tvServiceChargePrice;
    @Bind(R.id.tv_amount_to_pay_price)
    CustomTextView tvAmountToPayPrice;
    @Bind(R.id.tv_remaining_amount_to_pay_price)
    CustomTextView tvRemainingAmountToPayPrice;
    @Bind(R.id.edt_coupon_code)
    CustomEditText edtCouponCode;
    @Bind(R.id.tv_apply)
    CustomTextView tvApply;
    @Bind(R.id.tv_i_accept)
    CustomTextView tvIAccept;
    @Bind(R.id.tv_terms_conditions)
    CustomTextView tvTermsConditions;
    @Bind(R.id.tv_continue)
    CustomTextView tvContinue;
    @Bind(R.id.tv_cancel)
    CustomTextView tvCancel;
    @Bind(R.id.tv_time)
    CustomTextView tvTime;
    @Bind(R.id.tv_discount_price)
    CustomTextView tvDiscountPrice;
    @Bind(R.id.tv_discount)
    CustomTextView tvDiscount;
    @Bind(R.id.rl_discount)
    RelativeLayout rlDiscount;
    @Bind(R.id.tv_remove)
    CustomTextView tvRemove;
    @Bind(R.id.ll_cupon)
    LinearLayout llCupoun;
    @Bind(R.id.ll_apply)
    LinearLayout llApply;
    @Bind(R.id.rl_downpayment)
    RelativeLayout rlDownpayment;
    @Bind(R.id.rg_payment_options)
    RadioGroup rgPaymentOptions;
    @Bind(R.id.rb_visa_master)
    RadioButton rbVisaMaster;
    @Bind(R.id.rb_credits)
    RadioButton rbCredits;
    @Bind(R.id.rb_bookNowPayLater)
    RadioButton rbBookNowPayLeter;
    @Bind(R.id.sv_confirm_booking)
    ScrollView svConfirmBooking;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private String propertyTypeId, bookingName;
    private PropertyDetailModel.Data propertyDetail;
    private PropertyDetailModel.PropertyChaletDetail chaletDetails;
    private ManageSchedulesModel.SlotTime bookingSlotTime;
    private Date bookingChaletDate;
    private DateObject bookingSoccerFieldDate;
    private String bookingOrderId = "", termsAndConditions;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConfirmBookingFragment.
     */
    public static ConfirmBookingFragment newInstance(String propertyTypeId, PropertyDetailModel.Data propertyDetail, PropertyDetailModel.PropertyChaletDetail chaletDetails, String bookingName, Date bookingDate) {
        ConfirmBookingFragment fragment = new ConfirmBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PROPERTY_TYPE_ID, propertyTypeId);
        bundle.putSerializable(BUNDLE_PROPERTY_DETAILS, propertyDetail);
        bundle.putSerializable(BUNDLE_CHALET_DETAILS, chaletDetails);
        bundle.putString(BUNDLE_BOOKING_NAME, bookingName);
        bundle.putSerializable(BUNDLE_BOOKING_DATE, bookingDate);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ConfirmBookingFragment newInstance(String propertyTypeId, PropertyDetailModel.Data propertyDetail, PropertyDetailModel.PropertyChaletDetail chaletDetails, String bookingName, DateObject bookingDate, ManageSchedulesModel.SlotTime bookingSlot) {
        ConfirmBookingFragment fragment = new ConfirmBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PROPERTY_TYPE_ID, propertyTypeId);
        bundle.putSerializable(BUNDLE_PROPERTY_DETAILS, propertyDetail);
        bundle.putSerializable(BUNDLE_CHALET_DETAILS, chaletDetails);
        bundle.putString(BUNDLE_BOOKING_NAME, bookingName);
        bundle.putSerializable(BUNDLE_BOOKING_DATE, bookingDate);
        bundle.putSerializable(BUNDLE_BOOKING_SLOT, bookingSlot);
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
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_confirm_booking, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.confirm_booking), true, false, true, false, false, false, false,
                    false, false, false, false, true);
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
        if (propertyTypeId.equalsIgnoreCase("1")) {
            setChaletDetails();
        } else {
            setSoccerFieldDetails();
        }

        if (propertyDetail != null) {
            tvAddress.setText(CommonUtil.insertComma(propertyDetail.getPropertyDetails().get(0).getNeighbourhood(),
                    propertyDetail.getPropertyDetails().get(0).getCity(),
                    propertyDetail.getPropertyDetails().get(0).getCountry()));
            tvName.setText(chaletDetails.getChaletTitle().trim());
            tvInsuranceAmountPrice.setText(propertyDetail.getPropertyDetails().get(0).getInsuranceAmount() + "");
        }

        tvTitle.setText(propertyDetail.getPropertyDetails().get(0).getTitle());
        edtBookingName.setText(bookingName);
        edtBookingEmailAddress.setText(localStorage.getString(Constants.SP_KEY_EMAIL, ""));
        edtMobileNumber.setText(localStorage.getString(Constants.SP_KEY_MOBILE_NO, ""));

        requestGetPropertyPrice();

        edtBriefNote.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            setWalletData();
        }
    }

    private void setWalletData() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1 = new SpannableString(getString(R.string.balance) + " " + localStorage.getString(Constants.SP_KEY_CREDITS, "") + " " + String.format(getString(R.string.currency)));
        str1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.color_35_120_201)), getString(R.string.balance).length() + 1, str1.length(), 0);
        builder.append(str1);
        rbCredits.setText(builder);
    }

    private void setChaletDetails() {
        tvTime.setVisibility(View.GONE);
        tvDate.setText(DateUtil.getFormatDate("dd/MM/yyyy", bookingChaletDate));
    }

    private void setSoccerFieldDetails() {
        tvTime.setVisibility(View.VISIBLE);
        tvDate.setText(DateUtil.getFormatDate("dd/MM/yyyy", bookingSoccerFieldDate.date));
        tvTime.setVisibility(View.VISIBLE);
        tvTime.setText(DateUtil.convertToAppTime(bookingSlotTime.getSlotFromTime()) + " to " + DateUtil.convertToAppTime(bookingSlotTime.getSlotToTime()));
    }

    private void getBundleData() {
        if (getArguments() != null) {
            propertyTypeId = getArguments().getString(BUNDLE_PROPERTY_TYPE_ID);
            if (getArguments().containsKey(BUNDLE_PROPERTY_DETAILS)) {
                propertyDetail = (PropertyDetailModel.Data) getArguments().getSerializable(BUNDLE_PROPERTY_DETAILS);
            }
            if (getArguments().containsKey(BUNDLE_CHALET_DETAILS)) {
                chaletDetails = (PropertyDetailModel.PropertyChaletDetail) getArguments().getSerializable(BUNDLE_CHALET_DETAILS);
            }
            if (getArguments().containsKey(BUNDLE_BOOKING_NAME)) {
                bookingName = getArguments().getString(BUNDLE_BOOKING_NAME);
            }

            if (propertyTypeId.equalsIgnoreCase("1")) {//Chalet
                bookingChaletDate = (Date) getArguments().getSerializable(BUNDLE_BOOKING_DATE);
            } else {
                bookingSoccerFieldDate = (DateObject) getArguments().getSerializable(BUNDLE_BOOKING_DATE);
                bookingSlotTime = (ManageSchedulesModel.SlotTime) getArguments().getSerializable(BUNDLE_BOOKING_SLOT);
            }
            termsAndConditions = propertyDetail.getPropertyDetails().get(0).getTermsConditions();
        }
    }

    @Override
    @OnClick({R.id.tv_apply, R.id.tv_cancel, R.id.tv_continue, R.id.tv_remove, R.id.tv_terms_conditions, R.id.tv_i_accept})
    public void onClick(View v) {
        StaticUtil.hideSoftKeyboard(activity);
        switch (v.getId()) {
            case R.id.tv_apply:
                onApplyEvent();
                break;
            case R.id.tv_remove:
                onRemoveEvent();
                break;
            case R.id.tv_i_accept:
                tvIAccept.setSelected(!tvIAccept.isSelected());
                break;
            case R.id.tv_terms_conditions:
                onTermsConditionsEvent();
                break;
            case R.id.tv_continue:
                onContinueEvent();
                break;
            case R.id.tv_cancel:
                activity.popBack();
                break;
            default:
                break;
        }
    }

    private void onApplyEvent() {
        if (!TextUtils.isEmpty(edtCouponCode.getText().toString())) {
            requestDiscountCouponApply();
        } else {
            CommonUtil.showSnackbar(rootView, getString(R.string.please_enter_discount_code));
        }
    }

    private void onRemoveEvent() {
        llApply.setVisibility(View.VISIBLE);
        rlDiscount.setVisibility(View.GONE);
        llCupoun.setVisibility(View.GONE);
        edtCouponCode.setText("");
        requestGetPropertyPrice();
    }

    private void onContinueEvent() {
        if (TextUtils.isEmpty(edtBookingName.getText())) {
            CommonUtil.showSnackbar(rootView, getString(R.string.please_enter_booking_name));
            edtBookingName.requestFocus();
        } else if (TextUtils.isEmpty(edtBookingEmailAddress.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_email_address));
            edtBookingEmailAddress.requestFocus();
        } else if (!StaticUtil.isValidEmail(edtBookingEmailAddress.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_email_address));
            edtBookingEmailAddress.requestFocus();
        } else if (TextUtils.isEmpty(edtMobileNumber.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_mobile_number));
            edtMobileNumber.requestFocus();
        } else if (!PhoneUtil.validMobileNumber(edtMobileNumber.getText().toString().trim()) || edtMobileNumber.getText().toString().trim().length() < 9) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_mobile_number));
            edtMobileNumber.requestFocus();
        } else if (!tvIAccept.isSelected()) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_accept_terms_and_conditions));
        } else if (rbCredits.isChecked()) {
            if (!TextUtils.isEmpty(tvDownPaymentPrice.getText().toString())) {
                double downpaymentPrice = parseDouble(tvDownPaymentPrice.getText().toString());
                String credits = localStorage.getString(Constants.SP_KEY_CREDITS, "");
                if (!TextUtils.isEmpty(credits)) {
                    double dCredits = Double.parseDouble(credits);
                    if (downpaymentPrice > dCredits) {
                        PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.you_dont_have_sufficient_fund_in_your_credit_balance), getString(R.string.ok), null);
                    } else {
                        PopupUtil.showAlert(activity, getString(R.string.app_name), getString(R.string.are_you_sure_you_want_to_use_your_current_balance_), getString(R.string.allow), getString(R.string.deny), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Use credit balance
                                requestForBookPropertyByCustomer();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Dismiss dialog
                            }
                        });
                    }
                }
            }
        } else if (rbBookNowPayLeter.isChecked()) {
            requestForPayLaterPropertyByCustomer();
        } else {
            requestForBookPropertyByCustomer();
        }
    }

    private void onTermsConditionsEvent() {
        PopupUtil.showStaticPage(activity, getString(R.string.terms_conditions), termsAndConditions);
    }

    private void requestGetPropertyPrice() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_CHALET_ID, chaletDetails.getPropertyChaletId());

        if (propertyTypeId.equalsIgnoreCase("1")) {
            params.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingChaletDate));//2016-07-13
        } else {
            params.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingSoccerFieldDate.date));//2016-07-13
        }

        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_GET_PROPERTY_PRICE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_GET_PROPERTY_PRICE, this);
    }

    private void requestDiscountCouponApply() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_CUSTOMER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTY_CHALET_ID, chaletDetails.getPropertyChaletId());
        params.put(WSUtils.KEY_PROPERTY_TYPE, propertyTypeId);
        params.put(WSUtils.KEY_COUPON_CODE, edtCouponCode.getText().toString().trim());
        if (propertyTypeId.equalsIgnoreCase("1")) {//Chalet
            params.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingChaletDate));//2016-07-13
        } else {
            params.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingSoccerFieldDate.date));//2016-07-13
        }
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_DISCOUNT_COUPON_APPLY);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_DISCOUNT_COUPON_APPLY, this);
    }

    private void requestForBookPropertyByCustomer() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTY_CHALET_ID, chaletDetails.getPropertyChaletId());
        if (propertyTypeId.equalsIgnoreCase("1")) {//Chalet
            params.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingChaletDate));//2016-07-13
        } else {
            params.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingSoccerFieldDate.date));//2016-07-13
        }
        params.put(WSUtils.KEY_BOOKING_NAME, edtBookingName.getText().toString().trim());
        params.put(WSUtils.KEY_MOBILE_NO, edtMobileNumber.getText().toString().trim());
        params.put(WSUtils.KEY_BOOKING_EMAIL, edtBookingEmailAddress.getText().toString().trim());
        if (!TextUtils.isEmpty(edtBriefNote.getText())) {
            params.put(WSUtils.KEY_BRIEF_NOTE, edtBriefNote.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(edtNationalIdNumber.getText())) {
            params.put(WSUtils.KEY_NATIONAL_ID, edtNationalIdNumber.getText() + "");
        }
        if (bookingSlotTime != null) {
            params.put(WSUtils.KEY_BOOKING_TIME_SLOT, bookingSlotTime.getSlotFromTime() + "-" + bookingSlotTime.getSlotToTime());
        }
        if (!TextUtils.isEmpty(edtCouponCode.getText().toString().trim()) && rlDiscount.getVisibility() == View.VISIBLE) {
            params.put(WSUtils.KEY_COUPON, edtCouponCode.getText().toString().trim());
        }
        params.put(WSUtils.ENDPOINT, "192.168.34.181%2Fonline_chalet_booking%2FWS");
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_BOOK_PROPERTY_BY_CUSTOMER);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_BOOK_PROPERTY_BY_CUSTOMER, this);
    }

    HashMap<String, String> bookNowPayLaterParams;

    private void requestForPayLaterPropertyByCustomer() {
        activity.showHideProgress(true);
        bookNowPayLaterParams = new HashMap<>();
        bookNowPayLaterParams.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        bookNowPayLaterParams.put(WSUtils.KEY_PROPERTY_CHALET_ID, chaletDetails.getPropertyChaletId());
        if (propertyTypeId.equalsIgnoreCase("1")) {//Chalet
            bookNowPayLaterParams.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingChaletDate));//2016-07-13
        } else {
            bookNowPayLaterParams.put(WSUtils.KEY_BOOKING_DATE, DateUtil.getFormatDate("yyyy-MM-dd", bookingSoccerFieldDate.date));//2016-07-13
        }
        bookNowPayLaterParams.put(WSUtils.KEY_BOOKING_NAME, edtBookingName.getText().toString().trim());
        bookNowPayLaterParams.put(WSUtils.KEY_MOBILE_NO, edtMobileNumber.getText().toString().trim());
        bookNowPayLaterParams.put(WSUtils.KEY_BOOKING_EMAIL, edtBookingEmailAddress.getText().toString().trim());
        if (!TextUtils.isEmpty(edtBriefNote.getText())) {
            bookNowPayLaterParams.put(WSUtils.KEY_BRIEF_NOTE, edtBriefNote.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(edtNationalIdNumber.getText())) {
            bookNowPayLaterParams.put(WSUtils.KEY_NATIONAL_ID, edtNationalIdNumber.getText() + "");
        }
        if (bookingSlotTime != null) {
            bookNowPayLaterParams.put(WSUtils.KEY_BOOKING_TIME_SLOT, bookingSlotTime.getSlotFromTime() + "-" + bookingSlotTime.getSlotToTime());
        }
        if (!TextUtils.isEmpty(edtCouponCode.getText().toString().trim()) && rlDiscount.getVisibility() == View.VISIBLE) {
            bookNowPayLaterParams.put(WSUtils.KEY_COUPON, edtCouponCode.getText().toString().trim());
        }
        bookNowPayLaterParams.put(WSUtils.ENDPOINT, "192.168.34.181%2Fonline_chalet_booking%2FWS");
        bookNowPayLaterParams.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        bookNowPayLaterParams.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_PAY_LATER_PROPERTY_BY_CUSTOMER);
        wsUtils.WSRequest(activity, bookNowPayLaterParams, null, WSUtils.REQ_PAY_LATER_PROPERTY_BY_CUSTOMER, this);
    }

    private void requestForCustomerBookingConfirmation(PayFortData payFortData) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
//        params.put(WSUtils.KEY_ORDER_ID, "BOOK00000"+bookingOrderId);
        params.put(WSUtils.KEY_ORDER_ID, bookingOrderId);
        params.put(WSUtils.KEY_MERCHANT_REFERENCE, "BOOK00000" + bookingOrderId);
        params.put(WSUtils.KEY_TXN_ID, payFortData.fortId != null ? payFortData.fortId : "-1");
        params.put(WSUtils.KEY_PAYMENT_DATA, payFortData.paymentRequest/*payFortData.responseCode*/);
        params.put(WSUtils.KEY_PAYMENT_RESPONSE, payFortData.paymentResponse);

        if (payFortData.status.equalsIgnoreCase("14")) {
            params.put(WSUtils.KEY_BOOKING_STATUS, WSUtils.Booked);//Booked/Failed
            params.put(WSUtils.KEY_PAYMENT_STATUS, WSUtils.Paid);//Paid/UnPaid
        } else {
            params.put(WSUtils.KEY_BOOKING_STATUS, WSUtils.Failed);//Booked/Failed
            params.put(WSUtils.KEY_PAYMENT_STATUS, WSUtils.UnPaid);//Paid/UnPaid
        }

        params.put(WSUtils.KEY_PAYMENT_METHOD, WSUtils.Payfort);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_BOOKING_CONFIRMATION);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_BOOKING_CONFIRMATION, this);
    }

    /**
     * Request for booking, when user pay form his available credit.
     */
    private void requestForCustomerBookingConfirmationWithWallet() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_ORDER_ID, bookingOrderId);
        params.put(WSUtils.KEY_TXN_ID, "-1");

        //Remove fields
//        params.put(WSUtils.KEY_PAYMENT_DATA, payFortData.responseCode);
//        params.put(WSUtils.KEY_PAYMENT_RESPONSE, payFortData.paymentResponse);

        params.put(WSUtils.KEY_BOOKING_STATUS, WSUtils.Booked);//Booked/Failed
        params.put(WSUtils.KEY_PAYMENT_STATUS, WSUtils.Paid);//Paid/UnPaid
        params.put(WSUtils.KEY_PAYMENT_METHOD, WSUtils.Credit);

        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_BOOKING_CONFIRMATION);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_BOOKING_CONFIRMATION_USING_WALLET, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_GET_PROPERTY_PRICE:
                    parseGetPropertyPrice((GetPropertyPriceModel) response);
                    break;
                case WSUtils.REQ_DISCOUNT_COUPON_APPLY:
                    parseDiscountCouponApply((DiscountCouponApplyModel) response);
                    break;
                case WSUtils.REQ_BOOK_PROPERTY_BY_CUSTOMER:
                    parseBookSoccerByCustomer((BookPropertyByCustomerModel) response);
                    break;
                case WSUtils.REQ_PAY_LATER_PROPERTY_BY_CUSTOMER:
                    parseBookPayLaterByCustomer((BookPropertyByCustomerModel) response);
                    break;
                case WSUtils.REQ_CUSTOMER_BOOKING_CONFIRMATION:
                    parseCustomerBookingConfirmation((CustomerBookingConfirmationModel) response);
                    break;
                case WSUtils.REQ_CUSTOMER_BOOKING_CONFIRMATION_USING_WALLET:
                    parseCustomerBookingConfirmationFromWallet((CustomerBookingConfirmationModel) response);
                    break;
            }
        }
    }

    @Override
    public void errorResponse(int requestCode, Throwable t) {
        activity.showHideProgress(false);
        if (requestCode == WSUtils.REQ_GET_PROPERTY_PRICE) {
            CommonUtil.showSnackbar(getView(), getString(R.string.somthing_went_wrong));
            activity.popBack(true);
        }
    }

    @Override
    public void noInternetConnection(int requestCode) {
        CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
        activity.showHideProgress(false);
        if (requestCode == WSUtils.REQ_GET_PROPERTY_PRICE) {
            activity.popBack(true);
        }
    }

    private void parseGetPropertyPrice(GetPropertyPriceModel response) {
        activity.showHideProgress(false);
        try {
            if (response != null)
                if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                    tvTotalPrice.setText(response.getData().get(0).getPrice());
                    tvTotalPriceDay.setText(getString(R.string.total_price) + " (" + response.getData().get(0).getDay() + ")");
                    tvDownPaymentPrice.setText(response.getData().get(0).getDownPayment());
                    tvServiceChargePrice.setText(response.getData().get(0).getServiceCharge());
                    tvAmountToPayPrice.setText(response.getData().get(0).getAmountToPay());
                    tvRemainingAmountToPayPrice.setText(response.getData().get(0).getRemainingAmount());
                } else {
                    CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
                    activity.popBack(true);
                }
            else activity.popBack(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDiscountCouponApply(DiscountCouponApplyModel response) {
        activity.showHideProgress(false);
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            llApply.setVisibility(View.GONE);
            rlDiscount.setVisibility(View.VISIBLE);
            llCupoun.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(response.getData().getCalculation().get(0).getDiscountPrice())) {
                rlDownpayment.setVisibility(View.VISIBLE);
                tvDiscountPrice.setText(response.getData().getCalculation().get(0).getDiscountPrice());
            } else {
                rlDownpayment.setVisibility(View.GONE);
            }
            tvDiscount.setText(getString(R.string.discount_code) + " (" + edtCouponCode.getText().toString().trim() + ")");
            tvDownPaymentPrice.setText(response.getData().getCalculation().get(0).getDownPayment());
            tvServiceChargePrice.setText(response.getData().getCalculation().get(0).getServicePrice());
            tvAmountToPayPrice.setText(response.getData().getCalculation().get(0).getAmountToPay());
            tvRemainingAmountToPayPrice.setText(response.getData().getCalculation().get(0).getPropertyOwnerEarn());
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
        } else {
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
        }
    }

    private void parseBookSoccerByCustomer(BookPropertyByCustomerModel response) {
        activity.showHideProgress(false);
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            bookingOrderId = response.getData().getGetorderid().getOrderId();
//            requestForCustomerBookingConfirmation();
            // Proceed to Payment
            //requestForPayfortPayment();
            if (rbCredits.isChecked())
                //requestForCustomerBookingConfirmationWithWallet();
                activity.requestGetCredit(true, this);
            else
                requestForPayfortPayment();
//                requestForSadadPayfortPayment();
        } else if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("2")) {
            activity.manageTokenExpire();
        } else {
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
        }
    }

    private void parseBookPayLaterByCustomer(final BookPropertyByCustomerModel response) {
        activity.showHideProgress(false);
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            PopupUtil.showAlertMessage(activity, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.allow), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.replaceFragment(ListViewFragment.newInstance(), false, false, true, true);
//                    response.getData().getGetPropertyDetails().
//                    bookNowPayLaterParams.get(WSUtils.KEY_PROPERTY_CHALET_ID);
                    String bookingDetails = "Hello Weekend, \n I have booked Chalet : " + chaletDetails.getChaletTitle() + " with Booking details -\n Name : " + bookNowPayLaterParams.get(WSUtils.KEY_BOOKING_NAME) + " ,\n Mobile Number : " + bookNowPayLaterParams.get(WSUtils.KEY_MOBILE_NO) + " ,\n Email : " +
                            bookNowPayLaterParams.get(WSUtils.KEY_BOOKING_EMAIL) + "\n for the Date : " + bookNowPayLaterParams.get(WSUtils.KEY_BOOKING_DATE) + " between time slot : " + bookNowPayLaterParams.get(WSUtils.KEY_BOOKING_TIME_SLOT) + " by selecting book now and pay later option";
                    Intent chatIntent = new Intent(activity, ChatActivity.class);
                    chatIntent.putExtra("booking_details", bookingDetails);
                    activity.startActivity(chatIntent);
                }
            });
        } else if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("2")) {
            activity.manageTokenExpire();
        } else {
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
        }
    }

    private void requestForPayfortPayment() {
        PayFortData payFortData = new PayFortData();
        if (!TextUtils.isEmpty(tvAmountToPayPrice.getText().toString())) {
            payFortData.amount = String.valueOf((int) (Float.parseFloat(tvAmountToPayPrice.getText().toString()) * 100));// Multiplying with 100, bcz amount should not be in decimal format
            payFortData.command = PayFortPayment.PURCHASE;
            payFortData.currency = PayFortPayment.CURRENCY_TYPE;
            payFortData.customerEmail = localStorage.getString(Constants.SP_KEY_EMAIL, "");
            payFortData.language = "ar";
            payFortData.merchantReference = "BOOK00000" + bookingOrderId;//String.valueOf(System.currentTimeMillis());

            PayFortPayment payFortPayment = new PayFortPayment(activity, activity.fortCallback, this);
            payFortPayment.requestForPayment(payFortData);
        }
    }

    private void requestForSadadPayfortPayment() {
        PayFortData payFortData = new PayFortData();
        if (!TextUtils.isEmpty(tvAmountToPayPrice.getText().toString())) {
            payFortData.amount = String.valueOf((int) (Float.parseFloat(tvAmountToPayPrice.getText().toString()) * 100));// Multiplying with 100, bcz amount should not be in decimal format
            payFortData.command = PayFortPayment.PURCHASE;
            payFortData.currency = PayFortPayment.CURRENCY_TYPE;
            payFortData.customerEmail = localStorage.getString(Constants.SP_KEY_EMAIL, "");
            payFortData.language = "ar";
            payFortData.merchantReference = String.valueOf(System.currentTimeMillis());

            SadadPayFortPayment payFortPayment = new SadadPayFortPayment(activity, activity.fortCallback, this);
            payFortPayment.requestForPayment(payFortData);
        }
    }

    private void parseCustomerBookingConfirmation(CustomerBookingConfirmationModel response) {
        activity.showHideProgress(false);
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            WeekendApplication.trackEvent(getActivity(), "الحجز الملكية", propertyTypeId.equalsIgnoreCase("1") ? "شاليه الحجز" : "الحجز كرة القدم الحجز", chaletDetails.getChaletTitle().trim());
            activity.replaceFragment(BookingCompletedFragment.newInstance(response.getData().get(0).getBookingId()), false, true, false, true);
        } else if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("2")) {
            activity.manageTokenExpire();
        } else {
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            activity.popBack(true);
        }
    }

    private void parseCustomerBookingConfirmationFromWallet(CustomerBookingConfirmationModel response) {
        activity.showHideProgress(false);
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            WeekendApplication.trackEvent(getActivity(), "الحجز الملكية", propertyTypeId.equalsIgnoreCase("1") ? "شاليه الحجز" : "الحجز كرة القدم الحجز", chaletDetails.getChaletTitle().trim());
            activity.replaceFragment(BookingCompletedFragment.newInstance(response.getData().get(0).getBookingId()), false, true, false, true);
            activity.requestGetCredit();
        } else if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("2")) {
            activity.manageTokenExpire();
        } else {
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            activity.popBack(true);
        }
    }

    @Override
    public void onPaymentRequestResponse(int responseType, final PayFortData responseData) {
        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
            PopupUtil.showAlert(activity, getString(R.string.app_name), getString(R.string.payment_failed_please_try_again),
                    getString(R.string.allow), getString(R.string.deny), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestForPayfortPayment();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestForCustomerBookingConfirmation(responseData);
                        }
                    });
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
            if (responseData != null && !TextUtils.isEmpty(responseData.responseMessage)) {
                CommonUtil.showSnackbar(getView(), responseData.responseMessage);
            } else {
                CommonUtil.showSnackbar(getView(), getString(R.string.payment_cancelled));
            }
            requestForCustomerBookingConfirmation(responseData);
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
            if (responseData != null && !TextUtils.isEmpty(responseData.responseMessage)) {
                CommonUtil.showSnackbar(getView(), responseData.responseMessage);
            } else {
                CommonUtil.showSnackbar(getView(), getString(R.string.payment_failuer));
            }
            requestForCustomerBookingConfirmation(responseData);
        } else {
            requestForCustomerBookingConfirmation(responseData);
        }
    }

    @Override
    public void onDataRefresh() {
        setWalletData();
        requestForCustomerBookingConfirmationWithWallet();
    }
}
