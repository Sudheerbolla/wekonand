package com.weekend.server;

import com.weekend.server.factory.WSAbuseReasonList;
import com.weekend.server.factory.WSAddToFavorite;
import com.weekend.server.factory.WSAutoLoginChangeStatus;
import com.weekend.server.factory.WSBookPropertyByCustomer;
import com.weekend.server.factory.WSCityList;
import com.weekend.server.factory.WSContactUs;
import com.weekend.server.factory.WSCountryList;
import com.weekend.server.factory.WSCredit;
import com.weekend.server.factory.WSCustomerBookingConfirmation;
import com.weekend.server.factory.WSCustomerBookingDetail;
import com.weekend.server.factory.WSCustomerCancelBooking;
import com.weekend.server.factory.WSCustomerLogin;
import com.weekend.server.factory.WSCustomerNotificationCount;
import com.weekend.server.factory.WSCustomerNotificationDelete;
import com.weekend.server.factory.WSCustomerNotificationDeleteAll;
import com.weekend.server.factory.WSCustomerNotificationList;
import com.weekend.server.factory.WSCustomerOrderRating;
import com.weekend.server.factory.WSCustomerRegister;
import com.weekend.server.factory.WSDeviceTokenSave;
import com.weekend.server.factory.WSDiscountCouponApply;
import com.weekend.server.factory.WSFavoriteList;
import com.weekend.server.factory.WSFrontForgotPasswordEmail;
import com.weekend.server.factory.WSFrontForgotPasswordMobile;
import com.weekend.server.factory.WSGetISDCode;
import com.weekend.server.factory.WSGetPropertyPrice;
import com.weekend.server.factory.WSGetUserData;
import com.weekend.server.factory.WSLogOut;
import com.weekend.server.factory.WSManageSchedule;
import com.weekend.server.factory.WSMyBookings;
import com.weekend.server.factory.WSNeighbourhoodList;
import com.weekend.server.factory.WSPayLaterPropertyByCustomer;
import com.weekend.server.factory.WSPayfortTokenReturn;
import com.weekend.server.factory.WSPopularCityList;
import com.weekend.server.factory.WSPropertyDetailPage;
import com.weekend.server.factory.WSPropertyList;
import com.weekend.server.factory.WSPropertyReview;
import com.weekend.server.factory.WSPushNotificationChangeStatus;
import com.weekend.server.factory.WSRemoveFavorite;
import com.weekend.server.factory.WSResendOTP;
import com.weekend.server.factory.WSResetPassword;
import com.weekend.server.factory.WSSaveAbuseReason;
import com.weekend.server.factory.WSSearchOption;
import com.weekend.server.factory.WSSendOtp;
import com.weekend.server.factory.WSUpdateMobileNumberFromUph;
import com.weekend.server.factory.WSUpdateUserProfile;
import com.weekend.server.factory.WSUserChangePassword;
import com.weekend.server.factory.WSUtils;
import com.weekend.server.factory.WSValidateOTP;

/**
 * Created by hb on 28-May-16.
 */
