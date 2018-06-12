package com.weekend.server;

import android.os.Bundle;

import java.net.URLEncoder;

/**
 * Created by hb on 9/24/2016.
 */
public class WSUtils {

    @SuppressWarnings("deprecation")
    public static String encodeGETUrl(Bundle parameters) {
        StringBuilder sb = new StringBuilder();
        if (parameters != null && parameters.size() > 0) {
            boolean first = true;
            for (String key : parameters.keySet()) {
                if (key != null) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append("&");
                    }
                    String value = "";
                    Object object = parameters.get(key);
                    if (object != null) {
                        value = String.valueOf(object);
                    }

                    try {
                        sb.append(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
                    } catch (Exception e) {
                        sb.append(URLEncoder.encode(key) + "=" + URLEncoder.encode(value));
                    }
                }
            }
        }
        return sb.toString();
    }

    /**To encode parameters and return a encoded string url
     *
     * @param url
     * @param mParams
     * @return
     */
    public static String encodeUrl(String url, Bundle mParams) {
        return url + encodeGETUrl(mParams);
    }
}
