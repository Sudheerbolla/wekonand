/*
package com.weekend.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eftimoff.viewpagertransformers.AccordionTransformer;
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
import com.weekend.adapters.ImagesPagerAdapter;
import com.weekend.adapters.PropertyAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IPagerItemClicked;
import com.weekend.interfaces.IPropertyClicks;
import com.weekend.interfaces.IReportClick;
import com.weekend.models.AbuseReasonListModel;
import com.weekend.models.AddToFavoriteModel;
import com.weekend.models.CityModel;
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
import com.weekend.utils.Log;
import com.weekend.utils.MarshmallowPermissions;
import com.weekend.utils.PopupUtil;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.CustomTextView;
import com.weekend.views.Paginate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListViewFragmentbc extends BaseFragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, MarshmallowPermissions.PermissionCallbacks, Paginate.Callbacks, IParser<WSResponse>, IPropertyClicks, IReportClick, IPagerItemClicked {
    public static final String TAG = ListViewFragmentbc.class.getSimpleName();
    @Bind(R.id.tv_switch)
    TextView tvSwitch;
    @Bind(R.id.rg_soccer_chalet)
    RadioGroup rgSoccerChalet;
    @Bind(R.id.rb_soccer_fields)
    RadioButton rbSoccerFields;
    @Bind(R.id.rb_chalet)
    RadioButton rbChalet;
    @Bind(R.id.ll_tab_selection)
    LinearLayout llTabSelection;
    @Bind(R.id.v_chalets)
    View vChalets;
    @Bind(R.id.v_soccer_fields)
    View vSoccerFields;
    @Bind(R.id.rv_list)
    CustomRecyclerView rvList;
    @Bind(R.id.fl_map)
    FrameLayout flMap;
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
    View favoriteView;
    @Bind(R.id.view_pager_chalet)
    ViewPager viewPagerChalet;
    @Bind(R.id.imgPlaceHolder)
    ImageView imgPlaceHolder;

    @Bind(R.id.tv_calender)
    CustomTextView tvCalender;
    @Bind(R.id.iv_close)
    ImageView iv_close;

    PopupWindow filterWindow, sortWindow;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private List<PropertyListModel.GetPropertyListing> propertyList;
    BroadcastReceiver onBackPress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (flMap.getVisibility() == View.VISIBLE) {
                if (rlMapItem != null && rlMapItem.isShown()) {
                    rlMapItem.setVisibility(View.GONE);
                }
                switchToList();
            }
        }
    };
    private PropertyAdapter propertyAdapter;
    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1;
    private String propertyTypeId = "1";//Chalet=1, Soccer field=2
    private String propertyId;
    private int selectedFavoritePosition;
    private String sort_type = "", availibility = "All";
    private String filterAll, filterAvailable, filterUnavailable;
    private boolean isShowWithScheduleAvailable;
    private List<AbuseReasonListModel.Datum> abuseReasonList;
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


    // For calender search
    private SimpleDateFormat dateFormatter, dateFormatterNew;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, day;
    private String checkIn = "";

    public ListViewFragmentbc() {

    }

    */
