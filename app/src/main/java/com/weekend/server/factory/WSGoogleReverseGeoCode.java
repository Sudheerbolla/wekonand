package com.weekend.server.factory;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Callback;

/**
 * Created by hb on 10/6/16.
 */
public class WSGoogleReverseGeoCode extends WSUtils {
    public static final String GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/";
    public WSGoogleReverseGeoCode() {
        super(GOOGLE_BASE_URL);
    }
    @Override
    protected void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        /*Call<ReverseGeocodingModel> reverseAddressModel = webServices.requestReverseGeoCode(params);
        reverseAddressModel.enqueue(callback);*/
    }
}
