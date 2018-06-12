package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 10/6/16.
 */
public class ReverseGeocodingModel implements WSResponse, Serializable {
    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();
    @SerializedName("status")
    @Expose
    private String status;

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public class Southwest {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        /**
         *
         * @return
         * The lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

    }
    public class Southwest_ {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        /**
         *
         * @return
         * The lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

    }
    public class Viewport {

        @SerializedName("northeast")
        @Expose
        private Northeast_ northeast;
        @SerializedName("southwest")
        @Expose
        private Southwest_ southwest;

        /**
         *
         * @return
         * The northeast
         */
        public Northeast_ getNortheast() {
            return northeast;
        }

        /**
         *
         * @param northeast
         * The northeast
         */
        public void setNortheast(Northeast_ northeast) {
            this.northeast = northeast;
        }

        /**
         *
         * @return
         * The southwest
         */
        public Southwest_ getSouthwest() {
            return southwest;
        }

        /**
         *
         * @param southwest
         * The southwest
         */
        public void setSouthwest(Southwest_ southwest) {
            this.southwest = southwest;
        }

    }
    public class Result {

        @SerializedName("address_components")
        @Expose
        private List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
        @SerializedName("formatted_address")
        @Expose
        private String formattedAddress;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("types")
        @Expose
        private List<String> types = new ArrayList<String>();

        /**
         *
         * @return
         * The addressComponents
         */
        public List<AddressComponent> getAddressComponents() {
            return addressComponents;
        }

        /**
         *
         * @param addressComponents
         * The address_components
         */
        public void setAddressComponents(List<AddressComponent> addressComponents) {
            this.addressComponents = addressComponents;
        }

        /**
         *
         * @return
         * The formattedAddress
         */
        public String getFormattedAddress() {
            return formattedAddress;
        }

        /**
         *
         * @param formattedAddress
         * The formatted_address
         */
        public void setFormattedAddress(String formattedAddress) {
            this.formattedAddress = formattedAddress;
        }

        /**
         *
         * @return
         * The geometry
         */
        public Geometry getGeometry() {
            return geometry;
        }

        /**
         *
         * @param geometry
         * The geometry
         */
        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        /**
         *
         * @return
         * The placeId
         */
        public String getPlaceId() {
            return placeId;
        }

        /**
         *
         * @param placeId
         * The place_id
         */
        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        /**
         *
         * @return
         * The types
         */
        public List<String> getTypes() {
            return types;
        }

        /**
         *
         * @param types
         * The types
         */
        public void setTypes(List<String> types) {
            this.types = types;
        }

    }
    public class Northeast_ {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        /**
         *
         * @return
         * The lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

    }
    public class Northeast {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        /**
         *
         * @return
         * The lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

    }
    public class Location {

        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        /**
         *
         * @return
         * The lat
         */
        public Double getLat() {
            return lat;
        }

        /**
         *
         * @param lat
         * The lat
         */
        public void setLat(Double lat) {
            this.lat = lat;
        }

        /**
         *
         * @return
         * The lng
         */
        public Double getLng() {
            return lng;
        }

        /**
         *
         * @param lng
         * The lng
         */
        public void setLng(Double lng) {
            this.lng = lng;
        }

    }
    public class Geometry {

        @SerializedName("bounds")
        @Expose
        private Bounds bounds;
        @SerializedName("location")
        @Expose
        private Location location;
        @SerializedName("location_type")
        @Expose
        private String locationType;
        @SerializedName("viewport")
        @Expose
        private Viewport viewport;

        /**
         *
         * @return
         * The bounds
         */
        public Bounds getBounds() {
            return bounds;
        }

        /**
         *
         * @param bounds
         * The bounds
         */
        public void setBounds(Bounds bounds) {
            this.bounds = bounds;
        }

        /**
         *
         * @return
         * The location
         */
        public Location getLocation() {
            return location;
        }

        /**
         *
         * @param location
         * The location
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         *
         * @return
         * The locationType
         */
        public String getLocationType() {
            return locationType;
        }

        /**
         *
         * @param locationType
         * The location_type
         */
        public void setLocationType(String locationType) {
            this.locationType = locationType;
        }

        /**
         *
         * @return
         * The viewport
         */
        public Viewport getViewport() {
            return viewport;
        }

        /**
         *
         * @param viewport
         * The viewport
         */
        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }

    }
    public class Bounds {

        @SerializedName("northeast")
        @Expose
        private Northeast northeast;
        @SerializedName("southwest")
        @Expose
        private Southwest southwest;

        /**
         *
         * @return
         * The northeast
         */
        public Northeast getNortheast() {
            return northeast;
        }

        /**
         *
         * @param northeast
         * The northeast
         */
        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }

        /**
         *
         * @return
         * The southwest
         */
        public Southwest getSouthwest() {
            return southwest;
        }

        /**
         *
         * @param southwest
         * The southwest
         */
        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

    }
    public class AddressComponent {

        @SerializedName("long_name")
        @Expose
        private String longName;
        @SerializedName("short_name")
        @Expose
        private String shortName;
        @SerializedName("types")
        @Expose
        private List<String> types = new ArrayList<String>();

        /**
         *
         * @return
         * The longName
         */
        public String getLongName() {
            return longName;
        }

        /**
         *
         * @param longName
         * The long_name
         */
        public void setLongName(String longName) {
            this.longName = longName;
        }

        /**
         *
         * @return
         * The shortName
         */
        public String getShortName() {
            return shortName;
        }

        /**
         *
         * @param shortName
         * The short_name
         */
        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        /**
         *
         * @return
         * The types
         */
        public List<String> getTypes() {
            return types;
        }

        /**
         *
         * @param types
         * The types
         */
        public void setTypes(List<String> types) {
            this.types = types;
        }

    }

}