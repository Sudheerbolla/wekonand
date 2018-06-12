package com.weekend.server.factory;

import com.weekend.models.SaveAbuseReasonModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 28-May-16.
 */
public class WSSaveAbuseReason extends WSUtils {
    public WSSaveAbuseReason() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<SaveAbuseReasonModel> subCategoryCall = webServices.requestSaveAbuseReason(params);
        subCategoryCall.enqueue(callback);
    }
}
