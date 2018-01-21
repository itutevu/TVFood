package com.example.user.tvfood.UI.LayDanhSachQuanAnGanToi;

import android.net.Uri;

import com.example.user.tvfood.Common.CalculationByDistance;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.UI.MainActivity;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 04/11/2017.
 */

public class LayDanhSachQuanAnGanToi_Presenter {
    private LayDanhSachQuanAnGanToi_ViewListener layDanhSachQuanAnGanToi_viewListener;


    public void receiveHandle(LayDanhSachQuanAnGanToi_ViewListener layDanhSachQuanAnGanToi_viewListener, int numOfLoad, int distance) {
        this.layDanhSachQuanAnGanToi_viewListener = layDanhSachQuanAnGanToi_viewListener;
        getDanhSachQuanAnGanToi(numOfLoad, distance);
    }


    private int numOfItemGanToi = 0;
    private int totalItemGanToi = 0;

    public void getDanhSachQuanAnGanToi(final int numOfLoad, final int DISTANCE_NEAR_ME) {

        numOfItemGanToi = 0;
        totalItemGanToi = 0;

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<QuanAnDTO> itemGanTois = new ArrayList<>();

                //Toast.makeText(getContext(),"cacscascasc",Toast.LENGTH_SHORT).show();
                DataSnapshot dataQuanAn = dataSnapshot.child(Common.KEY_CODE.NODE_QUANANS);
                int dem = 0;
                for (DataSnapshot valueQuanAn : dataQuanAn.getChildren()
                        ) {

                    QuanAnDTO item = valueQuanAn.getValue(QuanAnDTO.class);
                    item.setIdquanan(valueQuanAn.getKey());
                    LatLng latLngCurrent = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
                    LatLng latLngQuanAn = new LatLng(item.getLatitude(), item.getLongitude());
                    if (Double.parseDouble(CalculationByDistance.getKm(latLngCurrent, latLngQuanAn)) > DISTANCE_NEAR_ME)
                        continue;
                    dem++;
                    if (dem > numOfLoad)
                        break;
                    //lấy data hình ảnh quán ăn
                    DataSnapshot dataHinhAnh = dataSnapshot.child(Common.KEY_CODE.NODE_HINHQUANANS).child(valueQuanAn.getKey());
                    List<String> listHinhAnh = new ArrayList<>();
                    for (DataSnapshot valueHinhAnh : dataHinhAnh.getChildren()
                            ) {
                        listHinhAnh.add(valueHinhAnh.getValue(String.class));
                    }
                    item.setHinhquanan(listHinhAnh);
                    itemGanTois.add(item);


                }


                if (itemGanTois.size() == 0) {
                    layDanhSachQuanAnGanToi_viewListener.onEmpty_LayDanhSachQuanAnGanToi();
                    return;
                }
                // Duyệt list Quán Ăn để đổi idhinhanh thành link url
                totalItemGanToi = itemGanTois.size();
                for (int j = 0; j < itemGanTois.size(); j++) {
                    if (itemGanTois.get(j).getHinhquanan().size() != 0) {

                        final int n = j;
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.FOLDER_HINHQUANAN).child(itemGanTois.get(n).getHinhquanan().get(0));
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                numOfItemGanToi++;
                                itemGanTois.get(n).getHinhquanan().set(0, uri.toString());

                                if (numOfItemGanToi == totalItemGanToi) {
                                    layDanhSachQuanAnGanToi_viewListener.onSuccess_LayDanhSachQuanAnGanToi(itemGanTois);
                                }

                            }
                        });


                    } else {
                        numOfItemGanToi++;
                        if (numOfItemGanToi == totalItemGanToi) {
                            layDanhSachQuanAnGanToi_viewListener.onSuccess_LayDanhSachQuanAnGanToi(itemGanTois);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                layDanhSachQuanAnGanToi_viewListener.onFailed_LayDanhSachQuanAnGanToi(databaseError.getMessage());
            }

        };
        MainActivity.firebaseDatabase.getReference().addListenerForSingleValueEvent(valueEventListener);

    }
}
