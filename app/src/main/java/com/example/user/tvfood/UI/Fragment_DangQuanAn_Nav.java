package com.example.user.tvfood.UI;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andremion.louvre.Louvre;
import com.andremion.louvre.home.GalleryActivity;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionNotification;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.Local_Address.QuanHuyenDTO;
import com.example.user.tvfood.Local_Address.SQLiteDataController;
import com.example.user.tvfood.Local_Address.SQLiteQuanTPHCMController;
import com.example.user.tvfood.Local_Address.SQLiteTinhThanhController;
import com.example.user.tvfood.Local_Address.TinhThanhPhoDTO;
import com.example.user.tvfood.Model.CategoryModel;
import com.example.user.tvfood.Model.EventBoSuuTap;
import com.example.user.tvfood.Model.EventSentLatLong;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.Model.QuanAnDTO_POST;
import com.example.user.tvfood.Model.UserDTO;
import com.example.user.tvfood.R;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.webianks.library.scroll_choice.ScrollChoice;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static java.util.Arrays.asList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_DangQuanAn_Nav extends Fragment implements View.OnClickListener {
    private static final int REQUEST = 666;

    private static final int CODE_ID_TINHTHANH = 1;
    private static final int CODE_ID_QUANHUYEN = 2;
    private static final int CODE_ID_DANHMUC = 3;

    private static final int CHOOSE_IMAGE = 555;
    private static final int CODE_READ_EXTERNAL_STORAGE = 556;
    private static final int CODE_ACTION_IMAGE_CAPTURE = 557;
    private static final int CODE_READ_EXTERNAL_STORAGE_CAMERA = 558;
    private static final int CODE_WRITE_EXTERNAL_STORAGE_CAMERA = 559;
    private static final int CODE_READ_WRITE_EXTERNAL_STORAGE = 560;

    private ImageView img_camera, img_thuvien;
    private int numOfImage = 6; // Số hình ảnh có thể chọn
    private ImageView image1, image2, image3, image4, image5, image6;
    private ImageView img_clear1, img_clear2, img_clear3, img_clear4, img_clear5, img_clear6;
    private String stringPath1 = "", stringPath2 = "", stringPath3 = "", stringPath4 = "", stringPath5 = "", stringPath6 = "";
    private Uri fileUri = null;
    private Louvre louvre;
    private NestedScrollView scrollView;
    private List<Uri> selection = new ArrayList<>();

    private RelativeLayout rela_ChonTinhThanh, rela_ChonQuanHuyen, rela_ChonDanhMuc;
    private FrameLayout frame1, frame2, frame3, frame4, frame5, frame6;

    //Data Select
    SQLiteTinhThanhController sqLiteTinhThanhController;
    SQLiteQuanTPHCMController sqLiteQuanTPHCMController;

    private List<TinhThanhPhoDTO> listTinhThanh;
    private List<String> listNameTinhThanh;

    private List<QuanHuyenDTO> listQuanHuyen;
    private List<String> listNameQuanHuyen;

    private List<CategoryModel> listDanhMuc;
    private List<String> listNameDanhMuc;
    private TextView txt_DanhMuc, txt_QuanHuyen, txt_TinhThanh, txt_Longitude, txt_Latitude, txt_GioMoCua, txt_GioDongCua;
    private EditText edt_TenQuan, edt_DiaChi, edt_SoDienThoai;
    private Button btn_XacNhan;
    private ImageView img_ChonGioMoCua, img_ChonGioDongCua;
    private boolean isOpen = true;

    private TinhThanhPhoDTO tinhThanhSelected = new TinhThanhPhoDTO();
    private QuanHuyenDTO quanHuyenSelected = new QuanHuyenDTO();
    private CategoryModel danhMucSelected = new CategoryModel();
    private List<String> urlImageResult = new ArrayList<>();
    private ProgressDialog progressDialog;
    private LinearLayout linear_XacDinhViTri;

    SessionUser sessionUser;
    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;

    public Fragment_DangQuanAn_Nav() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final String tokenID = loginResult.getAccessToken().getToken();
                        final String fbID = loginResult.getAccessToken().getUserId();
                        AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
                        MainActivity.relaLogin.setVisibility(View.GONE);
                        MainActivity.btn_Logout.setVisibility(View.VISIBLE);
                        MainActivity.rela_content.setVisibility(View.VISIBLE);
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage(getResources().getString(R.string.thongbao1));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                UserDTO userDTO = new UserDTO(tokenID, task.getResult().getUser().getUid(), task.getResult().getUser().getDisplayName(), "https://graph.facebook.com/" + fbID + "/picture?access_token=" + tokenID, task.getResult().getUser().getPhoneNumber(), FirebaseInstanceId.getInstance().getToken(), fbID);
                                sessionUser.createSessionUser(userDTO);

                                SessionNotification sessionNotification = new SessionNotification(getContext());
                                sessionNotification.createSessionNotificatione(Common.KEY_NOTIFICATION.KEY_ON);
                                Fragment_CaiDat_Nav.txt_NhanThongBao.setText("ON");

                                Picasso.with(getContext()).load(sessionUser.getUserDTO().getUrlavatar()).into(MainActivity.profile_image);
                                MainActivity.txt_Name.setText(sessionUser.getUserDTO().getHoten());
                                if (MainActivity.viewPager.getCurrentItem() == 2) {
                                    MainActivity.isFirst = false;
                                    GlobalBus.getBus().post(new EventBoSuuTap("1"));

                                } else
                                    MainActivity.isFirst = true;
                                if (sessionUser.getUserDTO().getSdt().equals(""))
                                    MainActivity.txt_SDT.setText(getResources().getString(R.string.activity_main_chuacosodienthoai));
                                else
                                    MainActivity.txt_SDT.setText(sessionUser.getUserDTO().getSdt());

                                if (sessionUser.getUserDTO().getId().equals("")) {
                                    MainActivity.relaLogin.setVisibility(View.VISIBLE);
                                    MainActivity.btn_Logout.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), getResources().getString(R.string.thongbao2), Toast.LENGTH_SHORT).show();

                                } else {
                                    HashMap map = new HashMap();
                                    map.put(sessionUser.getUserDTO().getId(), sessionUser.getUserDTO());
                                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THANHVIENS).updateChildren(map);
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Facebook Login", exception.toString());
                    }
                }
        );
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment__dang_quan_an, container, false);
        initView(v);
        createDB();
        initData();
        sessionUser = new SessionUser(getContext());
        Tiny.getInstance().init(getActivity().getApplication());
        return v;
    }

    private void initView(View v) {
        txt_GioDongCua = v.findViewById(R.id.txt_GioDongCua);
        txt_GioMoCua = v.findViewById(R.id.txt_GioMoCua);

        img_ChonGioDongCua = v.findViewById(R.id.img_ChonGioDongCua);
        img_ChonGioDongCua.setOnClickListener(this);

        img_ChonGioMoCua = v.findViewById(R.id.img_ChonGioMoCua);
        img_ChonGioMoCua.setOnClickListener(this);

        txt_Longitude = v.findViewById(R.id.txt_Longitude);
        txt_Latitude = v.findViewById(R.id.txt_Latitude);

        linear_XacDinhViTri = v.findViewById(R.id.linear_XacDinhViTri);
        linear_XacDinhViTri.setOnClickListener(this);

        edt_SoDienThoai = v.findViewById(R.id.edt_SoDienThoai);
        edt_DiaChi = v.findViewById(R.id.edt_DiaChi);
        edt_TenQuan = v.findViewById(R.id.edt_TenQuan);
        txt_DanhMuc = v.findViewById(R.id.txt_DanhMuc);
        txt_TinhThanh = v.findViewById(R.id.txt_TinhThanh);
        txt_QuanHuyen = v.findViewById(R.id.txt_QuanHuyen);


        btn_XacNhan = v.findViewById(R.id.btn_XacNhan);
        btn_XacNhan.setOnClickListener(this);

        rela_ChonDanhMuc = v.findViewById(R.id.rela_ChonDanhMuc);
        rela_ChonDanhMuc.setOnClickListener(this);

        rela_ChonQuanHuyen = v.findViewById(R.id.rela_ChonQuanHuyen);
        rela_ChonQuanHuyen.setOnClickListener(this);

        rela_ChonTinhThanh = v.findViewById(R.id.rela_ChonTinhThanh);
        rela_ChonTinhThanh.setOnClickListener(this);

        scrollView = v.findViewById(R.id.scrollView);
        //scrollView.setNestedScrollingEnabled(false);

        img_camera = v.findViewById(R.id.img_camera);
        img_camera.setOnClickListener(this);

        img_thuvien = v.findViewById(R.id.img_thuvien);
        img_thuvien.setOnClickListener(this);

        image1 = v.findViewById(R.id.image1);
        image2 = v.findViewById(R.id.image2);
        image3 = v.findViewById(R.id.image3);
        image4 = v.findViewById(R.id.image4);
        image5 = v.findViewById(R.id.image5);
        image6 = v.findViewById(R.id.image6);

        img_clear1 = v.findViewById(R.id.img_clear1);
        img_clear2 = v.findViewById(R.id.img_clear2);
        img_clear3 = v.findViewById(R.id.img_clear3);
        img_clear4 = v.findViewById(R.id.img_clear4);
        img_clear5 = v.findViewById(R.id.img_clear5);
        img_clear6 = v.findViewById(R.id.img_clear6);

        img_clear1.setOnClickListener(this);
        img_clear2.setOnClickListener(this);
        img_clear3.setOnClickListener(this);
        img_clear4.setOnClickListener(this);
        img_clear5.setOnClickListener(this);
        img_clear6.setOnClickListener(this);

        img_clear1.setVisibility(View.GONE);
        img_clear2.setVisibility(View.GONE);
        img_clear3.setVisibility(View.GONE);
        img_clear4.setVisibility(View.GONE);
        img_clear5.setVisibility(View.GONE);
        img_clear6.setVisibility(View.GONE);

        frame1 = v.findViewById(R.id.frame1);
        frame2 = v.findViewById(R.id.frame2);
        frame3 = v.findViewById(R.id.frame3);
        frame4 = v.findViewById(R.id.frame4);
        frame5 = v.findViewById(R.id.frame5);
        frame6 = v.findViewById(R.id.frame6);
    }

    private void createDB() {
        // khởi tạo database
        SQLiteDataController sql = new SQLiteDataController(getContext());
        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        //Data Tỉnh Thành
        sqLiteTinhThanhController = new SQLiteTinhThanhController(getContext());
        listTinhThanh = new ArrayList<>();
        listNameTinhThanh = new ArrayList<>();
        listTinhThanh = sqLiteTinhThanhController.getListTinhThanh2();
        for (TinhThanhPhoDTO item : listTinhThanh) {
            listNameTinhThanh.add(item.getName());
        }
        //Data Quận Huyện
        sqLiteQuanTPHCMController = new SQLiteQuanTPHCMController(getContext());
        listQuanHuyen = new ArrayList<>();
        listNameQuanHuyen = new ArrayList<>();

        //Data Danh Mục
        listDanhMuc = new ArrayList<>();
        listNameDanhMuc = new ArrayList<>();

        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_COFFEE, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_COFFEE));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_COFFEE);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_ANCHAY, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_ANCHAY));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_ANCHAY);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_ANVAT, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_ANVAT));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_ANVAT);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_BAR, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_BAR));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_BAR);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_BUFFET, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_BUFFET));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_BUFFET);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_NUOCUONG, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_NUOCUONG));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_NUOCUONG);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_NHAHANG, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_NHAHANG));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_NHAHANG);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_QUANNHAU, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_QUANNHAU));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_QUANNHAU);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_TIEMBANH, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_TIEMBANH));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_TIEMBANH);
        listDanhMuc.add(new CategoryModel(Common.KEY_SORT.KEY_QUANCOM, Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_QUANCOM));
        listNameDanhMuc.add(Common.KEY_NAME_CATEGORY.KEY_NAME_CATEGORY_QUANCOM);

    }

    private void initData_QuanHuyen(int idTinhThanh) {
        listQuanHuyen.clear();
        listNameQuanHuyen.clear();

        listQuanHuyen = sqLiteQuanTPHCMController.getListQuanHuyen(idTinhThanh);
        for (QuanHuyenDTO item : listQuanHuyen) {
            listNameQuanHuyen.add(item.getTenQuan());
        }
    }


    private int hr;
    private int min;

    protected Dialog createdDialog() {
        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), timePickerListener, hr, min, true);
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hr = hourOfDay;
            min = minutes;
            //txt_DanhMuc.setText(String.valueOf(hr) + ":" + String.valueOf(min));
            updateTime(hr, min);
        }

    };

    private static String utilTime(int value) {
        if (value < 10) return "0" + String.valueOf(value);
        else return String.valueOf(value);
    }

    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            //hours -= 12; // Kiểu 12
            timeSet = "PM";
        } else if (hours == 0) {

            //hours += 12;// Kiểu 12
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = String.valueOf(hours) + ':' + minutes;
        if (isOpen)
            txt_GioMoCua.setText(aTime);
        else
            txt_GioDongCua.setText(aTime);
    }


    private void showDialog(final int codeID) {
        final Dialog dialog = new Dialog(getContext());

        dialog.setContentView(R.layout.custom_layout_dialog);
        dialog.setTitle("");
        //dialog.setCanceledOnTouchOutside(false);
        TextView txt_Huy = dialog.findViewById(R.id.txt_Huy);
        TextView txt_Xong = dialog.findViewById(R.id.txt_Xong);
        final TextView txt_Title = dialog.findViewById(R.id.txt_Title);

        final ScrollChoice scroll_choice = dialog.findViewById(R.id.scroll_choice);


        switch (codeID) {
            case CODE_ID_TINHTHANH: {// Chọn Tỉnh Thành
                txt_Title.setText(getResources().getString(R.string.chontinhthanh));
                int index = 0;
                if (!txt_TinhThanh.getText().toString().isEmpty()) {
                    for (int i = 0; i < listNameTinhThanh.size(); i++) {
                        if (listNameTinhThanh.get(i).equals(txt_TinhThanh.getText().toString())) {
                            index = i;
                            break;
                        }
                    }
                }
                scroll_choice.addItems(listNameTinhThanh, index);
                break;
            }
            case CODE_ID_QUANHUYEN: {// Chọn Quận Huyện
                txt_Title.setText(getResources().getString(R.string.chonquanhuyen));
                int index = 0;
                if (!txt_QuanHuyen.getText().toString().isEmpty()) {
                    for (int i = 0; i < listNameQuanHuyen.size(); i++) {
                        if (listNameQuanHuyen.get(i).equals(txt_QuanHuyen.getText().toString())) {
                            index = i;
                            break;
                        }
                    }
                }
                scroll_choice.addItems(listNameQuanHuyen, index);
                break;
            }
            case CODE_ID_DANHMUC: {// Chọn Danh Mục
                txt_Title.setText(getResources().getString(R.string.chondanhmuc));
                int index = 0;
                if (!txt_DanhMuc.getText().toString().isEmpty()) {
                    for (int i = 0; i < listNameDanhMuc.size(); i++) {
                        if (listNameDanhMuc.get(i).equals(txt_DanhMuc.getText().toString())) {
                            index = i;
                            break;
                        }
                    }
                }
                scroll_choice.addItems(listNameDanhMuc, index);
                break;
            }
        }


        txt_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        txt_Xong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (codeID) {
                    case 1: {// Chọn Tỉnh Thành
                        initData_QuanHuyen(listTinhThanh.get(scroll_choice.getCurrentItemPosition()).getId());
                        tinhThanhSelected.createTinhThanhDTO(listTinhThanh.get(scroll_choice.getCurrentItemPosition()));
                        txt_TinhThanh.setText(scroll_choice.getCurrentSelection());

                        if (!txt_QuanHuyen.getText().toString().isEmpty()) {
                            quanHuyenSelected.createQuanHuyenDTO(new QuanHuyenDTO());
                            txt_QuanHuyen.setText("");
                        }
                        break;
                    }
                    case 2: {// Chọn Quận Huyện
                        quanHuyenSelected.createQuanHuyenDTO(listQuanHuyen.get(scroll_choice.getCurrentItemPosition()));
                        txt_QuanHuyen.setText(scroll_choice.getCurrentSelection());
                        break;
                    }
                    case 3: {// Chọn Danh Mục
                        danhMucSelected.createCategoryModel(listDanhMuc.get(scroll_choice.getCurrentItemPosition()));
                        txt_DanhMuc.setText(scroll_choice.getCurrentSelection());
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void openLibImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);

            } else {
                startACTION_PICK();

            }
        } else {
            startACTION_PICK();
        }
    }

    private void openCameraImage() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                startACTION_IMAGE_CAPTURE();

                return;
            }
            boolean READ = false;
            boolean WRITE = false;
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                READ = true;
            }
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                WRITE = true;
            }
            if (READ && WRITE) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_READ_WRITE_EXTERNAL_STORAGE);
            } else {
                if (READ)
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE_CAMERA);
                if (WRITE)
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_WRITE_EXTERNAL_STORAGE_CAMERA);
            }


        } else {
            startACTION_IMAGE_CAPTURE();
        }
    }

    private void startACTION_PICK() {

        if (numOfImage == 0) {
            Toast.makeText(getContext(), getResources().getString(R.string.thongbao4), Toast.LENGTH_SHORT).show();
            return;
        }
        selection.clear();
        louvre.init(this)
                .setMaxSelection(numOfImage)
                .setRequestCode(CHOOSE_IMAGE)
                .setSelection(selection)
                .open();

    }

    private void startACTION_IMAGE_CAPTURE() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File file = getOutputPhotoFile();
        fileUri = Uri.fromFile(file);
        //}
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // Truyền vào intent và Key để xác định kết quả trả về

        startActivityForResult(intent, CODE_ACTION_IMAGE_CAPTURE);

    }

    private void upLoadInfo(String tenQuan, String diaChi, String soDienThoai, String latitude, String longitude, String gioMoCua, String gioDongCua) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.thongbao1));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (!stringPath1.isEmpty()) {
            upFile(stringPath1, tenQuan, diaChi, soDienThoai, latitude, longitude, gioMoCua, gioDongCua);
        }
        if (!stringPath2.isEmpty()) {
            upFile(stringPath2, tenQuan, diaChi, soDienThoai, latitude, longitude, gioMoCua, gioDongCua);
        }
        if (!stringPath3.isEmpty()) {
            upFile(stringPath3, tenQuan, diaChi, soDienThoai, latitude, longitude, gioMoCua, gioDongCua);
        }
        if (!stringPath4.isEmpty()) {
            upFile(stringPath4, tenQuan, diaChi, soDienThoai, latitude, longitude, gioMoCua, gioDongCua);
        }
        if (!stringPath5.isEmpty()) {
            upFile(stringPath5, tenQuan, diaChi, soDienThoai, latitude, longitude, gioMoCua, gioDongCua);
        }
        if (!stringPath6.isEmpty()) {
            upFile(stringPath6, tenQuan, diaChi, soDienThoai, latitude, longitude, gioMoCua, gioDongCua);
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void upFile(String path, final String tenQuan, final String diaChi, final String soDienThoai, final String latitude, final String longitude, final String gioMoCua, final String gioDongCua) {
        synchronized (this) {
            Uri file = Uri.fromFile(new File(path));

            final String fileName = getFileName(file);
            String time = Calendar.getInstance().get(Calendar.MILLISECOND) + "";
            final String key = new SimpleDateFormat("yyyyMMddHHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date()) + time + sessionUser.getUserDTO().getId();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.HINHDANGQUANAN).child(key);

            UploadTask uploadTask = storageReference.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    urlImageResult.add(key);
                    numOfImage++;
                    if (numOfImage == 6) {
                        postInfo(tenQuan, diaChi, soDienThoai, latitude, longitude, gioMoCua, gioDongCua);
                    }


                }
            });
        }
    }


    public String ToFirstUpper(String s) {
        if (s.isEmpty())
            return s;
        s = s.trim().toLowerCase();

        StringBuilder result = new StringBuilder();

        //lấy danh sách các từ

        String[] words = s.split(" ");

        for (String word : words
                ) {
            // từ nào là các khoảng trắng thừa thì bỏ
            if (!word.trim().equals("")) {
                if (word.length() > 1)
                    result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
                else
                    result.append(word.toUpperCase()).append(" ");
            }
        }
        return result.toString().trim();
    }

    private void postInfo(String tenQuan, String diaChi, String soDienThoai, String latitute, String longitude, String gioMoCua, String gioDongCua) {
        //final String key = MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANAN_DEXUAT).push().getKey();
        final String key = new SimpleDateFormat("yyyyMMddHHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date()) + sessionUser.getUserDTO().getId();
        final QuanAnDTO_POST quanAnDTO = new QuanAnDTO_POST();

        quanAnDTO.setDiachi(ToFirstUpper(diaChi));

        quanAnDTO.setTenquanan(ToFirstUpper(tenQuan));

        quanAnDTO.setLatitude(Double.parseDouble(latitute));
        quanAnDTO.setLongitude(Double.parseDouble(longitude));

        quanAnDTO.setSodienthoai(soDienThoai);

        quanAnDTO.setGiodongcua(gioDongCua);
        quanAnDTO.setGiomocua(gioMoCua);

        quanAnDTO.setIddanhmuc(danhMucSelected.getId());

        quanAnDTO.setIdquanhuyen(quanHuyenSelected.getId());
        quanAnDTO.setIdtinhthanh(tinhThanhSelected.getId());

        quanAnDTO.setIdduong(0);
        quanAnDTO.setIdquanhuyen_iddanhmuc("0");

        String idQuanHuyen_DanhMuc = quanHuyenSelected.getId() + "_" + danhMucSelected.getId();
        quanAnDTO.setIdquanhuyen_iddanhmuc(idQuanHuyen_DanhMuc);

        String idTinhThanh_DanhMuc = tinhThanhSelected.getId() + "_" + danhMucSelected.getId();
        quanAnDTO.setIdtinhthanh_iddanhmuc(idTinhThanh_DanhMuc);

        String idDuong_DanhMuc = "0_" + danhMucSelected.getId();
        quanAnDTO.setIdduong_iddanhmuc(idDuong_DanhMuc);

        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_QUANAN_DEXUAT).child(key).setValue(quanAnDTO).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_HINHQUANAN_DEXUAT).child(key).setValue(urlImageResult).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                refreshData();
                                final Dialog dialog = new Dialog(getContext());
                                dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                dialog.setContentView(R.layout.dialog_success);
                                dialog.setTitle("");
                                dialog.setCanceledOnTouchOutside(false);
                                TextView txt_Co = (TextView) dialog.findViewById(R.id.txt_OK);

                                txt_Co.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();

                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });


                } else

                {
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });


    }

    private void refreshData() {
        image1.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
        img_clear1.setVisibility(View.GONE);
        stringPath1 = "";

        image2.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
        img_clear2.setVisibility(View.GONE);
        stringPath2 = "";

        image3.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
        img_clear3.setVisibility(View.GONE);
        stringPath3 = "";

        image4.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
        img_clear4.setVisibility(View.GONE);
        stringPath4 = "";

        image5.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
        img_clear5.setVisibility(View.GONE);
        stringPath5 = "";

        image6.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
        img_clear6.setVisibility(View.GONE);
        stringPath6 = "";

        numOfImage = 6;

        txt_GioDongCua.setText("");
        txt_GioMoCua.setText("");
        txt_DanhMuc.setText("");
        txt_Longitude.setText("");
        txt_Latitude.setText("");
        txt_TinhThanh.setText("");
        txt_QuanHuyen.setText("");
        edt_SoDienThoai.setText("");
        edt_DiaChi.setText("");
        edt_TenQuan.setText("");

        danhMucSelected = new CategoryModel();
        tinhThanhSelected = new TinhThanhPhoDTO();
        quanHuyenSelected = new QuanHuyenDTO();
        urlImageResult.clear();

        scrollView.scrollTo(0, 0);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.linear_XacDinhViTri: {
                Intent intent = new Intent(getActivity(), MapsActivity_XacDinhViTri.class);

                getActivity().startActivityForResult(intent, REQUEST);
                break;
            }
            case R.id.btn_XacNhan: {
                if (sessionUser.getUserDTO().getToken() == null || sessionUser.getUserDTO().getToken().isEmpty()) {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                    dialog.setContentView(R.layout.dialog_login);
                    dialog.setTitle("");
                    dialog.setCanceledOnTouchOutside(false);
                    Button btn_co = (Button) dialog.findViewById(R.id.btn_codangnhap);
                    Button btn_khong = (Button) dialog.findViewById(R.id.btn_khongdangnhap);
                    btn_co.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginManager.getInstance().logInWithReadPermissions(getActivity(), asList("email", "public_profile"));
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
                if (txt_DanhMuc.getText().toString().isEmpty() || txt_QuanHuyen.getText().toString().isEmpty() || txt_TinhThanh.getText().toString().isEmpty()
                        || edt_TenQuan.getText().toString().isEmpty() || edt_DiaChi.getText().toString().isEmpty()
                        || edt_SoDienThoai.getText().toString().isEmpty()
                        ) {
                    Toast.makeText(getContext(), getResources().getString(R.string.thongbao6), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_GioDongCua.getText().toString().isEmpty() || txt_GioMoCua.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.thongbao9), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (txt_Longitude.getText().toString().isEmpty() || txt_Latitude.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.thongbao8), Toast.LENGTH_SHORT).show();
                    return;
                }
                setNumOfImage();
                if (numOfImage > 3) {
                    Toast.makeText(getContext(), getResources().getString(R.string.thongbao7), Toast.LENGTH_SHORT).show();
                    return;
                }

                String sdt = edt_SoDienThoai.getText().toString().trim();
                if (!sdt.isEmpty()) {
                    String sdtPattern = "^[+]?[0-9]{8,20}$";
                    if (!sdt.matches(sdtPattern)) {
                        edt_SoDienThoai.setError(getResources().getString(R.string.thongbao10));
                        Toast.makeText(getContext(), getResources().getString(R.string.thongbao10), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String gioMoCua = txt_GioMoCua.getText().toString();
                String gioDongCua = txt_GioDongCua.getText().toString();
                if (!compareTime(gioMoCua, gioDongCua)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.thongbao11), Toast.LENGTH_SHORT).show();
                    return;
                }

                upLoadInfo(edt_TenQuan.getText().toString().trim(), edt_DiaChi.getText().toString().trim(), edt_SoDienThoai.getText().toString().trim(), txt_Latitude.getText().toString().trim(), txt_Longitude.getText().toString().trim(), txt_GioMoCua.getText().toString(), txt_GioDongCua.getText().toString());
                break;
            }

            case R.id.img_ChonGioDongCua: {
                isOpen = false;
                createdDialog().show();
                break;
            }
            case R.id.img_ChonGioMoCua: {
                isOpen = true;
                createdDialog().show();
                break;
            }
            case R.id.rela_ChonDanhMuc: {

                showDialog(CODE_ID_DANHMUC);
                break;
            }
            case R.id.rela_ChonQuanHuyen: {
                if (listNameQuanHuyen.isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.thongbao5), Toast.LENGTH_SHORT).show();
                    return;
                }
                showDialog(CODE_ID_QUANHUYEN);
                break;
            }
            case R.id.rela_ChonTinhThanh: {
                showDialog(CODE_ID_TINHTHANH);
                break;
            }
            case R.id.img_camera: {
                openCameraImage();
                break;
            }
            case R.id.img_thuvien: {
                openLibImage();

                break;
            }

            case R.id.img_clear1: {
                image1.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
                img_clear1.setVisibility(View.GONE);
                stringPath1 = "";
                numOfImage++;
                break;
            }
            case R.id.img_clear2: {
                image2.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
                img_clear2.setVisibility(View.GONE);
                stringPath2 = "";
                numOfImage++;
                break;
            }
            case R.id.img_clear3: {
                image3.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
                img_clear3.setVisibility(View.GONE);
                stringPath3 = "";
                numOfImage++;
                break;
            }
            case R.id.img_clear4: {
                image4.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
                img_clear4.setVisibility(View.GONE);
                stringPath4 = "";
                numOfImage++;
                break;
            }
            case R.id.img_clear5: {
                image5.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
                img_clear5.setVisibility(View.GONE);
                stringPath5 = "";
                numOfImage++;
                break;
            }
            case R.id.img_clear6: {
                image6.setImageDrawable(getResources().getDrawable(R.drawable.upload_empty));
                img_clear6.setVisibility(View.GONE);
                stringPath6 = "";
                numOfImage++;
                break;
            }
        }
    }

    private boolean compareTime(String gioMoCua, String gioDongCua) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        Date open = null;
        Date close = null;
        try {
            open = parser.parse(gioMoCua);
            close = parser.parse(gioDongCua);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        assert close != null;
        return !close.before(open);
    }

    private void setNumOfImage() {
        int num = 0;
        if (!stringPath1.isEmpty())
            num++;
        if (!stringPath2.isEmpty())
            num++;
        if (!stringPath3.isEmpty())
            num++;
        if (!stringPath4.isEmpty())
            num++;
        if (!stringPath5.isEmpty())
            num++;
        if (!stringPath6.isEmpty())
            num++;

        numOfImage = 6 - num;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!GlobalBus.getBus().isRegistered(this))
            GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (GlobalBus.getBus().isRegistered(this))
            GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventSentLatLong event) {
        if (event.getLatitude() != 0 && event.getLongitude() != 0) {
            txt_Latitude.setText(event.getLatitude() + "");
            txt_Longitude.setText(event.getLongitude() + "");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_READ_EXTERNAL_STORAGE: {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    startACTION_PICK();
                }
                break;
            }
            case CODE_READ_WRITE_EXTERNAL_STORAGE: {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startACTION_IMAGE_CAPTURE();
                } else
                    return;
                break;
            }
            case CODE_READ_EXTERNAL_STORAGE_CAMERA: {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startACTION_IMAGE_CAPTURE();
                } else
                    return;
                break;
            }
            case CODE_WRITE_EXTERNAL_STORAGE_CAMERA: {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startACTION_IMAGE_CAPTURE();
                } else
                    return;
                break;
            }


        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST) {
           /* if (resultCode == MapsActivity_XacDinhViTri.RESULT) {
                String latitude = String.valueOf(data.getDoubleExtra(Common.KEY_INTENT.KEY_LATITUDE, 0));
                String longitude = String.valueOf(data.getDoubleExtra(Common.KEY_INTENT.KEY_LONGITUDE, 0));
                if (!latitude.equals("0") && !longitude.equals("0")) {
                    txt_Latitude.setText(latitude);
                    txt_Longitude.setText(longitude);
                }
            }*/
        } else if (requestCode == CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data == null)
                    return;
                selection = GalleryActivity.getSelection(data);

                for (int i = 0; i < selection.size(); i++) {
                    if (stringPath1.isEmpty()) {
                        stringPath1 = selection.get(i).getPath();
                        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                        Tiny.getInstance().source(stringPath1).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                            @Override
                            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                                //return the compressed file path and bitmap object
                                image1.setImageBitmap(bitmap);
                                img_clear1.setVisibility(View.VISIBLE);
                                stringPath1 = outfile;
                                numOfImage--;
                            }
                        });
                    } else if (stringPath2.isEmpty()) {
                        stringPath2 = selection.get(i).getPath();
                        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                        Tiny.getInstance().source(stringPath2).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                            @Override
                            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                                //return the compressed file path and bitmap object
                                image2.setImageBitmap(bitmap);
                                img_clear2.setVisibility(View.VISIBLE);
                                stringPath2 = outfile;
                                numOfImage--;
                            }
                        });
                    } else if (stringPath3.isEmpty()) {
                        stringPath3 = selection.get(i).getPath();
                        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                        Tiny.getInstance().source(stringPath3).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                            @Override
                            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                                //return the compressed file path and bitmap object
                                image3.setImageBitmap(bitmap);
                                img_clear3.setVisibility(View.VISIBLE);
                                stringPath3 = outfile;
                                numOfImage--;
                            }
                        });
                    } else if (stringPath4.isEmpty()) {
                        stringPath4 = selection.get(i).getPath();
                        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                        Tiny.getInstance().source(stringPath4).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                            @Override
                            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                                //return the compressed file path and bitmap object
                                image4.setImageBitmap(bitmap);
                                img_clear4.setVisibility(View.VISIBLE);
                                stringPath4 = outfile;
                                numOfImage--;
                            }
                        });
                    } else if (stringPath5.isEmpty()) {
                        stringPath5 = selection.get(i).getPath();
                        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                        Tiny.getInstance().source(stringPath5).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                            @Override
                            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                                //return the compressed file path and bitmap object
                                image5.setImageBitmap(bitmap);
                                img_clear5.setVisibility(View.VISIBLE);
                                stringPath5 = outfile;
                                numOfImage--;
                            }
                        });
                    } else if (stringPath6.isEmpty()) {
                        stringPath6 = selection.get(i).getPath();
                        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                        Tiny.getInstance().source(stringPath6).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                            @Override
                            public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                                //return the compressed file path and bitmap object
                                image6.setImageBitmap(bitmap);
                                img_clear6.setVisibility(View.VISIBLE);
                                stringPath6 = outfile;
                                numOfImage--;
                            }
                        });
                    }

                }
            }


            return;
        } else if (requestCode == CODE_ACTION_IMAGE_CAPTURE) {

            if (resultCode == Activity.RESULT_OK) {
                if (fileUri == null)
                    return;

                if (stringPath1.isEmpty()) {
                    stringPath1 = fileUri.getPath();
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(stringPath1).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                        @Override
                        public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                            //return the compressed file path and bitmap object
                            image1.setImageBitmap(bitmap);
                            img_clear1.setVisibility(View.VISIBLE);
                            stringPath1 = outfile;
                            numOfImage--;
                        }
                    });
                } else if (stringPath2.isEmpty()) {
                    stringPath2 = fileUri.getPath();
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(stringPath2).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                        @Override
                        public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                            //return the compressed file path and bitmap object
                            image2.setImageBitmap(bitmap);
                            img_clear2.setVisibility(View.VISIBLE);
                            stringPath2 = outfile;
                            numOfImage--;
                        }
                    });
                } else if (stringPath3.isEmpty()) {
                    stringPath3 = fileUri.getPath();
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(stringPath3).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                        @Override
                        public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                            //return the compressed file path and bitmap object
                            image3.setImageBitmap(bitmap);
                            img_clear3.setVisibility(View.VISIBLE);
                            stringPath3 = outfile;
                            numOfImage--;
                        }
                    });
                } else if (stringPath4.isEmpty()) {
                    stringPath4 = fileUri.getPath();
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(stringPath4).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                        @Override
                        public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                            //return the compressed file path and bitmap object
                            image4.setImageBitmap(bitmap);
                            img_clear4.setVisibility(View.VISIBLE);
                            stringPath4 = outfile;
                            numOfImage--;
                        }
                    });
                } else if (stringPath5.isEmpty()) {
                    stringPath5 = fileUri.getPath();
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(stringPath5).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                        @Override
                        public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                            //return the compressed file path and bitmap object
                            image5.setImageBitmap(bitmap);
                            img_clear5.setVisibility(View.VISIBLE);
                            stringPath5 = outfile;
                            numOfImage--;
                        }
                    });
                } else if (stringPath6.isEmpty()) {
                    stringPath6 = fileUri.getPath();
                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(stringPath6).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                        @Override
                        public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                            //return the compressed file path and bitmap object
                            image6.setImageBitmap(bitmap);
                            img_clear6.setVisibility(View.VISIBLE);
                            stringPath6 = outfile;
                            numOfImage--;
                        }
                    });
                }
            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private File fileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private String imagePick(Intent intent) {

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            fileTemp = new File(Environment.getExternalStorageDirectory(), "" + seconds + TEMP_PHOTO_FILE_NAME);
        } else {

            fileTemp = new File(getActivity().getFilesDir(), "" + seconds + TEMP_PHOTO_FILE_NAME);
        }

        ContentResolver resolver = getActivity().getContentResolver();
        try {

            InputStream inputStream = resolver.openInputStream(intent.getData());
            FileOutputStream fos = new FileOutputStream(fileTemp);
            copyStream(inputStream, fos);
            inputStream.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {

        }

        return fileTemp.getPath();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    public Bitmap saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Bitmap bm = readBitmapAndScale(file.getPath());
            return bm;
        } catch (Exception e) {
            return null;
        }
    }

    public Bitmap readBitmapAndScale(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //Chỉ đọc thông tin ảnh, không đọc dữ liệu
        BitmapFactory.decodeFile(path, options); //Đọc thông tin ảnh

        //
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        //

        // options.inSampleSize = calculateInSampleSize(options, widthPixels, heightPixels);
        //Log.d("dsadá",calculateInSampleSize(options, widthPixels, heightPixels)+"");
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false; //Cho phép đọc dữ liệu ảnh ảnh

        options.inScaled = true;

        return BitmapFactory.decodeFile(path, options);
    }

    private File getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getContext().getPackageName());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                //Log.e(TAG, "Failed to create storage directory.");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date());
        String path = directory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
        //setStringPath(path);
        return new File(path);
    }
}
