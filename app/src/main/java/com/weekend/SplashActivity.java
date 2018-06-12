package com.weekend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.weekend.gcm.MyIDListenerService;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.CommonUtil;
import com.weekend.utils.Constants;
import com.weekend.utils.Log;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    public final static String TAG = SplashActivity.class.getSimpleName();

    @Bind(R.id.rl_main_splash)
    RelativeLayout rlMain;

    private Handler mLoadingHandler;
    private Runnable mLoadingRunnable;
    private LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(MyIDListenerService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(MyIDListenerService.REGISTRATION_ERROR));
        initialize();
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop");
        super.onStop();
        if (mLoadingHandler != null && mLoadingRunnable != null) {
            mLoadingHandler.removeCallbacks(mLoadingRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void initialize() {
        Log.e(TAG, "initialize");
        localStorage = LocalStorage.getInstance(this);
        initPushNotifications();
        loading();
    }

    private void initPushNotifications() {
        Log.e(TAG, "initPushNotifications");
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (ConnectionResult.SUCCESS != resultCode) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                CommonUtil.showSnackbar(rlMain, "Google Play Service is not install/enabled in this device!");
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                CommonUtil.showSnackbar(rlMain, "This device does not support for Google Play Service!");
            }
        } else {
            Intent itent = new Intent(this, MyIDListenerService.class);
            startService(itent);
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
                loading();
            } else {
                loading();
                CommonUtil.showSnackbar(rlMain, "GCM registration error!");
            }
        }
    };

    private void loading() {
        Log.e(TAG, "loading");
        mLoadingHandler = new Handler();
        mLoadingRunnable = new Runnable() {
            @Override
            public void run() {
                navigateToHomeActivity();
            }
        };
        mLoadingHandler.postDelayed(mLoadingRunnable, Constants.SPLASH_TIME);
    }

    private void navigateToHomeActivity() {
        Log.e(TAG, "navigateToHomeActivity");
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.putExtra("isFromApp", true);
        CommonUtil.startActivity(SplashActivity.this, intent, true, false, true, "RL");
    }
}
