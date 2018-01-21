package com.example.user.tvfood.UI.LayDanhSachQuanAnGanToi;

import com.example.user.tvfood.Model.QuanAnDTO;

import java.util.ArrayList;

/**
 * Created by USER on 04/11/2017.
 */

public interface LayDanhSachQuanAnGanToi_ViewListener {
    void onSuccess_LayDanhSachQuanAnGanToi(ArrayList<QuanAnDTO> itemGanTois);

    void onFailed_LayDanhSachQuanAnGanToi(String message);

    void onEmpty_LayDanhSachQuanAnGanToi();
}
