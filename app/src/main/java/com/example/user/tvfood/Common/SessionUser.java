package com.example.user.tvfood.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.user.tvfood.Model.UserDTO;

/**
 * Created by USER on 02/09/2017.
 */

public class SessionUser {
    private final String TAG = SessionUser.class.getName();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "User";

    private static String KEY_ID = "id";
    private static String KEY_TOKEN = "token";
    private static String KEY_HOTEN = "hoten";
    private static String KEY_URLAVATAR = "urlavatar";
    private static String KEY_SDT = "sdt";
    private static String KEY_FCM_TOKEN = "fcm_token";
    private static String KEY_ID_FB = "id_fb";

    public SessionUser(Context _context) {
        this._context = _context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createSessionUser(UserDTO userDTO) {
        if (userDTO == null)
            return;
        editor.putString(KEY_ID, userDTO.getId());
        editor.putString(KEY_TOKEN, userDTO.getToken());
        editor.putString(KEY_HOTEN, userDTO.getHoten());
        editor.putString(KEY_URLAVATAR, userDTO.getUrlavatar());
        editor.putString(KEY_SDT, userDTO.getSdt());
        editor.putString(KEY_FCM_TOKEN, userDTO.getFcmToken());
        editor.putString(KEY_ID_FB, userDTO.getIdFB());

        editor.commit();
    }

    public UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(pref.getString(KEY_ID, ""));
        userDTO.setToken(pref.getString(KEY_TOKEN, ""));
        userDTO.setHoten(pref.getString(KEY_HOTEN, ""));
        userDTO.setUrlavatar(pref.getString(KEY_URLAVATAR, ""));
        userDTO.setSdt(pref.getString(KEY_SDT, ""));
        userDTO.setFcmToken(pref.getString(KEY_FCM_TOKEN, ""));
        userDTO.setIdFB(pref.getString(KEY_ID_FB, ""));

        return userDTO;
    }


    // Clear session details
    public void clearData() {

        editor.clear();
        editor.commit();

    }
}
