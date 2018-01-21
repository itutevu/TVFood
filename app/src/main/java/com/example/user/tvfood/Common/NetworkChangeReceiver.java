package com.example.user.tvfood.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by USER on 21/11/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isOnline(context)) {
            iNetworkChange.onDisConnect();
            IsConnect.getInstance().setConnect(false);
        } else {
            iNetworkChange.onConnect();
            IsConnect.getInstance().setConnect(true);
        }
    }

    private INetworkChange iNetworkChange;

    public NetworkChangeReceiver(INetworkChange iNetworkChange) {
        this.iNetworkChange = iNetworkChange;
    }


    public boolean isOnline(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
