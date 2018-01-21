package com.example.user.tvfood;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by USER on 18/11/2017.
 */

public class RecyclerViewItemDecoratorList extends RecyclerView.ItemDecoration {
    private int spaceInPixels;

    public RecyclerViewItemDecoratorList(int spaceInPixels) {
        this.spaceInPixels = spaceInPixels;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = spaceInPixels;
        outRect.right = spaceInPixels;
        outRect.bottom = spaceInPixels;
        outRect.top = spaceInPixels;

    }
}