package com.weekend.server.factory;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.webkit.MimeTypeMap;

import com.payfort.fort.android.sdk.base.FortSdk;
import com.weekend.server.IParser;
import com.weekend.server.WSResponse;
import com.weekend.server.Webservice;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hb on 28-May-16.
 */
public abstract class WSUtils {

    public static final int REQ_CUSTOMER_REGISTER = 101;
    public static final int REQ_CUSTOMER_LOGIN = 102;
    public static final int REQ_COUNTRY_LIST = 103;
    public static final int REQ_CITY_LIST = 104;
    public static final int REQ_GET_USER_DATA = 105;
    public static final int REQ_UPDATE_USER_PROFILE = 106;
    public static final int REQ_VALIDATE_OTP = 107;
    public static final int REQ_RESEND_OTP = 108;
    public static final int REQ_GET_ISD_CODE = 109;
    public static final int REQ_USER_CHANGE_PASSWORD = 110;
    public static final int REQ_FAVORITE_LIST = 111;
    public static final int REQ_ADD_TO_FAVORITE = 112;
    public static final int REQ_REMOVE_FAVORITE = 113;
    public static final int REQ_FRONT_FORGOT_PASSWORD = 115;//Either using email or using mobile
    public static final int REQ_RESET_PASSWORD = 116;
    public static final int REQ_MY_BOOKINGS = 117;
    public static final int REQ_CUSTOMER_BOOKING_DETAIL = 118;
    public static final int REQ_CUSTOMER_CANCEL_BOOKING = 119;
    public static final int REQ_CUSTOMER_ORDER_RATING = 121;
    public static final int REQ_PROPERTY_LIST = 122;
    public static final int REQ_PROPERTY_DETAIL_PAGE = 123;
    public static final int REQ_SEARCH_OPTION = 124;
    public static final int REQ_NEIGHBOURHOOD_LIST = 125;
    public static final int REQ_CUSTOMER_NOTIFICATION_LIST = 126;
    public static final int REQ_CUSTOMER_NOTIFICATION_DELETE_ALL = 127;
    public static final int REQ_CUSTOMER_NOTIFICATION_DELETE = 128;
    public static final int REQ_PUSH_NOTIFICATION_CHANGE_STATUS = 129;
    public static final int REQ_PROPERTY_REVIEW = 130;
    public static final int REQ_AUTO_LOGIN_CHANGE_STATUS = 131;
    public static final int REQ_UPDATE_MOBILE_NUMBER_FROM_UPH = 132;
    public static final int REQ_POPULAR_CITY_LIST = 133;
    public static final int REQ_CONTACT_US = 134;
    public static final int REQ_DISCOUNT_COUPON_APPLY = 135;
    public static final int REQ_CUSTOMER_NOTIFICATION_COUNT = 136;
    public static final int REQ_BOOK_PROPERTY_BY_CUSTOMER = 137;
    public static final int REQ_MANAGE_SCHEDULE = 138;
    public static final int REQ_GET_PROPERTY_PRICE = 139;
    public static final int REQ_CUSTOMER_BOOKING_CONFIRMATION = 140;
    public static final int REQ_SEND_OTP = 141;
    public static final int REQ_ABUSE_REASON_LIST = 142;
    public static final int REQ_SAVE_ABUSE_REASON = 143;
    public static final int REQ_PAYFORT_TOKEN_RETURN = 144;
    public static final int REQ_LOG_OUT = 145;
    public static final int REQ_CREDIT = 146;
    public static final int REQ_CUSTOMER_BOOKING_CONFIRMATION_USING_WALLET = 147;
    public static final int REQ_PAY_LATER_PROPERTY_BY_CUSTOMER = 148;

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_MOBILE_CODE = "mobile_code";
    public static final String KEY_MOBILE_NO = "mobile_no";
    public static final String KEY_PLATFORM = "platform";//platform [Android]
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_LANG_ID = "lang_id";
    public static final String KEY_COUNTRY_ID = "country_id";
    public static final String KEY_USERID = "iUserId";
    public static final String KEY_CITYID = "city_id";
    public static final String KEY_CITY = "city";
    public static final String KEY_MOBILE_OTP = "mobile_otp";
    public static final String KEY_USER_TYPE = "user_type";//user_type [0 - customer/ 1 - owner]
    public static final String KEY_OTP_TYPE = "otp_type";//otp_type otp_type [0 - register/ 1- profile / 2 - MobileResetPwd]
    public static final String KEY_OLD_PASSWORD = "old_password";
    public static final String KEY_NEW_PASSWORD = "new_password";
    public static final String KEY_PAGE_INDEX = "page_index";
    public static final String KEY_PROPERTYID = "iPropertyId";
    public static final String KEY_PROPERTY_TYPE_ID = "property_type_id";//[1 - Chalet/2 - Soccer Fields]
    public static final String KEY_ORDER_ID = "order_id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_REASON = "reason";
    public static final String KEY_PROPERTY_ID = "property_id";
    public static final String KEY_RATING = "rating";
    public static final String KEY_LEASING_TYPE = "leasing_type";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_CHECK_IN = "check_in";
    public static final String KEY_NEIGHBOURHOOD = "neighbourhood";
    public static final String KEY_AMENITIES = "amenities";
    public static final String KEY_TITLE = "title";
    public static final String KEY_SECTION = "section";
    public static final String KEY_SUITABLE_FOR = "suitable_for";
    public static final String KEY_MAX_PRICE = "max_price";
    public static final String KEY_MIN_PRICE = "min_price";
    public static final String KEY_SIZE = "size";
    public static final String KEY_TO = "to";
    public static final String KEY_FROM = "from";
    public static final String KEY_SCHEDULE = "schedule";//schedule [ Yes / No]
    public static final String KEY_AVAILIBILITY = "availibility";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_REVIEW = "review";
    public static final String KEY_LOCATION_ID = "location_id";
    public static final String KEY_ACTION = "action";//[0 - NO/1 - Yes]
    public static final String KEY_NOTIFICATION_ID = "notification_id";
    public static final String KEY_PREV_OTP = "prev_otp";
    public static final String KEY_CANCEL_BY = "cancel_by";// [Customer-0 / Owner-1]
    public static final String KEY_SORT_BY_TYPE = "sort_by_type";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String KEY_CITY_NAME = "city_name";
    public static final String KEY_PROPERTYTYPEID = "iPropertyTypeId";
    public static final String KEY_MOBILE_UPDATE = "mobile_update";// [0 - No / 1- Update]
    public static final String KEY_LANG_CODE = "lang_code";
    public static final String OTP_TYPE = "otp_type";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_PHONE_NO = "phone_no";
    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_PROPERTY_CHALET_ID = "property_chalet_id";
    public static final String KEY_PROPERTY_TYPE = "property_type";
    public static final String KEY_COUPON_CODE = "coupon_code";
    public static final String KEY_BOOKING_DATE = "booking_date";
    public static final String KEY_CALENDER_YEAR = "calendar_year";
    public static final String KEY_CALENDAR_MONTH = "calendar_month";
    public static final String KEY_BOOKING_NAME = "booking_name";
    public static final String KEY_BOOKING_EMAIL = "booking_email";
    public static final String KEY_NATIONAL_ID = "national_id";
    public static final String KEY_BOOKING_TIME_SLOT = "booking_time_slot";
    public static final String KEY_COUPON = "coupon";
    public static final String ENDPOINT = "endpoint";
    public static final String KEY_USER_PASSWORD_HISTORY_ID = "user_password_history_id";
    public static final String KEY_BRIEF_NOTE = "brief_note";
    public static final String KEY_TXN_ID = "txn_id";
    public static final String KEY_PAYMENT_DATA = "payment_data";
    public static final String KEY_PAYMENT_RESPONSE = "payment_response";
    public static final String KEY_BOOKING_STATUS = "booking_status";//Booked/Failed
    public static final String KEY_PAYMENT_STATUS = "payment_status";//Paid/UnPaid
    public static final String KEY_POSTEDBY_NAME = "postedby_name";
    public static final String KEY_ABUSE_REASON_ID = "abuse_reason_id";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_MERCHANT_REFERENCE = "merchant_reference";
    /*output params*/
    public static final String Cancelled = "Cancelled";
    public static final String Closed = "Closed";
    public static final String Booked = "Booked";
    public static final String Pending = "Pending";
    public static final String Credit = "Credit";
    public static final String Payfort = "Payfort";

