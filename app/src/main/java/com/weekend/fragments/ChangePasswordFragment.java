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
import com.weekend.models.UserChangePasswordModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.AnimationUtil;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 18/7/16.
 */
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = ChangePasswordFragment.class.getSimpleName();
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangePasswordFragment.
     */
    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
            activity.setTopbar(getString(R.string.change_password), true, false, false, false, false, true, true, false, false, false, false);
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

    private void showScreenWithAnimations() {
        edtOldPasword.startAnimation(AnimationUtil.fadeIn(activity));
        edtOldPasword.setVisibility(View.VISIBLE);
        Animation animNewPassword = AnimationUtil.fadeIn(activity);
        animNewPassword.setStartOffset(2 * Constants.ANIMATION_OFFSET);
        edtNewPasword.startAnimation(animNewPassword);
        edtNewPasword.setVisibility(View.VISIBLE);
        Animation animConfirmPassword = AnimationUtil.fadeIn(activity);
        animConfirmPassword.setStartOffset(3 * Constants.ANIMATION_OFFSET);
        edtConfirmPasword.startAnimation(animConfirmPassword);
        edtConfirmPasword.setVisibility(View.VISIBLE);
        Animation animButtons = AnimationUtil.fadeIn(activity);
        animButtons.setStartOffset(4 * Constants.ANIMATION_OFFSET);
        llButtons.startAnimation(animButtons);
        llButtons.setVisibility(View.VISIBLE);
    }


    private void getBundleData() {
        if (getArguments() != null) {
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
        if (TextUtils.isEmpty(edtOldPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_old_password));
            edtOldPasword.requestFocus();
            return;
        } else if (edtOldPasword.getText().toString().trim().length() < 8) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_correct_old_password));
            edtOldPasword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(edtNewPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_new_password));
            edtNewPasword.requestFocus();
            return;
        } else if (edtNewPasword.getText().toString().trim().length() < 8) {
            CommonUtil.showSnackbar(getView(), getString(R.string.new_password_should_not_be_less_than_8_characters));
            edtNewPasword.requestFocus();
            return;
        } else if (edtOldPasword.getText().toString().trim().equals(edtNewPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.old_password_and_new_password_should_not_be_same));
            edtNewPasword.requestFocus();
            return;
        } else if (TextUtils.isEmpty(edtConfirmPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_confirm_password));
            edtConfirmPasword.requestFocus();
            return;
        } else if (!edtNewPasword.getText().toString().trim().equals(edtConfirmPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.new_password_and_confirm_password_should_not_mismatch));
            edtConfirmPasword.requestFocus();
            return;
        } else {
            requestUserChangePassword();
        }
    }

    private void requestUserChangePassword() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_OLD_PASSWORD, edtOldPasword.getText().toString().trim());
        params.put(WSUtils.KEY_NEW_PASSWORD, edtNewPasword.getText().toString().trim());
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_USER_CHANGE_PASSWORD);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_USER_CHANGE_PASSWORD, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_USER_CHANGE_PASSWORD:
                    parseUserChangePassword((UserChangePasswordModel) response);
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

    private void parseUserChangePassword(UserChangePasswordModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                localStorage.putString(Constants.SP_KEY_PASSWORD, edtNewPasword.getText().toString().trim());
                PopupUtil.showAlertMessage(activity, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
                    }
                });
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }
}
