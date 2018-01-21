package com.example.user.tvfood.Controller.Interface;

import com.example.user.tvfood.Model.QuanAnDTO;

import java.util.List;

/**
 * Created by Valkyzone on 26/08/2017.
 */

public interface Tab_TrangChu_Interface {
    void getDanhSachQuanAnDTO(List<QuanAnDTO> quanAnDTOList);
    void onFailed_GetDanhSachQuanAnDTO(String message);
    void onEmpty_GetDanhSachQuanAnDTO(String message);
}
