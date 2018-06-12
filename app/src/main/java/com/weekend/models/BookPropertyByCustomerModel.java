package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 13/8/16.
 */
public class BookPropertyByCustomerModel implements WSResponse, Serializable {

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

    public class Data {

        @SerializedName("check_user_token_for_api")
        @Expose
        private List<CheckUserTokenForApus> checkUserTokenForApi = new ArrayList<CheckUserTokenForApus>();
        @SerializedName("get_property_details")
        @Expose
        private List<GetPropertyDetail> getPropertyDetails = new ArrayList<GetPropertyDetail>();
        @SerializedName("check_available_booking_slot")
        @Expose
        private CheckAvailableBookingSlot checkAvailableBookingSlot;
        @SerializedName("check_chalet_price")
        @Expose
        private CheckChaletPrice checkChaletPrice;
        @SerializedName("decodepriceforbooking")
        @Expose
        private Decodepriceforbooking decodepriceforbooking;
        @SerializedName("get_refund_policy")
        @Expose
        private List<GetRefundPolicy> getRefundPolicy = new ArrayList<GetRefundPolicy>();
        @SerializedName("update_book_id")
        @Expose
        private List<Object> updateBookId = new ArrayList<Object>();
        @SerializedName("insertbookingdates")
        @Expose
        private List<Object> insertbookingdates = new ArrayList<Object>();
        @SerializedName("getorderid")
        @Expose
        private Getorderid getorderid;

        /**
         * @return The checkUserTokenForApi
         */
        public List<CheckUserTokenForApus> getCheckUserTokenForApi() {
            return checkUserTokenForApi;
        }

        /**
         * @param checkUserTokenForApi The check_user_token_for_api
         */
        public void setCheckUserTokenForApi(List<CheckUserTokenForApus> checkUserTokenForApi) {
            this.checkUserTokenForApi = checkUserTokenForApi;
        }

        /**
         * @return The getPropertyDetails
         */
        public List<GetPropertyDetail> getGetPropertyDetails() {
            return getPropertyDetails;
        }

        /**
         * @param getPropertyDetails The get_property_details
         */
        public void setGetPropertyDetails(List<GetPropertyDetail> getPropertyDetails) {
            this.getPropertyDetails = getPropertyDetails;
        }

        /**
         * @return The checkAvailableBookingSlot
         */
        public CheckAvailableBookingSlot getCheckAvailableBookingSlot() {
            return checkAvailableBookingSlot;
        }

        /**
         * @param checkAvailableBookingSlot The check_available_booking_slot
         */
        public void setCheckAvailableBookingSlot(CheckAvailableBookingSlot checkAvailableBookingSlot) {
            this.checkAvailableBookingSlot = checkAvailableBookingSlot;
        }

        /**
         * @return The checkChaletPrice
         */
        public CheckChaletPrice getCheckChaletPrice() {
            return checkChaletPrice;
        }

        /**
         * @param checkChaletPrice The check_chalet_price
         */
        public void setCheckChaletPrice(CheckChaletPrice checkChaletPrice) {
            this.checkChaletPrice = checkChaletPrice;
        }

        /**
         * @return The decodepriceforbooking
         */
        public Decodepriceforbooking getDecodepriceforbooking() {
            return decodepriceforbooking;
        }

        /**
         * @param decodepriceforbooking The decodepriceforbooking
         */
        public void setDecodepriceforbooking(Decodepriceforbooking decodepriceforbooking) {
            this.decodepriceforbooking = decodepriceforbooking;
        }

        /**
         * @return The getRefundPolicy
         */
        public List<GetRefundPolicy> getGetRefundPolicy() {
            return getRefundPolicy;
        }

        /**
         * @param getRefundPolicy The get_refund_policy
         */
        public void setGetRefundPolicy(List<GetRefundPolicy> getRefundPolicy) {
            this.getRefundPolicy = getRefundPolicy;
        }

        /**
         * @return The updateBookId
         */
        public List<Object> getUpdateBookId() {
            return updateBookId;
        }

        /**
         * @param updateBookId The update_book_id
         */
        public void setUpdateBookId(List<Object> updateBookId) {
            this.updateBookId = updateBookId;
        }

        /**
         * @return The insertbookingdates
         */
        public List<Object> getInsertbookingdates() {
            return insertbookingdates;
        }

        /**
         * @param insertbookingdates The insertbookingdates
         */
        public void setInsertbookingdates(List<Object> insertbookingdates) {
            this.insertbookingdates = insertbookingdates;
        }

        /**
         * @return The getorderid
         */
        public Getorderid getGetorderid() {
            return getorderid;
        }

