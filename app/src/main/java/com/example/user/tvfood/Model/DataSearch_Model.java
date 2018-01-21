package com.example.user.tvfood.Model;

/**
 * Created by USER on 01/12/2017.
 */

public class DataSearch_Model {
    private String idQuanAn;
    private String diachi;
    private String tenquanan;

    public DataSearch_Model() {
    }

    public DataSearch_Model(String idQuanAn, String diachi, String tenquanan) {
        this.idQuanAn = idQuanAn;
        this.diachi = diachi;
        this.tenquanan = tenquanan;
    }

    public String getIdQuanAn() {
        return idQuanAn;
    }

    public void setIdQuanAn(String idQuanAn) {
        this.idQuanAn = idQuanAn;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }
}
