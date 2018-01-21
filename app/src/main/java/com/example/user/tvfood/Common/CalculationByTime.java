package com.example.user.tvfood.Common;

import android.app.Activity;
import android.content.Context;

import com.example.user.tvfood.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by USER on 30/08/2017.
 */

public class CalculationByTime {
    public static String GetTime(String timeComment, Context context, Activity activity) {
        Date dateComment = StringToDate2(timeComment);

        Calendar calendar = new GregorianCalendar(Integer.parseInt(getYear(dateComment)), Integer.parseInt(getMonth(dateComment)) + 1, Integer.parseInt(getDay(dateComment)));

        Calendar c = Calendar.getInstance();
        String timeCurrent = c.get(Calendar.YEAR) + "";

        if ((c.get(Calendar.MONTH) + 1) < 10)
            timeCurrent += "0" + (c.get(Calendar.MONTH) + 1);
        else
            timeCurrent += (c.get(Calendar.MONTH) + 1) + "";

        if (c.get(Calendar.DAY_OF_MONTH) < 10)
            timeCurrent += "0" + c.get(Calendar.DAY_OF_MONTH);
        else
            timeCurrent += c.get(Calendar.DAY_OF_MONTH);

        if (c.get(Calendar.HOUR_OF_DAY) < 10)
            timeCurrent += "0" + c.get(Calendar.HOUR_OF_DAY);
        else
            timeCurrent += c.get(Calendar.HOUR_OF_DAY);

        if (c.get(Calendar.MINUTE) < 10)
            timeCurrent += "0" + c.get(Calendar.MINUTE);
        else
            timeCurrent += c.get(Calendar.MINUTE);

        if (c.get(Calendar.SECOND) < 10)
            timeCurrent += "0" + c.get(Calendar.SECOND);
        else
            timeCurrent += c.get(Calendar.SECOND);

        Date dateCurrent = StringToDate2(timeCurrent);

        long secondsBetween = (dateCurrent.getTime() - dateComment.getTime()) / 1000;


        if ((int) secondsBetween / (86400 * 365) != 0)
            return getDay(dateComment) + " " + context.getApplicationContext().getResources().getString(R.string.thang) + getMonth(dateComment) + " " + getYear(dateComment);
        else {
            if ((int) secondsBetween / (86400 * 30) != 0) {
                if (c.get(Calendar.YEAR) - Integer.parseInt(getYear(dateComment)) >= 1)
                    return getDay(dateComment) + "/" + getMonth(dateComment) + " " + getYear(dateComment);
                else
                    return getDay(dateComment) + "/" + getMonth(dateComment);
            } else {
                if ((int) secondsBetween / 86400 != 0) {
                    if ((int) secondsBetween / 86400 > 1)
                        return (int) secondsBetween / 86400 + " " + activity.getResources().getString(R.string.ngaytruocs);
                    else
                        return (int) secondsBetween / 86400 + " " + activity.getResources().getString(R.string.ngaytruoc);
                } else {
                    if ((int) secondsBetween / 3600 != 0) {
                        if ((int) secondsBetween / 3600 > 1)
                            return (int) secondsBetween / 3600 + " " + activity.getResources().getString(R.string.giotruocs);
                        else
                            return (int) secondsBetween / 3600 + " " + activity.getResources().getString(R.string.giotruoc);
                    } else {
                        if ((int) secondsBetween / 60 != 0) {
                            if ((int) secondsBetween / 60 > 1)
                                return (int) secondsBetween / 60 + " " + activity.getResources().getString(R.string.phuttruocs);
                            else
                                return (int) secondsBetween / 60 + " " + activity.getResources().getString(R.string.phuttruoc);
                        } else {
                            if ((int) secondsBetween <= 1)
                                return " " + activity.getResources().getString(R.string.vuamoi);
                            if ((int) secondsBetween > 1)
                                return (int) secondsBetween + " " + activity.getResources().getString(R.string.giaytruocs);
                            else
                                return (int) secondsBetween + " " + activity.getResources().getString(R.string.giaytruoc);
                        }
                    }
                }
            }
        }


    }


    public static Date StringToDate2(String s) {
        //SimpleDateFormat format = new SimpleDateFormat("hh:mm a"); //if 24 hour format
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static boolean SoSanhTime(String time) {
        Calendar c = Calendar.getInstance();
        int hourCurrent = c.get(Calendar.HOUR_OF_DAY);
        int minuteCurrent = c.get(Calendar.MINUTE);
        long secondCurrent = TimeUnit.HOURS.toSeconds(hourCurrent) + TimeUnit.MINUTES.toSeconds(minuteCurrent);

        Date date = StringToDate(time);

        int hour = Integer.parseInt(getHour(date));

        int minute = Integer.parseInt(getMinute(date));
        long second = TimeUnit.HOURS.toSeconds(hour) + TimeUnit.MINUTES.toSeconds(minute);

        if (second <= secondCurrent)
            return true;
        else
            return false;
    }

    public static Date StringToDate(String s) {
        //SimpleDateFormat format = new SimpleDateFormat("hh:mm a"); //if 24 hour format
        DateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getHour(Date date) {
        String s = "";
        String myFormat = "HH"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }

    public static String getSecond(Date date) {
        String s = "";
        String myFormat = "ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }

    public static String getMinute(Date date) {
        String s = "";
        String myFormat = "mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }

    public static String getMonth(Date date) {
        String s = "";
        String myFormat = "MM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }

    public static String getYear(Date date) {
        String s = "";
        String myFormat = "yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }

    public static String getDay(Date date) {
        String s = "";
        String myFormat = "dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }
}
