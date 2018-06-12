package com.weekend.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.users.model.QBUser;
import com.weekend.R;
import com.weekend.base.WeekendApplication;
import com.weekend.models.AdminUsersModel;
import com.weekend.utils.imageUtils.ScalingUtilities;

import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaticUtil {
    public static final int PROFILE_IMAGE_SIZE = 300;

    public static final String PROPERTY_OCCUPANTS_IDS = "occupants_ids";
    public static final String PROPERTY_DIALOG_TYPE = "dialog_type";
    public static final String PROPERTY_DIALOG_NAME = "dialog_name";
    public static final String PROPERTY_NOTIFICATION_TYPE = "notification_type";
    public static final String CREATING_DIALOG = "creating_dialog";
    public static final String DELETING_DIALOG = "deleting_dialog";
    public static final String UPDATING_DIALOG = "updating_dialog";

    public static final String DIALOG_BROADCAST = "DIALOG_SYNC_COMPLETE";
    public static final String CHAT_BROADCAST = "CHAT_SYNC_COMPLETE";
    public static final String NEWMESSAGE_RECEIVE = "com.customer.weekend.intent.RECEIVE";
    public static final String DIALOG_CREATED = "com.customer.weekend.intent.DIALOG_CREATED";
    public static final String DIALOG_DELETED = "com.customer.weekend.intent.DIALOG_DELETED";
    public static final String DIALOG_UPDATE = "com.customer.weekend.intent.DIALOG_UPDATE";

    public static final String ADMIN_USERS_TAG = "admin";

    private static final int IMAGE_SAMPLE_SIZE = 4;
    public static final int PAGINATION_AND_LIMIT_QB_MESSAGES = 100;

    public static String generateRandomLoginQb() {
        return "WCust_" + Calendar.getInstance().getTimeInMillis();
    }

    public static String getWeekendCustomerPasswordQb() {
        return "WCust_P_" + "ba3ec4a347d03f0808fefbf50f3c980f";
    }

    public static int dpToPx(Context context, int dp) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String decodeBase64(String encodedString) throws UnsupportedEncodingException {
        return new String(Base64.decode(encodedString, Base64.DEFAULT), "UTF-8");
    }

    public static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    public static Bitmap getImageFromCamera(Context mContext, Uri IMAGE_CAPTURE_URI) {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = IMAGE_SAMPLE_SIZE;
        options.inJustDecodeBounds = false;

        if (manufacturer.equalsIgnoreCase("samsung") || model.equalsIgnoreCase("samsung")) {
            int rotation = getCameraPhotoOrientation(mContext, IMAGE_CAPTURE_URI, IMAGE_CAPTURE_URI.getPath());
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            final Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.getPath(), options);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return BitmapFactory.decodeFile(IMAGE_CAPTURE_URI.getPath(), options);
        }
    }

    private static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            try {
                if (imageUri != null)
                    context.getContentResolver().notifyChange(imageUri, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static final String storageDir = Environment.getExternalStorageDirectory().getPath() + "/" + "WeekendCustomer" + "/";

    public static boolean hasAttachments(QBChatMessage chatMessage) {
        Collection<QBAttachment> attachments = chatMessage.getAttachments();
        return attachments != null && !attachments.isEmpty();
    }

    private static File getOutputMediaFile() {
        String imageFileName = "QBSample" + "_" + System.currentTimeMillis();
        File image;
        File storageDirFile = new File(storageDir);
        if (!storageDirFile.exists()) {
            storageDirFile.mkdirs();
        }
        if (storageDirFile.exists()) {
            image = new File(storageDirFile, imageFileName + ".jpeg");
            return image;
        }
        return null;
    }

    public static File bitmapToFile(Bitmap bitmap) {
        try {
            File file = null;
            try {
                file = getOutputMediaFile();
                if (file != null) {
                    FileOutputStream fOut;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                    fOut.flush();
                    fOut.close();
                } else {
                    return null;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizeImage(Context context, int dstWidth, int dstHeight, ScalingUtilities.ScalingLogic scalingLogic, boolean rotationNeeded, String currentPhotoPath, Uri IMAGE_CAPTURE_URI) {
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            bmOptions.inJustDecodeBounds = false;
            if (bmOptions.outWidth < dstWidth && bmOptions.outHeight < dstHeight) {
                Bitmap bitmap;
                bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                return setSelectedImage(bitmap, context, currentPhotoPath, IMAGE_CAPTURE_URI);
            } else {
                Bitmap unscaledBitmap = ScalingUtilities.decodeResource(currentPhotoPath, dstWidth, dstHeight, scalingLogic);
                Matrix matrix = new Matrix();
                if (rotationNeeded) {
                    matrix.setRotate(getCameraPhotoOrientation(context, Uri.fromFile(new File(currentPhotoPath)), currentPhotoPath));
                    unscaledBitmap = Bitmap.createBitmap(unscaledBitmap, 0, 0, unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                            matrix, false);
                }
                return unscaledBitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap setSelectedImage(Bitmap orignalBitmap, Context context, String imagePath, Uri IMAGE_CAPTURE_URI) {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (manufacturer.equalsIgnoreCase("samsung") || model.equalsIgnoreCase("samsung")) {
                return rotateBitmap(context, orignalBitmap, imagePath, IMAGE_CAPTURE_URI);
            } else {
                return orignalBitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return orignalBitmap;
        }

    }

    public static Bitmap rotateBitmap(Context context, Bitmap bit, String imagePath, Uri IMAGE_CAPTURE_URI) {
        int rotation = getCameraPhotoOrientation(context, IMAGE_CAPTURE_URI, imagePath);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(bit, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
    }

    public static int pxToDp(Context context, int px) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return dp;
        } catch (Exception e) {
        }
        return 0;
    }

    public static float pixelsToSp(Context context, Float px) {
        try {
            float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
            return px / scaledDensity;
        } catch (Exception e) {
        }
        return 0f;
    }

    public static int spToPixels(Context context, int sp) {
        try {
            float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
            return (int) (sp * scaledDensity);
        } catch (Exception e) {
        }
        return 0;
    }

    public static void showKeyboard(View view, Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity act) {
        try {
            if (act.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
        }
    }

    public static void shareIntent(Context context, String text, String imagePath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (!TextUtils.isEmpty(text))
            intent.putExtra(Intent.EXTRA_TEXT, text);
        if (!TextUtils.isEmpty(imagePath)) {
            Uri uri = Uri.fromFile(new File(imagePath));
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        context.startActivity(intent);
    }

    public static void sharingIntent(Context context, String body) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, fromHtml(body));
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, fromHtml(body));
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_with)));
    }

    @NonNull
    public static String fromHtml(@NonNull String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return String.valueOf(Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return String.valueOf(Html.fromHtml(source));
        }
    }

    public static void copyToClipboard(Context context, String textToCopy) {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", textToCopy);
            clipboard.setPrimaryClip(clip);
        } else {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(textToCopy);
        }
    }

    /**
     * This method is used set window dimensions
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void setWindowDimensions(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Constants.SCREEN_WIDTH = size.x;
        Constants.SCREEN_HEIGHT = size.y;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNo, int maxLength) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
        //validate phone numbers of format "1234567"
        if (phoneNo.matches("\\d{9}")) return true;
            //validating phone number with -, . or spaces
            //else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
            //else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
            //else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;
    }

    public static String getMD51(String string) {
      /*  String result = "";
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(string.getBytes(Charset.forName("UTF8")));
            final byte[] resultByte = messageDigest.digest();
            result = new String(Hex.encodeHex(resultByte));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
        return string;
    }

    public static void setHeightToSpinner(Spinner spinner, int height) {
        if (spinner != null) {
            try {
                Field popup = Spinner.class.getDeclaredField("mPopup");
                popup.setAccessible(true);

                // Get private mPopup member variable and try cast to ListPopupWindow
                android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

                // Set popupWindow height to 500px
                popupWindow.setHeight(height);
            } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getYoutubeThumbnailUrlFromVideoUrl(String videoUrl) {
        return "http://img.youtube.com/vi/" + getYoutubeVideoIdFromUrl(videoUrl) + "/0.jpg";
    }

    public static String getYoutubeVideoIdFromUrl(String inUrl) {
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1);
        }
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(inUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static void changeLocaleConfiguration(Context context, String lang) {
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static String getTime(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.US);
        return dateFormat.format(new Date(milliseconds));
    }

    public static String getFormattedDate(long milliseconds) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(milliseconds);
        Calendar now = Calendar.getInstance();
        final String timeFormatString = "hh:mm aa";
        final String dateTimeFormatString = "EEEE, MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd MMMM yyyy, hh:mm aa", smsTime).toString();
        }
    }

    public static String getDisplayTimeInChatList(long milliseconds) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(milliseconds);
        Calendar now = Calendar.getInstance();
        final String timeFormatString = "hh:mm aa";
        final String dateTimeFormatString = "dd/MM/yyyy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return DateFormat.format(timeFormatString, smsTime).toString();
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday";
        } else {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        }
    }

    public static String getDate(long milliseconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd", Locale.US);
        return dateFormat.format(new Date(milliseconds));
    }

    public static void sendSystemMessageAboutCreatingDialog(QBSystemMessagesManager systemMessagesManager, QBChatDialog dialog) {
        QBChatMessage systemMessageCreatingDialog = buildSystemMessageAboutCreatingGroupDialog(dialog);
        try {
            for (Integer recipientId : dialog.getOccupants()) {
                if (!recipientId.equals(QBChatService.getInstance().getUser().getId())) {
                    systemMessageCreatingDialog.setRecipientId(recipientId);
                    systemMessagesManager.sendSystemMessage(systemMessageCreatingDialog);
                }
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public static void sendSystemMessageAboutUpdatingDialog(QBSystemMessagesManager systemMessagesManager, QBChatDialog dialog) {
        QBChatMessage systemMessageCreatingDialog = buildSystemMessageAboutUpdatingGroupDialog(dialog);
        try {
            for (Integer recipientId : dialog.getOccupants()) {
                if (!recipientId.equals(QBChatService.getInstance().getUser().getId())) {
                    systemMessageCreatingDialog.setRecipientId(recipientId);
                    systemMessagesManager.sendSystemMessage(systemMessageCreatingDialog);
                }
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    public static void sendSystemMessageAboutDeletingDialog(QBSystemMessagesManager systemMessagesManager, QBChatDialog dialog) {
        QBChatMessage systemMessageCreatingDialog = buildSystemMessageAboutDeletingGroupDialog(dialog);
        try {
            for (Integer recipientId : dialog.getOccupants()) {
                if (!recipientId.equals(QBChatService.getInstance().getUser().getId())) {
                    systemMessageCreatingDialog.setRecipientId(recipientId);
                    systemMessagesManager.sendSystemMessage(systemMessageCreatingDialog);
                }
            }
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }

    private static QBChatMessage buildSystemMessageAboutCreatingGroupDialog(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, TextUtils.join(",", dialog.getOccupants()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, String.valueOf(dialog.getType().getCode()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, String.valueOf(dialog.getName()));
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, CREATING_DIALOG);
        return qbChatMessage;
    }

    private static QBChatMessage buildSystemMessageAboutUpdatingGroupDialog(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_OCCUPANTS_IDS, TextUtils.join(",", dialog.getOccupants()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_TYPE, String.valueOf(dialog.getType().getCode()));
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, String.valueOf(dialog.getName()));
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, UPDATING_DIALOG);
        return qbChatMessage;
    }

    private static QBChatMessage buildSystemMessageAboutDeletingGroupDialog(QBChatDialog dialog) {
        QBChatMessage qbChatMessage = new QBChatMessage();
        qbChatMessage.setDialogId(dialog.getDialogId());
        qbChatMessage.setProperty(PROPERTY_DIALOG_NAME, String.valueOf(dialog.getName()));
        qbChatMessage.setProperty(PROPERTY_NOTIFICATION_TYPE, DELETING_DIALOG);
        return qbChatMessage;
    }

    public static boolean isCurrentUserADmin() {
        QBUser currentUser = WeekendApplication.loggedInQBUser;
        return currentUser != null && currentUser.getTags().contains(ADMIN_USERS_TAG);
    }

    private boolean isBlankMessage(final QBChatMessage qbChatMessage) {
        return TextUtils.isEmpty(qbChatMessage.getBody()) && qbChatMessage.getAttachments().size() <= 0;
    }

    public static String getImageUrl(String customData) {
        try {
            return new JSONObject(customData).getString("pic_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getProfilePic(int senderId, ArrayList<AdminUsersModel> adminUsersModelArrayList) {
        for (int i = 0; i < adminUsersModelArrayList.size(); i++) {
            if (senderId == adminUsersModelArrayList.get(i).getQbUser().getId()) {
                try {
                    return new JSONObject(adminUsersModelArrayList.get(i).getQbUser().getCustomData()).getString("pic_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
