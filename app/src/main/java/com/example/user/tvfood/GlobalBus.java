package com.example.user.tvfood;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by USER on 13/10/2017.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }

}
