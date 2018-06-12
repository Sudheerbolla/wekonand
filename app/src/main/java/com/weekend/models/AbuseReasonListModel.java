package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AbuseReasonListModel implements WSResponse, Serializable {

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


    public static class Datum {

        @SerializedName("abuse_reason_master_id")
        @Expose
        private String abuseReasonMasterId;
        @SerializedName("reason_title")
        @Expose
        private String reasonTitle;
        @SerializedName("lang_code")
        @Expose
        private String langCode;

        /**
         * @return The abuseReasonMasterId
         */
        public String getAbuseReasonMasterId() {
            return abuseReasonMasterId;
        }

        /**
         * @param abuseReasonMasterId The abuse_reason_master_id
         */
        public void setAbuseReasonMasterId(String abuseReasonMasterId) {
            this.abuseReasonMasterId = abuseReasonMasterId;
        }

        /**
         * @return The reasonTitle
         */
        public String getReasonTitle() {
            return reasonTitle;
        }

        /**
         * @param reasonTitle The reason_title
         */
        public void setReasonTitle(String reasonTitle) {
            this.reasonTitle = reasonTitle;
        }

        /**
         * @return The langCode
         */
        public String getLangCode() {
            return langCode;
        }

        /**
         * @param langCode The lang_code
         */
        public void setLangCode(String langCode) {
            this.langCode = langCode;
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