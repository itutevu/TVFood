package com.example.user.tvfood.UI;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionDiaDiem;
import com.example.user.tvfood.Common.SessionIDTinhThanh;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.Local_Address.Activity_ChonTinhThanh;
import com.example.user.tvfood.Local_Address.CustomAdapterExpandableList;
import com.example.user.tvfood.Local_Address.DuongDTO;
import com.example.user.tvfood.Local_Address.Parent_ExpandableListDTO;
import com.example.user.tvfood.Local_Address.QuanHuyenDTO;
import com.example.user.tvfood.Local_Address.SQLiteDataController;
import com.example.user.tvfood.Local_Address.SQLiteDuongController;
import com.example.user.tvfood.Local_Address.SQLiteQuanTPHCMController;
import com.example.user.tvfood.Local_Address.SQLiteTinhThanhController;
import com.example.user.tvfood.Local_Address.TinhThanhPhoDTO;
import com.example.user.tvfood.Model.EventFillter;
import com.example.user.tvfood.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_DiaChi extends Fragment implements View.OnClickListener {
    private static int KEYCODE = 101;
    private TextView txt_DoiTinhThanh, txt_DiaChi;
    private LinearLayout linear_TinhThanh;

    private HashMap<Parent_ExpandableListDTO, List<DuongDTO>> mData;
    private ExpandableListView expandableListView;
    private CustomAdapterExpandableList customAdapterExpandableList;

    private int idTinhThanh = -1;

    private ArrayList<Parent_ExpandableListDTO> parentExpandableListDTOs = new ArrayList<>();
    private SQLiteTinhThanhController sqLiteTinhThanhController;
    private SQLiteQuanTPHCMController sqLiteQuanTPHCMController;

    private SQLiteDuongController sqLiteDuongController;
    SessionIDTinhThanh sessionIDTinhThanh;

    SessionUser sessionUser;
    private String tokenID = "";

    public Tab_DiaChi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab__dia_chi, container, false);
        initView(v);
        sessionUser = new SessionUser(getContext());
        if (sessionUser.getUserDTO() != null)
            tokenID = sessionUser.getUserDTO().getId();

        sqLiteTinhThanhController = new SQLiteTinhThanhController(getContext());
        sqLiteQuanTPHCMController = new SQLiteQuanTPHCMController(getContext());
        sqLiteDuongController = new SQLiteDuongController(getContext());

        sessionIDTinhThanh = new SessionIDTinhThanh(getContext());

        //set data cho expandableListView
        mData = new HashMap<>();
        customAdapterExpandableList = new CustomAdapterExpandableList(getContext(), parentExpandableListDTOs, mData);
        expandableListView.setAdapter(customAdapterExpandableList);


        setData_ExpandableListView(sessionIDTinhThanh.getTinhThanhPhoDTO().getId());
        txt_DiaChi.setText(sqLiteTinhThanhController.getNameTinhThanh(sessionIDTinhThanh.getTinhThanhPhoDTO().getId()));
        // Inflate the layout for this fragment

        setClick_ExpandableListView();


        return v;
    }



    private void setClick_ExpandableListView() {
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Tab_TrangChu.setDT();
                SessionDiaDiem.getInstance().setKEY_QuanHuyen(Common.KEY_SORT.KEY_QUANHUYEN_TRUE);
                SessionDiaDiem.getInstance().setKEY_Duong(Common.KEY_SORT.KEY_DUONG_FALSE);
                SessionDiaDiem.getInstance().setKEY_ThanhPho(Common.KEY_SORT.KEY_THANHPHO_FALSE);
                SessionDiaDiem.getInstance().setVALUE_QuanHuyen(parentExpandableListDTOs.get(i).getIdQuanHuyen());


                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));

                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                return true;
            }
        });


    }

    private void initView(View v) {
        linear_TinhThanh = v.findViewById(R.id.linear_TinhThanh);
        linear_TinhThanh.setOnClickListener(this);
        txt_DiaChi = v.findViewById(R.id.txt_DiaChi);
        txt_DoiTinhThanh = v.findViewById(R.id.txt_DoiTinhThanh);
        txt_DoiTinhThanh.setOnClickListener(this);

        expandableListView = v.findViewById(R.id.expandableListView);
    }

    private void setData_ExpandableListView(int idTinhThanh) {
        parentExpandableListDTOs.clear();
        mData.clear();


        ArrayList<QuanHuyenDTO> list_QuanHuyen = new ArrayList<>();
        ArrayList<String> list_SoDuong = new ArrayList<>();
        list_QuanHuyen = sqLiteQuanTPHCMController.getListQuanHuyen(idTinhThanh);
        list_SoDuong = sqLiteDuongController.getListSoDuong(idTinhThanh);

        // Thêm số đường của những quận huyện chưa có đường
        for (int i = list_SoDuong.size(); i < list_QuanHuyen.size(); i++) {
            list_SoDuong.add("0 ");
        }


        // Set data cho parentExpandableListDTOs
        for (int i = 0; i < list_QuanHuyen.size(); i++) {
            parentExpandableListDTOs.add(new Parent_ExpandableListDTO(list_QuanHuyen.get(i).getId(), list_QuanHuyen.get(i).getTenQuan(), list_SoDuong.get(i)));
        }


        for (int i = 0; i < list_QuanHuyen.size(); i++) {
            List<DuongDTO> listTenDuong = new ArrayList<>();

            listTenDuong = sqLiteDuongController.getListDuong(list_QuanHuyen.get(i).getId(), idTinhThanh);

            mData.put(parentExpandableListDTOs.get(i), listTenDuong);

        }
        customAdapterExpandableList.notifyDataSetChanged();

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.linear_TinhThanh: {
                SessionDiaDiem.getInstance().setKEY_QuanHuyen(Common.KEY_SORT.KEY_QUANHUYEN_FALSE);
                SessionDiaDiem.getInstance().setKEY_Duong(Common.KEY_SORT.KEY_DUONG_FALSE);
                SessionDiaDiem.getInstance().setKEY_ThanhPho(Common.KEY_SORT.KEY_THANHPHO_TRUE);
                SessionDiaDiem.getInstance().setVALUE_ThanhPho(sessionIDTinhThanh.getTinhThanhPhoDTO().getId());

                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));

                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);

                break;
            }

            case R.id.txt_DoiTinhThanh: {
                Intent intent = new Intent(getActivity(), Activity_ChonTinhThanh.class);
                this.startActivityForResult(intent, Tab_DiaChi.KEYCODE);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Tab_DiaChi.KEYCODE) {
            if (resultCode == Activity_ChonTinhThanh.KEY_CODE_RESULT) {

                int id = data.getIntExtra(Activity_ChonTinhThanh.TAG_ID_TINHTHANH, -1);
                if (idTinhThanh != id) {
                    idTinhThanh = id;
                    if (idTinhThanh == -1) {
                        Toast.makeText(getContext(), getResources().getString(R.string.thongbao2), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setData_ExpandableListView(idTinhThanh);
                    String tinhThanh = sqLiteTinhThanhController.getNameTinhThanh(idTinhThanh);
                    txt_DiaChi.setText(tinhThanh);

                    sessionIDTinhThanh.createSessionIDTinhThanh(new TinhThanhPhoDTO(idTinhThanh, tinhThanh));
                }
            }
        }
    }
}
