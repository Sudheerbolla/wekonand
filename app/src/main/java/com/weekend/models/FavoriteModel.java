package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoriteModel implements WSResponse, Serializable {

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

    public class Datum {

        @SerializedName("min_prize")
        @Expose
        private String minPrize;
        @SerializedName("total_chalet")
        @Expose
        private String totalChalet;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("property_id")
        @Expose
        private String propertyId;
        @SerializedName("added_date")
        @Expose
        private String addedDate;
        @SerializedName("property_title")
        @Expose
        private String propertyTitle;
        @SerializedName("neighbourhood_id")
        @Expose
        private String neighbourhoodId;
        @SerializedName("mark_as_featured")
        @Expose
        private String markAsFeatured;
        @SerializedName("mark_as_suggested")
        @Expose
        private String markAsSuggested;
        @SerializedName("mark_as_verified")
        @Expose
        private String markAsVerified;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("property_view_count")
        @Expose
        private String propertyViewCount;
        @SerializedName("avg_rating")
        @Expose
        private String avgRating;
        @SerializedName("total_reviews")
        @Expose
        private String totalReviews;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("country")
        @Expose
        private String country;

        /**
         * @return The minPrize
         */
        public String getMinPrize() {
            return minPrize;
        }

        /**
         * @param minPrize The min_prize
         */
        public void setMinPrize(String minPrize) {
            this.minPrize = minPrize;
        }

        /**
         * @return The totalChalet
         */
        public String getTotalChalet() {
            return totalChalet;
        }

        /**
         * @param totalChalet The total_chalet
         */
        public void setTotalChalet(String totalChalet) {
            this.totalChalet = totalChalet;
        }

        /**
         * @return The userId
         */
        public String getUserId() {
            return userId;
        }

        /**
         * @param userId The user_id
         */
        public void setUserId(String userId) {
            this.userId = userId;
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
         * @return The addedDate
         */
        public String getAddedDate() {
            return addedDate;
        }

        /**
         * @param addedDate The added_date
         */
        public void setAddedDate(String addedDate) {
            this.addedDate = addedDate;
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
         * @return The neighbourhoodId
         */
        public String getNeighbourhoodId() {
            return neighbourhoodId;
        }

        /**
         * @param neighbourhoodId The neighbourhood_id
         */
        public void setNeighbourhoodId(String neighbourhoodId) {
            this.neighbourhoodId = neighbourhoodId;
        }

        /**
         * @return The markAsFeatured
         */
        public String getMarkAsFeatured() {
            return markAsFeatured;
        }

        /**
         * @param markAsFeatured The mark_as_featured
         */
        public void setMarkAsFeatured(String markAsFeatured) {
            this.markAsFeatured = markAsFeatured;
        }

        /**
         * @return The markAsSuggested
         */
        public String getMarkAsSuggested() {
            return markAsSuggested;
        }

        /**
         * @param markAsSuggested The mark_as_suggested
         */
        public void setMarkAsSuggested(String markAsSuggested) {
            this.markAsSuggested = markAsSuggested;
        }

        /**
         * @return The markAsVerified
         */
        public String getMarkAsVerified() {
            return markAsVerified;
        }

        /**
         * @param markAsVerified The mark_as_verified
         */
        public void setMarkAsVerified(String markAsVerified) {
            this.markAsVerified = markAsVerified;
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
         * @return The propertyViewCount
         */
        public String getPropertyViewCount() {
            return propertyViewCount;
        }

        /**
         * @param propertyViewCount The property_view_count
         */
        public void setPropertyViewCount(String propertyViewCount) {
            this.propertyViewCount = propertyViewCount;
        }

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

        /**
         * @return The image
         */
        public String getImage() {
            return image;
        }

        /**
         * @param image The image
         */
        public void setImage(String image) {
            this.image = image;
        }

        /**
         * @return The country
         */
        public String getCountry() {
            return country;
        }

        /**
         * @param country The country
         */
        public void setCountry(String country) {
            this.country = country;
        }

    }
}