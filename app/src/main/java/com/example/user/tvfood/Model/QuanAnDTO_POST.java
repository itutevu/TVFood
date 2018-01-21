package com.example.user.tvfood.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valkyzone on 24/08/2017.
 */

public class QuanAnDTO_POST {

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
    private String idduong_iddanhmuc;
    private double latitude;
    private double longitude;
    private String sodienthoai = "";
    private String tenquanan;
    private String trangthai = "1";// Chờ phê duyệt


    public QuanAnDTO_POST() {
    }

    public String getIdduong_iddanhmuc() {
        return idduong_iddanhmuc;
    }

    public void setIdduong_iddanhmuc(String idduong_iddanhmuc) {
        this.idduong_iddanhmuc = idduong_iddanhmuc;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getIdquanan() {
        return idquanan;
    }

    public void setIdquanan(String idquanan) {
        this.idquanan = idquanan;
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

    public String getIdquanhuyen_iddanhmuc() {
        return idquanhuyen_iddanhmuc;
    }

    public void setIdquanhuyen_iddanhmuc(String idquanhuyen_iddanhmuc) {
        this.idquanhuyen_iddanhmuc = idquanhuyen_iddanhmuc;
    }

    public String getIdtinhthanh_iddanhmuc() {
        return idtinhthanh_iddanhmuc;
    }

    public void setIdtinhthanh_iddanhmuc(String idtinhthanh_iddanhmuc) {
        this.idtinhthanh_iddanhmuc = idtinhthanh_iddanhmuc;
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
