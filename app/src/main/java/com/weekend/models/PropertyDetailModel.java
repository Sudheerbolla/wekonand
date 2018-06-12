package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyDetailModel implements WSResponse, Serializable {

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

    public static class ChaletImage implements Serializable{

        @SerializedName("iPropertyChaletImagesId")
        @Expose
        private String iPropertyChaletImagesId;
        @SerializedName("vTitle")
        @Expose
        private String vTitle;
        @SerializedName("vImage")
        @Expose
        private String vImage;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        /**
         * @return The iPropertyChaletImagesId
         */
        public String getIPropertyChaletImagesId() {
            return iPropertyChaletImagesId;
        }

        /**
         * @param iPropertyChaletImagesId The iPropertyChaletImagesId
         */
        public void setIPropertyChaletImagesId(String iPropertyChaletImagesId) {
            this.iPropertyChaletImagesId = iPropertyChaletImagesId;
        }

        /**
         * @return The vTitle
         */
        public String getVTitle() {
            return vTitle;
        }

        /**
         * @param vTitle The vTitle
         */
        public void setVTitle(String vTitle) {
            this.vTitle = vTitle;
        }

        /**
         * @return The vImage
         */
        public String getVImage() {
            return vImage;
        }

        /**
         * @param vImage The vImage
         */
        public void setVImage(String vImage) {
            this.vImage = vImage;
        }

        /**
         * @return The imageUrl
         */
        public String getImageUrl() {
            return imageUrl;
        }

        /**
         * @param imageUrl The image_url
         */
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }

    public static class Data implements Serializable {

        @SerializedName("property_details")
        @Expose
        private List<PropertyDetail> propertyDetails = new ArrayList<PropertyDetail>();
        @SerializedName("property_images")
        @Expose
        private List<PropertyImage> propertyImages = new ArrayList<PropertyImage>();
        @SerializedName("property_chalet")
        @Expose
        private List<PropertyChalet> propertyChalet = new ArrayList<PropertyChalet>();
        @SerializedName("discount_code_info")
        @Expose
        private List<DiscountCodeInfo> discountCodeInfo = new ArrayList<DiscountCodeInfo>();

        /**
         * @return The propertyDetails
         */
        public List<PropertyDetail> getPropertyDetails() {
            return propertyDetails;
        }

        /**
         * @param propertyDetails The property_details
         */
        public void setPropertyDetails(List<PropertyDetail> propertyDetails) {
            this.propertyDetails = propertyDetails;
        }

        /**
         * @return The propertyImages
         */
        public List<PropertyImage> getPropertyImages() {
            return propertyImages;
        }

        /**
         * @param propertyImages The property_images
         */
        public void setPropertyImages(List<PropertyImage> propertyImages) {
            this.propertyImages = propertyImages;
        }

        /**
         * @return The propertyChalet
         */
        public List<PropertyChalet> getPropertyChalet() {
            return propertyChalet;
        }

        /**
         * @param propertyChalet The property_chalet
         */
        public void setPropertyChalet(List<PropertyChalet> propertyChalet) {
            this.propertyChalet = propertyChalet;
        }

        /**
         * @return The discountCodeInfo
         */
        public List<DiscountCodeInfo> getDiscountCodeInfo() {
            return discountCodeInfo;
        }

        /**
         * @param discountCodeInfo The discount_code_info
         */
        public void setDiscountCodeInfo(List<DiscountCodeInfo> discountCodeInfo) {
            this.discountCodeInfo = discountCodeInfo;
        }

    }

    public static class DiscountCodeInfo implements  Serializable{

        @SerializedName("discount_code")
        @Expose
        private String discountCode;
        @SerializedName("discount_price")
        @Expose
        private String discountPrice;
        @SerializedName("discount_type")
        @Expose
        private String discountType;
        @SerializedName("discount_start_date")
        @Expose
        private String discountStartDate;
        @SerializedName("discount_end_date")
        @Expose
        private String discountEndDate;

        /**
         * @return The discountCode
         */
        public String getDiscountCode() {
            return discountCode;
        }

        /**
         * @param discountCode The discount_code
         */
        public void setDiscountCode(String discountCode) {
            this.discountCode = discountCode;
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

        /**
         * @return The discountType
         */
        public String getDiscountType() {
            return discountType;
        }

        /**
         * @param discountType The discount_type
         */
        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        /**
         * @return The discountStartDate
         */
        public String getDiscountStartDate() {
            return discountStartDate;
        }

        /**
         * @param discountStartDate The discount_start_date
         */
        public void setDiscountStartDate(String discountStartDate) {
            this.discountStartDate = discountStartDate;
        }

        /**
         * @return The discountEndDate
         */
        public String getDiscountEndDate() {
            return discountEndDate;
        }

        /**
         * @param discountEndDate The discount_end_date
         */
        public void setDiscountEndDate(String discountEndDate) {
            this.discountEndDate = discountEndDate;
        }

    }

    public static class PropertyChalet implements Serializable{

        @SerializedName("property_chalet_details")
        @Expose
        private List<PropertyChaletDetail> propertyChaletDetails = new ArrayList<PropertyChaletDetail>();

        /**
         * @return The propertyChaletDetails
         */
        public List<PropertyChaletDetail> getPropertyChaletDetails() {
            return propertyChaletDetails;
        }

        /**
         * @param propertyChaletDetails The property_chalet_details
         */
        public void setPropertyChaletDetails(List<PropertyChaletDetail> propertyChaletDetails) {
            this.propertyChaletDetails = propertyChaletDetails;
        }

    }

    public static class PropertyChaletDetail implements Serializable{

        @SerializedName("property_chalet_id")
        @Expose
        private String propertyChaletId;
        @SerializedName("chalet_title")
        @Expose
        private String chaletTitle;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("down_payment")
        @Expose
        private String downPayment;
        @SerializedName("leasing")
        @Expose
        private String leasing;
        @SerializedName("section")
        @Expose
        private String section;
        @SerializedName("kitchen")
        @Expose
        private String kitchen;
        @SerializedName("bathroom")
        @Expose
        private String bathroom;
        @SerializedName("bedroom")
        @Expose
        private String bedroom;
        @SerializedName("living_room_female")
        @Expose
        private String livingRoomFemale;
        @SerializedName("living_room_male")
        @Expose
        private String livingRoomMale;
        @SerializedName("extra_amenities")
        @Expose
        private String extraAmenities;
        @SerializedName("pc_price_thursday")
        @Expose
        private String pcPriceThursday;
        @SerializedName("pc_price_friday")
        @Expose
        private String pcPriceFriday;
        @SerializedName("pc_price_saturday")
        @Expose
        private String pcPriceSaturday;
        @SerializedName("pc_price_eid_al_fitr")
        @Expose
        private String pcPriceEidAlFitr;
        @SerializedName("pc_price_eid_ul_adha")
        @Expose
        private String pcPriceEidUlAdha;
        @SerializedName("pc_price_sun_wed")
        @Expose
        private String pcPriceSunWed;
        @SerializedName("price_starts_from")
        @Expose
        private String priceStartsFrom;
        @SerializedName("amenities")
        @Expose
        private List<Amenity> amenities = new ArrayList<>();
        @SerializedName("suitable")
        @Expose
        private List<Suitable> suitable = new ArrayList<>();
        @SerializedName("latest_avaliablity")
        @Expose
        private String latestAvaliablity;
        @SerializedName("chalet_images")
        @Expose
        private List<ChaletImage> chaletImages = new ArrayList<>();

        @SerializedName("pc_video")
        @Expose
        private String youtube;


        public String leasingType, sectionType;

        /**
         * @return The propertyChaletId
         */
        public String getPropertyChaletId() {
            return propertyChaletId;
        }

        /**
         * @param propertyChaletId The property_chalet_id
         */
        public void setPropertyChaletId(String propertyChaletId) {
            this.propertyChaletId = propertyChaletId;
        }

        /**
         * @return The chaletTitle
         */
        public String getChaletTitle() {
            return chaletTitle;
        }

        /**
         * @param chaletTitle The chalet_title
         */
        public void setChaletTitle(String chaletTitle) {
            this.chaletTitle = chaletTitle;
        }

        /**
         * @return The size
         */
        public String getSize() {
            return size;
        }

        /**
         * @param size The size
         */
        public void setSize(String size) {
            this.size = size;
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
         * @return The leasing
         */
        public String getLeasing() {
            return leasing;
        }

        /**
         * @param leasing The leasing
         */
        public void setLeasing(String leasing) {
            this.leasing = leasing;
        }

        /**
         * @return The section
         */
        public String getSection() {
            return section;
        }

        /**
         * @param section The section
         */
        public void setSection(String section) {
            this.section = section;
        }

        /**
         * @return The kitchen
         */
        public String getKitchen() {
            return kitchen;
        }

        /**
         * @param kitchen The kitchen
         */
        public void setKitchen(String kitchen) {
            this.kitchen = kitchen;
        }

        /**
         * @return The bathroom
         */
        public String getBathroom() {
            return bathroom;
        }

        /**
         * @param bathroom The bathroom
         */
        public void setBathroom(String bathroom) {
            this.bathroom = bathroom;
        }

        /**
         * @return The bedroom
         */
        public String getBedroom() {
            return bedroom;
        }

        /**
         * @param bedroom The bedroom
         */
        public void setBedroom(String bedroom) {
            this.bedroom = bedroom;
        }

        /**
         * @return The livingRoomFemale
         */
        public String getLivingRoomFemale() {
            return livingRoomFemale;
        }

        /**
         * @param livingRoomFemale The living_room_female
         */
        public void setLivingRoomFemale(String livingRoomFemale) {
            this.livingRoomFemale = livingRoomFemale;
        }

        /**
         * @return The livingRoomMale
         */
        public String getLivingRoomMale() {
            return livingRoomMale;
        }

        /**
         * @param livingRoomMale The living_room_male
         */
        public void setLivingRoomMale(String livingRoomMale) {
            this.livingRoomMale = livingRoomMale;
        }

        /**
         * @return The extraAmenities
         */
        public String getExtraAmenities() {
            return extraAmenities;
        }

        /**
         * @param extraAmenities The extra_amenities
         */
        public void setExtraAmenities(String extraAmenities) {
            this.extraAmenities = extraAmenities;
        }

        /**
         * @return The pcPriceThursday
         */
        public String getPcPriceThursday() {
            return pcPriceThursday;
        }

        /**
         * @param pcPriceThursday The pc_price_thursday
         */
        public void setPcPriceThursday(String pcPriceThursday) {
            this.pcPriceThursday = pcPriceThursday;
        }

        /**
         * @return The pcPriceFriday
         */
        public String getPcPriceFriday() {
            return pcPriceFriday;
        }

        /**
         * @param pcPriceFriday The pc_price_friday
         */
        public void setPcPriceFriday(String pcPriceFriday) {
            this.pcPriceFriday = pcPriceFriday;
        }

        /**
         * @return The pcPriceSaturday
         */
        public String getPcPriceSaturday() {
            return pcPriceSaturday;
        }

        /**
         * @param pcPriceSaturday The pc_price_saturday
         */
        public void setPcPriceSaturday(String pcPriceSaturday) {
            this.pcPriceSaturday = pcPriceSaturday;
        }

        /**
         * @return The pcPriceEidAlFitr
         */
        public String getPcPriceEidAlFitr() {
            return pcPriceEidAlFitr;
        }

        /**
         * @param pcPriceEidAlFitr The pc_price_eid_al_fitr
         */
        public void setPcPriceEidAlFitr(String pcPriceEidAlFitr) {
            this.pcPriceEidAlFitr = pcPriceEidAlFitr;
        }

        /**
         * @return The pcPriceEidUlAdha
         */
        public String getPcPriceEidUlAdha() {
            return pcPriceEidUlAdha;
        }

        /**
         * @param pcPriceEidUlAdha The pc_price_eid_ul_adha
         */
        public void setPcPriceEidUlAdha(String pcPriceEidUlAdha) {
            this.pcPriceEidUlAdha = pcPriceEidUlAdha;
        }

        /**
         * @return The pcPriceSunWed
         */
        public String getPcPriceSunWed() {
            return pcPriceSunWed;
        }

        /**
         * @param pcPriceSunWed The pc_price_sun_wed
         */
        public void setPcPriceSunWed(String pcPriceSunWed) {
            this.pcPriceSunWed = pcPriceSunWed;
        }

        /**
         * @return The priceStartsFrom
         */
        public String getPriceStartsFrom() {
            return priceStartsFrom;
        }

        /**
         * @param priceStartsFrom The price_starts_from
         */
        public void setPriceStartsFrom(String priceStartsFrom) {
            this.priceStartsFrom = priceStartsFrom;
        }

        /**
         * @return The amenities
         */
        public List<Amenity> getAmenities() {
            return amenities;
        }

        /**
         * @param amenities The amenities
         */
        public void setAmenities(List<Amenity> amenities) {
            this.amenities = amenities;
        }

        /**
         * @return The suitable
         */
        public List<Suitable> getSuitable() {
            return suitable;
        }

        /**
         * @param suitable The suitable
         */
        public void setSuitable(List<Suitable> suitable) {
            this.suitable = suitable;
        }

        /**
         * @return The latestAvaliablity
         */
        public String getLatestAvaliablity() {
            return latestAvaliablity;
        }

        /**
         * @param latestAvaliablity The latest_avaliablity
         */
        public void setLatestAvaliablity(String latestAvaliablity) {
            this.latestAvaliablity = latestAvaliablity;
        }

        /**
         * @return The chaletImages
         */
        public List<ChaletImage> getChaletImages() {
            return chaletImages;
        }

        /**
         * @param chaletImages The chalet_images
         */
        public void setChaletImages(List<ChaletImage> chaletImages) {
            this.chaletImages = chaletImages;
        }

        public String getYoutube() {
            return youtube;
        }

        public void setYoutube(String youtube) {
            this.youtube = youtube;
        }

    }


    public static class Amenity implements Serializable {

        @SerializedName("iPropertyChaletAmenityId")
        @Expose
        private String iPropertyChaletAmenityId;
        @SerializedName("vIconImage")
        @Expose
        private String vIconImage;
        @SerializedName("iPropertyChaletId")
        @Expose
        private String iPropertyChaletId;
        @SerializedName("vAmenityName")
        @Expose
        private String vAmenityName;
        @SerializedName("iTypeFor")
        @Expose
        private String iTypeFor;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        /**
         * @return The iPropertyChaletAmenityId
         */
        public String getIPropertyChaletAmenityId() {
            return iPropertyChaletAmenityId;
        }

        /**
         * @param iPropertyChaletAmenityId The iPropertyChaletAmenityId
         */
        public void setIPropertyChaletAmenityId(String iPropertyChaletAmenityId) {
            this.iPropertyChaletAmenityId = iPropertyChaletAmenityId;
        }

        /**
         * @return The vIconImage
         */
        public String getVIconImage() {
            return vIconImage;
        }

        /**
         * @param vIconImage The vIconImage
         */
        public void setVIconImage(String vIconImage) {
            this.vIconImage = vIconImage;
        }

        /**
         * @return The iPropertyChaletId
         */
        public String getIPropertyChaletId() {
            return iPropertyChaletId;
        }

        /**
         * @param iPropertyChaletId The iPropertyChaletId
         */
        public void setIPropertyChaletId(String iPropertyChaletId) {
            this.iPropertyChaletId = iPropertyChaletId;
        }

        /**
         * @return The vAmenityName
         */
        public String getVAmenityName() {
            return vAmenityName;
        }

        /**
         * @param vAmenityName The vAmenityName
         */
        public void setVAmenityName(String vAmenityName) {
            this.vAmenityName = vAmenityName;
        }

        /**
         * @return The iTypeFor
         */
        public String getITypeFor() {
            return iTypeFor;
        }

        /**
         * @param iTypeFor The iTypeFor
         */
        public void setITypeFor(String iTypeFor) {
            this.iTypeFor = iTypeFor;
        }

        /**
         * @return The imageUrl
         */
        public String getImageUrl() {
            return imageUrl;
        }

        /**
         * @param imageUrl The image_url
         */
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }

    public static class PropertyDetail implements Serializable {

        @SerializedName("propertyid")
        @Expose
        private String propertyid;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("neighbourhood")
        @Expose
        private String neighbourhood;
        @SerializedName("view_count")
        @Expose
        private String viewCount;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("total_review")
        @Expose
        private String totalReview;
        @SerializedName("site_check_in_time")
        @Expose
        private String siteCheckInTime;
        @SerializedName("site_check_out_time")
        @Expose
        private String siteCheckOutTime;
        @SerializedName("verified")
        @Expose
        private String verified;
        @SerializedName("recommended")
        @Expose
        private String recommended;
        @SerializedName("property_desc")
        @Expose
        private String propertyDesc;
        @SerializedName("insurance_amount")
        @Expose
        private String insuranceAmount;
        @SerializedName("property_phone1")
        @Expose
        private String propertyPhone1;
        @SerializedName("property_phone2")
        @Expose
        private String propertyPhone2;
        @SerializedName("property_email")
        @Expose
        private String propertyEmail;
        @SerializedName("instagram")
        @Expose
        private String instagram;
        @SerializedName("twitter")
        @Expose
        private String twitter;
        @SerializedName("facebook")
        @Expose
        private String facebook;
        @SerializedName("pm_video")
        @Expose
        private String youtube;
        @SerializedName("favorite")
        @Expose
        private String favorite;
        @SerializedName("phone_no")
        @Expose
        private String phoneNo;
        @SerializedName("policy_type_eng")
        @Expose
        private String policyTypeEng;
        @SerializedName("policy_type_arabic")
        @Expose
        private String policyTypeArabic;
        @SerializedName("description_eng")
        @Expose
        private String descriptionEng;
        @SerializedName("description_arabic")
        @Expose
        private String descriptionArabic;
        @SerializedName("cancel_before")
        @Expose
        private String cancelBefore;
        @SerializedName("cancel_before_type")
        @Expose
        private String cancelBeforeType;
        @SerializedName("show_schedule")
        @Expose
        private String showSchedule;
        @SerializedName("online_booking")
        @Expose
        private String onlineBooking;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("price_start_from")
        @Expose
        private String priceStartFrom;
        @SerializedName("refulnd_policy")
        @Expose
        private String refulndPolicy;
        @SerializedName("terms_conditions")
        @Expose
        private String termsConditions;
        @SerializedName("insurance_description")
        @Expose
        private String insuranceDescription;
        @SerializedName("is_featured")
        @Expose
        private String isFeatured;

        /**
         * @return The propertyid
         */
        public String getPropertyid() {
            return propertyid;
        }

        /**
         * @param propertyid The propertyid
         */
        public void setPropertyid(String propertyid) {
            this.propertyid = propertyid;
        }

        /**
         * @return The title
         */
        public String getTitle() {
            return title;
        }

        /**
         * @param title The title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return The address
         */
        public String getAddress() {
            return address;
        }

        /**
         * @param address The address
         */
        public void setAddress(String address) {
            this.address = address;
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
         * @return The neighbourhood
         */
        public String getNeighbourhood() {
            return neighbourhood;
        }

        /**
         * @param neighbourhood The neighbourhood
         */
        public void setNeighbourhood(String neighbourhood) {
            this.neighbourhood = neighbourhood;
        }

        /**
         * @return The viewCount
         */
        public String getViewCount() {
            return viewCount;
        }

        /**
         * @param viewCount The view_count
         */
        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
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
         * @return The totalReview
         */
        public String getTotalReview() {
            return totalReview;
        }

        /**
         * @param totalReview The total_review
         */
        public void setTotalReview(String totalReview) {
            this.totalReview = totalReview;
        }

        /**
         * @return The siteCheckInTime
         */
        public String getSiteCheckInTime() {
            return siteCheckInTime;
        }

        /**
         * @param siteCheckInTime The site_check_in_time
         */
        public void setSiteCheckInTime(String siteCheckInTime) {
            this.siteCheckInTime = siteCheckInTime;
        }

        /**
         * @return The siteCheckOutTime
         */
        public String getSiteCheckOutTime() {
            return siteCheckOutTime;
        }

        /**
         * @param siteCheckOutTime The site_check_out_time
         */
        public void setSiteCheckOutTime(String siteCheckOutTime) {
            this.siteCheckOutTime = siteCheckOutTime;
        }

        /**
         * @return The verified
         */
        public String getVerified() {
            return verified;
        }

        /**
         * @param verified The verified
         */
        public void setVerified(String verified) {
            this.verified = verified;
        }

        /**
         * @return The recommended
         */
        public String getRecommended() {
            return recommended;
        }

        /**
         * @param recommended The recommended
         */
        public void setRecommended(String recommended) {
            this.recommended = recommended;
        }

        /**
         * @return The propertyDesc
         */
        public String getPropertyDesc() {
            return propertyDesc;
        }

        /**
         * @param propertyDesc The property_desc
         */
        public void setPropertyDesc(String propertyDesc) {
            this.propertyDesc = propertyDesc;
        }

        /**
         * @return The insuranceAmount
         */
        public String getInsuranceAmount() {
            return insuranceAmount;
        }

        /**
         * @param insuranceAmount The insurance_amount
         */
        public void setInsuranceAmount(String insuranceAmount) {
            this.insuranceAmount = insuranceAmount;
        }

        /**
         * @return The propertyPhone1
         */
        public String getPropertyPhone1() {
            return propertyPhone1;
        }

        /**
         * @param propertyPhone1 The property_phone1
         */
        public void setPropertyPhone1(String propertyPhone1) {
            this.propertyPhone1 = propertyPhone1;
        }

        /**
         * @return The propertyPhone2
         */
        public String getPropertyPhone2() {
            return propertyPhone2;
        }

        /**
         * @param propertyPhone2 The property_phone2
         */
        public void setPropertyPhone2(String propertyPhone2) {
            this.propertyPhone2 = propertyPhone2;
        }

        /**
         * @return The propertyEmail
         */
        public String getPropertyEmail() {
            return propertyEmail;
        }

        /**
         * @param propertyEmail The property_email
         */
        public void setPropertyEmail(String propertyEmail) {
            this.propertyEmail = propertyEmail;
        }

        /**
         * @return The instagram
         */
        public String getInstagram() {
            return instagram;
        }

        /**
         * @param instagram The instagram
         */
        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }

        /**
         * @return The twitter
         */
        public String getTwitter() {
            return twitter;
        }

        /**
         * @param twitter The twitter
         */
        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        /**
         * @return The facebook
         */
        public String getFacebook() {
            return facebook;
        }

        /**
         * @param facebook The facebook
         */
        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        /**
         * @return The favorite
         */
        public String getFavorite() {
            return favorite;
        }

        /**
         * @param favorite The favorite
         */
        public void setFavorite(String favorite) {
            this.favorite = favorite;
        }

        /**
         * @return The phoneNo
         */
        public String getPhoneNo() {
            return phoneNo;
        }

        /**
         * @param phoneNo The phone_no
         */
        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        /**
         * @return The policyTypeEng
         */
        public String getPolicyTypeEng() {
            return policyTypeEng;
        }

        /**
         * @param policyTypeEng The policy_type_eng
         */
        public void setPolicyTypeEng(String policyTypeEng) {
            this.policyTypeEng = policyTypeEng;
        }

        /**
         * @return The policyTypeArabic
         */
        public String getPolicyTypeArabic() {
            return policyTypeArabic;
        }

        /**
         * @param policyTypeArabic The policy_type_arabic
         */
        public void setPolicyTypeArabic(String policyTypeArabic) {
            this.policyTypeArabic = policyTypeArabic;
        }

        /**
         * @return The descriptionEng
         */
        public String getDescriptionEng() {
            return descriptionEng;
        }

        /**
         * @param descriptionEng The description_eng
         */
        public void setDescriptionEng(String descriptionEng) {
            this.descriptionEng = descriptionEng;
        }

        /**
         * @return The descriptionArabic
         */
        public String getDescriptionArabic() {
            return descriptionArabic;
        }

        /**
         * @param descriptionArabic The description_arabic
         */
        public void setDescriptionArabic(String descriptionArabic) {
            this.descriptionArabic = descriptionArabic;
        }

        /**
         * @return The cancelBefore
         */
        public String getCancelBefore() {
            return cancelBefore;
        }

        /**
         * @param cancelBefore The cancel_before
         */
        public void setCancelBefore(String cancelBefore) {
            this.cancelBefore = cancelBefore;
        }

        /**
         * @return The cancelBeforeType
         */
        public String getCancelBeforeType() {
            return cancelBeforeType;
        }

        /**
         * @param cancelBeforeType The cancel_before_type
         */
        public void setCancelBeforeType(String cancelBeforeType) {
            this.cancelBeforeType = cancelBeforeType;
        }

        /**
         * @return The showSchedule
         */
        public String getShowSchedule() {
            return showSchedule;
        }

        /**
         * @param showSchedule The show_schedule
         */
        public void setShowSchedule(String showSchedule) {
            this.showSchedule = showSchedule;
        }

        /**
         * @return The onlineBooking
         */
        public String getOnlineBooking() {
            return onlineBooking;
        }

        /**
         * @param onlineBooking The online_booking
         */
        public void setOnlineBooking(String onlineBooking) {
            this.onlineBooking = onlineBooking;
        }

        /**
         * @return The latitude
         */
        public String getLatitude() {
            return latitude;
        }

        /**
         * @param latitude The latitude
         */
        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        /**
         * @return The longitude
         */
        public String getLongitude() {
            return longitude;
        }

        /**
         * @param longitude The longitude
         */
        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        /**
         * @return The priceStartFrom
         */
        public String getPriceStartFrom() {
            return priceStartFrom;
        }

        /**
         * @param priceStartFrom The price_start_from
         */
        public void setPriceStartFrom(String priceStartFrom) {
            this.priceStartFrom = priceStartFrom;
        }

        /**
         * @return The refulndPolicy
         */
        public String getRefulndPolicy() {
            return refulndPolicy;
        }

        /**
         * @param refulndPolicy The refulnd_policy
         */
        public void setRefulndPolicy(String refulndPolicy) {
            this.refulndPolicy = refulndPolicy;
        }

        /**
         * @return The termsConditions
         */
        public String getTermsConditions() {
            return termsConditions;
        }

        /**
         * @param termsConditions The terms_conditions
         */
        public void setTermsConditions(String termsConditions) {
            this.termsConditions = termsConditions;
        }

        /**
         * @return The insuranceDescription
         */
        public String getInsuranceDescription() {
            return insuranceDescription;
        }

        /**
         * @param insuranceDescription The insurance_description
         */
        public void setInsuranceDescription(String insuranceDescription) {
            this.insuranceDescription = insuranceDescription;
        }

        /**
         * @return The isFeatured
         */
        public String getIsFeatured() {
            return isFeatured;
        }

        /**
         * @param isFeatured The is_featured
         */
        public void setIsFeatured(String isFeatured) {
            this.isFeatured = isFeatured;
        }

        public String getYoutube() {
            return youtube;
        }

        public void setYoutube(String youtube) {
            this.youtube = youtube;
        }
    }

    public static class PropertyImage implements Serializable{

        @SerializedName("web_image")
        @Expose
        private String webImage;
        @SerializedName("property_images_id")
        @Expose
        private String propertyImagesId;
        @SerializedName("property_images")
        @Expose
        private String propertyImages;
        @SerializedName("property_id")
        @Expose
        private String propertyId;

        /**
         * @return The webImage
         */
        public String getWebImage() {
            return webImage;
        }

        /**
         * @param webImage The web_image
         */
        public void setWebImage(String webImage) {
            this.webImage = webImage;
        }

        /**
         * @return The propertyImagesId
         */
        public String getPropertyImagesId() {
            return propertyImagesId;
        }

        /**
         * @param propertyImagesId The property_images_id
         */
        public void setPropertyImagesId(String propertyImagesId) {
            this.propertyImagesId = propertyImagesId;
        }

        /**
         * @return The propertyImages
         */
        public String getPropertyImages() {
            return propertyImages;
        }

        /**
         * @param propertyImages The property_images
         */
        public void setPropertyImages(String propertyImages) {
            this.propertyImages = propertyImages;
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

    }

    public static class Settings implements Serializable {

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

    public static class Suitable implements Serializable {

        @SerializedName("iPropertyChaletAmenityId")
        @Expose
        private String iPropertyChaletAmenityId;
        @SerializedName("vIconImage")
        @Expose
        private String vIconImage;
        @SerializedName("iPropertyChaletId")
        @Expose
        private String iPropertyChaletId;
        @SerializedName("vAmenityName")
        @Expose
        private String vAmenityName;
        @SerializedName("iTypeFor")
        @Expose
        private String iTypeFor;
        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        /**
         * @return The iPropertyChaletAmenityId
         */
        public String getIPropertyChaletAmenityId() {
            return iPropertyChaletAmenityId;
        }

        /**
         * @param iPropertyChaletAmenityId The iPropertyChaletAmenityId
         */
        public void setIPropertyChaletAmenityId(String iPropertyChaletAmenityId) {
            this.iPropertyChaletAmenityId = iPropertyChaletAmenityId;
        }

        /**
         * @return The vIconImage
         */
        public String getVIconImage() {
            return vIconImage;
        }

        /**
         * @param vIconImage The vIconImage
         */
        public void setVIconImage(String vIconImage) {
            this.vIconImage = vIconImage;
        }

        /**
         * @return The iPropertyChaletId
         */
        public String getIPropertyChaletId() {
            return iPropertyChaletId;
        }

        /**
         * @param iPropertyChaletId The iPropertyChaletId
         */
        public void setIPropertyChaletId(String iPropertyChaletId) {
            this.iPropertyChaletId = iPropertyChaletId;
        }

        /**
         * @return The vAmenityName
         */
        public String getVAmenityName() {
            return vAmenityName;
        }

        /**
         * @param vAmenityName The vAmenityName
         */
        public void setVAmenityName(String vAmenityName) {
            this.vAmenityName = vAmenityName;
        }

        /**
         * @return The iTypeFor
         */
        public String getITypeFor() {
            return iTypeFor;
        }

        /**
         * @param iTypeFor The iTypeFor
         */
        public void setITypeFor(String iTypeFor) {
            this.iTypeFor = iTypeFor;
        }

        /**
         * @return The imageUrl
         */
        public String getImageUrl() {
            return imageUrl;
        }

        /**
         * @param imageUrl The image_url
         */
        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

    }
}