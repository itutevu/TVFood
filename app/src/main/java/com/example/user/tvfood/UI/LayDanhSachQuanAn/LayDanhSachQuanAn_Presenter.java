package com.example.user.tvfood.UI.LayDanhSachQuanAn;

import android.net.Uri;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionDiaDiem;
import com.example.user.tvfood.Controller.Interface.Tab_TrangChu_Interface;
import com.example.user.tvfood.UI.MainActivity;
import com.example.user.tvfood.Model.BinhLuanDTO;
import com.example.user.tvfood.Model.QuanAnDTO;
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
 * Created by USER on 29/10/2017.
 */

public class LayDanhSachQuanAn_Presenter {
    private LayDanhSachQuanAn_ViewListener layDanhSachQuanAn_viewListener;


    public void receiveHandle(LayDanhSachQuanAn_ViewListener layDanhSachQuanAn_viewListener, long KEY_SORT, int numOfLoad, String tokenID, int numOfCurrent) {
        this.layDanhSachQuanAn_viewListener = layDanhSachQuanAn_viewListener;
        getDataQuanAn(KEY_SORT, numOfLoad, tokenID, numOfCurrent);
    }


    private int numOfImage = 0;
    private ArrayList<QuanAnDTO> quanAnDTOs = new ArrayList<>();

