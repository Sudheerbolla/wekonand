package com.weekend.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.weekend.R;
import com.weekend.views.CustomEditText;
import com.weekend.views.CustomTextView;
import com.weekend.views.FontManager;

import java.util.Locale;

/**
 * Created by hb on 7/14/2016.
 */
public class CommonUtil {

    /**
     * to navigate from one activity to another activity
     *
     * @param fromActivity     Activity context from moving
     * @param navigationIntent intent to pass moving activity
     * @param toFinish         true to finish current activity before moving to next else false
     * @param clearStack       true to clear full stack else false
     * @param isAnimation      true to do activity transaction ith animation else false
     * @param animationType    'LR' from Left to Right
     *                         'RL' from Right to Left
     */
    public static void startActivity(Activity fromActivity, Intent navigationIntent, boolean toFinish, boolean clearStack, boolean isAnimation, String animationType) {
        //Clear stack
        if (clearStack) {
            navigationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        fromActivity.startActivity(navigationIntent);

        //Finish current activity
        if (toFinish) {
            fromActivity.finish();
        }

        //Animation
        if (isAnimation) {
            if (animationType.equalsIgnoreCase("RL")) {
                fromActivity.overridePendingTransition(R.animator.enter_from_right, R.animator.exit_to_left);
            } else if (animationType.equalsIgnoreCase("LR")) {
                fromActivity.overridePendingTransition(R.animator.enter_from_left, R.animator.exit_to_right);
            }
        }
    }

    public static void configureLocale(Context context) {
        //Java Locale “Arabic (Saudi Arabia)” (ar-SA)
        Locale locale = new Locale("SA");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    /**
     * @param container
     * @param message
     * @return
     */
    public static Snackbar snackbar(View container, String message) {
        Snackbar snackbar = Snackbar.make(container, message, Snackbar.LENGTH_SHORT);

        //Snackbar comes with default white color text and #323232 background color. You can override these colors as mentioned below.

        // Changing background color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(container.getResources().getColor(R.color.color_35_120_201));

        // Changing message text color
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        // Changing action button text color
        snackbar.setActionTextColor(Color.RED);

        return snackbar;
    }

    public static void showSnackbar(View container, String message) {
        try {
            Snackbar snackbar = snackbar(container, message);
            snackbar.show();
        } catch (Exception e) {
        }
    }

    public static void showSnackbarWithAction(View container, String message, String action, final View.OnClickListener actionListener) {
        Snackbar snackbar = snackbar(container, message);
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (actionListener != null) {
                    actionListener.onClick(view);
                }
            }
        });
        snackbar.show();
    }

    public static void showShortTimeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setTypefacerb(Context context, RadioButton radioButton, int fontStyle) {
        String str = null;
        switch (fontStyle) {
            case 1:
                str = "fonts/AvenirNextLTPro_Bold_0.otf";
                break;
            case 2:
                str = "fonts/AvenirNextLTPro_Demi_0.otf";
                break;
            case 3:
                str = "fonts/AvenirNextLTPro_DemiIt_0.otf";
                break;
            case 4:
                str = "fonts/AvenirNextLTPro_It_0.otf";
                break;
            case 5:
                str = "fonts/AvenirNextLTPro_Medium.otf";
                break;
            case 6:
                str = "fonts/AvenirNextLTPro_Regular_0.otf";
                break;
            default:
                str = "fonts/AvenirNextLTPro_Regular_0.otf";
                break;
        }
        if (radioButton != null)
            radioButton.setTypeface(FontManager.getInstance(context).loadFont(str));
    }

    public static void changeColorOfString(Context context, CustomTextView textView, String message, String stringToChangeColor, boolean isFontChange, int... rgb) {
        int index = message.indexOf(stringToChangeColor, 0);
        if (index >= 0) {
            SpannableString res = new SpannableString(message);

            //Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNextLTPro_Medium.otf");
            final ForegroundColorSpan color = new ForegroundColorSpan(Color.rgb(rgb[0], rgb[1], rgb[2]));
            if (isFontChange) {
                //res.setSpan(font, index, index + stringToChangeColor.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                res.setSpan(new StyleSpan(Typeface.BOLD), index, index + stringToChangeColor.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                res.setSpan(color, index, index + stringToChangeColor.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(res);
        }
    }

    public static void changeTimeString(Context context, CustomTextView textView, String message, String stringToChangeColor, boolean isFontChange, int... rgb) {
        int index = message.indexOf(stringToChangeColor, 0);
        if (index >= 0) {
            SpannableString res = new SpannableString(message);

            //Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNextLTPro_Medium.otf");
            final ForegroundColorSpan color = new ForegroundColorSpan(Color.rgb(rgb[0], rgb[1], rgb[2]));
            if (isFontChange) {
                //res.setSpan(font, index, index + stringToChangeColor.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                res.setSpan(new StyleSpan(Typeface.BOLD), index, index + stringToChangeColor.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                res.setSpan(color, index, index + stringToChangeColor.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            res = new SpannableString(" " + res.toString().replace(stringToChangeColor, "-"));
            textView.setText(res);
        }
    }

    public static String insertComma(String... commaString) {
        String commaValue = "";
        if (commaString != null) {
            for (String comma : commaString) {
                if (!TextUtils.isEmpty(comma)) {
                    commaValue = comma + ", " + commaValue;
                }
            }
            commaValue = commaValue.trim();
            if (commaValue.endsWith(",")) {
                commaValue = commaValue.substring(0, commaValue.length() - 1);
            }
        }
        return commaValue;
    }

    public static String getTotalAmount(String... priceString) {
        double dTotalValue = 0.0;
        if (priceString != null) {
            for (String price : priceString) {
                if (!TextUtils.isEmpty(price)) {
                    dTotalValue = dTotalValue + Double.parseDouble(price);
                }
            }
        }
        return String.format("%.2f", dTotalValue);
    }

    public static void makeCall(Context context, String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(callIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setMaxLength(CustomEditText edtMobileNumber, int maxLength) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        edtMobileNumber.setFilters(fArray);
    }

    public static int getIsdCodeCount(String isdCode) {
        return isdCode.replaceAll("[^\\p{Digit}]+", "").length();
    }

    /**
     * Used for RTL format support
     * Unicode Character 'LEFT-TO-RIGHT OVERRIDE' (U+202D)
     * http://www.fileformat.info/info/unicode/char/202d/index.htm
     * Suppose you have some arabic text with special characters then it will not support RTL exactly to show arabic text with special character
     * To support this scenario use 'LEFT-TO-RIGHT OVERRIDE' "\u202D"
     *
     * @param convert
     * @return
     */
    public static String unicodeLeftToRightOverride(String convert) {
        if (convert != null) {
            return "\u202D" + convert;
        }
        return "";
    }
    public static boolean isEmpty(String data) {
        return data == null || data.trim().length() == 0;
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || String.valueOf(str).trim().length() == 0 || "null".equalsIgnoreCase(String.valueOf(str)))
            return true;
        else
            return false;
    }

}