/**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListViewFragment.
     *//*

    public static ListViewFragmentbc newInstance() {
        ListViewFragmentbc fragment = new ListViewFragmentbc();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView");
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_list_view_bc, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(activity.getString(R.string.list_view), true, true, false, false, false, true, true, true, true, false, false);
            initialize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (mGoogleApiClient != null)
//            mGoogleApiClient.connect();
        LocalBroadcastManager.getInstance(activity).registerReceiver(onBackPress, new IntentFilter("OnBackPress"));
    }

    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        Log.e(TAG, "onStop");
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (filterWindow != null && filterWindow.isShowing()) {
            filterWindow.dismiss();
        }
        if (sortWindow != null && sortWindow.isShowing()) {
            sortWindow.dismiss();
        }
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(onBackPress);
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.disconnect();
//        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView");
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

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        WeekendApplication.selectedFragment = TAG;
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

        if (!TextUtils.isEmpty(activity.cityId)) {
            pageIndex = 1;
            requestPropertyList(true);
        } else if (checkLocationPermission()) {
            if (isAdded())
                locationRequest();
            else
                activity.hideSplashScreen();
        }

        if (TextUtils.isEmpty(activity.cityId)) {
            rvList.setVisibility(View.GONE);
            flMap.setVisibility(View.GONE);
            tvNoResult.setVisibility(View.VISIBLE);
        }

        //Updating Drawer menu UI as per guest or logged in user
//        if (!localStorage.getBoolean(Constants.SP_KEY_AUTO_LOGIN, true)) {
//            localStorage.putString(Constants.SP_KEY_USER_ID, "");
//        }
        requestAbuseReasonList();
        activity.updateUI(!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, "")));
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.mapView, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        propertyList = new ArrayList<>();
        rvList.setLayoutManager(new LinearLayoutManager(activity));
        rbChalet.setChecked(true);
        setTheAdapter();
        setListeners();
        CommonUtil.setTypefacerb(activity, rbChalet, 6);
        CommonUtil.setTypefacerb(activity, rbSoccerFields, 6);
    }

    private void setListeners() {
        activity.ivNavbarFilter.setOnClickListener(this);
        activity.ivNavbarSorting.setOnClickListener(this);
        rvList.setListPagination(this);
    }

    @MarshmallowPermissions.AfterPermissionGranted(MarshmallowPermissions.RUNTIME_LOCATION_PERMISSION)
    private boolean checkLocationPermission() {
        boolean hasPermission = false;
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (MarshmallowPermissions.hasPermissions(activity, perms)) {
            // Have permissions, do the thing!
            hasPermission = true;
        } else {
            // Ask for both permissions
            MarshmallowPermissions.requestPermissions(this, getString(R.string.rationale_location), MarshmallowPermissions.RUNTIME_LOCATION_PERMISSION, perms);
            hasPermission = false;
        }
        return hasPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MarshmallowPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == MarshmallowPermissions.RUNTIME_LOCATION_PERMISSION) {
            locationRequest();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // (Optional) Check whether the user denied permissions and checked NEVER ASK AGAIN.
        // This will display a dialog directing them to enable the permission in app settings.
        MarshmallowPermissions.checkDeniedPermissionsNeverAskAgain(this, getString(R.string.rationale_ask_again), R.string.action_settings, R.string.action_cancel, perms);
        if (requestCode == MarshmallowPermissions.RUNTIME_LOCATION_PERMISSION) {
            navigateToChangeLocation();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case Constants.REQUEST_LOCATION_LISTVIEW_FRAGMENT:
                    if (resultCode == Activity.RESULT_OK) {
                        showProgressDialog(getString(R.string.fetching_location), false);
                        checkLocationFound();
                    } else {
                        CommonUtil.showSnackbar(getView(), getString(R.string.enable_location_service_to_get_location));
                        navigateToChangeLocation();
                    }
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void locationRequest() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (mGoogleApiClient != null && (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected())) {
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
                if (isAdded()) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
//                            showProgressDialog(getString(R.string.fetching_location), false);
                            checkLocationFound();
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(activity, Constants.REQUEST_LOCATION_LISTVIEW_FRAGMENT);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            navigateToChangeLocation();
                            break;
                    }
                } else {
                    activity.hideSplashScreen();
                    hideProgressDialog();
                    navigateToChangeLocation();
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
                        hideProgressDialog();
                        PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.cant_get_location_please_select_city), getString(R.string.allow), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                navigateToChangeLocation();
                            }
                        });
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
        if (mLastLocation != null) {
            Log.e("Location", mLastLocation.toString());
            getUserCity();
        } else {
            if (count < 5) {
                checkLocationFound();
            }
        }
    }

    private void getUserCity() {
        Geocoder gcd = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            if (gcd != null && mLastLocation != null) {
                addresses = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
            }
        } catch (IOException e) {
            hideProgressDialog();
            e.printStackTrace();
            PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.cant_get_location_please_select_city), getString(R.string.allow), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToChangeLocation();
                }
            });
            return;
        }

        if (addresses != null && addresses.size() > 0) {
            Log.e("City", addresses.get(0).getLocality());
            if (locationHandler != null && locationCheckThread != null) {
                locationHandler.removeCallbacks(locationCheckThread);
            }
            activity.currentCity = addresses.get(0).getLocality();
            hideProgressDialog();
            requestCityList(addresses.get(0).getLocality());
        } else {
            hideProgressDialog();
            PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.cant_get_location_please_select_city), getString(R.string.allow), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToChangeLocation();
                }
            });
        }
    }

    @Override
    @OnClick({R.id.view_pager_chalet, R.id.ll_bottombar, R.id.iv_map_close, R.id.rl_map_detail_item, R.id.iv_map_favorite, R.id.rb_chalet, R.id.rb_soccer_fields, R.id.iv_map_report, R.id.iv_close, R.id.tv_calender})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_navbar_filter:
                onFilteringEvent();
                break;
            case R.id.iv_navbar_sorting:
                onSortingEvent();
                break;
            case R.id.ll_bottombar:
                if (tvSwitch.getText().toString().equalsIgnoreCase(getString(R.string.switch_to_list_view))) {
                    switchToList();
                } else {
                    switchToMap();
                }
                break;
            case R.id.iv_map_close:
                AnimationUtil.closeSlider(rlMapItem, true);
                break;
            case R.id.rl_map_detail_item:
//                onMapItemEvent();
                break;
            case R.id.iv_map_favorite:
                onFavoriteEvent(v);
                break;
            case R.id.rb_chalet:
                onCheckChange();
                break;
            case R.id.rb_soccer_fields:
                onCheckChange();
                break;
            case R.id.iv_map_report:
                if (selectedFavoritePosition > -1) {
                    onReportClick(selectedFavoritePosition);
                }
                break;
            case R.id.iv_close:
                pageIndex = 1;
                checkIn = "";
                tvCalender.setText("");
                requestPropertyList(true);
                break;
            case R.id.tv_calender:
                if (TextUtils.isEmpty(activity.cityId)) {
                    PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.cant_get_location_please_select_city), getString(R.string.allow), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            navigateToChangeLocation();
                        }
                    });
                } else {
                    setDatePickerData();
                }
                break;

        }
    }

    private void onFilteringEvent() {
        filterWindow = PopupUtil.showFilterPopup(activity, activity.ivNavbarFilter, filterAll, filterAvailable, filterUnavailable, isShowWithScheduleAvailable,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        availibility = "All";
                        pageIndex = 1;
                        propertyList.clear();
                        requestPropertyList(true);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        availibility = "Yes";
                        isShowWithScheduleAvailable = true;//On availability show with schedule will be enable by default
                        pageIndex = 1;
                        propertyList.clear();
                        requestPropertyList(true);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        availibility = "No";
                        pageIndex = 1;
                        propertyList.clear();
                        requestPropertyList(true);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomTextView tvClicked = (CustomTextView) v;
                        tvClicked.setSelected(!tvClicked.isSelected());
                        isShowWithScheduleAvailable = tvClicked.isSelected();
                        pageIndex = 1;
                        propertyList.clear();
                        requestPropertyList(true);
                    }
                });
    }

    private void onSortingEvent() {
        sortWindow = PopupUtil.showSortPopup(activity, activity.ivNavbarSorting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_type = */
