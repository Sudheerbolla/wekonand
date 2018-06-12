package com.weekend.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.models.CustomerLoginModel;
import com.weekend.models.ResendOTPModel;
import com.weekend.models.ValidateOTPModel;
import com.weekend.qbutils.UserLoginAndUpdateService;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 18/7/16.
 */
public class RegisterOTPConfirmFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = RegisterOTPConfirmFragment.class.getSimpleName();
    private static final String BUNDLE_OTP = "otp";
    private static final String BUNDLE_TOKEN = "token";
    private static final String BUNDLE_IS_PUSH_NOTIFICATION = "isPushNotification";
    private static final String BUNDLE_IS_AUTO_LOGIN = "isAutoLogin";
    private static final String BUNDLE_EMAIL_ID = "email_id";
    private static final String BUNDLE_PASSWORD = "password";
    private static final String BUNDLE_MOBILE_NO = "mobile";
    @Bind(R.id.edt_otp)
    CustomEditText edtOtp;
    @Bind(R.id.tv_resend_otp)
    CustomTextView tvResendOtp;
    @Bind(R.id.tv_verify_mobile_number)
    CustomTextView tvVerifyMobileNumber;
    @Bind(R.id.tv_thank)
    CustomTextView tvThank;
    private View rootView;
    private String otp, email, password, mobile, token = "";
    private boolean isPushNotification, isAutoLogin;

    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private DbHelper localDbHelper;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForgotPasswordRecoverUsingMobileFragment.
     */
    public static RegisterOTPConfirmFragment newInstance(String otp, String token, boolean isPushNotification, boolean isAutoLogin, String emailId, String password, String mobileNo) {
        RegisterOTPConfirmFragment fragment = new RegisterOTPConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_OTP, otp);
        bundle.putString(BUNDLE_TOKEN, token);
        bundle.putBoolean(BUNDLE_IS_PUSH_NOTIFICATION, isPushNotification);
        bundle.putBoolean(BUNDLE_IS_AUTO_LOGIN, isAutoLogin);
        bundle.putString(BUNDLE_EMAIL_ID, emailId);
        bundle.putString(BUNDLE_PASSWORD, password);
        bundle.putString(BUNDLE_MOBILE_NO, mobileNo);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static RegisterOTPConfirmFragment newInstance(String otp, String emailId, String password) {
        RegisterOTPConfirmFragment fragment = new RegisterOTPConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_OTP, otp);
        bundle.putString(BUNDLE_EMAIL_ID, emailId);
        bundle.putString(BUNDLE_PASSWORD, password);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localDbHelper = new DbHelper(activity);
        getBundleData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_register_otp_confirm, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.otp_sent), true, false, true, false, false, false, false, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        WeekendApplication.selectedFragment = TAG;
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        if (TextUtils.isEmpty(token)) {
            tvThank.setVisibility(View.GONE);
        }

