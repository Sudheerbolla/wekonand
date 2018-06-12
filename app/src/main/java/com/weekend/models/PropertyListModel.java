package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyListModel implements WSResponse, Serializable {
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

        @SerializedName("get_property_listing")
        @Expose
        private List<GetPropertyListing> getPropertyListing = new ArrayList<GetPropertyListing>();
        @SerializedName("get_total_detail_chalet")
        @Expose
        private List<GetTotalDetailChalet> getTotalDetailChalet = new ArrayList<GetTotalDetailChalet>();

        /**
         * @return The getPropertyListing
         */
        public List<GetPropertyListing> getGetPropertyListing() {
            return getPropertyListing;
        }

        /**
         * @param getPropertyListing The get_property_listing
         */
        public void setGetPropertyListing(List<GetPropertyListing> getPropertyListing) {
            this.getPropertyListing = getPropertyListing;
        }

        /**
         * @return The getTotalDetailChalet
         */
        public List<GetTotalDetailChalet> getGetTotalDetailChalet() {
            return getTotalDetailChalet;
        }

        /**
         * @param getTotalDetailChalet The get_total_detail_chalet
         */
        public void setGetTotalDetailChalet(List<GetTotalDetailChalet> getTotalDetailChalet) {
            this.getTotalDetailChalet = getTotalDetailChalet;
        }

    }

    public class GetPropertyListing {

        @SerializedName("pi_image")
        @Expose
        private String piImage;
        @SerializedName("get_property_images_from_pl")
        @Expose
        private List<PropertyImage> propertyImages = new ArrayList<>();
        @SerializedName("get_total_detail")
        @Expose
        private List<GetTotalDetail> getTotalDetail = new ArrayList<GetTotalDetail>();
        @SerializedName("property_chalet_id")
        @Expose
        private String propertyChaletId;
        @SerializedName("property_id")
        @Expose
        private String propertyId;
        @SerializedName("property_title")
        @Expose
        private String propertyTitle;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("mark_as_verified")
        @Expose
        private String markAsVerified;
        @SerializedName("mark_as_featured")
        @Expose
        private String markAsFeatured;
        @SerializedName("avg_rating")
        @Expose
        private String avgRating;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("distance")
        @Expose
        private String distance;
        @SerializedName("mark_as_recommended")
        @Expose
        private String markAsRecommended;
        @SerializedName("is_favorite")
        @Expose
        private String isFavorite;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("neighbourhood")
        @Expose
        private String neighbourhood;

        /**
         * @return The piImage
         */
        public String getPiImage() {
            return piImage;
        }

        /**
         * @param piImage The pi_image
         */
        public void setPiImage(String piImage) {
            this.piImage = piImage;
        }

        /**
         * @return The getTotalDetail
         */
        public List<GetTotalDetail> getGetTotalDetail() {
            return getTotalDetail;
        }

        /**
         * @param getTotalDetail The get_total_detail
         */
        public void setGetTotalDetail(List<GetTotalDetail> getTotalDetail) {
            this.getTotalDetail = getTotalDetail;
        }

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
         * @return The distance
         */
        public String getDistance() {
            return distance;
        }

        /**
         * @param distance The distance
         */
        public void setDistance(String distance) {
            this.distance = distance;
        }

        /**
         * @return The markAsRecommended
         */
        public String getMarkAsRecommended() {
            return markAsRecommended;
        }

        /**
         * @param markAsRecommended The mark_as_recommended
         */
        public void setMarkAsRecommended(String markAsRecommended) {
            this.markAsRecommended = markAsRecommended;
        }

        /**
         * @return The isFavorite
         */
        public String getIsFavorite() {
            return isFavorite;
        }

        /**
         * @param isFavorite The is_favorite
         */
        public void setIsFavorite(String isFavorite) {
            this.isFavorite = isFavorite;
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

        public List<PropertyImage> getPropertyImages() {
            return propertyImages;
        }

        public void setPropertyImages(List<PropertyImage> propertyImages) {
            this.propertyImages = propertyImages;
        }
    }

    public static class PropertyImage implements Serializable {

        @SerializedName("pmpl_image")
        @Expose
        private String propertyImage;
        @SerializedName("pmpl_property_id")
        @Expose
        private String propertyImageId;

        public String getPropertyImage() {
            return propertyImage;
        }

        public void setPropertyImage(String propertyImage) {
            this.propertyImage = propertyImage;
        }

        public String getPropertyImageId() {
            return propertyImageId;
        }

        public void setPropertyImageId(String propertyImageId) {
            this.propertyImageId = propertyImageId;
        }
    }

    public class GetTotalDetail {

        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("avail")
        @Expose
        private String avail;
        @SerializedName("not_avail")
        @Expose
        private String notAvail;

        /**
         * @return The total
         */
        public String getTotal() {
            return total;
        }

        /**
         * @param total The total
         */
        public void setTotal(String total) {
            this.total = total;
        }

        /**
         * @return The avail
         */
        public String getAvail() {
            return avail;
        }

        /**
         * @param avail The avail
         */
        public void setAvail(String avail) {
            this.avail = avail;
        }

        /**
         * @return The notAvail
         */
        public String getNotAvail() {
            return notAvail;
        }

        /**
         * @param notAvail The not_avail
         */
        public void setNotAvail(String notAvail) {
            this.notAvail = notAvail;
        }

    }

    public class GetTotalDetailChalet {

        @SerializedName("all")
        @Expose
        private String tAll;
        @SerializedName("available")
        @Expose
        private String available;
        @SerializedName("notAvailable")
        @Expose
        private String notAvailable;

        /**
         * @return The tAll
         */
        public String getTAll() {
            return tAll;
        }

        /**
         * @param tAll The t_all
         */
        public void setTAll(String tAll) {
            this.tAll = tAll;
        }

        /**
         * @return The available
         */
        public String getAvailable() {
            return available;
        }

        /**
         * @param available The available
         */
        public void setAvailable(String available) {
            this.available = available;
        }

        /**
         * @return The notAvailable
         */
        public String getNotAvailable() {
            return notAvailable;
        }

        /**
         * @param notAvailable The notAvailable
         */
        public void setNotAvailable(String notAvailable) {
            this.notAvailable = notAvailable;
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
}