/*"default"*//*
"";
                pageIndex = 1;
                propertyList.clear();
                requestPropertyList(true);
                //SortPriceRelevance
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_type = */
/*"low-high"*//*
"price asc";
                pageIndex = 1;
                propertyList.clear();
                requestPropertyList(true);
                //SortPriceLowToHigh
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_type = */
/*"high-low"*//*
"price desc";
                pageIndex = 1;
                propertyList.clear();
                requestPropertyList(true);
                //SortPriceHighToLow
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort_type = */
/*"nearest_first"*//*
"distance asc";
                pageIndex = 1;
                propertyList.clear();
                requestPropertyList(true);
                //SortNearestFirst
            }
        });
    }

    private void onMapItemEvent() {
        AnimationUtil.closeSlider(rlMapItem, true);
        if (rgSoccerChalet.getCheckedRadioButtonId() == R.id.rb_chalet) {
            activity.replaceFragment(ChaletParentDetailsFragment.newInstance(propertyId, ""), true, true, false, false);
        } else {
            activity.replaceFragment(SoccerFieldParentDetailsFragment.newInstance(propertyId, ""), true, true, false, false);
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

    private void onCheckChange() {
        if (propertyList != null && propertyList.size() > 0) {
            rvList.scrollToPosition(0);
        }
        if (rbChalet.isChecked()) {
            vChalets.setVisibility(View.VISIBLE);
            vSoccerFields.setVisibility(View.INVISIBLE);
            tvNoResult.setText(R.string.no_chalets_found);
            pageIndex = 1;
            propertyTypeId = "1";
            if (propertyList != null) {
                propertyList.clear();
            }
            rvList.setVisibility(View.GONE);
            requestPropertyList(true);
        } else if (rbSoccerFields.isChecked()) {
            vChalets.setVisibility(View.INVISIBLE);
            vSoccerFields.setVisibility(View.VISIBLE);
            tvNoResult.setText(R.string.no_soccer_fields_found);
            pageIndex = 1;
            propertyTypeId = "2";
            if (propertyList != null) {
                propertyList.clear();
            }
            rvList.setVisibility(View.GONE);
            requestPropertyList(true);
        }
    }

    private void switchToMap() {
        WeekendApplication.selectedFragment = TAG + "-MapView";
        flMap.setVisibility(View.VISIBLE);
        llTabSelection.setVisibility(View.GONE);
        rvList.setVisibility(View.GONE);
        tvSwitch.setText(getString(R.string.switch_to_list_view));
        tvSwitch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.btn_list_btm, 0);
        activity.setTopbar(activity.getString(R.string.map_view), true, true, false, false, false, true, true, true, true, false, false);
        tvNoResult.setVisibility(View.GONE);
        setMarker();
    }

    private void switchToList() {
        WeekendApplication.selectedFragment = TAG;
        flMap.setVisibility(View.GONE);
        llTabSelection.setVisibility(View.VISIBLE);
        rvList.setVisibility(View.VISIBLE);
        tvSwitch.setText(getString(R.string.switch_to_map_view));
        tvSwitch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.btn_map_btm, 0);
        activity.setTopbar(activity.getString(R.string.list_view), true, true, false, false, false, true, true, true, true, false, false);
        if (propertyList == null || propertyList.size() == 0) {
            tvNoResult.setVisibility(View.VISIBLE);
        }
    }

    private void setTheAdapter() {
        if (propertyList == null) {
            propertyList = new ArrayList<>();
        }
        propertyAdapter = new PropertyAdapter(activity, propertyList, (IPropertyClicks) this);
        rvList.setAdapter(propertyAdapter);
    }

    @Override
    public void onItemClick(int position) {
        rvList.setVisibility(View.GONE);
        if (rgSoccerChalet.getCheckedRadioButtonId() == R.id.rb_chalet) {
            activity.replaceFragment(ChaletParentDetailsFragment.newInstance(propertyList.get(position).getPropertyId(), ""), true, true, false, false);
        } else {
            activity.replaceFragment(SoccerFieldParentDetailsFragment.newInstance(propertyList.get(position).getPropertyId(), ""), true, true, false, false);
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
        try {
            propertyId = propertyList.get(position).getPropertyId();
            if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
                String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
                PopupUtil.showReport(activity, name, abuseReasonList, (IReportClick) this);
            } else {
                propertyAdapter.notifyDataSetChanged();
                activity.manageGuestUserAction(getString(R.string.please_login_first_to_submit_report));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReportSubmitClick(String comment, String name, String reasonId) {
        requestSaveAbuseReason(comment, name, reasonId);
    }

    //Recycler view pagination
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
        */
