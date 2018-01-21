package com.example.user.tvfood.Local_Address;

/**
 * Created by Valkyzone on 08/08/2017.
 */

public class Parent_ExpandableListDTO {
    private int idQuanHuyen;
    private String tenQuan;
    private String soDuong;

    public int getIdQuanHuyen() {
        return idQuanHuyen;
    }

    public void setIdQuanHuyen(int idQuanHuyen) {
        this.idQuanHuyen = idQuanHuyen;
    }

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public String getSoDuong() {
        return soDuong;
    }

    public void setSoDuong(String soDuong) {
        this.soDuong = soDuong;
    }

    public Parent_ExpandableListDTO() {
    }

    public Parent_ExpandableListDTO(int id, String tenQuan, String soDuong) {
        this.idQuanHuyen = id;
        this.tenQuan = tenQuan;
        this.soDuong = soDuong;
    }
}
