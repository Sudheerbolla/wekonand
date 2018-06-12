package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.models.ResendOTPModel;
import com.weekend.models.UpdateMobileNumberFromUphModel;
import com.weekend.models.ValidateOTPModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PopupUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 18/7/16.
 */
public class EditProfileOTPConfirmFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = EditProfileOTPConfirmFragment.class.getSimpleName();
    private final static String BUNDLE_ISD_CODE = "ISDCode";
    private final static String BUNDLE_MOBILE_NO = "mobileNo";
    private final static String BUNDLE_OTP = "otp";
    private final static String BUNDLE_UPHID = "uph_id";
    @Bind(R.id.edt_otp)
    CustomEditText edtOtp;
    @Bind(R.id.tv_resend_otp)
    CustomTextView tvResendOtp;
    @Bind(R.id.tv_verify_mobile_number)
    CustomTextView tvVerifyMobileNumber;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private String ISDCode, mobileNo, otp, uphId;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForgotPasswordRecoverUsingMobileFragment.
     */
    public static EditProfileOTPConfirmFragment newInstance(String isdCode, String mobileNo, String otp, String uphId) {
        EditProfileOTPConfirmFragment fragment = new EditProfileOTPConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_ISD_CODE, isdCode);
        bundle.putString(BUNDLE_MOBILE_NO, mobileNo);
        bundle.putString(BUNDLE_OTP, otp);
        bundle.putString(BUNDLE_UPHID, uphId);
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
                rootView = inflater.inflate(R.layout.fragment_edit_profile_otp_confirm, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.verify_mobile_number_l), true, false, true, false, false, false, false, false, false, false, false);
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
    }

    private void getBundleData() {
        if (getArguments() != null) {
            ISDCode = getArguments().getString(BUNDLE_ISD_CODE);
            mobileNo = getArguments().getString(BUNDLE_MOBILE_NO);
            otp = getArguments().getString(BUNDLE_OTP);
            uphId = getArguments().getString(BUNDLE_UPHID);
        }
    }

    @Override
    @OnClick({R.id.tv_resend_otp, R.id.tv_verify_mobile_number})
    public void onClick(View v) {
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
            return;
        } else {
            requestValidateOTP();
        }
    }

    private void requestResendOTP() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
//        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_PREV_OTP, otp);
        params.put(WSUtils.OTP_TYPE, "1");// [0 - register/ 1- profile / 2 - MobileResetPwd]
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_RESEND_OTP);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_RESEND_OTP, this);
    }

    private void requestValidateOTP() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_MOBILE_OTP, edtOtp.getText().toString().trim());
        params.put(WSUtils.KEY_USER_TYPE, "0");
        params.put(WSUtils.KEY_OTP_TYPE, "1");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_VALIDATE_OTP);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_VALIDATE_OTP, this);
    }

    private void requestUpdateMobileNumberFromUph() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_PASSWORD_HISTORY_ID, uphId);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_UPDATE_MOBILE_NUMBER_FROM_UPH);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_UPDATE_MOBILE_NUMBER_FROM_UPH, this);
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
                case WSUtils.REQ_UPDATE_MOBILE_NUMBER_FROM_UPH:
                    parseUpdateMobileNumber((UpdateMobileNumberFromUphModel) response);
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
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                otp = response.getData().get(0).getOtp();
            }
            CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
        }
        activity.showHideProgress(false);
    }

    private void parseValidateOTP(ValidateOTPModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                requestUpdateMobileNumberFromUph();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void parseUpdateMobileNumber(final UpdateMobileNumberFromUphModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                PopupUtil.showAlertMessage(activity, getString(R.string.verification_is_successful), response.getSettings().getMessage(),
                        getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                localStorage.putString(Constants.SP_KEY_LATEST_OTP, otp);
                                localStorage.putString(Constants.SP_KEY_MOBILE_NO, response.getData().get(0).getExplodedNumber());
                                localStorage.putString(Constants.SP_KEY_MOBILE_CODE, response.getData().get(0).getExplodedCode());
                                activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
                            }
                        });
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }
}
