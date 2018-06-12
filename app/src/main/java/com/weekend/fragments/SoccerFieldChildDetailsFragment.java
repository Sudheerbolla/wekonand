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
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.eftimoff.viewpagertransformers.AccordionTransformer;
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
import com.weekend.calendar.SoccerFieldSlotsAdapter;
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
import com.weekend.utils.Log;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.SpaceItemDecoration;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;
import com.weekend.views.ExpandableHeightGridView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 21/7/16.
 */
public class SoccerFieldChildDetailsFragment extends BaseFragment implements Serializable, View.OnClickListener, IParser<WSResponse>, IPagerItemClicked {
    public static final String TAG = SoccerFieldChildDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_PROPERTY_CHALET_DETAIL = "property_chalet_detail";
    private static final String BUNDLE_PROPERTY_MODEL_DATA = "bundle_property_model_data";
    private static final String BUNDLE_POSITION = "position";

    private transient View rootView;
    @Bind(R.id.tv_calender_for_title)
    transient CustomTextView tvCalenderForTitle;
    @Bind(R.id.cv_calender)
    transient CalendarView calendarView;
    @Bind(R.id.iv_calendar_next)
    transient ImageView ivCalendarNext;
    @Bind(R.id.iv_calendar_previous)
    transient ImageView ivCalendarPrevious;
    @Bind(R.id.edt_calender_booking_name)
    transient CustomEditText edtCalenderBookingName;
    @Bind(R.id.ll_calender_booking)
    transient LinearLayout llCalenderBooking;
    @Bind(R.id.rl_calender_booking)
    transient RelativeLayout rlCalenderBooking;
    @Bind(R.id.view_pager_soccer_field)
    transient ViewPager viewPagerSoccerField;
    @Bind(R.id.iv_next)
    transient ImageView ivNext;
    @Bind(R.id.iv_previous)
    transient ImageView ivPrevious;
    @Bind(R.id.tv_soccer_field_title)
    transient CustomTextView tvSoccerFieldTitle;
    @Bind(R.id.tv_address)
    transient CustomTextView tvAddress;
    @Bind(R.id.tv_city_neighbourhood)
    transient CustomTextView tvCityNeighbourhood;
    @Bind(R.id.tv_size)
    transient CustomTextView tvSize;
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
    @Bind(R.id.tv_time)
    transient CustomTextView tvTime;
    @Bind(R.id.tv_book_now)
    transient CustomTextView tvBookNow;
    @Bind(R.id.tv_check_availability)
    transient CustomTextView tvCheckAvailability;
    @Bind(R.id.llPhoneCall)
    transient LinearLayout llPhoneCall;
    @Bind(R.id.tvPhoneCall)
    transient CustomTextView tvPhoneCall;
    @Bind(R.id.rv_amenities)
    transient RecyclerView rvAmenities;
    @Bind(R.id.v_amenities)
    transient View vAmenities;
    @Bind(R.id.gvSlots)
    transient ExpandableHeightGridView gvSlotTimes;
    @Bind(R.id.tv_selected_date)
    transient CustomTextView tvSelectedDate;
    @Bind(R.id.llSlots)
    transient LinearLayout llSlots;
    @Bind(R.id.imgPlaceHolder)
    transient ImageView imgPlaceHolder;
    @Bind(R.id.tv_calender_cancellation_type)
    transient CustomTextView tvCalenderCancellationType;
    @Bind(R.id.rl_downpayment)
    transient RelativeLayout rlDownpayment;
    @Bind(R.id.ll_available_date)
    transient LinearLayout llAvailableDate;
    @Bind(R.id.ll_time)
    transient LinearLayout llTime;
    @Bind(R.id.vpFullImages)
    ViewPager vpFullImages;
    @Bind(R.id.rl_full_image)
    transient RelativeLayout rlFullImage;
    @Bind(R.id.tv_full_image_done)
    transient CustomTextView tvFullImageDone;
    @Bind(R.id.ll_cancellation_policy)
    transient LinearLayout llCancellationPolicy;
    @Bind(R.id.relYouTubeVideo)
    transient RelativeLayout relYouTubeVideo;

    private transient LocalStorage localStorage;
    private transient WSFactory wsFactory = new WSFactory();

