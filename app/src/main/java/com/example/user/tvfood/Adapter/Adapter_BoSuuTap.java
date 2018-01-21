package com.example.user.tvfood.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.Activity_ChiTietQuanAn;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER on 11/09/2017.
 */

public class Adapter_BoSuuTap extends BaseAdapter {
    private List<QuanAnDTO> mCustomList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    public Adapter_BoSuuTap(Context context, List<QuanAnDTO> mCustomList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Adapter_BoSuuTap.ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_grid_ghim, null);
            holder = getViewHodler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Adapter_BoSuuTap.ViewHolder) convertView.getTag();
        }
        //Log.d("adfsxncbxz",position+"");
        QuanAnDTO item = setDatasourceViewHolder(position, holder);


        return convertView;
    }

    private void setClickDetail(ImageView img) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
                mContext.startActivity(intent);
            }
        });
    }


    public QuanAnDTO setDatasourceViewHolder(final int position, final Adapter_BoSuuTap.ViewHolder holder) {
        //Log.d("agdsfhdgnhmv", position + "");
        final QuanAnDTO item = (QuanAnDTO) getItem(position);


        holder.txtDiaChi.setText(item.getDiachi());
        holder.txtTenQuan.setText(item.getTenquanan());


        Picasso.with(mContext).load(item.getHinhquanan().get(0)).into(holder.imageView);
        if (CalculationByTime.SoSanhTime(item.getGiomocua()) && !CalculationByTime.SoSanhTime(item.getGiodongcua())) {
            holder.imgOpen.setBackgroundResource(R.drawable.custom_icon_open);
        } else {
            holder.imgOpen.setBackgroundResource(R.drawable.custom_icon_close);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imageView.setTransitionName("imageBanner");
        }

        holder.linear_GanToi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
                intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());

                Adapter_List_TrangChu.setUrlImage(item.getHinhquanan().get(0));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext,
                                holder.imageView,
                                ViewCompat.getTransitionName(holder.imageView));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mContext.startActivity(intent, options.toBundle());
                } else
                    mContext.startActivity(intent);
               
            }
        });

        //holder.txtSoLike.setText(String.valueOf(item.getSoLike()));


        return item;

    }

    public Adapter_BoSuuTap.ViewHolder getViewHodler(View convertView) {
        Adapter_BoSuuTap.ViewHolder holder = new Adapter_BoSuuTap.ViewHolder();
        holder.txtTenQuan = (TextView) convertView.findViewById(R.id.txt_TenQuan);
        holder.txtDiaChi = (TextView) convertView.findViewById(R.id.txt_DiaChi);
        holder.imageView = (ImageView) convertView.findViewById(R.id.image);
        holder.imgOpen = (ImageView) convertView.findViewById(R.id.img_open);
        holder.linear_GanToi = convertView.findViewById(R.id.linear_GanToi);

        return holder;
    }

    public class ViewHolder {

        TextView txtTenQuan;
        TextView txtDiaChi;
        ImageView imageView;
        ImageView imgOpen;
        LinearLayout linear_GanToi;

    }
}
