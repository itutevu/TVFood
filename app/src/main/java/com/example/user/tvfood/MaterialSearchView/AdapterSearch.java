package com.example.user.tvfood.MaterialSearchView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.tvfood.Model.DataSearch_Model;
import com.example.user.tvfood.R;

import java.util.List;

/**
 * Created by USER on 01/12/2017.
 */

public class AdapterSearch extends ArrayAdapter<DataSearch_Model> {
    private List<DataSearch_Model> objects;
    private Context context;



    public AdapterSearch(Context context, int resourceId,
                         List<DataSearch_Model> objects) {
        super(context, resourceId, objects);
        this.objects = objects;
        this.context = context;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_search, parent, false);

        TextView txt_TenQuanAn = (TextView) row.findViewById(R.id.txt_TenQuanAn);
        TextView txt_DiaChi = (TextView) row.findViewById(R.id.txt_DiaChi);


        txt_TenQuanAn.setText(objects.get(position).getTenquanan());
        txt_DiaChi.setText(objects.get(position).getDiachi());


        return row;
    }
}
