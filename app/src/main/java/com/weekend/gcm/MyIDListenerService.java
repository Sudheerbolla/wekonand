package com.weekend.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.messages.QBPushNotifications;
import com.quickblox.messages.model.QBEnvironment;
import com.quickblox.messages.model.QBNotificationChannel;
import com.quickblox.messages.model.QBSubscription;
import com.weekend.R;
import com.weekend.server.IParser;
import com.weekend.server.WSFactory;
import com.weekend.server.WSResponse;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MyIDListenerService extends IntentService {

    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    private static final String[] TOPICS = {"global"};

    //Class constructor
    public MyIDListenerService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Registering gcm to the device
        registerGCM();
    }

    private void registerGCM() {
        //Registration complete intent initially null
        Intent registrationComplete = null;

        //Register token is also null
        //we will get the token on successfull registration
        String token = null;
        try {
            //Creating an instanceId
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            //Getting the token from the instance id
            token = instanceID.getToken(getString(R.string.sender_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //Displaying the token in the log so that we can copy it to send push notification
            //You can also extend the app by storing the token in to your server
            Log.w("GCMRegIntentService", "token:" + token);
            sendRegistrationToServer(token);

            //on registration complete creating intent with success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);

            //Putting the token to the intent
            registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            //If any error occurred
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        Log.e("Registration Token", token);
        LocalStorage.getInstance(getApplicationContext()).putString(Constants.SP_KEY_GCM_REGISTRATION_ID, token);
        if (!LocalStorage.getInstance(this).getString(Constants.SP_KEY_USER_ID, "").isEmpty()) {
            requestSaveDeviceToken(token);
        }
        subscribeToPush();
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

    private void requestSaveDeviceToken(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put(WSUtils.KEY_USER_ID, LocalStorage.getInstance(this).getString(Constants.SP_KEY_USER_ID, ""));
        params.put(WSUtils.KEY_DEVICE_TOKEN, token);
        params.put(WSUtils.KEY_LANG_ID, getString(R.string.lang_id));
        params.put(WSUtils.KEY_TOKEN, LocalStorage.getInstance(this).getString(Constants.SP_KEY_TOKEN, ""));
        WSUtils wsUtils = new WSFactory().getWsUtils(WSFactory.WSType.WS_DEVICE_TOKEN_SAVE);
        wsUtils.WSRequest(this, params, null, WSUtils.REQ_CUSTOMER_LOGIN, new IParser<WSResponse>() {
            @Override
            public void successResponse(int requestCode, WSResponse response) {
                Log.e("Token Saved", "true");
                if (QBChatService.getInstance().isLoggedIn())
                    subscribeToPush();
            }

            @Override
            public void errorResponse(int requestCode, Throwable t) {
                Log.e("Token Saved", "false");
            }

            @Override
            public void noInternetConnection(int requestCode) {

            }
        });
    }

    private void subscribeToPush() {
        final TelephonyManager mTelephony = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = mTelephony.getDeviceId() != null ? mTelephony.getDeviceId() : Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("GCM_REGISTRATION_ID", LocalStorage.getInstance(this).getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        QBSubscription subscription = new QBSubscription();
        subscription.setNotificationChannel(QBNotificationChannel.GCM);
        subscription.setDeviceUdid(deviceId);
        subscription.setRegistrationID(LocalStorage.getInstance(this).getString(Constants.SP_KEY_GCM_REGISTRATION_ID, ""));
        subscription.setEnvironment(QBEnvironment.PRODUCTION);
        QBPushNotifications.createSubscription(subscription).performAsync(new QBEntityCallback<ArrayList<QBSubscription>>() {
            @Override
            public void onSuccess(ArrayList<QBSubscription> subscriptions, Bundle args) {
                Log.e("sudheer", "QBPushNotifications onSuccess");
            }

            @Override
            public void onError(QBResponseException error) {
                Log.e("sudheer", "QBPushNotifications onError " + error.toString());
            }
        });
    }
}
