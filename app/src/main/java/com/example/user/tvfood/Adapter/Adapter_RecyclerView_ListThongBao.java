package com.example.user.tvfood.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.Validate;
import com.example.user.tvfood.Model.Notification_Model;
import com.example.user.tvfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Valkyzone on 11/30/2017.
 */

public class Adapter_RecyclerView_ListThongBao extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_1 = 1;


    private Context mContext;
    private ArrayList<Notification_Model> itemNotificationList;

    LayoutInflater inflater;


    public Adapter_RecyclerView_ListThongBao(Context mContext, ArrayList<Notification_Model> itemNotificationList) {
        this.mContext = mContext;
        this.itemNotificationList = itemNotificationList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addData(Notification_Model item) {
        if (item == null)
            return;
        this.itemNotificationList.add(0, item);
        notifyItemInserted(0);
    }

    public void removeData(int position) {
        this.itemNotificationList.remove(position);
        notifyItemRemoved(position);
    }

    public void appendData(ArrayList<Notification_Model> items) {
        if (items.isEmpty() || items.size() == 0)
            return;
        int index = this.itemNotificationList.size();
        this.itemNotificationList.addAll(items);
        notifyItemRangeInserted(index, items.size());
    }

    public void refreshData() {
        final int index = this.itemNotificationList.size();
        this.itemNotificationList.clear();
        notifyDataSetChanged();

    }


    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM_1;
    }

    @Override
    public int getItemCount() {
        return itemNotificationList.size();
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

            case TYPE_ITEM_1: {
                view = inflater.inflate(R.layout.item_list_notification, parent, false);
                viewHolder = new ViewHolderItemSingleImage(view);

                break;
            }

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {


            case TYPE_ITEM_1: {

                final ViewHolderItemSingleImage viewHolderItemSingleImage = (ViewHolderItemSingleImage) viewHolder;
                final Notification_Model item = (Notification_Model) itemNotificationList.get(i);

                //Binding Data
                viewHolderItemSingleImage.txt_noti_title.setText(item.getTitle());
                viewHolderItemSingleImage.txt_noti_content.setText(item.getBody());

                Date date = Validate.StringToDate2(item.getNotiDate());

                viewHolderItemSingleImage.txt_noti_date.setText(Validate.DateToStringSetText(date));
                viewHolderItemSingleImage.txt_noti_time.setText(Validate.DateToStringSetText2(date));

                break;
            }

        }
    }


    private static class ViewHolderItemSingleImage extends RecyclerView.ViewHolder {

        TextView txt_noti_title;
        TextView txt_noti_content;
        TextView txt_noti_date;
        TextView txt_noti_time;


        ViewHolderItemSingleImage(View v) {
            super(v);
            txt_noti_time = v.findViewById(R.id.txt_noti_time);
            txt_noti_title = v.findViewById(R.id.txt_noti_title);
            txt_noti_content = v.findViewById(R.id.txt_noti_content);
            txt_noti_date = v.findViewById(R.id.txt_noti_date);
        }
    }


}
