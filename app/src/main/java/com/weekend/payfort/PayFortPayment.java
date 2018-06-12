package com.weekend.payfort;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.sdk.android.dependancies.base.FortInterfaces;
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
import java.util.Map;

import static com.weekend.server.factory.WSUtils.REQ_PAYFORT_TOKEN_RETURN;

public class PayFortPayment implements IParser<WSResponse> {

    public static final int RESPONSE_GET_TOKEN = 111;
    public static final int RESPONSE_PURCHASE = 222;
    public static final int RESPONSE_PURCHASE_CANCEL = 333;
    public static final int RESPONSE_PURCHASE_SUCCESS = 444;
    public static final int RESPONSE_PURCHASE_FAILURE = 555;

    private final static String KEY_MERCHANT_IDENTIFIER = "merchant_identifier";
    private final static String KEY_SERVICE_COMMAND = "service_command";
    private final static String KEY_DEVICE_ID = "device_id";

    private final static String KEY_LANGUAGE = "language";
    private final static String KEY_ACCESS_CODE = "access_code";
    private final static String KEY_SIGNATURE = "signature";

    public final static String AUTHORIZATION = "AUTHORIZATION";
    public final static String PURCHASE = "PURCHASE";
    private final static String SDK_TOKEN = "SDK_TOKEN";

    private final static String TEST_TOKEN_URL = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi";
    public final static String LIVE_TOKEN_URL = "https://paymentservices.payfort.com/FortAPI/paymentApi";
    private final static String WS_GET_TOKEN = TEST_TOKEN_URL;
    private final static String MERCHANT_IDENTIFIER = "hbfTmAkg";
    private final static String ACCESS_CODE = "K4OF6bnIVs36IihWoeuy";
    private final static String SHA_TYPE = "SHA-256";
    private final static String SHA_REQUEST_PHRASE = "asdflkjh";//"asfasdsadee";
    public final static String SHA_RESPONSE_PHRASE = "asdadseeerg";
    public final static String CURRENCY_TYPE = "SAR";

    private final Gson gson;
    private Activity context;
    private IPaymentRequestCallBack iPaymentRequestCallBack;

    private FortCallBackManager fortCallback = null;
    private ProgressDialog progressDialog;
    private String sdkToken;

    private PayFortData payFortData;

    public PayFortPayment(Activity context, FortCallBackManager fortCallback, IPaymentRequestCallBack iPaymentRequestCallBack) {
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

    private void requestForToken() {
        progressDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_DEVICE_ID, FortSdk.getDeviceId(context));
        params.put(WSUtils.KEY_LANG_ID, context.getString(R.string.lang_id));
        WSUtils wsUtils = new WSFactory().getWsUtils(WSFactory.WSType.WS_PAYFORT_TOKEN_RETURN);
        wsUtils.WSRequest(context, params, null, REQ_PAYFORT_TOKEN_RETURN, this);
    }

    public void requestForPayment(PayFortData payFortData) {
        this.payFortData = payFortData;
        //new GetTokenFromServer().execute(WS_GET_TOKEN);
        requestForToken();
    }

    private void requestPurchase() {
        try {
            FortSdk.getInstance().registerCallback(context, getPurchaseFortRequest(), WSUtils.PAYFORT_ENVIRONMENT, 2112,
                    fortCallback, new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map<String, String> requestParamsMap, Map<String,
                                String> fortResponseMap) {
                            JSONObject request = new JSONObject(requestParamsMap);
                            JSONObject response = new JSONObject(fortResponseMap);
                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
                            payFortData.paymentRequest = request.toString();
                            payFortData.paymentResponse = response.toString();
                            Log.e("Cancel Response", response.toString());
                            if (iPaymentRequestCallBack != null) {
                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_CANCEL, payFortData);
                            }
                        }

                        @Override
                        public void onSuccess(Map<String, String> requestParamsMap, Map<String,
                                String> fortResponseMap) {
                            JSONObject request = new JSONObject(requestParamsMap);
                            JSONObject response = new JSONObject(fortResponseMap);
                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
                            payFortData.paymentRequest = request.toString();
                            payFortData.paymentResponse = response.toString();
                            Log.e("Success Response", response.toString());
                            if (iPaymentRequestCallBack != null) {
                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_SUCCESS, payFortData);
                            }
                        }