    /*input params*/
    public static final String Customer = "Customer";
    public static final String Failed = "Failed";
    public static final String Paid = "Paid";
    public static final String UnPaid = "UnPaid";

    private static final String LOCAL_URL = "http://192.168.34.181/online_chalet_booking/WS/";
    // private static final String STAGING_URL = "http://108.170.62.151/projects/onlinesoccer/WS/";
    private static final String STAGING_HOST_URL = "https://www.weekend.sa/staging/";
    private static final String STAGING_URL = STAGING_HOST_URL + "WS/";

    private static final String LIVE_HOST_URL = "https://www.weekend.sa/";
    private static final String LIVE_URL = LIVE_HOST_URL + "WS/";

    public static final String ABOUT_US_URL = "https://www.weekend.sa/about-us.html?pageview=mobile&lang_id=AR";
    public static final String PRIVACY_POLICY_URL = "https://www.weekend.sa/privacy-policy.html?pageview=mobile&lang_id=AR";
    public static final String TERMS_CONDITIONS_URL = "https://www.weekend.sa/terms-conditions.html?pageview=mobile&lang_id=AR";
    public static final String FAQ_URL = "https://www.weekend.sa/faq.html?pageview=mobile&faq_type=Customer&lang_id=AR";

//    public static final String BASE_URL = LIVE_URL; // Change to live for production mode
//    public static final String BASE_HOST_URL = LIVE_HOST_URL; // Change to live for production mode

