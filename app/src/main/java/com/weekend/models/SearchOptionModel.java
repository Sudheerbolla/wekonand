package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchOptionModel implements WSResponse, Serializable {

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

    public class AmenitiesList {

        @SerializedName("amenity_master_id")
        @Expose
        private String amenityMasterId;
        @SerializedName("amenity_name")
        @Expose
        private String amenityName;

        private boolean isSelected;

        /**
         * @return The amenityMasterId
         */
        public String getAmenityMasterId() {
            return amenityMasterId;
        }

        /**
         * @param amenityMasterId The amenity_master_id
         */
        public void setAmenityMasterId(String amenityMasterId) {
            this.amenityMasterId = amenityMasterId;
        }

        /**
         * @return The amenityName
         */
        public String getAmenityName() {
            return amenityName;
        }

        /**
         * @param amenityName The amenity_name
         */
        public void setAmenityName(String amenityName) {
            this.amenityName = amenityName;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public class ChaletLeasingTypeList {

        @SerializedName("chalet_leasing_type_id")
        @Expose
        private String chaletLeasingTypeId;
        @SerializedName("chalet_leasing_type_title")
        @Expose
        private String chaletLeasingTypeTitle;

        private boolean isSelected;

        /**
         * @return The chaletLeasingTypeId
         */
        public String getChaletLeasingTypeId() {
            return chaletLeasingTypeId;
        }

        /**
         * @param chaletLeasingTypeId The chalet_leasing_type_id
         */
        public void setChaletLeasingTypeId(String chaletLeasingTypeId) {
            this.chaletLeasingTypeId = chaletLeasingTypeId;
        }

        /**
         * @return The chaletLeasingTypeTitle
         */
        public String getChaletLeasingTypeTitle() {
            return chaletLeasingTypeTitle;
        }

        /**
         * @param chaletLeasingTypeTitle The chalet_leasing_type_title
         */
        public void setChaletLeasingTypeTitle(String chaletLeasingTypeTitle) {
            this.chaletLeasingTypeTitle = chaletLeasingTypeTitle;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public class ChaletSize {

        @SerializedName("value")
        @Expose
        private String value;
        @SerializedName("text")
        @Expose
        private String text;

        private boolean isSelected;

        /**
         * @return The value
         */
        public String getValue() {
            return value;
        }

        /**
         * @param value The value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * @return The text
         */
        public String getText() {
            return text;
        }

        /**
         * @param text The text
         */
        public void setText(String text) {
            this.text = text;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public class CityLocation {

        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        private boolean isSelected;

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
         * @return The locationName
         */
        public String getLocationName() {
            return locationName;
        }

        /**
         * @param locationName The location_name
         */
        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public class Data {

        @SerializedName("city_location")
        @Expose
        private List<CityLocation> cityLocation = new ArrayList<CityLocation>();
        @SerializedName("get_section_list")
        @Expose
        private List<GetSectionList> getSectionList = new ArrayList<GetSectionList>();
        @SerializedName("chalet_leasing_type_list")
        @Expose
        private List<ChaletLeasingTypeList> chaletLeasingTypeList = new ArrayList<ChaletLeasingTypeList>();
        @SerializedName("amenities_list")
        @Expose
        private List<AmenitiesList> amenitiesList = new ArrayList<AmenitiesList>();
        @SerializedName("suitable_for_list")
        @Expose
        private List<SuitableForList> suitableForList = new ArrayList<SuitableForList>();
        @SerializedName("soccer_size")
        @Expose
        private List<SoccerSize> soccerSize = new ArrayList<SoccerSize>();
        @SerializedName("chalet_size")
        @Expose
        private List<ChaletSize> chaletSize = new ArrayList<ChaletSize>();

        /**
         * @return The cityLocation
         */
        public List<CityLocation> getCityLocation() {
            return cityLocation;
        }

        /**
         * @param cityLocation The city_location
         */
        public void setCityLocation(List<CityLocation> cityLocation) {
            this.cityLocation = cityLocation;
        }

        /**
         * @return The getSectionList
         */
        public List<GetSectionList> getGetSectionList() {
            return getSectionList;
        }

        /**
         * @param getSectionList The get_section_list
         */
        public void setGetSectionList(List<GetSectionList> getSectionList) {
            this.getSectionList = getSectionList;
        }

        /**
         * @return The chaletLeasingTypeList
         */
        public List<ChaletLeasingTypeList> getChaletLeasingTypeList() {
            return chaletLeasingTypeList;
        }

        /**
         * @param chaletLeasingTypeList The chalet_leasing_type_list
         */
        public void setChaletLeasingTypeList(List<ChaletLeasingTypeList> chaletLeasingTypeList) {
            this.chaletLeasingTypeList = chaletLeasingTypeList;
        }

        /**
         * @return The amenitiesList
         */
        public List<AmenitiesList> getAmenitiesList() {
            return amenitiesList;
        }

        /**
         * @param amenitiesList The amenities_list
         */
        public void setAmenitiesList(List<AmenitiesList> amenitiesList) {
            this.amenitiesList = amenitiesList;
        }

        /**
         * @return The suitableForList
         */
        public List<SuitableForList> getSuitableForList() {
            return suitableForList;
        }

        /**
         * @param suitableForList The suitable_for_list
         */
        public void setSuitableForList(List<SuitableForList> suitableForList) {
            this.suitableForList = suitableForList;
        }

        /**
         * @return The soccerSize
         */
        public List<SoccerSize> getSoccerSize() {
            return soccerSize;
        }

        /**
         * @param soccerSize The soccer_size
         */
        public void setSoccerSize(List<SoccerSize> soccerSize) {
            this.soccerSize = soccerSize;
        }

        /**
         * @return The chaletSize
         */
        public List<ChaletSize> getChaletSize() {
            return chaletSize;
        }

        /**
         * @param chaletSize The chalet_size
         */
        public void setChaletSize(List<ChaletSize> chaletSize) {
            this.chaletSize = chaletSize;
        }

    }

    public class GetSectionList {

        @SerializedName("section_id")
        @Expose
        private String sectionId;
        @SerializedName("section_title")
        @Expose
        private String sectionTitle;

        private boolean isSelected;

        /**
         * @return The sectionId
         */
        public String getSectionId() {
            return sectionId;
        }

        /**
         * @param sectionId The section_id
         */
        public void setSectionId(String sectionId) {
            this.sectionId = sectionId;
        }

        /**
         * @return The sectionTitle
         */
        public String getSectionTitle() {
            return sectionTitle;
        }

        /**
         * @param sectionTitle The section_title
         */
        public void setSectionTitle(String sectionTitle) {
            this.sectionTitle = sectionTitle;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
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

    public class SoccerSize {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        private boolean isSelected;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public class SuitableForList {

        @SerializedName("suitable_amenity_master_id")
        @Expose
        private String suitableAmenityMasterId;
        @SerializedName("suitable_amenity_name")
        @Expose
        private String suitableAmenityName;

        private boolean isSelected;

        /**
         * @return The suitableAmenityMasterId
         */
        public String getSuitableAmenityMasterId() {
            return suitableAmenityMasterId;
        }

        /**
         * @param suitableAmenityMasterId The suitable_amenity_master_id
         */
        public void setSuitableAmenityMasterId(String suitableAmenityMasterId) {
            this.suitableAmenityMasterId = suitableAmenityMasterId;
        }

        /**
         * @return The suitableAmenityName
         */
        public String getSuitableAmenityName() {
            return suitableAmenityName;
        }

        /**
         * @param suitableAmenityName The suitable_amenity_name
         */
        public void setSuitableAmenityName(String suitableAmenityName) {
            this.suitableAmenityName = suitableAmenityName;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}