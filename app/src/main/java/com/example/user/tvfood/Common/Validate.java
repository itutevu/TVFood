package com.example.user.tvfood.Common;

import android.util.Log;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tskil on 7/27/2017.
 */

public class Validate {
    /* todo: other function */
    public static Boolean isNumber(String s){
        try {
            long i = Long.parseLong(s);
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }
    public static Boolean isEmpty(String s){
        if(s.equals(""))
            return true;
        return false;
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

    public static Date StringToDate(String s){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String DateToStringSetText2(Date date){
        String s = "";
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }
    public static String DateToStringSetText(Date date){
        String s = "";
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }
    public static String getYear(Date date){
        String s = "";
        String myFormat = "yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }
    public static String getMonth(Date date){
        String s = "";
        String myFormat = "MM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }
    public static String getDay(Date date){
        String s = "";
        String myFormat = "dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }
    public static String StringDateFormatToUploadData(String s){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf2.format(date);
    }
    public static String StringDateFormatToUploadData2(String s){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return sdf2.format(date);
    }
    public static Date parseStringToDate(String date) {
        if (date == null || date.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            Log.e("Obuut", "ParseException: " + e.getMessage());
            e.printStackTrace();

        }
        return null;
    }
    public static Date parseStringToDate2(String date) {
        if (date == null || date.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            Log.e("Obuut", "ParseException: " + e.getMessage());
            e.printStackTrace();

        }
        return null;
    }
    public static String FormatDateToString(Date date){
        String s = "";
        String myFormat = "HH:mm, dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        s = sdf.format(date);
        return s;
    }
    public static String StringDateFormatToSetText(String s){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return sdf2.format(date);
    }

    public static String getDistanceWithUnit(int meter) {
        String result = meter + " m";
        if (meter > 999) {
            NumberFormat df = DecimalFormat.getInstance();
            df.setMaximumFractionDigits(2);
            df.setRoundingMode(RoundingMode.DOWN);
            result = df.format((float) meter / 1000) + " km";
        }
        return result;
    }
}