/*if next page not available means we loaded all items so return true *//*

        return true;
    }

    private void requestCityList(String cityName) {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_CITY_NAME, cityName);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CITY_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_CITY_LIST, this);
    }

    private void requestPropertyList(boolean showDefaultProgress) {
        if (!TextUtils.isEmpty(activity.cityId)) {
            if (showDefaultProgress) {
                activity.showHideProgress(true);
            }
            HashMap<String, String> params = new HashMap<>();
            params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
            params.put(WSUtils.KEY_PAGE_INDEX, pageIndex + "");
            params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
            params.put(WSUtils.KEY_PROPERTY_TYPE_ID, propertyTypeId);
            params.put(WSUtils.KEY_SCHEDULE, isShowWithScheduleAvailable ? "Yes" : "No");
            if (mLastLocation != null) {
                params.put(WSUtils.KEY_LAT, String.valueOf(mLastLocation.getLatitude()));
                params.put(WSUtils.KEY_LNG, String.valueOf(mLastLocation.getLongitude()));
            }
            if (activity.cityId != null) {
                params.put(WSUtils.KEY_CITYID, activity.cityId);
            }

            // add check in parameters

            if (!TextUtils.isEmpty(checkIn)) {
                params.put(WSUtils.KEY_CHECK_IN, checkIn);
                params.put(WSUtils.KEY_SCHEDULE, "Yes");
                params.put(WSUtils.KEY_AVAILIBILITY, "All");
            }

            params.put(WSUtils.KEY_SORT_BY_TYPE, sort_type);//[relevance => "",  low-high => price asc, high-low => price desc , nearest_first => distance asc]
            params.put(WSUtils.KEY_AVAILIBILITY, availibility);//availibility [All available => All, available => Yes, not available => No]

            WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_PROPERTY_LIST);
            wsUtils.WSRequest(activity, params, null, WSUtils.REQ_PROPERTY_LIST, this);
        } else {
            activity.hideSplashScreen();
            PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.cant_get_location_please_select_city), getString(R.string.allow), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToChangeLocation();
                }
            });
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
//        activity.showHideProgress(true);
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
                case WSUtils.REQ_CITY_LIST:
                    parseCityList((CityModel) response);
                    break;
                case WSUtils.REQ_ABUSE_REASON_LIST:
                    parseAbuseReasonList((AbuseReasonListModel) response);
                    break;
                case WSUtils.REQ_SAVE_ABUSE_REASON:
                    parseSaveAbuseReason((SaveAbuseReasonModel) response);
                    break;
            }
        }else{
            activity.hideSplashScreen();
        }
    }

    @Override
    public void errorResponse(int requestCode, Throwable t) {
        activity.showHideProgress(false);
        if (rvList != null && tvNoResult != null) {
            rvList.setVisibility(View.GONE);
            tvNoResult.setVisibility(View.VISIBLE);
        }
        CommonUtil.showSnackbar(rootView, getString(R.string.somthing_went_wrong));
        activity.hideSplashScreen();
    }

    @Override
    public void noInternetConnection(int requestCode) {
        CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
        activity.showHideProgress(false);
    }

    private void parseCityList(CityModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                activity.cityId = response.getData().get(0).getId();
                pageIndex = 1;
                requestPropertyList(true);
            } else {
                activity.hideSplashScreen();
                PopupUtil.showAlertMessage(activity, getString(R.string.app_name), getString(R.string.cant_get_location_please_select_city), getString(R.string.allow), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigateToChangeLocation();
                    }
                });
            }
        }else{
            activity.hideSplashScreen();
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
//        activity.showHideProgress(false);
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

    private void parsePropertyList(PropertyListModel response) {
        activity.hideSplashScreen();
        try {
            if (response != null) {
                if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                    if (response.getSettings().getNextPage().equalsIgnoreCase("1")) {
                        if (response.getData() != null) {
                            isNextPageAvailable = true;
                        }
                    } else {
                        isNextPageAvailable = false;
                    }

                    if (pageIndex == 1 && propertyList != null) {
                        propertyList.clear();
                    }

                    if (isNextPageAvailable)
                        pageIndex = pageIndex + 1;

                    isLoading = false;

                    */
