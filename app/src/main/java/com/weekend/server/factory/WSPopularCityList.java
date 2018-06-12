package com.weekend.server.factory;

import com.weekend.models.PopularCityListModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 28-May-16.
 */
public class WSPopularCityList extends WSUtils {
    public WSPopularCityList() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<PopularCityListModel> subCategoryCall = webServices.requestPopularCityList(params);
        subCategoryCall.enqueue(callback);
    }
}
