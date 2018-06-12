package com.weekend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.weekend.gcm.MyIDListenerService;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.Constants;
import com.weekend.utils.Log;

/**
 * Created by hb on 4/9/17.
 */

public class Splash {
    public final static String TAG = Splash.class.getSimpleName();


    private LocalStorage localStorage;
    private HomeActivity context;

    public Splash(HomeActivity context){
        this.context = context;
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(MyIDListenerService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(MyIDListenerService.REGISTRATION_ERROR));
        initialize();
    }

    public void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mRegistrationBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        Log.e(TAG, "initialize");
        localStorage = LocalStorage.getInstance(context);
        initPushNotifications();
    }

    private void initPushNotifications() {
        Log.e(TAG, "initPushNotifications");
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context.getApplicationContext());
        if (ConnectionResult.SUCCESS != resultCode) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.showErrorNotification(resultCode, context.getApplicationContext());
            } else {
                Toast.makeText(context, "This device does not support for Google Play Service!",Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent itent = new Intent(context, MyIDListenerService.class);
            context.startService(itent);
        }
    }

    BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "mRegistrationBroadcastReceiver");
            if (intent.getAction().equals(MyIDListenerService.REGISTRATION_SUCCESS)) {
                String token = intent.getStringExtra("token");
                localStorage.putString(Constants.SP_KEY_GCM_REGISTRATION_ID, token);
                Log.e("registration_id", token);
            } else {
                Toast.makeText(context, "GCM registration error!",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
