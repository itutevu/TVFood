package com.example.user.tvfood.MaterialSearchView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.DataSearch;
import com.example.user.tvfood.Common.Validate;
import com.example.user.tvfood.Model.DataSearch_Model;
import com.example.user.tvfood.Model.Notification_Model;
import com.example.user.tvfood.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Valkyzone on 11/30/2017.
 */

public class Adapter_RecyclerView_Search extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_1 = 1;


    private Context mContext;
    private ArrayList<DataSearch_Model> dataSearch_models;
    private onMyClickItemRecyclerView onMyClickItemRecyclerView;

    LayoutInflater inflater;


    public Adapter_RecyclerView_Search(Context mContext, ArrayList<DataSearch_Model> dataSearch_models, onMyClickItemRecyclerView onMyClickItemRecyclerView) {
        this.mContext = mContext;
        this.onMyClickItemRecyclerView = onMyClickItemRecyclerView;
        this.dataSearch_models = dataSearch_models;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public DataSearch_Model getItem(int position) {
        return dataSearch_models.get(position);
    }

    public void addData(DataSearch_Model item) {
        if (item == null)
            return;
        this.dataSearch_models.add(0, item);
        notifyItemInserted(0);
    }

    public void removeData(int position) {
        this.dataSearch_models.remove(position);
        notifyItemRemoved(position);
    }

    public void appendData(ArrayList<DataSearch_Model> items) {
        if (items.isEmpty() || items.size() == 0)
            return;
        int index = this.dataSearch_models.size();
        this.dataSearch_models.addAll(items);
        notifyItemRangeInserted(index, items.size());
    }

    public void refreshData() {
        final int index = this.dataSearch_models.size();
        this.dataSearch_models.clear();
        notifyDataSetChanged();

    }


    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM_1;
    }

    @Override
    public int getItemCount() {
        return dataSearch_models.size();
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
                view = inflater.inflate(R.layout.item_search, parent, false);
                viewHolder = new ViewHolder(view);

                break;
            }

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        switch (viewHolder.getItemViewType()) {


            case TYPE_ITEM_1: {

                final ViewHolder viewHolder1 = (ViewHolder) viewHolder;
                final DataSearch_Model item = (DataSearch_Model) dataSearch_models.get(i);

                //Binding Data
                viewHolder1.txt_DiaChi.setText(item.getDiachi());
                viewHolder1.txt_TenQuanAn.setText(item.getTenquanan());
                final int position = i;
                viewHolder1.linear_Main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onMyClickItemRecyclerView.onItemClick(position);
                    }
                });
                break;
            }

        }
    }


    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_TenQuanAn;
        private TextView txt_DiaChi;
        private LinearLayout linear_Main;

        ViewHolder(View v) {
            super(v);
            txt_TenQuanAn = v.findViewById(R.id.txt_TenQuanAn);
            txt_DiaChi = v.findViewById(R.id.txt_DiaChi);
            linear_Main = v.findViewById(R.id.linear_Main);

        }
    }


}
