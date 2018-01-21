package com.example.user.tvfood.Common;

/**
 * Created by USER on 19/09/2017.
 */

public class SesstionFocus {
    private long KEY_SORT = Common.KEY_SORT.KEY_MOINHAT;
    private static SesstionFocus instance;

    public SesstionFocus() {

    }

    public static SesstionFocus getInstance() {
        if (instance == null)
            instance = new SesstionFocus();
        return instance;
    }

    public long getKEY_SORT() {
        return KEY_SORT;
    }

    public void setKEY_SORT(long KEY_SORT) {
        this.KEY_SORT = KEY_SORT;
    }
}
