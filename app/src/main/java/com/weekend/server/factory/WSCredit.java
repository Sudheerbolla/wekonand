package com.weekend.server.factory;

import com.weekend.models.CreditModel;
import com.weekend.models.LogoutModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 29/5/17.
 */

public class WSCredit extends WSUtils {
    public WSCredit() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<CreditModel> subCategoryCall = webServices.requestCredit(params);
        subCategoryCall.enqueue(callback);
    }
}
