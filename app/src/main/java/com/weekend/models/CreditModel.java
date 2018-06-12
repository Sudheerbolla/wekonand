package com.weekend.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weekend.server.WSResponse;
import com.weekend.utils.CommonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 29/5/17.
 */

public class CreditModel implements WSResponse, Serializable {

    @SerializedName("settings")
    @Expose
    private Settings settings;

    @SerializedName("data")
    @Expose
    private List<Credit> credit = new ArrayList<Credit>();

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

    public List<Credit> getCredit() {
        return credit;
    }

    public void setCredit(ArrayList<Credit> credit) {
        this.credit = credit;
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

    public static class Credit {
        @SerializedName("u_credit")
        @Expose
        private String uCredit;

        public String getUCredit() {
            return  CommonUtil.isEmpty(uCredit)?"":uCredit;
        }

        public void setUCredit(String uCredit) {
            this.uCredit = uCredit;
        }

    }
}
