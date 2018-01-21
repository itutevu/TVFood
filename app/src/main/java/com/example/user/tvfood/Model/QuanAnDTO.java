package com.example.user.tvfood.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valkyzone on 24/08/2017.
 */

public class QuanAnDTO {

    private String idquanan;
    private String diachi;
    private String giodongcua;
    private String giomocua;
    private long iddanhmuc;
    private long idduong;
    private long idquanhuyen;
    private long idtinhthanh;
    private String idquanhuyen_iddanhmuc;
    private String idtinhthanh_iddanhmuc;
    private double latitude;
    private double longitude;
    private String sodienthoai = "";
    private String tenquanan;
    private List<String> hinhquanan = new ArrayList<>();
    private String diemdanhgia = "5";
    private int sl_danhgia = 1;
    private int sl_camxuc1 = 0;
    private int sl_camxuc2 = 0;
    private int sl_camxuc3 = 0;
    private int sl_camxuc4 = 0;
    private int sl_camxuc5 = 0;
    private boolean isOrder;
    private boolean isYeuThich = false;
    private long sobinhluan = 0;
    private long sohinhanh = 0;
    private long soyeuthich = 0;

    public QuanAnDTO() {
    }

    public QuanAnDTO(QuanAnDTO quanAnDTO) {
        this.idquanan = quanAnDTO.getIdquanan();
        this.diachi = quanAnDTO.getDiachi();
        this.giodongcua = quanAnDTO.getGiodongcua();
        this.giomocua = quanAnDTO.getGiomocua();
        this.iddanhmuc = quanAnDTO.getIddanhmuc();
        this.idduong = quanAnDTO.getIdduong();
        this.idquanhuyen = quanAnDTO.getIdquanhuyen();
        this.idtinhthanh = quanAnDTO.getIdtinhthanh();
        this.idquanhuyen_iddanhmuc = quanAnDTO.getIdquanhuyen_iddanhmuc();
        this.idtinhthanh_iddanhmuc = quanAnDTO.getIdtinhthanh_iddanhmuc();
        this.latitude = quanAnDTO.getLatitude();
        this.longitude = quanAnDTO.getLongitude();
        this.sodienthoai = quanAnDTO.getSodienthoai();
        this.tenquanan = quanAnDTO.getTenquanan();
        this.hinhquanan = quanAnDTO.getHinhquanan();
        this.diemdanhgia = quanAnDTO.getDiemdanhgia();
        this.sl_danhgia = quanAnDTO.getSl_danhgia();
        this.sl_camxuc1 = quanAnDTO.getSl_camxuc1();
        this.sl_camxuc2 = quanAnDTO.getSl_camxuc2();
        this.sl_camxuc3 = quanAnDTO.getSl_camxuc3();
        this.sl_camxuc4 = quanAnDTO.getSl_camxuc4();
        this.sl_camxuc5 = quanAnDTO.getSl_camxuc5();
        this.isOrder = quanAnDTO.isOrder();
        this.isYeuThich = quanAnDTO.isYeuThich();
        this.sobinhluan = quanAnDTO.getSobinhluan();
        this.sohinhanh = quanAnDTO.getSohinhanh();
        this.soyeuthich = quanAnDTO.getSoyeuthich();
        this.binhLuanDTOs = quanAnDTO.getBinhLuanDTOs();
    }

    public boolean isYeuThich() {
        return isYeuThich;
    }

    public void setYeuThich(boolean yeuThich) {
        isYeuThich = yeuThich;
    }

    public String getIdtinhthanh_iddanhmuc() {
        return idtinhthanh_iddanhmuc;
    }

    public void setIdtinhthanh_iddanhmuc(String idtinhthanh_iddanhmuc) {
        this.idtinhthanh_iddanhmuc = idtinhthanh_iddanhmuc;
    }

    public String getIdquanhuyen_iddanhmuc() {
        return idquanhuyen_iddanhmuc;
    }

