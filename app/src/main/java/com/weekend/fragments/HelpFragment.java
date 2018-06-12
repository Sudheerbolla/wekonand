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
import android.widget.Spinner;

import com.weekend.R;
import com.weekend.adapters.SpinnerISDCodeAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.models.ContactUsModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 2/8/16.
 */
public class HelpFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse> {

    public static final String TAG = HelpFragment.class.getSimpleName();
    @Bind(R.id.spinner_isd)
    Spinner spinnerIsd;
    @Bind(R.id.edt_first_name)
    CustomEditText edtFirstName;
    @Bind(R.id.edt_email)
    CustomEditText edtEmail;
    @Bind(R.id.edt_mobile_number)
    CustomEditText edtMobileNumber;
    @Bind(R.id.edt_message)
    CustomEditText edtMessage;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private List<ISDCodeModel.Datum> ISDCodeList;
    private String selectedISDCode;
    private int selectedISDCodeLength;

    private String phoneNumber = "919033072569";
    private String to = "onlinesoccer%40mailinator.com";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HelpFragment.
     */
    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        Bundle bundle = new Bundle();

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
                rootView = inflater.inflate(R.layout.fragment_help, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.contact_us), true, false, false, false, false, true, true,
                    false, false, false, false,true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initialize();
                }
            }, Constants.FRAGMENT_TRANSACTION_DELAY);
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
        ISDCodeList = new ArrayList<>();
        localStorage = LocalStorage.getInstance(activity);
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
        spinnerIsd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                selectedISDCode = ISDCodeList.get(spinnerIsd.getSelectedItemPosition()).getMcIsdcode();
                selectedISDCodeLength = CommonUtil.getIsdCodeCount(selectedISDCode);
                if (selectedISDCodeLength == 2) {
                    CommonUtil.setMaxLength(edtMobileNumber, 10);
                } else if (selectedISDCodeLength == 3) {
                    CommonUtil.setMaxLength(edtMobileNumber, 9);
                }
//                if (!localStorage.getString(Constants.SP_KEY_MOBILE_CODE, "").equalsIgnoreCase(selectedISDCode)) {
//                    edtMobileNumber.setText("");
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        requestGetISDCode();
        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            //logged in user
            edtFirstName.setText(localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""));
            edtEmail.setText(localStorage.getString(Constants.SP_KEY_EMAIL, ""));
            edtMobileNumber.setText(localStorage.getString(Constants.SP_KEY_MOBILE_NO, ""));
        } else {
            //not logged in user
        }
    }

    private void getBundleData() {
        if (getArguments() != null) {
        }
    }

    @Override
    @OnClick({R.id.tv_send, R.id.tv_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send:
                validateData();
                break;
            case R.id.tv_cancel:
                activity.onBackPressed();
                break;
            default:
                break;
        }
    }

    private void validateData() {
        StaticUtil.hideSoftKeyboard(activity);
        if (TextUtils.isEmpty(edtFirstName.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_firstname));
            edtFirstName.requestFocus();
            return;
        } else if (TextUtils.isEmpty(edtEmail.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_email_address));
            edtEmail.requestFocus();
            return;
        } else if (!StaticUtil.isValidEmail(edtEmail.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_email_address));
            return;
        } else if (TextUtils.isEmpty(edtMobileNumber.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_mobile_number));
            edtMobileNumber.requestFocus();
            return;
        } else if (!PhoneUtil.validMobileNumber(edtMobileNumber.getText().toString().trim()) || (edtMobileNumber.getText().toString().trim().length() + selectedISDCodeLength) != 12) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_mobile_number));
            edtMobileNumber.requestFocus();
            return;
        } else if (TextUtils.isEmpty(edtMessage.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_message));
            return;
        } else {
            requestContactUs();
        }
    }

    private void requestGetISDCode() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_GET_ISD_CODE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_GET_ISD_CODE, this);
    }

    private void requestContactUs() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_NAME, edtFirstName.getText().toString());
        params.put(WSUtils.KEY_EMAIL, edtEmail.getText().toString());
        params.put(WSUtils.KEY_MOBILE_CODE, ISDCodeList.get(spinnerIsd.getSelectedItemPosition()).getMcIsdcode());
        params.put(WSUtils.KEY_MOBILE_NO, edtMobileNumber.getText().toString().trim());
        params.put(WSUtils.KEY_DESCRIPTION, edtMessage.getText().toString());
        params.put(WSUtils.KEY_PHONE_NO, phoneNumber);
        params.put(WSUtils.KEY_TO, to);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CONTACT_US);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CONTACT_US, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_GET_ISD_CODE:
                    parseGetISDCode((ISDCodeModel) response);
                    break;
                case WSUtils.REQ_CONTACT_US:
                    parseContactUS((ContactUsModel) response);
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
                spinnerIsd.setAdapter(spinnerISDCodeAdapter);
                setSpinnerSelection();
                selectedISDCode = ISDCodeList.get(spinnerIsd.getSelectedItemPosition()).getMcIsdcode();
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

    private void parseContactUS(ContactUsModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                PopupUtil.showAlertMessage(activity, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
                            }
                        });
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void setSpinnerSelection() {
        if (ISDCodeList != null && ISDCodeList.size() > 0) {
            for (int i = 0; i < ISDCodeList.size(); i++) {
                if (ISDCodeList.get(i).getMcIsdcode().equalsIgnoreCase(localStorage.getString(Constants.SP_KEY_MOBILE_CODE, ""))) {
                    spinnerIsd.setSelection(i);
                }
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
