package com.example.user.tvfood.Local_Address;

/**
 * Created by THANH on 3/26/2017.
 */

public class SQLiteQuanTPHCM {
    private int ID;
    private String Name;
    private int ID2;
    public SQLiteQuanTPHCM(){}
    public SQLiteQuanTPHCM(int ID, String name, int ID2) {
        this.ID = ID;
        Name = name;
        this.ID2 = ID2;
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

    public int getID2() {
        return ID2;
    }

    public void setID2(int ID2) {
        this.ID2 = ID2;
    }
}
