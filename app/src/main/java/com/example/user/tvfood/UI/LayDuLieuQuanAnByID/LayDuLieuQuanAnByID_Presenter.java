package com.example.user.tvfood.UI.LayDuLieuQuanAnByID;

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
 * Created by USER on 07/11/2017.
 */

public class LayDuLieuQuanAnByID_Presenter {
    private boolean isYeuThich = false;
    private boolean isPin = false;
    private boolean isRating = false;
    private String rating = "";


    public boolean isYeuThich() {
        return isYeuThich;
    }

    public void setYeuThich(boolean yeuThich) {
        isYeuThich = yeuThich;
    }

    public boolean isPin() {
        return isPin;
    }

    public void setPin(boolean pin) {
        isPin = pin;
    }

    public boolean isRating() {
        return isRating;
    }

    public void setRating(boolean rating) {
        isRating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private LayDuLieuQuanAnByID_ViewListener layDuLieuQuanAnByID_viewListener;


    public void receiveHandle(LayDuLieuQuanAnByID_ViewListener layDuLieuQuanAnByID_viewListener, String idQuanAn, SessionUser sessionUser) {
        this.layDuLieuQuanAnByID_viewListener = layDuLieuQuanAnByID_viewListener;
        getDataDetail(idQuanAn, sessionUser);
    }

    private QuanAnDTO quanAnDTO;
    private int dem = 0;

    public synchronized int getDem() {
        return dem;
    }

    public synchronized void setDem(int dem) {
        this.dem = dem;
    }

    private void getDataDetail(final String idQuanAn, final SessionUser sessionUser) {
        //Khởi tạo lại biến đếm
        setDem(0);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                quanAnDTO = new QuanAnDTO();
                DataSnapshot dataQuanAnDetail = dataSnapshot.child(Common.KEY_CODE.NODE_QUANANS).child(idQuanAn);
                quanAnDTO = dataQuanAnDetail.getValue(QuanAnDTO.class);
                quanAnDTO.setIdquanan(idQuanAn);

                DataSnapshot dataHinhAnh = dataSnapshot.child(Common.KEY_CODE.NODE_HINHQUANANS).child(idQuanAn);
                List<String> listHinhAnh = new ArrayList<>();
                for (DataSnapshot valueHinhAnh : dataHinhAnh.getChildren()
                        ) {
                    listHinhAnh.add(valueHinhAnh.getValue(String.class));
                }
                //Lấy điểm đánh giá
                DataSnapshot dataRating = dataSnapshot.child(Common.KEY_CODE.NODE_DANHGIAS).child(idQuanAn);

                for (DataSnapshot valueRating : dataRating.getChildren()
                        ) {
                    quanAnDTO.setSl_danhgia(quanAnDTO.getSl_danhgia() + 1);
                    quanAnDTO.setDiemdanhgia(String.valueOf(Float.parseFloat(quanAnDTO.getDiemdanhgia()) + 2 * Float.parseFloat(valueRating.getValue(String.class))));

                    if (Float.parseFloat(valueRating.getValue(String.class)) * 2 <= 2f)
                        quanAnDTO.setSl_camxuc1(quanAnDTO.getSl_camxuc1() + 1);
                    else {
                        if (Float.parseFloat(valueRating.getValue(String.class)) * 2 <= 4f)
                            quanAnDTO.setSl_camxuc2(quanAnDTO.getSl_camxuc2() + 1);
                        else {
                            if (Float.parseFloat(valueRating.getValue(String.class)) * 2 <= 6f)
                                quanAnDTO.setSl_camxuc3(quanAnDTO.getSl_camxuc3() + 1);
                            else {
                                if (Float.parseFloat(valueRating.getValue(String.class)) * 2 <= 8f)
                                    quanAnDTO.setSl_camxuc4(quanAnDTO.getSl_camxuc4() + 1);
                                else
                                    quanAnDTO.setSl_camxuc5(quanAnDTO.getSl_camxuc5() + 1);
                            }
                        }
                    }
                }
                // Lấy tổng số bình luân
                long sobinhluan = dataSnapshot.child(Common.KEY_CODE.NODE_BINHLUANS).child(idQuanAn).getChildrenCount();
                quanAnDTO.setSobinhluan(sobinhluan);


                // Kiểm tra YÊU THÍCH và GHIM
                if (sessionUser.getUserDTO().getId() != null && !sessionUser.getUserDTO().getId().isEmpty()) {
                    if (dataSnapshot.child(Common.KEY_CODE.NODE_GHIMS).child(sessionUser.getUserDTO().getId() + "").child(idQuanAn).getValue(String.class) != null) {
                        setPin(true);
                    } else {
                        setPin(false);
                    }

                    if (dataSnapshot.child(Common.KEY_CODE.NODE_LUOTTHICHS).child(idQuanAn).child(sessionUser.getUserDTO().getId() + "").getValue(String.class) != null) {
                        setYeuThich(true);
                    } else {
                        setYeuThich(false);
                    }
                }
                //Lấy thông tin đánh giá
                if (!sessionUser.getUserDTO().getId().isEmpty() && sessionUser.getUserDTO().getId() != null && dataSnapshot.child(Common.KEY_CODE.NODE_DANHGIAS).child(idQuanAn).hasChild(sessionUser.getUserDTO().getId())) {
                    String rating = dataSnapshot.child(Common.KEY_CODE.NODE_DANHGIAS).child(idQuanAn).child(sessionUser.getUserDTO().getId()).getValue(String.class);
                    setRating(rating);
                    setRating(true);
                } else {
                    setRating(false);
                }

                quanAnDTO.setHinhquanan(listHinhAnh);


                quanAnDTO.setSohinhanh(quanAnDTO.getHinhquanan().size());
                // Duyệt list HinhAnh đổi thành link url
                for (int i = 0; i < quanAnDTO.getHinhquanan().size(); i++) {
                    final int n = i;

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhquanan").child(quanAnDTO.getHinhquanan().get(n));
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            setDem(dem + 1);
                            quanAnDTO.getHinhquanan().set(n, uri.toString());
                            if (getDem() == quanAnDTO.getHinhquanan().size()) {
                                layDuLieuQuanAnByID_viewListener.onSuccess_LayDuLieuQuanAnByID(quanAnDTO, isYeuThich(), isPin(), isRating(), getRating());


                            }
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                layDuLieuQuanAnByID_viewListener.onFailed_LayDuLieuQuanAnByID(databaseError.getMessage());
            }
        };
        MainActivity.firebaseDatabase.getReference().addListenerForSingleValueEvent(valueEventListener);
    }
}
