package com.weekend.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.eftimoff.viewpagertransformers.AccordionTransformer;
import com.weekend.R;
import com.weekend.YouTubeActivity;
import com.weekend.adapters.ImagesPagerAdapter;
import com.weekend.adapters.SoccerFieldAdapter;
import com.weekend.adapters.ViewPagerFullImageAdapter;
import com.weekend.base.BaseFragment;
import com.weekend.base.WeekendApplication;
import com.weekend.interfaces.IPagerItemClicked;
import com.weekend.interfaces.IReportClick;
import com.weekend.models.AbuseReasonListModel;
import com.weekend.models.AddToFavoriteModel;
import com.weekend.models.PropertyDetailModel;
import com.weekend.models.RemoveFavoriteModel;
import com.weekend.models.SaveAbuseReasonModel;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.PopupUtil;
import com.weekend.utils.StaticUtil;
import com.weekend.views.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Hymavathi.kadali on 21/7/16.
 */
public class SoccerFieldParentDetailsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, IParser<WSResponse>, IReportClick, IPagerItemClicked {
    public static final String TAG = SoccerFieldParentDetailsFragment.class.getSimpleName();
    private static final String BUNDLE_PROPERTY_ID = "propertyId";
    private static final String BUNDLE_KEY_CHECK_IN = "check_in";
    @Bind(R.id.view_pager_soccer_field)
    ViewPager viewPagerSoccerField;
    @Bind(R.id.iv_favorite)
    ImageView ivFavorite;
    @Bind(R.id.iv_report)
    ImageView ivReport;
    @Bind(R.id.iv_verified)
    ImageView ivVerified;
    @Bind(R.id.iv_recommend)
    ImageView ivRecommend;
    @Bind(R.id.iv_next)
    ImageView ivNext;
    @Bind(R.id.iv_previous)
    ImageView ivPrevious;
    @Bind(R.id.rb_soccer_fields)
    RadioButton rbSoccerField;
    @Bind(R.id.rb_info)
    RadioButton rbInfo;
    /*@Bind(R.id.v_soccer_fields)
    View vSoccerField;
    @Bind(R.id.v_info)
    View vInfo;*/
    @Bind(R.id.rv_soccer_fields)
    RecyclerView rvSoccerFields;
    @Bind(R.id.tv_no_soccer_fields)
    CustomTextView tvNoSoccerFields;
    @Bind(R.id.rl_full_image)
    RelativeLayout rlFullImage;
    @Bind(R.id.vpFullImages)
    ViewPager vpFullImages;
    @Bind(R.id.tv_full_image_done)
    CustomTextView tvFullImageDone;

    /*Info*/