    transient ManageSchedulesModel.SlotTime selectedSlot;
    transient DateObject selectedDate;

    private transient Calendar currentDate = Calendar.getInstance();
    private int curentPage;
    private transient List<PropertyDetailModel.ChaletImage> chaletImages;
    private transient List<PropertyDetailModel.Amenity> amenityImages;
    private String sunToWedday, thursday, friday, saturday, eidAlFitr, eidAlAdha, downPayment;
    private transient PropertyDetailModel.Data property;
    private transient List<PropertyDetailModel.PropertyDetail> propertyDetails;
    private transient PropertyDetailModel.PropertyChaletDetail chaletDetail;

    transient BroadcastReceiver onBackPress = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (rlCalenderBooking.getVisibility() == View.VISIBLE) {
                closeCalender();
                WeekendApplication.selectedFragment = TAG;
            } else {
                activity.popBack();
            }
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
    private transient ViewPager.OnPageChangeListener onMonthChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 5) {
                ivCalendarNext.setVisibility(View.VISIBLE);
                ivCalendarPrevious.setVisibility(View.INVISIBLE);
            } else if (position == 0) {
                ivCalendarNext.setVisibility(View.INVISIBLE);
                ivCalendarPrevious.setVisibility(View.VISIBLE);
            } else {
                ivCalendarNext.setVisibility(View.VISIBLE);
                ivCalendarPrevious.setVisibility(View.VISIBLE);
            }
            currentDate = calendarView.getFragmentAt(position).getCurrentCalender();
            requestManageSchedule();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private transient OnSlotSelectedListener onSlotSelectedListener = new OnSlotSelectedListener() {
        @Override
        public void onSlotSelected(ManageSchedulesModel.SlotTime slotTime) {
            Log.e("Selected Slot :", slotTime.getSlotFromTime() + "-" + slotTime.getSlotToTime());
            if (slotTime.isSelected)
                selectedSlot = slotTime;
            else
                selectedDate = null;
        }
    };

    private transient CalendarView.OnDateSelectedListener onDateSelectedListener = new CalendarView.OnDateSelectedListener() {
        @Override
        public void onDateSelected(DateObject dateObj, List<ManageSchedulesModel.SlotTime> allSlots) {
            //Date currentDate = DateUtil.getCurrentDayStartDate();
            //if(currentDate != null && currentDate.before(dateObj.date)) {
            selectedDate = dateObj;
            selectedSlot = null;
            Log.e("Selected Date :", selectedDate.toString());
            String dateStr = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(dateObj.date);
            if (allSlots != null && allSlots.size() > 0) {
                tvSelectedDate.setText(getString(R.string.select_time_slot_for_date) + " " + dateStr);
                setSlotAdapter(dateObj.bookedSlots, allSlots);
            } else {
                tvSelectedDate.setText(getString(R.string.no_slot_available_for_date) + " " + dateStr);
            }
            if (gvSlotTimes != null && gvSlotTimes.getVisibility() != View.VISIBLE) {
                gvSlotTimes.setVisibility(View.VISIBLE);
            }
            //} else {
            //    CommonUtil.showSnackbar(getView(), getString(R.string.you_cant_book_this_offer));
            //}
        }
    };
    private transient boolean isAvailability;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SoccerFieldChildDetailsFragment.
     */
    public static SoccerFieldChildDetailsFragment newInstance(PropertyDetailModel.Data propertyDetail, int pos) {
        SoccerFieldChildDetailsFragment fragment = new SoccerFieldChildDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_PROPERTY_MODEL_DATA, propertyDetail);
        bundle.putInt(BUNDLE_POSITION, pos);
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
        try {
            if (rootView == null) {
                rootView = inflater.inflate(R.layout.fragment_soccer_field_child_details, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.soccer_field_details), true, false, true, false, false, true, true,
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
        if (WeekendApplication.selectedFragment.equalsIgnoreCase(TAG + "-Calendar")) {
            if (calendarView != null) {
                String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
                edtCalenderBookingName.setText(name.trim());
                requestManageSchedule();
            }
        } else {
            WeekendApplication.selectedFragment = TAG;
        }
        super.onResume();
        LocalBroadcastManager.getInstance(activity).registerReceiver(onBackPress, new IntentFilter("OnBackPress"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(onBackPress);
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

    private void initialize() {
        llSlots.setVisibility(View.VISIBLE);
        localStorage = LocalStorage.getInstance(activity);
        amenityImages = new ArrayList<>();
        chaletImages = new ArrayList<>();
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
        WeekendApplication.trackScreenView(getActivity(), getString(R.string.soccer_field) + ": " + chaletDetail.getChaletTitle());
    }

    private void setChaletData() {
        setAmenitiesImages();
        if (property != null) {
            tvAddress.setText(property.getPropertyDetails().get(0).getAddress());
            tvCityNeighbourhood.setText(CommonUtil.insertComma(property.getPropertyDetails().get(0).getNeighbourhood().trim(), property.getPropertyDetails().get(0).getCity().trim()));
        }
        tvSoccerFieldTitle.setText(chaletDetail.getChaletTitle());
        tvSize.setText(chaletDetail.getSize());
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
        String dateTime = chaletDetail.getLatestAvaliablity();
        if (!dateTime.isEmpty()) {
            if (dateTime.contains(" ")) {
                tvDate.setText(" " + dateTime.substring(0, dateTime.indexOf(" ")));
                String time = dateTime.substring(dateTime.indexOf(' ') + 1);
                if (!time.isEmpty()) {
                    SpannableString sTime = new SpannableString(" \u202D" + time);
                    if (time.contains(getString(R.string.to_small))) {
                        sTime = new SpannableString(" \u202D" + time.toString().replace(getString(R.string.to_small), "-"));
                    }
                    //tvTime.setText(" \u202D" + time);
                    tvTime.setText(sTime);
                }
            } else {
                SpannableString sTime = new SpannableString(" " + dateTime);
                if (dateTime.contains(getString(R.string.to_small))) {
                    sTime = new SpannableString(" " + dateTime.toString().replace(getString(R.string.to_small), "-"));
                }
                //tvDate.setText(" " + dateTime);
                tvDate.setText(sTime);
            }
        }
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
            llTime.setVisibility(View.GONE);
        } else if (!property.getOnlineBooking().equalsIgnoreCase("3") && property.getShowSchedule().equalsIgnoreCase("Yes")) {
            rlDownpayment.setVisibility(View.GONE);
            downPayment = "";//Making it blank for price range alert
            llAvailableDate.setVisibility(View.VISIBLE);
            llTime.setVisibility(View.VISIBLE);
        } else {
            rlDownpayment.setVisibility(View.VISIBLE);
            llAvailableDate.setVisibility(View.VISIBLE);
            llTime.setVisibility(View.VISIBLE);
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

    private void setAmenitiesImages() {
        amenityImages = chaletDetail.getAmenities();
        if (amenityImages != null && amenityImages.size() > 0) {
            vAmenities.setVisibility(View.VISIBLE);
            llAmenities.setVisibility(View.VISIBLE);
            AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(activity, amenityImages, null, true);
            GridLayoutManager layout = new GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false);
            rvAmenities.setLayoutManager(layout);
            rvAmenities.addItemDecoration(new SpaceItemDecoration(10));
            rvAmenities.setAdapter(amenitiesAdapter);
        } else {
            vAmenities.setVisibility(View.GONE);
            llAmenities.setVisibility(View.GONE);
        }
    }

    @Override
    @OnClick({R.id.tv_book_now, R.id.tv_check_availability, R.id.tv_show_price, R.id.iv_next, R.id.iv_previous, R.id.iv_soccer_field_location, R.id.tv_calender_cancel, R.id.tv_calender_book, R.id.ll_cancellation_policy, R.id.tv_full_image_done, R.id.llPhoneCall, R.id.relYouTubeVideo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_book_now:
                isAvailability = false;
                WeekendApplication.selectedFragment = TAG + "-Calendar";
                llCalenderBooking.setVisibility(View.VISIBLE);
                llPhoneCall.setVisibility(View.GONE);
                tvCalenderForTitle.setText(getString(R.string.select_date));
                calendarView.initView(getChildFragmentManager(), onMonthChangeListener, activity, onDateSelectedListener, false);
                requestManageSchedule();
                AnimationUtil.openSlider(rlCalenderBooking, true);
                String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
                edtCalenderBookingName.setText(name);
                break;
            case R.id.tv_show_price:
                PopupUtil.showPriceRange(activity, sunToWedday, thursday, friday, saturday, eidAlFitr, eidAlAdha, downPayment);
                break;
            case R.id.iv_previous:
                if (curentPage > 1) {
                    viewPagerSoccerField.setCurrentItem(viewPagerSoccerField.getCurrentItem() - 1);
                }
                break;
            case R.id.iv_next:
                if (curentPage < chaletImages.size()) {
                    viewPagerSoccerField.setCurrentItem(viewPagerSoccerField.getCurrentItem() + 1);
                }
                break;
            case R.id.iv_soccer_field_location:
                activity.replaceFragment(SingleLocationMapFragment.newInstance(property, "2"), true, true, false, false);
                break;
            case R.id.tv_check_availability:
                WeekendApplication.selectedFragment = TAG + "-Calendar";
                isAvailability = true;
                tvCalenderForTitle.setText(getString(R.string.available_dates));
                llCalenderBooking.setVisibility(View.GONE);
                llPhoneCall.setVisibility(View.VISIBLE);
                AnimationUtil.openSlider(rlCalenderBooking, true);
                calendarView.initView(getChildFragmentManager(), onMonthChangeListener, activity, onDateSelectedListener, true);
                requestManageSchedule();
                break;
            case R.id.tv_calender_cancel:
                closeCalender();
                break;
            case R.id.tv_calender_book:
                if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
                    Date currentDate = DateUtil.getCurrentDayStartDate();
                    if (TextUtils.isEmpty(edtCalenderBookingName.getText().toString())) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.please_enter_booking_name));
                    } else if (selectedDate == null || selectedDate.date == null) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.please_select_booking_date));
                    } else if (selectedSlot == null) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.please_select_booking_slot));
                    } else if (currentDate == null || currentDate.equals(selectedDate.date)) {
                        CommonUtil.showSnackbar(getView(), getString(R.string.you_cant_book_this_offer));
                    } else {
                        ConfirmBookingFragment fragment = ConfirmBookingFragment.newInstance("2", property, chaletDetail, edtCalenderBookingName.getText().toString().trim(), selectedDate, selectedSlot);
                        activity.replaceFragment(fragment, true, true, false, false);
                        closeCalender();
                    }
                } else {
//                    activity.manageGuestUserAction(getString(R.string.please_login_to_book_property));
                    activity.showLoginSignupDialog(getString(R.string.please_login_to_book_property));
                }
                break;
            case R.id.ll_cancellation_policy:
                if (propertyDetails != null) {
                    String cancellationDescription = getString(R.string.cancellation_before) + " " + propertyDetails.get(0).getCancelBefore() + " " + propertyDetails.get(0).getCancelBeforeType() + " " + getString(R.string.of_check_in_time);
                    PopupUtil.showRefundPolicy(activity, "", propertyDetails.get(0).getPolicyTypeArabic(), propertyDetails.get(0).getDescriptionArabic(), cancellationDescription, propertyDetails.get(0).getRefulndPolicy(), true, null);
                }
                break;
