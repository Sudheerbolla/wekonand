package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyReviewListModel implements WSResponse, Serializable {

    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("data")
    @Expose
    private Data data;

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
    public Data getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(Data data) {
        this.data = data;
    }

    public class Settings {

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

    public class Data {

        @SerializedName("property_average_rating")
        @Expose
        private List<PropertyAverageRating> propertyAverageRating = new ArrayList<PropertyAverageRating>();
        @SerializedName("property_reviews")
        @Expose
        private List<PropertyReview> propertyReviews = new ArrayList<PropertyReview>();

        /**
         * @return The propertyAverageRating
         */
        public List<PropertyAverageRating> getPropertyAverageRating() {
            return propertyAverageRating;
        }

        /**
         * @param propertyAverageRating The property_average_rating
         */
        public void setPropertyAverageRating(List<PropertyAverageRating> propertyAverageRating) {
            this.propertyAverageRating = propertyAverageRating;
        }

        /**
         * @return The propertyReviews
         */
        public List<PropertyReview> getPropertyReviews() {
            return propertyReviews;
        }

        /**
         * @param propertyReviews The property_reviews
         */
        public void setPropertyReviews(List<PropertyReview> propertyReviews) {
            this.propertyReviews = propertyReviews;
        }

    }

    public class PropertyAverageRating {

        @SerializedName("avg_rating")
        @Expose
        private String avgRating;
        @SerializedName("total_reviews")
        @Expose
        private String totalReviews;

        /**
         * @return The avgRating
         */
        public String getAvgRating() {
            return avgRating;
        }

        /**
         * @param avgRating The avg_rating
         */
        public void setAvgRating(String avgRating) {
            this.avgRating = avgRating;
        }

        /**
         * @return The totalReviews
         */
        public String getTotalReviews() {
            return totalReviews;
        }

        /**
         * @param totalReviews The total_reviews
         */
        public void setTotalReviews(String totalReviews) {
            this.totalReviews = totalReviews;
        }

    }

    public class PropertyReview {

        @SerializedName("property_review_id")
        @Expose
        private String propertyReviewId;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("review")
        @Expose
        private String review;
        @SerializedName("booking_date")
        @Expose
        private String bookingDate;
        @SerializedName("review_date")
        @Expose
        private String reviewDate;
        @SerializedName("owner_reply")
        @Expose
        private String ownerReply;
        @SerializedName("reply_text")
        @Expose
        private String replyText;
        @SerializedName("reply_date")
        @Expose
        private String replyDate;
        @SerializedName("report_by_owner")
        @Expose
        private String reportByOwner;
        @SerializedName("owner_report_text")
        @Expose
        private String ownerReportText;
        @SerializedName("owner_report_date")
        @Expose
        private String ownerReportDate;

        /**
         * @return The propertyReviewId
         */
        public String getPropertyReviewId() {
            return propertyReviewId;
        }

        /**
         * @param propertyReviewId The property_review_id
         */
        public void setPropertyReviewId(String propertyReviewId) {
            this.propertyReviewId = propertyReviewId;
        }

        /**
         * @return The customerName
         */
        public String getCustomerName() {
            return customerName;
        }

        /**
         * @param customerName The customer_name
         */
        public void setCustomerName(String customerName) {
            this.customerName = customerName;
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
         * @return The review
         */
        public String getReview() {
            return review;
        }

        /**
         * @param review The review
         */
        public void setReview(String review) {
            this.review = review;
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
         * @return The reviewDate
         */
        public String getReviewDate() {
            return reviewDate;
        }

        /**
         * @param reviewDate The review_date
         */
        public void setReviewDate(String reviewDate) {
            this.reviewDate = reviewDate;
        }

        /**
         * @return The ownerReply
         */
        public String getOwnerReply() {
            return ownerReply;
        }

        /**
         * @param ownerReply The owner_reply
         */
        public void setOwnerReply(String ownerReply) {
            this.ownerReply = ownerReply;
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
         * @return The replyDate
         */
        public String getReplyDate() {
            return replyDate;
        }

        /**
         * @param replyDate The reply_date
         */
        public void setReplyDate(String replyDate) {
            this.replyDate = replyDate;
        }

        /**
         * @return The reportByOwner
         */
        public String getReportByOwner() {
            return reportByOwner;
        }

        /**
         * @param reportByOwner The report_by_owner
         */
        public void setReportByOwner(String reportByOwner) {
            this.reportByOwner = reportByOwner;
        }

        /**
         * @return The ownerReportText
         */
        public String getOwnerReportText() {
            return ownerReportText;
        }

        /**
         * @param ownerReportText The owner_report_text
         */
        public void setOwnerReportText(String ownerReportText) {
            this.ownerReportText = ownerReportText;
        }

        /**
         * @return The ownerReportDate
         */
        public String getOwnerReportDate() {
            return ownerReportDate;
        }

        /**
         * @param ownerReportDate The owner_report_date
         */
        public void setOwnerReportDate(String ownerReportDate) {
            this.ownerReportDate = ownerReportDate;
        }

    }
}