    public static final String BASE_URL = STAGING_URL; // Change to live for production mode
    public static final String BASE_HOST_URL = STAGING_HOST_URL; // Change to live for production mode

    public static final String CREDITS_URL = BASE_HOST_URL + "creditbalance.html?pageview=mobile";
    public static final String PAYFORT_ENVIRONMENT_TEST = FortSdk.ENVIRONMENT.TEST;
    public static final String PAYFORT_ENVIRONMENT_PRODUCTION = FortSdk.ENVIRONMENT.PRODUCTION;
    public static final String PAYFORT_ENVIRONMENT = PAYFORT_ENVIRONMENT_PRODUCTION;

    public Webservice webServices;


    public WSUtils() {
        initRetrofit(BASE_URL);
    }

    public WSUtils(String customBaseUrl) {
        initRetrofit(customBaseUrl);
    }

    private void initRetrofit(String baseURl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        /*Uncomment following line to enable logging of WS*/
        httpClient.addInterceptor(logging);

        httpClient.connectTimeout(5, TimeUnit.MINUTES);
        httpClient.readTimeout(5, TimeUnit.MINUTES);
        httpClient.writeTimeout(5, TimeUnit.MINUTES);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        webServices = retrofit.create(Webservice.class);
        httpClient.addInterceptor(logging);

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        webServicesStatic = retrofit.create(Webservice.class);
    }
    webServices = webServicesStatic;
    webServices = new WeakReference<>(webServicesStatic).get();*/

    }

    protected abstract void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback);

    public void WSRequest(final Context context, Map<String, String> params, Map<String, RequestBody> fileUploadParams,
                          final int requestCode, final IParser<WSResponse> iParser) {
        if (!isNetworkAvailable(context)) {
            iParser.noInternetConnection(requestCode);
        } else {
            Callback<WSResponse> callback = new Callback<WSResponse>() {
                @Override
                public void onResponse(Call<WSResponse> call, Response<WSResponse> response) {
                    if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                        return;
                    }
                    iParser.successResponse(requestCode, response.body());
                }

                @Override
                public void onFailure(Call<WSResponse> call, Throwable t) {
                    if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                        return;
                    }
                    iParser.errorResponse(requestCode, t);
                }
            };
            enqueueWebService(params, fileUploadParams, callback);
        }
    }

    private boolean isNetworkAvailable(Context mContext) {
        boolean connection = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo net_info = cm.getActiveNetworkInfo();
                if (net_info != null && net_info.isConnected()) {
                    connection = true;
                    int type = net_info.getType();
                    switch (type) {
                        case ConnectivityManager.TYPE_MOBILE:
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public RequestBody getFileRequestBody(File file) {
        MediaType MEDIA_TYPE_JPEG = MediaType.parse(getMimeType(file.getAbsolutePath()));
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JPEG, file);
        return requestBody;
    }

    public RequestBody getStringRequestBody(String value) {
        MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_TEXT, value);
        return requestBody;
    }

    public String getFileUploadKey(String key, File file) {
        return "" + key + "\"; filename=\"" + file.getName();
    }

    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
