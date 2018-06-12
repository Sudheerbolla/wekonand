package com.weekend.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TimePicker;

import com.weekend.HomeActivity;
import com.weekend.R;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IMultipleSelectionClick;
import com.weekend.interfaces.ISingleSelectionClick;
import com.weekend.models.NeighbourhoodModel;
import com.weekend.models.SearchOptionModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.FragmentUtils;
import com.weekend.utils.PopupUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 20/7/16.
 */
public class SearchFragmentBotBc extends BottomSheetDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, IParser<WSResponse>, ISingleSelectionClick, IMultipleSelectionClick {
    public static final String TAG = SearchFragmentBotBc.class.getSimpleName();
//    @Bind(R.id.rb_soccer_fields)
//    RadioButton rbSoccerFields;
//    @Bind(R.id.rb_chalet)
//    RadioButton rbChalet;
//    @Bind(R.id.v_chalets)
//    View vChalets;
//    @Bind(R.id.v_soccer_fields)
//    View vSoccerFields;
//    @Bind(R.id.edt_title)
//    CustomEditText edtTitle;

    @Bind(R.id.rbLowestPrice)
    RadioButton rbLowestPrice;
    @Bind(R.id.rbDistance)
    RadioButton rbDistance;
    @Bind(R.id.rbRelevance)
    RadioButton rbRelevance;

    @Bind(R.id.tv_checkin_date)
    CustomTextView tvCheckinDate;
    @Bind(R.id.tv_location)
    CustomTextView tvLocation;
    @Bind(R.id.tv_neighbourhood)
    CustomTextView tvNeighbourhood;
    @Bind(R.id.tv_leasing)
    CustomTextView tvLeasing;
    @Bind(R.id.ll_leasing)
    LinearLayout llLeasing;
    @Bind(R.id.tv_size)
    CustomTextView tvSize;
    @Bind(R.id.tv_amenities)
    CustomTextView tvAmenities;
    @Bind(R.id.ll_amenities)
    LinearLayout llAmenities;
    @Bind(R.id.tv_from_time)
    CustomTextView tvFromTime;
    @Bind(R.id.tv_to_time)
    CustomTextView tvToTime;
    @Bind(R.id.ll_time)
    LinearLayout llTime;
    @Bind(R.id.edt_max_price)
    CustomEditText edtMaxPrice;
    @Bind(R.id.edt_min_price)
    CustomEditText edtMinPrice;
    @Bind(R.id.tv_sections)
    CustomTextView tvSections;
    @Bind(R.id.ll_sections)
    LinearLayout llSections;
    @Bind(R.id.tv_suitable_for)
    CustomTextView tvSuitableFor;
    @Bind(R.id.ll_suitable_for)
    LinearLayout llSuitableFor;
    @Bind(R.id.tv_size_title)
    CustomTextView tvSizeTitle;
    @Bind(R.id.sv_search)
    NestedScrollView svSearch;

    private View rootView;
    private SimpleDateFormat dateFormatter, dateFormatterNew;
    private TimePickerDialog timePickerDialogfrom;
    private TimePickerDialog timePickerDialogto;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar, toCalender, fromCalender;
    private int year, month, day;
    private Date fromTime, toTime;
    private WSFactory wsFactory = new WSFactory();

    private SearchOptionModel.Data searchData;
    private List<SearchOptionModel.CityLocation> cityLocationList;
    private List<SearchOptionModel.ChaletSize> chaletSizeList;
    private List<SearchOptionModel.SoccerSize> soccerSizeList;
    private List<SearchOptionModel.GetSectionList> sectionLists;
    private List<SearchOptionModel.ChaletLeasingTypeList> chaletLeasingTypeLists;
    private List<SearchOptionModel.AmenitiesList> amenitiesLists, selectedAmenitiesList;
    private List<SearchOptionModel.SuitableForList> suitableForLists;
    private List<NeighbourhoodModel.Datum> neighbourhoodList;

