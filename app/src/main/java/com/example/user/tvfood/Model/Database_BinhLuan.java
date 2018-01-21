package com.example.user.tvfood.Model;

/**
 * Created by USER on 06/09/2017.
 */

public class Database_BinhLuan {
    private String noidung;
    private String urlimage;
    private String userid;

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Database_BinhLuan(String noidung, String urlimage, String userid) {
        this.noidung = noidung;
        this.urlimage = urlimage;
        this.userid = userid;
    }
}
