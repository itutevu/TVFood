package com.example.user.tvfood.Local_Address;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;

/**
 * Created by THANH on 3/26/2017.
 */

public class SQLiteQuanTPHCMController extends SQLiteDataController {

    public SQLiteQuanTPHCMController(Context con) {
        super(con);
    }
    // Lấy IDquanhuyen theo tên Quận huyện
    public int getIDQuanHuyen(String Name) {
        int quanHuyen=1;
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select ID from tbQuanHuyen where Name='"+Name+"'", null);
            while (cs.moveToNext()) {
                quanHuyen = cs.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return quanHuyen;
    }
    // Lấy tên quận huyện theo IDquanhuyen
    public String getNameQuanHuyen(int ID) {
        String s="";
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select Name from tbQuanHuyen where ID='"+ID+"'", null);
            String tinhThanh;
            while (cs.moveToNext()) {
                s = cs.getString(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return s;
    }
    // Lấy danh sách quận huyện theo ID_tinhthanh
    public ArrayList<QuanHuyenDTO> getListQuanHuyen(int ID2) {
        ArrayList<QuanHuyenDTO> listQuanTPHCM = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select Name,ID from tbQuanHuyen where ID2='"+ID2+"'", null);
            String tenQuan;
            int id;
            while (cs.moveToNext()) {
                tenQuan = cs.getString(0);
                id = cs.getInt(1);
                listQuanTPHCM.add(new QuanHuyenDTO(id,tenQuan));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return listQuanTPHCM;
    }
}
