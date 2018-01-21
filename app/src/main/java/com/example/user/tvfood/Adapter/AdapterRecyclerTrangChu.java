package com.example.user.tvfood.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;

import java.util.List;

/**
 * Created by Valkyzone on 25/08/2017.
 */

public class AdapterRecyclerTrangChu extends RecyclerView.Adapter<AdapterRecyclerTrangChu.ViewHolder>{

    private List<QuanAnDTO> quanAnDTOList;
    private int resource;

    public AdapterRecyclerTrangChu(List<QuanAnDTO> quanAnDTOlist, int resource){
        this.quanAnDTOList = quanAnDTOlist;
        this.resource = resource;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenQuanAn;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenQuanAn = (TextView) itemView.findViewById(R.id.txt_TenQuan);
        }
    }
    @Override
    public AdapterRecyclerTrangChu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(AdapterRecyclerTrangChu.ViewHolder holder, int position) {
        QuanAnDTO quanAnDTO = quanAnDTOList.get(position);
        holder.txtTenQuanAn.setText(quanAnDTO.getTenquanan());
    }

    @Override
    public int getItemCount() {
        return quanAnDTOList.size();
    }


}
