package com.example.user.tvfood.Common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by USER on 17/11/2017.
 */

public class SessionLanguage {
    private final String TAG = SessionLanguage.class.getName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Language";

    private static String KEY_LANGUAGE = "id";


    public SessionLanguage(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createSessionLanguage(int id) {

        editor.putInt(KEY_LANGUAGE, id);

        editor.commit();
    }

    public int getKeyLanguage() {
        return pref.getInt(KEY_LANGUAGE, Common.KEY_LANGUAGE.KEY_VN);
    }


    // Clear session details
    public void clearData() {

        editor.clear();
        editor.commit();

    }
}
