package com.weekend.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weekend.R;
import com.weekend.adapters.PropertyAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IPropertyClicks;
import com.weekend.interfaces.IReportClick;
import com.weekend.models.AbuseReasonListModel;
import com.weekend.models.AddToFavoriteModel;
import com.weekend.models.PropertyListModel;
import com.weekend.models.RemoveFavoriteModel;
import com.weekend.models.SaveAbuseReasonModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.AnimationUtil;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.ImageUtil;
import com.weekend.utils.Log;
import com.weekend.utils.MarshmallowPermissions;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.CustomTextView;
import com.weekend.views.Paginate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultFragment extends BaseFragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, MarshmallowPermissions.PermissionCallbacks, Paginate.Callbacks, IParser<WSResponse>, IPropertyClicks, IReportClick {
    public static final String TAG = SearchResultFragment.class.getSimpleName();

    @Bind(R.id.iv_search_close)
    ImageView ivSearchClose;
    @Bind(R.id.edt_search)
    CustomEditText edtSearch;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;
    @Bind(R.id.tv_switch)
    CustomTextView tvSwitch;
    @Bind(R.id.ll_bottombar)
    LinearLayout llBottombar;
    @Bind(R.id.rv_property_list)
    CustomRecyclerView rvPropertyList;
    @Bind(R.id.rl_map)
    RelativeLayout rlMap;
    @Bind(R.id.tv_no_result)
    CustomTextView tvNoResult;
    @Bind(R.id.rl_map_item)
    RelativeLayout rlMapItem;
    @Bind(R.id.iv_map_logo)
    ImageView ivMapLogo;
    @Bind(R.id.tv_map_price)
    CustomTextView tvMapPrice;
    @Bind(R.id.iv_map_report)
    ImageView ivMapReport;
    @Bind(R.id.iv_map_favorite)
    ImageView ivMapFavorite;
    @Bind(R.id.tv_map_rating)
    CustomTextView tvMapRating;
    @Bind(R.id.iv_map_verified)
    ImageView ivMapVerified;
    @Bind(R.id.iv_map_recommend)
    ImageView ivMapRecommend;
    @Bind(R.id.tv_map_address)
    CustomTextView tvMapAddress;
    @Bind(R.id.tv_map_title)
    CustomTextView tvMapTitle;
    private View rootView;
    private int selectedFavoritePosition;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private List<PropertyListModel.GetPropertyListing> propertyList;
    BroadcastReceiver onBackPress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (rlMap.getVisibility() == View.VISIBLE) {
                if (rlMapItem != null && rlMapItem.isShown()) {
                    rlMapItem.setVisibility(View.GONE);
                }
                switchToList();
            }
        }
    };
    private String propertyTypeId = "1";//Chalet=1, Soccer field=2
    //Google map
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private HashMap<Marker, Integer> markerMap;
    private int count = 0;
    private Handler locationHandler;
    private Runnable locationCheckThread;
    private PropertyAdapter propertyAdapter;
    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1;
    private String leasing_type, location, check_in, neighbourhood, amenities, title, section, suitable_for, max_price, min_price,
            size, to, from, availibility, lat, lng;
    private boolean tmpNextPage;
    private String propertyId;
    private View favoriteView;
    private List<AbuseReasonListModel.Datum> abuseReasonList;

    public SearchResultFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param b
     * @return A new instance of fragment ListViewFragment.
     */
    public static SearchResultFragment newInstance(Bundle b) {
        SearchResultFragment fragment = new SearchResultFragment();
        b.putString(Constants.BUNDLE_KEY_LEASING_TYPE, b.getString(Constants.BUNDLE_KEY_LEASING_TYPE));
        b.putString(Constants.BUNDLE_KEY_LOCATION, b.getString(Constants.BUNDLE_KEY_LOCATION));
        b.putString(Constants.BUNDLE_KEY_CHECK_IN, b.getString(Constants.BUNDLE_KEY_CHECK_IN));
        b.putString(Constants.BUNDLE_KEY_NEIGHBOURHOOD, b.getString(Constants.BUNDLE_KEY_NEIGHBOURHOOD));
        b.putString(Constants.BUNDLE_KEY_AMENITIES, b.getString(Constants.BUNDLE_KEY_AMENITIES));
        b.putString(Constants.BUNDLE_KEY_TITLE, b.getString(Constants.BUNDLE_KEY_TITLE));
        b.putString(Constants.BUNDLE_KEY_SECTION, b.getString(Constants.BUNDLE_KEY_SECTION));
        b.putString(Constants.BUNDLE_KEY_SUITABLE_FOR, b.getString(Constants.BUNDLE_KEY_SUITABLE_FOR));
        b.putString(Constants.BUNDLE_KEY_MAX_PRICE, b.getString(Constants.BUNDLE_KEY_MAX_PRICE));
        b.putString(Constants.BUNDLE_KEY_MIN_PRICE, b.getString(Constants.BUNDLE_KEY_MIN_PRICE));
        b.putString(Constants.BUNDLE_KEY_SIZE, b.getString(Constants.BUNDLE_KEY_SIZE));
        b.putString(Constants.BUNDLE_KEY_PROPERTY_TYPE_ID, b.getString(Constants.BUNDLE_KEY_PROPERTY_TYPE_ID));
        b.putString(Constants.BUNDLE_KEY_TO, b.getString(Constants.BUNDLE_KEY_TO));
        b.putString(Constants.BUNDLE_KEY_FROM, b.getString(Constants.BUNDLE_KEY_FROM));
        b.putString(Constants.BUNDLE_KEY_AVAILIBILITY, b.getString(Constants.BUNDLE_KEY_AVAILIBILITY));
        b.putString(Constants.BUNDLE_KEY_LAT, b.getString(Constants.BUNDLE_KEY_LAT));
        b.putString(Constants.BUNDLE_KEY_LNG, b.getString(Constants.BUNDLE_KEY_LNG));
        fragment.setArguments(b);
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
                rootView = inflater.inflate(R.layout.fragment_search_result, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(activity.getString(R.string.search_result), true, true, true, false, false, false, true, false, false,
                    false, false,true);
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(activity).registerReceiver(onBackPress, new IntentFilter("OnBackPress"));
    }

    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(onBackPress);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.mapView);
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if (fragment != null) {
                ft.remove(fragment).commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBundleData() {
        if (getArguments() != null) {
            leasing_type = getArguments().getString(Constants.BUNDLE_KEY_LEASING_TYPE);
            location = getArguments().getString(Constants.BUNDLE_KEY_LOCATION);
            check_in = getArguments().getString(Constants.BUNDLE_KEY_CHECK_IN);
            neighbourhood = getArguments().getString(Constants.BUNDLE_KEY_NEIGHBOURHOOD);
            amenities = getArguments().getString(Constants.BUNDLE_KEY_AMENITIES);
            title = getArguments().getString(Constants.BUNDLE_KEY_TITLE);
            section = getArguments().getString(Constants.BUNDLE_KEY_SECTION);
            suitable_for = getArguments().getString(Constants.BUNDLE_KEY_SUITABLE_FOR);
            max_price = getArguments().getString(Constants.BUNDLE_KEY_MAX_PRICE);
            min_price = getArguments().getString(Constants.BUNDLE_KEY_MIN_PRICE);
            size = getArguments().getString(Constants.BUNDLE_KEY_SIZE);
            propertyTypeId = getArguments().getString(Constants.BUNDLE_KEY_PROPERTY_TYPE_ID);
            to = getArguments().getString(Constants.BUNDLE_KEY_TO);
            from = getArguments().getString(Constants.BUNDLE_KEY_FROM);
            availibility = getArguments().getString(Constants.BUNDLE_KEY_AVAILIBILITY);
            lat = getArguments().getString(Constants.BUNDLE_KEY_LAT);
            lng = getArguments().getString(Constants.BUNDLE_KEY_LNG);
        }
    }

    private void initialize() {
        WeekendApplication.selectedFragment = TAG;
        localStorage = LocalStorage.getInstance(activity);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        if (propertyTypeId.equalsIgnoreCase("1")) {
            tvNoResult.setText(getString(R.string.no_chalets_found));
        } else {
            tvNoResult.setText(getString(R.string.no_soccer_fields_found));
        }
        setUpSearchView();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.mapView, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        propertyList = new ArrayList<>();
        rvPropertyList.setLayoutManager(new LinearLayoutManager(activity));
        setTheAdapter();
        rvPropertyList.setListPagination(this);
        pageIndex = 1;
        requestAbuseReasonList();
        requestPropertyList(true);
    }

    private void setUpSearchView() {
        edtSearch.requestFocus();
        edtSearch.setCursorVisible(true);
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (propertyAdapter != null) {

                    propertyList = propertyAdapter.onTextChange(s.toString().trim());

                    if (propertyList != null && propertyList.size() != 0) {
                        rvPropertyList.setVisibility(View.VISIBLE);
                        tvNoResult.setVisibility(View.GONE);
                    } else {
                        rvPropertyList.setVisibility(View.GONE);
                        tvNoResult.setVisibility(View.VISIBLE);
                        tvNoResult.setText(getString(R.string.no_properties_match_in_your_search));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    tmpNextPage = isNextPageAvailable;
                    isNextPageAvailable = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    isNextPageAvailable = tmpNextPage;
                }
            }
        });
    }

    @MarshmallowPermissions.AfterPermissionGranted(MarshmallowPermissions.RUNTIME_LOCATION_PERMISSION)
    private boolean checkLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (MarshmallowPermissions.hasPermissions(activity, perms)) {
            // Have permissions, do the thing!
            return true;
        } else {
            // Ask for both permissions
            MarshmallowPermissions.requestPermissions(this, getString(R.string.rationale_location), MarshmallowPermissions.RUNTIME_LOCATION_PERMISSION, perms);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // MarshmallowPermissions handles the request result.
        MarshmallowPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        if (requestCode == MarshmallowPermissions.RUNTIME_LOCATION_PERMISSION) {
            locationRequest();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied permissions and checked NEVER ASK AGAIN.
        // This will display a dialog directing them to enable the permission in app settings.
        MarshmallowPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.rationale_ask_again), R.string.action_settings, R.string.action_cancel, perms);
    }

    @Override
    @OnClick({R.id.ll_bottombar, R.id.iv_search_close, R.id.rl_map_detail_item, R.id.iv_map_favorite, R.id.iv_map_close, R.id.iv_map_report})
    public void onClick(View v) {
        StaticUtil.hideSoftKeyboard(activity);
        switch (v.getId()) {
            case R.id.ll_bottombar:
                if (tvSwitch.getText().toString().equalsIgnoreCase(getString(R.string.switch_to_list_view))) {
                    switchToList();
                } else {
                    if (checkLocationPermission()) {
                        locationRequest();
                    }
                }
                break;
            case R.id.iv_search_close:
                if (edtSearch.getText().toString().length() > 0) {
                    edtSearch.setText("");
                    StaticUtil.hideSoftKeyboard(activity);
                }
                break;
            case R.id.rl_map_detail_item:
                onMapItemEvent();
                break;
            case R.id.iv_map_favorite:
                onFavoriteEvent(v);
                break;
            case R.id.iv_map_close:
                AnimationUtil.closeSlider(rlMapItem, true);
                break;
            case R.id.iv_map_report:
                if (selectedFavoritePosition > -1) {
                    onReportClick(selectedFavoritePosition);
                }
                break;
        }
    }

    private void switchToMap() {
        WeekendApplication.selectedFragment = TAG + "-MapView";
        activity.setTopbar(activity.getString(R.string.map_view), true, true, true, false, false, false, true, false, false,
                false, false, true);
        rlMap.setVisibility(View.VISIBLE);
        rvPropertyList.setVisibility(View.GONE);
        tvSwitch.setText(getString(R.string.switch_to_list_view));
        tvSwitch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.btn_list_btm, 0);
        tvNoResult.setVisibility(View.GONE);
        setMarker();
    }

    private void switchToList() {
        WeekendApplication.selectedFragment = TAG;
        activity.setTopbar(activity.getString(R.string.search_result), true, true, true, false, false, false, true, false,
                false, false, false, true);
        rlMap.setVisibility(View.GONE);
        rvPropertyList.setVisibility(View.VISIBLE);
        tvSwitch.setText(getString(R.string.switch_to_map_view));
        tvSwitch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.btn_map_btm, 0);
        if (propertyList == null || propertyList.size() == 0) {
            tvNoResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadMore() {
        isLoading = true;
        requestPropertyList(false);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        if (isNextPageAvailable) {
            return false;
        }
        /*if next page not available means we loaded all items so return true */
        return true;
    }


    private void setTheAdapter() {
        propertyAdapter = new PropertyAdapter(activity, propertyList, (IPropertyClicks) this);
        rvPropertyList.setAdapter(propertyAdapter);
    }

    @Override
    public void onItemClick(int position) {
        rvPropertyList.setVisibility(View.GONE);
        if (propertyTypeId.equalsIgnoreCase("1")) {
            activity.replaceFragment(ChaletParentDetailsFragment.newInstance(propertyList.get(position).getPropertyId(), check_in != null ? check_in : ""), true, true, false, false);
        } else {
            activity.replaceFragment(SoccerFieldParentDetailsFragment.newInstance(propertyList.get(position).getPropertyId(), check_in != null ? check_in : ""), true, true, false, false);
        }
    }

    @Override
    public void onFavoriteClick(int position) {
        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            selectedFavoritePosition = position;
            if (propertyList.get(position).getIsFavorite().equalsIgnoreCase("Yes")) {
                requestRemoveFavorite(propertyList.get(position).getPropertyId());
            } else {
                requestAddToFavorite(propertyList.get(position).getPropertyId());
            }
        } else {
            propertyAdapter.notifyDataSetChanged();
            activity.manageGuestUserAction(getString(R.string.please_login_first_to_add_or_remove_favorite));
        }
    }

    @Override
    public void onReportClick(int position) {
        propertyId = propertyList.get(position).getPropertyId();
        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
            PopupUtil.showReport(activity, name, abuseReasonList, (IReportClick) this);
        } else {
            propertyAdapter.notifyDataSetChanged();
            activity.manageGuestUserAction(getString(R.string.please_login_first_to_submit_report));
        }
    }

    @Override
    public void onReportSubmitClick(String comment, String name, String reasonId) {
        requestSaveAbuseReason(comment, name, reasonId);
    }

    private void requestPropertyList(boolean showDefaultProgress) {

        try {
            if (showDefaultProgress) {
                activity.showHideProgress(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
            params.put(WSUtils.KEY_PAGE_INDEX, pageIndex + "");
            params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
            params.put(WSUtils.KEY_LEASING_TYPE, leasing_type);
            params.put(WSUtils.KEY_LOCATION, location);
            params.put(WSUtils.KEY_CHECK_IN, check_in);
            params.put(WSUtils.KEY_NEIGHBOURHOOD, neighbourhood);
            params.put(WSUtils.KEY_AMENITIES, amenities);
            params.put(WSUtils.KEY_TITLE, title);
            params.put(WSUtils.KEY_SECTION, section);
            params.put(WSUtils.KEY_SUITABLE_FOR, suitable_for);
            params.put(WSUtils.KEY_MAX_PRICE, max_price);
            params.put(WSUtils.KEY_MIN_PRICE, min_price);
            params.put(WSUtils.KEY_SIZE, size);
            params.put(WSUtils.KEY_PROPERTY_TYPE_ID, propertyTypeId);
            params.put(WSUtils.KEY_TO, to/*URLEncoder.encode(to, "UTF-8")*/);
            params.put(WSUtils.KEY_FROM, from/*URLEncoder.encode(from, "UTF-8")*/);
            params.put(WSUtils.KEY_AVAILIBILITY, availibility);
            params.put(WSUtils.KEY_LAT, lat);
            params.put(WSUtils.KEY_LNG, lng);
            params.put(WSUtils.KEY_CITYID, activity.cityId);
            if (!TextUtils.isEmpty(check_in)) {
                params.put(WSUtils.KEY_SCHEDULE, "Yes");
                params.put(WSUtils.KEY_AVAILIBILITY, "All");
            }

            WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_PROPERTY_LIST);
            wsUtils.WSRequest(activity, params, null, WSUtils.REQ_PROPERTY_LIST, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestAddToFavorite(String propertyID) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USERID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTYID, propertyID);
        params.put(WSUtils.KEY_PROPERTYTYPEID, propertyTypeId);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_ADD_TO_FAVORITE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_ADD_TO_FAVORITE, this);
    }

    private void requestRemoveFavorite(String propertyID) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USERID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTYID, propertyID);
        params.put(WSUtils.KEY_PROPERTYTYPEID, propertyTypeId);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_REMOVE_FAVORITE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_REMOVE_FAVORITE, this);
    }

    private void requestAbuseReasonList() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_ABUSE_REASON_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_ABUSE_REASON_LIST, this);
    }

    private void requestSaveAbuseReason(String comment, String name, String reasonId) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_ID, propertyId);
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_POSTEDBY_NAME, name);
        params.put(WSUtils.KEY_ABUSE_REASON_ID, reasonId);
        params.put(WSUtils.KEY_COMMENT, comment);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_SAVE_ABUSE_REASON);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_SAVE_ABUSE_REASON, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_PROPERTY_LIST:
                    parsePropertyList((PropertyListModel) response);
                    break;
                case WSUtils.REQ_ADD_TO_FAVORITE:
                    parseAddToFavorite((AddToFavoriteModel) response);
                    break;
                case WSUtils.REQ_REMOVE_FAVORITE:
                    parseRemoveFavorite((RemoveFavoriteModel) response);
                    break;
                case WSUtils.REQ_ABUSE_REASON_LIST:
                    parseAbuseReasonList((AbuseReasonListModel) response);
                    break;
                case WSUtils.REQ_SAVE_ABUSE_REASON:
                    parseSaveAbuseReason((SaveAbuseReasonModel) response);
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
        if (isAdded()) {
            CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
            activity.showHideProgress(false);
        }
    }

    private void parsePropertyList(PropertyListModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (response.getSettings().getNextPage().equalsIgnoreCase("1")) {
                    if (response.getData() != null) {
                        isNextPageAvailable = true;
                        pageIndex = pageIndex + 1;
                    }
                } else {
                    isNextPageAvailable = false;
                }

                if (pageIndex == 1 && propertyList != null) {
                    propertyList.clear();
                }

                isLoading = false;

                if (response.getData() != null && response.getData().getGetPropertyListing().size() > 0) {
                    if (propertyList == null) {
                        propertyList = new ArrayList<>();
                    }
                    propertyList.addAll(response.getData().getGetPropertyListing());
                    propertyAdapter.addItems(propertyList);
                    showHideSearchResultList(true);
                } else {
                    showHideSearchResultList(false);
                }
            } else {
                showHideSearchResultList(false);
            }
            if (propertyAdapter != null) {
                propertyAdapter.notifyDataSetChanged();
            }
            if (rlMap != null && rlMap.getVisibility() == View.VISIBLE) {
                WeekendApplication.selectedFragment = TAG + "-MapView";
                setMarker();
            }
        }
    }

    private void showHideSearchResultList(boolean isShow) {
        if (isShow) {
            tvNoResult.setVisibility(View.GONE);
            rvPropertyList.setVisibility(View.VISIBLE);
            llBottombar.setVisibility(View.VISIBLE);
            rlSearch.setVisibility(View.VISIBLE);
        } else {
            tvNoResult.setVisibility(View.VISIBLE);
            rvPropertyList.setVisibility(View.GONE);
            llBottombar.setVisibility(View.GONE);
            rlSearch.setVisibility(View.GONE);
        }
    }

    private void parseAddToFavorite(AddToFavoriteModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                propertyList.get(selectedFavoritePosition).setIsFavorite("Yes");
                propertyAdapter.notifyDataSetChanged();
                if (favoriteView != null) {
                    favoriteView.setSelected(!favoriteView.isSelected());
                }
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void parseRemoveFavorite(RemoveFavoriteModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                propertyList.get(selectedFavoritePosition).setIsFavorite("No");
                propertyAdapter.notifyDataSetChanged();
                if (favoriteView != null) {
                    favoriteView.setSelected(!favoriteView.isSelected());
                }
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void parseAbuseReasonList(AbuseReasonListModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                abuseReasonList = response.getData();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            }
        }
        addBlankSelectionAbuseReason();
        activity.showHideProgress(false);
    }

    private void parseSaveAbuseReason(SaveAbuseReasonModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage());
            }
        }
        activity.showHideProgress(false);
    }

    private void addBlankSelectionAbuseReason() {
        AbuseReasonListModel.Datum tempModel = new AbuseReasonListModel.Datum();
        tempModel.setAbuseReasonMasterId("");
        tempModel.setLangCode(getString(R.string.lang_id));
        tempModel.setReasonTitle(getString(R.string.please_select_reason));
        if (abuseReasonList == null) {
            abuseReasonList = new ArrayList<>();
        }

        abuseReasonList.add(0, tempModel);
    }

    /***************
     * Location services
     *****************/

    public void locationRequest() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setFastestInterval(2000);
        mLocationRequest.setSmallestDisplacement(10); // 10 meters
        mLocationRequest.setInterval(60 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        checkLocationFound();
                        switchToMap();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(getActivity(), Constants.REQUEST_LOCATION_SEARCHRESULT_FRAGMENT);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    private void checkLocationFound() {
        locationHandler = new Handler();
        locationCheckThread = new Runnable() {
            @Override
            public void run() {
                count++;
                if (mLastLocation == null) {
                    if (count < 5) {
                        getUserLocation();
                    } else {
                        count = 0;
                        //switchToMap();
                    }
                    Log.e(TAG, "checkLocationFound " + count);
                } else {
                    getUserLocation();
                }
            }
        };
        locationHandler.postDelayed(locationCheckThread, 500);
    }

    private void getUserLocation() {
        if (mGoogleMap != null) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation == null && count < 5) {
            checkLocationFound();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case Constants.REQUEST_LOCATION_SEARCHRESULT_FRAGMENT:
                    if (resultCode == Activity.RESULT_OK) {
                        checkLocationFound();
                    } else {
                        CommonUtil.showSnackbar(getView(), getString(R.string.enable_location_service_to_get_location));
                    }
                    switchToMap();
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setMarker() {
        if (mGoogleMap != null) {
            mGoogleMap.clear();
            markerMap = new HashMap<>();
            if (propertyList != null && propertyList.size() > 0) {
                for (int i = 0; i < propertyList.size(); i++) {
                    Marker marker;
                    PropertyListModel.GetPropertyListing property = propertyList.get(i);
                    if (propertyTypeId.equalsIgnoreCase("1")) {//chalet
                        int icon = R.mipmap.icon_chalet;
                        if (property.getMarkAsRecommended().equalsIgnoreCase("Yes") && property.getMarkAsVerified().equalsIgnoreCase("Yes")) {
                            icon = R.mipmap.icon_chalet_recommanded;
                        } else if (property.getMarkAsRecommended().equalsIgnoreCase("Yes")) {
                            icon = R.mipmap.icon_chalet_recommanded;
                        } else if (property.getMarkAsVerified().equalsIgnoreCase("Yes")) {
                            icon = R.mipmap.icon_chalet_verified;
                        }
                        marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.valueOf(propertyList.get(i).getLatitude()), Double.valueOf(propertyList.get(i).getLongitude())))
                                .icon(BitmapDescriptorFactory.fromResource(icon)));

                    } else {//soccer field
                        int icon = R.mipmap.icon_soccer_field;
                        if (property.getMarkAsRecommended().equalsIgnoreCase("Yes") && property.getMarkAsVerified().equalsIgnoreCase("Yes")) {
                            icon = R.mipmap.icon_soccer_field_recommanded;
                        } else if (property.getMarkAsRecommended().equalsIgnoreCase("Yes")) {
                            icon = R.mipmap.icon_soccer_field_recommanded;
                        } else if (property.getMarkAsVerified().equalsIgnoreCase("Yes")) {
                            icon = R.mipmap.icon_soccer_field_verified;
                        }
                        marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.valueOf(propertyList.get(i).getLatitude()), Double.valueOf(propertyList.get(i).getLongitude())))
                                .icon(BitmapDescriptorFactory.fromResource(icon)));
                    }
                    if (marker != null) {
                        markerMap.put(marker, i);
                    }
                }
                boundMarkers();
            }
        }
    }

    private void boundMarkers() {
        if (markerMap != null && markerMap.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            Iterator itr = markerMap.keySet().iterator();
            while (itr.hasNext()) {
                Marker marker = (Marker) itr.next();
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            /**call the map call back to know map is loaded or not*/
            //http://www.androidhub4you.com/2015/06/android-maximum-zoom-in-google-map.html
            mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    /**set animated zoom camera into map*/
                    mGoogleMap.animateCamera(cu);
                }
            });
        } else {
            if (mLastLocation != null) {
                LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLocation)      // Sets the center of the map to Mountain View
                        .zoom(10)                   // Sets the zoom
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                //mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        if (mGoogleMap != null) {
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (markerMap != null && markerMap.size() > 0) {
            int propertyListPosition = markerMap.get(marker);
            if (propertyListPosition != -1) {
                selectedFavoritePosition = propertyListPosition;
                PropertyListModel.GetPropertyListing property = propertyList.get(propertyListPosition);
                ImageUtil.loadPropertyImage(activity, property.getPiImage(), ivMapLogo);
                if (!TextUtils.isEmpty(property.getAvgRating()) && Math.round(Float.valueOf(property.getAvgRating())) > 0) {
                    tvMapRating.setText(property.getAvgRating());
                    tvMapRating.setVisibility(View.VISIBLE);
                } else {
                    tvMapRating.setVisibility(View.GONE);
                }
                ivMapFavorite.setSelected(property.getIsFavorite().equalsIgnoreCase("Yes"));
                tvMapTitle.setText(property.getPropertyTitle());
                tvMapAddress.setText(property.getAddress());
                tvMapPrice.setText(String.format("%s %s", property.getPrice(), getString(R.string.currency)));
                ivMapRecommend.setVisibility(property.getMarkAsRecommended().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
                ivMapVerified.setVisibility(property.getMarkAsVerified().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
                ivMapReport.setVisibility(View.VISIBLE/*property.getMarkAsFeatured().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE)*/);
                propertyId = property.getPropertyId();
            }
        }
        AnimationUtil.openSlider(rlMapItem, true);
        return false;
    }

    private void onMapItemEvent() {
        AnimationUtil.closeSlider(rlMapItem, true);
        if (propertyTypeId.equalsIgnoreCase("1")) {
            activity.replaceFragment(ChaletParentDetailsFragment.newInstance(propertyId, check_in != null ? check_in : ""), true, true, false, false);
        } else {
            activity.replaceFragment(SoccerFieldParentDetailsFragment.newInstance(propertyId, check_in != null ? check_in : ""), true, true, false, false);
        }
    }

    private void onFavoriteEvent(View favorite) {
        favoriteView = favorite;
        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            if (favorite.isSelected()) {
                requestRemoveFavorite(propertyId);
            } else {
                requestAddToFavorite(propertyId);
            }
        } else {
            propertyAdapter.notifyDataSetChanged();
            activity.manageGuestUserAction(getString(R.string.please_login_first_to_add_or_remove_favorite));
        }
    }

    /**************** Google map***************************/

    /**
     * Callback called when connected to GCore..
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, REQUEST, this);  // LocationListener
    }

    /**
     * Callback called when disconnected from GCore..
     */
    @Override
    public void onConnectionSuspended(int cause) {
        // Do nothing
    }

    /**
     * Implementation of OnConnectionFailedListener.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do nothing
    }

}
