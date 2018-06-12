package com.weekend.payfort;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.weekend.HomeActivity;
import com.weekend.R;
import com.weekend.models.PayfortToken;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.weekend.server.factory.WSUtils.REQ_PAYFORT_TOKEN_RETURN;

public class SadadPayFortPayment implements IParser<WSResponse> {

    public static final int RESPONSE_GET_TOKEN = 111;
    public static final int RESPONSE_PURCHASE = 222;
    public static final int RESPONSE_PURCHASE_CANCEL = 333;
    public static final int RESPONSE_PURCHASE_SUCCESS = 444;
    public static final int RESPONSE_PURCHASE_FAILURE = 555;

    private final static String KEY_MERCHANT_IDENTIFIER = "merchant_identifier";
    private final static String KEY_SERVICE_COMMAND = "service_command";
    private final static String KEY_DEVICE_ID = "device_id";
    private final static String KEY_MERCHANT_REFERENCE = "merchant_reference";
    private final static String KEY_EXPIRY_DATE = "expiry_date";
    private final static String KEY_CARD_NUMBER = "card_number";
    private final static String KEY_CARD_SECURITY_CODE = "card_security_code";
    private final static String KEY_LANGUAGE = "language";
    private final static String KEY_COMMAND = "command";
    private final static String KEY_ACCESS_CODE = "access_code";
    private final static String KEY_SIGNATURE = "signature";
    private final static String KEY_TOKEN_NAME = "token_name";
    private final static String KEY_AMOUNT = "amount";
    private final static String KEY_CURRENCY = "currency";
    private final static String KEY_CUSTOMER_EMAIL = "customer_email";
    private final static String KEY_PAYMENT_OPTION = "payment_option";
    private final static String KEY_SADAD_OLP = "sadad_olp";
    private final static String KEY_SADAD_ECI = "eci";
    private final static String KEY_RETURN_URL = "return_url";

    public final static String AUTHORIZATION = "AUTHORIZATION";
    public final static String PURCHASE = "PURCHASE";
    private final static String SDK_TOKEN = "SDK_TOKEN"; //"TOKENIZATION";// "SDK_TOKEN";//;

    //    private final static String TEST_TOKEN_URL = "https://sbcheckout.payfort.com/FortAPI/paymentPage";
//    public final static String LIVE_TOKEN_URLOKEN_URL = "https://checkout.payfort.com/FortAPI/paymentPage";
    private final static String TEST_TOKEN_URL = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi";
    public final static String LIVE_TOKEN_URL = "https://paymentservices.payfort.com/FortAPI/paymentApi";
    private final static String WS_GET_TOKEN = TEST_TOKEN_URL;
    private final static String MERCHANT_IDENTIFIER = "hbfTmAkg";
    private final static String MERCHANT_REFERENCE = "Test010";
    private final static String ACCESS_CODE = "K4OF6bnIVs36IihWoeuy";
    private final static String SHA_TYPE = "SHA-256";
    private final static String SHA_REQUEST_PHRASE = "asdflkjh";//asdflkjh
    public final static String SHA_RESPONSE_PHRASE = "asdadseeerg";//lkjhasdf
    public final static String CURRENCY_TYPE = "SAR";
    public final static String SADAD_PAYMENT_OPTION = "SADAD";
    public final static String SADAD_PAYMENT_ID = "SABBP2P_UAT2";
    public final static String SADAD_PASSWORD = "hsbc1234";
    public final static String SADAD_OTP = "728293";
    public final static String SADAD_ECI = "ECOMMERCE";

    private final Gson gson;
    private Activity context;
    private IPaymentRequestCallBack iPaymentRequestCallBack;

    private FortCallBackManager fortCallback = null;
    private ProgressDialog progressDialog;
    private String sdkToken;

    private PayFortData payFortData;

    public SadadPayFortPayment(Activity context, FortCallBackManager fortCallback, IPaymentRequestCallBack iPaymentRequestCallBack) {
        this.context = context;
        this.fortCallback = fortCallback;
        this.iPaymentRequestCallBack = iPaymentRequestCallBack;

        progressDialog = new ProgressDialog(context);
        //progressDialog.setMessage("Processing your payment\nPlease wait...");
        progressDialog.setMessage(context.getResources().getString(R.string.processing_your_payment) + "\n" + context.getResources().getString(R.string.please_wait_));
        progressDialog.setCancelable(false);
        sdkToken = "";
        gson = new Gson();
    }

    public void requestForPayment(PayFortData payFortData) {
        this.payFortData = payFortData;
//        new GetTokenFromServer().execute(WS_GET_TOKEN);
        requestForToken();
    }

