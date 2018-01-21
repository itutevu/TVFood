package com.example.user.tvfood.UI.LayDuLieuQuanAnByID;

import com.example.user.tvfood.Model.QuanAnDTO;

/**
 * Created by USER on 07/11/2017.
 */

public interface LayDuLieuQuanAnByID_ViewListener {
    void onSuccess_LayDuLieuQuanAnByID(QuanAnDTO quanAnDTO, boolean isYeuThich, boolean isPin, boolean isRating, String rating);
    void onFailed_LayDuLieuQuanAnByID(String message);

}