        /**
         * @param getorderid The getorderid
         */
        public void setGetorderid(Getorderid getorderid) {
            this.getorderid = getorderid;
        }

    }

    public class CheckAvailableBookingSlot {

        @SerializedName("booked_slots")
        @Expose
        private List<Object> bookedSlots = new ArrayList<Object>();
        @SerializedName("property_slot_times_pk")
        @Expose
        private String propertySlotTimesPk;
        @SerializedName("not_available_pk")
        @Expose
        private List<Object> notAvailablePk = new ArrayList<Object>();

        /**
         * @return The bookedSlots
         */
        public List<Object> getBookedSlots() {
            return bookedSlots;
        }

        /**
         * @param bookedSlots The booked_slots
         */
        public void setBookedSlots(List<Object> bookedSlots) {
            this.bookedSlots = bookedSlots;
        }

        /**
         * @return The propertySlotTimesPk
         */
        public String getPropertySlotTimesPk() {
            return propertySlotTimesPk;
        }

        /**
         * @param propertySlotTimesPk The property_slot_times_pk
         */
        public void setPropertySlotTimesPk(String propertySlotTimesPk) {
            this.propertySlotTimesPk = propertySlotTimesPk;
        }

        /**
         * @return The notAvailablePk
         */
        public List<Object> getNotAvailablePk() {
            return notAvailablePk;
        }

        /**
         * @param notAvailablePk The not_available_pk
         */
        public void setNotAvailablePk(List<Object> notAvailablePk) {
            this.notAvailablePk = notAvailablePk;
        }

    }

    public class CheckChaletPrice {

        @SerializedName("count")
        @Expose
        private String count;
        @SerializedName("bookingcount")
        @Expose
        private String bookingcount;
        @SerializedName("success")
        @Expose
        private String success;
        @SerializedName("downprice")
        @Expose
        private String downprice;
        @SerializedName("bookingprice")
        @Expose
        private String bookingprice;
        @SerializedName("servicePrice")
        @Expose
        private String servicePrice;

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
         * @return The bookingcount
         */
        public String getBookingcount() {
            return bookingcount;
        }

        /**
         * @param bookingcount The bookingcount
         */
        public void setBookingcount(String bookingcount) {
            this.bookingcount = bookingcount;
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
         * @return The downprice
         */
        public String getDownprice() {
            return downprice;
        }

        /**
         * @param downprice The downprice
         */
        public void setDownprice(String downprice) {
            this.downprice = downprice;
        }

        /**
         * @return The bookingprice
         */
        public String getBookingprice() {
            return bookingprice;
        }

        /**
         * @param bookingprice The bookingprice
         */
        public void setBookingprice(String bookingprice) {
            this.bookingprice = bookingprice;
        }

        /**
         * @return The servicePrice
         */
        public String getServicePrice() {
            return servicePrice;
        }

        /**
         * @param servicePrice The servicePrice
         */
        public void setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
        }

    }

    public class CheckUserTokenForApus {

        @SerializedName("u_user_id")
        @Expose
        private String uUserId;

        /**
         * @return The uUserId
         */
        public String getUUserId() {
            return uUserId;
        }

        /**
         * @param uUserId The u_user_id
         */
        public void setUUserId(String uUserId) {
            this.uUserId = uUserId;
        }

    }

    public class Decodepriceforbooking {

        @SerializedName("downPayment")
        @Expose
        private String downPayment;
        @SerializedName("orderSubTotal")
        @Expose
        private String orderSubTotal;
        @SerializedName("adminCommisionPrice")
        @Expose
        private String adminCommisionPrice;
        @SerializedName("discountPrice")
        @Expose
        private String discountPrice;
        @SerializedName("orderTotal")
        @Expose
        private String orderTotal;
        @SerializedName("orderTotalAfterCommision")
        @Expose
        private String orderTotalAfterCommision;

        /**
         * @return The downPayment
         */
        public String getDownPayment() {
            return downPayment;
        }

        /**
         * @param downPayment The downPayment
         */
        public void setDownPayment(String downPayment) {
            this.downPayment = downPayment;
        }

        /**
         * @return The orderSubTotal
         */
        public String getOrderSubTotal() {
            return orderSubTotal;
        }

        /**
         * @param orderSubTotal The orderSubTotal
         */
        public void setOrderSubTotal(String orderSubTotal) {
            this.orderSubTotal = orderSubTotal;
        }

        /**
         * @return The adminCommisionPrice
         */
        public String getAdminCommisionPrice() {
            return adminCommisionPrice;
        }