    private void requestPurchase() {
//        try {
//            FortSdk.getInstance().registerCallback(context, getPurchaseFortRequest(), FortSdk.ENVIRONMENT.TEST, 2112,
//                    fortCallback, true,new FortInterfaces.OnTnxProcessed() {
//                        @Override
//                        public void onCancel(Map<String, String> requestParamsMap, Map<String,
//                                String> responseMap) {
//                            JSONObject response = new JSONObject(responseMap);
//                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
//                            payFortData.paymentResponse = response.toString();
//                            Log.e("Cancel Response", response.toString());
//                            if (iPaymentRequestCallBack != null) {
//                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_CANCEL, payFortData);
//                            }
//                        }
//
//                        @Override
//                        public void onSuccess(Map<String, String> requestParamsMap, Map<String,
//                                String> fortResponseMap) {
//                            JSONObject response = new JSONObject(fortResponseMap);
//                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
//                            payFortData.paymentResponse = response.toString();
//                            Log.e("Success Response", response.toString());
//                            if (iPaymentRequestCallBack != null) {
//                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_SUCCESS, payFortData);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Map<String, String> requestParamsMap, Map<String,
//                                String> fortResponseMap) {
//                            JSONObject response = new JSONObject(fortResponseMap);
//                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
//                            payFortData.paymentResponse = response.toString();
//                            Log.e("Failure Response", response.toString());
//                            if (iPaymentRequestCallBack != null) {
//                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_FAILURE, payFortData);
//                            }
//                        }
//                    });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private FortRequest getPurchaseFortRequest() {
        FortRequest fortRequest = new FortRequest();
        if (payFortData != null) {
            HashMap<String, String> parameters = new HashMap<>();

            parameters.put(KEY_COMMAND, PayFortPayment.PURCHASE);
            parameters.put(KEY_ACCESS_CODE, ACCESS_CODE);
            parameters.put(KEY_MERCHANT_IDENTIFIER, MERCHANT_IDENTIFIER);
            parameters.put(KEY_MERCHANT_REFERENCE, payFortData.merchantReference);
            parameters.put(KEY_AMOUNT, String.valueOf(payFortData.amount));
            parameters.put(KEY_CURRENCY, PayFortPayment.CURRENCY_TYPE);
            parameters.put(KEY_LANGUAGE, "en");
            parameters.put(KEY_CUSTOMER_EMAIL, payFortData.customerEmail);
//            parameters.put(KEY_SIGNATURE, getSADADSignature());
            parameters.put(KEY_TOKEN_NAME, sdkToken);
            parameters.put(KEY_PAYMENT_OPTION, SADAD_PAYMENT_OPTION);
            parameters.put(KEY_SADAD_OLP, SADAD_PAYMENT_ID);
            parameters.put(KEY_SADAD_ECI, SADAD_ECI);
            parameters.put(KEY_RETURN_URL, "https://www.weekend.sa/payment-ipn-return.html");
            //parameters.put("submit", "Pay");

            fortRequest.setRequestMap(parameters);
        }
        return fortRequest;
    }

