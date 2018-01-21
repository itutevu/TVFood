package com.example.user.tvfood.Local_Address;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;

/**
 * Created by THANH on 3/27/2017.
 */

public class SQLiteDuongController extends SQLiteDataController {
    public SQLiteDuongController(Context con) {
        super(con);
    }

    // Lấy danh sách đường đi theo IDquanhuyen và IDtinhthanh
    public ArrayList<DuongDTO> getListDuong(int IDquanhuyen, int IDtinhthanh) {
        ArrayList<DuongDTO> listDuong = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select Name,ID from tbDuong where IDquanhuyen='" + IDquanhuyen + "' and IDtinhthanh='" + IDtinhthanh + "'", null);
            String tenDuong;
            int id;
            while (cs.moveToNext()) {
                tenDuong = cs.getString(0);
                id = cs.getInt(1);
                listDuong.add(new DuongDTO(id, tenDuong));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return listDuong;
    }

    // Lấy danh sách Số đường đi của mỗi quận huyện của 1 tỉnh thành
    public ArrayList<String> getListSoDuong(int IDtinhthanh) {
        ArrayList<String> listSoDuong = new ArrayList<>();
        // mo ket noi
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select Count(IDquanhuyen) from tbDuong where IDtinhthanh='" + IDtinhthanh + "' group by IDquanhuyen", null);
            int duong;
            while (cs.moveToNext()) {
                duong = cs.getInt(0);
                listSoDuong.add(String.valueOf(duong));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return listSoDuong;
    }
}
