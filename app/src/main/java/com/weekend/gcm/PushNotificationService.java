package com.weekend.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.gcm.GcmListenerService;
import com.weekend.HomeActivity;
import com.weekend.R;
import com.weekend.qbutils.ChatService;
import com.weekend.server.factory.WSUtils;
import com.weekend.storage.LocalStorage;
import com.weekend.utils.Constants;
import com.weekend.utils.Log;

public class PushNotificationService extends GcmListenerService {

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String navigateTo = "";
        Log.e("onMessageReceived", data.toString());
        if (data.containsKey("nt")) {
            Intent mIntent = new Intent(getApplicationContext(), HomeActivity.class);
            PushNotificationModel notificationModel = new PushNotificationModel();
            if (data.containsKey(WSUtils.KEY_USER_ID)) {
                notificationModel.userId = data.getString(WSUtils.KEY_USER_ID);
            }
            if (data.containsKey(WSUtils.KEY_ORDER_ID)) {
                notificationModel.orderId = data.getString(WSUtils.KEY_ORDER_ID);
            }
            if (data.containsKey(WSUtils.KEY_TOKEN)) {
                notificationModel.token = data.getString(WSUtils.KEY_TOKEN);
            }
            if (data.containsKey(WSUtils.KEY_PROPERTY_TYPE_ID)) {
                notificationModel.propertyTypeId = data.getString(WSUtils.KEY_PROPERTY_TYPE_ID);
            }
            if (data.containsKey(WSUtils.KEY_PROPERTY_ID)) {
                notificationModel.propertyId = data.getString(WSUtils.KEY_PROPERTY_ID);
            }

            if (data.getString("nt").equalsIgnoreCase(Constants.CUSTOMER_ACCOUNT_ACTIVATE)) {
                navigateTo = Constants.CUSTOMER_ACCOUNT_ACTIVATE_FRAGMENT;
            } else if (data.getString("nt").equalsIgnoreCase(Constants.BOOKING_CONFIRMED) ||
                    data.getString("nt").equalsIgnoreCase(Constants.CUSTOMER_CANCELLED_BOOKING)) {
                if (data.getString(WSUtils.KEY_PROPERTY_TYPE_ID).equalsIgnoreCase("1")) {
                    navigateTo = Constants.BOOKING_CONFIRMED_CANCELLED_CHALET_FRAGMENT;
                } else {
                    navigateTo = Constants.BOOKING_CONFIRMED_CANCELLED_SOCCER_FRAGMENT;
                }
            } else if (data.getString("nt").equalsIgnoreCase(Constants.PROPERTY_OWNER_REPLY_COMMENT)) {
                navigateTo = Constants.PROPERTY_OWNER_REPLY_COMMENT_FRAGMENT;
            }
            Bundle notificatioBundel = new Bundle();
            notificatioBundel.putString("navigate_to", navigateTo);
            notificatioBundel.putBoolean("isFromApp", false);
            notificatioBundel.putString("Message", data.getString("message"));
            notificatioBundel.putSerializable("push_notification", notificationModel);
            mIntent.putExtras(notificatioBundel);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            if (!TextUtils.isEmpty(LocalStorage.getInstance(getApplicationContext()).getString(Constants.SP_KEY_USER_ID, ""))) {
                createNotification(mIntent, getApplicationContext(), data.getString("message"), data.containsKey("notification_title")
                        ? data.getString("notification_title") : getString(R.string.app_name), (int) System.currentTimeMillis());
            }
        }

// 467675705579
// Bundle
// [
// {
// google.sent_time=1521723756567,
// google.ttl=86400,
// google.message_id=0:1521723756576241%de3abb24f9fd7ecd,
// message=asdfghj,
// collapse_key=event23173382
// }
// ]

        else if (data.containsKey("message") && data.containsKey("google.message_id")) {
            Intent mIntent = new Intent(getApplicationContext(), HomeActivity.class);
            Bundle notificatioBundel = new Bundle();
            notificatioBundel.putString("message", data.getString("message"));
            notificatioBundel.putString("google.message_id", data.getString("google.message_id"));
            notificatioBundel.putBoolean("isFromChat", true);
            mIntent.putExtras(notificatioBundel);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            createNotification(mIntent, getApplicationContext(), data.getString("message"), getString(R.string.app_name),/*Integer.parseInt(data.getString("message"))*/(int) System.currentTimeMillis());
            startService(new Intent(this, ChatService.class));
        }
    }

    private void createNotification(Intent intent, final Context context, final String message, String title, int id) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_app_launcher);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_app_launcher);
        builder.setLargeIcon(image);
        builder.setStyle(new android.support.v7.app.NotificationCompat.BigTextStyle().bigText(message));
        builder.setColor(context.getResources().getColor(R.color.colorPrimary));
        builder.setContentIntent(contentIntent).setAutoCancel(true).build();
        builder.setContentTitle(title);
        builder.setContentText(message);
//        builder.setTicker(message);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}
