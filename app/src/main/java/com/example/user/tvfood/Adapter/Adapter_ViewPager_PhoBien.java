package com.example.user.tvfood.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.tvfood.Model.detailDTO;
import com.example.user.tvfood.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER on 12/09/2017.
 */

public class Adapter_ViewPager_PhoBien extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<detailDTO> detailDTOs;

    public Adapter_ViewPager_PhoBien(Context context,List<detailDTO> detailDTOs) {
        this.context = context;
        this.detailDTOs=detailDTOs;
    }

    @Override
    public int getCount() {
        return detailDTOs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view;
        view = layoutInflater.inflate(R.layout.item, container, false);
        TextView txt = (TextView) view.findViewById(R.id.txt_item);
        ImageView img = (ImageView) view.findViewById(R.id.img_item);

        txt.setText(detailDTOs.get(position).getName());

        Picasso.with(context).load(detailDTOs.get(position).getUrlImage()).into(img);


        ViewPager vp=(ViewPager) container;
        vp.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp=(ViewPager) container;
        View view=(View) object;
        vp.removeView(view);
    }
}
