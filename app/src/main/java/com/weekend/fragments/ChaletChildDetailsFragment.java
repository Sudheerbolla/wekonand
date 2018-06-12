package com.weekend.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eftimoff.viewpagertransformers.AccordionTransformer;
import com.weekend.HomeActivity;
import com.weekend.R;
import com.weekend.YouTubeActivity;
import com.weekend.adapters.AmenitiesAdapter;
import com.weekend.adapters.ImagesPagerAdapter;
import com.weekend.adapters.ViewPagerFullImageAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.calendar.CalendarView;
import com.weekend.calendar.DateObject;
import com.weekend.calendar.MonthCalenderFragment;
import com.weekend.interfaces.IPagerItemClicked;
import com.weekend.models.ManageSchedulesModel;
import com.weekend.models.PropertyDetailModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.AnimationUtil;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.DateUtil;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.SpaceItemDecoration;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 21/7/16.
 */
public class ChaletChildDetailsFragment extends BaseFragment implements Serializable, View.OnClickListener, IParser<WSResponse>, IPagerItemClicked {
    public static final String TAG = ChaletChildDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_PROPERTY_MODEL_DATA = "bundle_property_model_data";
    private static final String BUNDLE_POSITION = "position";
    public Date selectedDate;
    @Bind(R.id.tv_kitchen_count)
    transient CustomTextView tvKitchenCount;
    @Bind(R.id.tv_bedroom_count)
    transient CustomTextView tvBedroomCount;
    @Bind(R.id.tv_bathroom_count)
    transient CustomTextView tvBathroomCount;
    @Bind(R.id.rv_amenities)
    transient RecyclerView rvAmenities;
    @Bind(R.id.v_amenities)
    transient View vAmenities;
    @Bind(R.id.rv_suitableFor)
    transient RecyclerView rvSuitableFor;
    @Bind(R.id.v_suitableFor)
    transient View vSuitableFor;
    @Bind(R.id.view_pager_chalet)
    transient ViewPager viewPagerChalet;
    @Bind(R.id.iv_next)
    transient ImageView ivNext;
    @Bind(R.id.iv_previous)
    transient ImageView ivPrevious;
    @Bind(R.id.tv_chalet_title)
    transient CustomTextView tvChaletTitle;
    @Bind(R.id.tv_address)
    transient CustomTextView tvAddress;
    @Bind(R.id.tv_city_neighbourhood)
    transient CustomTextView tvCityNeighbourhood;
    @Bind(R.id.tv_section)
    transient CustomTextView tvSection;
    @Bind(R.id.tv_size)
    transient CustomTextView tvSize;
    @Bind(R.id.tv_leasing)
    transient CustomTextView tvLeasing;
    @Bind(R.id.tv_living_room_male_count)
    transient CustomTextView tvLivingRoomMaleCount;
    @Bind(R.id.tv_living_room_female_count)
    transient CustomTextView tvLivingRoomFemaleCount;
    @Bind(R.id.ll_suitable_for)
    transient LinearLayout llSuitableFor;
    @Bind(R.id.ll_amenities)
    transient LinearLayout llAmenities;
    @Bind(R.id.ll_extra_amenities)
    transient LinearLayout llExtraAmenities;
    @Bind(R.id.v_extra_amenities)
    transient View vExtraAmenities;
    @Bind(R.id.tv_extra_amenities)
    transient CustomTextView tvExtraAmenities;
    @Bind(R.id.tv_down_price)
    transient CustomTextView tvDownPrice;
    @Bind(R.id.tv_start_price)
    transient CustomTextView tvStartPrice;
    @Bind(R.id.tv_date)
    transient CustomTextView tvDate;
    @Bind(R.id.tv_book_now)
    transient CustomTextView tvBookNow;
    @Bind(R.id.tv_check_availability)
    transient CustomTextView tvCheckAvailability;
    //Calender
    @Bind(R.id.rl_calender_booking)
    transient RelativeLayout rlCalenderBooking;
    @Bind(R.id.tv_calender_for_title)
    transient CustomTextView tvCalenderForTitle;
    @Bind(R.id.cv_calender)
    transient CalendarView calendarView;
    @Bind(R.id.iv_calendar_next)
    transient ImageView ivCalendarNext;
    @Bind(R.id.iv_calendar_previous)
    transient ImageView ivCalendarPrevious;
    @Bind(R.id.ll_calender_booking)
    transient LinearLayout llCalenderBooking;
    @Bind(R.id.edt_calender_booking_name)
    transient CustomEditText edtCalenderBookingName;
    @Bind(R.id.tv_calender_cancellation_type)
    transient CustomTextView tvCalenderCancellationType;
    @Bind(R.id.llSlots)
    transient LinearLayout llSlots;
    @Bind(R.id.imgPlaceHolder)
    transient ImageView imgPlaceHolder;
    @Bind(R.id.rl_downpayment)
    transient RelativeLayout rlDownpayment;
    @Bind(R.id.rl_full_image)
    transient RelativeLayout rlFullImage;
    @Bind(R.id.vpFullImages)
    transient ViewPager vpFullImages;
    @Bind(R.id.tv_full_image_done)
    transient CustomTextView tvFullImageDone;
    @Bind(R.id.ll_available_date)
    transient LinearLayout llAvailableDate;
    @Bind(R.id.ll_cancellation_policy)
    transient LinearLayout llCancellationPolicy;
    @Bind(R.id.llPhoneCall)
    transient LinearLayout llPhoneCall;
    @Bind(R.id.tvPhoneCall)
    transient CustomTextView tvPhoneCall;
    //    @Bind(R.id.iv_youtube)
//    transient ImageView ivYoutube;
    @Bind(R.id.relYouTubeVideo)
    transient RelativeLayout relYouTubeVideo;

