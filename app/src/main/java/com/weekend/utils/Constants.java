package com.weekend.utils;

/**
 * Created by hb on 7/14/2016.
 */
public class Constants {

    public static final int SPLASH_TIME = 4000;
    public static final int FRAGMENT_TRANSACTION_DELAY = 250;
    public static final long ANIMATION_OFFSET = 100;
    public static final int FRAGMENT_TRANSACTION_SNACKBAR_DELAY = 1500;//Snackbar.LENGTH_SHORT // 1500 millis  //Snackbar.LENGTH_LONG // 2750 millis //Snackbar.LENGTH_INDEFINITE
    // pre defined constants

    public static int SCREEN_HEIGHT, SCREEN_WIDTH;
    public static final String NOTIFICATION_COUNT_BROADCAST = "NotificationCountBroadcast";
    //SharedPreferences keys
    public static final String SP_KEY_GCM_REGISTRATION_ID = "gcmRegistrationId";
    public static final String SP_KEY_IS_USER_FIRST_TIME = "isUserFirstTime";
    public static final String SP_KEY_IS_REMEMBER_ME = "isRememberMe";
    public static final String SP_KEY_USER_ID = "userID";
    public static final String SP_KEY_EMAIL = "email";
    public static final String SP_KEY_MOBILE_NO = "mobileNo";
    public static final String SP_KEY_MOBILE_CODE = "mobileCode";
    public static final String SP_KEY_PASSWORD = "password";
    public static final String SP_KEY_LATEST_OTP = "latestOTP";
    public static final String SP_KEY_TOKEN = "token";
    public static final String SP_KEY_PUSH_NOTIFICATION = "pushNotification";
    public static final String SP_KEY_AUTO_LOGIN = "autoLogin";
    public static final String SP_KEY_FIRST_NAME = "firstName";
    public static final String SP_KEY_LAST_NAME = "lastName";
    public static final String SP_KEY_LANG_ID = "langId";
    public static final String SP_KEY_CREDITS = "credits";

    public static final int REQUEST_LOCATION_LISTVIEW_FRAGMENT = 1001;
    public static final int REQUEST_LOCATION_SEARCHRESULT_FRAGMENT = 1002;

    public static final String CUSTOMER_ACCOUNT_ACTIVATE = "customer_account_activate";
    public static final String BOOKING_CONFIRMED = "booking_received";
    public static final String CUSTOMER_CANCELLED_BOOKING = "customer_cancelled_booking";
    public static final String PROPERTY_OWNER_REPLY_COMMENT = "property_owner_reply_comment";

    public static final String CUSTOMER_ACCOUNT_ACTIVATE_FRAGMENT = "LoginFragment";
    public static final String BOOKING_CONFIRMED_CANCELLED_CHALET_FRAGMENT = "ChaletBookingDetailsFragment";
    public static final String BOOKING_CONFIRMED_CANCELLED_SOCCER_FRAGMENT = "SoccerFieldBookingDetailsFragment ";
    public static final String PROPERTY_OWNER_REPLY_COMMENT_FRAGMENT = "ReviewsFragment ";

    // search parameters
    public static final String BUNDLE_KEY_LEASING_TYPE = "leasing_type";
    public static final String BUNDLE_KEY_LOCATION = "location";
    public static final String BUNDLE_KEY_CHECK_IN = "check_in";
    public static final String BUNDLE_KEY_NEIGHBOURHOOD = "neighbourhood";
    public static final String BUNDLE_KEY_AMENITIES = "amenities";
    public static final String BUNDLE_KEY_TITLE = "title";
    public static final String BUNDLE_KEY_SECTION = "section";
    public static final String BUNDLE_KEY_SUITABLE_FOR = "suitable_for";
    public static final String BUNDLE_KEY_MAX_PRICE = "max_price";
    public static final String BUNDLE_KEY_MIN_PRICE = "min_price";
    public static final String BUNDLE_KEY_SIZE = "size";
    public static final String BUNDLE_KEY_PROPERTY_TYPE_ID = "property_type_id";
    public static final String BUNDLE_KEY_TO = "to";
    public static final String BUNDLE_KEY_FROM = "from";
    public static final String BUNDLE_KEY_AVAILIBILITY = "availibility";
    public static final String BUNDLE_KEY_LAT = "lat";
    public static final String BUNDLE_KEY_LNG = "lng";
    public static final String BUNDLE_KEY_SORT_TYPE = "sort_type";

    // Chat Preferences

    public static final String QB_OCCUPANT_ID = "qb_occupant_id";
//    public static final String QB_OCCUPANT_PASSWORD = "qb_occupant_password";
    public static final String QB_LOGIN = "qb_login";
    public static final String QB_EMAIL_ID= "qb_email_id";
    public static final String QB_FIRST_NAME = "first_name";
    public static final String QB_LAST_NAME = "last_name";
    public static final String DEVICE_TOKEN = "device_token";

    public static final int QB_SIGNIN_EMAIL = 001;
    public static final int QB_SIGNIN_LOGIN = 002;
    public static final int QB_SIGNUP = 003;

    public static final class NotificationParams {
        public static String KEY_ORDER_ID = "";
        public static String KEY_PROPERTY_ID = "";
        public static String KEY_TOKEN = "";
        public static String KEY_USER_ID = "";
    }
}
