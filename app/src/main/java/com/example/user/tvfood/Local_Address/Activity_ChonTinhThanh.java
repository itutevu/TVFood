package com.example.user.tvfood.Local_Address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tvfood.Common.GetIDTinhThanh;
import com.example.user.tvfood.Common.LocaleUtils;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.R;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Activity_ChonTinhThanh extends AppCompatActivity implements View.OnClickListener {
    private ListView listTinhThanh;
    public static String TAG_ID_TINHTHANH = "idTinhThanh";
    public static int KEY_CODE_RESULT = 102;
    private TextView txt_Xong;
    private SQLiteTinhThanhController sqLiteTinhThanhController;
    private ArrayList<TinhThanhPhoDTO> tinhThanhPhoDTOs;
    ArrayList<TinhThanhPhoDTO> tinhThanhPhoDTO_Temp = new ArrayList<>();
    private Adapter_List_TinhThanh adapter_list_tinhThanh;

    private TextView txtTinhThanh;
    private LinearLayout linear_DoiQuocGia;
    private EditText edit_Search;
    private ImageView imgBack;
    private RelativeLayout rela_AutoAddress;
    private int idTinhThanh;

    public Activity_ChonTinhThanh() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__chon_tinh_thanh);

        initView();

        setData_ListTinhThanh();
        edtSearch_Controller();
    }

    private void initView() {
        listTinhThanh = (ListView) findViewById(R.id.list_tinhthanhpho);
        txt_Xong = (TextView) findViewById(R.id.txt_Xong);
        linear_DoiQuocGia = (LinearLayout) findViewById(R.id.linear_DoiQuocGia);
        edit_Search = (EditText) findViewById(R.id.edtsearch);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        rela_AutoAddress = (RelativeLayout) findViewById(R.id.rela_AutoAddress);


        rela_AutoAddress.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        linear_DoiQuocGia.setOnClickListener(this);
        txt_Xong.setOnClickListener(this);
    }

    private void edtSearch_Controller() {


        edit_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tinhThanhPhoDTOs.clear();
                adapter_list_tinhThanh.setIdTinhThanh(-1);
                adapter_list_tinhThanh.setId_Select(-1);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textSearch = charSequence.toString();
                for (TinhThanhPhoDTO item : tinhThanhPhoDTO_Temp
                        ) {
                    if (item.getName().trim().toLowerCase().contains(textSearch.trim().toLowerCase())) {
                        tinhThanhPhoDTOs.add(item);
                    }
                }
                adapter_list_tinhThanh.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setData_ListTinhThanh() {
        sqLiteTinhThanhController = new SQLiteTinhThanhController(getApplicationContext());
        tinhThanhPhoDTOs = new ArrayList<>();
        tinhThanhPhoDTOs = sqLiteTinhThanhController.getListTinhThanh2();
        tinhThanhPhoDTO_Temp = sqLiteTinhThanhController.getListTinhThanh2();
        adapter_list_tinhThanh = new Adapter_List_TinhThanh(getBaseContext(), tinhThanhPhoDTOs);
        listTinhThanh.setAdapter(adapter_list_tinhThanh);

    }


    public String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String tinhThanh = "";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                tinhThanh = returnedAddress.getAdminArea();
            } else {
                makeText(getResources().getString(R.string.thongbao2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tinhThanh;
    }

    private void autoAddress_Controller(LatLng latLng) {

        String tinhThanh = getCompleteAddressString(latLng.latitude, latLng.longitude);

        idTinhThanh = sqLiteTinhThanhController.getIDTinhThanh(tinhThanh);
        if (idTinhThanh > 63 || idTinhThanh < 1)
            idTinhThanh = GetIDTinhThanh.getIDTinhThanh(tinhThanh);
        if (idTinhThanh == -1) {
            makeText(getResources().getString(R.string.thongbao2));
        } else {
            final Dialog dialog = new Dialog(Activity_ChonTinhThanh.this);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle("");
            dialog.setCanceledOnTouchOutside(false);
            Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
            Button btnThoat = dialog.findViewById(R.id.btnThoat);
            txtTinhThanh = dialog.findViewById(R.id.txt_Text);

            txtTinhThanh.setText(tinhThanh);
            btnXacNhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (idTinhThanh == -1) {
                        makeText(getResources().getString(R.string.thongbao2));
                        dialog.dismiss();
                    } else {
                        adapter_list_tinhThanh.setIdTinhThanh(-1);
                        Intent intentResult = new Intent();
                        intentResult.putExtra(TAG_ID_TINHTHANH, idTinhThanh);
                        setResult(KEY_CODE_RESULT, intentResult);
                        //Fragment_TrangChu_Nav.mPager.setCurrentItem(1);
                        finish();
                    }
                }
            });
            btnThoat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rela_AutoAddress: {

                if (!checkGPS()) {
                    showSettingsAlert();

                } else {
                    LatLng latLng = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
                    autoAddress_Controller(latLng);
                }
                break;
            }
            case R.id.imgBack: {
                finish();
                break;
            }
            case R.id.linear_DoiQuocGia: {
                makeText(getResources().getString(R.string.tinhnangdangxaydung));
                break;
            }
            case R.id.txt_Xong: {
                idTinhThanh = adapter_list_tinhThanh.getIdTinhThanh();
                if (idTinhThanh == -1) {
                    makeText(getResources().getString(R.string.banchuachontinhthanhpho));
                    return;
                } else {
                    adapter_list_tinhThanh.setIdTinhThanh(-1);
                    Intent intentResult = new Intent();
                    intentResult.putExtra(TAG_ID_TINHTHANH, idTinhThanh);
                    setResult(KEY_CODE_RESULT, intentResult);
                    finish();
                }
                break;
            }
        }
    }

    public boolean checkGPS() {
        LocationManager lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    private void makeText(String text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_ChonTinhThanh.this);

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.caidatgps));
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.gpskhonghoatdongbancomuondidencaidat));

        // On pressing Settings button
        alertDialog.setPositiveButton(getResources().getString(R.string.caidat), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 101);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(getResources().getString(R.string.thoat), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
