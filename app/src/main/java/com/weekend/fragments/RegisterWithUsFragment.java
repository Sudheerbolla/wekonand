package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.weekend.R;
import com.weekend.adapters.SpinnerISDCodeAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.models.CustomerRegisterModel;
import com.weekend.models.ISDCodeModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PhoneUtil;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 18/7/16.
 */
public class RegisterWithUsFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = RegisterWithUsFragment.class.getSimpleName();
    @Bind(R.id.edt_first_name)
    CustomEditText edtFirstName;
    @Bind(R.id.edt_last_name)
    CustomEditText edtLastName;
    @Bind(R.id.rgGender)
    RadioGroup rgGender;
    @Bind(R.id.rbMale)
    RadioButton rbMale;
    @Bind(R.id.rbFemale)
    RadioButton rbFemale;
    @Bind(R.id.edt_email)
    CustomEditText edtEmail;
    @Bind(R.id.edt_pasword)
    CustomEditText edtPasword;
    @Bind(R.id.edt_confirm_password)
    CustomEditText edtConfirmPassword;
    @Bind(R.id.spinner_isd)
    Spinner spinnerISD;
    @Bind(R.id.edt_mobile_number)
    CustomEditText edtMobileNumber;
    @Bind(R.id.tv_i_accept)
    CustomTextView tvIAccept;
    @Bind(R.id.tv_register)
    CustomTextView tvRegister;
    @Bind(R.id.tv_cancel)
    CustomTextView tvCancel;
    @Bind(R.id.tv_login)
    CustomTextView tvLogin;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();

    private List<ISDCodeModel.Datum> ISDCodeList;
    private String selectedISDCode;
    private int selectedISDCodeLength;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListViewFragment.
     */
    public static RegisterWithUsFragment newInstance() {
        RegisterWithUsFragment fragment = new RegisterWithUsFragment();
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
                rootView = inflater.inflate(R.layout.fragment_register_with_us, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.register_with_us), true, false, false, false, false, false, false, false, false, false, true);
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
        ISDCodeList = new ArrayList<>();

        edtMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.toString().startsWith("0")) {
                    edtMobileNumber.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinnerISD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                //edtMobileNumber.setText("");
                selectedISDCode = ((CustomTextView) v).getText().toString().trim();
                selectedISDCodeLength = CommonUtil.getIsdCodeCount(selectedISDCode);
                if (selectedISDCodeLength == 2) {
                    CommonUtil.setMaxLength(edtMobileNumber, 10);
                } else if (selectedISDCodeLength == 3) {
                    CommonUtil.setMaxLength(edtMobileNumber, 9);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if (ISDCodeList != null && ISDCodeList.size() > 3) {
            StaticUtil.setHeightToSpinner(spinnerISD, 400);
        }
        requestGetISDCode();
    }

    private void getBundleData() {
        if (getArguments() != null) {
        }
    }

    @Override
    @OnClick({R.id.tv_i_accept, R.id.tv_terms_conditions, R.id.tv_register, R.id.tv_cancel, R.id.tv_login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_i_accept:
                tvIAccept.setSelected(!tvIAccept.isSelected());
                break;
            case R.id.tv_terms_conditions:
                onTermsConditionsEvent();
                break;
            case R.id.tv_register:
                validateRegistration();
                break;
            case R.id.tv_cancel:
                onCancelEvent();
                break;
            case R.id.tv_login:
                onLoginEvent();
                break;
            default:
                break;
        }
    }

    private void onTermsConditionsEvent() {
        activity.replaceFragment(StaticPageFragment.newInstance(getString(R.string.terms_conditions), true), true, true, false, false);

    }

    private void onCancelEvent() {
        PopupUtil.showAlert(activity, activity.getString(R.string.are_you_sure_you_want_to_cancel), getString(R.string.allow), getString(R.string.deny), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
            }
        });
    }

    private void onLoginEvent() {
        // activity.replaceFragment(LoginFragment.newInstance(false), true, true, false, true);
        activity.replaceFragment(LoginFragment.newInstance(false), false, true, false, true);
    }

    private void validateRegistration() {
        StaticUtil.hideSoftKeyboard(activity);
        if (TextUtils.isEmpty(edtFirstName.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_firstname));
            edtFirstName.requestFocus();
        } else if (TextUtils.isEmpty(edtLastName.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_lastname));
            edtLastName.requestFocus();
        } else if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_email_address));
            edtEmail.requestFocus();
        } else if (!StaticUtil.isValidEmail(edtEmail.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_email_address));
            edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(edtPasword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_password));
            edtPasword.requestFocus();
        } else if (edtPasword.getText().toString().trim().length() < 8) {
            CommonUtil.showSnackbar(getView(), getString(R.string.password_should_not_be_less_than_8_characters));
            edtPasword.requestFocus();
        } else if (TextUtils.isEmpty(edtConfirmPassword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_confirm_password));
            edtConfirmPassword.requestFocus();
        } else if (!edtPasword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.password_and_confirm_password_should_not_mismatch));
            edtConfirmPassword.requestFocus();
        } else if (TextUtils.isEmpty(edtMobileNumber.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_mobile_number));
            edtMobileNumber.requestFocus();
        } else if (!PhoneUtil.validMobileNumber(edtMobileNumber.getText().toString().trim()) ||
                (edtMobileNumber.getText().toString().trim().length() + selectedISDCodeLength) != 12) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_mobile_number));
            edtMobileNumber.requestFocus();
        } else if (!tvIAccept.isSelected()) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_accept_terms_and_conditions));
        } else if (spinnerISD.getSelectedItem() == null) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_select_country_code));
        } else {
            requestCustomerRegister();
        }
    }

    private void requestGetISDCode() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_GET_ISD_CODE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_GET_ISD_CODE, this);
    }

    private void requestCustomerRegister() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_FIRST_NAME, edtFirstName.getText().toString().trim());
        params.put(WSUtils.KEY_LAST_NAME, edtLastName.getText().toString().trim());
        params.put(WSUtils.KEY_EMAIL, edtEmail.getText().toString().trim());
        params.put(WSUtils.KEY_PASSWORD, edtPasword.getText().toString().trim());
        params.put(WSUtils.KEY_MOBILE_CODE, ISDCodeList.get(spinnerISD.getSelectedItemPosition()).getMcIsdcode());
        params.put(WSUtils.KEY_MOBILE_NO, edtMobileNumber.getText().toString().trim());
        params.put(WSUtils.KEY_PLATFORM, "Android");
        params.put(WSUtils.KEY_DEVICE_TOKEN, localStorage.getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_GENDER, rbMale.isChecked() ? "Male" : "Female");
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_REGISTER);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CUSTOMER_REGISTER, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_GET_ISD_CODE:
                    activity.hideSplashScreen();
                    parseGetISDCode((ISDCodeModel) response);
                    break;
                case WSUtils.REQ_CUSTOMER_REGISTER:
                    parseCustomerRegister((CustomerRegisterModel) response);
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

    private void parseGetISDCode(ISDCodeModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                ISDCodeList = response.getData();
                rearrangeISDList();
                SpinnerISDCodeAdapter spinnerISDCodeAdapter = new SpinnerISDCodeAdapter(activity, ISDCodeList);
                spinnerISD.setAdapter(spinnerISDCodeAdapter);
                selectedISDCode = ISDCodeList.get(spinnerISD.getSelectedItemPosition()).getMcIsdcode();
                selectedISDCodeLength = CommonUtil.getIsdCodeCount(selectedISDCode);
                if (selectedISDCodeLength == 2) {
                    CommonUtil.setMaxLength(edtMobileNumber, 10);
                } else if (selectedISDCodeLength == 3) {
                    CommonUtil.setMaxLength(edtMobileNumber, 9);
                }
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }


    private void parseCustomerRegister(final CustomerRegisterModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                PopupUtil.showAlertMessage(activity, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String otp = response.getData().get(0).getOtp();
                        String token = response.getData().get(0).getToken();
                        boolean isPustNotification = response.getData().get(0).getPushNotification().equalsIgnoreCase("Yes");
                        boolean isAutoLogin = response.getData().get(0).getAutoLogin().equalsIgnoreCase("Yes");
                        String emailId = edtEmail.getText().toString().trim();
                        String password = edtPasword.getText().toString().trim();
                        String mobileNo = edtMobileNumber.getText().toString().trim();
                        localStorage.putString(Constants.SP_KEY_FIRST_NAME, edtFirstName.getText().toString());
                        localStorage.putString(Constants.SP_KEY_LAST_NAME, edtLastName.getText().toString());
                        activity.replaceFragment(RegisterOTPConfirmFragment.newInstance(otp, token, isPustNotification, isAutoLogin, emailId, password, mobileNo), true, true, false, false);
                    }
                });
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void rearrangeISDList() {
        //Functionaliy need +966 should be always at top
        if (ISDCodeList != null && ISDCodeList.size() > 0) {
            for (int i = 0; i < ISDCodeList.size(); i++) {
                ISDCodeModel.Datum ISD = ISDCodeList.get(i);
                if (!TextUtils.isEmpty(ISD.getMcIsdcode()) && ISD.getMcIsdcode().equalsIgnoreCase("+966")) {
                    ISDCodeList.remove(i);
                    ISDCodeList.add(0, ISD);
                }
            }
        }
    }

}
