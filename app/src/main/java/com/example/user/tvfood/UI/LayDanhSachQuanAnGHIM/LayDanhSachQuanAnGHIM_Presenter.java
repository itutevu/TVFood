package com.example.user.tvfood.UI.LayDanhSachQuanAnGHIM;

import android.net.Uri;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.UI.MainActivity;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 18/11/2017.
 */

public class LayDanhSachQuanAnGHIM_Presenter {
    private LayDanhSachQuanAnGHIM_ViewListener layDanhSachQuanAnGHIM_viewListener;
    private SessionUser sessionUser;


    public void receiveHandle(LayDanhSachQuanAnGHIM_ViewListener layDanhSachQuanAnGHIM_viewListener, SessionUser sessionUser, int numOfLoad, String endAt) {
        this.layDanhSachQuanAnGHIM_viewListener = layDanhSachQuanAnGHIM_viewListener;
        this.sessionUser = sessionUser;
        getDataGhim(numOfLoad, endAt);
    }


    private int itemCurrent = 0;
    private int numOfItem = 0;

    public void getDataGhim(final int numOfLoad, final String endAt) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<String> idQuanAns = new ArrayList<>();
                final List<QuanAnDTO> quanAnDTOList = new ArrayList<>();
                int dem = 0;
                for (DataSnapshot dataQuanAn : dataSnapshot.getChildren()
                        ) {

                    String idquanan = dataQuanAn.getKey();
                    dem++;
                    if (dem > Integer.parseInt(endAt))
                        idQuanAns.add(idquanan);
                    if (idQuanAns.size() == numOfLoad)
                        break;

                }
                if (idQuanAns.size() == 0) {
                    layDanhSachQuanAnGHIM_viewListener.onEmpty_LayDanhSachQuanAnGHIM("empty");
                    return;
                }

                numOfItem = idQuanAns.size();
                itemCurrent = 0;
                for (int i = idQuanAns.size() - 1; i >= 0; i--)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).child(idQuanAns.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final QuanAnDTO quanAnDTO = dataSnapshot.getValue(QuanAnDTO.class);
                            assert quanAnDTO != null;
                            quanAnDTO.setIdquanan(dataSnapshot.getKey());


                            //lấy data hình ảnh quán ăn
                            MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_HINHQUANANS).child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<String> listHinhAnh = new ArrayList<>();
                                    for (DataSnapshot valueHinhAnh : dataSnapshot.getChildren()
                                            ) {
                                        listHinhAnh.add(valueHinhAnh.getValue(String.class));
                                    }
                                    quanAnDTO.setHinhquanan(listHinhAnh);
                                    // Duyệt list Quán Ăn để đổi idhinhanh thành link url


                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhquanan").child(quanAnDTO.getHinhquanan().get(0));
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            quanAnDTO.getHinhquanan().set(0, uri.toString());
                                            quanAnDTOList.add(quanAnDTO);
                                            itemCurrent++;
                                            if (itemCurrent == numOfItem) {
                                                layDanhSachQuanAnGHIM_viewListener.onSuccess_LayDanhSachQuanAnGHIM(quanAnDTOList);
                                            }

                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    layDanhSachQuanAnGHIM_viewListener.onFailed_LayDanhSachQuanAnGHIM(databaseError.getMessage());
                                }
                            });


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            layDanhSachQuanAnGHIM_viewListener.onFailed_LayDanhSachQuanAnGHIM(databaseError.getMessage());
                        }
                    });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                layDanhSachQuanAnGHIM_viewListener.onFailed_LayDanhSachQuanAnGHIM(databaseError.getMessage());
            }
        };


        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_GHIMS).child(sessionUser.getUserDTO().getId()).orderByValue().addListenerForSingleValueEvent(valueEventListener);
    }

}
