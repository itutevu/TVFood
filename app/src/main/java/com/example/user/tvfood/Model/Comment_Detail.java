package com.example.user.tvfood.Model;

/**
 * Created by USER on 06/08/2017.
 */

public class Comment_Detail {
    private int idQuanAn;
    private String token;
    private String urlAva;
    private String urlImage;
    private String noiDung;
    private String tenTaiKhoan;
    private int soLike;
    private int soComment;

    public String getUrlAva() {
        return urlAva;
    }
    public boolean isUrlImage()
    {
        if(this.urlImage==null||this.urlImage=="")
            return false;
        return true;
    }

    public void setUrlAva(String urlAva) {
        this.urlAva = urlAva;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIdQuanAn() {
        return idQuanAn;
    }

    public void setIdQuanAn(int idQuanAn) {
        this.idQuanAn = idQuanAn;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getTenTaiKhoan() {
        return tenTaiKhoan;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    public int getSoLike() {
        return soLike;
    }

    public void setSoLike(int soLike) {
        this.soLike = soLike;
    }

    public int getSoComment() {
        return soComment;
    }

    public void setSoComment(int soComment) {
        this.soComment = soComment;
    }

    public Comment_Detail(int idQuanAn, String token, String urlAva, String urlImage, String noiDung, String tenTaiKhoan, int soLike, int soComment) {
        this.idQuanAn = idQuanAn;
        this.token = token;
        this.urlAva = urlAva;
        this.urlImage = urlImage;
        this.noiDung = noiDung;
        this.tenTaiKhoan = tenTaiKhoan;
        this.soLike = soLike;
        this.soComment = soComment;
    }

    public Comment_Detail() {

    }
}
