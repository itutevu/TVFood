package com.example.user.tvfood.Local_Address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvfood.R;

import java.util.ArrayList;

/**
 * Created by USER on 11/08/2017.
 */

public class Adapter_List_TinhThanh extends BaseAdapter {
    private ArrayList<TinhThanhPhoDTO> mCustomList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public Adapter_List_TinhThanh(Context context, ArrayList<TinhThanhPhoDTO> movies) {
        this.mContext = context;
        this.mCustomList = movies;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mCustomList != null ? mCustomList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mCustomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Adapter_List_TinhThanh.ViewHolder holder;
        if (convertView == null) {
            holder = new Adapter_List_TinhThanh.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_list_tinhthanh, null);
            holder.mImage = (ImageView) convertView.findViewById(R.id.img_Image);
            holder.mTitle = (TextView) convertView.findViewById(R.id.txt_Name);
            holder.linear_main = convertView.findViewById(R.id.linear_main);
            convertView.setTag(holder);
        } else {
            holder = (Adapter_List_TinhThanh.ViewHolder) convertView.getTag();
        }
        final TinhThanhPhoDTO item = (TinhThanhPhoDTO) getItem(position);
        holder.mImage.setVisibility(View.INVISIBLE);
        holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.black_35));
        if(id_Select!=-1&&id_Select==position)
        {
            holder.mImage.setVisibility(View.VISIBLE);
            holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorRed));
        }

        holder.mTitle.setText(item.getName());
        holder.linear_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id_Select==-1) {
                    id_Select = position;
                    idTinhThanh = item.getId();
                    holder.mImage.setVisibility(View.VISIBLE);
                    holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                }
                else {
                    if(id_Select==position) {
                        id_Select = -1;
                        idTinhThanh = -1;
                        holder.mImage.setVisibility(View.INVISIBLE);
                        holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.black_35));
                    }
                    else {
                        id_Select = position;
                        idTinhThanh = item.getId();
                        holder.mImage.setVisibility(View.VISIBLE);
                        holder.mTitle.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    private int id_Select=-1;

    public void setId_Select(int id_Select) {
        this.id_Select = id_Select;
    }

    private int idTinhThanh=-1;

    public int getIdTinhThanh() {
        return idTinhThanh;
    }

    public void setIdTinhThanh(int idTinhThanh) {
        this.idTinhThanh = idTinhThanh;
    }

    public class ViewHolder {
        private ImageView mImage;
        private TextView mTitle;
        private LinearLayout linear_main;
    }
}
