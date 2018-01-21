package com.example.user.tvfood.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tvfood.Common.CalculationByDistance;
import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.IsConnect;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.DividerItemDecoration;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.Activity_ChiTietBinhLuan;
import com.example.user.tvfood.UI.Activity_ChiTietQuanAn;
import com.example.user.tvfood.UI.LayDanhSachQuanAnGanToi.LayDanhSachQuanAnGanToi_Presenter;
import com.example.user.tvfood.UI.LayDanhSachQuanAnGanToi.LayDanhSachQuanAnGanToi_ViewListener;
import com.example.user.tvfood.UI.MapsActivity;
import com.example.user.tvfood.UI.MapsActivity_TimQuanAn;
import com.example.user.tvfood.UI.Tab_TrangChu;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by USER on 31/10/2017.
 */

public class Adapter_RecyclerView_ListTrangChu extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LayDanhSachQuanAnGanToi_ViewListener, CustomRecyclerViewAdapter.MyClickListener {
    private static final int TYPE_ITEM_HEADER = 0;
    private static final int TYPE_ITEM_HEADER_GANTOI = 1;
    private static final int TYPE_ITEM_SINGLE_IMAGE = 2;
    private static final int TYPE_ITEM_MULTI_IMAGE = 3;


    private static final int NUM_OF_HEADER = 2;
    private Context mContext;
    private ArrayList<QuanAnDTO> itemList;

    public static String urlImage = "";

    // Biến kiểm tra khởi tạo banner
    private boolean isFirst = true;
    private boolean isEmptyGanToi = false;
    // Recycler GanToi
    private ArrayList<QuanAnDTO> itemGanTois;
    private LayDanhSachQuanAnGanToi_Presenter layDanhSachQuanAnGanToi_presenter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;
    private FrameLayout progressLoad;
    private RelativeLayout relaKhongCoKetQua;
    LayoutInflater inflater;
    private FrameLayout frameLayout;


    public static String getUrlImage() {
        return urlImage;
    }

    public static void setUrlImage(String urlImage) {
        Adapter_List_TrangChu.urlImage = urlImage;
    }

    public Adapter_RecyclerView_ListTrangChu(Context mContext, ArrayList<QuanAnDTO> itemList, FrameLayout frameLayout) {
        this.mContext = mContext;
        this.itemList = itemList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.frameLayout = frameLayout;

        // ListGanToi
        itemGanTois = new ArrayList<>();
        layDanhSachQuanAnGanToi_presenter = new LayDanhSachQuanAnGanToi_Presenter();
        layDanhSachQuanAnGanToi();
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(itemGanTois, mContext);
        customRecyclerViewAdapter.setOnItemClickListener(this);
    }

    private void setDataGanToi(ArrayList<QuanAnDTO> itemGanTois) {
        if (itemGanTois.isEmpty() || itemGanTois.size() == 0)
            return;

        int index = this.itemGanTois.size();
        this.itemGanTois.addAll(itemGanTois);
        customRecyclerViewAdapter.notifyItemRangeInserted(index, this.itemGanTois.size());
        if (progressLoad != null)
            progressLoad.setVisibility(View.GONE);
        if (relaKhongCoKetQua != null)
            relaKhongCoKetQua.setVisibility(View.GONE);
    }


    public void appendData(ArrayList<QuanAnDTO> items) {
        if (items.isEmpty() || items.size() == 0)
            return;
        int index = this.itemList.size();
        this.itemList.addAll(items);
        ////Log.w("c//Log", "curent item size: " + itemList.size());
        notifyItemRangeInserted(index + NUM_OF_HEADER, items.size() + NUM_OF_HEADER);
    }