    public void setIdquanhuyen_iddanhmuc(String idquanhuyen_iddanhmuc) {
        this.idquanhuyen_iddanhmuc = idquanhuyen_iddanhmuc;
    }

    public int getSl_camxuc1() {
        return sl_camxuc1;
    }

    public void setSl_camxuc1(int sl_camxuc1) {
        this.sl_camxuc1 = sl_camxuc1;
    }

    public int getSl_camxuc2() {
        return sl_camxuc2;
    }

    public void setSl_camxuc2(int sl_camxuc2) {
        this.sl_camxuc2 = sl_camxuc2;
    }

    public int getSl_camxuc3() {
        return sl_camxuc3;
    }

    public void setSl_camxuc3(int sl_camxuc3) {
        this.sl_camxuc3 = sl_camxuc3;
    }

    public int getSl_camxuc4() {
        return sl_camxuc4;
    }

    public void setSl_camxuc4(int sl_camxuc4) {
        this.sl_camxuc4 = sl_camxuc4;
    }

    public int getSl_camxuc5() {
        return sl_camxuc5;
    }

    public void setSl_camxuc5(int sl_camxuc5) {
        this.sl_camxuc5 = sl_camxuc5;
    }

    public int getSl_danhgia() {
        return sl_danhgia;
    }

    public void setSl_danhgia(int sl_danhgia) {
        this.sl_danhgia = sl_danhgia;
    }

    public long getSoyeuthich() {

        return soyeuthich;
    }

    public void setSoyeuthich(long soyeuthich) {
        this.soyeuthich = soyeuthich;
    }

    public long getSohinhanh() {
        return sohinhanh;
    }

    public void setSohinhanh(long sohinhanh) {
        this.sohinhanh = sohinhanh;
    }

    public long getSobinhluan() {
        return sobinhluan;
    }

    public void setSobinhluan(long sobinhluan) {
        this.sobinhluan = sobinhluan;
    }

    private List<BinhLuanDTO> binhLuanDTOs;

    public List<BinhLuanDTO> getBinhLuanDTOs() {
        return binhLuanDTOs;
    }

    public void setBinhLuanDTOs(List<BinhLuanDTO> binhLuanDTOs) {
        this.binhLuanDTOs = binhLuanDTOs;
    }

    public String getDiemdanhgia() {
        return diemdanhgia;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
    }

    public void setDiemdanhgia(String diemdanhgia) {
        this.diemdanhgia = diemdanhgia;
    }

    public String getIdquanan() {
        return idquanan;
    }

    public void setIdquanan(String idquanan) {
        this.idquanan = idquanan;
    }

    public synchronized List<String> getHinhquanan() {
        return hinhquanan;
    }

    public void setHinhquanan(List<String> hinhquanan) {
        this.hinhquanan = hinhquanan;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getGiodongcua() {
        return giodongcua;
    }

    public void setGiodongcua(String giodongcua) {
        this.giodongcua = giodongcua;
    }

    public String getGiomocua() {
        return giomocua;
    }

    public void setGiomocua(String giomocua) {
        this.giomocua = giomocua;
    }

    public long getIddanhmuc() {
        return iddanhmuc;
    }

    public void setIddanhmuc(long iddanhmuc) {
        this.iddanhmuc = iddanhmuc;
    }

    public long getIdduong() {
        return idduong;
    }

    public void setIdduong(long idduong) {
        this.idduong = idduong;
    }

    public long getIdquanhuyen() {
        return idquanhuyen;
    }

    public void setIdquanhuyen(long idquanhuyen) {
        this.idquanhuyen = idquanhuyen;
    }

    public long getIdtinhthanh() {
        return idtinhthanh;
    }

    public void setIdtinhthanh(long idtinhthanh) {
        this.idtinhthanh = idtinhthanh;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getTenquanan() {
        return tenquanan;
    }

    public void setTenquanan(String tenquanan) {
        this.tenquanan = tenquanan;
    }


}
