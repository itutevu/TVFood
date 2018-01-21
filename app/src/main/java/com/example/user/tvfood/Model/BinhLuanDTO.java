package com.example.user.tvfood.Model;

/**
 * Created by USER on 30/08/2017.
 */

public class BinhLuanDTO {
    private String idbinhluan;
    private String userid;
    private String noidung;
    private String idquanan;
    private String luotthich = "0";
    private UserDTO userDTO;
    private String urlimage;
    private boolean isNew = false;
    private boolean isYeuThich = false;

    public String getUrlimage() {
        return urlimage;
    }

    public synchronized void setUrlimage(String urlimage) {
        synchronized (this) {
            this.urlimage = urlimage;
        }
    }

    public String getLuotthich() {
        return luotthich;
    }

    public void setLuotthich(String luotthich) {
        this.luotthich = luotthich;
    }

    public boolean isYeuThich() {
        return isYeuThich;
    }

    public void setYeuThich(boolean yeuThich) {
        isYeuThich = yeuThich;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getIdbinhluan() {
        return idbinhluan;
    }

    public void setIdbinhluan(String idbinhluan) {
        this.idbinhluan = idbinhluan;
    }

    public String getIdquanan() {
        return idquanan;
    }

    public void setIdquanan(String idquanan) {
        this.idquanan = idquanan;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public BinhLuanDTO() {
    }
}
