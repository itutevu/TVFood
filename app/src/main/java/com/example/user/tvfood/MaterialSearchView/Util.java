package com.example.user.tvfood.MaterialSearchView;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by USER on 01/12/2017.
 */

public class Util {
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

    public static void showSnackBarMessage(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
