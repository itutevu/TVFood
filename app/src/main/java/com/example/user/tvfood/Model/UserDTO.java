package com.example.user.tvfood.Model;

/**
 * Created by USER on 30/08/2017.
 */

public class UserDTO {
    private String token;
    private String id;
    private String hoten;
    private String urlavatar;
    private String sdt;
    private String fcmToken;
    private String idFB;

    public String getIdFB() {
        return idFB;
    }

    public void setIdFB(String idFB) {
        this.idFB = idFB;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getUrlavatar() {
        return urlavatar;
    }

    public void setUrlavatar(String urlavatar) {
        this.urlavatar = urlavatar;
    }

    public UserDTO() {
    }

    public UserDTO(String token, String id, String hoten, String urlavatar, String sdt) {
        this.token = token;
        this.id = id;
        this.hoten = hoten;
        this.urlavatar = urlavatar;
        this.sdt = sdt;
    }

    public UserDTO(String token, String id, String hoten, String urlavatar, String sdt, String fcmToken, String idFB) {
        this.token = token;
        this.id = id;
        this.hoten = hoten;
        this.urlavatar = urlavatar;
        this.sdt = sdt;
        this.fcmToken = fcmToken;
        this.idFB = idFB;
    }
}