    private class GetTokenFromServer extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... postParams) {
            String response = "";
            try {
                HttpURLConnection conn;
                URL url = new URL(postParams[0].replace(" ", "%20"));
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("content-type", "application/json");

                String str = getTokenParams();
                byte[] outputInBytes = str.getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputInBytes);
                os.close();

                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = conn.getInputStream();
                    response = convertStreamToString(inputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.hide();
            if (!TextUtils.isEmpty(response)) {
                try {
                    PayFortData payFortData = gson.fromJson(response, PayFortData.class);
                    if (!TextUtils.isEmpty(payFortData.sdkToken)) {
                        sdkToken = payFortData.sdkToken;
                        requestPurchase();
                    } else {
                        payFortData.paymentResponse = response;
                        iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_GET_TOKEN, payFortData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getSADADSignature() {
//        String concatenatedString = SHA_REQUEST_PHRASE +
//                KEY_ACCESS_CODE + "=" + ACCESS_CODE +
//                KEY_AMOUNT + "=" + String.valueOf(payFortData.amount) +
//                KEY_COMMAND + "=" + PURCHASE +
//                KEY_CURRENCY + "=" + PayFortPayment.CURRENCY_TYPE +
//                KEY_CUSTOMER_EMAIL + "=" + payFortData.customerEmail +
//                //KEY_DEVICE_ID + "=" + FortSdk.getDeviceId(context) +
//                KEY_LANGUAGE + "=" + "en" +
//                KEY_MERCHANT_IDENTIFIER + "=" + MERCHANT_IDENTIFIER +
//                KEY_MERCHANT_REFERENCE + "=" + payFortData.merchantReference +
//                SHA_REQUEST_PHRASE;
//        return getSignatureSHA256(concatenatedString);
        String device_id = FortSdk.getDeviceId(context);
        String concatenatedString = SHA_REQUEST_PHRASE +
                KEY_ACCESS_CODE + "=" + ACCESS_CODE +
                KEY_DEVICE_ID + "=" + device_id +
                KEY_LANGUAGE + "=" + "ar" +
                KEY_MERCHANT_IDENTIFIER + "=" + MERCHANT_IDENTIFIER +
                KEY_SERVICE_COMMAND + "=" + SDK_TOKEN +
                SHA_REQUEST_PHRASE;
        return getSignatureSHA256(concatenatedString);
    }

    public String getTokenParams() {
        JSONObject jsonObject = new JSONObject();
        try {
            String device_id = FortSdk.getDeviceId(context);
            /*service_command = TOKENIZATION
            language = en
            merchant_identifier = MxvOupuG
            access_code = SILgpo7pWbmzuURp2qri
            merchant_reference = MyReference0001
            card_security_code = 123
            card_number = 4005550000000001
            expiry_date = 1705
            remember_me = YES
            card_holder_name = John Smith*/

            String concatenatedString = SHA_REQUEST_PHRASE +
                    KEY_ACCESS_CODE + "=" + ACCESS_CODE +
                    KEY_DEVICE_ID + "=" + device_id +
                    KEY_LANGUAGE + "=" + "en" +
                    KEY_MERCHANT_IDENTIFIER + "=" + MERCHANT_IDENTIFIER +
                    KEY_MERCHANT_REFERENCE + "=" + payFortData.merchantReference +
                    KEY_SERVICE_COMMAND + "=" + SDK_TOKEN +
                    SHA_REQUEST_PHRASE;
            String signature = getSignatureSHA256(concatenatedString);
            jsonObject.put(KEY_SERVICE_COMMAND, SDK_TOKEN);
            jsonObject.put(KEY_LANGUAGE, "en");
            jsonObject.put(KEY_MERCHANT_IDENTIFIER, MERCHANT_IDENTIFIER);
            jsonObject.put(KEY_ACCESS_CODE, ACCESS_CODE);
            jsonObject.put(KEY_MERCHANT_REFERENCE, payFortData.merchantReference);
            jsonObject.put(KEY_CARD_SECURITY_CODE, "123");
            jsonObject.put(KEY_CARD_NUMBER, "4005550000000001");
            jsonObject.put(KEY_EXPIRY_DATE, "2105");
            jsonObject.put(KEY_SIGNATURE, signature);
            jsonObject.put(KEY_DEVICE_ID, device_id);
            Log.e("concatenatedString", concatenatedString);
            Log.e("signature", signature);
            Log.e("TokenParams", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JsonString", String.valueOf(jsonObject));
        return String.valueOf(jsonObject);
    }

    private static String convertStreamToString(InputStream inputStream) {
        if (inputStream == null)
            return null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream), 1024);
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Converting inputStream to string to see what response we are getting
     *
     * @param is (InputStream)
     * @return String as a response after inputStream convert
     */
    private static String convertStreamToString1(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private static String getSignatureSHA256(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(SHA_TYPE);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return String.format("%0" + (messageDigest.length * 2) + 'x', new BigInteger(1, messageDigest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void requestForToken() {
        progressDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_DEVICE_ID, FortSdk.getDeviceId(context));
        //params.put(WSUtils.KEY_LANG_ID, context.getString(R.string.lang_id));
        params.put(WSUtils.KEY_LANG_ID, "en");
        WSUtils wsUtils = new WSFactory().getWsUtils(WSFactory.WSType.WS_PAYFORT_TOKEN_RETURN);
        wsUtils.WSRequest(context, params, null, REQ_PAYFORT_TOKEN_RETURN, this);
    }

    @Override
    public void successResponse(int requestCode, WSResponse response) {
        switch (requestCode) {
            case REQ_PAYFORT_TOKEN_RETURN:
                parseToken((PayfortToken) response);
                break;
        }
    }

    @Override
    public void errorResponse(int requestCode, Throwable t) {
        progressDialog.dismiss();
    }

    @Override
    public void noInternetConnection(int requestCode) {

    }

    private void parseToken(PayfortToken response) {
        progressDialog.dismiss();
        if (response != null && response.getSettings().getSuccess().equalsIgnoreCase("1")) {
            String result = response.getData().get(0).getResult();
            if (!TextUtils.isEmpty(result)) {
                try {
                    PayFortData payFortData = gson.fromJson(result, PayFortData.class);
                    if (!TextUtils.isEmpty(payFortData.sdkToken)) {
                        sdkToken = payFortData.sdkToken;
                        requestPurchase();
                    } else {
                        payFortData.paymentResponse = result;
                        iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_GET_TOKEN, payFortData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
//            CommonUtil.showSnackbar(((HomeActivity) context).ivNavbarFilter, response.getSettings().getMessage());
            CommonUtil.showShortTimeToast(context, response.getSettings().getMessage());
            ((HomeActivity) context).popBack(true);
        }
    }
}
