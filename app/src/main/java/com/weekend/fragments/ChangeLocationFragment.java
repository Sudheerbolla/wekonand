package com.weekend.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.weekend.R;
import com.weekend.adapters.CityAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IPopularCityClick;
import com.weekend.models.PopularCityListModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.Log;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 19/7/16.
 */
public class ChangeLocationFragment extends BaseFragment implements View.OnClickListener, IParser<WSResponse>, IPopularCityClick, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = ChangeLocationFragment.class.getSimpleName();
    @Bind(R.id.edt_search_city)
    CustomEditText edtSearchCity;
    @Bind(R.id.tv_recenter_current_location)
    CustomTextView tvRecenterCurrentLocation;
    @Bind(R.id.rv_city)
    RecyclerView rvCity;
    @Bind(R.id.tv_no_result)
    CustomTextView tvNoResult;
    private View rootView;
    private CityAdapter cityAdapter;
    private List<PopularCityListModel.Datum> popularCityList;
    private WSFactory wsFactory = new WSFactory();
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeLocationFragment.
     */
    public static ChangeLocationFragment newInstance() {
        return new ChangeLocationFragment();
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
                rootView = inflater.inflate(R.layout.fragment_change_location, null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isAdded())//
                            initialize();
                    }
                }, Constants.FRAGMENT_TRANSACTION_DELAY);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.change_location), true, false, true, false, false, true, true, false, false, false, false);
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialize() {
        popularCityList = new ArrayList<>();
        String city = activity.currentCity;
        if (!TextUtils.isEmpty(city)) {
            city = " (" + city + ")";
        } else {
            buildGoogleApiClient();
            getUserLocation();
        }
        tvRecenterCurrentLocation.setText(getString(R.string.recenter_my_current_location) + city);
        setUpSearchView();
        setAdapter();
        requestCityList();
    }

    private void getBundleData() {
        if (getArguments() != null) {

        }
    }

    @Override
    @OnClick({R.id.tv_recenter_current_location})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_recenter_current_location:
                activity.cityId = "";
                activity.popBack();
                break;
        }
    }

    private void requestCityList() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_POPULAR_CITY_LIST);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_POPULAR_CITY_LIST, this);
    }

    private void setAdapter() {
        cityAdapter = new CityAdapter(activity, popularCityList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvCity.setLayoutManager(layoutManager);
        cityAdapter.addItems(popularCityList);
        rvCity.setAdapter(cityAdapter);
    }

    private void setUpSearchView() {
        edtSearchCity.requestFocus();
        edtSearchCity.setCursorVisible(true);
        edtSearchCity.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cityAdapter != null) {
                    List<PopularCityListModel.Datum> arrayList = cityAdapter.onTextChange(s.toString().trim());
                    if (arrayList != null && arrayList.size() != 0) {
                        rvCity.setVisibility(View.VISIBLE);
                        tvNoResult.setVisibility(View.GONE);
                    } else {
                        rvCity.setVisibility(View.GONE);
                        tvNoResult.setVisibility(View.VISIBLE);
                        tvNoResult.setText(getString(R.string.no_cities_match_in_your_search));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        switch (requestCode) {
            case WSUtils.REQ_POPULAR_CITY_LIST:
                parsePopularCityList((PopularCityListModel) response);
                break;
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

    private void parsePopularCityList(PopularCityListModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                popularCityList.addAll(response.getData());
                makeCitySelected();
                cityAdapter.addItems(popularCityList);
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
        activity.showHideProgress(false);
    }

    private void makeCitySelected() {
        for (int i = 0; i < popularCityList.size(); i++) {
            if (popularCityList.get(i).getCityId().equalsIgnoreCase(activity.cityId)) {
                popularCityList.get(i).setSelected(true);
            }
        }
    }

    @Override
    public void onCitySelect(int position, String cityId) {
        activity.cityId = cityId;
        LocalStorage.getInstance(activity).putString("cityId", cityId);
        activity.popBack();
    }

    public void buildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void getUserLocation() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e("Location", mLastLocation.toString());
            getUserCity();
        }
    }

    private void getUserCity() {
        Geocoder gcd = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (addresses != null && addresses.size() > 0) {
            Log.e("City", addresses.get(0).getLocality());
            activity.currentCity = addresses.get(0).getLocality();
            String city = activity.currentCity;
            if (!TextUtils.isEmpty(city)) {
                city = " (" + city + ")";
            }
            tvRecenterCurrentLocation.setText(getString(R.string.recenter_my_current_location) + city);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
