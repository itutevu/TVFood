package com.example.user.tvfood.UI.LayDanhSachThongBao;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.UI.MainActivity;
import com.example.user.tvfood.Model.Notification_Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Valkyzone on 11/30/2017.
 */

public class LayDanhSachThongBao_Presenter {
    private LayDanhSachThongBao_ViewListener layDanhSachThongBao_viewListener;

    public void receiveHandle(LayDanhSachThongBao_ViewListener layDanhSachThongBao_viewListener, int numOfLoad, String endAt) {
        this.layDanhSachThongBao_viewListener = layDanhSachThongBao_viewListener;
        getDataNoti(numOfLoad, endAt);
    }

    private void getDataNoti(int numOfLoad, final String endAt) {


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Notification_Model> notification_models = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    if (item.getKey().equals(endAt))
                        break;
                    Notification_Model notification_model = item.getValue(Notification_Model.class);
                    assert notification_model != null;
                    notification_model.setNotiDate(item.getKey());
                    notification_models.add(notification_model);

                }
                if (notification_models.size() == 0) {
                    layDanhSachThongBao_viewListener.onEmpty_GetNotification("empty");
                    return;
                }
                // Đảo ngược lại
                ArrayList<Notification_Model> notification_models2 = new ArrayList<>();
                for (int i = notification_models.size() - 1; i >= 0; i--) {
                    notification_models2.add(notification_models.get(i));
                }

                layDanhSachThongBao_viewListener.onSuccess_GetNotification(notification_models2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                layDanhSachThongBao_viewListener.onFailed_GetNotification(databaseError.getMessage());
            }
        };
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THONGBAOS).orderByKey().limitToLast(numOfLoad).addListenerForSingleValueEvent(valueEventListener);


    }
}
