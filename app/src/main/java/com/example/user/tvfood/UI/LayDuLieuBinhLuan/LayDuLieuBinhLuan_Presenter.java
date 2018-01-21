package com.example.user.tvfood.UI.LayDuLieuBinhLuan;

import android.net.Uri;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.UI.MainActivity;
import com.example.user.tvfood.Model.BinhLuanDTO;
import com.example.user.tvfood.Model.UserDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 13/10/2017.
 */

public class LayDuLieuBinhLuan_Presenter {
    private LayDuLieuBinhLuan_ViewListener layDuLieuBinhLuan_viewListener;


    public void receiveHandle(LayDuLieuBinhLuan_ViewListener layDuLieuBinhLuan_viewListener, int numOfLoad, String idQuanAn, boolean isMore, String endAt, final int sizeCurrent, String tokenID) {
        this.layDuLieuBinhLuan_viewListener = layDuLieuBinhLuan_viewListener;
        if (isMore) {
            getDataBinhLuanMore(endAt, numOfLoad, idQuanAn, sizeCurrent, tokenID);
        } else {
            getDataBinhLuan(numOfLoad, idQuanAn, tokenID);
        }
    }


    private int numOfItemImage = 0;

    public synchronized int getNumOfItemImage() {
        synchronized (this) {
            return numOfItemImage;
        }
    }

    public synchronized void setNumOfItemImage(int numOfItemImage) {
        synchronized (this) {
            this.numOfItemImage = numOfItemImage;
        }
    }


    private int soBinhLuan = 0;

    private void getDataBinhLuan(int numOfLoad, final String idQuanAn, final String tokenID) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<BinhLuanDTO> listBinhLuan = new ArrayList<>();
                final List<BinhLuanDTO> binhLuanDTOs = new ArrayList<>();
                for (DataSnapshot dataBinhLuan : dataSnapshot.getChildren()
                        ) {
                    BinhLuanDTO binhLuanDTO = dataBinhLuan.getValue(BinhLuanDTO.class);
                    binhLuanDTO.setIdbinhluan(dataBinhLuan.getKey());
                    binhLuanDTO.setIdquanan(idQuanAn + "");
                    binhLuanDTOs.add(binhLuanDTO);


                }
                if (binhLuanDTOs.size() == 0) {
                    setNumOfItemImage(0);
                    layDuLieuBinhLuan_viewListener.onEmpty("", false);

                    return;
                }