//            case R.id.iv_navbar_search:
//            case R.id.iv_navbar_notification:
//                closeCalender();
//                activity.onClick(v);
//                break;
            case R.id.tv_full_image_done:
                rlFullImage.setVisibility(View.GONE);
                viewPagerSoccerField.setCurrentItem(vpFullImages.getCurrentItem());
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
        selectedSlot = null;
        MonthCalenderFragment.selectedDate = null;
        edtCalenderBookingName.setText("");
        tvSelectedDate.setText("");
        if (gvSlotTimes != null) {
            gvSlotTimes.setVisibility(View.GONE);
        }
    }

    public void loginOrRegisterSuccess() {
        String name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
        edtCalenderBookingName.setText(name.trim());
        rootView.findViewById(R.id.tv_calender_book).callOnClick();
    }

    private void setPagerAdapter() {
        if (chaletImages != null && chaletImages.size() > 0) {
            imgPlaceHolder.setVisibility(View.GONE);
            ImagesPagerAdapter pagerAdapter = new ImagesPagerAdapter(activity);
            pagerAdapter.setChaletImages(chaletImages, true, this);
            viewPagerSoccerField.setOffscreenPageLimit(chaletImages.size());
            viewPagerSoccerField.setPageTransformer(true, new AccordionTransformer());
            viewPagerSoccerField.setAdapter(pagerAdapter);
            viewPagerSoccerField.removeOnPageChangeListener(onPageChangeListener);
            viewPagerSoccerField.addOnPageChangeListener(onPageChangeListener);
            viewPagerSoccerField.setPageTransformer(true, new AccordionTransformer());
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
            viewPagerSoccerField.setPageTransformer(true, new AccordionTransformer());
        } else {
            ivNext.setAlpha(0.5f);
            imgPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private void requestManageSchedule() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_CHALET_ID, chaletDetail.getPropertyChaletId());
        params.put(WSUtils.KEY_CALENDER_YEAR, String.valueOf(currentDate.get(Calendar.YEAR)));
        params.put(WSUtils.KEY_CALENDAR_MONTH, String.valueOf(currentDate.get(Calendar.MONTH) + 1));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_MANAGE_SCHEDULE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_MANAGE_SCHEDULE, this);
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
        activity.showHideProgress(false);
    }

    @Override
    public void noInternetConnection(int requestCode) {
        CommonUtil.showSnackbar(getView(), getString(R.string.no_internet_connection));
        activity.showHideProgress(false);
    }

    private void parseManageSchedule(ManageSchedulesModel response) {
        activity.showHideProgress(false);
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (response.getData().getSchduleDetails().size() > 0) {
                    calendarView.setCalenderViewOnResponse(response.getData().getSchduleDetails().get(0).getSchdule(), response.getData().getSlotTimes(), true);
//                    setSlotAdapter(null, scheduleResponse.getData().getSlotTimes());
                    selectedSlot = null;
                }
                if (!calendarView.isShown()) {
                    AnimationUtil.openSlider(rlCalenderBooking, true);
                }
            } else {
                CommonUtil.showSnackbar(rootView, response.getSettings().getMessage());
            }
        }
    }

    private void setSlotAdapter(List<ManageSchedulesModel.Slot> bookedSlots, List<ManageSchedulesModel.SlotTime> slotTimes) {
        for (int i = 0; i < slotTimes.size(); i++) {
            slotTimes.get(i).status = DateObject.STATUS_AVAILABLE;
            slotTimes.get(i).isSelected = false;
        }
        if (bookedSlots != null) {
            for (int i = 0; i < slotTimes.size(); i++) {
                ManageSchedulesModel.SlotTime slotTime = slotTimes.get(i);
                for (int j = 0; j < bookedSlots.size(); j++) {
                    if (slotTime.getSlotFromTime().equalsIgnoreCase(bookedSlots.get(j).getFromTime()) && slotTime.getSlotToTime().equalsIgnoreCase(bookedSlots.get(j).getToTime())) {
                        if (bookedSlots.get(j).getBookedStatusId().equalsIgnoreCase("3")) {
                            slotTimes.get(i).status = DateObject.STATUS_BOOKED;
                            break;
                        } else if (bookedSlots.get(j).getBookedStatusId().equalsIgnoreCase("4")) {
                            slotTimes.get(i).status = DateObject.STATUS_CLOSED;
                            break;
                        } else {
                            slotTimes.get(i).status = DateObject.STATUS_CLOSED;
                            break;
                        }
                    }
                }
            }
        }
        SoccerFieldSlotsAdapter soccerFieldSlotsAdapter = new SoccerFieldSlotsAdapter(activity, slotTimes, onSlotSelectedListener, isAvailability);
        gvSlotTimes.setAdapter(soccerFieldSlotsAdapter);
        gvSlotTimes.setExpanded(true);
    }

    public interface OnSlotSelectedListener {
        void onSlotSelected(ManageSchedulesModel.SlotTime slotTime);
    }

    @Override
    public void onPagerItemClicked(int position) {
        rlFullImage.setVisibility(View.VISIBLE);
        vpFullImages.setCurrentItem(position);
    }

    private void openUrl(String url) {
        try {
            /*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);*/
            Intent intent = new Intent(activity, YouTubeActivity.class);
            intent.putExtra("urlToLoad", url);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
