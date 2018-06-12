package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.models.ResetPasswordModel;
import com.weekend.models.ValidateOTPModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.AnimationUtil;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 18/7/16.
 */
public class ResetPasswordFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = ResetPasswordFragment.class.getSimpleName();
    @Bind(R.id.edt_old_pasword)
    CustomEditText edtOldPasword;
    @Bind(R.id.edt_new_pasword)
    CustomEditText edtNewPasword;
    @Bind(R.id.edt_confirm_pasword)
    CustomEditText edtConfirmPasword;
    @Bind(R.id.ll_buttons)
    LinearLayout llButtons;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();

    private boolean isFromDeepLinking = false;
    private String otp = "", userID = "";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ResetPasswordFragment.
     */
    public static ResetPasswordFragment newInstance(String otp, String userId) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("otp", otp);
        bundle.putString("userId", userId);
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
                rootView = inflater.inflate(R.layout.fragment_change_password, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                        showScreenWithAnimations();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.reset_password), true, false, false, false, false, false, false, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
            activity.hideSplashScreen();
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
        edtOldPasword.setVisibility(View.GONE);
        if (isFromDeepLinking) {
            requestValidateOTP();
        }
    }

    private void showScreenWithAnimations() {
        edtNewPasword.startAnimation(AnimationUtil.fadeIn(activity));
        edtNewPasword.setVisibility(View.VISIBLE);
        Animation animConfirmPassword = AnimationUtil.fadeIn(activity);
        animConfirmPassword.setStartOffset(2 * Constants.ANIMATION_OFFSET);
        edtConfirmPasword.startAnimation(animConfirmPassword);
        edtConfirmPasword.setVisibility(View.VISIBLE);
        Animation animButtons = AnimationUtil.fadeIn(activity);
        animButtons.setStartOffset(3 * Constants.ANIMATION_OFFSET);
        llButtons.startAnimation(animButtons);
        llButtons.setVisibility(View.VISIBLE);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            if (getArguments().containsKey("otp")) {
                otp = getArguments().getString("otp");
            }
            if (getArguments().containsKey("userId")) {
                userID = getArguments().getString("userId");
            }

            if (TextUtils.isEmpty(userID)) {
                isFromDeepLinking = true;
            }
        }
    }

    @Override
    @OnClick({R.id.tv_update, R.id.tv_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update:
                validateChangePassword();
                break;
            case R.id.tv_cancel:
                activity.onBackPressed();
                break;
            default:
                break;
        }
    }

    private void validateChangePassword() {
        StaticUtil.hideSoftKeyboard(activity);
        if (TextUtils.isEmpty(edtNewPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_new_password));
            edtNewPasword.requestFocus();
        } else if (edtNewPasword.getText().toString().trim().length() < 8) {
            CommonUtil.showSnackbar(getView(), getString(R.string.new_password_should_not_be_less_than_8_characters));
            edtNewPasword.requestFocus();
        } else if (TextUtils.isEmpty(edtConfirmPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_confirm_password));
            edtConfirmPasword.requestFocus();
        } else if (!edtNewPasword.getText().toString().trim().equals(edtConfirmPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.new_password_and_confirm_password_should_not_mismatch));
            edtConfirmPasword.requestFocus();
        } else {
            requestResetPassword();
        }
    }

    private void requestValidateOTP() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_MOBILE_OTP, otp);
        params.put(WSUtils.KEY_USER_TYPE, "0");
        params.put(WSUtils.KEY_OTP_TYPE, "0");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_VALIDATE_OTP);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_VALIDATE_OTP, this);
    }

    private void requestResetPassword() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, userID/*localStorage.getString(Constants.SP_KEY_USER_ID, "")*/);
        params.put(WSUtils.KEY_PASSWORD, edtNewPasword.getText().toString().trim());
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_RESET_PASSWORD);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_RESET_PASSWORD, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_VALIDATE_OTP:
                    activity.hideSplashScreen();
                    parseValidateOTP((ValidateOTPModel) response);
                    break;
                case WSUtils.REQ_RESET_PASSWORD:
                    parseResetPassword((ResetPasswordModel) response);
                    break;
            }
        } else {
            activity.hideSplashScreen();
        }
    }

    @Override
    public void errorResponse(int requestCode, Throwable t) {
        activity.showHideProgress(false);
        activity.hideSplashScreen();
    }

    @Override
    public void noInternetConnection(int requestCode) {
        CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
        activity.showHideProgress(false);
    }

    private void parseValidateOTP(final ValidateOTPModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                localStorage.putString(Constants.SP_KEY_LATEST_OTP, otp);
//                localStorage.putString(Constants.SP_KEY_USER_ID, response.getData().get(0).getUserId());
                userID = response.getData().get(0).getUserId();
                requestResetPassword();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void parseResetPassword(ResetPasswordModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                localStorage.putString(Constants.SP_KEY_PASSWORD, edtNewPasword.getText().toString().trim());
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
                activity.replaceFragment(LoginFragment.newInstance(false), false, true, false, true);
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

}
