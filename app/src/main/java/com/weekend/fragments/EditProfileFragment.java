package com.weekend.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.weekend.R;
import com.weekend.adapters.SpinnerISDCodeAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.ICityClick;
import com.weekend.interfaces.ICountryClick;
import com.weekend.models.AutoLoginChangeStatusModel;
import com.weekend.models.CityModel;
import com.weekend.models.CountryModel;
import com.weekend.models.ISDCodeModel;
import com.weekend.models.UpdateUserProfileModel;
import com.weekend.models.UserDataModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.AnimationUtil;
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
 * Created by Hymavathi.kadali on 19/7/16.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener, ICountryClick, IParser<WSResponse>, ICityClick {
    public static final String TAG = EditProfileFragment.class.getSimpleName();
    @Bind(R.id.iv_auto_login)
    ImageView ivAutoLogin;
    @Bind(R.id.tv_auto_login)
    CustomTextView tvAutoLogin;
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
    @Bind(R.id.edt_mobile_number)
    CustomEditText edtMobileNumber;
    @Bind(R.id.spinner_isd)
    Spinner spinnerISD;
    @Bind(R.id.tv_country)
    CustomTextView tvCountry;
    @Bind(R.id.tv_city)
    CustomTextView tvCity;
    @Bind(R.id.tv_update)
    CustomTextView tvUpdate;
    @Bind(R.id.tv_cancel)
    CustomTextView tvCancel;
    @Bind(R.id.ll_edit_profile)
    LinearLayout llEditProfile;
    @Bind(R.id.rl_menu)
    RelativeLayout rlMenu;
    @Bind(R.id.edt_other_city)
    CustomEditText edtOtherCity;
    @Bind(R.id.rl_auto_login)
    RelativeLayout rlAutoLogin;
    @Bind(R.id.rl_mobile_number)
    RelativeLayout rlMobileNumber;
    @Bind(R.id.ll_buttons)
    LinearLayout llButtons;
    @Bind(R.id.tv_menu1)
    CustomTextView tvMenu1;
    @Bind(R.id.tv_menu2)
    CustomTextView tvMenu2;
    @Bind(R.id.tv_menu_cancel)
    CustomTextView tvMenuCancel;
    @Bind(R.id.ll_popup)
    LinearLayout llPopup;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();

    private List<CountryModel.Datum> countryList;
    private List<CityModel.Datum> cityList;
    private List<ISDCodeModel.Datum> ISDCodeList;

    private String selectedCountryId, selectedCityId;
    private boolean verifyMobile;
    private String selectedISDCode;
    private int selectedISDCodeLength = 0;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditProfileFragment.
     */
    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
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
                rootView = inflater.inflate(R.layout.fragment_edit_profile, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.edit_profile), true, false, true, false, false, true, true, false, false, false, false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        initialize();
                        showScreenWithAnimations();
                    }
                }
            }, Constants.FRAGMENT_TRANSACTION_DELAY);

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

    private void showScreenWithAnimations() {
        rlAutoLogin.startAnimation(AnimationUtil.fadeIn(activity));
        rlAutoLogin.setVisibility(View.VISIBLE);
        Animation animFirstName = AnimationUtil.fadeIn(activity);
        animFirstName.setStartOffset(2 * Constants.ANIMATION_OFFSET);
        edtFirstName.startAnimation(animFirstName);
        edtFirstName.setVisibility(View.VISIBLE);
        Animation animLastName = AnimationUtil.fadeIn(activity);
        animLastName.setStartOffset(3 * Constants.ANIMATION_OFFSET);
        edtLastName.startAnimation(animLastName);
        edtLastName.setVisibility(View.VISIBLE);
        Animation animGender = AnimationUtil.fadeIn(activity);
        animGender.setStartOffset(4 * Constants.ANIMATION_OFFSET);
        rgGender.startAnimation(animGender);
        rgGender.setVisibility(View.VISIBLE);
        Animation animMobileNumber = AnimationUtil.fadeIn(activity);
        animMobileNumber.setStartOffset(5 * Constants.ANIMATION_OFFSET);
        rlMobileNumber.startAnimation(animMobileNumber);
        rlMobileNumber.setVisibility(View.VISIBLE);
        Animation animCountry = AnimationUtil.fadeIn(activity);
        animCountry.setStartOffset(6 * Constants.ANIMATION_OFFSET);
        tvCountry.startAnimation(animCountry);
        tvCountry.setVisibility(View.VISIBLE);
        Animation animCity = AnimationUtil.fadeIn(activity);
        animCity.setStartOffset(7 * Constants.ANIMATION_OFFSET);
        tvCity.startAnimation(animCity);
        tvCity.setVisibility(View.VISIBLE);
        Animation animButtons = AnimationUtil.fadeIn(activity);
        animButtons.setStartOffset(8 * Constants.ANIMATION_OFFSET);
        llButtons.startAnimation(animButtons);
        llButtons.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        countryList = new ArrayList<>();
        cityList = new ArrayList<>();
        localStorage = LocalStorage.getInstance(activity);
        ivAutoLogin.setSelected(localStorage.getBoolean(Constants.SP_KEY_AUTO_LOGIN, false));

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
                selectedISDCode = ISDCodeList.get(spinnerISD.getSelectedItemPosition()).getMcIsdcode();
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

        if (ISDCodeList != null && ISDCodeList.size() > 3) {
            StaticUtil.setHeightToSpinner(spinnerISD, 400);
        }
        requestGetISDCode();
    }

    private void getBundleData() {

    }

    @Override
    @OnClick({R.id.iv_auto_login, R.id.tv_auto_login, R.id.tv_update, R.id.tv_cancel, R.id.tv_country, R.id.tv_city, R.id.tv_menu1, R.id.tv_menu2, R.id.tv_menu_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_auto_login:
            case R.id.tv_auto_login:
                requestAutoLoginChangeStatus();
                break;
            case R.id.tv_update:
                validateProfile();
                break;
            case R.id.tv_cancel:
                activity.onBackPressed();
                break;
            case R.id.tv_country:
                onCountryEvent();
                break;
            case R.id.tv_city:
                onCityEvent();
                break;

            //Before update mobile no. update menu events
            case R.id.tv_menu1:
                verifyMobile = true;
                AnimationUtil.closeSlider(rlMenu, true);
                requestUpdateUserProfile();
                break;
            case R.id.tv_menu2:
                verifyMobile = false;
                AnimationUtil.closeSlider(rlMenu, true);
                requestUpdateUserProfile();
                break;
            case R.id.tv_menu_cancel:
                AnimationUtil.closeSlider(rlMenu, true);
                break;
            default:
                break;
        }
    }

    private void onCountryEvent() {
        PopupUtil.showCountry(activity, getString(R.string.select_country), countryList, this, null);
    }

    private void onCityEvent() {
        if (cityList != null && cityList.size() > 0) {
            PopupUtil.showCity(activity, getString(R.string.select_city), cityList, this, null);
        } else {
            CommonUtil.showSnackbar(rootView, getString(R.string.please_select_country_first));
        }
    }

    @Override
    public void onCountrySelect(int position, CountryModel.Datum country, Dialog dialog) {
        dialog.dismiss();
        tvCountry.setText(country.getCountry());
        if (selectedCountryId != null && !selectedCountryId.equalsIgnoreCase(country.getCountryId())) {
            selectedCountryId = country.getCountryId();
            tvCity.setText("");
            selectedCityId = "";
            cityList.clear();
            requestCityList();
        }

    }

    @Override
    public void onCitySelect(int position, CityModel.Datum city, Dialog dialog) {
        dialog.dismiss();
        tvCity.setText(city.getName());
        selectedCityId = city.getId();
        if (selectedCityId.equalsIgnoreCase("0")) {
            edtOtherCity.setVisibility(View.VISIBLE);
        } else {
            edtOtherCity.setVisibility(View.GONE);
        }
    }


    private void validateProfile() {
        StaticUtil.hideSoftKeyboard(activity);
        if (TextUtils.isEmpty(edtFirstName.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_firstname));
            edtFirstName.requestFocus();
        } else if (TextUtils.isEmpty(edtLastName.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_lastname));
            edtLastName.requestFocus();
        } else if (TextUtils.isEmpty(edtMobileNumber.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_mobile_number));
            edtMobileNumber.requestFocus();
        } else if (!PhoneUtil.validMobileNumber(edtMobileNumber.getText().toString().trim()) ||
                (edtMobileNumber.getText().toString().trim().length() + selectedISDCodeLength) != 12) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_valid_mobile_number));
            edtMobileNumber.requestFocus();
        } else if (TextUtils.isEmpty(tvCountry.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_select_country));
        } else if (TextUtils.isEmpty(tvCity.getText().toString().trim()) && TextUtils.isEmpty(edtOtherCity.getText().toString().trim())) {
            CommonUtil.showSnackbar(getView(), getString(R.string.please_select_city));
        } else {
            AnimationUtil.openSlider(rlMenu, true);
        }
    }

    private void requestAutoLoginChangeStatus() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_ACTION, ivAutoLogin.isSelected() ? "No" : "Yes");
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_AUTO_LOGIN_CHANGE_STATUS);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_AUTO_LOGIN_CHANGE_STATUS, this);
    }

    private void requestGetISDCode() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_GET_ISD_CODE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_GET_ISD_CODE, this);
    }

    private void requestCountryList() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_COUNTRY_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_COUNTRY_LIST, this);
    }

    private void requestCityList() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_COUNTRY_ID, selectedCountryId);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CITY_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CITY_LIST, this);
    }

    private void requestGetUserData() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_GET_USER_DATA);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_GET_USER_DATA, this);
    }

    private void requestUpdateUserProfile() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_FIRST_NAME, edtFirstName.getText().toString().trim());
        params.put(WSUtils.KEY_LAST_NAME, edtLastName.getText().toString().trim());
        params.put(WSUtils.KEY_COUNTRY_ID, selectedCountryId);
        params.put(WSUtils.KEY_CITYID, selectedCityId);
        params.put(WSUtils.KEY_MOBILE_UPDATE, verifyMobile ? "Yes" : "No");
        params.put(WSUtils.KEY_MOBILE_CODE, ((ISDCodeModel.Datum) spinnerISD.getSelectedItem()).getMcIsdcode());
        params.put(WSUtils.KEY_MOBILE_NO, edtMobileNumber.getText().toString().trim());
        String key_city = tvCity.getText().toString().trim();
        if (!TextUtils.isEmpty(key_city) && !key_city.equalsIgnoreCase(getString(R.string.other))) {
            params.put(WSUtils.KEY_CITY, key_city);
        } else {
            params.put(WSUtils.KEY_CITY, edtOtherCity.getText().toString().trim());
        }
        params.put(WSUtils.KEY_GENDER, rbMale.isChecked() ? "Male" : "Female");
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_CODE, getString(R.string.lang_id));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_UPDATE_USER_DATA);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_UPDATE_USER_PROFILE, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_GET_ISD_CODE:
                    parseGetISDCode((ISDCodeModel) response);
                    break;
                case WSUtils.REQ_COUNTRY_LIST:
                    parseCountryList((CountryModel) response);
                    break;
                case WSUtils.REQ_CITY_LIST:
                    parseCityList((CityModel) response);
                    break;
                case WSUtils.REQ_GET_USER_DATA:
                    parseGetUserProfile((UserDataModel) response);
                    break;
                case WSUtils.REQ_UPDATE_USER_PROFILE:
                    parseUpdateUserProfile((UpdateUserProfileModel) response);
                    break;
                case WSUtils.REQ_AUTO_LOGIN_CHANGE_STATUS:
                    parseAutoLoginChangeStatus((AutoLoginChangeStatusModel) response);
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
               /* selectedISDCode = ISDCodeList.get(spinnerISD.getSelectedItemPosition()).getMcIsdcode();
                selectedISDCodeLength = CommonUtil.getIsdCodeCount(selectedISDCode);
                if (selectedISDCodeLength == 2) {
                    CommonUtil.setMaxLength(edtMobileNumber, 10);
                } else if (selectedISDCodeLength == 3) {
                    CommonUtil.setMaxLength(edtMobileNumber, 9);
                } else {
                    CommonUtil.setMaxLength(edtMobileNumber, 9);
                }*/
                requestGetUserData();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void parseAutoLoginChangeStatus(AutoLoginChangeStatusModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                ivAutoLogin.setSelected(!ivAutoLogin.isSelected());
                localStorage.putBoolean(Constants.SP_KEY_AUTO_LOGIN, ivAutoLogin.isSelected());
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void parseCountryList(CountryModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                countryList = response.getData();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void parseCityList(CityModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                cityList = response.getData();
            }
        }
        if (cityList == null) {
            cityList = new ArrayList<>();
        }
        CityModel.Datum city = new CityModel.Datum();
        city.setId("0");
        city.setName(getString(R.string.other));
        cityList.add(city);
        activity.showHideProgress(false);
    }

    private void parseGetUserProfile(UserDataModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                edtFirstName.setText(response.getData().get(0).getFirstName());
                edtLastName.setText(response.getData().get(0).getLastName());
                if (ISDCodeList != null && ISDCodeList.size() > 0) {
                    for (int i = 0; i < ISDCodeList.size(); i++) {
                        if (ISDCodeList.get(i).getMcIsdcode().equalsIgnoreCase(response.getData().get(0).getMobileCode())) {
                            spinnerISD.setSelection(i);
                            break;
                        }
                    }
                }
                selectedISDCode = response.getData().get(0).getMobileCode();
                selectedISDCodeLength = CommonUtil.getIsdCodeCount(selectedISDCode);
                if (selectedISDCodeLength == 2) {
                    CommonUtil.setMaxLength(edtMobileNumber, 10);
                } else if (selectedISDCodeLength == 3) {
                    CommonUtil.setMaxLength(edtMobileNumber, 9);
                } else {
                    CommonUtil.setMaxLength(edtMobileNumber, 9);
                }
                edtMobileNumber.setText(response.getData().get(0).getMobileNo());
                tvCity.setText(response.getData().get(0).getCity());
                tvCountry.setText(response.getData().get(0).getCountry());
                selectedCountryId = response.getData().get(0).getCountryId();
                selectedCityId = response.getData().get(0).getCityId();
                if (response.getData().get(0).getGender().equalsIgnoreCase("Male")) {
                    rbMale.setChecked(true);
                    rbFemale.setChecked(false);
                } else {
                    rbFemale.setChecked(true);
                    rbMale.setChecked(false);
                }
                requestCountryList();
                if (!TextUtils.isEmpty(selectedCountryId)) {
                    requestCityList();
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void parseUpdateUserProfile(UpdateUserProfileModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (verifyMobile) {
                    UpdateUserProfileModel.Datum datum = response.getData().get(0);
                    String ISDCode = ISDCodeList.get(spinnerISD.getSelectedItemPosition()).getMcIsdcode();
                    String mobileNo = edtMobileNumber.getText().toString();
                    String otp = datum.getOtp();
                    String uphId = datum.getUphId();
                    activity.replaceFragment(EditProfileOTPConfirmFragment.newInstance(ISDCode, mobileNo, otp, uphId), true, true, false, false);
                } else {
                    PopupUtil.showAlertMessage(activity, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activity.replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
                        }
                    });
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
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