/*if (response.getData().getGetTotalDetailChalet() != null) {
                        filterAll = response.getData().getGetTotalDetailChalet().get(0).getTAll();
                        filterAvailable = response.getData().getGetTotalDetailChalet().get(0).getAvailable();
                        filterUnavailable = response.getData().getGetTotalDetailChalet().get(0).getNotAvailable();
                    }*//*

                    if (response.getData().getGetPropertyListing() != null && response.getData().getGetPropertyListing().size() > 0) {
                        propertyList.addAll(response.getData().getGetPropertyListing());
                        hideShowPropertyList(true);
                    } else {
                        hideShowPropertyList(false);
                    }
                } else {
                    hideShowPropertyList(false);
                }

                if (response.getData() != null) {
                    if (response.getData().getGetTotalDetailChalet() != null && response.getData().getGetTotalDetailChalet().size() > 0) {
                        if (response.getData().getGetTotalDetailChalet().get(0) != null) {
                            filterAll = response.getData().getGetTotalDetailChalet().get(0).getTAll();
                            filterAvailable = response.getData().getGetTotalDetailChalet().get(0).getAvailable();
                            filterUnavailable = response.getData().getGetTotalDetailChalet().get(0).getNotAvailable();
                        }
                    }
                }

                if (propertyAdapter == null) {
                    setTheAdapter();
                } else {
                    propertyAdapter.notifyDataSetChanged();
                }

                if (flMap != null && flMap.getVisibility() == View.VISIBLE) {
                    WeekendApplication.selectedFragment = TAG + "-MapView";
                    setMarker();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            activity.showHideProgress(false);
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

    private void hideShowPropertyList(boolean isShow) {
        if (isShow) {
            if (tvNoResult != null && rvList != null) {
                tvNoResult.setVisibility(View.GONE);
                rvList.setVisibility(View.VISIBLE);
                if (pageIndex == 1 && propertyList != null && propertyList.size() > 0) {
                    rvList.scrollToPosition(0);
                }
            }
        } else {
            tvNoResult.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        }
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

    private void navigateToChangeLocation() {
        hideProgressDialog();
        activity.replaceFragment(ChangeLocationFragment.newInstance(), true, true, false, false);
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
            // Change the padding as per needed
            //final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, Constants.SCREEN_WIDTH-50, Constants.SCREEN_HEIGHT-100, 30);
            */
