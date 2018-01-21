package com.example.user.tvfood.Model;

/**
 * Created by USER on 31/10/2017.
 */

public class EventFillter {
    private String message="";

    public EventFillter(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
