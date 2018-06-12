package com.weekend.payfort;

/**
 * Created by hb on 27/9/16.
 */

public interface IPaymentRequestCallBack {
    void onPaymentRequestResponse(int responseType, PayFortData responseData);
}
