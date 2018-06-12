package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetPropertyPriceModel implements WSResponse, Serializable {

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

        @SerializedName("down_payment")
        @Expose
        private String downPayment;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("service_charge")
        @Expose
        private String serviceCharge;
        @SerializedName("amount_to_pay")
        @Expose
        private String amountToPay;
        @SerializedName("day")
        @Expose
        private String day;
        @SerializedName("remaining_amount")
        @Expose
        private String remainingAmount;

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
         * @return The price
         */
        public String getPrice() {
            return price;
        }

        /**
         * @param price The price
         */
        public void setPrice(String price) {
            this.price = price;
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
         * @return The amountToPay
         */
        public String getAmountToPay() {
            return amountToPay;
        }

        /**
         * @param amountToPay The amount_to_pay
         */
        public void setAmountToPay(String amountToPay) {
            this.amountToPay = amountToPay;
        }

        /**
         * @return The day
         */
        public String getDay() {
            return day;
        }

        /**
         * @param day The day
         */
        public void setDay(String day) {
            this.day = day;
        }

        /**
         * @return The remainingAmount
         */
        public String getRemainingAmount() {
            return remainingAmount;
        }

        /**
         * @param remainingAmount The remaining_amount
         */
        public void setRemainingAmount(String remainingAmount) {
            this.remainingAmount = remainingAmount;
        }

    }

}