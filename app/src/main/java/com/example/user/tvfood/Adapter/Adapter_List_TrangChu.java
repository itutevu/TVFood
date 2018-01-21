package com.example.user.tvfood.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tvfood.Common.CalculationByDistance;
import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.Activity_ChiTietBinhLuan;
import com.example.user.tvfood.UI.Activity_ChiTietQuanAn;
import com.example.user.tvfood.UI.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class Adapter_List_TrangChu extends BaseAdapter {
    private ArrayList<QuanAnDTO> mCustomList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public static String urlImage = "";


    public static String getUrlImage() {
        return urlImage;
    }

    public static void setUrlImage(String urlImage) {
        Adapter_List_TrangChu.urlImage = urlImage;
    }

    public Adapter_List_TrangChu(Context context, ArrayList<QuanAnDTO> mCustomList) {
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_list_trangchu, null);
            holder = getViewHodler(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final QuanAnDTO item = setDatasourceViewHolder(position, holder);


        setClickDetail(holder.imgImage, item);


        if (item.getBinhLuanDTOs().size() != 0) {
            holder.linear_BinhLuan.removeAllViews();
            for (int i = 0; i < item.getBinhLuanDTOs().size(); i++) {
                View view = mLayoutInflater.inflate(R.layout.item_binhluan, null);
                TextView txtNoiDung = view.findViewById(R.id.txt_Comment);
                CircleImageView Ava = view.findViewById(R.id.img_Ava);

                Picasso.with(mContext).load(item.getBinhLuanDTOs().get(i).getUserDTO().getUrlavatar()).into(Ava);
                txtNoiDung.setText(Html.fromHtml("<b><font color=\"#000000\">" + item.getBinhLuanDTOs().get(i).getUserDTO().getHoten() +
                        "</b></font>     " + item.getBinhLuanDTOs().get(i).getNoidung()));
                holder.linear_BinhLuan.addView(view);
            }
        } else {
            holder.linear_BinhLuan.removeAllViews();
            View view = mLayoutInflater.inflate(R.layout.view_chuacobinhluan, null);
            holder.linear_BinhLuan.addView(view);
        }
        holder.linear_BinhLuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (item.getBinhLuanDTOs().size() == 0) {
                    Toast.makeText(mContext, "Không có bình luận", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                Intent intent = new Intent(mContext, Activity_ChiTietBinhLuan.class);
                intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());
                mContext.startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imgImage.setTransitionName("imageBanner");
        }
        holder.linear_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUrlImage(item.getHinhquanan().get(0));
                Intent intent = new Intent(mContext, Activity_ChiTietQuanAn.class);
                intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdquanan());

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext,
                                holder.imgImage,
                                ViewCompat.getTransitionName(holder.imgImage));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mContext.startActivity(intent, options.toBundle());
                } else
                    mContext.startActivity(intent);
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    Pair imgBanner = Pair.create(holder.imgImage, ViewCompat.getTransitionName(holder.imgImage));
                    Pair txtTitle = Pair.create(holder.txtTenQuan, ViewCompat.getTransitionName(holder.txtTenQuan));

                    ActivityOptions options = ActivityOptions.
                            makeSceneTransitionAnimation((Activity) mContext, imgBanner, txtTitle);

                    mContext.startActivity(intent, options.toBundle());

                } else
                    mContext.startActivity(intent);*/
            }
        });


        holder.linear_XemBanDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        return convertView;
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
        alertDialog.setTitle(mContext.getResources().getString(R.string.gpskhonghoatdong));
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

    public QuanAnDTO setDatasourceViewHolder(final int position, final ViewHolder holder) {


        QuanAnDTO item = (QuanAnDTO) getItem(position);

        float diemdanhgia = (float) Math.round(((Float.parseFloat(item.getDiemdanhgia()) / item.getSl_danhgia()) / 1) * 10) / 10;

        if (diemdanhgia >= 5f)
            holder.txtDiemDanhGia.setBackgroundResource(R.drawable.custom_item_danhgia);
        else
            holder.txtDiemDanhGia.setBackgroundResource(R.drawable.custom_item_danhgia2);
        holder.txtDiemDanhGia.setText(String.valueOf(diemdanhgia));

        holder.txtTenQuan.setText(item.getTenquanan());
        holder.txtDiaChi.setText(item.getDiachi());
        holder.txtSoBinhLuan.setText(String.valueOf(item.getSobinhluan()));
        holder.txtSoLike.setText(String.valueOf(item.getSoyeuthich()));
        if (item.isYeuThich()) {
            holder.img_YeuThich.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_love_hover));
        } else {
            holder.img_YeuThich.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_love));
        }
        //holder.txtKhoangCach.setText(item.getTxtKhoangCach());
        if (item.getHinhquanan().size() != 0) {
            Picasso.with(mContext).load(item.getHinhquanan().get(0)).into(holder.imgImage);
            //StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("hinhquanan").child(item.getHinhquanan().get(0));

           /* storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {

                   Picasso.with(mContext).load(uri).into( holder.imgImage);
               }
           });*/

           /* final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    Toast.makeText(mContext,"load = "+position,Toast.LENGTH_LONG).show();
                    holder.imgImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(mContext,"that bai",Toast.LENGTH_LONG).show();
                }
            });*/


        }


        if (item.isOrder()) {
            holder.linear_DatGiaoHang.setVisibility(View.VISIBLE);
            holder.linear_KhongDatGiaoHang.setVisibility(View.GONE);
        } else {
            holder.linear_DatGiaoHang.setVisibility(View.GONE);
            holder.linear_KhongDatGiaoHang.setVisibility(View.VISIBLE);
        }
        if (CalculationByTime.SoSanhTime(item.getGiomocua()) && !CalculationByTime.SoSanhTime(item.getGiodongcua())) {
            holder.linear_Close.setVisibility(View.GONE);
            holder.linear_Open.setVisibility(View.VISIBLE);
        } else {
            holder.linear_Close.setVisibility(View.VISIBLE);
            holder.linear_Open.setVisibility(View.GONE);
        }

        if (SessionLocation.getIntance().getLatitude() != -9999) {
            LatLng latLngCurrent = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
            LatLng latLngQuanAn = new LatLng(item.getLatitude(), item.getLongitude());
            String km = CalculationByDistance.getKm(latLngCurrent, latLngQuanAn);
            holder.txtKhoangCach.setText(km + " km");
        } else
            holder.txtKhoangCach.setText("... km");


        holder.btnDatGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.tinhnangdangxaydung), Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnKhongDatGiaoHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.diadiemnaykhonghotrodathang), Toast.LENGTH_SHORT).show();
            }
        });


        return item;

    }

    public ViewHolder getViewHodler(View convertView) {
        ViewHolder holder = new Adapter_List_TrangChu.ViewHolder();
        holder.txtDiemDanhGia = (TextView) convertView.findViewById(R.id.txt_DiemDanhGia);
        holder.txtTenQuan = (TextView) convertView.findViewById(R.id.txt_TenQuan);
        holder.txtDiaChi = (TextView) convertView.findViewById(R.id.txt_DiaChi);
        holder.txtKhoangCach = (TextView) convertView.findViewById(R.id.txt_KhoangCach);
        holder.imgImage = (ImageView) convertView.findViewById(R.id.img_Image);
        holder.btnDatGiaoHang = (Button) convertView.findViewById(R.id.btn_DatGiaoHang);
        holder.btnKhongDatGiaoHang = (Button) convertView.findViewById(R.id.btn_KhongDatGiaoHang);
        holder.linear_DatGiaoHang = (LinearLayout) convertView.findViewById(R.id.linear_DatGiaoHang);
        holder.linear_KhongDatGiaoHang = (LinearLayout) convertView.findViewById(R.id.linear_KhongDatGiaoHang);

        holder.linear_Close = (LinearLayout) convertView.findViewById(R.id.linear_close);
        holder.linear_Open = (LinearLayout) convertView.findViewById(R.id.linear_open);
        holder.txtSoBinhLuan = (TextView) convertView.findViewById(R.id.txt_SoBinhLuan);
        holder.txtSoLike = (TextView) convertView.findViewById(R.id.txt_Like);
        holder.linear_Title = convertView.findViewById(R.id.linear_Title);

        holder.linear_BinhLuan = (LinearLayout) convertView.findViewById(R.id.linearBinhLuan);
        holder.linear_XemBanDo = convertView.findViewById(R.id.linear_XemBanDo);

        holder.img_YeuThich = convertView.findViewById(R.id.img_YeuThich);

        return holder;
    }

    public class ViewHolder {
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

    }
}