public class WSFactory {
    public WSUtils getWsUtils(WSType wsType) {
        WSUtils wsUtils = null;
        switch (wsType) {
            case WS_CUSTOMER_REGISTER:
                wsUtils = new WSCustomerRegister();
                break;
            case WS_CUSTOMER_LOGIN:
                wsUtils = new WSCustomerLogin();
                break;
            case WS_COUNTRY_LIST:
                wsUtils = new WSCountryList();
                break;
            case WS_CITY_LIST:
                wsUtils = new WSCityList();
                break;
            case WS_GET_USER_DATA:
                wsUtils = new WSGetUserData();
                break;
            case WS_UPDATE_USER_DATA:
                wsUtils = new WSUpdateUserProfile();
                break;
            case WS_VALIDATE_OTP:
                wsUtils = new WSValidateOTP();
                break;
            case WS_RESEND_OTP:
                wsUtils = new WSResendOTP();
                break;
            case WS_GET_ISD_CODE:
                wsUtils = new WSGetISDCode();
                break;
            case WS_USER_CHANGE_PASSWORD:
                wsUtils = new WSUserChangePassword();
                break;
            case WS_FAVORITE_LIST:
                wsUtils = new WSFavoriteList();
                break;
            case WS_ADD_TO_FAVORITE:
                wsUtils = new WSAddToFavorite();
                break;
            case WS_REMOVE_FAVORITE:
                wsUtils = new WSRemoveFavorite();
                break;
            case WS_FRONT_FORGOT_PASSWORD_MOBILE:
                wsUtils = new WSFrontForgotPasswordMobile();
                break;
            case WS_FRONT_FORGOT_PASSWORD_EMAIL:
                wsUtils = new WSFrontForgotPasswordEmail();
                break;
            case WS_RESET_PASSWORD:
                wsUtils = new WSResetPassword();
                break;
            case WS_MY_BOOKINGS:
                wsUtils = new WSMyBookings();
                break;
            case WS_CUSTOMER_BOOKING_DETAIL:
                wsUtils = new WSCustomerBookingDetail();
                break;
            case WS_CUSTOMER_CANCEL_BOOKING:
                wsUtils = new WSCustomerCancelBooking();
                break;
            case WS_CUSTOMER_ORDER_RATING:
                wsUtils = new WSCustomerOrderRating();
                break;
            case WS_PROPERTY_LIST:
                wsUtils = new WSPropertyList();
                break;
            case WS_PROPERTY_DETAIL_PAGE:
                wsUtils = new WSPropertyDetailPage();
                break;
            case WS_SEARCH_OPTION:
                wsUtils = new WSSearchOption();
                break;
            case WS_NEIGHBOURHOOD_LIST:
                wsUtils = new WSNeighbourhoodList();
                break;
            case WS_CUSTOMER_NOTIFICATION_LIST:
                wsUtils = new WSCustomerNotificationList();
                break;
            case WS_CUSTOMER_NOTIFICATION_DELETE_ALL:
                wsUtils = new WSCustomerNotificationDeleteAll();
                break;
            case WS_CUSTOMER_NOTIFICATION_DELETE:
                wsUtils = new WSCustomerNotificationDelete();
                break;
            case WS_PUSH_NOTIFICATION_CHANGE_STATUS:
                wsUtils = new WSPushNotificationChangeStatus();
                break;
            case WS_PROPERTY_REVIEW:
                wsUtils = new WSPropertyReview();
                break;
            case WS_AUTO_LOGIN_CHANGE_STATUS:
                wsUtils = new WSAutoLoginChangeStatus();
                break;
            case WS_POPULAR_CITY_LIST:
                wsUtils = new WSPopularCityList();
                break;
            case WS_CONTACT_US:
                wsUtils = new WSContactUs();
                break;
            case WS_DISCOUNT_COUPON_APPLY:
                wsUtils = new WSDiscountCouponApply();
                break;
            case WS_CUSTOMER_NOTIFICATION_COUNT:
                wsUtils = new WSCustomerNotificationCount();
                break;
            case WS_BOOK_PROPERTY_BY_CUSTOMER:
                wsUtils = new WSBookPropertyByCustomer();
                break;
            case WS_PAY_LATER_PROPERTY_BY_CUSTOMER:
                wsUtils = new WSPayLaterPropertyByCustomer();
                break;
            case WS_MANAGE_SCHEDULE:
                wsUtils = new WSManageSchedule();
                break;
            case WS_UPDATE_MOBILE_NUMBER_FROM_UPH:
                wsUtils = new WSUpdateMobileNumberFromUph();
                break;
            case WS_GET_PROPERTY_PRICE:
                wsUtils = new WSGetPropertyPrice();
                break;
            case WS_CUSTOMER_BOOKING_CONFIRMATION:
                wsUtils = new WSCustomerBookingConfirmation();
                break;
            case WS_DEVICE_TOKEN_SAVE:
                wsUtils = new WSDeviceTokenSave();
                break;
            case WS_SEND_OTP:
                wsUtils = new WSSendOtp();
                break;
            case WS_ABUSE_REASON_LIST:
                wsUtils = new WSAbuseReasonList();
                break;
            case WS_SAVE_ABUSE_REASON:
                wsUtils = new WSSaveAbuseReason();
                break;
            case WS_PAYFORT_TOKEN_RETURN:
                wsUtils = new WSPayfortTokenReturn();
                break;
            case WS_LOG_OUT:
                wsUtils = new WSLogOut();
                break;
            case WS_CREDIT:
                wsUtils = new WSCredit();
                break;
        }
        return wsUtils;
    }

    public enum WSType {
        WS_CUSTOMER_REGISTER,
        WS_CUSTOMER_LOGIN,
        WS_COUNTRY_LIST,
        WS_CITY_LIST,
        WS_GET_USER_DATA,
        WS_UPDATE_USER_DATA,
        WS_VALIDATE_OTP,
        WS_RESEND_OTP,
        WS_GET_ISD_CODE,
        WS_USER_CHANGE_PASSWORD,
        WS_FAVORITE_LIST,
        WS_ADD_TO_FAVORITE,
        WS_REMOVE_FAVORITE,
        WS_FRONT_FORGOT_PASSWORD_MOBILE,
        WS_FRONT_FORGOT_PASSWORD_EMAIL,
        WS_RESET_PASSWORD,
        WS_MY_BOOKINGS,
        WS_CUSTOMER_BOOKING_DETAIL,
        WS_CUSTOMER_CANCEL_BOOKING,
        WS_CUSTOMER_ORDER_RATING,
        WS_PROPERTY_LIST,
        WS_PROPERTY_DETAIL_PAGE,
        WS_SEARCH_OPTION,
        WS_NEIGHBOURHOOD_LIST,
        WS_CUSTOMER_NOTIFICATION_LIST,
        WS_CUSTOMER_NOTIFICATION_DELETE_ALL,
        WS_PUSH_NOTIFICATION_CHANGE_STATUS,
        WS_CUSTOMER_NOTIFICATION_DELETE,
        WS_PROPERTY_REVIEW,
        WS_AUTO_LOGIN_CHANGE_STATUS,
        WS_POPULAR_CITY_LIST,
        WS_CONTACT_US,
        WS_DISCOUNT_COUPON_APPLY,
        WS_CUSTOMER_NOTIFICATION_COUNT,
        WS_BOOK_PROPERTY_BY_CUSTOMER,
        WS_PAY_LATER_PROPERTY_BY_CUSTOMER,
        WS_MANAGE_SCHEDULE,
        WS_UPDATE_MOBILE_NUMBER_FROM_UPH,
        WS_GET_PROPERTY_PRICE,
        WS_CUSTOMER_BOOKING_CONFIRMATION,
        WS_DEVICE_TOKEN_SAVE,
        WS_SEND_OTP,
        WS_ABUSE_REASON_LIST,
        WS_SAVE_ABUSE_REASON,
        WS_PAYFORT_TOKEN_RETURN,
        WS_LOG_OUT,
        WS_CREDIT;
    }
}
