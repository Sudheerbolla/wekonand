package com.weekend;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBSubscription;
import com.weekend.adapters.SpinnerISDCodeAdapter;
import com.weekend.base.WeekendApplication;
import com.weekend.fragments.ChaletBookingDetailsFragment;
import com.weekend.fragments.ChaletChildDetailsFragment;
import com.weekend.fragments.ChangeLocationFragment;
import com.weekend.fragments.ChangePasswordFragment;
import com.weekend.fragments.EditProfileFragment;
import com.weekend.fragments.HelpFragment;
import com.weekend.fragments.ListViewFragment;
import com.weekend.fragments.LoginFragment;
import com.weekend.fragments.MyBookingsFragment;
import com.weekend.fragments.MyFavoritesFragment;
import com.weekend.fragments.NotificationsFragment;
import com.weekend.fragments.RegisterWithUsFragment;
import com.weekend.fragments.ResetPasswordFragment;
import com.weekend.fragments.ReviewsFragment;
import com.weekend.fragments.SearchFragment;
import com.weekend.fragments.SearchResultFragment;
import com.weekend.fragments.SoccerFieldBookingDetailsFragment;
import com.weekend.fragments.SoccerFieldChildDetailsFragment;
import com.weekend.fragments.StaticPageFragment;
import com.weekend.gcm.PushNotificationModel;
import com.weekend.interfaces.WalletDataRefreshCallback;
import com.weekend.models.CreditModel;
import com.weekend.models.CustomerLoginModel;
import com.weekend.models.CustomerNotificationCountModel;
import com.weekend.models.CustomerRegisterModel;
import com.weekend.models.ISDCodeModel;
import com.weekend.models.LogoutModel;
import com.weekend.models.ResendOTPModel;
import com.weekend.models.ValidateOTPModel;
import com.weekend.qbutils.ChatService;
import com.weekend.qbutils.ChatServiceUtil;
import com.weekend.qbutils.UserLoginAndUpdateService;
import com.weekend.qbutils.dbUtils.DbHelper;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.FragmentUtils;
import com.weekend.utils.Log;
import com.weekend.utils.NotificationCountReceiver;
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


