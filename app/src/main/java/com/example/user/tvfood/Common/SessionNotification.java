package com.example.user.tvfood.Common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by USER on 30/11/2017.
 */

public class SessionNotification {
    private final String TAG = SessionNotification.class.getName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Language";

    private static String KEY_NOTIFICATION = "id";


    public SessionNotification(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createSessionNotificatione(int id) {

        editor.putInt(KEY_NOTIFICATION, id);

        editor.commit();
    }

    public int getKeyNotification() {
        return pref.getInt(KEY_NOTIFICATION, Common.KEY_NOTIFICATION.KEY_OFF);
    }


    // Clear session details
    public void clearData() {

        editor.clear();
        editor.commit();

    }
}