    transient BroadcastReceiver onBackPress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (rlCalenderBooking.getVisibility() == View.VISIBLE) {
                closeCalender();
                WeekendApplication.selectedFragment = TAG;
            } else {
                ((HomeActivity) getActivity()).popBack();
            }
        }
    };
    private transient View rootView;
    private transient LocalStorage localStorage;
    private transient WSFactory wsFactory = new WSFactory();
    private transient Calendar currentDate = Calendar.getInstance();
    private int curentPage;
    private transient List<PropertyDetailModel.ChaletImage> chaletImages;
    private transient List<PropertyDetailModel.Amenity> amenityImages;
    private transient List<PropertyDetailModel.Suitable> suitableForImages;
    private String sunToWedday, thursday, friday, saturday, eidAlFitr, eidAlAdha, downPayment;
    private transient PropertyDetailModel.Data property;
    private transient List<PropertyDetailModel.PropertyDetail> propertyDetails;
    private transient PropertyDetailModel.PropertyChaletDetail chaletDetail;
    transient ViewPager.OnPageChangeListener onMonthChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 5) {
                ivCalendarNext.setVisibility(View.INVISIBLE);
                ivCalendarPrevious.setVisibility(View.VISIBLE);
            } else if (position == 0) {
                ivCalendarNext.setVisibility(View.VISIBLE);
                ivCalendarPrevious.setVisibility(View.INVISIBLE);
            } else {
                ivCalendarNext.setVisibility(View.VISIBLE);
                ivCalendarPrevious.setVisibility(View.VISIBLE);
            }
            if (calendarView != null && calendarView.getFragmentAt(position) != null) {
                currentDate = calendarView.getFragmentAt(position).getCurrentCalender();
            }
            if (MonthCalenderFragment.selectedDate == null) {
                selectedDate = null;
            }
            requestManageSchedule();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private transient ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            curentPage = position + 1;
            if (curentPage <= 1) {
                ivPrevious.setAlpha(0.5f);
                ivNext.setAlpha(1.0f);
            } else if (curentPage >= chaletImages.size()) {
                ivPrevious.setAlpha(1.0f);
                ivNext.setAlpha(0.5f);
            } else {
                ivPrevious.setAlpha(1.0f);
                ivNext.setAlpha(1.0f);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChaletParentDetailsFragment.
     */
    public static ChaletChildDetailsFragment newInstance(PropertyDetailModel.Data propertyDetail, int pos) {
        ChaletChildDetailsFragment fragment = new ChaletChildDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_PROPERTY_MODEL_DATA, propertyDetail);
        bundle.putInt(BUNDLE_POSITION, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ChaletChildDetailsFragment newInstance() {
        ChaletChildDetailsFragment fragment = new ChaletChildDetailsFragment();
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
                rootView = inflater.inflate(R.layout.fragment_chalet_child_details, null);
            }
            ((HomeActivity) getActivity()).setTopbar(getString(R.string.chalet_details), true, false, true, false, false, true,
                    true, false, false, false, false,true);
            ButterKnife.bind(this, rootView);
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
        super.onResume();
        if (WeekendApplication.selectedFragment.equalsIgnoreCase(TAG + "-Calendar")) {
            if (calendarView != null) {
                String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
                edtCalenderBookingName.setText(name.trim());
                requestManageSchedule();
            }
        } else {
            WeekendApplication.selectedFragment = TAG;
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onBackPress, new IntentFilter("OnBackPress"));
    }

    public void loginOrRegisterSuccess() {
        String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
        edtCalenderBookingName.setText(name.trim());
        rootView.findViewById(R.id.tv_calender_book).callOnClick();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onBackPress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void getBundleData() {
        if (getArguments() != null && getArguments().containsKey(BUNDLE_PROPERTY_MODEL_DATA)) {
            property = (PropertyDetailModel.Data) getArguments().getSerializable(BUNDLE_PROPERTY_MODEL_DATA);
            chaletDetail = property.getPropertyChalet().get(0).getPropertyChaletDetails().get(getArguments().getInt(BUNDLE_POSITION));
            propertyDetails = property.getPropertyDetails();
        }
    }

    private void initialize() {/*
        calendarView.initView(getChildFragmentManager(), onMonthChangeListener, activity, new CalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(DateObject dateObj, List<ManageSchedulesModel.SlotTime> allSlots) {
                selectedDate = dateObj.date;
            }
        }, false);*/
        llSlots.setVisibility(View.GONE);
        localStorage = LocalStorage.getInstance(getActivity());
        amenityImages = new ArrayList<>();
        chaletImages = new ArrayList<>();
        suitableForImages = new ArrayList<>();
        chaletImages = chaletDetail.getChaletImages();
        downPayment = chaletDetail.getDownPayment();
        sunToWedday = chaletDetail.getPcPriceSunWed();
        thursday = chaletDetail.getPcPriceThursday();
        friday = chaletDetail.getPcPriceFriday();
        saturday = chaletDetail.getPcPriceSaturday();
        eidAlFitr = chaletDetail.getPcPriceEidAlFitr();
        eidAlAdha = chaletDetail.getPcPriceEidUlAdha();
        setPagerAdapter();
        setChaletData();
        WeekendApplication.trackScreenView(getActivity(), getString(R.string.chalet) + ": " + chaletDetail.getChaletTitle());
    }

    private void setChaletData() {
        setAmenityImages();
        setSuitableForImages();
        if (property != null) {
            tvAddress.setText(property.getPropertyDetails().get(0).getAddress());
            tvCityNeighbourhood.setText(CommonUtil.insertComma(property.getPropertyDetails().get(0).getNeighbourhood(), property.getPropertyDetails().get(0).getCity()));
        }
        tvChaletTitle.setText(chaletDetail.getChaletTitle());
        tvSection.setText(chaletDetail.sectionType);
        tvLeasing.setText(chaletDetail.leasingType);
        tvSize.setText("\u200F" + chaletDetail.getSize() + " " + getString(R.string.sq_mt_));
        tvLivingRoomMaleCount.setText(" " + chaletDetail.getLivingRoomMale());
        tvLivingRoomFemaleCount.setText(" " + chaletDetail.getLivingRoomFemale());
        tvKitchenCount.setText(" " + chaletDetail.getKitchen());
        tvBedroomCount.setText(" " + chaletDetail.getBedroom());
        tvBathroomCount.setText(" " + chaletDetail.getBathroom());
        if (!TextUtils.isEmpty(chaletDetail.getExtraAmenities())) {
            llExtraAmenities.setVisibility(View.VISIBLE);
            vExtraAmenities.setVisibility(View.VISIBLE);
            tvExtraAmenities.setText(chaletDetail.getExtraAmenities());
        } else {
            llExtraAmenities.setVisibility(View.GONE);
            vExtraAmenities.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(chaletDetail.getDownPayment())) {
            tvDownPrice.setText(chaletDetail.getDownPayment());
            rlDownpayment.setVisibility(View.VISIBLE);
        } else {
            rlDownpayment.setVisibility(View.INVISIBLE);
        }
        tvStartPrice.setText(chaletDetail.getPriceStartsFrom());
        tvDate.setText(" " + chaletDetail.getLatestAvaliablity());
        PropertyDetailModel.PropertyDetail property = this.property.getPropertyDetails().get(0);
        if (!TextUtils.isEmpty(property.getPolicyTypeArabic())) {
            llCancellationPolicy.setVisibility(View.VISIBLE);
            tvCalenderCancellationType.setText(property.getPolicyTypeArabic());
        } else {
            llCancellationPolicy.setVisibility(View.GONE);
        }

        tvBookNow.setVisibility((property.getOnlineBooking().equalsIgnoreCase("3") && property.getShowSchedule().equalsIgnoreCase("Yes")) ? (View.VISIBLE) : (View.GONE));
        tvCheckAvailability.setVisibility((property.getOnlineBooking().equalsIgnoreCase("4") && property.getShowSchedule().equalsIgnoreCase("Yes")) ? (View.VISIBLE) : (View.GONE));

        if (!property.getOnlineBooking().equalsIgnoreCase("3") && !property.getShowSchedule().equalsIgnoreCase("Yes")) {
            rlDownpayment.setVisibility(View.GONE);
            downPayment = "";//Making it blank for price range alert
            llAvailableDate.setVisibility(View.GONE);
        } else if (!property.getOnlineBooking().equalsIgnoreCase("3") && property.getShowSchedule().equalsIgnoreCase("Yes")) {
            rlDownpayment.setVisibility(View.GONE);
            downPayment = "";//Making it blank for price range alert
            llAvailableDate.setVisibility(View.VISIBLE);
        } else {
            rlDownpayment.setVisibility(View.VISIBLE);
            llAvailableDate.setVisibility(View.VISIBLE);
        }

        if (propertyDetails != null && propertyDetails.get(0) != null) {
            tvPhoneCall.setText(propertyDetails.get(0).getPropertyPhone1());
            if (TextUtils.isEmpty(chaletDetail.getYoutube()) || chaletDetail.getYoutube().equals("NULL"))
                relYouTubeVideo.setVisibility(View.GONE);
            else {
//                ImageUtil.loadImageYoutube(activity, StaticUtil.getYoutubeThumbnailUrlFromVideoUrl(chaletDetail.getYoutube()), ivYoutube);
                relYouTubeVideo.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setSuitableForImages() {
        suitableForImages = chaletDetail.getSuitable();
        if (suitableForImages != null && suitableForImages.size() > 0) {
            vSuitableFor.setVisibility(View.VISIBLE);
            llSuitableFor.setVisibility(View.VISIBLE);
            AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(getActivity(), null, suitableForImages, true);
            GridLayoutManager layout = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
            rvSuitableFor.setLayoutManager(layout);
            rvSuitableFor.addItemDecoration(new SpaceItemDecoration(10));
            rvSuitableFor.setAdapter(amenitiesAdapter);
        } else {
            vSuitableFor.setVisibility(View.GONE);
            llSuitableFor.setVisibility(View.GONE);
        }
    }

    private void setAmenityImages() {
        amenityImages = chaletDetail.getAmenities();
        if (amenityImages != null && amenityImages.size() > 0) {
            vAmenities.setVisibility(View.VISIBLE);
            llAmenities.setVisibility(View.VISIBLE);
            AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(getActivity(), amenityImages, null, true);
            GridLayoutManager layout = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
            rvAmenities.setLayoutManager(layout);
            rvAmenities.addItemDecoration(new SpaceItemDecoration(10));
            rvAmenities.setAdapter(amenitiesAdapter);
        } else {
            vAmenities.setVisibility(View.GONE);
            llAmenities.setVisibility(View.GONE);
        }
    }

    private void setPagerAdapter() {
        if (chaletImages != null && chaletImages.size() > 0) {
            imgPlaceHolder.setVisibility(View.GONE);
            ImagesPagerAdapter pagerAdapter = new ImagesPagerAdapter(activity);
            pagerAdapter.setChaletImages(chaletImages, true, this);
            viewPagerChalet.setOffscreenPageLimit(chaletImages.size());
            viewPagerChalet.setPageTransformer(true, new AccordionTransformer());
            viewPagerChalet.setAdapter(pagerAdapter);
            viewPagerChalet.removeOnPageChangeListener(onPageChangeListener);
            viewPagerChalet.addOnPageChangeListener(onPageChangeListener);
            viewPagerChalet.setPageTransformer(true, new AccordionTransformer());
            ivPrevious.setAlpha(0.5f);
            if (chaletImages.size() > 1) {
                ivNext.setAlpha(1.0f);
            } else {
                ivNext.setAlpha(0.5f);
            }

            //Full screen view pager
            ViewPagerFullImageAdapter fullImageAdapter = new ViewPagerFullImageAdapter(activity);
            fullImageAdapter.setChaletImages(chaletImages);
            vpFullImages.setAdapter(fullImageAdapter);
            viewPagerChalet.setPageTransformer(true, new AccordionTransformer());
        } else {
            ivNext.setAlpha(0.5f);
            imgPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    @OnClick({R.id.tv_book_now, R.id.tv_check_availability, R.id.tv_show_price, R.id.iv_next, R.id.iv_previous, R.id.iv_chalet_location, R.id.tv_calender_cancel, R.id.tv_calender_book, R.id.ll_cancellation_policy, R.id.tv_full_image_done, R.id.llPhoneCall, R.id.relYouTubeVideo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_book_now:
                WeekendApplication.selectedFragment = TAG + "-Calendar";
                calendarView.initView(getChildFragmentManager(), onMonthChangeListener, getActivity(), new CalendarView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(DateObject dateObj, List<ManageSchedulesModel.SlotTime> allSlots) {
                        selectedDate = dateObj.date;
                    }
                }, false);
                requestManageSchedule();
                AnimationUtil.openSlider(rlCalenderBooking, true);
                llCalenderBooking.setVisibility(View.VISIBLE);
                llPhoneCall.setVisibility(View.GONE);
                tvCalenderForTitle.setText(getString(R.string.select_date));
                String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
                edtCalenderBookingName.setText(name.trim());
                break;
            case R.id.tv_show_price:
                PopupUtil.showPriceRange(getActivity(), sunToWedday, thursday, friday, saturday, eidAlFitr, eidAlAdha, downPayment);
                break;
            case R.id.iv_previous:
                if (curentPage > 1) {
                    viewPagerChalet.setCurrentItem(viewPagerChalet.getCurrentItem() - 1);
                }
                break;
            case R.id.iv_next:
                if (curentPage < chaletImages.size()) {
                    viewPagerChalet.setCurrentItem(viewPagerChalet.getCurrentItem() + 1);
                }
                break;
            case R.id.iv_chalet_location:
                ((HomeActivity) getActivity()).replaceFragment(SingleLocationMapFragment.newInstance(property, "1"), true, true, false, false);
                break;
            case R.id.tv_check_availability:
                WeekendApplication.selectedFragment = TAG + "-Calendar";
                tvCalenderForTitle.setText(getString(R.string.available_dates));
                llCalenderBooking.setVisibility(View.GONE);
                llPhoneCall.setVisibility(View.VISIBLE);
                calendarView.initView(getChildFragmentManager(), onMonthChangeListener, getActivity(), new CalendarView.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(DateObject dateObj, List<ManageSchedulesModel.SlotTime> allSlots) {
                        selectedDate = dateObj.date;
                    }
                }, true);
                requestManageSchedule();
                AnimationUtil.openSlider(rlCalenderBooking, true);
                break;
            case R.id.tv_calender_cancel:
                closeCalender();
                break;
            case R.id.tv_calender_book:
                if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
                    Date currentDate = DateUtil.getCurrentDayStartDate();
                    if (TextUtils.isEmpty(edtCalenderBookingName.getText().toString())) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_booking_name));
                    } else if (selectedDate == null) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.please_select_booking_date));
                    } else if (currentDate == null || currentDate.equals(selectedDate)) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.you_cant_book_this_offer));
                    } else {
                        ConfirmBookingFragment fragment = ConfirmBookingFragment.newInstance("1", property, chaletDetail, edtCalenderBookingName.getText().toString().trim(), selectedDate);
                        ((HomeActivity) getActivity()).replaceFragment(fragment, true, true, false, false);
                        rlCalenderBooking.setVisibility(View.GONE);
                        closeCalender();
                    }
                } else {
//                    ((HomeActivity) getActivity()).manageGuestUserAction(getString(R.string.please_login_to_book_property));
                    ((HomeActivity) getActivity()).showLoginSignupDialog(getString(R.string.please_login_to_book_property));
                }
                break;
            case R.id.ll_cancellation_policy:
                if (propertyDetails != null) {
                    String cancellationDescription = getString(R.string.cancellation_before) + " " + propertyDetails.get(0).getCancelBefore() + " " + propertyDetails.get(0).getCancelBeforeType() + " " + getString(R.string.of_check_in_time);
                    PopupUtil.showRefundPolicy(getActivity(), "", propertyDetails.get(0).getPolicyTypeArabic(), propertyDetails.get(0).getDescriptionArabic(), cancellationDescription, propertyDetails.get(0).getRefulndPolicy(), true, null);
                }
                break;
