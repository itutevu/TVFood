package com.example.user.tvfood.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.Activity_ChiTietQuanAn;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 31/10/2017.
 */

public class Adapter_RecyclerView_GridBoSuuTap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_SINGLE_IMAGE = 1;
    private static final int TYPE_ITEM_MULTI_IMAGE = 2;


    private Context mContext;
    private ArrayList<QuanAnDTO> itemList;

    LayoutInflater inflater;


    public Adapter_RecyclerView_GridBoSuuTap(Context mContext, ArrayList<QuanAnDTO> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addData(QuanAnDTO item) {
        if (item == null)
            return;
        this.itemList.add(0, item);
        notifyItemInserted(0);
    }

    public void removeData(int position) {
        this.itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void appendData(List<QuanAnDTO> items) {
        if (items.isEmpty() || items.size() == 0)
            return;
        int index = this.itemList.size();
        this.itemList.addAll(items);
        ////Log.w("c//Log", "curent item size: " + itemList.size());
        notifyItemRangeInserted(index, items.size());
    }

    public void refreshData() {
        final int index = this.itemList.size();
        this.itemList.clear();
        notifyDataSetChanged();
        //notifyItemRangeRemoved(NUM_OF_HEADER, index); // trừ 2 header

    }


    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM_SINGLE_IMAGE;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {

            case TYPE_ITEM_SINGLE_IMAGE:
                //Log.w("log", "TYPE_ITEM_SINGLE_IMAGE");
                view = inflater.inflate(R.layout.item_grid_ghim, parent, false);
                viewHolder = new Adapter_RecyclerView_GridBoSuuTap.ViewHolderItemSingleImage(view);

                break;
            case TYPE_ITEM_MULTI_IMAGE:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        Log.d("sadá", i + "");
        switch (viewHolder.getItemViewType()) {


            case TYPE_ITEM_SINGLE_IMAGE: {
                final Adapter_RecyclerView_GridBoSuuTap.ViewHolderItemSingleImage viewHolderItemSingleImage = (Adapter_RecyclerView_GridBoSuuTap.ViewHolderItemSingleImage) viewHolder;
                final QuanAnDTO item = (QuanAnDTO) itemList.get(i);

                viewHolderItemSingleImage.txtDiaChi.setText(item.getDiachi());
                viewHolderItemSingleImage.txtTenQuan.setText(item.getTenquanan());


                Picasso.with(mContext).load(item.getHinhquanan().get(0)).into(viewHolderItemSingleImage.imageView);
                if (CalculationByTime.SoSanhTime(item.getGiomocua()) && !CalculationByTime.SoSanhTime(item.getGiodongcua())) {
                    viewHolderItemSingleImage.imgOpen.setBackgroundResource(R.drawable.custom_icon_open);
                } else {
                    viewHolderItemSingleImage.imgOpen.setBackgroundResource(R.drawable.custom_icon_close);
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolderItemSingleImage.imageView.setTransitionName("imageBanner");
                }

                viewHolderItemSingleImage.linear_GanToi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
                        intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());

                        Adapter_List_TrangChu.setUrlImage(item.getHinhquanan().get(0));
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext,
                                        viewHolderItemSingleImage.imageView,
                                        ViewCompat.getTransitionName(viewHolderItemSingleImage.imageView));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mContext.startActivity(intent, options.toBundle());
                        } else
                            mContext.startActivity(intent);

                    }
                });


                break;
            }

            case TYPE_ITEM_MULTI_IMAGE:
                break;
        }
    }


    private static class ViewHolderItemSingleImage extends RecyclerView.ViewHolder {

        TextView txtTenQuan;
        TextView txtDiaChi;
        ImageView imageView;
        ImageView imgOpen;
        LinearLayout linear_GanToi;


        ViewHolderItemSingleImage(View v) {
            super(v);

            txtTenQuan = (TextView) v.findViewById(R.id.txt_TenQuan);
            txtDiaChi = (TextView) v.findViewById(R.id.txt_DiaChi);
            imageView = (ImageView) v.findViewById(R.id.image);
            imgOpen = (ImageView) v.findViewById(R.id.img_open);
            linear_GanToi = v.findViewById(R.id.linear_GanToi);

        }
    }


}