        /**
         * @param adminCommisionPrice The adminCommisionPrice
         */
        public void setAdminCommisionPrice(String adminCommisionPrice) {
            this.adminCommisionPrice = adminCommisionPrice;
        }

        /**
         * @return The discountPrice
         */
        public String getDiscountPrice() {
            return discountPrice;
        }

        /**
         * @param discountPrice The discountPrice
         */
        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        /**
         * @return The orderTotal
         */
        public String getOrderTotal() {
            return orderTotal;
        }

        /**
         * @param orderTotal The orderTotal
         */
        public void setOrderTotal(String orderTotal) {
            this.orderTotal = orderTotal;
        }

        /**
         * @return The orderTotalAfterCommision
         */
        public String getOrderTotalAfterCommision() {
            return orderTotalAfterCommision;
        }

        /**
         * @param orderTotalAfterCommision The orderTotalAfterCommision
         */
        public void setOrderTotalAfterCommision(String orderTotalAfterCommision) {
            this.orderTotalAfterCommision = orderTotalAfterCommision;
        }

    }

    public class GetPropertyDetail {

        @SerializedName("chalet_id")
        @Expose
        private String chaletId;
        @SerializedName("property_id")
        @Expose
        private String propertyId;
        @SerializedName("chalet_name")
        @Expose
        private String chaletName;
        @SerializedName("property_type_id")
        @Expose
        private String propertyTypeId;
        @SerializedName("property_title")
        @Expose
        private String propertyTitle;
        @SerializedName("propery_mobile_number1")
        @Expose
        private String properyMobileNumber1;
        @SerializedName("propery_mobile_number2")
        @Expose
        private String properyMobileNumber2;
        @SerializedName("propery_email")
        @Expose
        private String properyEmail;
        @SerializedName("pm_address")
        @Expose
        private String pmAddress;
        @SerializedName("pm_city_id")
        @Expose
        private String pmCityId;
        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("propery_phone_no")
        @Expose
        private String properyPhoneNo;
        @SerializedName("pc_chalet_leasing_type_id")
        @Expose
        private String pcChaletLeasingTypeId;
        @SerializedName("pm_property_id")
        @Expose
        private String pmPropertyId;
        @SerializedName("pm_property_owner_id")
        @Expose
        private String pmPropertyOwnerId;
        @SerializedName("mcl_city_id")
        @Expose
        private String mclCityId;
        @SerializedName("pc_down_payment")
        @Expose
        private String pcDownPayment;
        @SerializedName("pm_refund_policy_master_id")
        @Expose
        private String pmRefundPolicyMasterId;
        @SerializedName("pm_check_in_time")
        @Expose
        private String pmCheckInTime;
        @SerializedName("pm_check_out_time")
        @Expose
        private String pmCheckOutTime;
        @SerializedName("pm_refulnd_policy")
        @Expose
        private String pmRefulndPolicy;
        @SerializedName("pm_cancel_description")
        @Expose
        private String pmCancelDescription;
        @SerializedName("pm_cancel_before")
        @Expose
        private String pmCancelBefore;
        @SerializedName("pm_cancel_before_type")
        @Expose
        private String pmCancelBeforeType;
        @SerializedName("pm_booking_service_id")
        @Expose
        private String pmBookingServiceId;

        /**
         * @return The chaletId
         */
        public String getChaletId() {
            return chaletId;
        }