//            case R.id.iv_navbar_search:
//            case R.id.iv_navbar_notification:
//                closeCalender();
//                activity.onClick(v);
//                break;
            case R.id.tv_full_image_done:
                rlFullImage.setVisibility(View.GONE);
                viewPagerChalet.setCurrentItem(vpFullImages.getCurrentItem());
                break;
            case R.id.llPhoneCall:
                CommonUtil.makeCall(activity, tvPhoneCall.getText().toString());
                break;
            case R.id.relYouTubeVideo:
                openUrl(chaletDetail.getYoutube());
                break;
        }
    }

    private void closeCalender() {
        AnimationUtil.closeSlider(rlCalenderBooking, true);
        selectedDate = null;
        MonthCalenderFragment.selectedDate = null;
        edtCalenderBookingName.setText("");
    }

    private void requestManageSchedule() {
        ((HomeActivity) getActivity()).showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_CHALET_ID, chaletDetail.getPropertyChaletId());
        params.put(WSUtils.KEY_CALENDER_YEAR, String.valueOf(currentDate.get(Calendar.YEAR)));
        params.put(WSUtils.KEY_CALENDAR_MONTH, String.valueOf(currentDate.get(Calendar.MONTH) + 1));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_MANAGE_SCHEDULE);
        wsUtils.WSRequest(getActivity(), params, null, WSUtils.REQ_MANAGE_SCHEDULE, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (isAdded()) {
            switch (requestCode) {
                case WSUtils.REQ_MANAGE_SCHEDULE:
                    parseManageSchedule((ManageSchedulesModel) response);
                    break;
            }
        }
    }

    @Override
    public void errorResponse(int requestCode, Throwable t) {
        ((HomeActivity) getActivity()).showHideProgress(false);
    }

    @Override
    public void noInternetConnection(int requestCode) {
        CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
        ((HomeActivity) getActivity()).showHideProgress(false);
    }

    private void parseManageSchedule(ManageSchedulesModel response) {
        ((HomeActivity) getActivity()).showHideProgress(false);
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (!calendarView.isShown()) {
                    calendarView.getViewPagerCalender().setCurrentItem(5);
                }
                if (response.getData().getSchduleDetails().size() > 0) {
                    calendarView.setCalenderViewOnResponse(response.getData().getSchduleDetails().get(0).getSchdule(), response.getData().getSlotTimes(), false);
                }

            } else {
                CommonUtil.showSnackbar(rootView, response.getSettings().getMessage());
            }
        }
    }

    @Override
    public void onPagerItemClicked(int position) {
        rlFullImage.setVisibility(View.VISIBLE);
        vpFullImages.setCurrentItem(position);
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(activity, YouTubeActivity.class);
            intent.putExtra("urlToLoad", url);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
