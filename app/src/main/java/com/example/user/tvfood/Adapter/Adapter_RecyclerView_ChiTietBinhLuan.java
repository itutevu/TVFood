package com.example.user.tvfood.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.Model.BinhLuanDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.YeuThichBinhLuan.YeuThichBinhLuan_Presenter;
import com.example.user.tvfood.UI.YeuThichBinhLuan.YeuThichBinhLuan_ViewListener;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Arrays.asList;

/**
 * Created by USER on 31/10/2017.
 */

public class Adapter_RecyclerView_ChiTietBinhLuan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM_SINGLE_IMAGE = 1;
    private static final int TYPE_ITEM_MULTI_IMAGE = 2;


    private Context mContext;
    private List<BinhLuanDTO> itemList;

    LayoutInflater inflater;

    private YeuThichBinhLuan_Presenter yeuThichBinhLuan_presenter;
    private YeuThichBinhLuan_ViewListener yeuThichBinhLuan_viewListener;
    private SessionUser sessionUser;
    Activity Activity_ChiTietBinhLuan;


    public Adapter_RecyclerView_ChiTietBinhLuan(Context mContext, List<BinhLuanDTO> itemList, YeuThichBinhLuan_ViewListener yeuThichBinhLuan_viewListener, Activity Activity_ChiTietBinhLuan) {
        this.mContext = mContext;
        this.itemList = itemList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.yeuThichBinhLuan_viewListener = yeuThichBinhLuan_viewListener;
        yeuThichBinhLuan_presenter = new YeuThichBinhLuan_Presenter();
        sessionUser = new SessionUser(mContext);
        this.Activity_ChiTietBinhLuan = Activity_ChiTietBinhLuan;


    }

    public void addData(BinhLuanDTO item) {
        if (item == null)
            return;
        this.itemList.add(0, item);
        notifyItemInserted(0);
    }

    public void removeData(int position) {
        this.itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void appendData(List<BinhLuanDTO> items) {
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
                view = inflater.inflate(R.layout.item_list_binhluan, parent, false);
                viewHolder = new Adapter_RecyclerView_ChiTietBinhLuan.ViewHolderItemSingleImage(view);

                break;
            case TYPE_ITEM_MULTI_IMAGE:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        switch (viewHolder.getItemViewType()) {


            case TYPE_ITEM_SINGLE_IMAGE: {
                final Adapter_RecyclerView_ChiTietBinhLuan.ViewHolderItemSingleImage viewHolderItemSingleImage = (Adapter_RecyclerView_ChiTietBinhLuan.ViewHolderItemSingleImage) viewHolder;
                final BinhLuanDTO item = (BinhLuanDTO) itemList.get(i);

                String Idbinhluan = item.getIdbinhluan().substring(0, 14);
                if (item.isNew()) {
                    viewHolderItemSingleImage.txtThoiGian.setText(Html.fromHtml("<font color='#4a90e2'>" + CalculationByTime.GetTime(Idbinhluan, mContext, Activity_ChiTietBinhLuan) + "</font>"));
                } else {
                    viewHolderItemSingleImage.txtThoiGian.setText(CalculationByTime.GetTime(Idbinhluan, mContext, Activity_ChiTietBinhLuan));
                }


                if (item.getUserDTO() != null)
                    Picasso.with(mContext).load(item.getUserDTO().getUrlavatar()).into(viewHolderItemSingleImage.imgAva);
                if (item.getNoidung().equals(""))
                    viewHolderItemSingleImage.txtNoiDung.setVisibility(View.GONE);
                else {
                    viewHolderItemSingleImage.txtNoiDung.setVisibility(View.VISIBLE);
                    viewHolderItemSingleImage.txtNoiDung.setText(item.getNoidung());
                }
                if (item.getUserDTO() != null)
                    viewHolderItemSingleImage.txtTenTaiKhoan.setText(item.getUserDTO().getHoten());

                if (item.isYeuThich()) {
                    //viewHolderItemSingleImage.btnLike.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.liked_48), null, null, null);
                    viewHolderItemSingleImage.btnLike.setTextColor(Color.parseColor("#3F51B5"));
                    if ((Long.parseLong(item.getLuotthich()) - 1) != 0) {
                        viewHolderItemSingleImage.txtSoLike.setText(mContext.getResources().getString(R.string.banva) + " " + (Long.parseLong(item.getLuotthich()) - 1) + " " + mContext.getResources().getString(R.string.nguoikhac));
                    } else {
                        viewHolderItemSingleImage.txtSoLike.setText(item.getLuotthich());
                    }
                } else {
                    //viewHolderItemSingleImage.btnLike.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.like_48), null, null, null);
                    viewHolderItemSingleImage.btnLike.setTextColor(Color.parseColor("#111111"));
                    viewHolderItemSingleImage.txtSoLike.setText(item.getLuotthich());
                }

                if (item.getUrlimage().equals("null"))
                    viewHolderItemSingleImage.rela_Image.setVisibility(View.GONE);
                else {

                    viewHolderItemSingleImage.rela_Image.setVisibility(View.VISIBLE);
                    Picasso.with(mContext).load(item.getUrlimage()).into(viewHolderItemSingleImage.imgImage);

                }

                viewHolderItemSingleImage.imgImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(mContext.getResources())
                                .setFailureImage(R.drawable.upload_empty)
                                /*.setProgressBarImage(R.drawable.ic_loading)
                                .setPlaceholderImage(R.drawable.ic_loading)*/;

                        List<String> strings = new ArrayList<>();
                        strings.add(item.getUrlimage());

                        new ImageViewer.Builder<>(Activity_ChiTietBinhLuan, strings)
                                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                                .show();
                    }
                });

                viewHolderItemSingleImage.btnLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sessionUser.getUserDTO().getToken() == null || sessionUser.getUserDTO().getToken().isEmpty()) {
                            final Dialog dialog = new Dialog(Activity_ChiTietBinhLuan);
                            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                            dialog.setContentView(R.layout.dialog_login);
                            dialog.setTitle("");
                            dialog.setCanceledOnTouchOutside(false);
                            Button btn_co = (Button) dialog.findViewById(R.id.btn_codangnhap);
                            Button btn_khong = (Button) dialog.findViewById(R.id.btn_khongdangnhap);
                            btn_co.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    LoginManager.getInstance().logInWithReadPermissions(Activity_ChiTietBinhLuan, asList("email", "public_profile"));
                                    dialog.dismiss();
                                }
                            });
                            btn_khong.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            return;
                        }
                        yeuThichBinhLuan_presenter.receiveHandle(yeuThichBinhLuan_viewListener, item.getIdbinhluan(), sessionUser, item.getIdbinhluan(), item.getIdquanan());
                    }
                });

                break;
            }

            case TYPE_ITEM_MULTI_IMAGE:
                break;
        }
    }


    private static class ViewHolderItemSingleImage extends RecyclerView.ViewHolder {

        private CircleImageView imgAva;
        private TextView txtTenTaiKhoan;
        private ImageView imgImage;
        private TextView txtNoiDung;
        private TextView txtSoBinhLuan;
        private TextView txtSoLike;
        private TextView btnLike;
        private TextView btnComment;
        private TextView txtThoiGian;
        private ImageView img_Like;
        private RelativeLayout rela_Image;

        private LinearLayout linear_BinhLuan;


        ViewHolderItemSingleImage(View v) {
            super(v);

            imgAva = (CircleImageView) v.findViewById(R.id.img_Ava);
            txtTenTaiKhoan = v.findViewById(R.id.txtTeTaiKhoan);
            imgImage = (ImageView) v.findViewById(R.id.img_Image);
            txtNoiDung = (TextView) v.findViewById(R.id.txt_Comment);
            txtSoBinhLuan = (TextView) v.findViewById(R.id.txt_SoBinhLuan);
            txtSoLike = (TextView) v.findViewById(R.id.txt_SoLike);
            btnLike = (TextView) v.findViewById(R.id.btnLike);
            btnComment = (TextView) v.findViewById(R.id.btnBinhLuan);
            linear_BinhLuan = (LinearLayout) v.findViewById(R.id.linearBinhLuan);
            txtThoiGian = v.findViewById(R.id.txtThoiGian);
            img_Like = v.findViewById(R.id.img_Like);
            rela_Image = v.findViewById(R.id.rela_Image);

        }
    }


}
