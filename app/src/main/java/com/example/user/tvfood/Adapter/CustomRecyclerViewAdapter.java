package com.example.user.tvfood.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class CustomRecyclerViewAdapter extends RecyclerView
        .Adapter<CustomRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "CustomRecyclerViewAdapter";
    private ArrayList<QuanAnDTO> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView txtTenQuan;
        TextView txtDiaChi;
        ImageView imageView;
        ImageView imgOpen;
        LinearLayout linear_GanToi;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txtTenQuan = (TextView) itemView.findViewById(R.id.txt_TenQuan);
            txtDiaChi = (TextView) itemView.findViewById(R.id.txt_DiaChi);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imgOpen = (ImageView) itemView.findViewById(R.id.img_open);
            linear_GanToi = itemView.findViewById(R.id.linear_GanToi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CustomRecyclerViewAdapter(ArrayList<QuanAnDTO> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_gantoi, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.txtDiaChi.setText(mDataset.get(position).getDiachi());
        holder.txtTenQuan.setText(mDataset.get(position).getTenquanan());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.txtTenQuan.setTransitionName("imageBanner");
        }

        Picasso.with(context).load(mDataset.get(position).getHinhquanan().get(0)).into(holder.imageView);
        if (CalculationByTime.SoSanhTime(mDataset.get(position).getGiomocua()) && !CalculationByTime.SoSanhTime(mDataset.get(position).getGiodongcua())) {
            holder.imgOpen.setBackgroundResource(R.drawable.custom_icon_open);
        } else {
            holder.imgOpen.setBackgroundResource(R.drawable.custom_icon_close);
        }

    }

    public void addItem(QuanAnDTO dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
