package com.weekend.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.users.model.QBUser;
import com.splunk.mint.Mint;
import com.weekend.R;
import com.weekend.models.AdminUsersModel;
import com.weekend.qbutils.ChatServiceUtil;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.Constants;

import java.util.ArrayList;

/**
 * Created by hb on 7/14/2016.
 */
public class WeekendApplication extends CoreApp {

    public static String selectedFragment;
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    private static WeekendApplication instance;
    public static QBUser loggedInQBUser = null;
    public static ArrayList<AdminUsersModel> adminUsersModelArrayList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Mint.disableNetworkMonitoring();
        Mint.initAndStartSession(this, "06f90749");
        Mint.disableNetworkMonitoring();
        ChatServiceUtil.init(this);
        initAnalytics(this);
        adminUsersModelArrayList = new ArrayList<>();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private static void initAnalytics(Context context) {
        analytics = GoogleAnalytics.getInstance(context);
        analytics.setLocalDispatchPeriod(30);

        tracker = analytics.newTracker(context.getResources().getString(R.string.ga_trackerId));
        tracker.enableAutoActivityTracking(true);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public static void trackScreenView(Context context, String screenName) {
        if (tracker == null) {
            initAnalytics(context);
        }

        // Set screen name.
        tracker.setScreenName(screenName);

        if (!TextUtils.isEmpty(LocalStorage.getInstance(context).getString(Constants.SP_KEY_USER_ID, ""))) {
            tracker.set("&UserID", LocalStorage.getInstance(context).getString(Constants.SP_KEY_USER_ID, ""));
        }

        // Send a screen view.
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(context).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
    public static void trackException(Context context, Exception e) {
        if (tracker == null) {
            initAnalytics(context);
        }

        if (e != null) {
            if (!TextUtils.isEmpty(LocalStorage.getInstance(context).getString(Constants.SP_KEY_USER_ID, ""))) {
                tracker.set("&UserID", LocalStorage.getInstance(context).getString(Constants.SP_KEY_USER_ID, ""));
            }

            tracker.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(context, null).getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public static void trackEvent(Context context, String category, String action, String label) {
        if (tracker == null) {
            initAnalytics(context);
        }

        if (!TextUtils.isEmpty(LocalStorage.getInstance(context).getString(Constants.SP_KEY_USER_ID, ""))) {
            tracker.set("&UserID", LocalStorage.getInstance(context).getString(Constants.SP_KEY_USER_ID, ""));
        }

        // Build and send an Event.
        tracker.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    public static WeekendApplication getInstance() {
        return instance;
    }
}
