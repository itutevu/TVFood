package com.example.user.tvfood.Model;

/**
 * Created by USER on 01/08/2017.
 */

public class Item_GanToi {
    private String Image;
    private String txtTenQuan;
    private String txtDiaChi;
    private boolean isOpen;

    public Item_GanToi() {
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
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

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Item_GanToi(String image, String txtTenQuan, String txtDiaChi, boolean isOpen) {
        Image = image;
        this.txtTenQuan = txtTenQuan;
        this.txtDiaChi = txtDiaChi;
        this.isOpen = isOpen;
    }
}
