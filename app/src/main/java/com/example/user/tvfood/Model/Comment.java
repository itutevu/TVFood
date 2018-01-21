package com.example.user.tvfood.Model;

/**
 * Created by USER on 01/08/2017.
 */

public class Comment {
    private String UrlImage;
    private String noidung;
    private String TenTaiKhoan;
    private String userid;
    public Comment() {

    }
    public Comment(String urlImage, String noiDung, String tenTaiKhoan) {
        UrlImage = urlImage;
        noidung = noiDung;
        TenTaiKhoan = tenTaiKhoan;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        UrlImage = urlImage;
    }

    public String getNoiDung() {
        return noidung;
    }

    public void setNoiDung(String noiDung) {
        noidung = noiDung;
    }

    public String getTenTaiKhoan() {
        return TenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        TenTaiKhoan = tenTaiKhoan;
    }
}
