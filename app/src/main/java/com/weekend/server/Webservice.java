package com.weekend.server;

import com.weekend.models.AbuseReasonListModel;
import com.weekend.models.AddToFavoriteModel;
import com.weekend.models.AutoLoginChangeStatusModel;
import com.weekend.models.BookPropertyByCustomerModel;
import com.weekend.models.CityModel;
import com.weekend.models.ContactUsModel;
import com.weekend.models.CountryModel;
import com.weekend.models.CreditModel;
import com.weekend.models.CustomerBookingConfirmationModel;
import com.weekend.models.CustomerBookingsDetailsModel;
import com.weekend.models.CustomerCancelBookingModel;
import com.weekend.models.CustomerLoginModel;
import com.weekend.models.CustomerNotificationCountModel;
import com.weekend.models.CustomerNotificationDeleteAllModel;
import com.weekend.models.CustomerNotificationDeleteModel;
import com.weekend.models.CustomerNotificationListModel;
import com.weekend.models.CustomerOrderRatingModel;
import com.weekend.models.CustomerRegisterModel;
import com.weekend.models.DeviceTokenSaveModel;
import com.weekend.models.DiscountCouponApplyModel;
import com.weekend.models.FavoriteModel;
import com.weekend.models.FrontForgotPasswordEmailModel;
import com.weekend.models.FrontForgotPasswordMobileModel;
import com.weekend.models.GetPropertyPriceModel;
import com.weekend.models.ISDCodeModel;
import com.weekend.models.LogoutModel;
import com.weekend.models.ManageSchedulesModel;
import com.weekend.models.MyBookingsModel;
import com.weekend.models.NeighbourhoodModel;
import com.weekend.models.PayfortToken;
import com.weekend.models.PopularCityListModel;
import com.weekend.models.PropertyDetailModel;
import com.weekend.models.PropertyListModel;
import com.weekend.models.PropertyReviewListModel;
import com.weekend.models.PushNotificationChangeStatusModel;
import com.weekend.models.RemoveFavoriteModel;
import com.weekend.models.ResendOTPModel;
import com.weekend.models.ResetPasswordModel;
import com.weekend.models.SaveAbuseReasonModel;
import com.weekend.models.SearchOptionModel;
import com.weekend.models.SendOtp;
import com.weekend.models.UpdateMobileNumberFromUphModel;
import com.weekend.models.UpdateUserProfileModel;
import com.weekend.models.UserChangePasswordModel;
import com.weekend.models.UserDataModel;
import com.weekend.models.ValidateOTPModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by hb on 28-May-16.
 */
public interface Webservice {

    /*@Multipart
    @POST("signup_email")
    Call<SignUpModel> requestSignUp(@PartMap Map<String, RequestBody> fileParams);

    @FormUrlEncoded
    @POST("interest_list")
    Call<InterestListModel> requestInterestList(@FieldMap Map<String, String> params);

    @GET("place/autocomplete/json")
    Call<AddressListModel> requestGoogleApi(@QueryMap Map<String, String> params);

    @GET("geocode/json")
    Call<ReverseGeocodingModel> requestReverseGeoCode(@QueryMap Map<String, String> params);*/

    @FormUrlEncoded
    @POST("customer_register")
    Call<CustomerRegisterModel> requestCustomerRegister(@FieldMap Map<String, String> params);

    @GET("customer_login")
    Call<CustomerLoginModel> requestCustomerLogin(@QueryMap Map<String, String> params);

    @GET("country_list")
    Call<CountryModel> requestCountryList(@QueryMap Map<String, String> params);

    @GET("city_list")
    Call<CityModel> requestCityList(@QueryMap Map<String, String> params);

