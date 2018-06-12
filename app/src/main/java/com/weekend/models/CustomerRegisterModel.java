package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerRegisterModel implements WSResponse, Serializable {

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

    public class Datum {

        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("push_notification")
        @Expose
        private String pushNotification;
        @SerializedName("auto_login")
        @Expose
        private String autoLogin;

        /**
         * @return The otp
         */
        public String getOtp() {
            return otp;
        }

        /**
         * @param otp The otp
         */
        public void setOtp(String otp) {
            this.otp = otp;
        }

        /**
         * @return The token
         */
        public String getToken() {
            return token;
        }

        /**
         * @param token The token
         */
        public void setToken(String token) {
            this.token = token;
        }

        /**
         * @return The pushNotification
         */
        public String getPushNotification() {
            return pushNotification;
        }

        /**
         * @param pushNotification The push_notification
         */
        public void setPushNotification(String pushNotification) {
            this.pushNotification = pushNotification;
        }

        /**
         * @return The autoLogin
         */
        public String getAutoLogin() {
            return autoLogin;
        }

        /**
         * @param autoLogin The auto_login
         */
        public void setAutoLogin(String autoLogin) {
            this.autoLogin = autoLogin;
        }

    }
}