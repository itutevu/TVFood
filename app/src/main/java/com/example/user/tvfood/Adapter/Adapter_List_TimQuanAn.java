package com.example.user.tvfood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.CalculationByDistance;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.Activity_ChiTietQuanAn;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by USER on 23/09/2017.
 */

public class Adapter_List_TimQuanAn extends BaseAdapter {
    private List<QuanAnDTO> mCustomList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public Adapter_List_TimQuanAn(Context context, List<QuanAnDTO> mCustomList) {
        this.mContext = context;
        this.mCustomList = mCustomList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Adapter_List_TimQuanAn.ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_list_timquanan, null);
            holder = getViewHodler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Adapter_List_TimQuanAn.ViewHolder) convertView.getTag();
        }

        final QuanAnDTO item = setDatasourceViewHolder(position, holder);


        return convertView;
    }

    public QuanAnDTO setDatasourceViewHolder(final int position, final Adapter_List_TimQuanAn.ViewHolder holder) {


        final QuanAnDTO item = (QuanAnDTO) getItem(position);


        holder.txtTenQuan.setText(item.getTenquanan());
        holder.txtDiaChi.setText(item.getDiachi());
        if (SessionLocation.getIntance().getLatitude() != -9999) {
            LatLng latLngCurrent = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
            LatLng latLngQuanAn = new LatLng(item.getLatitude(), item.getLongitude());
            String km = CalculationByDistance.getKm(latLngCurrent, latLngQuanAn);
            holder.txtKhoangCach.setText(km + " km");
        } else
            holder.txtKhoangCach.setText("... km");

        holder.linear_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adapter_List_TrangChu.setUrlImage("");
                Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
                intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        return item;

    }

    public Adapter_List_TimQuanAn.ViewHolder getViewHodler(View convertView) {
        Adapter_List_TimQuanAn.ViewHolder holder = new Adapter_List_TimQuanAn.ViewHolder();
        holder.txtTenQuan = (TextView) convertView.findViewById(R.id.txt_TenQuan);
        holder.txtDiaChi = (TextView) convertView.findViewById(R.id.txt_DiaChi);
        holder.txtKhoangCach = (TextView) convertView.findViewById(R.id.txt_KhoangCach);
        holder.linear_Main = convertView.findViewById(R.id.linear_Main);


        return holder;
    }

    public class ViewHolder {
        private TextView txtTenQuan;
        private TextView txtDiaChi;
        private TextView txtKhoangCach;
        private LinearLayout linear_Main;

    }
}
