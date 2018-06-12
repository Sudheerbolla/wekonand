package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ISDCodeModel implements WSResponse, Serializable {

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

        @SerializedName("mc_country_id")
        @Expose
        private String mcCountryId;
        @SerializedName("mc_isdcode")
        @Expose
        private String mcIsdcode;

        /**
         * @return The mcCountryId
         */
        public String getMcCountryId() {
            return mcCountryId;
        }

        /**
         * @param mcCountryId The mc_country_id
         */
        public void setMcCountryId(String mcCountryId) {
            this.mcCountryId = mcCountryId;
        }

        /**
         * @return The mcIsdcode
         */
        public String getMcIsdcode() {
            return mcIsdcode;
        }

        /**
         * @param mcIsdcode The mc_isdcode
         */
        public void setMcIsdcode(String mcIsdcode) {
            this.mcIsdcode = mcIsdcode;
        }

    }
}