//        edtOtp.setText(otp);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            otp = getArguments().getString(BUNDLE_OTP, "");
            token = getArguments().getString(BUNDLE_TOKEN, "");
            isPushNotification = getArguments().getBoolean(BUNDLE_IS_PUSH_NOTIFICATION, true);
            isAutoLogin = getArguments().getBoolean(BUNDLE_IS_AUTO_LOGIN, true);
            email = getArguments().getString(BUNDLE_EMAIL_ID, "");
            password = getArguments().getString(BUNDLE_PASSWORD, "");
            mobile = getArguments().getString(BUNDLE_MOBILE_NO, "");
        }
    }

    @Override
    @OnClick({R.id.tv_resend_otp, R.id.tv_verify_mobile_number})
    public void onClick(View v) {
        StaticUtil.hideSoftKeyboard(activity);
        switch (v.getId()) {
            case R.id.tv_resend_otp:
                onResendOtpEvent();
                break;
            case R.id.tv_verify_mobile_number:
                onVerifyMobileNumberEvent();
                break;
            default:
                break;
        }
    }

    private void onResendOtpEvent() {
        requestResendOTP();
    }

    private void onVerifyMobileNumberEvent() {
        if (TextUtils.isEmpty(edtOtp.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_the_otp_received));
            edtOtp.requestFocus();
        } else {
            requestValidateOTP();
        }
    }

    private void requestResendOTP() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PREV_OTP, otp);
        params.put(WSUtils.KEY_OTP_TYPE, "0");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_RESEND_OTP);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_RESEND_OTP, this);
    }

    private void requestValidateOTP() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_MOBILE_OTP, edtOtp.getText().toString().trim());
        params.put(WSUtils.KEY_USER_TYPE, "0");
        params.put(WSUtils.KEY_OTP_TYPE, "0");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_VALIDATE_OTP);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_VALIDATE_OTP, this);
    }

    private void requestCustomerLogin() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_EMAIL, email);
        params.put(WSUtils.KEY_PASSWORD, password);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_DEVICE_TOKEN, localStorage.getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_LOGIN);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_LOGIN, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_RESEND_OTP:
                    parseResendOTP((ResendOTPModel) response);
                    break;
                case WSUtils.REQ_VALIDATE_OTP:
                    parseValidateOTP((ValidateOTPModel) response);
                    break;
                case WSUtils.REQ_CUSTOMER_LOGIN:
                    parseCustomerLogin((CustomerLoginModel) response);
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

    private void parseResendOTP(ResendOTPModel response) {
        if (response != null) {
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                otp = response.getData().get(0).getOtp();
            }
        }
        activity.showHideProgress(false);
    }

    private void parseValidateOTP(final ValidateOTPModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (!TextUtils.isEmpty(token)) {
                    WeekendApplication.trackEvent(getActivity(), getString(R.string.register), "سجلت بنجاح", "User-ID: " + response.getData().get(0).getUserId());
                    navigateToHomeActivity(response);
                } else {
                    requestCustomerLogin();
                }
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void navigateToHomeActivity(final ValidateOTPModel response) {
        localStorage.putString(Constants.SP_KEY_USER_ID, response.getData().get(0).getUserId());
        localStorage.putString(Constants.SP_KEY_LATEST_OTP, otp);
        localStorage.putString(Constants.SP_KEY_TOKEN, token);
        localStorage.putBoolean(Constants.SP_KEY_PUSH_NOTIFICATION, isPushNotification);
        localStorage.putBoolean(Constants.SP_KEY_AUTO_LOGIN, isAutoLogin);
        localStorage.putString(Constants.SP_KEY_EMAIL, email);
        localStorage.putString(Constants.QB_EMAIL_ID, email);
        localStorage.putString(Constants.SP_KEY_MOBILE_NO, mobile);
        localStorage.putString(Constants.SP_KEY_PASSWORD, password);
        checkForQuickBloxSignUpLogin();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
            }
        }, 2500);
    }

    private void parseCustomerLogin(CustomerLoginModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                localStorage.putString(Constants.SP_KEY_USER_ID, response.getData().get(0).getUserId());
                localStorage.putString(Constants.SP_KEY_TOKEN, response.getData().get(0).getToken());
                localStorage.putString(Constants.SP_KEY_MOBILE_NO, response.getData().get(0).getMobileNo());
                localStorage.putString(Constants.SP_KEY_MOBILE_CODE, response.getData().get(0).getMobileCode());
                localStorage.putString(Constants.SP_KEY_EMAIL, response.getData().get(0).getEmailId());
                localStorage.putString(Constants.QB_EMAIL_ID, response.getData().get(0).getEmailId());
                localStorage.putString(Constants.SP_KEY_FIRST_NAME, response.getData().get(0).getFirstName());
                localStorage.putString(Constants.SP_KEY_PASSWORD, password);
                localStorage.putBoolean(Constants.SP_KEY_IS_REMEMBER_ME, false);
                localStorage.putBoolean(Constants.SP_KEY_AUTO_LOGIN, response.getData().get(0).getAutoLogin().equalsIgnoreCase("Yes"));
                localStorage.putBoolean(Constants.SP_KEY_PUSH_NOTIFICATION, response.getData().get(0).getPushNotification().equalsIgnoreCase("Yes"));
                checkForQuickBloxSignUpLogin();
                WeekendApplication.selectedFragment = "";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
                    }
                }, 2500);
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
//                activity.manageTokenExpire();
                PopupUtil.showAlertMessage(activity, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.replaceFragment(LoginFragment.newInstance(false), false, true, false, true);
                    }
                });
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void checkForQuickBloxSignUpLogin() {
        activity.startService(new Intent(activity, UserLoginAndUpdateService.class));
    }

}
