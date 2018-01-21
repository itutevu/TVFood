package com.example.user.tvfood.Local_Address;

/**
 * Created by Valkyzone on 08/08/2017.
 */

public class QuanHuyenDTO {
    private int id;
    private String tenQuan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenQuan() {
        return tenQuan;
    }

    public void setTenQuan(String tenQuan) {
        this.tenQuan = tenQuan;
    }

    public QuanHuyenDTO(int id, String tenQuan) {
        this.id = id;
        this.tenQuan = tenQuan;
    }

    public void createQuanHuyenDTO(QuanHuyenDTO quanHuyenDTO) {
        this.id = quanHuyenDTO.getId();
        this.tenQuan = quanHuyenDTO.getTenQuan();
    }

    public QuanHuyenDTO() {
    }
}
