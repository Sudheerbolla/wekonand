package com.weekend.server.factory;

import com.weekend.models.ManageSchedulesModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 28-May-16.
 */
public class WSManageSchedule extends WSUtils {
    public WSManageSchedule() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<ManageSchedulesModel> subCategoryCall = webServices.requestManageSchedule(params);
        subCategoryCall.enqueue(callback);
    }
}
