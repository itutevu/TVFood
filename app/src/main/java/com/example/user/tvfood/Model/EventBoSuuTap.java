package com.example.user.tvfood.Model;

/**
 * Created by USER on 18/11/2017.
 */

public class EventBoSuuTap {
    private String idEvent = "";  // 1 : Refresh ; 2 : Load ; 3 : Clear

    public EventBoSuuTap() {
    }

    public EventBoSuuTap(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }
}
