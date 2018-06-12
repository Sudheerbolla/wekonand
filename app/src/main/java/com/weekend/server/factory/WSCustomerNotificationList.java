package com.weekend.server.factory;

import com.weekend.models.CustomerNotificationListModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 28-May-16.
 */
public class WSCustomerNotificationList extends WSUtils {

    public WSCustomerNotificationList() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<CustomerNotificationListModel> subCategoryCall = webServices.requestCustomerNotificationList(params);
        subCategoryCall.enqueue(callback);
    }
}
