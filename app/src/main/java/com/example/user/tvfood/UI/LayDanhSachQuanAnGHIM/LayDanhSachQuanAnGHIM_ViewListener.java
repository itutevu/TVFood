package com.example.user.tvfood.UI.LayDanhSachQuanAnGHIM;

import com.example.user.tvfood.Model.QuanAnDTO;

import java.util.List;

/**
 * Created by USER on 18/11/2017.
 */

public interface LayDanhSachQuanAnGHIM_ViewListener {
    void onSuccess_LayDanhSachQuanAnGHIM(List<QuanAnDTO> quanAnDTOList);

    void onFailed_LayDanhSachQuanAnGHIM(String message);

    void onEmpty_LayDanhSachQuanAnGHIM(String message);
}