    public void getDataQuanAn(long KEY_SORT, final int numOfLoad, String tokenID, final int numOfCurrent) {
        numOfImage = 0;
        Tab_TrangChu_Interface tab_trangChu_interface = new Tab_TrangChu_Interface() {
            @Override
            public void getDanhSachQuanAnDTO(final List<QuanAnDTO> quanAnDTOList) {
                quanAnDTOs.clear();
                if (quanAnDTOList.size() == 0) {
                    layDanhSachQuanAn_viewListener.onFailed_LayDanhSachQuanAn("");

                    return;
                }
                for (QuanAnDTO item : quanAnDTOList)
                    quanAnDTOs.add(item);

                //Trả biến đếm về 0
                numOfImage = 0;
                //Duyệt đỏi idhinhanh thành link url


                for (int i = 0; i < quanAnDTOs.size(); i++) {
                    final int n = i;
                    if (quanAnDTOs.get(n).getHinhquanan().size() != 0) {

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.FOLDER_HINHQUANAN).child(quanAnDTOs.get(n).getHinhquanan().get(0));
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                quanAnDTOs.get(n).getHinhquanan().set(0, uri.toString());
                                if (++numOfImage == quanAnDTOs.size() - (quanAnDTOs.size() - quanAnDTOList.size())) {
                                    layDanhSachQuanAn_viewListener.onSuccess_LayDanhSachQuanAn(quanAnDTOs);


                                }
                            }

                        });
                    } else {
                        if (++numOfImage == quanAnDTOs.size() - (quanAnDTOs.size() - quanAnDTOList.size())) {
                            layDanhSachQuanAn_viewListener.onSuccess_LayDanhSachQuanAn(quanAnDTOs);

                        }
                    }
                }

            }

            @Override
            public void onFailed_GetDanhSachQuanAnDTO(String message) {
                layDanhSachQuanAn_viewListener.onFailed_LayDanhSachQuanAn(message);
            }

            @Override
            public void onEmpty_GetDanhSachQuanAnDTO(String message) {
                layDanhSachQuanAn_viewListener.onEmpty_LayDanhSachQuanAn(message);
            }
        };

        getDanhSachQuanAn(KEY_SORT, tab_trangChu_interface, numOfLoad, numOfCurrent, tokenID);
    }

    int numOfComment_Check = 0;
    int numOfComment_Check2 = 0;
    boolean isCheckRequest = false;

    public void getDanhSachQuanAn(long KEY_SORT, final Tab_TrangChu_Interface tab_trangChu_interface, final int NumOfLoad, final int NumOfCurrent, final String tokenID) {

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isCheckRequest)
                    return;
                isCheckRequest = true;

                long totalItem = dataSnapshot.getChildrenCount();
                //Tab_TrangChu.NumOfItem = dataSnapshot.getChildrenCount();
                List<QuanAnDTO> quanAnDTOListTEMP = new ArrayList<>();
                int numOfItem = 0;
                for (DataSnapshot valueQuanAn : dataSnapshot.getChildren()) {
                    numOfItem++;
                    if (numOfItem > totalItem - NumOfCurrent && NumOfCurrent != 0)
                        break;
                    QuanAnDTO quanAnDTO = new QuanAnDTO();
                    quanAnDTO = valueQuanAn.getValue(QuanAnDTO.class);
                    quanAnDTO.setIdquanan(valueQuanAn.getKey());

                    quanAnDTOListTEMP.add(quanAnDTO);
                }

                final List<QuanAnDTO> quanAnDTOList = new ArrayList<>();
                for (int i = quanAnDTOListTEMP.size() - 1; i >= 0; i--)
                    quanAnDTOList.add(quanAnDTOListTEMP.get(i));

                if (quanAnDTOList.size() == 0) {
                    tab_trangChu_interface.onEmpty_GetDanhSachQuanAnDTO("Empty");
                    isCheckRequest = false;
                    return;
                }

                for (int k = 0; k < quanAnDTOList.size(); k++) {

                    final int n = k;


                    //Lấy số lượt yêu thích
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_LUOTTHICHS).child(String.valueOf(quanAnDTOList.get(n).getIdquanan())).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(tokenID) && !tokenID.isEmpty()) {
                                quanAnDTOList.get(n).setYeuThich(true);
                            }
                            quanAnDTOList.get(n).setSoyeuthich(dataSnapshot.getChildrenCount());

                            //Lấy danh sách hình ảnh
                            MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_HINHQUANANS).child(String.valueOf(quanAnDTOList.get(n).getIdquanan())).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    List<String> listHinhAnh = new ArrayList<>();
                                    for (DataSnapshot valueHinhAnh : dataSnapshot.getChildren()
                                            ) {
                                        listHinhAnh.add(valueHinhAnh.getValue(String.class));

                                    }
                                    quanAnDTOList.get(n).setHinhquanan(listHinhAnh);


                                    //Lấy điểm đánh giá
                                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_DANHGIAS).child(String.valueOf(quanAnDTOList.get(n).getIdquanan())).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot valueRating : dataSnapshot.getChildren()
                                                    ) {
                                                quanAnDTOList.get(n).setSl_danhgia(quanAnDTOList.get(n).getSl_danhgia() + 1);
                                                quanAnDTOList.get(n).setDiemdanhgia(String.valueOf(Float.parseFloat(quanAnDTOList.get(n).getDiemdanhgia()) + 2 * Float.parseFloat(valueRating.getValue(String.class))));
                                            }

                                            //Lấy dữ liệu bình luận
                                            numOfComment_Check2 = 0;
                                            final List<BinhLuanDTO> listBinhLuan = new ArrayList<>();
                                            quanAnDTOList.get(n).setBinhLuanDTOs(listBinhLuan);
                                            MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_BINHLUANS).child(String.valueOf(quanAnDTOList.get(n).getIdquanan())).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    //Lấy số bình luận
                                                    long soBinhLuan = dataSnapshot.getChildrenCount();
                                                    quanAnDTOList.get(n).setSobinhluan(soBinhLuan);




                                                    final List<BinhLuanDTO> listBinhLuanTemp = new ArrayList<>();
                                                    int numOfComment = 0;
                                                    for (DataSnapshot valueBinhLuan : dataSnapshot.getChildren()
                                                            ) {

                                                        BinhLuanDTO itemBinhLuan = valueBinhLuan.getValue(BinhLuanDTO.class);
                                                        itemBinhLuan.setIdbinhluan(valueBinhLuan.getKey());
                                                        listBinhLuanTemp.add(itemBinhLuan);
                                                    }
                                                    for (int i = listBinhLuanTemp.size() - 1; i >= 0; i--) {
                                                        numOfComment++;
                                                        if (numOfComment > 2)
                                                            break;
                                                        listBinhLuan.add(listBinhLuanTemp.get(i));

                                                    }


                                                    // Duyệt list Bình Luận lấy thông tin của user đã bình luận
                                                    if (listBinhLuan.size() == 0) {

                                                        quanAnDTOList.get(n).setBinhLuanDTOs(listBinhLuan);
                                                        if (++numOfComment_Check2 == quanAnDTOList.size()) {
                                                            quanAnDTOList.get(n).setBinhLuanDTOs(listBinhLuan);
                                                            tab_trangChu_interface.getDanhSachQuanAnDTO(quanAnDTOList);
                                                            isCheckRequest = false;
                                                        }
                                                    } else {
                                                        for (int j = 0; j < listBinhLuan.size(); j++) {
                                                            final int m = j;
                                                            MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THANHVIENS).child(listBinhLuan.get(m).getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    UserDTO itemUser = dataSnapshot.getValue(UserDTO.class);
                                                                    listBinhLuan.get(m).setUserDTO(itemUser);
                                                                    if (++numOfComment_Check == listBinhLuan.size()) {
                                                                        numOfComment_Check = 0;
                                                                        quanAnDTOList.get(n).setBinhLuanDTOs(listBinhLuan);
                                                                        if (++numOfComment_Check2 == quanAnDTOList.size()) {
                                                                            tab_trangChu_interface.getDanhSachQuanAnDTO(quanAnDTOList);
                                                                            isCheckRequest = false;
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    tab_trangChu_interface.onFailed_GetDanhSachQuanAnDTO(databaseError.getMessage());
                                                                }
                                                            });


                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    tab_trangChu_interface.onFailed_GetDanhSachQuanAnDTO(databaseError.getMessage());
                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            tab_trangChu_interface.onFailed_GetDanhSachQuanAnDTO(databaseError.getMessage());
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    tab_trangChu_interface.onFailed_GetDanhSachQuanAnDTO(databaseError.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            tab_trangChu_interface.onFailed_GetDanhSachQuanAnDTO(databaseError.getMessage());
                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        if (SessionDiaDiem.getInstance().getKEY_ThanhPho() == Common.KEY_SORT.KEY_THANHPHO_FALSE
                && SessionDiaDiem.getInstance().getKEY_QuanHuyen() == Common.KEY_SORT.KEY_QUANHUYEN_FALSE
                && SessionDiaDiem.getInstance().getKEY_Duong() == Common.KEY_SORT.KEY_DUONG_FALSE) {
            if (KEY_SORT == Common.KEY_SORT.KEY_MOINHAT)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByKey().limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_ANCHAY)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_ANCHAY).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_ANVAT)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_ANVAT).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_BAR)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_BAR).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_BUFFET)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_BUFFET).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_NHAHANG)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_NHAHANG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_NUOCUONG)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_NUOCUONG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_QUANNHAU)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_QUANNHAU).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_TIEMBANH)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_TIEMBANH).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_QUANCOM)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_QUANCOM).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else if (KEY_SORT == Common.KEY_SORT.KEY_COFFEE)
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDANHMUC).equalTo(Common.KEY_SORT.KEY_COFFEE).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            else
                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByKey().limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
        } else {
            if (SessionDiaDiem.getInstance().getKEY_QuanHuyen() == Common.KEY_SORT.KEY_QUANHUYEN_TRUE) {
                if (KEY_SORT == Common.KEY_SORT.KEY_MOINHAT)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen()).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_ANCHAY)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_ANCHAY).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_ANVAT)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_ANVAT).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_BAR)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_BAR).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_BUFFET)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_BUFFET).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_NHAHANG)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_NHAHANG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_NUOCUONG)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_NUOCUONG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_QUANNHAU)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_QUANNHAU).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_TIEMBANH)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_TIEMBANH).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_QUANCOM)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_QUANCOM).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_COFFEE)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen() + "_" + Common.KEY_SORT.KEY_COFFEE).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDQUANHUYEN).equalTo(SessionDiaDiem.getInstance().getVALUE_QuanHuyen()).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            } else if (SessionDiaDiem.getInstance().getKEY_ThanhPho() == Common.KEY_SORT.KEY_THANHPHO_TRUE) {
                if (KEY_SORT == Common.KEY_SORT.KEY_MOINHAT)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho()).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_ANCHAY)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_ANCHAY).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_ANVAT)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_ANVAT).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_BAR)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_BAR).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_BUFFET)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_BUFFET).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_NHAHANG)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_NHAHANG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_NUOCUONG)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_NUOCUONG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_QUANNHAU)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_QUANNHAU).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_TIEMBANH)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_TIEMBANH).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_QUANCOM)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_QUANCOM).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_COFFEE)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho() + "_" + Common.KEY_SORT.KEY_COFFEE).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDTINHTHANH).equalTo(SessionDiaDiem.getInstance().getVALUE_ThanhPho()).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            } else if (SessionDiaDiem.getInstance().getKEY_Duong() == Common.KEY_SORT.KEY_DUONG_TRUE) {
                if (KEY_SORT == Common.KEY_SORT.KEY_MOINHAT)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong()).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_ANCHAY)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_ANCHAY).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_ANVAT)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_ANVAT).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_BAR)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_BAR).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_BUFFET)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_BUFFET).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_NHAHANG)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_NHAHANG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_NUOCUONG)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_NUOCUONG).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_QUANNHAU)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_QUANNHAU).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_TIEMBANH)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_TIEMBANH).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_QUANCOM)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_QUANCOM).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else if (KEY_SORT == Common.KEY_SORT.KEY_COFFEE)
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG_IDDANHMUC).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong() + "_" + Common.KEY_SORT.KEY_COFFEE).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
                else
                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANANS).orderByChild(Common.KEY_CODE.CHILD_IDDUONG).equalTo(SessionDiaDiem.getInstance().getVALUE_Duong()).limitToLast(NumOfLoad + NumOfCurrent).addListenerForSingleValueEvent(valueEventListener);
            }
        }
    }
}
