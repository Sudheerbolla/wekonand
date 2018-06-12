package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageSchedulesModel implements WSResponse, Serializable{

    @SerializedName("settings")
    @Expose
    private Settings settings;
    @SerializedName("data")
    @Expose
    private Data data;

    public ManageSchedulesModel() {
    }

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

    public class Data implements Serializable{

        @SerializedName("slot_times")
        @Expose
        private List<SlotTime> slotTimes = new ArrayList<SlotTime>();
        @SerializedName("price_list_chalet_soccer")
        @Expose
        private List<PriceListChaletSoccer> priceListChaletSoccer = new ArrayList<PriceListChaletSoccer>();
        @SerializedName("schdule_details")
        @Expose
        private List<SchduleDetail> schduleDetails = new ArrayList<SchduleDetail>();

        /**
         * @return The slotTimes
         */
        public List<SlotTime> getSlotTimes() {
            return slotTimes;
        }

        /**
         * @param slotTimes The slot_times
         */
        public void setSlotTimes(List<SlotTime> slotTimes) {
            this.slotTimes = slotTimes;
        }

        /**
         * @return The priceListChaletSoccer
         */
        public List<PriceListChaletSoccer> getPriceListChaletSoccer() {
            return priceListChaletSoccer;
        }

        /**
         * @param priceListChaletSoccer The price_list_chalet_soccer
         */
        public void setPriceListChaletSoccer(List<PriceListChaletSoccer> priceListChaletSoccer) {
            this.priceListChaletSoccer = priceListChaletSoccer;
        }

        /**
         * @return The schduleDetails
         */
        public List<SchduleDetail> getSchduleDetails() {
            return schduleDetails;
        }

        /**
         * @param schduleDetails The schdule_details
         */
        public void setSchduleDetails(List<SchduleDetail> schduleDetails) {
            this.schduleDetails = schduleDetails;
        }

    }

    public class PriceListChaletSoccer implements Serializable{

        @SerializedName("down_payment")
        @Expose
        private String downPayment;
        @SerializedName("price_eid_ul_adha")
        @Expose
        private String priceEidUlAdha;
        @SerializedName("price_eid_al_fitr")
        @Expose
        private String priceEidAlFitr;
        @SerializedName("price_saturday")
        @Expose
        private String priceSaturday;
        @SerializedName("price_friday")
        @Expose
        private String priceFriday;
        @SerializedName("price_thursday")
        @Expose
        private String priceThursday;
        @SerializedName("price_sun_wed")
        @Expose
        private String priceSunWed;

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
         * @return The priceEidUlAdha
         */
        public String getPriceEidUlAdha() {
            return priceEidUlAdha;
        }

        /**
         * @param priceEidUlAdha The price_eid_ul_adha
         */
        public void setPriceEidUlAdha(String priceEidUlAdha) {
            this.priceEidUlAdha = priceEidUlAdha;
        }

        /**
         * @return The priceEidAlFitr
         */
        public String getPriceEidAlFitr() {
            return priceEidAlFitr;
        }

        /**
         * @param priceEidAlFitr The price_eid_al_fitr
         */
        public void setPriceEidAlFitr(String priceEidAlFitr) {
            this.priceEidAlFitr = priceEidAlFitr;
        }

        /**
         * @return The priceSaturday
         */
        public String getPriceSaturday() {
            return priceSaturday;
        }

        /**
         * @param priceSaturday The price_saturday
         */
        public void setPriceSaturday(String priceSaturday) {
            this.priceSaturday = priceSaturday;
        }

        /**
         * @return The priceFriday
         */
        public String getPriceFriday() {
            return priceFriday;
        }

        /**
         * @param priceFriday The price_friday
         */
        public void setPriceFriday(String priceFriday) {
            this.priceFriday = priceFriday;
        }

        /**
         * @return The priceThursday
         */
        public String getPriceThursday() {
            return priceThursday;
        }

        /**
         * @param priceThursday The price_thursday
         */
        public void setPriceThursday(String priceThursday) {
            this.priceThursday = priceThursday;
        }

        /**
         * @return The priceSunWed
         */
        public String getPriceSunWed() {
            return priceSunWed;
        }

        /**
         * @param priceSunWed The price_sun_wed
         */
        public void setPriceSunWed(String priceSunWed) {
            this.priceSunWed = priceSunWed;
        }

    }

    public class Schdule implements Serializable {

        @SerializedName("booked_status_id")
        @Expose
        private String bookedStatusId;
        @SerializedName("holiday_type")
        @Expose
        private String holidayType;
        @SerializedName("booked_date")
        @Expose
        private String bookedDate;
        @SerializedName("slot")
        @Expose
        private List<Slot> slot = new ArrayList<Slot>();
        @SerializedName("availability")
        @Expose
        private String availability;

        /**
         * @return The bookedStatusId
         */
        public String getBookedStatusId() {
            return bookedStatusId;
        }

        /**
         * @param bookedStatusId The booked_status_id
         */
        public void setBookedStatusId(String bookedStatusId) {
            this.bookedStatusId = bookedStatusId;
        }

        /**
         * @return The holidayType
         */
        public String getHolidayType() {
            return holidayType;
        }

        /**
         * @param holidayType The holiday_type
         */
        public void setHolidayType(String holidayType) {
            this.holidayType = holidayType;
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
         * @return The slot
         */
        public List<Slot> getSlot() {
            return slot;
        }

        /**
         * @param slot The slot
         */
        public void setSlot(List<Slot> slot) {
            this.slot = slot;
        }

        /**
         * @return The availability
         */
        public String getAvailability() {
            return availability;
        }

        /**
         * @param availability The availability
         */
        public void setAvailability(String availability) {
            this.availability = availability;
        }

    }

    public class SchduleDetail implements Serializable {

        @SerializedName("schdule")
        @Expose
        private List<Schdule> schdule = new ArrayList<Schdule>();

        /**
         * @return The schdule
         */
        public List<Schdule> getSchdule() {
            return schdule;
        }

        /**
         * @param schdule The schdule
         */
        public void setSchdule(List<Schdule> schdule) {
            this.schdule = schdule;
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

    public class SlotTime implements Serializable {

        @SerializedName("slot_times_id")
        @Expose
        private String slotTimesId;
        @SerializedName("slot_from_time")
        @Expose
        private String slotFromTime;
        @SerializedName("slot_to_time")
        @Expose
        private String slotToTime;

        public int status;
        public boolean isSelected;
        /**
         * @return The slotTimesId
         */
        public String getSlotTimesId() {
            return slotTimesId;
        }

        /**
         * @param slotTimesId The slot_times_id
         */
        public void setSlotTimesId(String slotTimesId) {
            this.slotTimesId = slotTimesId;
        }

        /**
         * @return The slotFromTime
         */
        public String getSlotFromTime() {
            return slotFromTime;
        }

        /**
         * @param slotFromTime The slot_from_time
         */
        public void setSlotFromTime(String slotFromTime) {
            this.slotFromTime = slotFromTime;
        }

        /**
         * @return The slotToTime
         */
        public String getSlotToTime() {
            return slotToTime;
        }

        /**
         * @param slotToTime The slot_to_time
         */
        public void setSlotToTime(String slotToTime) {
            this.slotToTime = slotToTime;
        }

    }

    public class Slot implements Serializable{

        @SerializedName("booked_status_id")
        @Expose
        private String bookedStatusId;
        @SerializedName("from_time")
        @Expose
        private String fromTime;
        @SerializedName("to_time")
        @Expose
        private String toTime;
        @SerializedName("order_id")
        @Expose
        private String orderId;

        /**
         * @return The bookedStatusId
         */
        public String getBookedStatusId() {
            return bookedStatusId;
        }

        /**
         * @param bookedStatusId The booked_status_id
         */
        public void setBookedStatusId(String bookedStatusId) {
            this.bookedStatusId = bookedStatusId;
        }

        /**
         * @return The fromTime
         */
        public String getFromTime() {
            return fromTime;
        }

        /**
         * @param fromTime The from_time
         */
        public void setFromTime(String fromTime) {
            this.fromTime = fromTime;
        }

        /**
         * @return The toTime
         */
        public String getToTime() {
            return toTime;
        }

        /**
         * @param toTime The to_time
         */
        public void setToTime(String toTime) {
            this.toTime = toTime;
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

    }
}