package com.weekend.server.factory;

import com.weekend.models.GetPropertyPriceModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 28-May-16.
 */
public class WSGetPropertyPrice extends WSUtils {
    public WSGetPropertyPrice() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<GetPropertyPriceModel> subCategoryCall = webServices.requestGetPropertyPrice(params);
        subCategoryCall.enqueue(callback);
    }
}
