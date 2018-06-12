package com.weekend.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.weekend.R;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.models.PropertyDetailModel;
import com.weekend.utils.CommonUtil;

import butterknife.ButterKnife;

/**
 * Created by hb on 1/8/16.
 */
public class SingleLocationMapFragment extends BaseFragment implements OnMapReadyCallback {

    private static String BUNDLE_PROPERTY_DETAILS_DATA = "property_details";
    private static String BUNDLE_PROPERTY_TYPE_ID = "property_type_id";
    private View rootView;
    private SupportMapFragment mapFragment;
    private PropertyDetailModel.Data propertyDetailsData;
    private LatLng destination;
    private GoogleMap googleMap;
    private String propertyTypeId;

    public static SingleLocationMapFragment newInstance(PropertyDetailModel.Data propertyDetailsData, String propertyTypeId) {
        SingleLocationMapFragment fragment = new SingleLocationMapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_PROPERTY_DETAILS_DATA, propertyDetailsData);
        bundle.putString(BUNDLE_PROPERTY_TYPE_ID, propertyTypeId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_single_map_location, null);
        ButterKnife.bind(this, rootView);
        initialize();
        activity.setTopbar(activity.getString(R.string.map), true, false, true, false, false, false, false, false,
                false, false, false,true);
        return rootView;
    }

    @Override
    public void onResume() {
        WeekendApplication.selectedFragment = TAG;
        super.onResume();
    }

    private void getBundleData() {
        if (getArguments() != null && getArguments().containsKey(BUNDLE_PROPERTY_DETAILS_DATA)) {
            propertyDetailsData = (PropertyDetailModel.Data) getArguments().getSerializable(BUNDLE_PROPERTY_DETAILS_DATA);
            propertyTypeId = getArguments().getString(BUNDLE_PROPERTY_TYPE_ID);
        }
    }

    private void initialize() {
        if (propertyDetailsData != null) {
            Double lat = Double.parseDouble(propertyDetailsData.getPropertyDetails().get(0).getLatitude());
            Double lng = Double.parseDouble(propertyDetailsData.getPropertyDetails().get(0).getLongitude());
            destination = new LatLng(lat, lng);
        }
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (mapFragment == null) {
            //GoogleMapOptions options = new GoogleMapOptions();
            //options.liteMode(true).mapToolbarEnabled(true);
            // mapFragment = SupportMapFragment.newInstance(options);
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.mapView, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setMapToolbarEnabled(true);
        this.googleMap.setMyLocationEnabled(true);
        if (destination != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(destination, 10);
            googleMap.animateCamera(cameraUpdate);
            this.googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(final Marker marker) {
                    View v = View.inflate(activity, R.layout.inflate_map_point_layout, null);
                    TextView tvTitle = (TextView) v.findViewById(R.id.txtTitle);
                    TextView tvAddress = (TextView) v.findViewById(R.id.txtAddress);
                    TextView tvCity = (TextView) v.findViewById(R.id.txtCity);
                    if (!TextUtils.isEmpty(propertyDetailsData.getPropertyDetails().get(0).getTitle())) {
                        tvTitle.setText(propertyDetailsData.getPropertyDetails().get(0).getTitle());
                    } else {
                        tvTitle.setVisibility(View.GONE);
                    }
                    if (!TextUtils.isEmpty(propertyDetailsData.getPropertyDetails().get(0).getAddress())) {
                        tvAddress.setText(propertyDetailsData.getPropertyDetails().get(0).getAddress());
                    } else {
                        tvAddress.setVisibility(View.GONE);
                    }
                    tvCity.setText(CommonUtil.insertComma(propertyDetailsData.getPropertyDetails().get(0).getCity(), propertyDetailsData.getPropertyDetails().get(0).getCountry()));
                    return v;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    if (marker.isInfoWindowShown()) {
                        marker.hideInfoWindow();
                        marker.showInfoWindow();
                    }
                    return null;
                }
            });

            PropertyDetailModel.PropertyDetail property = propertyDetailsData.getPropertyDetails().get(0);
            if (propertyTypeId.equalsIgnoreCase("1")) {//chalet
                int icon = R.mipmap.icon_chalet;
                if (property.getRecommended().equalsIgnoreCase("Yes") && property.getVerified().equalsIgnoreCase("Yes")) {
                    icon = R.mipmap.icon_chalet_recommanded;
                } else if (property.getRecommended().equalsIgnoreCase("Yes")) {
                    icon = R.mipmap.icon_chalet_recommanded;
                } else if (property.getVerified().equalsIgnoreCase("Yes")) {
                    icon = R.mipmap.icon_chalet_verified;
                }
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(property.getLatitude()), Double.valueOf(property.getLongitude())))
                        .icon(BitmapDescriptorFactory.fromResource(icon))).showInfoWindow();

            } else {//soccer field
                int icon = R.mipmap.icon_soccer_field;
                if (property.getRecommended().equalsIgnoreCase("Yes") && property.getVerified().equalsIgnoreCase("Yes")) {
                    icon = R.mipmap.icon_soccer_field_recommanded;
                } else if (property.getRecommended().equalsIgnoreCase("Yes")) {
                    icon = R.mipmap.icon_soccer_field_recommanded;
                } else if (property.getVerified().equalsIgnoreCase("Yes")) {
                    icon = R.mipmap.icon_soccer_field_verified;
                }
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(property.getLatitude()), Double.valueOf(property.getLongitude())))
                        .icon(BitmapDescriptorFactory.fromResource(icon))).showInfoWindow();
            }
        } else {
            CommonUtil.showSnackbar(getView(), getString(R.string.location_not_available));
        }
    }
}
