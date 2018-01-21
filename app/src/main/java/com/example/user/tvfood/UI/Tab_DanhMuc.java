package com.example.user.tvfood.UI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.Common.SesstionFocus;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.Model.EventFillter;
import com.example.user.tvfood.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_DanhMuc extends Fragment implements View.OnClickListener {

    private LinearLayout linear_KhamPha, linear_AnChay, linear_AnVat, linear_Bar, linear_Buffet, linear_NuocUong, linear_NhaHang,
            linear_QuanNhau, linear_TiemBanh, linear_QuanCom, linear_Coffee;

    private TextView txt_KhamPha, txt_AnChay, txt_AnVat, txt_Bar, txt_Buffet, txt_NuocUong, txt_NhaHang,
            txt_QuanNhau, txt_TiemBanh, txt_QuanCom, txt_Coffee;

    SessionUser sessionUser;
    private String tokenID = "";
    private TextView txt_Temp;

    public Tab_DanhMuc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab__danh_muc, container, false);
        initView(v);
        sessionUser = new SessionUser(getContext());
        if (sessionUser.getUserDTO() != null)
            tokenID = sessionUser.getUserDTO().getId();


        txt_Temp = txt_KhamPha;
        txt_KhamPha.setTextColor(getResources().getColor(R.color.colorWhite));
        txt_KhamPha.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);

        return v;
    }

    private void initView(View v) {

        txt_AnChay = v.findViewById(R.id.txt_AnChay);
        txt_AnVat = v.findViewById(R.id.txt_AnVat);
        txt_Bar = v.findViewById(R.id.txt_Bar);
        txt_NuocUong = v.findViewById(R.id.txt_NuocUong);
        txt_Buffet = v.findViewById(R.id.txt_Buffet);
        txt_NhaHang = v.findViewById(R.id.txt_NhaHang);
        txt_QuanNhau = v.findViewById(R.id.txt_QuanNhau);
        txt_TiemBanh = v.findViewById(R.id.txt_TiemBanh);
        txt_QuanCom = v.findViewById(R.id.txt_QuanCom);
        txt_Coffee = v.findViewById(R.id.txt_Coffee);
        txt_KhamPha = v.findViewById(R.id.txt_KhamPha);


        linear_AnChay = v.findViewById(R.id.linear_AnChay);
        linear_AnVat = v.findViewById(R.id.linear_AnVat);
        linear_Bar = v.findViewById(R.id.linear_Bar);
        linear_NuocUong = v.findViewById(R.id.linear_NuocUong);
        linear_Buffet = v.findViewById(R.id.linear_Buffet);
        linear_NhaHang = v.findViewById(R.id.linear_NhaHang);
        linear_QuanNhau = v.findViewById(R.id.linear_QuanNhau);
        linear_TiemBanh = v.findViewById(R.id.linear_TiemBanh);
        linear_QuanCom = v.findViewById(R.id.linear_QuanCom);
        linear_Coffee = v.findViewById(R.id.linear_Coffee);
        linear_KhamPha = v.findViewById(R.id.linear_KhamPha);

        linear_AnChay.setOnClickListener(this);
        linear_AnVat.setOnClickListener(this);
        linear_Bar.setOnClickListener(this);
        linear_NuocUong.setOnClickListener(this);
        linear_Buffet.setOnClickListener(this);
        linear_NhaHang.setOnClickListener(this);
        linear_QuanNhau.setOnClickListener(this);
        linear_TiemBanh.setOnClickListener(this);
        linear_QuanCom.setOnClickListener(this);
        linear_Coffee.setOnClickListener(this);
        linear_KhamPha.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.linear_KhamPha: {
                if (txt_KhamPha == txt_Temp)
                    return;
                txt_KhamPha.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_KhamPha.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_KhamPha;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_MOINHAT);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_AnChay: {
                if (txt_AnChay == txt_Temp)
                    return;
                txt_AnChay.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_AnChay.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_AnChay;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_ANCHAY);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_AnVat: {
                if (txt_AnVat == txt_Temp)
                    return;
                txt_AnVat.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_AnVat.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_AnVat;

                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_ANVAT);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_Bar: {
                if (txt_Bar == txt_Temp)
                    return;
                txt_Bar.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_Bar.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_Bar;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_BAR);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_NuocUong: {
                if (txt_NuocUong == txt_Temp)
                    return;
                txt_NuocUong.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_NuocUong.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_NuocUong;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_NUOCUONG);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_Buffet: {
                if (txt_Buffet == txt_Temp)
                    return;
                txt_Buffet.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_Buffet.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_Buffet;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_BUFFET);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_NhaHang: {
                if (txt_NhaHang == txt_Temp)
                    return;
                txt_NhaHang.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_NhaHang.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_NhaHang;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_NHAHANG);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_QuanNhau: {
                if (txt_QuanNhau == txt_Temp)
                    return;
                txt_QuanNhau.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_QuanNhau.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_QuanNhau;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_QUANNHAU);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_TiemBanh: {
                if (txt_TiemBanh == txt_Temp)
                    return;
                txt_TiemBanh.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_TiemBanh.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                //txt_TiemBanh.setBackgroundColor(getResources().getColor(R.color.colorBottomBar));
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_TiemBanh;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_TIEMBANH);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_QuanCom: {
                if (txt_QuanCom == txt_Temp)
                    return;
                txt_QuanCom.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_QuanCom.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_QuanCom;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_QUANCOM);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
            case R.id.linear_Coffee: {
                if (txt_Coffee == txt_Temp)
                    return;
                txt_Coffee.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_Coffee.setBackgroundResource(R.drawable.custom_textview_danhmuc_selected);
                if (txt_Temp != null) {
                    txt_Temp.setTextColor(getResources().getColor(R.color.colorBlack));
                    txt_Temp.setBackgroundResource(R.drawable.custom_textview_danhmuc);
                }
                txt_Temp = txt_Coffee;


                SesstionFocus.getInstance().setKEY_SORT(Common.KEY_SORT.KEY_COFFEE);
                GlobalBus.getBus().postSticky(new EventFillter("Fillter"));
                Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                break;
            }
        }
    }
}
