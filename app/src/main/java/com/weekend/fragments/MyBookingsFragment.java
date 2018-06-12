package com.weekend.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.weekend.R;
import com.weekend.adapters.MyBookingsAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IBookingClick;
import com.weekend.models.MyBookingsModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomRecyclerView;
import com.weekend.views.CustomTextView;
import com.weekend.views.Paginate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * Created by Hymavathi.kadali on 20/7/16.
 */
public class MyBookingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, IBookingClick, Paginate.Callbacks, IParser<WSResponse> {
    public static final String TAG = MyBookingsFragment.class.getSimpleName();
    @Bind(R.id.rb_chalet)
    RadioButton rbChalet;
    @Bind(R.id.rb_soccer_fields)
    RadioButton rbSoccerFields;
    @Bind(R.id.rg_soccer_chalet)
    RadioGroup rgSoccerChalet;
    @Bind(R.id.v_chalets)
    View vChalets;
    @Bind(R.id.v_soccer_fields)
    View vSoccerFields;
    @Bind(R.id.rv_my_bookings)
    CustomRecyclerView rvMyBookings;
    @Bind(R.id.tv_no_result)
    CustomTextView tvNoResult;
    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();

    private MyBookingsAdapter myBookingsAdapter;
    private List<MyBookingsModel.Datum> myBookingsList;
    private boolean isNextPageAvailable = false;
    private boolean isLoading = false;
    private int pageIndex = 1;
    private String propertyTypeId = "1";

    public MyBookingsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyBookingsFragment.
     */
    public static MyBookingsFragment newInstance() {
        MyBookingsFragment fragment = new MyBookingsFragment();
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
                rootView = inflater.inflate(R.layout.fragment_my_bookings, null);
            }
            ButterKnife.bind(this, rootView);
            initialize();
            activity.setTopbar(activity.getString(R.string.my_bookings), true, false, true, false, false, false, true, false,
                    false, false, false,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtil.setTypefacerb(activity, rbChalet, 6);
        CommonUtil.setTypefacerb(activity, rbSoccerFields, 6);
        Log.e(TAG, "onCreateView");
        return rootView;
    }

    @Override
    public void onResume() {
        WeekendApplication.selectedFragment = TAG;
        Log.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView");
    }

    private void initialize() {
        localStorage = LocalStorage.getInstance(activity);
        myBookingsList = new ArrayList<>();
        pageIndex = 1;
        setAdapter();
        if (isAdded())
            requestMyBookingsList(true);
    }

    private void getBundleData() {
    }

