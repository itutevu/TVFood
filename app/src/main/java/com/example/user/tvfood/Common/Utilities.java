package com.example.user.tvfood.Common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    private static HttpClient client = null;

    @SuppressWarnings("deprecation")
    public static String parseDateToString(Date date) {
        if (date == null) {
            return "";
        }
        return date.getDate() + "/" + (date.getMonth() + 1) + "/"
                + (date.getYear() + 1900);
    }

    @SuppressWarnings("deprecation")
    public static String parseTimeToString(Date date) {
        if (date == null) {
            return "";
        }
        return date.getHours() + ":" + date.getMinutes();
    }

    public static String parseDateTimeToString(Date date) {
        if (date == null) {
            return "";
        }
        return Utilities.parseTimeToString(date) + " "
                + Utilities.parseDateToString(date);
    }

    public static Date parseStringToDate(String date) {
        if (date == null || date.equals("")) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            Log.e("Obuut", "ParseException: " + e.getMessage());
            e.printStackTrace();

        }
        return null;
    }

    public static String parseMilliSecondsToTimeString(int milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(new Date(milliSeconds));
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String collapseTagFriends(List<String> listWithFriend) {
        String text = "";
        if (listWithFriend.size() > 3) {
            text = String.format("%s, %s, %s and %d other people",
                    listWithFriend.get(0), listWithFriend.get(1),
                    listWithFriend.get(2), listWithFriend.size() - 3);
        } else {
            for (int i = 0; i < listWithFriend.size(); i++) {
                if (i == listWithFriend.size() - 1) {
                    text += listWithFriend.get(i);
                } else {
                    text += listWithFriend.get(i) + ", ";
                }
            }
        }
        return text;
    }

    public static String collapseComments(int numberComment) {
        return String.format("%d Comment%s", numberComment,
                numberComment < 2 ? "" : "s");
    }

    public static String collapseWows(int numberWow) {
        return String.format("%d Wow%s", numberWow, numberWow < 2 ? "" : "s");
    }

    public static String parseMtoKmWithString(float distance) {
        String value = " | ";
        if (distance >= 1000) {
            float unit = distance % 1000;
            unit = Math.round(unit / 100);
            unit = unit / 10;
            distance = Math.round(distance / 1000);
            value = value + Float.toString(distance + unit) + "km";
        } else {
            value = value + Float.toString(Math.round(distance)) + "m";
        }
        return value;
    }

    public static HttpClient getClient() {
        if (client == null) {
            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            client = new DefaultHttpClient(params);
        }
        return client;
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    // check Internet
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    //check install Package
    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    // function to verify if directory exists
    public static void checkAndCreateDirectory(String dirName, File rootDir) {
        File new_dir = new File(rootDir + dirName);
        if (!new_dir.exists()) {
            new_dir.mkdirs();
        }
    }

    public static JSONArray remove(JSONArray jsonArray, int index) {

        JSONArray output = new JSONArray();
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            if (i != index) {
                try {
                    output.put(jsonArray.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return jsonArray;
                }
            }
        }
        return output;
    }

    public static String getNameDpiDevices(Context context) {
        int density = context.getResources().getDisplayMetrics().densityDpi;
        String nameDpi = "hdpi";
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                nameDpi = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                nameDpi = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                nameDpi = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                nameDpi = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                nameDpi = "xxhdpi";
        }
        return nameDpi;
    }

    public static String getNameScreenLayoutDevice(Context context) {
        int screenSize = context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        String nameLayout = "LARGE SCREEN";
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                nameLayout = "LARGE SCREEN";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                nameLayout = "NORMAL SCREEN";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                nameLayout = "SMALL SCREEN";
                break;
            default:
                nameLayout = "EXTRA LAYOUT";
        }
        return nameLayout;
    }

    public static JSONObject createJSONOBjectFromParams(List<NameValuePair> params) {
        try {
            JSONObject json = new JSONObject();
            for (int i = 0; i < params.size(); i++) {
                NameValuePair param = params.get(i);
                json.put(param.getName(), param.getValue());
            }
            return json;
        } catch (Exception e) {

        }
        return null;
    }

    public static boolean isEmailValidNew(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    //--------------------------------------------
}
