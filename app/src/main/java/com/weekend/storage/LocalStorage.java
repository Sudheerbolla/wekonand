package com.weekend.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.weekend.utils.Constants;

public class LocalStorage {
    public static final String PREF_NAME = "user_data";
    private static LocalStorage mInstance;
    private static SharedPreferences preferences;
    private static Editor editor;
    private String deviceToken;

    private LocalStorage(Context mContext) {
        preferences = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static synchronized LocalStorage getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new LocalStorage(mContext);
        }
        return mInstance;
    }

    public void putString(String key, String value) {
        editor = editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        editor = editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public void putFloat(String key, float value) {
        editor = editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        editor = editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public void putObject(String key, Object/*Your object class*/ value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public String getObject(String key, String defValue) {
        Gson gson = new Gson();
        String json = preferences.getString(key, "");
        return json;

        //To get Object use this json
        //Object/*Your object class*/ obj = gson.fromJson(json, Object.class/*Your object class*/);
        //Use @Expose annotation for Object class getter setter variables
    }

    private boolean tempAuto, isRememberMe;
    private String email, password;

    /**
     * Here clear the shared preferences and restore the user first time install key value as false,
     * because user is launched this app already
     */
    public void clearLocalStorage() {
        isRememberMe = this.getBoolean(Constants.SP_KEY_IS_REMEMBER_ME, false);
        email = this.getString(Constants.SP_KEY_EMAIL, "");
        password = this.getString(Constants.SP_KEY_PASSWORD, "");
        deviceToken = this.getString(Constants.SP_KEY_GCM_REGISTRATION_ID, "");
        editor.clear().commit();
        this.putBoolean(Constants.SP_KEY_IS_USER_FIRST_TIME, false);
        this.putBoolean(Constants.SP_KEY_IS_REMEMBER_ME, isRememberMe);
        this.putString(Constants.SP_KEY_EMAIL, email);
        this.putString(Constants.SP_KEY_GCM_REGISTRATION_ID, deviceToken);
        this.putString(Constants.SP_KEY_PASSWORD, password);
        this.putString(Constants.SP_KEY_CREDITS, "0.00");
    }
}