    @GET("get_user_data")
    Call<UserDataModel> requestGetUserData(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer_update_profile_mobile")
    Call<UpdateUserProfileModel> requestUpdateUserProfile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("validate_otp")
    Call<ValidateOTPModel> requestValidateOTP(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("resend_otp")
    Call<ResendOTPModel> requestResendOTP(@FieldMap Map<String, String> params);

    @GET("get_isd_code")
    Call<ISDCodeModel> requestGetISDCode(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user_change_password")
    Call<UserChangePasswordModel> requestUserChangePassword(@FieldMap Map<String, String> params);

    @GET("favourite_list")
    Call<FavoriteModel> requestFavoriteList(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("add_to_favorite")
    Call<AddToFavoriteModel> requestAddToFavorite(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("remove_favorite")
    Call<RemoveFavoriteModel> requestRemoveFavorite(@FieldMap Map<String, String> params);

    //Using mobile
    @FormUrlEncoded
    @POST("front_forgot_password")
    Call<FrontForgotPasswordMobileModel> requestFrontForgotPasswordUsingMobile(@FieldMap Map<String, String> params);

    //Using email
    @FormUrlEncoded
    @POST("front_forgot_password")
    Call<FrontForgotPasswordEmailModel> requestFrontForgotPasswordUsingEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("reset_password")
    Call<ResetPasswordModel> requestResetPassword(@FieldMap Map<String, String> params);

    @GET("my_bookings")
    Call<MyBookingsModel> requestMyBookings(@QueryMap Map<String, String> params);

    @GET("customer_booking_detail")
    Call<CustomerBookingsDetailsModel> requestCustomerBookingDetail(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer_cancel_booking")
    Call<CustomerCancelBookingModel> requestCancelBooking(@FieldMap Map<String, String> params);

    @GET("customer_order_rating")
    Call<CustomerOrderRatingModel> requestCustomerOrderRating(@QueryMap Map<String, String> params);

    @GET("property_listing")
    Call<PropertyListModel> requestPropertyList(@QueryMap Map<String, String> params);

    @GET("property_detail_page")
    Call<PropertyDetailModel> requestPropertyDetailPage(@QueryMap Map<String, String> params);

    @GET("search_options")
    Call<SearchOptionModel> requestSearchOption(@QueryMap Map<String, String> params);

    @GET("neighbourhood_list")
    Call<NeighbourhoodModel> requestNeighbourhoodList(@QueryMap Map<String, String> params);

    @GET("customer_notification_list")
    Call<CustomerNotificationListModel> requestCustomerNotificationList(@QueryMap Map<String, String> params);

    @GET("customer_notification_delete_all")
    Call<CustomerNotificationDeleteAllModel> requestCustomerNotificationDeleteAll(@QueryMap Map<String, String> params);

    @GET("customer_notification_delete")
    Call<CustomerNotificationDeleteModel> requestCustomerNotificationDelete(@QueryMap Map<String, String> params);

    @GET("push_notification_change_status")
    Call<PushNotificationChangeStatusModel> requestPushNotificationChangeStatus(@QueryMap Map<String, String> params);

    @GET("property_review")
    Call<PropertyReviewListModel> requestPropertyReviewList(@QueryMap Map<String, String> params);

    @GET("auto_login_change_status")
    Call<AutoLoginChangeStatusModel> requestAutoLoginChangeStatus(@QueryMap Map<String, String> params);

    @GET("popular_city_list")
    Call<PopularCityListModel> requestPopularCityList(@QueryMap Map<String, String> params);

    @GET("contact_us")
    Call<ContactUsModel> requestContactUs(@QueryMap Map<String, String> params);

    @GET("discount_coupon_apply")
    Call<DiscountCouponApplyModel> requestDiscountCouponApply(@QueryMap Map<String, String> params);

    @GET("customer_notification_count")
    Call<CustomerNotificationCountModel> requestCustomerNotificationCount(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("book_property_by_customer")
    Call<BookPropertyByCustomerModel> requestBookPropertyByCustomer(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("pay_later_property_by_customer")
    Call<BookPropertyByCustomerModel> requestPayLaterPropertyByCustomer(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("manage_schedules")
    Call<ManageSchedulesModel> requestManageSchedule(@FieldMap Map<String, String> params);

    @GET("update_mobile_number_from_uph")
    Call<UpdateMobileNumberFromUphModel> requestUpdateMobileNumberFromUph(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_property_price")
    Call<GetPropertyPriceModel> requestGetPropertyPrice(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("customer_booking_confirmation")
    Call<CustomerBookingConfirmationModel> requestCustomerBookingConfirmation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("device_token_save")
    Call<DeviceTokenSaveModel> requestDeviceTokenSave(@FieldMap Map<String, String> params);

    @GET("send_otp")
    Call<SendOtp> requestSendOtp(@QueryMap Map<String, String> params);

    @GET("abuse_reason_list")
    Call<AbuseReasonListModel> requestAbuseReasonList(@QueryMap Map<String, String> params);

    @GET("save_abuse_reason")
    Call<SaveAbuseReasonModel> requestSaveAbuseReason(@QueryMap Map<String, String> params);

    @GET("payfort_token_return")
    Call<PayfortToken> requestPayfortTokenReturn(@QueryMap Map<String, String> params);

    @GET("log_out")
    Call<LogoutModel> requestLogout(@QueryMap Map<String, String> params);

    @GET("user_credit")
    Call<CreditModel> requestCredit(@QueryMap Map<String, String> params);
}