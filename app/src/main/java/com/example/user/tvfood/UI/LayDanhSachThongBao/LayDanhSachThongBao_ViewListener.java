package com.example.user.tvfood.UI.LayDanhSachThongBao;

import com.example.user.tvfood.Model.Notification_Model;

import java.util.ArrayList;

/**
 * Created by Valkyzone on 11/30/2017.
 */

public interface LayDanhSachThongBao_ViewListener {
    void onSuccess_GetNotification(ArrayList<Notification_Model> notification_models);
    void onFailed_GetNotification(String message);
    void onEmpty_GetNotification(String message);
}
