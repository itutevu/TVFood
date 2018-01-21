package com.example.user.tvfood.Local_Address;

/**
 * Created by Valkyzone on 08/08/2017.
 */

public class DuongDTO {
    private int id;
    private String tenDuong;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDuong() {
        return tenDuong;
    }

    public void setTenDuong(String tenDuong) {
        this.tenDuong = tenDuong;
    }

    public DuongDTO(int id, String tenDuong) {
        this.id = id;
        this.tenDuong = tenDuong;
    }

    public DuongDTO() {
    }
}