    public void refreshData() {
        final int index = this.itemList.size();
        this.itemList.clear();
        notifyDataSetChanged();
        //notifyItemRangeRemoved(NUM_OF_HEADER, index); // trừ 2 header

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ITEM_HEADER;
        } else {
            if (position == 1)
                return TYPE_ITEM_HEADER_GANTOI;
            return TYPE_ITEM_SINGLE_IMAGE;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size() + NUM_OF_HEADER;
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
            case TYPE_ITEM_HEADER: {
                view = inflater.inflate(R.layout.header_recycler_trangchu, parent, false);
                viewHolder = new ViewHolderHeader(view);
                break;
            }
            case TYPE_ITEM_HEADER_GANTOI: {
                view = inflater.inflate(R.layout.header_gantoi_recycler_trangchu, parent, false);
                viewHolder = new ViewHolderHeaderGanToi(view);

                break;
            }

            case TYPE_ITEM_SINGLE_IMAGE:
                //Log.w("log", "TYPE_ITEM_SINGLE_IMAGE");
                view = inflater.inflate(R.layout.item_list_trangchu, parent, false);
                viewHolder = new Adapter_RecyclerView_ListTrangChu.ViewHolderItemSingleImage(view);

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
            case TYPE_ITEM_HEADER: {
                final ViewHolderHeader viewHolderHeader = (ViewHolderHeader) viewHolder;

                if (isFirst) {
                    isFirst = false;
                    setResoure_ViewPager(viewHolderHeader.viewPager, viewHolderHeader.indicator);
                    setPlay_ViewPager(viewHolderHeader.viewPager);
                }
                break;
            }
            case TYPE_ITEM_HEADER_GANTOI: {
                final ViewHolderHeaderGanToi viewHolderHeaderGanToi = (ViewHolderHeaderGanToi) viewHolder;

                viewHolderHeaderGanToi.recyclerView.setHasFixedSize(false);
                viewHolderHeaderGanToi.recyclerView.setLayoutManager(mLayoutManager);

                RecyclerView.ItemDecoration itemDecoration =
                        new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL);
                viewHolderHeaderGanToi.recyclerView.addItemDecoration(itemDecoration);

                viewHolderHeaderGanToi.recyclerView.setAdapter(customRecyclerViewAdapter);


                viewHolderHeaderGanToi.txtThuLai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!IsConnect.getInstance().isConnect()) {
                            Snackbar.make(frameLayout, mContext.getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (!checkGPS()) {
                            showSettingsAlert();
                            return;
                        }
                        viewHolderHeaderGanToi.relaKhongCoKetQua.setVisibility(View.GONE);
                        viewHolderHeaderGanToi.progressBar_GanToi.setVisibility(View.VISIBLE);
                        layDanhSachQuanAnGanToi();
                    }
                });

                viewHolderHeaderGanToi.img_reload_GanToi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!IsConnect.getInstance().isConnect()) {
                            Snackbar.make(frameLayout, mContext.getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (!checkGPS()) {
                            showSettingsAlert();
                            return;
                        }
                        Intent intent = new Intent(mContext, MapsActivity_TimQuanAn.class);
                        mContext.startActivity(intent);
                    }
                });
                relaKhongCoKetQua = viewHolderHeaderGanToi.relaKhongCoKetQua;
                progressLoad = viewHolderHeaderGanToi.progressBar_GanToi;
                if (itemGanTois.size() != 0)
                    ((ViewHolderHeaderGanToi) viewHolder).progressBar_GanToi.setVisibility(View.GONE);
                if (isEmptyGanToi) {
                    ((ViewHolderHeaderGanToi) viewHolder).progressBar_GanToi.setVisibility(View.GONE);
                    viewHolderHeaderGanToi.relaKhongCoKetQua.setVisibility(View.VISIBLE);
                }
                break;
            }


            case TYPE_ITEM_SINGLE_IMAGE: {
                final Adapter_RecyclerView_ListTrangChu.ViewHolderItemSingleImage viewHolderItemSingleImage = (Adapter_RecyclerView_ListTrangChu.ViewHolderItemSingleImage) viewHolder;
                final QuanAnDTO item = (QuanAnDTO) itemList.get(i - NUM_OF_HEADER);

                setClickDetail(viewHolderItemSingleImage.imgImage, item);

                float diemdanhgia = (float) Math.round(((Float.parseFloat(item.getDiemdanhgia()) / item.getSl_danhgia()) / 1) * 10) / 10;

                if (diemdanhgia >= 5f)
                    viewHolderItemSingleImage.txtDiemDanhGia.setBackgroundResource(R.drawable.custom_item_danhgia);
                else
                    viewHolderItemSingleImage.txtDiemDanhGia.setBackgroundResource(R.drawable.custom_item_danhgia2);
                viewHolderItemSingleImage.txtDiemDanhGia.setText(String.valueOf(diemdanhgia));

                viewHolderItemSingleImage.txtTenQuan.setText(item.getTenquanan());
                viewHolderItemSingleImage.txtDiaChi.setText(item.getDiachi());
                viewHolderItemSingleImage.txtSoBinhLuan.setText(String.valueOf(item.getSobinhluan()));
                viewHolderItemSingleImage.txtSoLike.setText(String.valueOf(item.getSoyeuthich()));
                if (item.isYeuThich()) {
                    viewHolderItemSingleImage.img_YeuThich.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_love_hover));
                } else {
                    viewHolderItemSingleImage.img_YeuThich.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_love));
                }
                //viewHolderItemSingleImage.txtKhoangCach.setText(item.getTxtKhoangCach());
                if (item.getHinhquanan().size() != 0) {
                    Picasso.with(mContext).load(item.getHinhquanan().get(0)).into(viewHolderItemSingleImage.imgImage);
                }


                if (item.isOrder()) {
                    viewHolderItemSingleImage.linear_DatGiaoHang.setVisibility(View.VISIBLE);
                    viewHolderItemSingleImage.linear_KhongDatGiaoHang.setVisibility(View.GONE);
                } else {
                    viewHolderItemSingleImage.linear_DatGiaoHang.setVisibility(View.GONE);
                    viewHolderItemSingleImage.linear_KhongDatGiaoHang.setVisibility(View.VISIBLE);
                }
                if (CalculationByTime.SoSanhTime(item.getGiomocua()) && !CalculationByTime.SoSanhTime(item.getGiodongcua())) {
                    viewHolderItemSingleImage.linear_Close.setVisibility(View.GONE);
                    viewHolderItemSingleImage.linear_Open.setVisibility(View.VISIBLE);
                } else {
                    viewHolderItemSingleImage.linear_Close.setVisibility(View.VISIBLE);
                    viewHolderItemSingleImage.linear_Open.setVisibility(View.GONE);
                }

                if (SessionLocation.getIntance().getLatitude() != -9999) {
                    LatLng latLngCurrent = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
                    LatLng latLngQuanAn = new LatLng(item.getLatitude(), item.getLongitude());
                    String km = CalculationByDistance.getKm(latLngCurrent, latLngQuanAn);
                    viewHolderItemSingleImage.txtKhoangCach.setText(km + " km");
                } else
                    viewHolderItemSingleImage.txtKhoangCach.setText("... km");


                viewHolderItemSingleImage.btnDatGiaoHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.tinhnangdangxaydung), Toast.LENGTH_SHORT).show();
                    }
                });
                viewHolderItemSingleImage.btnKhongDatGiaoHang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.diadiemnaykhonghotrodathang), Toast.LENGTH_SHORT).show();
                    }
                });

                if (item.getBinhLuanDTOs().size() != 0) {
                    viewHolderItemSingleImage.linear_BinhLuan.removeAllViews();
                    for (int index = 0; index < item.getBinhLuanDTOs().size(); index++) {
                        View view = inflater.inflate(R.layout.item_binhluan, null);
                        TextView txtNoiDung = view.findViewById(R.id.txt_Comment);
                        CircleImageView Ava = view.findViewById(R.id.img_Ava);

                        Picasso.with(mContext).load(item.getBinhLuanDTOs().get(index).getUserDTO().getUrlavatar()).into(Ava);
                        txtNoiDung.setText(Html.fromHtml("<b><font color=\"#000000\">" + item.getBinhLuanDTOs().get(index).getUserDTO().getHoten() +
                                "</b></font>     " + item.getBinhLuanDTOs().get(index).getNoidung()));
                        viewHolderItemSingleImage.linear_BinhLuan.addView(view);
                    }
                } else {
                    viewHolderItemSingleImage.linear_BinhLuan.removeAllViews();
                    View view = inflater.inflate(R.layout.view_chuacobinhluan, null);
                    viewHolderItemSingleImage.linear_BinhLuan.addView(view);
                }
                viewHolderItemSingleImage.linear_BinhLuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!IsConnect.getInstance().isConnect()) {
                            Snackbar.make(frameLayout, mContext.getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        Intent intent = new Intent(mContext, Activity_ChiTietBinhLuan.class);
                        intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());
                        mContext.startActivity(intent);
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    viewHolderItemSingleImage.imgImage.setTransitionName("imageBanner");
                }
                viewHolderItemSingleImage.linear_Title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!IsConnect.getInstance().isConnect()) {
                            Snackbar.make(frameLayout, mContext.getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        setUrlImage(item.getHinhquanan().get(0));
                        Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
                        intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());

                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext,
                                        viewHolderItemSingleImage.imgImage,
                                        ViewCompat.getTransitionName(viewHolderItemSingleImage.imgImage));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mContext.startActivity(intent, options.toBundle());
                        } else
                            mContext.startActivity(intent);

                    }
                });


                viewHolderItemSingleImage.linear_XemBanDo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!IsConnect.getInstance().isConnect()) {
                            Snackbar.make(frameLayout, mContext.getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (!checkGPS()) {
                            showSettingsAlert();
                            return;
                        }
                        Intent intent = new Intent(mContext, MapsActivity.class);
                        intent.putExtra(Common.KEY_CODE.LATITUDE, item.getLatitude());
                        intent.putExtra(Common.KEY_CODE.LONGITUDE, item.getLongitude());
                        mContext.startActivity(intent);
                    }
                });

                break;
            }

            case TYPE_ITEM_MULTI_IMAGE:
                break;
        }
    }

    private void layDanhSachQuanAnGanToi() {
        if (layDanhSachQuanAnGanToi_presenter != null)
            layDanhSachQuanAnGanToi_presenter.receiveHandle(this, 10, Tab_TrangChu.DISTANCE);
    }

    public boolean checkGPS() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle(mContext.getResources().getString(R.string.caidatgps));
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage(mContext.getResources().getString(R.string.gpskhonghoatdongbancomuondidencaidat));

        // On pressing Settings button
        alertDialog.setPositiveButton(mContext.getResources().getString(R.string.caidat), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(mContext.getResources().getString(R.string.thoat), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    private void setClickDetail(final ImageView img, final QuanAnDTO item) {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IsConnect.getInstance().isConnect()) {
                    Snackbar.make(frameLayout, mContext.getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                    return;
                }
                setUrlImage(item.getHinhquanan().get(0));

                Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
                intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext,
                                img,
                                ViewCompat.getTransitionName(img));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mContext.startActivity(intent, options.toBundle());
                } else
                    mContext.startActivity(intent);
            }
        });
    }


    // Set ViewPager Banner
    private void setResoure_ViewPager(ViewPager viewPager, CircleIndicator indicator) {
        Integer[] images = new Integer[]{R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4};
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(mContext, images);
        viewPager.setAdapter(pagerAdapter);

        indicator.setViewPager(viewPager);
        pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    private void setPlay_ViewPager(final ViewPager viewPager) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int i = viewPager.getCurrentItem();
                if (i == 0) {
                    viewPager.setCurrentItem(1);
                } else if (i == 1) {
                    viewPager.setCurrentItem(2);
                } else if (i == 2) {
                    viewPager.setCurrentItem(3);
                } else {
                    viewPager.setCurrentItem(0);
                }
                handler.postDelayed(this, 2000);
            }
        }, 2000);

    }

    @Override
    public void onSuccess_LayDanhSachQuanAnGanToi(ArrayList<QuanAnDTO> itemGanTois) {
        setDataGanToi(itemGanTois);
        isEmptyGanToi = false;
    }

    @Override
    public void onFailed_LayDanhSachQuanAnGanToi(String message) {
        isEmptyGanToi = true;
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        if (progressLoad != null)
            progressLoad.setVisibility(View.GONE);
        if (relaKhongCoKetQua != null)
            relaKhongCoKetQua.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEmpty_LayDanhSachQuanAnGanToi() {
        isEmptyGanToi = true;
        if (progressLoad != null)
            progressLoad.setVisibility(View.GONE);
        if (relaKhongCoKetQua != null)
            relaKhongCoKetQua.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position, View v) {
        if (!IsConnect.getInstance().isConnect()) {
            Snackbar.make(frameLayout, mContext.getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
            return;
        }
        if (itemGanTois.size() != 0) {
            ImageView imageView = v.findViewById(R.id.image);
            Adapter_RecyclerView_ListTrangChu.setUrlImage(itemGanTois.get(position).getHinhquanan().get(0));
            Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
            intent.putExtra(Common.KEY_CODE.IDQUANAN, itemGanTois.get(position).getIdquanan());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) mContext,
                            imageView,
                            ViewCompat.getTransitionName(imageView));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mContext.startActivity(intent, options.toBundle());
            } else
                mContext.startActivity(intent);
        }
    }


    private static class ViewHolderItemSingleImage extends RecyclerView.ViewHolder {
        private TextView txtDiemDanhGia;
        private TextView txtTenQuan;
        private TextView txtDiaChi;
        private TextView txtKhoangCach;
        private ImageView imgImage;
        private LinearLayout linear_KhongDatGiaoHang;
        private LinearLayout linear_DatGiaoHang;
        private Button btnDatGiaoHang;
        private Button btnKhongDatGiaoHang;
        private LinearLayout linear_Close;
        private LinearLayout linear_Open;
        private TextView txtSoBinhLuan;
        private TextView txtSoLike;
        private LinearLayout linear_Title;
        private LinearLayout linear_XemBanDo;
        private ImageView img_YeuThich;


        private LinearLayout linear_BinhLuan;


        ViewHolderItemSingleImage(View v) {
            super(v);

            txtDiemDanhGia = (TextView) v.findViewById(R.id.txt_DiemDanhGia);
            txtTenQuan = (TextView) v.findViewById(R.id.txt_TenQuan);
            txtDiaChi = (TextView) v.findViewById(R.id.txt_DiaChi);
            txtKhoangCach = (TextView) v.findViewById(R.id.txt_KhoangCach);
            imgImage = (ImageView) v.findViewById(R.id.img_Image);
            btnDatGiaoHang = (Button) v.findViewById(R.id.btn_DatGiaoHang);
            btnKhongDatGiaoHang = (Button) v.findViewById(R.id.btn_KhongDatGiaoHang);
            linear_DatGiaoHang = (LinearLayout) v.findViewById(R.id.linear_DatGiaoHang);
            linear_KhongDatGiaoHang = (LinearLayout) v.findViewById(R.id.linear_KhongDatGiaoHang);

            linear_Close = (LinearLayout) v.findViewById(R.id.linear_close);
            linear_Open = (LinearLayout) v.findViewById(R.id.linear_open);
            txtSoBinhLuan = (TextView) v.findViewById(R.id.txt_SoBinhLuan);
            txtSoLike = (TextView) v.findViewById(R.id.txt_Like);
            linear_Title = v.findViewById(R.id.linear_Title);

            linear_BinhLuan = (LinearLayout) v.findViewById(R.id.linearBinhLuan);
            linear_XemBanDo = v.findViewById(R.id.linear_XemBanDo);

            img_YeuThich = v.findViewById(R.id.img_YeuThich);

        }
    }

    private static class ViewHolderHeader extends RecyclerView.ViewHolder {
        private CircleIndicator indicator;
        private ViewPager viewPager;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            indicator = itemView.findViewById(R.id.indicator);
            viewPager = itemView.findViewById(R.id.viewPager);


        }
    }

    private static class ViewHolderHeaderGanToi extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private ImageView img_reload_GanToi;
        private FrameLayout progressBar_GanToi;
        private RelativeLayout relaKhongCoKetQua;
        private TextView txtThuLai;

        public ViewHolderHeaderGanToi(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycleView);

            img_reload_GanToi = itemView.findViewById(R.id.img_reload_GanToi);

            progressBar_GanToi = itemView.findViewById(R.id.progressBarGanToi);

            relaKhongCoKetQua = itemView.findViewById(R.id.relaKhongCoKetQua);
            txtThuLai = itemView.findViewById(R.id.txtThuLai);
        }
    }

    private static class ViewHolderItemMultiImage extends RecyclerView.ViewHolder {

        public ViewHolderItemMultiImage(View itemView) {
            super(itemView);
        }
    }
}
