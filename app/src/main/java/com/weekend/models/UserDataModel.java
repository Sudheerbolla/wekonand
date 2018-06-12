package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDataModel implements WSResponse, Serializable {

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

        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("email_id")
        @Expose
        private String emailId;
        @SerializedName("user_type")
        @Expose
        private String userType;
        @SerializedName("mobile_code")
        @Expose
        private String mobileCode;
        @SerializedName("mobile_no")
        @Expose
        private String mobileNo;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("auto_login")
        @Expose
        private String autoLogin;
        @SerializedName("push_notification")
        @Expose
        private String pushNotification;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("gender")
        @Expose
        private String gender;

        /**
         * @return The firstName
         */
        public String getFirstName() {
            return firstName;
        }

        /**
         * @param firstName The first_name
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        /**
         * @return The lastName
         */
        public String getLastName() {
            return lastName;
        }

        /**
         * @param lastName The last_name
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        /**
         * @return The emailId
         */
        public String getEmailId() {
            return emailId;
        }

        /**
         * @param emailId The email_id
         */
        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        /**
         * @return The userType
         */
        public String getUserType() {
            return userType;
        }

        /**
         * @param userType The user_type
         */
        public void setUserType(String userType) {
            this.userType = userType;
        }

        /**
         * @return The mobileCode
         */
        public String getMobileCode() {
            return mobileCode;
        }

        /**
         * @param mobileCode The mobile_code
         */
        public void setMobileCode(String mobileCode) {
            this.mobileCode = mobileCode;
        }

        /**
         * @return The mobileNo
         */
        public String getMobileNo() {
            return mobileNo;
        }

        /**
         * @param mobileNo The mobile_no
         */
        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }

        /**
         * @return The countryId
         */
        public String getCountryId() {
            return countryId;
        }

        /**
         * @param countryId The country_id
         */
        public void setCountryId(String countryId) {
            this.countryId = countryId;
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
         * @return The cityId
         */
        public String getCityId() {
            return cityId;
        }

        /**
         * @param cityId The city_id
         */
        public void setCityId(String cityId) {
            this.cityId = cityId;
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

        /**
         * @return The status
         */
        public String getStatus() {
            return status;
        }

        /**
         * @param status The status
         */
        public void setStatus(String status) {
            this.status = status;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }
}