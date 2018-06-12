package com.weekend.server.factory;

import com.weekend.models.DiscountCouponApplyModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by hb on 6/8/16.
 */
public class WSDiscountCouponApply extends WSUtils{

    public WSDiscountCouponApply() {
        super();
    }

    @Override
    public void enqueueWebService(Map<String, String> params, Map<String, RequestBody> fileUploadParams, Callback callback) {
        Call<DiscountCouponApplyModel> subCategoryCall = webServices.requestDiscountCouponApply(params);
        subCategoryCall.enqueue(callback);
    }

}