public class HomeActivity extends FragmentActivity implements View.OnClickListener, IParser<WSResponse> {
    public static final String TAG = HomeActivity.class.getSimpleName();
    @Bind(R.id.iv_navbar_rating)
    public ImageView ivNavbarRating;//Click event is in BookingDetailsFragment
    @Bind(R.id.iv_navbar_share)
    public ImageView ivNavbarShare;//Click event is in BookingDetailsFragment
    @Bind(R.id.iv_navbar_location_map)
    public ImageView ivNavbarLocationMap;
    // variable to track back device button click event time
    public String notificationCount = "";
    public String currentCity = "", cityId = "";//currentCity == current lat lng location city name
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.fl_content_frame)
    FrameLayout flContentFrame;
    @Bind(R.id.ll_navbar_container)
    LinearLayout llNavbarContainer;
    @Bind(R.id.fl_progress_bar)
    FrameLayout flProgressBar;
    @Bind(R.id.iv_navbar_menu)
    ImageView ivNavbarMenu;
    @Bind(R.id.iv_navbar_map)
    ImageView ivNavbarMap;
    @Bind(R.id.iv_navbar_back)
    ImageView ivNavbarBack;
    @Bind(R.id.tv_title)
    CustomTextView tvTitle;
    @Bind(R.id.iv_customer_service)
    ImageView ivCustomerService;
    @Bind(R.id.tv_skip)
    CustomTextView tvSkip;
    @Bind(R.id.ll_home)
    LinearLayout llHome;
    @Bind(R.id.ll_signup_signin)
    LinearLayout llSignupSignin;
    @Bind(R.id.ll_search)
    LinearLayout llSearch;
    @Bind(R.id.ll_credits)
    LinearLayout llCredits;
    @Bind(R.id.ll_loggedin_user)
    LinearLayout llLoggedinUser;
    @Bind(R.id.ll_my_favorites)
    LinearLayout llMyFavorites;
    @Bind(R.id.ll_my_bookings)
    LinearLayout llMyBookings;
    @Bind(R.id.ll_my_profile)
    LinearLayout llMyProfile;
    @Bind(R.id.ll_change_password)
    LinearLayout llChangePassword;
    @Bind(R.id.ll_about_us)
    LinearLayout llAboutUs;
    @Bind(R.id.ll_terms_conditions)
    LinearLayout llTermsConditions;
    @Bind(R.id.ll_privacy_policy)
    LinearLayout llPrivacyPolicy;
    @Bind(R.id.ll_faq)
    LinearLayout llFaq;
    @Bind(R.id.ll_help)
    LinearLayout llHelp;
    @Bind(R.id.ll_logout)
    LinearLayout llLogout;
    @Bind(R.id.ll_notification)
    LinearLayout ll_notification;
    @Bind(R.id.ll_chats)
    LinearLayout ll_chats;
    @Bind(R.id.tv_chats)
    CustomTextView tv_chats;

    @Bind(R.id.tv_credits)
    CustomTextView tvCredits;
    private LocalStorage localStorage;
    private WSFactory wsFactory = new WSFactory();
    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
                requestCustomerNotificationCount();
            }
        }
    };
    private long mLastClickTime = 0;
    private int backPressCount = 0, selectedISDCodeLength;
    private boolean isFromDeepLinking = false;
    private String mResetcode, navigateTo, loginEmailAddress = "", loginPassword = "", signUpEmailAddress = "", signUpPassword = "", signUpMobileNumber = "", selectedISDCode;
    private Fragment navigateToFrag;
    private boolean isFromApp = true, isRememberMeChecked = false;
    private Bundle notificationBundle;

    public FortCallBackManager fortCallback = null;

    //For load data on splash
    private Dialog splashDialog, otpDialog, logindialog, signUpDialog;
    private Handler mLoadingHandler;
    private Runnable mLoadingRunnable;
    private Splash splash;
    private List<ISDCodeModel.Datum> ISDCodeList;
    private Spinner spinner_isd;
    private CustomEditText edt_mobile_number;

    /**
     * For L--> R support
     * R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right
     * For R--> L support
     * R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left
     *
     * @param ft
     * @param reverseAnimation
     */
    private static void setCustomAnimation(FragmentTransaction ft, boolean reverseAnimation) {
        if (reverseAnimation) {
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        splash = new Splash(this);
        ButterKnife.bind(this);
        setChatIconBackground();
        startChatService();
        ISDCodeList = new ArrayList<>();
        getBundleData();
        checkDeepLinking();
        initialize();
        initilizePayFortSDK();
        killRunningApp();

        showSplashWindow();

       /* if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("notification_data")) {
            manageNotificationFlow(getIntent());
        } else {

        }*/
        //TODO manage notification flow
    }

    private void setChatIconBackground() {
        //    button.getBackground().setColorFilter(Color.rgb(40, 50, 60), PorterDuff.Mode.SRC_ATOP);
//        mDrawable.setColorFilter(new PorterDuffColorFilter(0xffff00, PorterDuff.Mode.MULTIPLY));
//        mDrawable.setColorFilter(new PorterDuffColorFilter(Color.rgb(105, 111, 116), PorterDuff.Mode.MULTIPLY));
//        Drawable mDrawable = getResources().getDrawable(R.mipmap.ic_chat_nav);
        Drawable mDrawable = ContextCompat.getDrawable(this, R.mipmap.ic_chat_nav);
        mDrawable.setColorFilter(new PorterDuffColorFilter(Color.rgb(105, 111, 116), PorterDuff.Mode.SRC_ATOP));
        tv_chats.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawable, null);
    }

    private void initilizePayFortSDK() {
        fortCallback = FortCallback.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(notificationReceiver, new IntentFilter(Constants.NOTIFICATION_COUNT_BROADCAST));
        StaticUtil.changeLocaleConfiguration(getApplicationContext(), getString(R.string.lang_id));
    }

    @Override
    protected void onStart() {
        super.onStart();
        startNotificationAlarm();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onDestroy() {
        if (splash != null)
            splash.onDestroy();
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void getBundleData() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("isFromApp")) {
            isFromApp = getIntent().getExtras().getBoolean("isFromApp");
        }
        if (getIntent().getExtras() != null) {
            notificationBundle = getIntent().getExtras();
            if (notificationBundle.containsKey("push_notification")) {
                PushNotificationModel pushNotificationModel = (PushNotificationModel) notificationBundle.getSerializable("push_notification");
                if (notificationBundle.containsKey("navigate_to")) {
                    navigateTo = notificationBundle.getString("navigate_to");
                    if (navigateTo.equalsIgnoreCase(Constants.CUSTOMER_ACCOUNT_ACTIVATE_FRAGMENT)) {
                        navigateToFrag = LoginFragment.newInstance();
                    } else if (navigateTo.equalsIgnoreCase(Constants.BOOKING_CONFIRMED_CANCELLED_CHALET_FRAGMENT)) {
                        navigateToFrag = ChaletBookingDetailsFragment.newInstance(pushNotificationModel);
                    } else if (navigateTo.equalsIgnoreCase(Constants.BOOKING_CONFIRMED_CANCELLED_SOCCER_FRAGMENT)) {
                        navigateToFrag = SoccerFieldBookingDetailsFragment.newInstance(pushNotificationModel);
                    } else if (navigateTo.equalsIgnoreCase(Constants.PROPERTY_OWNER_REPLY_COMMENT_FRAGMENT)) {
                        navigateToFrag = ReviewsFragment.newInstance(pushNotificationModel);
                    }
                }
            } else if (notificationBundle.containsKey("isFromChat")) {
//                if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("isFromChat")) {
//                    isFromApp = getIntent().getExtras().getBoolean("isFromChat");
//                }
                notificationBundle.getString("message");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openChatActivity(true);
                    }
                }, 4500);
            }
        }
    }

    private void initialize() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        localStorage = LocalStorage.getInstance(this);

        if (localStorage.getBoolean(Constants.SP_KEY_IS_USER_FIRST_TIME, true)) {
            localStorage.putBoolean(Constants.SP_KEY_IS_USER_FIRST_TIME, false);
            replaceFragment(RegisterWithUsFragment.newInstance(), false, false, false, true);
        } else if (isFromDeepLinking) {
            replaceFragment(ResetPasswordFragment.newInstance(mResetcode, ""), false, false, false, true);
        } else {
            if (!isFromApp) {
                hideSplashScreen();
                replaceFragment(navigateToFrag, false, false, false, true);
            } else {
                if (!localStorage.getBoolean(Constants.SP_KEY_AUTO_LOGIN, true)) {
                    localStorage.putString(Constants.SP_KEY_USER_ID, "");
                }
                replaceFragment(ListViewFragment.newInstance(), false, false, false, true);
            }
        }

        if (!CommonUtil.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            setCreditText(localStorage.getString(Constants.SP_KEY_CREDITS, ""));
        } else {
            setCreditText("0.00");
        }
        cityId = LocalStorage.getInstance(this).getString("cityId", "");
    }

    private void checkDeepLinking() {
        String scheme;
        try {
            Uri data = getIntent().getData();
            Log.e("data ", data + "");
            if (data != null) {
                scheme = data.getScheme();
                Log.e("Reset", "scheme= " + scheme);
                if (scheme != null) {
                    isFromDeepLinking = true;
                    mResetcode = data.getQueryParameter("resetcode");
                    Log.e("mResetcode ", mResetcode + "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void killRunningApp() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    /**
     * @param title
     * @param isNavbarMenuVisible
     * @param isNavbarMapVisible
     * @param isNavbarBackVisible
     * @param isNavbarShareVisible
     * @param isNavbarRatingVisible
     * @param isNavbarSearchVisible
     * @param isNavbarNotificationVisible
     * @param isNavbarFilterVisible
     * @param isNavbarSortingVisible
     * @param isNavbarLocationMapVisible
     * @param isNavbarSkipVisible
     */
    public void setTopbar(String title, boolean isNavbarMenuVisible, boolean isNavbarMapVisible, boolean isNavbarBackVisible, boolean isNavbarShareVisible, boolean isNavbarRatingVisible, boolean isNavbarSearchVisible,
                          boolean isNavbarNotificationVisible, boolean isNavbarFilterVisible, boolean isNavbarSortingVisible, boolean isNavbarLocationMapVisible, boolean isNavbarSkipVisible) {
        setTopbar(title, isNavbarMenuVisible, isNavbarMapVisible, isNavbarBackVisible, isNavbarShareVisible, isNavbarRatingVisible, isNavbarLocationMapVisible, isNavbarSkipVisible, false);
    }

    /**
     * @param title
     * @param isNavbarMenuVisible
     * @param isNavbarMapVisible
     * @param isNavbarBackVisible
     * @param isNavbarShareVisible
     * @param isNavbarRatingVisible
     * @param isNavbarSearchVisible
     * @param isNavbarNotificationVisible
     * @param isNavbarFilterVisible
     * @param isNavbarSortingVisible
     * @param isNavbarLocationMapVisible
     * @param isNavbarSkipVisible
     */
    public void setTopbar(String title, boolean isNavbarMenuVisible, boolean isNavbarMapVisible, boolean isNavbarBackVisible, boolean isNavbarShareVisible, boolean isNavbarRatingVisible, boolean isNavbarSearchVisible,
                          boolean isNavbarNotificationVisible, boolean isNavbarFilterVisible, boolean isNavbarSortingVisible, boolean isNavbarLocationMapVisible, boolean isNavbarSkipVisible, boolean isChatIconVisible) {
        setTopbar(title, isNavbarMenuVisible, isNavbarMapVisible, isNavbarBackVisible, isNavbarShareVisible, isNavbarRatingVisible, isNavbarLocationMapVisible, isNavbarSkipVisible, isChatIconVisible);
    }

    public void setTopbar(String title, boolean isNavbarMenuVisible, boolean isNavbarMapVisible, boolean isNavbarBackVisible, boolean isNavbarShareVisible, boolean isNavbarRatingVisible, boolean isNavbarLocationMapVisible, boolean isNavbarSkipVisible, boolean isChatIconVisible) {

        tvTitle.setText(title);

        //Topbar Btns: On right of screen

        if (isNavbarMenuVisible) {
            ivNavbarMenu.setVisibility(View.VISIBLE);
        } else {
            ivNavbarMenu.setVisibility(View.GONE);
        }

        if (isNavbarMapVisible) {
            ivNavbarMap.setVisibility(View.VISIBLE);
        } else {
            ivNavbarMap.setVisibility(View.GONE);
        }

        //Topbar Btns: On left of screen

        if (isNavbarBackVisible) {
            ivNavbarBack.setVisibility(View.VISIBLE);
        } else {
            ivNavbarBack.setVisibility(View.GONE);
        }

        if (isNavbarShareVisible) {
            ivNavbarShare.setVisibility(View.VISIBLE);
        } else {
            ivNavbarShare.setVisibility(View.GONE);
        }

        if (isNavbarRatingVisible) {
            ivNavbarRating.setVisibility(View.VISIBLE);
        } else {
            ivNavbarRating.setVisibility(View.GONE);
        }

        if (isNavbarLocationMapVisible) {
            ivNavbarLocationMap.setVisibility(View.VISIBLE);
        } else {
            ivNavbarLocationMap.setVisibility(View.GONE);
        }

        if (isNavbarSkipVisible) {
            tvSkip.setVisibility(View.VISIBLE);
        } else {
            tvSkip.setVisibility(View.GONE);
        }

        if (isChatIconVisible) {
            ivCustomerService.setVisibility(View.VISIBLE);
        } else {
            ivCustomerService.setVisibility(View.GONE);
        }

    }

    public void setTitle(String text) {
        tvTitle.setText(text);
    }

    public void manageTopbarUI() {
        ivNavbarBack.setVisibility(View.INVISIBLE);
    }

    public void updateUI(boolean isUserLoggedIn) {
        if (isUserLoggedIn) {
            llLoggedinUser.setVisibility(View.VISIBLE);
            llLogout.setVisibility(View.VISIBLE);
            ll_notification.setVisibility(View.VISIBLE);
            llSignupSignin.setVisibility(View.GONE);
            llCredits.setVisibility(View.VISIBLE);
            requestGetCredit();
        } else {
            llLoggedinUser.setVisibility(View.GONE);
            llLogout.setVisibility(View.GONE);
            ll_notification.setVisibility(View.GONE);
            llSignupSignin.setVisibility(View.VISIBLE);
//            llCredits.setVisibility(View.GONE);
            setCreditText("0.00");
            llCredits.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2112) {
            fortCallback.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == Constants.REQUEST_LOCATION_LISTVIEW_FRAGMENT) {
            onActivityRequestResult(requestCode, resultCode, data, ListViewFragment.class.getSimpleName());
        } else if (requestCode == Constants.REQUEST_LOCATION_SEARCHRESULT_FRAGMENT) {
            onActivityRequestResult(requestCode, resultCode, data, SearchResultFragment.class.getSimpleName());
        }
    }

    private void onActivityRequestResult(int requestCode, int resultCode, Intent data, String fragmentName) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getFragments().size() > 0) {
                for (int i = 0; i < fm.getFragments().size(); i++) {
                    Fragment fragment = fm.getFragments().get(i);
                    if (fragment != null && fragment.getClass().getSimpleName().equalsIgnoreCase(fragmentName)) {
                        fragment.onActivityResult(requestCode, resultCode, data);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFragmentAtTop() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getFragments().size() > 0) {
                for (int i = fm.getFragments().size() - 1; i >= 0; i--) {
                    Fragment fragment = fm.getFragments().get(i);
                    if (fragment != null) {
                        return fragment.getClass().getSimpleName();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    @OnClick({//Navigation Topbar events
            R.id.iv_navbar_menu, R.id.iv_navbar_map, R.id.iv_navbar_back, /*R.id.iv_navbar_search, R.id.iv_navbar_notification,*/R.id.ll_notification, R.id.tv_skip, R.id.iv_customer_service,
            //Drawer menu events
            R.id.ll_home, R.id.ll_signup_signin, R.id.ll_search, R.id.ll_credits, R.id.ll_my_favorites, R.id.ll_my_bookings, R.id.ll_my_profile, R.id.ll_change_password,
            R.id.ll_about_us, R.id.ll_terms_conditions, R.id.ll_privacy_policy, R.id.ll_faq, R.id.ll_help, R.id.ll_logout, R.id.ll_chats})
    public void onClick(View v) {
        String selectedFragment = ((WeekendApplication) getApplication()).selectedFragment;
        switch (v.getId()) {
            //Navigation Topbar events
            case R.id.iv_navbar_menu:
                StaticUtil.hideSoftKeyboard(this);
                mDrawerLayout.openDrawer(llNavbarContainer);
                break;
            case R.id.iv_navbar_map:
                replaceFragment(ChangeLocationFragment.newInstance(), true, true, false, false);
                break;
            case R.id.iv_customer_service:
//                CommonUtil.showShortTimeToast(this, "Chat Module Coming Soon....");
//                replaceFragment(ChatListFragment.newInstance(), true, true, false, false);
                openChatActivity(false);
                break;
            case R.id.iv_navbar_back:
                onBackPressed();
                break;
//            case R.id.iv_navbar_search:
//                replaceFragment(SearchFragment.newInstance(), true, true, false, false);
//                break;
//            case R.id.iv_navbar_notification:
//                notificationCount = "";
//                tvNotificationCount.setText(notificationCount);
//                tvNotificationCount.setVisibility(View.GONE);
//                replaceFragment(NotificationsFragment.newInstance(), true, true, false, false);
//                break;
            case R.id.tv_skip:
                replaceFragment(ListViewFragment.newInstance(), false, false, false, true);
                break;

            //Drawer menu events
            case R.id.ll_home:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(ListViewFragment.TAG)) {
                    replaceFragment(ListViewFragment.newInstance(), false, false, false, true);
                }
                break;
            case R.id.ll_signup_signin:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && (!selectedFragment.equalsIgnoreCase(LoginFragment.TAG) || !selectedFragment.equalsIgnoreCase(LoginFragment.TAG + "-TokenExpire"))) {
                    replaceFragment(LoginFragment.newInstance(false), false, false, false, true);
                }
                break;
            case R.id.ll_search:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(SearchFragment.TAG)) {
                    replaceFragment(SearchFragment.newInstance(), false, false, false, true);
                }
                break;
            case R.id.ll_credits:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(StaticPageFragment.TAG + "-" + getString(R.string.credit))) {
                    replaceFragment(StaticPageFragment.newInstance(getString(R.string.credit), false), false, false, false, true);
                }
                break;
            case R.id.ll_my_favorites:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(MyFavoritesFragment.TAG)) {
                    replaceFragment(MyFavoritesFragment.newInstance(), false, false, false, true);
                }
                break;
            case R.id.ll_my_bookings:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(MyBookingsFragment.TAG)) {
                    replaceFragment(MyBookingsFragment.newInstance(), false, false, false, true);
                }
                break;
            case R.id.ll_my_profile:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(EditProfileFragment.TAG)) {
                    replaceFragment(EditProfileFragment.newInstance(), false, false, false, true);
                }
                break;
            case R.id.ll_change_password:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(ChangePasswordFragment.TAG)) {
                    replaceFragment(ChangePasswordFragment.newInstance(), false, false, false, true);
                }
                break;
            case R.id.ll_about_us:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(StaticPageFragment.TAG + "-" + getString(R.string.about_us))) {
                    replaceFragment(StaticPageFragment.newInstance(getString(R.string.about_us), false), false, false, false, true);
                }
                break;
            case R.id.ll_terms_conditions:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(StaticPageFragment.TAG + "-" + getString(R.string.terms_conditions))) {
                    replaceFragment(StaticPageFragment.newInstance(getString(R.string.terms_conditions), false), false, false, false, true);
                }
                break;
            case R.id.ll_privacy_policy:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(StaticPageFragment.TAG + "-" + getString(R.string.privacy_policy))) {
                    replaceFragment(StaticPageFragment.newInstance(getString(R.string.privacy_policy), false), false, false, false, true);
                }
                break;
            case R.id.ll_faq:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(StaticPageFragment.TAG + "-" + getString(R.string.faqs))) {
                    replaceFragment(StaticPageFragment.newInstance(getString(R.string.faqs), false), false, false, false, true);
                }
                break;
            case R.id.ll_help:
                mDrawerLayout.closeDrawers();
                if (!TextUtils.isEmpty(selectedFragment) && !selectedFragment.equalsIgnoreCase(HelpFragment.TAG)) {
                    replaceFragment(HelpFragment.newInstance(), false, false, false, true);
                }
                break;
            case R.id.ll_logout:
                mDrawerLayout.closeDrawers();
                onLogoutEvent();
                break;
            case R.id.ll_notification:
                mDrawerLayout.closeDrawers();
                notificationCount = "";
                replaceFragment(NotificationsFragment.newInstance(), false, false, false, true);
                break;
            case R.id.ll_chats:
                mDrawerLayout.closeDrawers();
                Intent chatintent = new Intent(this, ChatActivity.class);
                chatintent.putExtra("showChatsList", true);
                startActivity(chatintent);
                break;
        }
    }

    private void openChatActivity(boolean isFromNotification) {
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("isFromNotification", isFromNotification);
        startActivity(chatIntent);
        overridePendingTransition(R.anim.enter_from_bottom, R.anim.exit_to_bottom);
    }

    private void onLogoutEvent() {
        PopupUtil.showAlert(this, getString(R.string.are_you_sure_you_want_to_logout), "", "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogout();
            }
        });
    }

    private void logout() {
        localStorage.clearLocalStorage();
        currentCity = "";
        cityId = "";
        updateUI(false);
        logoutFromQB();
        replaceFragment(LoginFragment.newInstance(false), false, true, false, true);
        NotificationManager notificationManager = (NotificationManager) HomeActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private void logoutFromQB() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (QBChatService.getInstance().isLoggedIn()) {
                    ChatServiceUtil.getInstance().logout(new QBEntityCallback<Void>() {
                        @Override
                        public void onSuccess(Void o, Bundle bundle) {
                            QBChatService.getInstance().destroy();
                            removeSubscription();
                            closeDbAndReStartChatService();
                        }

                        @Override
                        public void onError(QBResponseException e) {

                        }
                    });
                }
            }
        });
    }

    private void closeDbAndReStartChatService() {
        new DbHelper(this).closeDb();
        stopService(new Intent(this, ChatService.class));
        startService(new Intent(this, ChatService.class));
    }

    private void removeSubscription() {
        QBPushNotifications.getSubscriptions().performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {
                for (QBSubscription subscription : subscriptions) {
                    if (subscription.getDevice().getId().equals(localStorage.getString(Constants.DEVICE_TOKEN, ""))) {
                        QBPushNotifications.deleteSubscription(subscription.getId()).performAsync(new QBEntityCallback<Void>() {

                            @Override
                            public void onSuccess(Void aVoid, Bundle bundle) {

                            }

                            @Override
                            public void onError(QBResponseException errors) {

                            }
                        });
                        break;
                    }
                }
            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(llNavbarContainer)) {
            mDrawerLayout.closeDrawers();
        } else {
            showHideProgress(false);
            StaticUtil.hideSoftKeyboard(this);
            FragmentManager fm = getSupportFragmentManager();
            String selectedFragment = ((WeekendApplication) getApplication()).selectedFragment;
            if (fm.getBackStackEntryCount() > 0) {
                if (!TextUtils.isEmpty(selectedFragment)) {
                    if (selectedFragment.equalsIgnoreCase(ChaletChildDetailsFragment.TAG + "-Calendar")) {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("OnBackPress"));
                    } else if (selectedFragment.equalsIgnoreCase(SoccerFieldChildDetailsFragment.TAG + "-Calendar")) {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("OnBackPress"));
                    } else if (selectedFragment.equalsIgnoreCase(LoginFragment.TAG + "-TokenExpire")) {
                        manageApplicationExit(true);
                        //CommonUtil.showSnackbar(mDrawerLayout, getString(R.string.token_expired_please_login_to_continue));
                    } else if (selectedFragment.equalsIgnoreCase(SearchResultFragment.TAG + "-MapView")) {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("OnBackPress"));
                    } else {
                        fm.popBackStackImmediate();
                    }
                } else {
                    fm.popBackStackImmediate();
                }
            } else {
                if (!TextUtils.isEmpty(selectedFragment)) {
                    if (selectedFragment.equalsIgnoreCase(ListViewFragment.TAG + "-MapView")) {
                        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("OnBackPress"));
                    } else if (notificationBundle != null && (selectedFragment.equalsIgnoreCase(ChaletBookingDetailsFragment.TAG + "-PushNotification")
                            || selectedFragment.equalsIgnoreCase(SoccerFieldBookingDetailsFragment.TAG + "-PushNotification")
                            || selectedFragment.equalsIgnoreCase(ReviewsFragment.TAG + "-PushNotification"))) {
                        replaceFragment(ListViewFragment.newInstance(), false, true, false, true);
                    } else {
                        manageApplicationExit(false);
                    }
                } else {
                    manageApplicationExit(false);
                }
            }
        }
    }

    /************************************
     * Fragment management
     ***********************************************/

    private void manageApplicationExit(boolean exit) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000 && backPressCount >= 1) {
            if (exit) {
                finish();
                System.exit(0);
            } else {
                super.onBackPressed();
            }
        } else {
            CommonUtil.showSnackbar(mDrawerLayout, getString(R.string.press_again_to_exit));
        }
        backPressCount++;
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    public void showHideProgress(final boolean showProgress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flProgressBar.setVisibility(showProgress ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * @param fragment
     * @param needToAddBackStack
     * @param needAniamtion
     * @param clearStack
     */
    public void replaceFragment(final Fragment fragment, final boolean needToAddBackStack, final boolean needAniamtion, final boolean reverseAnimation, final boolean clearStack, final boolean delay) {
        StaticUtil.hideSoftKeyboard(this);
        showHideProgress(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0 && clearStack) {
            FragmentUtils.sDisableFragmentAnimations = true;

            fragmentManager.popBackStackImmediate(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentUtils.sDisableFragmentAnimations = false;
        }

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (needAniamtion) {
            FragmentUtils.sDisableFragmentAnimations = false;
            setCustomAnimation(fragmentTransaction, reverseAnimation);
        } else {
            FragmentUtils.sDisableFragmentAnimations = true;
        }

/*
        final boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragment.getClass().getSimpleName(), 0);
*/

        if (delay) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fragmentTransaction.replace(R.id.fl_content_frame, fragment);

                    if (needToAddBackStack) {
                        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                    }
                    fragmentTransaction.commitAllowingStateLoss();
                    StaticUtil.hideSoftKeyboard(HomeActivity.this);
                }
            }, Constants.FRAGMENT_TRANSACTION_SNACKBAR_DELAY);
        } else {
            fragmentTransaction.replace(R.id.fl_content_frame, fragment);

            if (needToAddBackStack) {
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
            fragmentTransaction.commitAllowingStateLoss();
//            StaticUtil.hideSoftKeyboard(HomeActivity.this);
        }
    }

    public void replaceFragment(Fragment fragment, final boolean needToAddBackStack, final boolean needAniamtion, final boolean reverseAnimation, final boolean clearStack) {
        replaceFragment(fragment, needToAddBackStack, needAniamtion, reverseAnimation, clearStack, false);
    }

    /**
     * @param fragment
     * @param needToAddBackStack
     * @param needAniamtion
     * @param clearStack
     */
    public void addFragment(Fragment fragment, final boolean needToAddBackStack, final boolean needAniamtion, final boolean reverseAnimation, final boolean clearStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        showHideProgress(false);
        if (fragmentManager.getBackStackEntryCount() > 0 && clearStack) {
            FragmentUtils.sDisableFragmentAnimations = true;
            fragmentManager.popBackStackImmediate(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentUtils.sDisableFragmentAnimations = false;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (needAniamtion) {
            FragmentUtils.sDisableFragmentAnimations = false;
            setCustomAnimation(fragmentTransaction, reverseAnimation);
        } else {
            FragmentUtils.sDisableFragmentAnimations = true;
        }

        fragmentTransaction.add(R.id.fl_content_frame, fragment);

        if (needToAddBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commitAllowingStateLoss();
        StaticUtil.hideSoftKeyboard(this);
    }

    public void clearBackStack(FragmentManager fragmentManager) {
        try {
            FragmentUtils.sDisableFragmentAnimations = true;
            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }
            FragmentUtils.sDisableFragmentAnimations = false;
        } catch (Exception e) {
        }
    }

    public void clearStack() {
        /*
         * Here we are clearing back stack fragment entries
         */
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();

        if (backStackEntry > 0) {
            FragmentUtils.sDisableFragmentAnimations = true;
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
            FragmentUtils.sDisableFragmentAnimations = false;
        }

        /*
         * Here we are removing all the fragment that are shown here
         */
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    public void popBack() {
        popBack(false);
    }

    public void popBack(boolean delay) {
        final FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            StaticUtil.hideSoftKeyboard(this);
            if (delay) {
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        fm.popBackStackImmediate();
                    }
                }, 2000);
            } else {
                fm.popBackStackImmediate();
            }
        }
    }

    public void requestCustomerNotificationCount() {
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_NOTIFICATION_COUNT);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_CUSTOMER_NOTIFICATION_COUNT, this);
    }

    public void requestLogout() {
        showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_LOG_OUT);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_LOG_OUT, this);
    }

    public void updateNotificationCount(String count) {
//        notificationCount = count;
//        if (!TextUtils.isEmpty(count)) {
//            tvNotificationCount.setText(count);
//        }
//
//        if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, "")) && !TextUtils.isEmpty(notificationCount) && !notificationCount.equalsIgnoreCase("0")) {
//            if (ivNavbarNotification.isShown())
//                tvNotificationCount.setVisibility(View.VISIBLE);
//        } else {
//            tvNotificationCount.setVisibility(View.GONE);
//        }
    }

    private void continueBooking() {
        if (getSupportFragmentManager().findFragmentById(R.id.fl_content_frame) instanceof ChaletChildDetailsFragment) {
            ChaletChildDetailsFragment chaletChildDetailsFragment = (ChaletChildDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content_frame);
            chaletChildDetailsFragment.loginOrRegisterSuccess();
        } else if (getSupportFragmentManager().findFragmentById(R.id.fl_content_frame) instanceof SoccerFieldChildDetailsFragment) {
            SoccerFieldChildDetailsFragment soccerFieldChildDetailsFragment = (SoccerFieldChildDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fl_content_frame);
            soccerFieldChildDetailsFragment.loginOrRegisterSuccess();
        }
    }

    private void showSignupDialog() {
        StaticUtil.setWindowDimensions(this);

        signUpDialog = new Dialog(this);
        signUpDialog.setCancelable(true);
        signUpDialog.setCanceledOnTouchOutside(false);
        signUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signUpDialog.setContentView(R.layout.signup_dialog);

        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = signUpDialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        final CustomEditText edt_email = (CustomEditText) signUpDialog.findViewById(R.id.edt_email);
        final CustomEditText edt_password = (CustomEditText) signUpDialog.findViewById(R.id.edt_password);
        edt_mobile_number = (CustomEditText) signUpDialog.findViewById(R.id.edt_mobile_number);
        spinner_isd = (Spinner) signUpDialog.findViewById(R.id.spinner_isd);

        CustomTextView tv_login = (CustomTextView) signUpDialog.findViewById(R.id.tv_login);
        CustomTextView tv_register = (CustomTextView) signUpDialog.findViewById(R.id.tv_register);

        edt_mobile_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.toString().startsWith("0")) {
                    edt_mobile_number.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner_isd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                //edtMobileNumber.setText("");
                selectedISDCode = ((CustomTextView) v).getText().toString().trim();
                selectedISDCodeLength = CommonUtil.getIsdCodeCount(selectedISDCode);
                if (selectedISDCodeLength == 2) {
                    CommonUtil.setMaxLength(edt_mobile_number, 10);
                } else if (selectedISDCodeLength == 3) {
                    CommonUtil.setMaxLength(edt_mobile_number, 9);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if (ISDCodeList != null && ISDCodeList.size() > 3) {
            StaticUtil.setHeightToSpinner(spinner_isd, 400);
        }
        requestGetISDCode();

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpDialog.dismiss();
                showLoginDialog();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_email.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_email_address));
                } else if (!StaticUtil.isValidEmail(edt_email.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_valid_email_address));
                } else if (TextUtils.isEmpty(edt_password.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_password));
                } else if (TextUtils.isEmpty(edt_mobile_number.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_mobile_number));
                } else if (!PhoneUtil.validMobileNumber(edt_mobile_number.getText().toString().trim()) ||
                        (edt_mobile_number.getText().toString().trim().length() + selectedISDCodeLength) != 12) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_valid_mobile_number));
                } else {
                    signUpEmailAddress = edt_email.getText().toString().trim();
                    signUpPassword = edt_password.getText().toString().trim();
                    signUpMobileNumber = edt_mobile_number.getText().toString().trim();
                    requestForSignupWS();
                }
            }
        });

        signUpDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showHideProgress(false);
            }
        });

        signUpDialog.show();
    }

    private void showLoginDialog() {
        StaticUtil.setWindowDimensions(this);

        logindialog = new Dialog(this);
        logindialog.setCancelable(true);
        logindialog.setCanceledOnTouchOutside(false);
        logindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logindialog.setContentView(R.layout.login_dialog);

        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = logindialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        final CustomEditText edt_email = (CustomEditText) logindialog.findViewById(R.id.edt_email);
        final CustomEditText edt_password = (CustomEditText) logindialog.findViewById(R.id.edt_password);
        CustomTextView tv_login = (CustomTextView) logindialog.findViewById(R.id.tv_login);
        CustomTextView tv_signup = (CustomTextView) logindialog.findViewById(R.id.tv_signup);

        final CustomTextView tv_remember_me = (CustomTextView) logindialog.findViewById(R.id.tv_remember_me);
        tv_remember_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_remember_me.setSelected(!tv_remember_me.isSelected());
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_email.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_email_address));
                } else if (!StaticUtil.isValidEmail(edt_email.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_valid_email_address));
                } else if (TextUtils.isEmpty(edt_password.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_password));
                } else {
                    loginEmailAddress = edt_email.getText().toString().trim();
                    loginPassword = edt_password.getText().toString().trim();
                    isRememberMeChecked = tv_remember_me.isSelected();
                    requestForLoginWS();
                }
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logindialog.dismiss();
                showSignupDialog();
            }
        });

        logindialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showHideProgress(false);
            }
        });
        logindialog.show();
    }

    public void showLoginSignupDialog(String message) {
        StaticUtil.setWindowDimensions(this);

        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_signup_dialog);

        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = dialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        CustomTextView txtTitle = (CustomTextView) dialog.findViewById(R.id.txtTitle);
        CustomTextView txtMessage = (CustomTextView) dialog.findViewById(R.id.txtMessage);
        CustomTextView txtLogin = (CustomTextView) dialog.findViewById(R.id.txtLogin);
        CustomTextView txtSignup = (CustomTextView) dialog.findViewById(R.id.txtSignup);

        txtMessage.setText(message);

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showSignupDialog();
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showLoginDialog();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showHideProgress(false);
            }
        });
        dialog.show();
    }

    private void showOtpDialog(CustomerLoginModel.Datum datum) {
        StaticUtil.setWindowDimensions(this);

        otpDialog = new Dialog(this);
        otpDialog.setCancelable(true);
        otpDialog.setCanceledOnTouchOutside(false);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.setContentView(R.layout.otp_dialog);

        WindowManager.LayoutParams mWindowLayoutParams = new WindowManager.LayoutParams();
        Window mWindow = otpDialog.getWindow();
        mWindowLayoutParams.copyFrom(mWindow.getAttributes());
        mWindowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = Constants.SCREEN_WIDTH - ((Constants.SCREEN_WIDTH) / 6);
        mWindow.setAttributes(mWindowLayoutParams);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        final CustomEditText edt_otp = (CustomEditText) otpDialog.findViewById(R.id.edt_otp);
        CustomTextView tv_resend_otp = (CustomTextView) otpDialog.findViewById(R.id.tv_resend_otp);
        CustomTextView tv_verify_mobile_number = (CustomTextView) otpDialog.findViewById(R.id.tv_verify_mobile_number);
        if (datum != null) {
            requestSendOtp(datum);
        }
        tv_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestResendOTP();
            }
        });
        tv_verify_mobile_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_otp.getText().toString().trim())) {
                    PopupUtil.showSingleButtonAlert(HomeActivity.this, getString(R.string.please_enter_the_otp_received));
                } else {
                    requestValidateOTP(edt_otp.getText().toString().trim());
                }
            }
        });
        otpDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showHideProgress(false);
            }
        });
        otpDialog.show();
    }

    public void manageGuestUserAction(String message) {
        PopupUtil.showAlert(this,
                message, getString(R.string.allow), getString(R.string.deny), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        localStorage.clearLocalStorage();
                        updateUI(false);
                        replaceFragment(LoginFragment.newInstance(true), true, true, false, false);
                    }
                });
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        if (requestCode == WSUtils.REQ_CUSTOMER_NOTIFICATION_COUNT) {
            CustomerNotificationCountModel notificationCountResponse = (CustomerNotificationCountModel) response;
            if (notificationCountResponse.getSettings().getSuccess().equalsIgnoreCase("1")) {
//                updateNotificationCount(notificationCountResponse.getData().get(0).getNotificationCount());
            } else if (notificationCountResponse.getSettings().getSuccess().equalsIgnoreCase("2")) {
                if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
                    manageTokenExpire();
                }
            }
        } else if (requestCode == WSUtils.REQ_LOG_OUT) {
            showHideProgress(false);
            LogoutModel logoutModel = (LogoutModel) response;
            if (logoutModel.getSettings().getSuccess().equalsIgnoreCase("1")) {
                logout();
            } else if (logoutModel.getSettings().getSuccess().equalsIgnoreCase("2")) {
                /*if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
                    manageTokenExpire();
                }*/
                logout();
            }
        } else if (requestCode == WSUtils.REQ_CUSTOMER_LOGIN) {
            showHideProgress(false);
            parseCustomerLogin((CustomerLoginModel) response);
        } else if (requestCode == WSUtils.REQ_VALIDATE_OTP) {
            showHideProgress(false);
            parseValidateOtp((ValidateOTPModel) response);
        } else if (requestCode == WSUtils.REQ_GET_ISD_CODE) {
            showHideProgress(false);
            parseGetISDCode((ISDCodeModel) response);
        } else if (requestCode == WSUtils.REQ_CUSTOMER_REGISTER) {
            showHideProgress(false);
            parseCustomerRegister((CustomerRegisterModel) response);
        } else if (requestCode == WSUtils.REQ_RESEND_OTP) {
            showHideProgress(false);
            parseResendOtpResponse((ResendOTPModel) response);
        }
    }

    private void startNotificationAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationCountReceiver.class);
        intent.putExtra(Constants.SP_KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
        intent.putExtra(Constants.SP_KEY_TOKEN, localStorage.getString(Constants.SP_KEY_TOKEN, ""));
        intent.putExtra(Constants.SP_KEY_LANG_ID, getString(R.string.lang_id));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000,
                120000, pendingIntent);
    }

    @Override
    public void errorResponse(int requestCode, Throwable t) {
        showHideProgress(false);
    }

    @Override
    public void noInternetConnection(int requestCode) {
        showHideProgress(false);
    }

    public void manageTokenExpire() {
        localStorage.clearLocalStorage();
        updateUI(false);
        PopupUtil.showAlertMessage(this, getString(R.string.app_name),
                getString(R.string.token_expired_please_login_to_continue), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        replaceFragment(LoginFragment.newInstance(true), true, true, false, false);
                    }
                });
    }

    /**
     * Call api for get user's credit money
     */
    public void requestGetCredit(final boolean isProgressShow, final WalletDataRefreshCallback walletDataRefreshCallback) {
        if (!CommonUtil.isEmpty(localStorage.getString(Constants.SP_KEY_USER_ID, ""))) {
            if (isProgressShow)
                showHideProgress(true);
            HashMap<String, String> params = new HashMap<>();
            params.put(WSUtils.KEY_USER_ID, localStorage.getString(Constants.SP_KEY_USER_ID, ""));
//            params.put(WSUtils.KEY_USER_ID, "160");
            WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CREDIT);
            wsUtils.WSRequest(this, params, null, WSUtils.REQ_CREDIT, new IParser<WSResponse>() {
                @Override
                public void successResponse(int requestCode, WSResponse response) {
                    if (isProgressShow)
                        showHideProgress(false);
                    CreditModel creditModel = (CreditModel) response;
                    if (creditModel.getSettings().getSuccess().equalsIgnoreCase("1")) {
                        localStorage.putString(Constants.SP_KEY_CREDITS, creditModel.getCredit().get(0).getUCredit());
                        setCreditText(localStorage.getString(Constants.SP_KEY_CREDITS, ""));
                        if (walletDataRefreshCallback != null)
                            walletDataRefreshCallback.onDataRefresh();
                    }
                }

                @Override
                public void errorResponse(int requestCode, Throwable t) {
                    showHideProgress(false);
                }

                @Override
                public void noInternetConnection(int requestCode) {
                    showHideProgress(false);
                }
            });
        }
    }

    /**
     * Call api for get user's credit money
     */
    public void requestGetCredit() {
        requestGetCredit(false, null);
    }

    private void requestResendOTP() {
        showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_PREV_OTP, "1234");
        params.put(WSUtils.KEY_OTP_TYPE, "0");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_RESEND_OTP);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_RESEND_OTP, this);
    }

    private void requestValidateOTP(String otp) {
        showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_MOBILE_OTP, otp);
        params.put(WSUtils.KEY_USER_TYPE, "0");
        params.put(WSUtils.KEY_OTP_TYPE, "0");
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_VALIDATE_OTP);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_VALIDATE_OTP, this);
    }

    private void requestGetISDCode() {
        showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_GET_ISD_CODE);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_GET_ISD_CODE, this);
    }

    private void requestForLoginWS() {
        showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_EMAIL, loginEmailAddress);
        params.put(WSUtils.KEY_PASSWORD, loginPassword);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_DEVICE_TOKEN, localStorage.getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_LOGIN);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_CUSTOMER_LOGIN, this);
    }

    private void requestForSignupWS() {
        showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_FIRST_NAME, "الاسم الأول");
        params.put(WSUtils.KEY_LAST_NAME, "الاسم الأخير");
        params.put(WSUtils.KEY_EMAIL, signUpEmailAddress);
        params.put(WSUtils.KEY_PASSWORD, signUpPassword);
        params.put(WSUtils.KEY_MOBILE_CODE, ISDCodeList.get(spinner_isd.getSelectedItemPosition()).getMcIsdcode());
        params.put(WSUtils.KEY_MOBILE_NO, signUpMobileNumber);
        params.put(WSUtils.KEY_PLATFORM, "Android");
        params.put(WSUtils.KEY_DEVICE_TOKEN, localStorage.getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_GENDER, "");
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_CUSTOMER_REGISTER);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_CUSTOMER_REGISTER, this);
    }

    private void requestSendOtp(CustomerLoginModel.Datum datum) {
        showHideProgress(true);
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_MOBILE_NO, datum.getMobileNo());
        params.put(WSUtils.KEY_MOBILE_CODE, datum.getMobileCode());
        params.put(WSUtils.KEY_USER_ID, datum.getUserId());
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        WSUtils wsUtils = wsFactory.getWsUtils(WSFactory.WSType.WS_SEND_OTP);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_SEND_OTP, this);
    }

    private void parseCustomerRegister(final CustomerRegisterModel response) {
        showHideProgress(false);
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                PopupUtil.showAlertMessage(this, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        String otp = response.getData().get(0).getOtp();
                        localStorage.putString(Constants.SP_KEY_TOKEN, response.getData().get(0).getToken());
                        localStorage.putBoolean(Constants.SP_KEY_AUTO_LOGIN, response.getData().get(0).getAutoLogin().equalsIgnoreCase("Yes"));
                        localStorage.putBoolean(Constants.SP_KEY_PUSH_NOTIFICATION, response.getData().get(0).getPushNotification().equalsIgnoreCase("Yes"));
                        localStorage.putString(Constants.SP_KEY_FIRST_NAME, "Weekend ");
                        localStorage.putString(Constants.SP_KEY_LAST_NAME, "Customer");
                        loginEmailAddress = signUpEmailAddress;
                        loginPassword = signUpPassword;
                        localStorage.putString(Constants.SP_KEY_MOBILE_NO, signUpMobileNumber);
                        localStorage.putString(Constants.SP_KEY_MOBILE_CODE, selectedISDCode);
                        localStorage.putString(Constants.SP_KEY_EMAIL, signUpEmailAddress);
                        localStorage.putString(Constants.QB_EMAIL_ID, signUpEmailAddress);
                        localStorage.putString(Constants.SP_KEY_PASSWORD, signUpPassword);
                        if (signUpDialog != null)
                            signUpDialog.dismiss();

                        showOtpDialog(null);
                    }
                });
            } else {
                PopupUtil.showSingleButtonAlert(this, response.getSettings().getMessage() + "");
            }
        }
    }

    private void parseGetISDCode(ISDCodeModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                ISDCodeList = response.getData();
                rearrangeISDList();
                SpinnerISDCodeAdapter spinnerISDCodeAdapter = new SpinnerISDCodeAdapter(this, ISDCodeList);
                spinner_isd.setAdapter(spinnerISDCodeAdapter);
                selectedISDCode = ISDCodeList.get(spinner_isd.getSelectedItemPosition()).getMcIsdcode();
                selectedISDCodeLength = CommonUtil.getIsdCodeCount(selectedISDCode);
                if (selectedISDCodeLength == 2) {
                    CommonUtil.setMaxLength(edt_mobile_number, 10);
                } else if (selectedISDCodeLength == 3) {
                    CommonUtil.setMaxLength(edt_mobile_number, 9);
                }
            } else {
                CommonUtil.showSnackbar(getWindow().getDecorView().getRootView(), response.getSettings().getMessage() + "");
            }
        }
        showHideProgress(false);
    }

    private void parseValidateOtp(ValidateOTPModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {
                if (!TextUtils.isEmpty(localStorage.getString(Constants.SP_KEY_TOKEN, ""))) {
                    PopupUtil.showAlertMessage(this, getString(R.string.verification_is_successful), getString(R.string.you_are_now_an_activated_user_),
                            getString(R.string.ok), null);
                    if (otpDialog != null)
                        otpDialog.dismiss();
                    localStorage.putString(Constants.SP_KEY_EMAIL, loginEmailAddress);
                    localStorage.putString(Constants.QB_EMAIL_ID, loginEmailAddress);
                    localStorage.putString(Constants.SP_KEY_PASSWORD, loginPassword);
                    localStorage.putBoolean(Constants.SP_KEY_IS_REMEMBER_ME, isRememberMeChecked);
                    localStorage.putString(Constants.SP_KEY_USER_ID, response.getData().get(0).getUserId());
                    checkForQuickBloxSignUpLogin();
                    continueBooking();
                } else {
                    requestForLoginWS();
                }
            } else {
                PopupUtil.showAlert(HomeActivity.this, response.getSettings().getMessage() + "", null);
            }
        }
    }

    private void parseCustomerLogin(CustomerLoginModel response) {
        if (response != null) {
            if (response.getSettings().getSuccess().equalsIgnoreCase("1")) {

                localStorage.putString(Constants.SP_KEY_USER_ID, response.getData().get(0).getUserId());
                localStorage.putString(Constants.SP_KEY_TOKEN, response.getData().get(0).getToken());
                localStorage.putString(Constants.SP_KEY_MOBILE_NO, response.getData().get(0).getMobileNo());
                localStorage.putString(Constants.SP_KEY_MOBILE_CODE, response.getData().get(0).getMobileCode());
                localStorage.putString(Constants.SP_KEY_EMAIL, loginEmailAddress);
                localStorage.putString(Constants.QB_EMAIL_ID, loginEmailAddress);
                localStorage.putString(Constants.SP_KEY_FIRST_NAME, response.getData().get(0).getFirstName());
                localStorage.putString(Constants.SP_KEY_LAST_NAME, response.getData().get(0).getLastName());
                localStorage.putString(Constants.SP_KEY_PASSWORD, loginPassword);
                localStorage.putBoolean(Constants.SP_KEY_IS_REMEMBER_ME, isRememberMeChecked);
                localStorage.putBoolean(Constants.SP_KEY_AUTO_LOGIN, response.getData().get(0).getAutoLogin().equalsIgnoreCase("Yes"));
                localStorage.putBoolean(Constants.SP_KEY_PUSH_NOTIFICATION, response.getData().get(0).getPushNotification().equalsIgnoreCase("Yes"));
                checkForQuickBloxSignUpLogin();
                if (logindialog != null)
                    logindialog.dismiss();
                continueBooking();
            } else if (response.getSettings().getSuccess().equalsIgnoreCase("3")) {
                final CustomerLoginModel.Datum datum = response.getData().get(0);
                if (logindialog != null)
                    logindialog.dismiss();
                PopupUtil.showAlertMessage(this, getString(R.string.app_name), response.getSettings().getMessage(), getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOtpDialog(datum);
                    }
                });
            } else {
                PopupUtil.showSingleButtonAlert(this, response.getSettings().getMessage() + "");
            }
            if (otpDialog != null && otpDialog.isShowing())
                otpDialog.dismiss();
        }

    }

    private void checkForQuickBloxSignUpLogin() {
        startService(new Intent(this, UserLoginAndUpdateService.class));
    }

    private void parseResendOtpResponse(ResendOTPModel response) {
        if (response != null) {
            CommonUtil.showSnackbar(getWindow().getDecorView().getRootView(), response.getSettings().getMessage() + "");
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    /**
     * Update wallet money on navigation view
     */
    public void setCreditText(String credit) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString str1 = new SpannableString(getString(R.string.credit) + " " + credit + " " + String.format(getString(R.string.currency)));
        str1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_35_120_201)), getString(R.string.credit).length() + 1, str1.length(), 0);
        builder.append(str1);
        tvCredits.setText(builder);
    }

    public void showSplashWindow() {
        StaticUtil.setWindowDimensions(this);
        splashDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        splashDialog.setCancelable(false);
        splashDialog.setCanceledOnTouchOutside(false);
        splashDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        splashDialog.setContentView(R.layout.activity_splash);
        splashDialog.show();

        mLoadingHandler = new Handler();
        mLoadingRunnable = new Runnable() {
            @Override
            public void run() {
                hideSplashScreenNew();
            }
        };
        mLoadingHandler.postDelayed(mLoadingRunnable, Constants.SPLASH_TIME);
    }

    public void hideSplashScreen() {
//        if(splashDialog!=null && splashDialog.isShowing())
//            splashDialog.dismiss();
//
//        if (mLoadingHandler != null && mLoadingRunnable != null) {
//            mLoadingHandler.removeCallbacks(mLoadingRunnable);
//        }
    }

    private void hideSplashScreenNew() {
        if (splashDialog != null && splashDialog.isShowing())
            splashDialog.dismiss();

        if (mLoadingHandler != null && mLoadingRunnable != null) {
            mLoadingHandler.removeCallbacks(mLoadingRunnable);
        }
    }

    private void startChatService() {
        if (ChatService.isRunning()) {
            stopService(new Intent(this, ChatService.class));
        }
        startService(new Intent(this, ChatService.class));
    }

}
