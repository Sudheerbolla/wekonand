package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.models.FrontForgotPasswordEmailModel;
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
public class ForgotPasswordRecoverUsingEmailFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = ForgotPasswordRecoverUsingEmailFragment.class.getSimpleName();
    //To recover password using email address
    @Bind(R.id.tv_email_address)
    CustomTextView tvEmailAddress;
    @Bind(R.id.edt_email_address)
    CustomEditText edtEmailAddress;
    @Bind(R.id.tv_recover_mobile_number)
    CustomTextView tvRecoverMobileNumber;
    @Bind(R.id.tv_submit)
    CustomTextView tvSubmit;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ForgotPasswordRecoverUsingMobileFragment.
     */
    public static ForgotPasswordRecoverUsingEmailFragment newInstance() {
        ForgotPasswordRecoverUsingEmailFragment fragment = new ForgotPasswordRecoverUsingEmailFragment();
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
                rootView = inflater.inflate(R.layout.fragment_forgot_password_recover_using_email, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.forgot_password_title), true, false, true, false, false, false, false, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ButterKnife.bind(this, rootView);
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
        }
    }

    @Override
    @OnClick({R.id.tv_recover_mobile_number, R.id.tv_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recover_mobile_number:
                onRecoverUsingMobileEvent();
                break;
            case R.id.tv_submit:
                validateForgotPassword();
                break;
            default:
                break;
        }
    }

    private void onRecoverUsingMobileEvent() {
        activity.popBack();
    }

    private void validateForgotPassword() {
        StaticUtil.hideSoftKeyboard(activity);
        if (TextUtils.isEmpty(edtEmailAddress.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_email_address));
            edtEmailAddress.requestFocus();
            return;
        } else if (!StaticUtil.isValidEmail(edtEmailAddress.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_email_address));
            edtEmailAddress.requestFocus();
            return;
        } else {
            requestFrontForgotPassword();
        }
    }

    private void requestFrontForgotPassword() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_EMAIL, edtEmailAddress.getText().toString().trim());
        params.put(WSUtils.KEY_USER_TYPE, "0");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_FRONT_FORGOT_PASSWORD_EMAIL);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_FRONT_FORGOT_PASSWORD, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_FRONT_FORGOT_PASSWORD:
                    parseFrontForgotPassword((FrontForgotPasswordEmailModel) response);
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

    private void parseFrontForgotPassword(FrontForgotPasswordEmailModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
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
        activity.showHideProgress(false);
    }

}