        /**
         * @param chaletId The chalet_id
         */
        public void setChaletId(String chaletId) {
            this.chaletId = chaletId;
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
         * @return The propertyTypeId
         */
        public String getPropertyTypeId() {
            return propertyTypeId;
        }

        /**
         * @param propertyTypeId The property_type_id
         */
        public void setPropertyTypeId(String propertyTypeId) {
            this.propertyTypeId = propertyTypeId;
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
         * @return The properyMobileNumber1
         */
        public String getProperyMobileNumber1() {
            return properyMobileNumber1;
        }

        /**
         * @param properyMobileNumber1 The propery_mobile_number1
         */
        public void setProperyMobileNumber1(String properyMobileNumber1) {
            this.properyMobileNumber1 = properyMobileNumber1;
        }

        /**
         * @return The properyMobileNumber2
         */
        public String getProperyMobileNumber2() {
            return properyMobileNumber2;
        }

        /**
         * @param properyMobileNumber2 The propery_mobile_number2
         */
        public void setProperyMobileNumber2(String properyMobileNumber2) {
            this.properyMobileNumber2 = properyMobileNumber2;
        }

        /**
         * @return The properyEmail
         */
        public String getProperyEmail() {
            return properyEmail;
        }

        /**
         * @param properyEmail The propery_email
         */
        public void setProperyEmail(String properyEmail) {
            this.properyEmail = properyEmail;
        }

        /**
         * @return The pmAddress
         */
        public String getPmAddress() {
            return pmAddress;
        }

        /**
         * @param pmAddress The pm_address
         */
        public void setPmAddress(String pmAddress) {
            this.pmAddress = pmAddress;
        }

        /**
         * @return The pmCityId
         */
        public String getPmCityId() {
            return pmCityId;
        }

        /**
         * @param pmCityId The pm_city_id
         */
        public void setPmCityId(String pmCityId) {
            this.pmCityId = pmCityId;
        }

        /**
         * @return The locationId
         */
        public String getLocationId() {
            return locationId;
        }

        /**
         * @param locationId The location_id
         */
        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        /**
         * @return The properyPhoneNo
         */
        public String getProperyPhoneNo() {
            return properyPhoneNo;
        }

        /**
         * @param properyPhoneNo The propery_phone_no
         */
        public void setProperyPhoneNo(String properyPhoneNo) {
            this.properyPhoneNo = properyPhoneNo;
        }

        /**
         * @return The pcChaletLeasingTypeId
         */
        public String getPcChaletLeasingTypeId() {
            return pcChaletLeasingTypeId;
        }

        /**
         * @param pcChaletLeasingTypeId The pc_chalet_leasing_type_id
         */
        public void setPcChaletLeasingTypeId(String pcChaletLeasingTypeId) {
            this.pcChaletLeasingTypeId = pcChaletLeasingTypeId;
        }

        /**
         * @return The pmPropertyId
         */
        public String getPmPropertyId() {
            return pmPropertyId;
        }

        /**
         * @param pmPropertyId The pm_property_id
         */
        public void setPmPropertyId(String pmPropertyId) {
            this.pmPropertyId = pmPropertyId;
        }

        /**
         * @return The pmPropertyOwnerId
         */
        public String getPmPropertyOwnerId() {
            return pmPropertyOwnerId;
        }

        /**
         * @param pmPropertyOwnerId The pm_property_owner_id
         */
        public void setPmPropertyOwnerId(String pmPropertyOwnerId) {
            this.pmPropertyOwnerId = pmPropertyOwnerId;
        }

        /**
         * @return The mclCityId
         */
        public String getMclCityId() {
            return mclCityId;
        }

        /**
         * @param mclCityId The mcl_city_id
         */
        public void setMclCityId(String mclCityId) {
            this.mclCityId = mclCityId;
        }

        /**
         * @return The pcDownPayment
         */
        public String getPcDownPayment() {
            return pcDownPayment;
        }

        /**
         * @param pcDownPayment The pc_down_payment
         */
        public void setPcDownPayment(String pcDownPayment) {
            this.pcDownPayment = pcDownPayment;
        }

        /**
         * @return The pmRefundPolicyMasterId
         */
        public String getPmRefundPolicyMasterId() {
            return pmRefundPolicyMasterId;
        }

        /**
         * @param pmRefundPolicyMasterId The pm_refund_policy_master_id
         */
        public void setPmRefundPolicyMasterId(String pmRefundPolicyMasterId) {
            this.pmRefundPolicyMasterId = pmRefundPolicyMasterId;
        }

        /**
         * @return The pmCheckInTime
         */
        public String getPmCheckInTime() {
            return pmCheckInTime;
        }

        /**
         * @param pmCheckInTime The pm_check_in_time
         */
        public void setPmCheckInTime(String pmCheckInTime) {
            this.pmCheckInTime = pmCheckInTime;
        }

        /**
         * @return The pmCheckOutTime
         */
        public String getPmCheckOutTime() {
            return pmCheckOutTime;
        }

        /**
         * @param pmCheckOutTime The pm_check_out_time
         */
        public void setPmCheckOutTime(String pmCheckOutTime) {
            this.pmCheckOutTime = pmCheckOutTime;
        }

        /**
         * @return The pmRefulndPolicy
         */
        public String getPmRefulndPolicy() {
            return pmRefulndPolicy;
        }

        /**
         * @param pmRefulndPolicy The pm_refulnd_policy
         */
        public void setPmRefulndPolicy(String pmRefulndPolicy) {
            this.pmRefulndPolicy = pmRefulndPolicy;
        }

        /**
         * @return The pmCancelDescription
         */
        public String getPmCancelDescription() {
            return pmCancelDescription;
        }

        /**
         * @param pmCancelDescription The pm_cancel_description
         */
        public void setPmCancelDescription(String pmCancelDescription) {
            this.pmCancelDescription = pmCancelDescription;
        }

        /**
         * @return The pmCancelBefore
         */
        public String getPmCancelBefore() {
            return pmCancelBefore;
        }

        /**
         * @param pmCancelBefore The pm_cancel_before
         */
        public void setPmCancelBefore(String pmCancelBefore) {
            this.pmCancelBefore = pmCancelBefore;
        }

        /**
         * @return The pmCancelBeforeType
         */
        public String getPmCancelBeforeType() {
            return pmCancelBeforeType;
        }

        /**
         * @param pmCancelBeforeType The pm_cancel_before_type
         */
        public void setPmCancelBeforeType(String pmCancelBeforeType) {
            this.pmCancelBeforeType = pmCancelBeforeType;
        }

        /**
         * @return The pmBookingServiceId
         */
        public String getPmBookingServiceId() {
            return pmBookingServiceId;
        }

        /**
         * @param pmBookingServiceId The pm_booking_service_id
         */
        public void setPmBookingServiceId(String pmBookingServiceId) {
            this.pmBookingServiceId = pmBookingServiceId;
        }

    }

    public class GetRefundPolicy {

        @SerializedName("rpm_policy_type1")
        @Expose
        private String rpmPolicyType1;
        @SerializedName("rpm_policy_type_ar")
        @Expose
        private String rpmPolicyTypeAr;
        @SerializedName("rpm_description_ar")
        @Expose
        private String rpmDescriptionAr;
        @SerializedName("rpm_cancel_before")
        @Expose
        private String rpmCancelBefore;
        @SerializedName("rpm_cancel_before_type")
        @Expose
        private String rpmCancelBeforeType;
        @SerializedName("rpm_policy_type")
        @Expose
        private String rpmPolicyType;
        @SerializedName("rpm_description")
        @Expose
        private String rpmDescription;

        /**
         * @return The rpmPolicyType1
         */
        public String getRpmPolicyType1() {
            return rpmPolicyType1;
        }

        /**
         * @param rpmPolicyType1 The rpm_policy_type1
         */
        public void setRpmPolicyType1(String rpmPolicyType1) {
            this.rpmPolicyType1 = rpmPolicyType1;
        }

        /**
         * @return The rpmPolicyTypeAr
         */
        public String getRpmPolicyTypeAr() {
            return rpmPolicyTypeAr;
        }

        /**
         * @param rpmPolicyTypeAr The rpm_policy_type_ar
         */
        public void setRpmPolicyTypeAr(String rpmPolicyTypeAr) {
            this.rpmPolicyTypeAr = rpmPolicyTypeAr;
        }

        /**
         * @return The rpmDescriptionAr
         */
        public String getRpmDescriptionAr() {
            return rpmDescriptionAr;
        }

        /**
         * @param rpmDescriptionAr The rpm_description_ar
         */
        public void setRpmDescriptionAr(String rpmDescriptionAr) {
            this.rpmDescriptionAr = rpmDescriptionAr;
        }

        /**
         * @return The rpmCancelBefore
         */
        public String getRpmCancelBefore() {
            return rpmCancelBefore;
        }

        /**
         * @param rpmCancelBefore The rpm_cancel_before
         */
        public void setRpmCancelBefore(String rpmCancelBefore) {
            this.rpmCancelBefore = rpmCancelBefore;
        }

        /**
         * @return The rpmCancelBeforeType
         */
        public String getRpmCancelBeforeType() {
            return rpmCancelBeforeType;
        }

        /**
         * @param rpmCancelBeforeType The rpm_cancel_before_type
         */
        public void setRpmCancelBeforeType(String rpmCancelBeforeType) {
            this.rpmCancelBeforeType = rpmCancelBeforeType;
        }

        /**
         * @return The rpmPolicyType
         */
        public String getRpmPolicyType() {
            return rpmPolicyType;
        }

        /**
         * @param rpmPolicyType The rpm_policy_type
         */
        public void setRpmPolicyType(String rpmPolicyType) {
            this.rpmPolicyType = rpmPolicyType;
        }

        /**
         * @return The rpmDescription
         */
        public String getRpmDescription() {
            return rpmDescription;
        }

        /**
         * @param rpmDescription The rpm_description
         */
        public void setRpmDescription(String rpmDescription) {
            this.rpmDescription = rpmDescription;
        }

    }

    public class Getorderid {

        @SerializedName("orderId")
        @Expose
        private String orderId;

        /**
         * @return The orderId
         */
        public String getOrderId() {
            return orderId;
        }

        /**
         * @param orderId The orderId
         */
        public void setOrderId(String orderId) {
            this.orderId = orderId;
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