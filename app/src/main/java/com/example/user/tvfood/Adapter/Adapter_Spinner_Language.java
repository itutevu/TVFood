package com.example.user.tvfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.tvfood.Model.SpinnerLanguage;
import com.example.user.tvfood.R;

import java.util.List;

/**
 * Created by USER on 17/11/2017.
 */

public class Adapter_Spinner_Language extends ArrayAdapter<SpinnerLanguage> {
    private List<SpinnerLanguage> objects;
    private Context context;

    public Adapter_Spinner_Language(Context context, int resourceId,
                                    List<SpinnerLanguage> objects) {
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
        View row = inflater.inflate(R.layout.item_spinner_language, parent, false);

        TextView txt_TabName = (TextView) row.findViewById(R.id.txt_TabName);
        ImageView img_Icon = row.findViewById(R.id.img_Icon);

        txt_TabName.setText(objects.get(position).getText());
        img_Icon.setImageResource(objects.get(position).getImage());

        /*if (position == 0) {//Special style for dropdown header
            label.setTextColor(context.getResources().getColor(R.color.text_hint_color));
        }*/

        return row;
    }

}