/**call the map call back to know map is loaded or not*//*

            //http://www.androidhub4you.com/2015/06/android-maximum-zoom-in-google-map.html
            mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    */
/**set animated zoom camera into map*//*

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

    */
/***********************
     * Location services
     ***************************//*


    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (markerMap != null && markerMap.size() > 0) {
            int propertyListPosition = markerMap.get(marker);
            if (propertyListPosition != -1) {
                selectedFavoritePosition = propertyListPosition;
                PropertyListModel.GetPropertyListing property = propertyList.get(propertyListPosition);
//                ImageUtil.loadPropertyImage(activity, property.getPropertyImages(), ivMapLogo);
                setPagerAdapter(property.getPropertyImages());
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
                ivMapReport.setVisibility(View.VISIBLE*/
/*property.getMarkAsFeatured().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE)*//*
);
                propertyId = property.getPropertyId();
            }
        }
        AnimationUtil.openSlider(rlMapItem, true);
        return false;
    }

    private void setPagerAdapter(List<PropertyListModel.PropertyImage> propertyImages) {
        if (propertyImages != null && propertyImages.size() == 0) {
            imgPlaceHolder.setVisibility(View.VISIBLE);
        } else {
            imgPlaceHolder.setVisibility(View.GONE);
            ImagesPagerAdapter pagerAdapter = new ImagesPagerAdapter(activity);
            pagerAdapter.setPropertyListImages(propertyImages, this);
            viewPagerChalet.setOffscreenPageLimit(propertyImages.size());
            viewPagerChalet.setPageTransformer(true, new AccordionTransformer());
            viewPagerChalet.setAdapter(pagerAdapter);
//            viewPagerChalet.removeOnPageChangeListener(onPageChangeListener);
//            viewPagerChalet.addOnPageChangeListener(onPageChangeListener);
//            ivPrevious.setAlpha(0.5f);
//            if (propertyImages != null && propertyImages.size() > 1) {
//                ivNext.setAlpha(1.0f);
//            } else {
//                ivNext.setAlpha(0.5f);
//            }
        }
    }

    */
/**
     * Callback called when connected to GCore..
     *//*

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e(TAG, "Connected");

    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.e(TAG, "Suspended "+cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "Failed "+result.getErrorMessage());
    }

    @Override
    public void onPagerItemClicked(int position) {
        onMapItemEvent();
    }


    private void setDatePickerData() {
        String inputTime = tvCalender.getText().toString();
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
                tvCalender.setText(dateFormatter.format(newDate.getTime()));
                pageIndex = 1;
                checkIn = dateFormatterNew.format(newDate.getTime());
                requestPropertyList(true);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.setTitle(getString(R.string.select_date));
        datePickerDialog.show();
    }

}

*/
