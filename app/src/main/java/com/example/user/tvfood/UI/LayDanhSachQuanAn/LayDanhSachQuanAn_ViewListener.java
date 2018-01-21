package com.example.user.tvfood.UI.LayDanhSachQuanAn;

import com.example.user.tvfood.Model.QuanAnDTO;

import java.util.ArrayList;

/**
 * Created by USER on 29/10/2017.
 */

public interface LayDanhSachQuanAn_ViewListener {
    void onSuccess_LayDanhSachQuanAn(ArrayList<QuanAnDTO> quanAnDTOs);

    void onFailed_LayDanhSachQuanAn(String message);
    void onEmpty_LayDanhSachQuanAn(String message);
}
