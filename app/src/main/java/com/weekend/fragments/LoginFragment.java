package com.weekend.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBSubscription;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.models.CustomerLoginModel;
import com.weekend.models.SendOtp;
import com.weekend.qbutils.ChatService;
import com.weekend.qbutils.ChatServiceUtil;
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

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 18/7/16.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = LoginFragment.class.getSimpleName();
    private static final String BUNDLE_IS_TOKEN_EXPIRE_OR_GUEST_USER = "is_token_expire_or_guest_user";
    @Bind(R.id.edt_email)
    CustomEditText edtEmail;
    @Bind(R.id.edt_password)
    CustomEditText edtPassword;
    @Bind(R.id.tv_remember_me)
    CustomTextView tvRememberMe;
    @Bind(R.id.tv_forgot_password)
    CustomTextView tvForgotPassword;
    @Bind(R.id.tv_login)
    CustomTextView tvLogin;
    @Bind(R.id.tv_signup)
    CustomTextView tvSignup;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private boolean isTokenExpireOrGuestUser;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(boolean isTokenExpireOrGuestUser) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_IS_TOKEN_EXPIRE_OR_GUEST_USER, isTokenExpireOrGuestUser);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
        localDbHelper = new DbHelper(activity);
        //id001.email@gmail.com/12345678
    }

    DbHelper localDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_login, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.login_to_your_account), true, false, false, false, false, false, false, false, false, false, false);
            activity.manageTopbarUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        if (!WeekendApplication.selectedFragment.equalsIgnoreCase("ChaletChildDetailsFragment-Calendar")
                && !WeekendApplication.selectedFragment.equalsIgnoreCase("SoccerFieldChildDetailsFragment-Calendar")) {
            if (isTokenExpireOrGuestUser) {
                WeekendApplication.selectedFragment = TAG + "-TokenExpire";
            } else {
                WeekendApplication.selectedFragment = TAG;
            }
        }
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        if (localStorage.getBoolean(Constants.SP_KEY_IS_REMEMBER_ME, false)) {
            edtEmail.setText(localStorage.getString(Constants.SP_KEY_EMAIL, ""));
            edtPassword.setText(localStorage.getString(Constants.SP_KEY_PASSWORD, ""));
            tvRememberMe.setSelected(true);
        }
        activity.hideSplashScreen();
    }

    private void getBundleData() {
        if (getArguments() != null) {
            isTokenExpireOrGuestUser = getArguments().getBoolean(BUNDLE_IS_TOKEN_EXPIRE_OR_GUEST_USER);
        }
    }

    @Override
    @OnClick({R.id.tv_forgot_password, R.id.tv_remember_me, R.id.tv_login, R.id.tv_signup})
    public void onClick(View v) {
        StaticUtil.hideSoftKeyboard(activity);
        switch (v.getId()) {
            case R.id.tv_forgot_password:
                onForgotPasswordEvent();
                break;
            case R.id.tv_remember_me:
                tvRememberMe.setSelected(!tvRememberMe.isSelected());
                break;
            case R.id.tv_login:
                validateLogin();
                break;
            case R.id.tv_signup:
                activity.replaceFragment(RegisterWithUsFragment.newInstance(), false, true, false, true);
                break;
            default:
                break;
        }
    }

    private void onForgotPasswordEvent() {
        activity.replaceFragment(ForgotPasswordRecoverUsingMobileFragment.newInstance(), true, true, false, true);
    }

    private void validateLogin() {
        if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_email_address));
            edtEmail.requestFocus();
            return;
        } else if (!StaticUtil.isValidEmail(edtEmail.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_email_address));
            edtEmail.requestFocus();
            return;
        } else if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_password));
            edtPassword.requestFocus();
            return;
        } else {
            requestCustomerLogin();
        }
    }

    private void requestCustomerLogin() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_EMAIL, edtEmail.getText().toString().trim());
        params.put(WSUtils.KEY_PASSWORD, edtPassword.getText().toString().trim());
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_DEVICE_TOKEN, localStorage.getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_LOGIN);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_LOGIN, this);
    }

    private void requestSendOtp(CustomerLoginModel.Datum datum) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_MOBILE_NO, datum.getMobileNo());
        params.put(WSUtils.KEY_MOBILE_CODE, datum.getMobileCode());
        params.put(WSUtils.KEY_USER_ID, datum.getUserId());
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_SEND_OTP);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_SEND_OTP, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_CUSTOMER_LOGIN:
                    parseCustomerLogin((CustomerLoginModel) response);
                    break;
                case WSUtils.REQ_SEND_OTP:
                    parseSendOtp((SendOtp) response);
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

    private void parseCustomerLogin(CustomerLoginModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                localStorage.putString(Constants.SP_KEY_USER_ID, response.getData().get(0).getUserId());
                localStorage.putString(Constants.SP_KEY_TOKEN, response.getData().get(0).getToken());
                localStorage.putString(Constants.SP_KEY_MOBILE_NO, response.getData().get(0).getMobileNo());
                localStorage.putString(Constants.SP_KEY_MOBILE_CODE, response.getData().get(0).getMobileCode());
                localStorage.putString(Constants.SP_KEY_EMAIL, edtEmail.getText().toString().trim());
                localStorage.putString(Constants.QB_EMAIL_ID, edtEmail.getText().toString().trim());
                localStorage.putString(Constants.SP_KEY_FIRST_NAME, response.getData().get(0).getFirstName());
                localStorage.putString(Constants.SP_KEY_LAST_NAME, response.getData().get(0).getLastName());
                localStorage.putString(Constants.SP_KEY_PASSWORD, edtPassword.getText().toString().trim());
                localStorage.putBoolean(Constants.SP_KEY_IS_REMEMBER_ME, tvRememberMe.isSelected());
                localStorage.putBoolean(Constants.SP_KEY_AUTO_LOGIN, response.getData().get(0).getAutoLogin().equalsIgnoreCase("Yes"));
                localStorage.putBoolean(Constants.SP_KEY_PUSH_NOTIFICATION, response.getData().get(0).getPushNotification().equalsIgnoreCase("Yes"));

                checkForQuickBloxSignUpLogin();

                if (!WeekendApplication.selectedFragment.equalsIgnoreCase("ChaletChildDetailsFragment-Calendar")
                        && !WeekendApplication.selectedFragment.equalsIgnoreCase("SoccerFieldChildDetailsFragment-Calendar")) {
                    WeekendApplication.selectedFragment = "";
                }
                if (isTokenExpireOrGuestUser) {
                    activity.updateUI(true);
                    activity.popBack();
                } else {
                    activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
                    WeekendApplication.trackEvent(getActivity(), getString(R.string.login), "تسجيل بنجاح في", "User-ID: " + response.getData().get(0).getUserId());
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("3")) {
                final CustomerLoginModel.Datum datum = response.getData().get(0);
                PopupUtil.showAlertMessage(activity, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestSendOtp(datum);
                    }
                });
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void checkForQuickBloxSignUpLogin() {
        activity.startService(new Intent(activity, UserLoginAndUpdateService.class));

//        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_EMAIL, ""))) {
//            final QBUser newUser = new QBUser();
//            newUser.setEmail(localStorage.getString(Constants.SP_KEY_EMAIL, ""));
//            newUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
//            ChatServiceUtil.getInstance().checkIfUserExistsAlreadyWithEmail(newUser, new QBEntityCallback<Void>() {
//                @Override
//                public void onSuccess(Void aVoid, Bundle bundle) {
//                    Log.e("onSuccess", "User Exists with email already");
//                    onSuccessfulLogin(newUser);
//                }
//
//                @Override
//                public void onError(QBResponseException e) {
//                    Log.e("onError", "User Doesn't Exists with email already");
//                    updateCurrentUserWithThisUser(newUser);
//                }
//            });
//        }
    }

    private void updateCurrentUserWithThisUser(QBUser newUser) {
        QBUser newQbUser = WeekendApplication.getInstance().loggedInQBUser;
        newQbUser.setEmail(newUser.getEmail());
        newQbUser.setPassword(StaticUtil.getWeekendCustomerPasswordQb());
        newQbUser.setFullName(localStorage.getString(Constants.SP_KEY_FIRST_NAME, "") + localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
        newQbUser.setPhone(localStorage.getString(Constants.SP_KEY_MOBILE_NO, ""));
        QBUsers.updateUser(newQbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.e("onSuccess", "updateUserDetails onSuccess");
                localDbHelper.closeDb();
                activity.stopService(new Intent(activity, ChatService.class));
                activity.startService(new Intent(activity, ChatService.class));
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("onError", "updateUserDetails onError");
            }
        });
    }

    private void onSuccessfulLogin(QBUser newUser) {
        if (QBChatService.getInstance().isLoggedIn()) {
            addNewUserInExistingDialogs(newUser);
        }
    }

    private void addNewUserInExistingDialogs(final QBUser newUser) {
        ArrayList<QBChatDialog> allChatDialogs = localDbHelper.getChatDialogs();
        for (final QBChatDialog qbChatDialog : allChatDialogs) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ChatServiceUtil.getInstance().updateChatDialogNew(qbChatDialog, newUser, new QBEntityCallback<QBChatDialog>() {
                        @Override
                        public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                            Log.e("onSuccess", "updateChatDialog onSuccess");
                        }

                        @Override
                        public void onError(QBResponseException e) {
                            Log.e("onError", "updateChatDialog onError");
                        }
                    });
                }
            });
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (QBChatService.getInstance().isLoggedIn()) {
                    ChatServiceUtil.getInstance().logout(new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void o, Bundle bundle) {
                            localDbHelper.closeDb();
                            QBChatService.getInstance().destroy();
//                            removeSubscription();
                            activity.stopService(new Intent(activity, ChatService.class));
                            activity.startService(new Intent(activity, ChatService.class));
                        }

                        @Override
                        public void onError(QBResponseException e) {

                        }
                    });
                }

            }
        }, 3500);
    }

    private void removeSubscription() {
        QBPushNotifications.getSubscriptions().performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {
                for (QBSubscription subscription : subscriptions) {
                    if (subscription.getDevice().getId().equals(localStorage.getString(Constants.DEVICE_TOKEN, ""))) {
                        QBPushNotifications.deleteSubscription(subscription.getId()).performAsync(new QBEntityCallback<Void>() {

                            @Override
                            public void onSuccess(Void aVoid, Bundle bundle) {

                            }

                            @Override
                            public void onError(QBResponseException errors) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });
    }

    private void parseSendOtp(SendOtp response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                checkForQuickBloxSignUpLogin();
                activity.replaceFragment(RegisterOTPConfirmFragment.newInstance(response.getData().get(0).getOtp(), edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim()), false, true, false, true);
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }
}