    @Bind(R.id.tv_chalet_desc)
    CustomTextView tvChaletDesc;
    @Bind(R.id.v_chalet_desc)
    View vChaletDesc;
    @Bind(R.id.tv_reviews)
    CustomTextView tvReviews;
    @Bind(R.id.tv_reviews_count)
    CustomTextView tvReviewsCount;
    @Bind(R.id.rl_reviews)
    RelativeLayout rlReviews;
    @Bind(R.id.v_reviews)
    View vReviews;
    @Bind(R.id.iv_time)
    ImageView ivTime;
    @Bind(R.id.tv_checkin_time)
    CustomTextView tvCheckinTime;
    @Bind(R.id.ll_checkin_time)
    LinearLayout llCheckinTime;
    @Bind(R.id.tv_checkout_time)
    CustomTextView tvCheckoutTime;
    @Bind(R.id.tv_chalet_title)
    CustomTextView tvChaletTitle;
    @Bind(R.id.v_chalet_title)
    View vChaletTitle;
    @Bind(R.id.tv_start_price)
    CustomTextView tvStartPrice;
    @Bind(R.id.rl_downpayment)
    RelativeLayout rlDownpayment;
    @Bind(R.id.ll_discount)
    LinearLayout llDiscount;
    @Bind(R.id.tv_discount)
    CustomTextView tvDiscount;
    @Bind(R.id.tv_coupon_date)
    CustomTextView tvCouponDate;
    @Bind(R.id.v_discount)
    View vDiscount;
    @Bind(R.id.rl_insurance)
    RelativeLayout rlInsurance;
    @Bind(R.id.v_insurance)
    View vInsurance;
    @Bind(R.id.tv_insurance_price)
    CustomTextView tvInsurancePrice;
    @Bind(R.id.tv_mobile_number_one)
    CustomTextView tvMobileNumberOne;
    @Bind(R.id.tv_mobile_number_two)
    CustomTextView tvMobileNumberTwo;
    @Bind(R.id.tv_email)
    CustomTextView tvEmail;
    @Bind(R.id.iv_twitter)
    ImageView ivTwitter;
    @Bind(R.id.iv_facebook)
    ImageView ivFacebook;
    @Bind(R.id.iv_instagram)
    ImageView ivInstagram;
    @Bind(R.id.iv_youtube)
    ImageView ivYoutube;
    @Bind(R.id.v_note)
    View vNote;
    @Bind(R.id.ll_cancellation_policy)
    LinearLayout llCancellationPolicy;
    @Bind(R.id.tv_cancellation_type)
    CustomTextView tvCancellationType;
    @Bind(R.id.imgPlaceHolder)
    ImageView imgPlaceHolder;
    @Bind(R.id.sv_row_chalet_details_info)
    ScrollView svInfo;
    @Bind(R.id.rlCheckInOut)
    RelativeLayout rlCheckInOut;

