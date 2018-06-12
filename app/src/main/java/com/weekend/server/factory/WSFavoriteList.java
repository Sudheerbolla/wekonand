package com.weekend.server.factory;

import com.weekend.models.FavoriteModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 28-May-16.
 */
public class WSFavoriteList extends WSUtils {
    public WSFavoriteList() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<FavoriteModel> subCategoryCall = webServices.requestFavoriteList(params);
        subCategoryCall.enqueue(callback);
    }
}
