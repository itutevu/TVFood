package com.example.user.tvfood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by USER on 21/12/2017.
 */

@SuppressLint("AppCompatCustomView")
public class ImageAnim_TapTap extends ImageView {
    public ImageAnim_TapTap(Context context) {
        super(context);
        init();
    }

    public ImageAnim_TapTap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageAnim_TapTap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageAnim_TapTap(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init() {
        setBackgroundResource(R.drawable.taptap_anim);
        final AnimationDrawable frameAnimation = (AnimationDrawable) getBackground();
        post(new Runnable(){
            public void run(){
                frameAnimation.start();
            }
        });
    }
}