    private View rootView;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    private SoccerFieldAdapter soccerFieldAdapter;
    private ImagesPagerAdapter pagerAdapter;
    private PropertyDetailModel.Data propertyDetailPage;
    private List<PropertyDetailModel.PropertyImage> propertyImages;
    private List<PropertyDetailModel.PropertyDetail> propertyDetails;
    private List<PropertyDetailModel.PropertyChaletDetail> propertyChaletDetailList;
    private List<AbuseReasonListModel.Datum> abuseReasonList;
    private int curentPage;
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            curentPage = position + 1;
            if (curentPage <= 1) {
                ivPrevious.setAlpha(0.5f);
                ivNext.setAlpha(1.0f);
            } else if (curentPage >= propertyImages.size()) {
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
    private String propertyId, checkIn, cancellationDescription;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChaletParentDetailsFragment.
     */
    public static SoccerFieldParentDetailsFragment newInstance(String propertyId, String check_in) {
        SoccerFieldParentDetailsFragment fragment = new SoccerFieldParentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PROPERTY_ID, propertyId);
        bundle.putString(BUNDLE_KEY_CHECK_IN, check_in);
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
                rootView = inflater.inflate(R.layout.fragment_soccer_field_details, null);
            }
            ButterKnife.bind(this, rootView);
            activity.setTopbar(getString(R.string.field_details), true, false, true, true, false, false, false, false,
                    false, true, false,true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initialize();
                }
            }, Constants.FRAGMENT_TRANSACTION_DELAY);

        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtil.setTypefacerb(activity, rbSoccerField, 6);
        CommonUtil.setTypefacerb(activity, rbInfo, 6);
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
        localStorage = LocalStorage.getInstance(activity);
        propertyImages = new ArrayList<>();
        propertyChaletDetailList = new ArrayList<>();
        activity.ivNavbarLocationMap.setOnClickListener(this);
        rvSoccerFields.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rvSoccerFields.setLayoutManager(linearLayoutManager);
        requestAbuseReasonList();
        requestPropertyDetailPage(true);
        rbSoccerField.setChecked(true);

        rlInsurance.setVisibility(View.GONE);
        vInsurance.setVisibility(View.GONE);
        activity.ivNavbarShare.setOnClickListener(this);
    }

    private void getBundleData() {
        if (getArguments() != null) {
            propertyId = getArguments().getString(BUNDLE_PROPERTY_ID);
            checkIn = getArguments().getString(BUNDLE_KEY_CHECK_IN);
        }
    }

    @Override
    @OnClick({R.id.iv_favorite, R.id.iv_next, R.id.iv_previous, R.id.tv_reviews, R.id.iv_report, R.id.ll_cancellation_policy, R.id.tv_mobile_number_one, R.id.tv_mobile_number_two, R.id.tv_full_image_done})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_favorite:
                if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
                    if (propertyDetailPage != null) {
                        if (propertyDetails.get(0).getFavorite().equalsIgnoreCase("1")) {
                            requestRemoveFavorite();
                        } else {
                            requestAddToFavorite();
                        }
                    }
                } else {
                    activity.manageGuestUserAction(getString(R.string.please_login_first_to_add_or_remove_favorite));
                }
                break;
            case R.id.tv_reviews:
                activity.replaceFragment(ReviewsFragment.newInstance(propertyDetails.get(0).getPropertyid()), true, true, false, false);
                break;
            case R.id.iv_previous:
                if (curentPage > 1) {
                    viewPagerSoccerField.setCurrentItem(viewPagerSoccerField.getCurrentItem() - 1);
                }
                break;
            case R.id.iv_next:
                if (curentPage < propertyImages.size()) {
                    viewPagerSoccerField.setCurrentItem(viewPagerSoccerField.getCurrentItem() + 1);
                }
                break;
            case R.id.ll_cancellation_policy:
                PopupUtil.showRefundPolicy(activity, "", propertyDetails.get(0).getPolicyTypeArabic(), propertyDetails.get(0).getDescriptionArabic(), cancellationDescription, propertyDetails.get(0).getRefulndPolicy(), true, null);
                break;
            case R.id.iv_navbar_location_map:
                activity.replaceFragment(SingleLocationMapFragment.newInstance(propertyDetailPage, "2"), true, true, false, false);
                break;
            case R.id.tv_mobile_number_one:
                CommonUtil.makeCall(activity, propertyDetails.get(0).getPropertyPhone1());
                break;
            case R.id.tv_mobile_number_two:
                CommonUtil.makeCall(activity, propertyDetails.get(0).getPropertyPhone2());
                break;
            case R.id.iv_report:
                onReportClickEvent();
                break;
            case R.id.tv_full_image_done:
                rlFullImage.setVisibility(View.GONE);
                viewPagerSoccerField.setCurrentItem(vpFullImages.getCurrentItem());
                break;
            case R.id.iv_navbar_share:
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                //String shareText = getString(R.string.soccer_field_name)+": " + (propertyDetailPage.getPropertyDetails().get(0).getTitle() + "").trim();
                String shareText = getString(R.string.thank_you_for_using_the_weekend_app) + "<br />"
                        + getString(R.string.name) + ": " + (propertyDetailPage.getPropertyDetails().get(0).getTitle() + "").trim();
                *//*shareText += "<br />" + getString(R.string.address) + ": ";
                if (!TextUtils.isEmpty(propertyDetailPage.getPropertyDetails().get(0).getAddress())) {
                    shareText += propertyDetailPage.getPropertyDetails().get(0).getAddress().trim();
                } else {
                    if (!TextUtils.isEmpty(propertyDetailPage.getPropertyDetails().get(0).getCity())) {
                        shareText += propertyDetailPage.getPropertyDetails().get(0).getCity().trim();
                    }
                    if (!TextUtils.isEmpty(propertyDetailPage.getPropertyDetails().get(0).getCountry())) {
                        shareText += ", " + propertyDetailPage.getPropertyDetails().get(0).getCountry();
                    }
                }*//*

                //Spanned linkToGoogle = Html.fromHtml("<A HREF=" + "\"" + "http://maps.google.com/maps?&z=10&q=" + propertyDetailPage.getPropertyDetails().get(0).getLatitude() + propertyDetailPage.getPropertyDetails().get(0).getLongitude() + "\"" + "> LINK TO GOOGLE MAP</a>");
                shareText += "<br />";

                String map = "http://maps.google.com/maps?&z=10&q=" + propertyDetailPage.getPropertyDetails().get(0).getLatitude() + "+" + propertyDetailPage.getPropertyDetails().get(0).getLongitude();
                //shareText += mapLocation;

                //String map = String.format(getString(R.string.link_to_google_map), propertyDetailPage.getPropertyDetails().get(0).getLatitude(), propertyDetailPage.getPropertyDetails().get(0).getLongitude());
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(shareText + map));
                startActivity(Intent.createChooser(intent, getString(R.string.share_with)));*/

                onShareClickEvent();
                break;
        }
    }

    private void onShareClickEvent() {
        String shareText = "<b>" + getString(R.string.thank_you_for_using_the_weekend_app) + "</b>&nbsp;" + "<br/>"
                + "<b>" + getString(R.string.name) + ": " + "</b>&nbsp;" + (propertyDetailPage.getPropertyDetails().get(0).getTitle() + "").trim();
        shareText += "<br />";
        String map = "http://maps.google.com/maps?&z=10&q=" + propertyDetailPage.getPropertyDetails().get(0).getLatitude() + "+" + propertyDetailPage.getPropertyDetails().get(0).getLongitude();
        String link = "<A HREF=" + "\"" + map + "\">" + map + "</A>";

        StaticUtil.sharingIntent(activity, shareText + link);
    }

    private void onReportClickEvent() {
        String name = "";
        name = String.format("%s %s", localStorage.getString(Constants.SP_KEY_FIRST_NAME, ""), localStorage.getString(Constants.SP_KEY_LAST_NAME, ""));
        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            PopupUtil.showReport(activity, name, abuseReasonList, (IReportClick) this);
        } else {
            activity.manageGuestUserAction(getString(R.string.please_login_first_to_submit_report));
        }
    }

    @Override
    @OnCheckedChanged({R.id.rb_soccer_fields, R.id.rb_info})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_soccer_fields:
                if (isChecked) {
                    onSoccerFieldEvent();
                }
                break;
            case R.id.rb_info:
                if (isChecked) {
                    onInfoEvent();
                }
                break;
        }
    }

    private void requestAddToFavorite() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USERID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTYID, propertyId);
        params.put(WSUtils.KEY_PROPERTYTYPEID, "2");
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_ADD_TO_FAVORITE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_ADD_TO_FAVORITE, this);
    }

    private void requestRemoveFavorite() {
        activity.showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USERID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTYID, propertyId);
        params.put(WSUtils.KEY_PROPERTYTYPEID, "2");
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_REMOVE_FAVORITE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_REMOVE_FAVORITE, this);
    }

    private void parseAddToFavorite(AddToFavoriteModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                ivFavorite.setSelected(!ivFavorite.isSelected());
                propertyDetails.get(0).setFavorite("1");
                Intent addFavorite = new Intent("CustomerAddFavorite");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isAdded", true);
                addFavorite.putExtras(bundle);
                activity.sendBroadcast(addFavorite);
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
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
                ivFavorite.setSelected(!ivFavorite.isSelected());
                propertyDetails.get(0).setFavorite("0");
                Intent removeFavorite = new Intent("CustomerRemoveFavorite");
                Bundle bundle = new Bundle();
                bundle.putBoolean("isRemoved", true);
                removeFavorite.putExtras(bundle);
                activity.sendBroadcast(removeFavorite);
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
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
        //activity.showHideProgress(false);
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

    private void onSoccerFieldEvent() {
        //vSoccerField.setVisibility(View.VISIBLE);
        //vInfo.setVisibility(View.INVISIBLE);
        rvSoccerFields.setVisibility(View.VISIBLE);
        svInfo.setVisibility(View.GONE);
    }

    private void onInfoEvent() {
        //vSoccerField.setVisibility(View.INVISIBLE);
        //vInfo.setVisibility(View.VISIBLE);
        rvSoccerFields.setVisibility(View.GONE);
        svInfo.setVisibility(View.VISIBLE);
    }

    private void setAdapter() {
        if (propertyChaletDetailList != null && propertyChaletDetailList.size() > 0) {
            tvNoSoccerFields.setVisibility(View.GONE);
            if (!svInfo.isShown()) {
                rvSoccerFields.setVisibility(View.VISIBLE);
            }
            soccerFieldAdapter = new SoccerFieldAdapter(activity, propertyChaletDetailList, new SoccerFieldAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String mString, int position) {
                    activity.replaceFragment(SoccerFieldChildDetailsFragment.newInstance(propertyDetailPage, position/*.getPropertyChalet().get(0).getPropertyChaletDetails().get(position)*/), true, true, false, false);
                }
            });
            soccerFieldAdapter.setBookingAndScheduleAvailability(propertyDetails.get(0).getShowSchedule().equalsIgnoreCase("Yes") ? true : false, propertyDetails.get(0).getOnlineBooking().equalsIgnoreCase("3") ? true : false);
            rvSoccerFields.setAdapter(soccerFieldAdapter);
        } else {
            tvNoSoccerFields.setVisibility(View.VISIBLE);
            rvSoccerFields.setVisibility(View.GONE);
        }
    }

    private void setPagerAdapter() {
        if (propertyImages != null && propertyImages.size() > 0) {
            imgPlaceHolder.setVisibility(View.GONE);
            pagerAdapter = new ImagesPagerAdapter(activity);
            pagerAdapter.setPropertyDetailListImages(propertyImages, this);
            viewPagerSoccerField.setOffscreenPageLimit(propertyImages.size());
            viewPagerSoccerField.setPageTransformer(true, new AccordionTransformer());
            viewPagerSoccerField.setAdapter(pagerAdapter);
            viewPagerSoccerField.removeOnPageChangeListener(onPageChangeListener);
            viewPagerSoccerField.addOnPageChangeListener(onPageChangeListener);
            viewPagerSoccerField.setPageTransformer(true, new AccordionTransformer());
            ivPrevious.setAlpha(0.5f);
            if (propertyImages != null && propertyImages.size() > 1) {
                ivNext.setAlpha(1.0f);
            } else {
                ivNext.setAlpha(0.5f);
            }

            //Full screen view pager
            ViewPagerFullImageAdapter fullImageAdapter = new ViewPagerFullImageAdapter(activity);
            fullImageAdapter.setPropertyDetailListImages(propertyImages);
            vpFullImages.setAdapter(fullImageAdapter);
            viewPagerSoccerField.setPageTransformer(true, new AccordionTransformer());
        } else {
            imgPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private String getServerDateFormat(String strDate) {
        String checkInTemp = strDate;
        if (!TextUtils.isEmpty(strDate)) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            SimpleDateFormat dateFormatterNew = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date checkInDate = dateFormatter.parse(strDate);
                checkInTemp = dateFormatterNew.format(checkInDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return checkInTemp;
    }

    private void requestSearchOption(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PROPERTY_TYPE_ID, "1");
        params.put(WSUtils.KEY_CITYID, activity.cityId);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_SEARCH_OPTION);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_SEARCH_OPTION, this);
    }

    private void requestPropertyDetailPage(boolean showDefaultProgress) {
        if (showDefaultProgress) {
            activity.showHideProgress(true);
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_PROPERTY_ID, propertyId);
        params.put(WSUtils.KEY_CHECK_IN, checkIn != null ? getServerDateFormat(checkIn) : "");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_PROPERTY_DETAIL_PAGE);
        wsUtils.WSRequest(activity, params, null, WSUtils.REQ_PROPERTY_DETAIL_PAGE, this);
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
                case WSUtils.REQ_PROPERTY_DETAIL_PAGE:
                    parsePropertyDetailPage((PropertyDetailModel) response);
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
                default:
                    activity.showHideProgress(false);
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

    private void parsePropertyDetailPage(PropertyDetailModel response) {
        activity.showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                propertyDetailPage = response.getData();
                setPropertyDetailPage();
            } else {
                CommonUtil.showSnackbar(getView(), response.getSettings().getMessage() + "");
            }
        }
    }

    private void setPropertyDetailPage() {
        activity.setTitle(propertyDetailPage.getPropertyDetails().get(0).getTitle());
        propertyImages = propertyDetailPage.getPropertyImages();
        propertyDetails = propertyDetailPage.getPropertyDetails();
        propertyChaletDetailList = propertyDetailPage.getPropertyChalet().get(0).getPropertyChaletDetails();
        setAdapter();
        setPropertyDetails();
        setPagerAdapter();
    }

    private void setPropertyDetails() {
        WeekendApplication.trackScreenView(getActivity(), getString(R.string.soccer_field) + ": " + propertyDetails.get(0).getTitle());
        final PropertyDetailModel.PropertyDetail propertyDetail = propertyDetails.get(0);
        PropertyDetailModel.DiscountCodeInfo discountCodeInfo = propertyDetailPage.getDiscountCodeInfo().get(0);
        rlCheckInOut.setVisibility(View.GONE);
        tvInsurancePrice.setVisibility(View.GONE);
        ivRecommend.setVisibility(propertyDetail.getRecommended().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
        ivVerified.setVisibility(propertyDetail.getVerified().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE));
        ivReport.setVisibility(View.VISIBLE/*propertyDetail.getIsFeatured().equalsIgnoreCase("Yes") ? (View.VISIBLE) : (View.GONE)*/);
        ivFavorite.setSelected(propertyDetail.getFavorite().equalsIgnoreCase("1"));
        /*Info*/
        if (!TextUtils.isEmpty(propertyDetail.getTitle())) {
            tvChaletTitle.setText(propertyDetail.getTitle());
            tvChaletTitle.setVisibility(View.VISIBLE);
            vChaletTitle.setVisibility(View.VISIBLE);
        } else {
            tvChaletTitle.setVisibility(View.GONE);
            vChaletTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(propertyDetail.getTotalReview()) && Integer.parseInt(propertyDetail.getTotalReview()) > 0) {
            tvReviews.setText(String.format("%s %s", getString(R.string.reviews), "(" + propertyDetail.getTotalReview() + ")"));
            tvReviewsCount.setText(propertyDetail.getRating());
            rlReviews.setVisibility(View.VISIBLE);
            vReviews.setVisibility(View.VISIBLE);
        } else {
            rlReviews.setVisibility(View.GONE);
            vReviews.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(propertyDetail.getPropertyDesc())) {
            tvChaletDesc.setText(propertyDetail.getPropertyDesc());
            tvChaletDesc.setVisibility(View.VISIBLE);
            vChaletDesc.setVisibility(View.VISIBLE);
        } else {
            tvChaletDesc.setText(R.string.there_is_no_description);
            tvChaletDesc.setVisibility(View.VISIBLE);
            vChaletDesc.setVisibility(View.VISIBLE);
//            tvChaletDesc.setVisibility(View.GONE);
//            vChaletDesc.setVisibility(View.GONE);
        }
        tvStartPrice.setText(propertyDetail.getPriceStartFrom());
        if (propertyDetailPage.getDiscountCodeInfo().size() > 0 && !TextUtils.isEmpty(discountCodeInfo.getDiscountCode())) {
            llDiscount.setVisibility(View.VISIBLE);
            vDiscount.setVisibility(View.VISIBLE);
            String amount = null;
            if (!discountCodeInfo.getDiscountType().equalsIgnoreCase("flat")) {//flat/%
                amount = " " + discountCodeInfo.getDiscountPrice() + " % ";
            } else {
                amount = " " + discountCodeInfo.getDiscountPrice() + " " + getString(R.string.currency) + " ";
            }
            String discount = getString(R.string.use_discount_coupon) + " " + discountCodeInfo.getDiscountCode() + " " + getString(R.string._to_get) + amount + getString(R.string.discount_on_your_booking);
            CommonUtil.changeColorOfString(activity, tvDiscount, discount, discountCodeInfo.getDiscountCode(), true, 35, 120, 201);
            tvCouponDate.setText(getString(R.string.coupon_is_valid_till) + " " + discountCodeInfo.getDiscountEndDate());
        } else {
            llDiscount.setVisibility(View.GONE);
            vDiscount.setVisibility(View.GONE);
        }

        SpannableString sPhone1 = new SpannableString(("\u202D" + propertyDetail.getPropertyPhone1()).trim());
        SpannableString sPhone2 = new SpannableString(("\u202D" + propertyDetail.getPropertyPhone2()).trim());
        tvMobileNumberOne.setText("\u202D" + propertyDetail.getPropertyPhone1());
        tvMobileNumberTwo.setText("\u202D" + propertyDetail.getPropertyPhone2());
        tvEmail.setText(propertyDetail.getPropertyEmail());
        if (TextUtils.isEmpty(propertyDetail.getInstagram())) {
            ivInstagram.setVisibility(View.GONE);
        } else {
            ivInstagram.setVisibility(View.VISIBLE);
            ivInstagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUrl(propertyDetail.getInstagram());
                }
            });
        }

        if (TextUtils.isEmpty(propertyDetail.getTwitter())) {
            ivTwitter.setVisibility(View.GONE);
        } else {
            ivTwitter.setVisibility(View.VISIBLE);
            ivTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUrl(propertyDetail.getTwitter());
                }
            });
        }
        if (TextUtils.isEmpty(propertyDetail.getFacebook())) {
            ivFacebook.setVisibility(View.GONE);
        } else {
            ivFacebook.setVisibility(View.VISIBLE);
            ivFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openUrl(propertyDetail.getFacebook());
                }
            });
        }
        if (CommonUtil.isEmpty(propertyDetail.getYoutube())) {
            ivYoutube.setVisibility(View.GONE);
        } else {
            ivYoutube.setVisibility(View.VISIBLE);
//            ImageUtil.loadImageYoutube(activity, StaticUtil.getYoutubeThumbnailUrlFromVideoUrl(propertyDetail.getYoutube()), ivYoutube);
            ivYoutube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    openUrl(propertyDetail.getYoutube());
                    openYouTubeUrl(propertyDetail.getYoutube());
                }
            });
        }
        if (!TextUtils.isEmpty(propertyDetail.getPolicyTypeArabic())) {
            tvCancellationType.setText(" " + propertyDetail.getPolicyTypeArabic());
            llCancellationPolicy.setVisibility(View.VISIBLE);
            vNote.setVisibility(View.VISIBLE);
        } else {
            llCancellationPolicy.setVisibility(View.GONE);
            vNote.setVisibility(View.GONE);
        }
        cancellationDescription = getString(R.string.cancellation_before) + " " + propertyDetail.getCancelBefore() + " " + propertyDetail.getCancelBeforeType() + " " + getString(R.string.of_check_in_time);
    }

    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openYouTubeUrl(String url) {
        try {
            Intent intent = new Intent(activity, YouTubeActivity.class);
            intent.putExtra("urlToLoad", url);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReportSubmitClick(String comment, String name, String reasonId) {
        requestSaveAbuseReason(comment, name, reasonId);
    }

    @Override
    public void onPagerItemClicked(int position) {
        rlFullImage.setVisibility(View.VISIBLE);
        vpFullImages.setCurrentItem(position);
    }
}
