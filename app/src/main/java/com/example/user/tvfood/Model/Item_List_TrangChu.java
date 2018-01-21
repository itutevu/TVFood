package com.example.user.tvfood.Model;

import java.util.ArrayList;

/**
 * Created by USER on 01/08/2017.
 */

public class Item_List_TrangChu {
    private String txtTenQuan;
    private String txtDiaChi;
    private String txtDiemDanhGia;
    private String txtKhoangCach;
    private String imgImage;
    private boolean isOrder;
    private String txtSoBinhLuan;
    private String txtSoHinhAnh;
    private boolean isOpen;
    private ArrayList<Comment> commentArrayList;

    public String getImgImage() {
        return imgImage;
    }

    public void setImgImage(String imgImage) {
        this.imgImage = imgImage;
    }

    public Item_List_TrangChu() {
    }

    public String getTxtTenQuan() {
        return txtTenQuan;
    }

    public void setTxtTenQuan(String txtTenQuan) {
        this.txtTenQuan = txtTenQuan;
    }

    public String getTxtDiaChi() {
        return txtDiaChi;
    }

    public void setTxtDiaChi(String txtDiaChi) {
        this.txtDiaChi = txtDiaChi;
    }

    public String getTxtDiemDanhGia() {
        return txtDiemDanhGia;
    }

    public void setTxtDiemDanhGia(String txtDiemDanhGia) {
        this.txtDiemDanhGia = txtDiemDanhGia;
    }

    public String getTxtKhoangCach() {
        return txtKhoangCach;
    }

    public void setTxtKhoangCach(String txtKhoangCach) {
        this.txtKhoangCach = txtKhoangCach;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    public String getTxtSoBinhLuan() {
        return txtSoBinhLuan;
    }

    public void setTxtSoBinhLuan(String txtSoBinhLuan) {
        this.txtSoBinhLuan = txtSoBinhLuan;
    }

    public String getTxtSoHinhAnh() {
        return txtSoHinhAnh;
    }

    public void setTxtSoHinhAnh(String txtSoHinhAnh) {
        this.txtSoHinhAnh = txtSoHinhAnh;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public ArrayList<Comment> getCommentArrayList() {
        return commentArrayList;
    }

    public void setCommentArrayList(ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    public Item_List_TrangChu(String txtTenQuan, String txtDiaChi, String txtDiemDanhGia, String txtKhoangCach, String imgImage, boolean isOrder, String txtSoBinhLuan, String txtSoHinhAnh, boolean isOpen, ArrayList<Comment> commentArrayList) {
        this.txtTenQuan = txtTenQuan;
        this.txtDiaChi = txtDiaChi;
        this.txtDiemDanhGia = txtDiemDanhGia;
        this.txtKhoangCach = txtKhoangCach;
        this.imgImage = imgImage;
        this.isOrder = isOrder;
        this.txtSoBinhLuan = txtSoBinhLuan;
        this.txtSoHinhAnh = txtSoHinhAnh;
        this.isOpen = isOpen;
        this.commentArrayList = commentArrayList;
    }
}
