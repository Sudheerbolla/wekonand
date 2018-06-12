package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyBookingsModel implements WSResponse, Serializable {

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

    public class Settings implements Serializable {

        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("curr_page")
        @Expose
        private String currPage;
        @SerializedName("prev_page")
        @Expose
        private String prevPage;
        @SerializedName("next_page")
        @Expose
        private String nextPage;
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
         * @return The count
         */
        public String getCount() {
            return count;
        }

        /**
         * @param count The count
         */
        public void setCount(String count) {
            this.count = count;
        }

        /**
         * @return The currPage
         */
        public String getCurrPage() {
            return currPage;
        }

        /**
         * @param currPage The curr_page
         */
        public void setCurrPage(String currPage) {
            this.currPage = currPage;
        }

        /**
         * @return The prevPage
         */
        public String getPrevPage() {
            return prevPage;
        }

        /**
         * @param prevPage The prev_page
         */
        public void setPrevPage(String prevPage) {
            this.prevPage = prevPage;
        }

        /**
         * @return The nextPage
         */
        public String getNextPage() {
            return nextPage;
        }

        /**
         * @param nextPage The next_page
         */
        public void setNextPage(String nextPage) {
            this.nextPage = nextPage;
        }

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

    public class Datum implements Serializable {

        @SerializedName("get_soccer_image")
        @Expose
        private List<GetSoccerImage> getSoccerImage = new ArrayList<GetSoccerImage>();
        @SerializedName("booking_id")
        @Expose
        private String bookingId;
        @SerializedName("property_title")
        @Expose
        private String propertyTitle;
        @SerializedName("chalet_name")
        @Expose
        private String chaletName;
        @SerializedName("booked_date")
        @Expose
        private String bookedDate;
        @SerializedName("booking_status")
        @Expose
        private String bookingStatus;
        @SerializedName("national_id")
        @Expose
        private String nationalId;
        @SerializedName("phone_no")
        @Expose
        private String phoneNo;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("booking_date")
        @Expose
        private String bookingDate;
        @SerializedName("recommended")
        @Expose
        private String recommended;
        @SerializedName("verified")
        @Expose
        private String verified;
        @SerializedName("property_id")
        @Expose
        private String propertyId;
        @SerializedName("rating_id")
        @Expose
        private String ratingId;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("order_by_user_id")
        @Expose
        private String orderByUserId;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("booking_type")
        @Expose
        private String bookingType;
        @SerializedName("neighbourhood")
        @Expose
        private String neighbourhood;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("review_comment")
        @Expose
        private String reviewComment;
        @SerializedName("reply_byowner")
        @Expose
        private String replyByowner;
        @SerializedName("reply_text")
        @Expose
        private String replyText;
        @SerializedName("cancel_flag")
        @Expose
        private String cancelFlag;
        @SerializedName("allow_rating")
        @Expose
        private String allowRating;
        @SerializedName("owner_reply_date")
        @Expose
        private String ownerReplyDate;
        @SerializedName("order_rating_date")
        @Expose
        private String orderRatingDate;
        @SerializedName("cancel_date")
        @Expose
        private String cancelDate;
        @SerializedName("report_byowner")
        @Expose
        private String reportByowner;
        @SerializedName("booking_name")
        @Expose
        private String bookingName;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("full_address")
        @Expose
        private String fullAddress;
        @SerializedName("p_type")
        @Expose
        private String propertyType;


        /**
         * @return The getSoccerImage
         */
        public List<GetSoccerImage> getGetSoccerImage() {
            return getSoccerImage;
        }

        /**
         * @param getSoccerImage The get_soccer_image
         */
        public void setGetSoccerImage(List<GetSoccerImage> getSoccerImage) {
            this.getSoccerImage = getSoccerImage;
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
         * @return The phoneNo
         */
        public String getPhoneNo() {
            return phoneNo;
        }

        /**
         * @param phoneNo The phone_no
         */
        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        /**
         * @return The amount
         */
        public String getAmount() {
            return amount;
        }

        /**
         * @param amount The amount
         */
        public void setAmount(String amount) {
            this.amount = amount;
        }

        /**
         * @return The bookingDate
         */
        public String getBookingDate() {
            return bookingDate;
        }

        /**
         * @param bookingDate The booking_date
         */
        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        /**
         * @return The recommended
         */
        public String getRecommended() {
            return recommended;
        }

        /**
         * @param recommended The recommended
         */
        public void setRecommended(String recommended) {
            this.recommended = recommended;
        }

        /**
         * @return The verified
         */
        public String getVerified() {
            return verified;
        }

        /**
         * @param verified The verified
         */
        public void setVerified(String verified) {
            this.verified = verified;
        }

        /**
         * @return The propertyId
         */
        public String getPropertyId() {
            return propertyId;
        }

        /**
         * @param propertyId The property_id
         */
        public void setPropertyId(String propertyId) {
            this.propertyId = propertyId;
        }

        /**
         * @return The ratingId
         */
        public String getRatingId() {
            return ratingId;
        }

        /**
         * @param ratingId The rating_id
         */
        public void setRatingId(String ratingId) {
            this.ratingId = ratingId;
        }

        /**
         * @return The rating
         */
        public String getRating() {
            return rating;
        }

        /**
         * @param rating The rating
         */
        public void setRating(String rating) {
            this.rating = rating;
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
         * @return The orderId
         */
        public String getOrderId() {
            return orderId;
        }

        /**
         * @param orderId The order_id
         */
        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        /**
         * @return The bookingType
         */
        public String getBookingType() {
            return bookingType;
        }

        /**
         * @param bookingType The booking_type
         */
        public void setBookingType(String bookingType) {
            this.bookingType = bookingType;
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
         * @return The reviewComment
         */
        public String getReviewComment() {
            return reviewComment;
        }

        /**
         * @param reviewComment The review_comment
         */
        public void setReviewComment(String reviewComment) {
            this.reviewComment = reviewComment;
        }

        /**
         * @return The replyByowner
         */
        public String getReplyByowner() {
            return replyByowner;
        }

        /**
         * @param replyByowner The reply_byowner
         */
        public void setReplyByowner(String replyByowner) {
            this.replyByowner = replyByowner;
        }

        /**
         * @return The replyText
         */
        public String getReplyText() {
            return replyText;
        }

        /**
         * @param replyText The reply_text
         */
        public void setReplyText(String replyText) {
            this.replyText = replyText;
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
         * @return The allowRating
         */
        public String getAllowRating() {
            return allowRating;
        }

        /**
         * @param allowRating The allow_rating
         */
        public void setAllowRating(String allowRating) {
            this.allowRating = allowRating;
        }

        /**
         * @return The ownerReplyDate
         */
        public String getOwnerReplyDate() {
            return ownerReplyDate;
        }

        /**
         * @param ownerReplyDate The owner_reply_date
         */
        public void setOwnerReplyDate(String ownerReplyDate) {
            this.ownerReplyDate = ownerReplyDate;
        }

        /**
         * @return The orderRatingDate
         */
        public String getOrderRatingDate() {
            return orderRatingDate;
        }

        /**
         * @param orderRatingDate The order_rating_date
         */
        public void setOrderRatingDate(String orderRatingDate) {
            this.orderRatingDate = orderRatingDate;
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
         * @return The reportByowner
         */
        public String getReportByowner() {
            return reportByowner;
        }

        /**
         * @param reportByowner The report_byowner
         */
        public void setReportByowner(String reportByowner) {
            this.reportByowner = reportByowner;
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
         * @return The address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @param address The address
         */
        public void setAddress(String address) {
            this.address = address;
        }

        /**
         * @return The fullAddress
         */
        public String getFullAddress() {
            return fullAddress;
        }

        /**
         * @param fullAddress The full_address
         */
        public void setFullAddress(String fullAddress) {
            this.fullAddress = fullAddress;
        }

        /**
         * @return The properyType
         */
        public String getPropertyType() {
            return propertyType;
        }

        /**
         * @param propertyType The p_type
         */
        public void setPropertyType(String propertyType) {
            this.propertyType = propertyType;
        }
    }

    public class GetSoccerImage implements Serializable {

        @SerializedName("soccer_image")
        @Expose
        private String soccerImage;
        @SerializedName("chalet_image")
        @Expose
        private String chaletImage;
        @SerializedName("pci_property_chalet_id")
        @Expose
        private String pciPropertyChaletId;

        /**
         * @return The soccerImage
         */
        public String getSoccerImage() {
            return soccerImage;
        }

        /**
         * @param soccerImage The soccer_image
         */
        public void setSoccerImage(String soccerImage) {
            this.soccerImage = soccerImage;
        }

        /**
         * @return The chaletImage
         */
        public String getChaletImage() {
            return chaletImage;
        }

        /**
         * @param chaletImage The chalet_image
         */
        public void setChaletImage(String chaletImage) {
            this.chaletImage = chaletImage;
        }

        /**
         * @return The pciPropertyChaletId
         */
        public String getPciPropertyChaletId() {
            return pciPropertyChaletId;
        }

        /**
         * @param pciPropertyChaletId The pci_property_chalet_id
         */
        public void setPciPropertyChaletId(String pciPropertyChaletId) {
            this.pciPropertyChaletId = pciPropertyChaletId;
        }

    }
}