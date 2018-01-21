package com.example.user.tvfood.Local_Address;

/**
 * Created by THANH on 3/27/2017.
 */

public class SQLiteDuong {
    private String Name;
    private int IDquanhuyen;
    private int IDtinhthanh;

    public SQLiteDuong(String name, int IDquanhuyen, int IDtinhthanh) {
        Name = name;
        this.IDquanhuyen = IDquanhuyen;
        this.IDtinhthanh = IDtinhthanh;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIDquanhuyen() {
        return IDquanhuyen;
    }

    public void setIDquanhuyen(int IDquanhuyen) {
        this.IDquanhuyen = IDquanhuyen;
    }

    public int getIDtinhthanh() {
        return IDtinhthanh;
    }

    public void setIDtinhthanh(int IDtinhthanh) {
        this.IDtinhthanh = IDtinhthanh;
    }
}
