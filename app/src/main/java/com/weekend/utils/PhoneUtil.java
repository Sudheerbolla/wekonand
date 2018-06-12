package com.weekend.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hb on 7/15/2016.
 */
public class PhoneUtil {

    public static String getUDID(Context context) {
        return android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }


    /**
     * Check Network Aviablity
     * @param context Context Object Of Activity
     * @return
     */
    public static boolean isNetworkConnected(Context context, boolean isShowMessage) {
        try{
            if(context!=null){
                ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }else {
                    if(isShowMessage){

                    }
                    return false;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isSimCardAvailabe(Context context) {
        boolean isAvailable = true;
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_READY:
                isAvailable = true;
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                isAvailable = false;
                break;
        }
        return isAvailable;
    }

    public static boolean isGoogleMapsInstalled(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean validMobileNumber(String phone){
        if(TextUtils.isEmpty(phone)){
            return false;
        }

        Pattern p = Pattern.compile("[0]+");
        Matcher m = p.matcher(phone);
       /* if(TextUtils.isEmpty(phone)){
            return false;
        }if(phone.length()==9 && phone.equalsIgnoreCase("000000000")){
            return false;
        }if(phone.length()==10 && phone.equalsIgnoreCase("0000000000")){
            return false;
        }*/
        return !m.matches();
    }
}
