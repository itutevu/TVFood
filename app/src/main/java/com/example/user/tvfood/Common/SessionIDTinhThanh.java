package com.example.user.tvfood.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.tvfood.Local_Address.TinhThanhPhoDTO;

/**
 * Created by USER on 15/08/2017.
 */

public class SessionIDTinhThanh {
    private final String TAG = SessionIDTinhThanh.class.getName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "TinhThanhPho";

    private static String KEY_ID = "id";
    private static String KEY_NAME = "tinhThanh";

    public SessionIDTinhThanh(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createSessionIDTinhThanh(TinhThanhPhoDTO tinhThanhPhoDTO) {
        if (tinhThanhPhoDTO == null)
            return;
        editor.putInt(KEY_ID, tinhThanhPhoDTO.getId());
        editor.putString(KEY_NAME, tinhThanhPhoDTO.getName());

        editor.commit();
    }

    public TinhThanhPhoDTO getTinhThanhPhoDTO() {
        TinhThanhPhoDTO tinhThanhPhoDTO = new TinhThanhPhoDTO();

        tinhThanhPhoDTO.setId(pref.getInt(KEY_ID, 1));
        tinhThanhPhoDTO.setName(pref.getString(KEY_NAME, null));

        return tinhThanhPhoDTO;
    }


    // Clear session details
    public void clearData() {

        editor.clear();
        editor.commit();

    }
}