    @Override
    @OnCheckedChanged({R.id.rb_chalet, R.id.rb_soccer_fields})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_chalet:
                if (isChecked) {
                    onChaletEvent();
                }
                break;
            case R.id.rb_soccer_fields:
                if (isChecked) {
                    onSoccerEvent();
                }
                break;
        }
    }

    private void onChaletEvent() {
        if (myBookingsList != null && myBookingsList.size() > 0) {
            rvMyBookings.scrollToPosition(0);
        }
        vChalets.setVisibility(View.VISIBLE);
        vSoccerFields.setVisibility(View.INVISIBLE);
        pageIndex = 1;
        propertyTypeId = "1";
        myBookingsList.clear();
        rvMyBookings.setVisibility(View.GONE);
        requestMyBookingsList(true);
    }

    private void onSoccerEvent() {
        if (myBookingsList != null && myBookingsList.size() > 0) {
            rvMyBookings.scrollToPosition(0);
        }
        vChalets.setVisibility(View.INVISIBLE);
        vSoccerFields.setVisibility(View.VISIBLE);
        pageIndex = 1;
        propertyTypeId = "2";
        myBookingsList.clear();
        rvMyBookings.setVisibility(View.GONE);
        requestMyBookingsList(true);
    }

    private void setAdapter() {
        myBookingsAdapter = new MyBookingsAdapter(activity, myBookingsList, this);
        rvMyBookings.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvMyBookings.setLayoutManager(layoutManager);
        rvMyBookings.setAdapter(myBookingsAdapter);
        rvMyBookings.setListPagination(this);
    }

    @Override
    public void onItemClick(int position) {
        rvMyBookings.setVisibility(View.GONE);
        if (rgSoccerChalet.getCheckedRadioButtonId() == R.id.rb_chalet) {
            activity.replaceFragment(ChaletBookingDetailsFragment.newInstance(myBookingsList.get(position)), true, true, false, false);
        } else
            activity.replaceFragment(SoccerFieldBookingDetailsFragment.newInstance(myBookingsList.get(position)), true, true, false, false);
    }

    @Override
    public void onShareClick(int position, MyBookingsModel.Datum booking) {
        /*Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        String bookingType = booking.getBookingType();
        String shareText = getString(R.string.thank_you_for_using_the_weekend_app) + "\n"
                + getString(R.string.name) + ": " + (booking.getPropertyTitle() + "").trim();
        if (!TextUtils.isEmpty(booking.getFullAddress())) {
            shareText += "\n" + getString(R.string.address) + ": " + booking.getFullAddress().trim();
        } *//*else if (!TextUtils.isEmpty(booking.getAddress())) {
            shareText += "\n" + getString(R.string.address) + ": " + booking.getAddress().trim();
        } else {
            shareText += "\n" + getString(R.string.address) + ": " + booking.getCity().trim();
        }*//*
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(intent, getString(R.string.share_with)));*/

        String shareText = "<b>" + getString(R.string.thank_you_for_using_the_weekend_app) + "</b>&nbsp;" + "<br/>"
                + "<b>" + getString(R.string.name) + ": " + "</b>&nbsp;" + (booking.getPropertyTitle() + "").trim();
        if (!TextUtils.isEmpty(booking.getFullAddress())) {
            shareText += "<br/>" + "<b>" + getString(R.string.address) + ": " + "</b>&nbsp;" + booking.getFullAddress().trim();
        }
        StaticUtil.sharingIntent(activity, shareText);
    }

    @Override
    public void onReviewClick(int position, String propertyId) {
        activity.replaceFragment(AddYourReviewFragment.newInstance(myBookingsList.get(position)), true, true, false, false);
    }

    //Recycler view pagination
    @Override
    public void onLoadMore() {
        isLoading = true;
        requestMyBookingsList(false);
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

    private void requestMyBookingsList(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PAGE_INDEX, pageIndex + "");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_PROPERTY_TYPE_ID, propertyTypeId);
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_MY_BOOKINGS);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_MY_BOOKINGS, this);
    }


    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (activity != null && isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_MY_BOOKINGS:
                    parseMyBookingsList((MyBookingsModel) response);
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


    private void parseMyBookingsList(MyBookingsModel response) {
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

                if (pageIndex == 1 && myBookingsList != null) {
                    myBookingsList.clear();
                }

                isLoading = false;

                if (response.getData() != null && response.getData().size() > 0) {
                    showHideBookingsList(true);
                    myBookingsList.addAll(response.getData());
                } else {
                    showHideBookingsList(false);
                }
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("2")) {
                activity.manageTokenExpire();
            } else {
                showHideBookingsList(false);
            }

            if (myBookingsAdapter != null) {
                myBookingsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void showHideBookingsList(boolean isShow) {
        if (isShow) {
            tvNoResult.setVisibility(View.GONE);
            rvMyBookings.setVisibility(View.VISIBLE);
        } else {
            tvNoResult.setVisibility(View.VISIBLE);
            if (propertyTypeId.equalsIgnoreCase("1")) {
                tvNoResult.setText(getString(R.string.you_have_no_chalet_bookings));
            } else {
                tvNoResult.setText(getString(R.string.you_have_no_soccer_field_bookings));
            }
            rvMyBookings.setVisibility(View.GONE);
        }
    }

}
