package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 6/8/16.
 */
public class DiscountCouponApplyModel implements WSResponse, Serializable {

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

    public class Calculation implements Serializable  {

        @SerializedName("actual_payment")
        @Expose
        private String actualPayment;
        @SerializedName("down_payment")
        @Expose
        private String downPayment;
        @SerializedName("property_owner_earn")
        @Expose
        private String propertyOwnerEarn;
        @SerializedName("service_price")
        @Expose
        private String servicePrice;
        @SerializedName("amount_to_pay")
        @Expose
        private String amountToPay;
        @SerializedName("discount_price")
        @Expose
        private String discountPrice;

        /**
         * @return The actualPayment
         */
        public String getActualPayment() {
            return actualPayment;
        }

        /**
         * @param actualPayment The actual_payment
         */
        public void setActualPayment(String actualPayment) {
            this.actualPayment = actualPayment;
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
         * @return The propertyOwnerEarn
         */
        public String getPropertyOwnerEarn() {
            return propertyOwnerEarn;
        }

        /**
         * @param propertyOwnerEarn The property_owner_earn
         */
        public void setPropertyOwnerEarn(String propertyOwnerEarn) {
            this.propertyOwnerEarn = propertyOwnerEarn;
        }

        /**
         * @return The servicePrice
         */
        public String getServicePrice() {
            return servicePrice;
        }

        /**
         * @param servicePrice The service_price
         */
        public void setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
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

    }

    public class CheckCouponCode implements Serializable{

        @SerializedName("p_promocode_id")
        @Expose
        private String pPromocodeId;
        @SerializedName("p_coupon_type")
        @Expose
        private String pCouponType;
        @SerializedName("p_coupon_value")
        @Expose
        private String pCouponValue;
        @SerializedName("p_property_owner_id")
        @Expose
        private String pPropertyOwnerId;

        /**
         * @return The pPromocodeId
         */
        public String getPPromocodeId() {
            return pPromocodeId;
        }

        /**
         * @param pPromocodeId The p_promocode_id
         */
        public void setPPromocodeId(String pPromocodeId) {
            this.pPromocodeId = pPromocodeId;
        }

        /**
         * @return The pCouponType
         */
        public String getPCouponType() {
            return pCouponType;
        }

        /**
         * @param pCouponType The p_coupon_type
         */
        public void setPCouponType(String pCouponType) {
            this.pCouponType = pCouponType;
        }

        /**
         * @return The pCouponValue
         */
        public String getPCouponValue() {
            return pCouponValue;
        }

        /**
         * @param pCouponValue The p_coupon_value
         */
        public void setPCouponValue(String pCouponValue) {
            this.pCouponValue = pCouponValue;
        }

        /**
         * @return The pPropertyOwnerId
         */
        public String getPPropertyOwnerId() {
            return pPropertyOwnerId;
        }

        /**
         * @param pPropertyOwnerId The p_property_owner_id
         */
        public void setPPropertyOwnerId(String pPropertyOwnerId) {
            this.pPropertyOwnerId = pPropertyOwnerId;
        }

    }

    public class Data implements Serializable{

        @SerializedName("check_coupon_code")
        @Expose
        private List<CheckCouponCode> checkCouponCode = new ArrayList<CheckCouponCode>();
        @SerializedName("calculation")
        @Expose
        private List<Calculation> calculation = new ArrayList<Calculation>();

        /**
         * @return The checkCouponCode
         */
        public List<CheckCouponCode> getCheckCouponCode() {
            return checkCouponCode;
        }

        /**
         * @param checkCouponCode The check_coupon_code
         */
        public void setCheckCouponCode(List<CheckCouponCode> checkCouponCode) {
            this.checkCouponCode = checkCouponCode;
        }

        /**
         * @return The calculation
         */
        public List<Calculation> getCalculation() {
            return calculation;
        }

        /**
         * @param calculation The calculation
         */
        public void setCalculation(List<Calculation> calculation) {
            this.calculation = calculation;
        }

    }

    public class Settings implements Serializable{

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