                for (int i = 0; i < binhLuanDTOs.size(); i++) {
                    final int n = i;
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_YEUTHICHBINHLUANS).child(idQuanAn).child(binhLuanDTOs.get(n).getIdbinhluan()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long soLuotThich = dataSnapshot.getChildrenCount();
                            binhLuanDTOs.get(n).setLuotthich(String.valueOf(soLuotThich));
                            if (dataSnapshot.hasChild(tokenID))
                                binhLuanDTOs.get(n).setYeuThich(true);
                            soBinhLuan++;
                            if (soBinhLuan == binhLuanDTOs.size()) {
                                soBinhLuan = 0;
                                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THANHVIENS).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (int i = 0; i < binhLuanDTOs.size(); i++)
                                                    for (DataSnapshot dataThanhVien : dataSnapshot.getChildren()
                                                            ) {
                                                        if (binhLuanDTOs.get(i).getUserid().equals(dataThanhVien.getKey())) {
                                                            UserDTO itemUser = dataThanhVien.getValue(UserDTO.class);
                                                            binhLuanDTOs.get(i).setUserDTO(itemUser);
                                                            break;
                                                        }
                                                    }

                                                for (int i = binhLuanDTOs.size() - 1; i >= 0; i--) {
                                                    listBinhLuan.add(binhLuanDTOs.get(i));
                                                }


                                                for (int i = 0; i < listBinhLuan.size(); i++) {
                                                    if (listBinhLuan.get(i).getUrlimage().equals("null")) {
                                                        setNumOfItemImage(getNumOfItemImage() + 1);
                                                        if (getNumOfItemImage() == listBinhLuan.size()) {
                                                            setNumOfItemImage(0);
                                                            layDuLieuBinhLuan_viewListener.onSuccessLayDuLieuBinhLuan(listBinhLuan, false);
                                                            break;
                                                        }
                                                    } else {
                                                        final int n = i;
                                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.HINHQUANAN).child(listBinhLuan.get(n).getUrlimage());
                                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                listBinhLuan.get(n).setUrlimage(uri.toString());
                                                                setNumOfItemImage(getNumOfItemImage() + 1);
                                                                if (getNumOfItemImage() == listBinhLuan.size()) {
                                                                    setNumOfItemImage(0);
                                                                    layDuLieuBinhLuan_viewListener.onSuccessLayDuLieuBinhLuan(listBinhLuan, false);

                                                                }
                                                            }
                                                        });
                                                    }

                                                }


                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                layDuLieuBinhLuan_viewListener.onFailedLayDuLieuBinhLuan("Đã có lỗi xảy ra", false);
                                            }
                                        }
                                );
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_BINHLUANS).child(idQuanAn + "").orderByKey().limitToLast(numOfLoad).addListenerForSingleValueEvent(valueEventListener);
    }


    private void getDataBinhLuanMore(final String endAt, int numOfLoad, final String idQuanAn, final int sizeCurrent, final String tokenID) {


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<BinhLuanDTO> listBinhLuan = new ArrayList<>();
                final List<BinhLuanDTO> binhLuanDTOs = new ArrayList<>();
                for (DataSnapshot dataBinhLuan : dataSnapshot.getChildren()
                        ) {
                    if (dataBinhLuan.getKey().equals(endAt))
                        break;
                    BinhLuanDTO binhLuanDTO = dataBinhLuan.getValue(BinhLuanDTO.class);
                    binhLuanDTO.setIdbinhluan(dataBinhLuan.getKey());
                    binhLuanDTO.setIdquanan(idQuanAn + "");
                    binhLuanDTOs.add(binhLuanDTO);
                }
                if (binhLuanDTOs.size() == 0) {
                    setNumOfItemImage(0);
                    layDuLieuBinhLuan_viewListener.onEmpty("", true);

                    return;

                }

                for (int i = 0; i < binhLuanDTOs.size(); i++) {
                    final int n = i;
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_YEUTHICHBINHLUANS).child(idQuanAn).child(binhLuanDTOs.get(n).getIdbinhluan()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long soLuotThich = dataSnapshot.getChildrenCount();
                            binhLuanDTOs.get(n).setLuotthich(String.valueOf(soLuotThich));
                            if (dataSnapshot.hasChild(tokenID))
                                binhLuanDTOs.get(n).setYeuThich(true);
                            soBinhLuan++;
                            if (soBinhLuan == binhLuanDTOs.size()) {
                                soBinhLuan = 0;
                                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THANHVIENS).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (int i = 0; i < binhLuanDTOs.size(); i++)
                                                    for (DataSnapshot dataThanhVien : dataSnapshot.getChildren()
                                                            ) {
                                                        if (binhLuanDTOs.get(i).getUserid().equals(dataThanhVien.getKey())) {

                                                            UserDTO itemUser = dataThanhVien.getValue(UserDTO.class);
                                                            binhLuanDTOs.get(i).setUserDTO(itemUser);
                                                            break;
                                                        }
                                                    }


                                                for (int i = binhLuanDTOs.size() - 1; i >= 0; i--) {
                                                    listBinhLuan.add(binhLuanDTOs.get(i));
                                                }


                                                // Duyệt đổi thành link urlimage
                                                for (int i = 0; i < listBinhLuan.size(); i++) {
                                                    if (listBinhLuan.get(i).getUrlimage().equals("null")) {
                                                        setNumOfItemImage(getNumOfItemImage() + 1);
                                                        if (getNumOfItemImage() == listBinhLuan.size()) {
                                                            setNumOfItemImage(0);
                                                            layDuLieuBinhLuan_viewListener.onSuccessLayDuLieuBinhLuan(listBinhLuan, true);
                                                        }

                                                    } else {
                                                        final int n = i;
                                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.HINHQUANAN).child(listBinhLuan.get(n).getUrlimage());
                                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                listBinhLuan.get(n).setUrlimage(uri.toString());
                                                                setNumOfItemImage(getNumOfItemImage() + 1);
                                                                if (getNumOfItemImage() == listBinhLuan.size()) {
                                                                    setNumOfItemImage(0);
                                                                    layDuLieuBinhLuan_viewListener.onSuccessLayDuLieuBinhLuan(listBinhLuan, true);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                layDuLieuBinhLuan_viewListener.onFailedLayDuLieuBinhLuan("Đã có lỗi xảy ra", true);

                                            }
                                        }
                                );
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        MainActivity.firebaseDatabase.getReference().child("binhluans").child(idQuanAn + "").orderByKey().limitToLast(numOfLoad).endAt(endAt).addListenerForSingleValueEvent(valueEventListener);
    }
}
