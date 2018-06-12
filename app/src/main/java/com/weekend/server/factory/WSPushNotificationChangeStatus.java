package com.weekend.server.factory;

import com.weekend.models.CustomerNotificationListModel;
import com.weekend.models.PushNotificationChangeStatusModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 28-May-16.
 */
public class WSPushNotificationChangeStatus extends WSUtils {

    public WSPushNotificationChangeStatus() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<PushNotificationChangeStatusModel> subCategoryCall = webServices.requestPushNotificationChangeStatus(params);
        subCategoryCall.enqueue(callback);
    }
}
