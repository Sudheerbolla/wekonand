package com.weekend.server.factory;

import com.weekend.models.BookPropertyByCustomerModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 29/7/17.
 */

public class WSPayLaterPropertyByCustomer extends WSUtils {
    public WSPayLaterPropertyByCustomer() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<BookPropertyByCustomerModel> subCategoryCall = webServices.requestPayLaterPropertyByCustomer(params);
        subCategoryCall.enqueue(callback);
    }
}
