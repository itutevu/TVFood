package com.example.user.tvfood.Local_Address;

/**
 * Created by THANH on 3/24/2017.
 */

public class SQLiteTinhThanh {
    private int ID;
    private String Name;

    public SQLiteTinhThanh(int ID, String name) {
        this.ID = ID;
        this.Name = name;
    }
    public SQLiteTinhThanh(String name) {
        this.Name = name;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public SQLiteTinhThanh(){}

}