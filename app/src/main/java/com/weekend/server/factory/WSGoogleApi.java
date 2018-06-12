package com.weekend.server.factory;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Callback;

/**
 * Created by hb on 9/6/16.
 */
public class WSGoogleApi extends WSUtils {
    public static final String GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/";
    public WSGoogleApi() {
        super(GOOGLE_BASE_URL);
    }

    @Override
    protected void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
       /* Call<AddressListModel> addressListModel = webServices.requestGoogleApi(params);
        addressListModel.enqueue(callback);*/
    }
}
