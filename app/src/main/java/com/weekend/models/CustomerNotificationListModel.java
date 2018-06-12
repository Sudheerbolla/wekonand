package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class CustomerNotificationListModel implements WSResponse, Serializable {

    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("data")
    @Expose
    private List<Datum> data = new ArrayList<Datum>();

    /**
     *
     * @return
     * The settings
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     *
     * @param settings
     * The settings
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     *
     * @return
     * The data
     */
    public List<Datum> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("notification_id")
        @Expose
        private String notificationId;
        @SerializedName("notification_text")
        @Expose
        private String notificationText;
        @SerializedName("notification_text_ar")
        @Expose
        private String notificationTextAr;
        @SerializedName("notification_date_time")
        @Expose
        private String notificationDateTime;

        /**
         *
         * @return
         * The notificationId
         */
        public String getNotificationId() {
            return notificationId;
        }

        /**
         *
         * @param notificationId
         * The notification_id
         */
        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
        }

        /**
         *
         * @return
         * The notificationText
         */
        public String getNotificationText() {
            return notificationText;
        }

        /**
         *
         * @param notificationText
         * The notification_text
         */
        public void setNotificationText(String notificationText) {
            this.notificationText = notificationText;
        }

        /**
         *
         * @return
         * The notificationTextAr
         */
        public String getNotificationTextAr() {
            return notificationTextAr;
        }

        /**
         *
         * @param notificationTextAr
         * The notification_text_ar
         */
        public void setNotificationTextAr(String notificationTextAr) {
            this.notificationTextAr = notificationTextAr;
        }

        /**
         *
         * @return
         * The notificationDateTime
         */
        public String getNotificationDateTime() {
            return notificationDateTime;
        }

        /**
         *
         * @param notificationDateTime
         * The notification_date_time
         */
        public void setNotificationDateTime(String notificationDateTime) {
            this.notificationDateTime = notificationDateTime;
        }
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
         *
         * @return
         * The count
         */
        public String getCount() {
            return count;
        }

        /**
         *
         * @param count
         * The count
         */
        public void setCount(String count) {
            this.count = count;
        }

        /**
         *
         * @return
         * The currPage
         */
        public String getCurrPage() {
            return currPage;
        }

        /**
         *
         * @param currPage
         * The curr_page
         */
        public void setCurrPage(String currPage) {
            this.currPage = currPage;
        }

        /**
         *
         * @return
         * The prevPage
         */
        public String getPrevPage() {
            return prevPage;
        }

        /**
         *
         * @param prevPage
         * The prev_page
         */
        public void setPrevPage(String prevPage) {
            this.prevPage = prevPage;
        }

        /**
         *
         * @return
         * The nextPage
         */
        public String getNextPage() {
            return nextPage;
        }

        /**
         *
         * @param nextPage
         * The next_page
         */
        public void setNextPage(String nextPage) {
            this.nextPage = nextPage;
        }

        /**
         *
         * @return
         * The success
         */
        public String getSuccess() {
            return success;
        }

        /**
         *
         * @param success
         * The success
         */
        public void setSuccess(String success) {
            this.success = success;
        }

        /**
         *
         * @return
         * The message
         */
        public String getMessage() {
            return message;
        }

        /**
         *
         * @param message
         * The message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         *
         * @return
         * The fields
         */
        public List<String> getFields() {
            return fields;
        }

        /**
         *
         * @param fields
         * The fields
         */
        public void setFields(List<String> fields) {
            this.fields = fields;
        }

    }

}