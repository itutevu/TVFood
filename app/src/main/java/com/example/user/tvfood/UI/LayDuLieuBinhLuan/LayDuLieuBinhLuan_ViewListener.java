package com.example.user.tvfood.UI.LayDuLieuBinhLuan;

import com.example.user.tvfood.Model.BinhLuanDTO;

import java.util.List;

/**
 * Created by USER on 13/10/2017.
 */

public interface LayDuLieuBinhLuan_ViewListener {
    void onSuccessLayDuLieuBinhLuan(List<BinhLuanDTO> binhLuanDTOs, boolean isMore);

    void onFailedLayDuLieuBinhLuan(String message, boolean isMore);

    void onEmpty(String message, boolean isMore);
}
