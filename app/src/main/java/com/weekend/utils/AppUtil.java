package com.weekend.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by hb on 7/19/2016.
 */
public class AppUtil {

    /**
     * Get the software version number
     *
     * @param Context context
     * @return The current version of Code
     */
    public static int getAppVersionCode (Context context ) {
        int verCode = -1;
        try {
            String packageName = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        }catch ( PackageManager. NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * Get the software version information is displayed
     *
     * @param Context context
     * @return The current version information
     */
    public static String getAppVerionName (Context context) {
        String major = "" ;
        try {
            String packageName = context.getPackageName();
            major = context.getPackageManager ().getPackageInfo(packageName, 0 ).versionName;
        }catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return major;
    }
}
