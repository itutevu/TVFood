package com.example.user.tvfood.Common;

/**
 * Created by USER on 20/09/2017.
 */

public class SessionDiaDiem {
    private long KEY_ThanhPho = Common.KEY_SORT.KEY_THANHPHO_FALSE;
    private long KEY_QuanHuyen = Common.KEY_SORT.KEY_QUANHUYEN_FALSE;
    private long KEY_Duong = Common.KEY_SORT.KEY_DUONG_FALSE;
    private long VALUE_ThanhPho = 0;
    private long VALUE_QuanHuyen = 0;
    private long VALUE_Duong = 0;
    private static SessionDiaDiem instance;

    public SessionDiaDiem() {

    }

    public static SessionDiaDiem getInstance() {
        if (instance == null)
            instance = new SessionDiaDiem();
        return instance;
    }

    public long getVALUE_ThanhPho() {
        return VALUE_ThanhPho;
    }

    public void setVALUE_ThanhPho(long VALUE_ThanhPho) {
        this.VALUE_ThanhPho = VALUE_ThanhPho;
    }

    public long getVALUE_QuanHuyen() {
        return VALUE_QuanHuyen;
    }

    public void setVALUE_QuanHuyen(long VALUE_QuanHuyen) {
        this.VALUE_QuanHuyen = VALUE_QuanHuyen;
    }

    public long getVALUE_Duong() {
        return VALUE_Duong;
    }

    public void setVALUE_Duong(long VALUE_Duong) {
        this.VALUE_Duong = VALUE_Duong;
    }

    public long getKEY_ThanhPho() {
        return KEY_ThanhPho;
    }

    public void setKEY_ThanhPho(long KEY_ThanhPho) {
        this.KEY_ThanhPho = KEY_ThanhPho;
    }

    public long getKEY_QuanHuyen() {
        return KEY_QuanHuyen;
    }

    public void setKEY_QuanHuyen(long KEY_QuanHuyen) {
        this.KEY_QuanHuyen = KEY_QuanHuyen;
    }

    public long getKEY_Duong() {
        return KEY_Duong;
    }

    public void setKEY_Duong(long KEY_Duong) {
        this.KEY_Duong = KEY_Duong;
    }
}
