package com.weekend.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.weekend.calendar.CalendarView.dateFormat;


public class DateUtil {

    private static long aDay = (1000 * 60 * 60 * 24);
    private static long aMinute = (1000 * 60);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("'at' hh:mm aa");
    public static final SimpleDateFormat fullDateTimeZome = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.Z");
    private static final SimpleDateFormat fbtimeformat = new SimpleDateFormat("MMMM dd 'at' hh:mm aa");
    private static final SimpleDateFormat twtimeformat = new SimpleDateFormat("MMMM dd");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("h:mma");
    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("EEEE, MMMM dd");
    private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private static final SimpleDateFormat RSS_DATE_FORMAT = new SimpleDateFormat("MMMM dd,yyyy");
    private static final SimpleDateFormat MESSAGE_DATE_FORMAT = new SimpleDateFormat("dd MMM");
    private static final SimpleDateFormat FULL_DISPLAY_FORMAT = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm aa");
    private static final SimpleDateFormat SMALL_DISPLAY_FORMAT = new SimpleDateFormat("MMM dd 'at' hh:mm aa");

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }

    public static SimpleDateFormat getFBDateFormat() {
        return fbtimeformat;
    }

    public static SimpleDateFormat getTwDateFormat() {
        return twtimeformat;
    }

    public static SimpleDateFormat getDbDateFormat() {
        return DB_DATE_FORMAT;
    }

    /**
     * converts date into Twitter like format
     *
     * @param fromdate date to be converted
     * @return
     */
    public static String getTWTimeString(Date fromdate) {

        long then;
        then = fromdate.getTime();
        Date date = new Date(then);

        StringBuffer dateStr = new StringBuffer();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar now = Calendar.getInstance();

        int days = daysBetween(calendar.getTime(), now.getTime());

        if (days == 0) {

            int minutes = hoursBetween(calendar.getTime(), now.getTime());

            if (minutes > 60) {
                int hours = minutes / 60;
                dateStr.append(hours).append(hours > 1 ? " hours" : " hour")
                        .append(" ago ");
            } else {

                if (minutes <= 1) {
                    dateStr.append("few seconds ago");
                } else {
                    dateStr.append(minutes).append(" mins");
                }
            }
        } else
            dateStr.append(twtimeformat.format(date));

        return dateStr.toString();
    }

    /**
     * converts date into Facebook like format
     *
     * @param dateStr1       date string to be converted
     * @param fromdateFormat format of the date passed
     * @return
     */
    public static String getTWTimeString(String dateStr1, String fromdateFormat) {

        long then;

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat formatter = new SimpleDateFormat(fromdateFormat);
        then = formatter.parse(dateStr1, pos).getTime();

        Date date = new Date(then);
        return getTWTimeString(date);

    }

    /**
     * converts date into Facebook like format
     *
     * @param fromdate date to be converted
     * @return
     */
    public static String getFBTimeString(Date fromdate) {

        long then;
        then = fromdate.getTime();
        Date date = new Date(then);

        StringBuffer dateStr = new StringBuffer();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar now = Calendar.getInstance();

        int days = daysBetween(calendar.getTime(), now.getTime());

        if (days == 0) {

            int minutes = hoursBetween(calendar.getTime(), now.getTime());

            if (minutes > 60) {
                int hours = minutes / 60;
                dateStr.append(hours).append(hours > 1 ? " hours" : " hour")
                        .append(" ago ").append(timeFormat.format(date));
            } else {

                if (minutes <= 1) {
                    dateStr.append("few seconds ago");
                } else {
                    dateStr.append(minutes).append(" minutes ago");
                }
            }


        } else if (days == 1) {
            dateStr.append("Yesterday ").append(timeFormat.format(date));
        } else {
            dateStr.append(fbtimeformat.format(date));
        }

        return dateStr.toString();
    }

    /**
     * converts date into Facebook like format
     *
     * @param dateStr1       date string to be converted
     * @param fromdateFormat format of the date passed
     * @return
     */
    public static String getFBTimeString(String dateStr1, String fromdateFormat) {

        long then;

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat formatter = new SimpleDateFormat(fromdateFormat);
        then = formatter.parse(dateStr1, pos).getTime();

        Date date = new Date(then);
        return getFBTimeString(date);

    }

    /**
     * converts date into Facebook like format.
     * use this function only if date is in yyyy-MM-dd'T'HH:mm:ssZ format
     *
     * @param dateStr1 date string to be converted
     * @return
     */
    public static String getFBTimeString(String dateStr1) {

        long then;

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat formatter = getDateFormat();
        then = formatter.parse(dateStr1, pos).getTime();

        Date date = new Date(then);
        return getFBTimeString(date);

    }

    public static int hoursBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / aMinute);
    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / aDay);
    }

    public static Date getSimpleDate(String dateString) throws ParseException {
        return DATE_FORMAT.parse(dateString);
    }

    public static String getSimpleDate(Calendar calendar) {
        return getSimpleDate(calendar.getTime());
    }

    public static String getSimpleDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date getDBDate(String dateString) throws ParseException {
        return DB_DATE_FORMAT.parse(dateString);
    }

    public static String getMessageDate(Date date) {
        return MESSAGE_DATE_FORMAT.format(date);
    }

    public static String getDBDate(Date date) {
        return DB_DATE_FORMAT.format(date);
    }

    public static Date getDisplayDate(String dateString) throws ParseException {
        DISPLAY_FORMAT.setTimeZone(TimeZone.getDefault());
        return DISPLAY_FORMAT.parse(dateString);
    }

    public static String getDisplayDate(Date date) {
        DISPLAY_FORMAT.setTimeZone(TimeZone.getDefault());
        return DISPLAY_FORMAT.format(date);
    }

    public static String getFullDisplayDate(Date date) {
        return FULL_DISPLAY_FORMAT.format(date);
    }

    public static String getSmallDisplayDate(Date date) {
        return SMALL_DISPLAY_FORMAT.format(date);
    }

    public static String getRSSDisplayDate(Date date) {
        RSS_DATE_FORMAT.setTimeZone(TimeZone.getDefault());
        return RSS_DATE_FORMAT.format(date);
    }

    public static String getTimeDate(Date date) {
        TIME_FORMAT.setTimeZone(TimeZone.getDefault());
        return TIME_FORMAT.format(date);
    }

    public static Date getFullDate(String date) throws ParseException {
        return FULL_DATE_FORMAT.parse(date);
    }

    public static String getCurrentTimeZoneOffset() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        SimpleDateFormat date = new SimpleDateFormat("Z");

        return date.format(currentLocalTime);

    }

    /*
       @return 11/18/2016
     */
    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String txtcurrentDate = dateFormat1.format(cal.getTime());
        return txtcurrentDate;
    }

    public static Date getCurrentDayStartDate() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String txtcurrentDate = dateFormat.format(cal.getTime());
        Date currentDate = null;
        try {
            currentDate = dateFormat1.parse(txtcurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentDate;
    }

    public static String getCurrentAppDate() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
        String txtcurrentDate = dateFormat.format(cal.getTime());
        return txtcurrentDate;
    }

    /**
     * converts date into Facebook like format.
     * use this function only if date is in yyyy-MM-dd format
     *
     * @param inputDate date string to be converted 2016-08-10
     * @return outputDate in 11/18/2016. dd/MM/yyyy
     */
    public static String convertToAppDate(String inputDate) {
        String showDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = dateFormat.parse(inputDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            showDate = dateFormat1.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return showDate;
    }

    /**
     * converts date into Facebook like format.
     * use this function only if date is in hh:mm:ss - hh:mm:ss format
     *
     * @param inputDate date string to be converted 12:12:00
     * @return outputDate in 12:12 AM
     */
    public static String convertToAppTime(String inputDate) {
        String showDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss",Locale.ENGLISH);
            Date myDate = dateFormat.parse(inputDate);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
            showDate = dateFormat1.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return showDate;
    }

    public static String getFormatDate(String format, Date date) {
        String formatedDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format,Locale.ENGLISH);
            formatedDate = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

}
