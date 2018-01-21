package com.example.user.tvfood.Model;

/**
 * Created by Valkyzone on 11/30/2017.
 */

public class Notification_Model {
    private String title;
    private String body;
    private String notiDate;

    public Notification_Model() {
    }

    public Notification_Model(String title, String body, String notiDate) {
        this.title = title;
        this.body = body;
        this.notiDate = notiDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotiDate() {
        return notiDate;
    }

    public void setNotiDate(String notiDate) {
        this.notiDate = notiDate;
    }
}