    private String leasing_type = "", location = "-1", check_in = "", neighbourhood = "", amenities = "",
            title = "", section = "", suitable_for = "", max_price = "", min_price = "", size = "",
            property_type_id = "1", to = "", from = "", availibility = "", lat = "", lng = "";

    public SearchFragmentBotBc() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchFragment.
     */
    public static SearchFragmentBotBc newInstance() {
        SearchFragmentBotBc fragment = new SearchFragmentBotBc();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public HomeActivity activity;
    public WeekendApplication application;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (HomeActivity) getActivity();
        application = (WeekendApplication) activity.getApplication();
        getBundleData();
    }

    @Override
    public void onPause() {
        super.onPause();
        activity.showHideProgress(false);
    }

    /**
     * Method to show loading dialog
     *
     * @param title        Title of loading dialog
     * @param isCancelable isCancelable
     */
    public void showProgressDialog(final String title, final boolean isCancelable) {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setMessage(title);
            mProgressDialog.setCancelable(isCancelable);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to hide Loading dialog
     */
    public void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            mProgressDialog = null;
        } catch (Exception e) {
            mProgressDialog = null;
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations) {
            Animation a = new Animation() {
            };
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        try {
            if (rootView == null) {
//                rootView = inflater.inflate(R.layout.fragment_search, null);
                rootView = View.inflate(getContext(), R.layout.fragment_filter_n_search, null);
            }
            dialog.setContentView(rootView);
            ButterKnife.bind(this, rootView);
//            activity.setTopbar(activity.getString(R.string.search), true, false, true, false, false, false, true, false, false, false, false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        initialize();
                    }
                }
            }, Constants.FRAGMENT_TRANSACTION_DELAY);

        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtil.setTypefacerb(activity, rbDistance, 6);
        CommonUtil.setTypefacerb(activity, rbLowestPrice, 6);
        CommonUtil.setTypefacerb(activity, rbRelevance, 6);
        rbRelevance.setChecked(true);
        return dialog;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        try {
//            if (rootView == null) {
////                rootView = inflater.inflate(R.layout.fragment_search, null);
//                rootView = inflater.inflate(R.layout.bottom_sheet, null);
//            }
//            ButterKnife.bind(this, rootView);
////            activity.setTopbar(activity.getString(R.string.search), true, false, true, false, false, false, true, false, false, false, false);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (isAdded()) {
//                        initialize();
//                    }
//                }
//            }, Constants.FRAGMENT_TRANSACTION_DELAY);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        CommonUtil.setTypefacerb(activity, rbDistance, 6);
//        CommonUtil.setTypefacerb(activity, rbLowestPrice, 6);
//        CommonUtil.setTypefacerb(activity, rbRelevance, 6);
//        rbRelevance.setChecked(true);
//        return rootView;
//    }

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
        cityLocationList = new ArrayList<>();
        sectionLists = new ArrayList<>();
        chaletLeasingTypeLists = new ArrayList<>();
        amenitiesLists = new ArrayList<>();
        selectedAmenitiesList = new ArrayList<>();
        suitableForLists = new ArrayList<>();
        neighbourhoodList = new ArrayList<>();
        chaletSizeList = new ArrayList<>();
        soccerSizeList = new ArrayList<>();
        setTimePickerDataFrom();
        if (!TextUtils.isEmpty(activity.cityId)) {
            requestSearchOption(true);
            requestNeighbourhoodList(true, "");
        } else {
            PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.select_city_to_start_search), getString(R.string.ok), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.replaceFragment(ChangeLocationFragment.newInstance(), true, true, false, false);
                }
            });
        }
    }

    private void getBundleData() {
        if (getArguments() != null) {

        }
    }

    @Override
    @OnClick({R.id.tv_location, R.id.tv_neighbourhood, R.id.tv_leasing, R.id.tv_size, R.id.tv_amenities, R.id.tv_to_time, R.id.tv_from_time, R.id.tv_checkin_date, R.id.tv_sections, R.id.tv_suitable_for, R.id.rl_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_time:
                if (TextUtils.isEmpty(tvFromTime.getText().toString().trim())) {
                    CommonUtil.showSnackbar(getView(), getString(R.string.please_select_from_time));
                } else {
                    setTimePickerDataTo();
                    timePickerDialogto.show();
                }
                break;
            case R.id.tv_from_time:
                timePickerDialogfrom.show();
                break;
            case R.id.tv_checkin_date:
                setDatePickerData();
                break;
            case R.id.tv_location:
                onLocationEvent();
                break;
            case R.id.tv_neighbourhood:
                onNeighbourhoodEvent();
                break;
            case R.id.tv_leasing:
                onLeasingEvent();
                break;
            case R.id.tv_size:
                onSizeEvent();
                break;
            case R.id.tv_amenities:
                onAmenitiesEvent();
                break;
            case R.id.tv_sections:
                onSectionsEvent();
                break;
            case R.id.tv_suitable_for:
                onSuitableForEvent();
                break;
            case R.id.rl_search:
                onSearchEvent();
                break;
        }
    }

    private void onLocationEvent() {
        if (cityLocationList != null && cityLocationList.size() > 0) {
            PopupUtil.showSingleSelection(activity, getString(R.string.select_location), cityLocationList, null, null, null, (ISingleSelectionClick) this);
        } else {
            CommonUtil.showSnackbar(rootView, getString(R.string.no_location_found));
        }
    }

    private void onNeighbourhoodEvent() {
        //if (!TextUtils.isEmpty(location) && !location.equalsIgnoreCase("-1")) {
        if (neighbourhoodList != null && neighbourhoodList.size() > 0) {
            PopupUtil.showMultipleSelection(activity, getString(R.string.select_neighbourhood), neighbourhoodList, null, null, null, (IMultipleSelectionClick) this);
        } else {
            if (TextUtils.isEmpty(location) || location.equalsIgnoreCase("-1")) {
                CommonUtil.showSnackbar(rootView, getString(R.string.no_neighbourhood_found));
            } else {
                CommonUtil.showSnackbar(rootView, getString(R.string.no_neighbourhood_found_for_selected_location));
            }
        }
        //} else {
        //   CommonUtil.showSnackbar(rootView, getString(R.string.please_select_location_first));
        //}
    }

    private void onLeasingEvent() {
        if (chaletLeasingTypeLists != null && chaletLeasingTypeLists.size() > 0) {
            PopupUtil.showSingleSelection(activity, getString(R.string.select_leasing), null, chaletLeasingTypeLists, null, null, (ISingleSelectionClick) this);
        }
    }

    private void onSizeEvent() {
        if (property_type_id.equalsIgnoreCase("1")) {//single selection
            if (chaletSizeList != null && chaletSizeList.size() > 0) {
                PopupUtil.showSingleSelection(activity, getString(R.string.select_size), null, null, null, chaletSizeList, (ISingleSelectionClick) this);
            }
        } else {//multiple selection
            if (chaletSizeList != null && chaletSizeList.size() > 0) {
                PopupUtil.showMultipleSelection(activity, getString(R.string.size_number_of_players), null, null, null, soccerSizeList, (IMultipleSelectionClick) this);
            }
        }
    }

    private void onAmenitiesEvent() {
        if (amenitiesLists != null && amenitiesLists.size() > 0) {
            PopupUtil.showMultipleSelection(activity, getString(R.string.select_amenities), null, amenitiesLists, null, null, (IMultipleSelectionClick) this);
        }
    }


    private void onSectionsEvent() {
        if (sectionLists != null && sectionLists.size() > 0) {
            PopupUtil.showSingleSelection(activity, getString(R.string.select_sections), null, null, sectionLists, null, (ISingleSelectionClick) this);
        }
    }

    private void onSuitableForEvent() {
        if (suitableForLists != null && suitableForLists.size() > 0) {
            PopupUtil.showMultipleSelection(activity, getString(R.string.suitable_for), null, null, suitableForLists, null, (IMultipleSelectionClick) this);
        }
    }

    private void onSearchEvent() {
//        title = edtTitle.getText().toString().trim();
        title = "";
        max_price = edtMaxPrice.getText().toString();
        min_price = edtMinPrice.getText().toString();

        /*to = tvToTime.getText().toString();
        from = tvFromTime.getText().toString();
        try {
            SimpleDateFormat stp = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
            if (!TextUtils.isEmpty(to)) {
                Date date = stp.parse(to);
                to = stf.format(date);
            }
            if (!TextUtils.isEmpty(from)) {
                Date date = stp.parse(from);
                from = stf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Bundle bundle = new Bundle();
        bundle.putString("leasing_type", leasing_type);//
        bundle.putString("location", location);//
        bundle.putString("check_in", check_in);//
        bundle.putString("neighbourhood", neighbourhood);//
        bundle.putString("amenities", amenities);//
        bundle.putString("title", title);//
        bundle.putString("section", section);//
        bundle.putString("suitable_for", suitable_for);//
        bundle.putString("max_price", max_price);//
        bundle.putString("min_price", min_price);//
        bundle.putString("size", size);//
        bundle.putString("property_type_id", property_type_id);//
        bundle.putString("to", to);//
        bundle.putString("from", from);//
        bundle.putString("availibility", availibility);
        bundle.putString("lat", lat);
        bundle.putString("lng", lng);
        activity.replaceFragment(SearchResultFragment.newInstance(bundle), true, true, false, false);
    }

    @Override
    @OnCheckedChanged({R.id.rbDistance, R.id.rbRelevance, R.id.rbLowestPrice})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rbRelevance:
                sortBy(1);
                break;
            case R.id.rbDistance:
                sortBy(2);
                break;
            case R.id.rbLowestPrice:
                sortBy(3);
                break;
            default:
                break;
        }
    }

    // 1 - relevence 2 - distance 3 - lowest price
    private void sortBy(int sortType) {
        switch (sortType) {
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            default:
                break;
        }
    }

    private void onChaletEvent() {
        property_type_id = "1";
        svSearch.fullScroll(ScrollView.FOCUS_UP);
        svSearch.pageScroll(View.FOCUS_UP);
        llLeasing.setVisibility(View.VISIBLE);
        tvSizeTitle.setText(getString(R.string.size));
        llAmenities.setVisibility(View.VISIBLE);
        llSections.setVisibility(View.VISIBLE);
        llSuitableFor.setVisibility(View.VISIBLE);
        llTime.setVisibility(View.GONE);
        clearData();
        requestSearchOption(true);
    }

    private void clearData() {
//        edtTitle.setText("");
        tvLocation.setText("");
        tvNeighbourhood.setText("");
        tvLeasing.setText("");
        tvSize.setText("");
        tvAmenities.setText("");
        edtMaxPrice.setText("");
        edtMinPrice.setText("");
        tvSections.setText("");
        tvSuitableFor.setText("");
        tvCheckinDate.setText("");
        tvFromTime.setText("");
        tvToTime.setText("");
        //ids
        location = "";
        leasing_type = "";
        section = "";
        neighbourhood = "";
        check_in = "";
        amenities = "";
        suitable_for = "";
        size = "";
        //neighbourhoodList.clear();
        resetCityLocationListSelection();
        cityLocationList.clear();
        chaletLeasingTypeLists.clear();
        chaletSizeList.clear();
        soccerSizeList.clear();
        amenitiesLists.clear();
        sectionLists.clear();
        suitableForLists.clear();
    }

    private void requestSearchOption(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_TYPE_ID, property_type_id);
        params.put(WSUtils.KEY_CITYID, activity.cityId);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_SEARCH_OPTION);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_SEARCH_OPTION, this);
    }

    private void requestNeighbourhoodList(boolean showDefaultProgress, String locationId) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LOCATION_ID, locationId + "");
        params.put(WSUtils.KEY_CITYID, activity.cityId);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_NEIGHBOURHOOD_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_NEIGHBOURHOOD_LIST, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_SEARCH_OPTION:
                    parseSearchOption((SearchOptionModel) response);
                    break;
                case WSUtils.REQ_NEIGHBOURHOOD_LIST:
                    parseNeighbourhoodList((NeighbourhoodModel) response);
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

    private void parseSearchOption(SearchOptionModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                searchData = response.getData();
                cityLocationList = searchData.getCityLocation();
                chaletLeasingTypeLists = searchData.getChaletLeasingTypeList();
                chaletSizeList = searchData.getChaletSize();
                soccerSizeList = searchData.getSoccerSize();
                amenitiesLists = searchData.getAmenitiesList();
                sectionLists = searchData.getGetSectionList();
                suitableForLists = searchData.getSuitableForList();
                makeSelectedIdTrue();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void makeSelectedIdTrue() {
        if (cityLocationList != null && cityLocationList.size() > 0 && location != null) {
            for (int i = 0; i < cityLocationList.size(); i++) {
                cityLocationList.get(i).setSelected(cityLocationList.get(i).getLocationId().equalsIgnoreCase(location));
            }
        }
        if (chaletLeasingTypeLists != null && chaletLeasingTypeLists.size() > 0 && leasing_type != null) {
            for (int i = 0; i < chaletLeasingTypeLists.size(); i++) {
                chaletLeasingTypeLists.get(i).setSelected(chaletLeasingTypeLists.get(i).getChaletLeasingTypeId().equalsIgnoreCase(leasing_type));
            }
        }
        if (chaletSizeList != null && chaletSizeList.size() > 0 && size != null) {
            for (int i = 0; i < chaletSizeList.size(); i++) {
                chaletSizeList.get(i).setSelected(chaletSizeList.get(i).getValue().equalsIgnoreCase(size));
            }
        }
        //multiple selection....
        if (soccerSizeList != null && soccerSizeList.size() > 0 && size != null) {
            List<String> sizeIdList = Arrays.asList(size.split(","));
            for (int i = 0; i < soccerSizeList.size(); i++) {
                for (int j = 0; j < sizeIdList.size(); j++) {
                    if (soccerSizeList.get(i).getId().equalsIgnoreCase(sizeIdList.get(j).trim())) {
                        soccerSizeList.get(i).setSelected(true);
                    }
                }
            }
        }
        if (amenitiesLists != null && amenitiesLists.size() > 0 && amenities != null) {
            List<String> amenitiesIdList = Arrays.asList(amenities.split(","));
            for (int i = 0; i < amenitiesLists.size(); i++) {
                for (int j = 0; j < amenitiesIdList.size(); j++) {
                    if (amenitiesLists.get(i).getAmenityMasterId().equalsIgnoreCase(amenitiesIdList.get(j).trim())) {
                        amenitiesLists.get(i).setSelected(true);
                    }
                }
            }
        }
        if (sectionLists != null && sectionLists.size() > 0 && section != null) {
            for (int i = 0; i < sectionLists.size(); i++) {
                sectionLists.get(i).setSelected(sectionLists.get(i).getSectionId().equalsIgnoreCase(section));
            }
        }
        if (suitableForLists != null && suitableForLists.size() > 0 && suitable_for != null) {
            List<String> suitable_forIdList = Arrays.asList(suitable_for.split(","));
            for (int i = 0; i < suitableForLists.size(); i++) {
                for (int j = 0; j < suitable_forIdList.size(); j++) {
                    if (suitableForLists.get(i).getSuitableAmenityMasterId().equalsIgnoreCase(suitable_forIdList.get(j).trim())) {
                        suitableForLists.get(i).setSelected(true);
                    }
                }
            }
        }
    }

    private void parseNeighbourhoodList(NeighbourhoodModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                neighbourhoodList = response.getData();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }


    @Override
    public void onItemSelect(int position, String id, String title, String fromClickEvent) {
        if (fromClickEvent.equalsIgnoreCase("city")) {
            tvLocation.setText(title);
            if (!location.equalsIgnoreCase(id)) {
                requestNeighbourhoodList(true, id);
                tvNeighbourhood.setText("");
            }
            location = id;
        } else if (fromClickEvent.equalsIgnoreCase("leasing")) {
            tvLeasing.setText(title);
            leasing_type = id;
        } else if (fromClickEvent.equalsIgnoreCase("section")) {
            tvSections.setText(title);
            section = id;
        } else if (fromClickEvent.equalsIgnoreCase("size")) {
            tvSize.setText(title);
            size = id;
        }
    }

    @Override
    public void onNoneSelect(String fromClickEvent) {
        if (fromClickEvent.equalsIgnoreCase("city")) {
            tvLocation.setText("");
            tvNeighbourhood.setText("");
            location = "-1";
            resetCityLocationListSelection();
            requestNeighbourhoodList(true, "");
        } else if (fromClickEvent.equalsIgnoreCase("leasing")) {
            tvLeasing.setText("");
            leasing_type = "";
            resetLeasingListSelection();
        } else if (fromClickEvent.equalsIgnoreCase("section")) {
            tvSections.setText("");
            section = "";
            resetSectionListSelection();
        } else if (fromClickEvent.equalsIgnoreCase("size")) {
            tvSize.setText("");
            size = "";
            resetSizeListSelection();
        }
    }

    private void resetCityLocationListSelection() {
        if (cityLocationList != null && cityLocationList.size() > 0) {
            for (SearchOptionModel.CityLocation location : cityLocationList) {
                location.setSelected(false);
            }
        }
    }

    private void resetLeasingListSelection() {
        if (chaletLeasingTypeLists != null && chaletLeasingTypeLists.size() > 0) {
            for (SearchOptionModel.ChaletLeasingTypeList leasingTypeList : chaletLeasingTypeLists) {
                leasingTypeList.setSelected(false);
            }
        }
    }

    private void resetSectionListSelection() {
        if (sectionLists != null && sectionLists.size() > 0) {
            for (SearchOptionModel.GetSectionList section : sectionLists) {
                section.setSelected(false);
            }
        }
    }

    private void resetSizeListSelection() {
        if (chaletSizeList != null && chaletSizeList.size() > 0) {
            for (SearchOptionModel.ChaletSize size : chaletSizeList) {
                size.setSelected(false);
            }
        }
    }

    @Override
    public void OnNeighbourItemSelected(List<NeighbourhoodModel.Datum> selectedList) {
        StringBuilder builder = new StringBuilder();
        StringBuilder builderId = new StringBuilder();
        for (int i = 0; i < selectedList.size(); i++) {
            if (i < selectedList.size() - 1) {
                builder.append(selectedList.get(i).getName()).append(", ");
                builderId.append(selectedList.get(i).getId()).append(", ");
            } else {
                builder.append(selectedList.get(i).getName());
                builderId.append(selectedList.get(i).getId());
            }
        }
        tvNeighbourhood.setText(builder.toString());
        neighbourhood = builderId.toString();
    }

    @Override
    public void OnAmenityItemSelected(List<SearchOptionModel.AmenitiesList> selectedAmenitiesList) {
        StringBuilder builder = new StringBuilder();
        StringBuilder builderId = new StringBuilder();
        for (int i = 0; i < selectedAmenitiesList.size(); i++) {
            if (i < selectedAmenitiesList.size() - 1) {
                builder.append(selectedAmenitiesList.get(i).getAmenityName()).append(", ");
                builderId.append(selectedAmenitiesList.get(i).getAmenityMasterId()).append(", ");
            } else {
                builder.append(selectedAmenitiesList.get(i).getAmenityName());
                builderId.append(selectedAmenitiesList.get(i).getAmenityMasterId());
            }
        }
        tvAmenities.setText(builder.toString());
        amenities = builderId.toString();
    }

    @Override
    public void OnSuitableForItemSelected(List<SearchOptionModel.SuitableForList> selectedsuitableForLists) {
        StringBuilder builderName = new StringBuilder();
        StringBuilder builderId = new StringBuilder();
        for (int i = 0; i < selectedsuitableForLists.size(); i++) {
            if (i < selectedsuitableForLists.size() - 1) {
                builderName.append(selectedsuitableForLists.get(i).getSuitableAmenityName()).append(", ");
                builderId.append(selectedsuitableForLists.get(i).getSuitableAmenityMasterId()).append(", ");
            } else {
                builderName.append(selectedsuitableForLists.get(i).getSuitableAmenityName());
                builderId.append(selectedsuitableForLists.get(i).getSuitableAmenityMasterId());
            }

        }
        tvSuitableFor.setText(builderName.toString());
        suitable_for = builderId.toString();
    }

    @Override
    public void OnSoccerSizeItemSelected(List<SearchOptionModel.SoccerSize> selectedSoccerSizeLists) {
        StringBuilder builderName = new StringBuilder();
        StringBuilder builderId = new StringBuilder();
        for (int i = 0; i < selectedSoccerSizeLists.size(); i++) {
            if (i < selectedSoccerSizeLists.size() - 1) {
                builderName.append(selectedSoccerSizeLists.get(i).getName()).append(", ");
                builderId.append(selectedSoccerSizeLists.get(i).getId()).append(", ");
            } else {
                builderName.append(selectedSoccerSizeLists.get(i).getName());
                builderId.append(selectedSoccerSizeLists.get(i).getId());
            }

        }
        tvSize.setText(builderName.toString());
        size = builderId.toString();
    }

    private void setDatePickerData() {
        String inputTime = tvCheckinDate.getText().toString();
        if (!TextUtils.isEmpty(inputTime)) {
            String[] parts = inputTime.split("/");
            year = Integer.parseInt(parts[2]);
            month = Integer.parseInt(parts[1]) - 1;
            day = Integer.parseInt(parts[0]);
        } else {
            calendar = Calendar.getInstance();
            //calendar.add(Calendar.HOUR_OF_DAY, 24);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        //dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        dateFormatterNew = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvCheckinDate.setText(dateFormatter.format(newDate.getTime()));
                check_in = dateFormatterNew.format(newDate.getTime());
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.setTitle(getString(R.string.select_date));
        datePickerDialog.show();
    }

    private void setTimePickerDataFrom() {
        if (fromCalender == null) {
            fromCalender = Calendar.getInstance();
        }
        timePickerDialogfrom = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDate = Calendar.getInstance();
                //newDate.set(year, month, day);
                newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newDate.set(Calendar.MINUTE, minute);
                newDate.set(Calendar.SECOND, 0);
                fromTime = newDate.getTime();
                try {
                    String timeSet = new SimpleDateFormat("hh:mm aa").format(fromTime);
                    //timeSet = timeSet.replace("am", "AM").replace("pm", "PM");
                    tvFromTime.setText(timeSet);
                    fromCalender.setTime(newDate.getTime());
                    from = hourOfDay + ":" + minute;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //updateTime(newDate.get(Calendar.HOUR), newDate.get(Calendar.MINUTE), newDate.get(Calendar.AM_PM), tvFromTime);
                tvToTime.setText("");
                if (toCalender == null) {
                    toCalender = Calendar.getInstance();
                }
                toCalender.setTime(Calendar.getInstance().getTime());
            }
        }, fromCalender.get(Calendar.HOUR_OF_DAY), fromCalender.get(Calendar.MINUTE), false);
        timePickerDialogfrom.setTitle(getString(R.string.select_time));
    }

    private void setTimePickerDataTo() {
        if (toCalender == null) {
            toCalender = Calendar.getInstance();
        }
        timePickerDialogto = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDate = Calendar.getInstance();
                //newDate.set(year, month, day);
                newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newDate.set(Calendar.MINUTE, minute);
                newDate.set(Calendar.SECOND, 0);
                toTime = newDate.getTime();
                if (fromTime != null) {
                    try {
                        String timeSet = new SimpleDateFormat("hh:mm aa").format(toTime);
                        long diff = 0;
                        if (minute == 0 && hourOfDay == 0) {
                            Calendar newTempDate = Calendar.getInstance();
                            newTempDate.set(Calendar.HOUR_OF_DAY, 23);
                            newTempDate.set(Calendar.MINUTE, 59);
                            newTempDate.set(Calendar.SECOND, 0);
                            Date toTempTime = newTempDate.getTime();
                            diff = ((toTempTime.getTime() + 60000) - fromTime.getTime());//added 60000 for 1 minute time difference
                        } else {
                            diff = toTime.getTime() - fromTime.getTime();
                        }
                        long diffHours = diff / (60 * 60 * 1000);
                        if (diffHours < 1 || diffHours == 0) {
                            CommonUtil.showSnackbar(getView(), getString(R.string.minimum_slot_time_is_1_hour));
                        } else if (diffHours < 0) {
                            CommonUtil.showSnackbar(getView(), getString(R.string.booking_end_time_should_be_after_booking_start_time));
                        } else {
                            tvToTime.setText(timeSet);
                            toCalender.setTime(newDate.getTime());
                            to = hourOfDay + ":" + minute;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CommonUtil.showSnackbar(getView(), getString(R.string.please_select_from_time));
                }
            }
        }, toCalender.get(Calendar.HOUR_OF_DAY), toCalender.get(Calendar.MINUTE), false);

        /*timePickerDialogto = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day, hourOfDay, minute);
                toTime = newDate.getTime();
                if (fromTime != null) {
                    long diff = toTime.getTime() - fromTime.getTime();
                    long diffHours = diff / (60 * 60 * 1000);
                    if (diffHours < 1) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.minimum_slot_time_is_1_hour));
                    } else {
                        toCalender = Calendar.getInstance();
                        toCalender.setTime(newDate.getTime());
                        try {
                            String timeSet = new SimpleDateFormat("hh:mm aa").format(toTime);
                            //timeSet = timeSet.replace("am", "AM").replace("pm", "PM");
                            tvToTime.setText(timeSet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //updateTime(newDate.get(Calendar.HOUR), newDate.get(Calendar.MINUTE), newDate.get(Calendar.AM_PM), tvToTime);
                    }
                } else {
                    CommonUtil.showSnackbar(getView(), getString(R.string.please_select_from_time));
                }
            }
        }, toCalender.get(Calendar.HOUR), toCalender.get(Calendar.MINUTE), false);*/
        timePickerDialogto.setTitle(

                getString(R.string.select_time)

        );
    }

    private void updateTime(int hours, int mins, int am_pm, CustomTextView tvTime) {
        String timeSet = "";
        if (am_pm == 1) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }
        String hour = "";
        if (hours < 10) {
            hour = "0" + hours;
        } else {
            hour = hours + "";
        }
        String minutes = "";
        if (mins < 10) {
            minutes = "0" + mins;
        } else {
            minutes = mins + "";
        }
        String aTime = hour + ':' + minutes + " " + timeSet;
        tvTime.setText(aTime);
    }

}
