package com.example.user.tvfood;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.user.tvfood.Common.LocaleHelper;

/**
 * Created by USER on 22/12/2017.
 */

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "vi"));
        MultiDex.install(this);
    }
}