                        @Override
                        public void onFailure(Map<String, String> requestParamsMap, Map<String,
                                String> fortResponseMap) {
                            JSONObject request = new JSONObject(requestParamsMap);
                            JSONObject response = new JSONObject(fortResponseMap);
                            PayFortData payFortData = gson.fromJson(response.toString(), PayFortData.class);
                            payFortData.paymentRequest = request.toString();
                            payFortData.paymentResponse = response.toString();
                            Log.e("Failure Response", response.toString());
                            if (iPaymentRequestCallBack != null) {
                                iPaymentRequestCallBack.onPaymentRequestResponse(RESPONSE_PURCHASE_FAILURE, payFortData);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FortRequest getPurchaseFortRequest() {
        FortRequest fortRequest = new FortRequest();
        if (payFortData != null) {
            HashMap<String, String> parameters = new HashMap<>();

            parameters.put("amount", String.valueOf(payFortData.amount));
            parameters.put("command", payFortData.command);
            parameters.put("currency", payFortData.currency);
            parameters.put("customer_email", payFortData.customerEmail);
            parameters.put("language", payFortData.language);
            parameters.put("merchant_reference", payFortData.merchantReference);
            parameters.put("sdk_token", sdkToken);

            fortRequest.setRequestMap(parameters);
        }
        return fortRequest;
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
//        CommonUtil.showSnackbar(((HomeActivity) context).ivNavbarFilter, context.getString(R.string.no_internet_connection));
        CommonUtil.showShortTimeToast(context, context.getString(R.string.no_internet_connection));
        progressDialog.dismiss();
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

    public String getTokenParams() {
        JSONObject jsonObject = new JSONObject();
        try {
            String device_id = FortSdk.getDeviceId(context);
            String concatenatedString = SHA_REQUEST_PHRASE +
                    KEY_ACCESS_CODE + "=" + ACCESS_CODE +
                    KEY_DEVICE_ID + "=" + device_id +
                    KEY_LANGUAGE + "=" + "ar" +
                    KEY_MERCHANT_IDENTIFIER + "=" + MERCHANT_IDENTIFIER +
                    KEY_SERVICE_COMMAND + "=" + SDK_TOKEN +
                    SHA_REQUEST_PHRASE;
            String signature = getSignatureSHA256(concatenatedString);
            jsonObject.put(KEY_SERVICE_COMMAND, SDK_TOKEN);
            jsonObject.put(KEY_SIGNATURE, signature);
            jsonObject.put(KEY_MERCHANT_IDENTIFIER, MERCHANT_IDENTIFIER);
            jsonObject.put(KEY_ACCESS_CODE, ACCESS_CODE);
            jsonObject.put(KEY_DEVICE_ID, device_id);
            jsonObject.put(KEY_LANGUAGE, "ar");
            Log.e("concatenatedString", concatenatedString);
            Log.e("signature", signature);
            Log.e("TokenParams", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            //Sample request / response
            /*- The request body is :
                    {"service_command":"SDK_TOKEN",
                    "signature":"5941abb6ba7bff4339de575ae2d4bf4e19c59e1114ceafb23652e18314a58037",
                    "merchant_identifier":"hbfTmAkg" ,
                    "access_code":"K4OF6bnIVs36IihWoeuy",
                    "device_id":"68F33432-58FD-47DA-A91B-285DD7D86D20",
                    "language":"en"}

               - The string to hash used to calculate the signature value is :
                     asdflkjhaccess_code=K4OF6bnIVs36IihWoeuydevice_id=68F33432-58FD-47DA-A91B-285DD7D86D20language=enmerchant_identifier=hbfTmAkgservice_command=SDK_TOKENasdflkjh

               - The response is :
                    {
                     "response_code": "22000",
                     "device_id": "68F33432-58FD-47DA-A91B-285DD7D86D20",
                     "response_message": "Success",
                     "service_command": "SDK_TOKEN",
                     "sdk_token": "47CE2E43B9A305EAE053321E320A7EE4",
                     "signature": "8de56c2d6a6298d27cd583bbb5f3c8bd7825356cd585faca62f107f06d4cb6d8",
                     "merchant_identifier": "hbfTmAkg",
                     "access_code": "K4OF6bnIVs36IihWoeuy",
                     "language": "en",
                     "status": "22"
                    }*/
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

    private static String getSignatureSHA256(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(SHA_TYPE);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return String.format("%0" + (messageDigest.length * 2) + 'x', new BigInteger(1, messageDigest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
