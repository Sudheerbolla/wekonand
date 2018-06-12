package com.weekend.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.weekend.R;
import com.weekend.adapters.SpinnerISDCodeAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.models.FrontForgotPasswordMobileModel;
import com.weekend.models.ISDCodeModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PhoneUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 18/7/16.
 */
public class ForgotPasswordRecoverUsingMobileFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = ForgotPasswordRecoverUsingMobileFragment.class.getSimpleName();
    //To recover password using mobile number
    @Bind(R.id.edt_mobile_number)
    CustomEditText edtMobileNumber;
    @Bind(R.id.spinner_isd)
    Spinner spinnerISD;
    @Bind(R.id.tv_recover_email_address)
    CustomTextView tvRecoverEmailAddress;
    @Bind(R.id.tv_submit)
    CustomTextView tvSubmit;
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
     * @return A new instance of fragment ForgotPasswordRecoverUsingMobileFragment.
     */
    public static ForgotPasswordRecoverUsingMobileFragment newInstance() {
        ForgotPasswordRecoverUsingMobileFragment fragment = new ForgotPasswordRecoverUsingMobileFragment();
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
                rootView = inflater.inflate(R.layout.fragment_forgot_password_recover_using_mobile, null);
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
        WeekendApplication.selectedFragment = TAG;
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        spinnerISD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                selectedISDCode = ISDCodeList.get(spinnerISD.getSelectedItemPosition()).getMcIsdcode();
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
        StaticUtil.setHeightToSpinner(spinnerISD, 500);
        requestGetISDCode();
    }

    private void getBundleData() {
        if (getArguments() != null) {
        }
    }

    @Override
    @OnClick({R.id.tv_recover_email_address, R.id.tv_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recover_email_address:
                onRecoverUsingEmailEvent();
                break;
            case R.id.tv_submit:
                validateForgotPassword();
                break;
            default:
                break;
        }
    }

    private void onRecoverUsingEmailEvent() {
        activity.replaceFragment(ForgotPasswordRecoverUsingEmailFragment.newInstance(), true, true, false, false);
    }

    private void validateForgotPassword() {
        StaticUtil.hideSoftKeyboard(activity);
        if (TextUtils.isEmpty(edtMobileNumber.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_mobile_number));
            edtMobileNumber.requestFocus();
            return;
        } else if (!PhoneUtil.validMobileNumber(edtMobileNumber.getText().toString().trim()) ||
                (edtMobileNumber.getText().toString().trim().length() + selectedISDCodeLength) != 12) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_mobile_number));
            edtMobileNumber.requestFocus();
        } else {
            requestFrontForgotPassword();
        }
    }

    private void requestGetISDCode() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_GET_ISD_CODE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_GET_ISD_CODE, this);
    }

    private void requestFrontForgotPassword() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        if (ISDCodeList != null && ISDCodeList.size() > 0) {
            params.put(WSUtils.KEY_MOBILE_CODE, ISDCodeList.get(spinnerISD.getSelectedItemPosition()).getMcIsdcode());
        }
        params.put(WSUtils.KEY_MOBILE_NO, edtMobileNumber.getText().toString().trim());
        params.put(WSUtils.KEY_USER_TYPE, "0");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_FRONT_FORGOT_PASSWORD_MOBILE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_FRONT_FORGOT_PASSWORD, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_GET_ISD_CODE:
                    parseGetISDCode((ISDCodeModel) response);
                    break;
                case WSUtils.REQ_FRONT_FORGOT_PASSWORD:
                    parseFrontForgotPassword((FrontForgotPasswordMobileModel) response);
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

    private void parseFrontForgotPassword(FrontForgotPasswordMobileModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
                String otp = response.getData().get(0).getOtp();
                activity.replaceFragment(ForgotPasswordVerifyOTPFragment.newInstance(otp), true, true, false, false);
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
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
