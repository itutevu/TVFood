package com.example.user.tvfood.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Model.BinhLuanDTO;
import com.example.user.tvfood.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by USER on 06/08/2017.
 */

public class Adapter_List_BinhLuan_Detail extends BaseAdapter {
    private List<BinhLuanDTO> mCustomList;
    private LayoutInflater mLayoutInflater;
    Activity activity;
    private Context mContext;

    public Adapter_List_BinhLuan_Detail(Context context, List<BinhLuanDTO> mCustomList, Activity activity) {
        this.mContext = context;
        this.mCustomList = mCustomList;
        this.activity=activity;
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
        final Adapter_List_BinhLuan_Detail.ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_list_binhluan2, null);
            holder = getViewHodler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Adapter_List_BinhLuan_Detail.ViewHolder) convertView.getTag();
        }

        BinhLuanDTO item = setDatasourceViewHolder(position, holder);

        return convertView;
    }


    public BinhLuanDTO setDatasourceViewHolder(final int position, final Adapter_List_BinhLuan_Detail.ViewHolder holder) {

        final BinhLuanDTO item = (BinhLuanDTO) getItem(position);

        Picasso.with(mContext).load(item.getUserDTO().getUrlavatar()).into(holder.img_Ava);
        if (item.getNoidung().isEmpty())
            holder.txt_Comment.setVisibility(View.GONE);
        else
            holder.txt_Comment.setVisibility(View.VISIBLE);
        holder.txt_Comment.setText(item.getNoidung());
        holder.txtTeTaiKhoan.setText(item.getUserDTO().getHoten());


        if (item.getUrlimage().equals("null"))
            holder.rela_Image.setVisibility(View.GONE);
        else {
            holder.rela_Image.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(item.getUrlimage()).into(holder.img_Image);
        }

        String Idbinhluan = item.getIdbinhluan().substring(0, 14);
        if (item.isNew()) {
            holder.txt_Time.setText(Html.fromHtml("<font color='#4a90e2'>" + CalculationByTime.GetTime(Idbinhluan, mContext, activity) + "</font>"));
        } else {
            holder.txt_Time.setText(CalculationByTime.GetTime(Idbinhluan, mContext, activity));
        }


        return item;

    }

    public Adapter_List_BinhLuan_Detail.ViewHolder getViewHodler(View convertView) {
        Adapter_List_BinhLuan_Detail.ViewHolder holder = new Adapter_List_BinhLuan_Detail.ViewHolder();
        holder.img_Ava = (CircleImageView) convertView.findViewById(R.id.img_Ava);
        holder.txt_Time = convertView.findViewById(R.id.txt_Time);
        holder.img_Image = (ImageView) convertView.findViewById(R.id.img_Image);
        holder.txtTeTaiKhoan = (TextView) convertView.findViewById(R.id.txtTeTaiKhoan);
        holder.txt_Comment = (TextView) convertView.findViewById(R.id.txt_Comment);
        holder.rela_Image = convertView.findViewById(R.id.rela_Image);

        return holder;
    }

    public class ViewHolder {

        private TextView txt_Time;
        private CircleImageView img_Ava;
        private TextView txtTeTaiKhoan;
        private TextView txt_Comment;
        private ImageView img_Image;
        private RelativeLayout rela_Image;
    }
}

