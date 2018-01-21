package com.example.user.tvfood.Local_Address;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;

/**
 * Created by THANH on 3/24/2017.
 */

public class SQLiteTinhThanhController extends SQLiteDataController {
    public SQLiteTinhThanhController(Context con) {
        super(con);
    }
    // Lấy danh sách tỉnh thành
    public ArrayList<SQLiteTinhThanh> getListTinhThanh() {
        ArrayList<SQLiteTinhThanh> listTinhThanh = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from tbTinhThanhTP", null);
            SQLiteTinhThanh tinhThanh;
            while (cs.moveToNext()) {
                tinhThanh = new SQLiteTinhThanh(cs.getInt(0), cs.getString(1));
                listTinhThanh.add(tinhThanh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return listTinhThanh;
    }
    public int getIDTinhThanh(String Name) {
        int i=-1;
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select ID from tbTinhThanhTP where Name='"+Name+"'", null);
            while (cs.moveToNext()) {
                i = cs.getInt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return i;
    }
    // Lấy tên tỉnh thành theo IDtinhthanh
    public String getNameTinhThanh(int ID) {
        String s="";
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select Name from tbTinhThanhTP where ID='"+ID+"'", null);
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
    //Lấy danh sách tỉnh thành
    public ArrayList<TinhThanhPhoDTO> getListTinhThanh2() {
        ArrayList<TinhThanhPhoDTO> listTinhThanh = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select ID,Name from tbTinhThanhTP", null);
            TinhThanhPhoDTO tinhThanh ;
            while (cs.moveToNext()) {
                tinhThanh = new TinhThanhPhoDTO();

                tinhThanh.setId(cs.getInt(0));
                tinhThanh.setName(cs.getString(1));

                listTinhThanh.add(tinhThanh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return listTinhThanh;
    }
}
