package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerBookingsDetailsModel implements WSResponse, Serializable {

    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<Datum>();

    /**
     * @return The settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * @param settings The settings
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * @return The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("booking_order_id")
        @Expose
        private String bookingOrderId;
        @SerializedName("order_by_user_id")
        @Expose
        private String orderByUserId;
        @SerializedName("booking_id")
        @Expose
        private String bookingId;
        @SerializedName("booking_status")
        @Expose
        private String bookingStatus;
        @SerializedName("booking_status_check")
        @Expose
        private String bookingStatusCheck;
        @SerializedName("booked_date")
        @Expose
        private String bookedDate;
        @SerializedName("property_title")
        @Expose
        private String propertyTitle;
        @SerializedName("property_type")
        @Expose
        private String propertyType;
        @SerializedName("booking_phone")
        @Expose
        private String bookingPhone;
        @SerializedName("booking_name")
        @Expose
        private String bookingName;
        @SerializedName("national_id")
        @Expose
        private String nationalId;
        @SerializedName("booking_email")
        @Expose
        private String bookingEmail;
        @SerializedName("booking_comment")
        @Expose
        private String bookingComment;
        @SerializedName("property_phone1")
        @Expose
        private String propertyPhone1;
        @SerializedName("property_phone2")
        @Expose
        private String propertyPhone2;
        @SerializedName("total_price")
        @Expose
        private String totalPrice;
        @SerializedName("amount_paid")
        @Expose
        private String amountPaid;
        @SerializedName("remaining_amount_to_owner")
        @Expose
        private String remainingAmountToOwner;
        @SerializedName("down_payment")
        @Expose
        private String downPayment;
        @SerializedName("service_charge")
        @Expose
        private String serviceCharge;
        @SerializedName("discount_code")
        @Expose
        private String discountCode;
        @SerializedName("discount_price")
        @Expose
        private String discountPrice;
        @SerializedName("discount_by")
        @Expose
        private String discountBy;
        @SerializedName("property_email")
        @Expose
        private String propertyEmail;
        @SerializedName("owner_name")
        @Expose
        private String ownerName;
        @SerializedName("owner_email")
        @Expose
        private String ownerEmail;
        @SerializedName("owner_mobile_no")
        @Expose
        private String ownerMobileNo;
        @SerializedName("chalet_name")
        @Expose
        private String chaletName;
        @SerializedName("cancel_date")
        @Expose
        private String cancelDate;
        @SerializedName("cancel_reason")
        @Expose
        private String cancelReason;
        @SerializedName("check_in_time")
        @Expose
        private String checkInTime;
        @SerializedName("check_out_time")
        @Expose
        private String checkOutTime;
        @SerializedName("cancellation_policy_type")
        @Expose
        private String cancellationPolicyType;
        @SerializedName("cancel_before_time")
        @Expose
        private String cancelBeforeTime;
        @SerializedName("cancel_before_type")
        @Expose
        private String cancelBeforeType;
        @SerializedName("cancellation_policy")
        @Expose
        private String cancellationPolicy;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("neighbourhood")
        @Expose
        private String neighbourhood;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("refulnd_policy")
        @Expose
        private String refulndPolicy;
        @SerializedName("booking_dates")
        @Expose
        private String bookingDates;
        @SerializedName("site_email")
        @Expose
        private String siteEmail;
        @SerializedName("site_phone")
        @Expose
        private String sitePhone;
        @SerializedName("booking_from_to_times")
        @Expose
        private String bookingFromToTimes;
        @SerializedName("cancel_flag")
        @Expose
        private String cancelFlag;
        @SerializedName("booking_day")
        @Expose
        private String bookingDay;
        @SerializedName("terms_conditions")
        @Expose
        private String termsConditions;
        @SerializedName("insurance_amount")
        @Expose
        private String insuranceAmount;

        /**
         * @return The bookingOrderId
         */
        public String getBookingOrderId() {
            return bookingOrderId;
        }

        /**
         * @param bookingOrderId The booking_order_id
         */
        public void setBookingOrderId(String bookingOrderId) {
            this.bookingOrderId = bookingOrderId;
        }

        /**
         * @return The orderByUserId
         */
        public String getOrderByUserId() {
            return orderByUserId;
        }

        /**
         * @param orderByUserId The order_by_user_id
         */
        public void setOrderByUserId(String orderByUserId) {
            this.orderByUserId = orderByUserId;
        }

        /**
         * @return The bookingId
         */
        public String getBookingId() {
            return bookingId;
        }

        /**
         * @param bookingId The booking_id
         */
        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        /**
         * @return The bookingStatus
         */
        public String getBookingStatus() {
            return bookingStatus;
        }

        /**
         * @param bookingStatus The booking_status
         */
        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        /**
         * @return The bookingStatus
         */
        public String getBookingStatusCheck() {
            return bookingStatusCheck;
        }

        /**
         * @param bookingStatus The booking_status
         */
        public void setBookingStatusCheck(String bookingStatusCheck) {
            this.bookingStatusCheck = bookingStatusCheck;
        }

        /**
         * @return The bookedDate
         */
        public String getBookedDate() {
            return bookedDate;
        }

        /**
         * @param bookedDate The booked_date
         */
        public void setBookedDate(String bookedDate) {
            this.bookedDate = bookedDate;
        }

        /**
         * @return The propertyTitle
         */
        public String getPropertyTitle() {
            return propertyTitle;
        }

        /**
         * @param propertyTitle The property_title
         */
        public void setPropertyTitle(String propertyTitle) {
            this.propertyTitle = propertyTitle;
        }

        /**
         * @return The propertyType
         */
        public String getPropertyType() {
            return propertyType;
        }

        /**
         * @param propertyType The property_type
         */
        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }

        /**
         * @return The bookingPhone
         */
        public String getBookingPhone() {
            return bookingPhone;
        }

        /**
         * @param bookingPhone The booking_phone
         */
        public void setBookingPhone(String bookingPhone) {
            this.bookingPhone = bookingPhone;
        }

        /**
         * @return The bookingName
         */
        public String getBookingName() {
            return bookingName;
        }

        /**
         * @param bookingName The booking_name
         */
        public void setBookingName(String bookingName) {
            this.bookingName = bookingName;
        }

        /**
         * @return The nationalId
         */
        public String getNationalId() {
            return nationalId;
        }

        /**
         * @param nationalId The national_id
         */
        public void setNationalId(String nationalId) {
            this.nationalId = nationalId;
        }

        /**
         * @return The bookingEmail
         */
        public String getBookingEmail() {
            return bookingEmail;
        }

        /**
         * @param bookingEmail The booking_email
         */
        public void setBookingEmail(String bookingEmail) {
            this.bookingEmail = bookingEmail;
        }

        /**
         * @return The bookingComment
         */
        public String getBookingComment() {
            return bookingComment;
        }

        /**
         * @param bookingComment The booking_comment
         */
        public void setBookingComment(String bookingComment) {
            this.bookingComment = bookingComment;
        }

        /**
         * @return The propertyPhone1
         */
        public String getPropertyPhone1() {
            return propertyPhone1;
        }

        /**
         * @param propertyPhone1 The property_phone1
         */
        public void setPropertyPhone1(String propertyPhone1) {
            this.propertyPhone1 = propertyPhone1;
        }

        /**
         * @return The propertyPhone2
         */
        public String getPropertyPhone2() {
            return propertyPhone2;
        }

        /**
         * @param propertyPhone2 The property_phone2
         */
        public void setPropertyPhone2(String propertyPhone2) {
            this.propertyPhone2 = propertyPhone2;
        }

        /**
         * @return The totalPrice
         */
        public String getTotalPrice() {
            return totalPrice;
        }

        /**
         * @param totalPrice The total_price
         */
        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        /**
         * @return The amountPaid
         */
        public String getAmountPaid() {
            return amountPaid;
        }

        /**
         * @param amountPaid The amount_paid
         */
        public void setAmountPaid(String amountPaid) {
            this.amountPaid = amountPaid;
        }

        /**
         * @return The remainingAmountToOwner
         */
        public String getRemainingAmountToOwner() {
            return remainingAmountToOwner;
        }

        /**
         * @param remainingAmountToOwner The remaining_amount_to_owner
         */
        public void setRemainingAmountToOwner(String remainingAmountToOwner) {
            this.remainingAmountToOwner = remainingAmountToOwner;
        }

        /**
         * @return The downPayment
         */
        public String getDownPayment() {
            return downPayment;
        }

        /**
         * @param downPayment The down_payment
         */
        public void setDownPayment(String downPayment) {
            this.downPayment = downPayment;
        }

        /**
         * @return The serviceCharge
         */
        public String getServiceCharge() {
            return serviceCharge;
        }

        /**
         * @param serviceCharge The service_charge
         */
        public void setServiceCharge(String serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        /**
         * @return The discountCode
         */
        public String getDiscountCode() {
            return discountCode;
        }

        /**
         * @param discountCode The discount_code
         */
        public void setDiscountCode(String discountCode) {
            this.discountCode = discountCode;
        }

        /**
         * @return The discountPrice
         */
        public String getDiscountPrice() {
            return discountPrice;
        }

        /**
         * @param discountPrice The discount_price
         */
        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        /**
         * @return The discountBy
         */
        public String getDiscountBy() {
            return discountBy;
        }

        /**
         * @param discountBy The discount_by
         */
        public void setDiscountBy(String discountBy) {
            this.discountBy = discountBy;
        }

        /**
         * @return The propertyEmail
         */
        public String getPropertyEmail() {
            return propertyEmail;
        }

        /**
         * @param propertyEmail The property_email
         */
        public void setPropertyEmail(String propertyEmail) {
            this.propertyEmail = propertyEmail;
        }

        /**
         * @return The ownerName
         */
        public String getOwnerName() {
            return ownerName;
        }

        /**
         * @param ownerName The owner_name
         */
        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        /**
         * @return The ownerEmail
         */
        public String getOwnerEmail() {
            return ownerEmail;
        }

        /**
         * @param ownerEmail The owner_email
         */
        public void setOwnerEmail(String ownerEmail) {
            this.ownerEmail = ownerEmail;
        }

        /**
         * @return The ownerMobileNo
         */
        public String getOwnerMobileNo() {
            return ownerMobileNo;
        }

        /**
         * @param ownerMobileNo The owner_mobile_no
         */
        public void setOwnerMobileNo(String ownerMobileNo) {
            this.ownerMobileNo = ownerMobileNo;
        }

        /**
         * @return The chaletName
         */
        public String getChaletName() {
            return chaletName;
        }

        /**
         * @param chaletName The chalet_name
         */
        public void setChaletName(String chaletName) {
            this.chaletName = chaletName;
        }

        /**
         * @return The cancelDate
         */
        public String getCancelDate() {
            return cancelDate;
        }

        /**
         * @param cancelDate The cancel_date
         */
        public void setCancelDate(String cancelDate) {
            this.cancelDate = cancelDate;
        }

        /**
         * @return The cancelReason
         */
        public String getCancelReason() {
            return cancelReason;
        }

        /**
         * @param cancelReason The cancel_reason
         */
        public void setCancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
        }

        /**
         * @return The checkInTime
         */
        public String getCheckInTime() {
            return checkInTime;
        }

        /**
         * @param checkInTime The check_in_time
         */
        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        /**
         * @return The checkOutTime
         */
        public String getCheckOutTime() {
            return checkOutTime;
        }

        /**
         * @param checkOutTime The check_out_time
         */
        public void setCheckOutTime(String checkOutTime) {
            this.checkOutTime = checkOutTime;
        }

        /**
         * @return The cancellationPolicyType
         */
        public String getCancellationPolicyType() {
            return cancellationPolicyType;
        }

        /**
         * @param cancellationPolicyType The cancellation_policy_type
         */
        public void setCancellationPolicyType(String cancellationPolicyType) {
            this.cancellationPolicyType = cancellationPolicyType;
        }

        /**
         * @return The cancelBeforeTime
         */
        public String getCancelBeforeTime() {
            return cancelBeforeTime;
        }

        /**
         * @param cancelBeforeTime The cancel_before_time
         */
        public void setCancelBeforeTime(String cancelBeforeTime) {
            this.cancelBeforeTime = cancelBeforeTime;
        }

        /**
         * @return The cancelBeforeType
         */
        public String getCancelBeforeType() {
            return cancelBeforeType;
        }

        /**
         * @param cancelBeforeType The cancel_before_type
         */
        public void setCancelBeforeType(String cancelBeforeType) {
            this.cancelBeforeType = cancelBeforeType;
        }

        /**
         * @return The cancellationPolicy
         */
        public String getCancellationPolicy() {
            return cancellationPolicy;
        }

        /**
         * @param cancellationPolicy The cancellation_policy
         */
        public void setCancellationPolicy(String cancellationPolicy) {
            this.cancellationPolicy = cancellationPolicy;
        }

        /**
         * @return The countryName
         */
        public String getCountryName() {
            return countryName;
        }

        /**
         * @param countryName The country_name
         */
        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        /**
         * @return The neighbourhood
         */
        public String getNeighbourhood() {
            return neighbourhood;
        }

        /**
         * @param neighbourhood The neighbourhood
         */
        public void setNeighbourhood(String neighbourhood) {
            this.neighbourhood = neighbourhood;
        }

        /**
         * @return The city
         */
        public String getCity() {
            return city;
        }

        /**
         * @param city The city
         */
        public void setCity(String city) {
            this.city = city;
        }

        /**
         * @return The refulndPolicy
         */
        public String getRefulndPolicy() {
            return refulndPolicy;
        }

        /**
         * @param refulndPolicy The refulnd_policy
         */
        public void setRefulndPolicy(String refulndPolicy) {
            this.refulndPolicy = refulndPolicy;
        }

        /**
         * @return The bookingDates
         */
        public String getBookingDates() {
            return bookingDates;
        }

        /**
         * @param bookingDates The booking_dates
         */
        public void setBookingDates(String bookingDates) {
            this.bookingDates = bookingDates;
        }

        /**
         * @return The siteEmail
         */
        public String getSiteEmail() {
            return siteEmail;
        }

        /**
         * @param siteEmail The site_email
         */
        public void setSiteEmail(String siteEmail) {
            this.siteEmail = siteEmail;
        }

        /**
         * @return The sitePhone
         */
        public String getSitePhone() {
            return sitePhone;
        }

        /**
         * @param sitePhone The site_phone
         */
        public void setSitePhone(String sitePhone) {
            this.sitePhone = sitePhone;
        }

        /**
         * @return The bookingFromToTimes
         */
        public String getBookingFromToTimes() {
            return bookingFromToTimes;
        }

        /**
         * @param bookingFromToTimes The booking_from_to_times
         */
        public void setBookingFromToTimes(String bookingFromToTimes) {
            this.bookingFromToTimes = bookingFromToTimes;
        }

        /**
         * @return The cancelFlag
         */
        public String getCancelFlag() {
            return cancelFlag;
        }

        /**
         * @param cancelFlag The cancel_flag
         */
        public void setCancelFlag(String cancelFlag) {
            this.cancelFlag = cancelFlag;
        }

        /**
         * @return The bookingDay
         */
        public String getBookingDay() {
            return bookingDay;
        }

        /**
         * @param bookingDay The booking_day
         */
        public void setBookingDay(String bookingDay) {
            this.bookingDay = bookingDay;
        }

        /**
         * @return The termsConditions
         */
        public String getTermsConditions() {
            return termsConditions;
        }

        /**
         * @param termsConditions The terms_conditions
         */
        public void setTermsConditions(String termsConditions) {
            this.termsConditions = termsConditions;
        }

        /**
         * @return The insuranceAmount
         */
        public String getInsuranceAmount() {
            return insuranceAmount;
        }

        /**
         * @param insuranceAmount The insurance_amount
         */
        public void setInsuranceAmount(String insuranceAmount) {
            this.insuranceAmount = insuranceAmount;
        }


    }

    public class Settings {

        @SerializedName("success")
        @Expose
        private String success;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("fields")
        @Expose
        private List<String> fields = new ArrayList<String>();

        /**
         * @return The success
         */
        public String getSuccess() {
            return success;
        }

        /**
         * @param success The success
         */
        public void setSuccess(String success) {
            this.success = success;
        }

        /**
         * @return The message
         */
        public String getMessage() {
            return message;
        }

        /**
         * @param message The message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * @return The fields
         */
        public List<String> getFields() {
            return fields;
        }

        /**
         * @param fields The fields
         */
        public void setFields(List<String> fields) {
            this.fields = fields;
        }

    }
}