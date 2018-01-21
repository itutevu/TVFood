package com.example.user.tvfood.UI;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.example.user.tvfood.Adapter.Adapter_RecyclerView_ChiTietBinhLuan;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.LocaleUtils;
import com.example.user.tvfood.Common.SessionNotification;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.LinearLayoutManagerWithSmoothScroller;
import com.example.user.tvfood.Model.BinhLuanDTO;
import com.example.user.tvfood.Model.EventBoSuuTap;
import com.example.user.tvfood.Model.UserDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.GuiBinhLuan.GuiBinhLuan_Presenter;
import com.example.user.tvfood.UI.GuiBinhLuan.GuiBinhLuan_ViewListener;
import com.example.user.tvfood.UI.LayDuLieuBinhLuan.LayDuLieuBinhLuan_Presenter;
import com.example.user.tvfood.UI.LayDuLieuBinhLuan.LayDuLieuBinhLuan_ViewListener;
import com.example.user.tvfood.UI.YeuThichBinhLuan.YeuThichBinhLuan_ViewListener;
import com.example.user.tvfood.libCrop.CropImage;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;

public class Activity_ChiTietBinhLuan extends AppCompatActivity implements View.OnClickListener, ChildEventListener, LayDuLieuBinhLuan_ViewListener, GuiBinhLuan_ViewListener, YeuThichBinhLuan_ViewListener {
    final private static int CODE_READ_EXTERNAL_STORAGE = 102;
    final private static int CODE_WRITE_EXTERNAL_STORAGE_CAMERA = 103;
    final private static int CODE_READ_WRITE_EXTERNAL_STORAGE = 105;
    final private static int CODE_READ_EXTERNAL_STORAGE_CAMERA = 106;
    final private static int CODE_ACTION_IMAGE_CAPTURE = 104;

    final private static int CHOOSE_IMAGE = 999;
    final private static int CROP_IMAGE = 888;
    private Uri fileUri = null;

    private static final int NUM_OF_ITEM = 10;

    private boolean isLoadMore = false;
    private MaterialMenuView materialMenu;
    private String idQuanAn = "";
    private RecyclerView recyclerView;
    private List<BinhLuanDTO> listBinhLuan = new ArrayList<>();
    private Adapter_RecyclerView_ChiTietBinhLuan adapter_recyclerView_chiTietBinhLuan;

    private FrameLayout progress_bar;
    private ProgressBar progressBar_LoadMore;
    private ImageView imgThuVien, imgCamera, imgComment, imgGui;
    private EditText edtNoiDung;
    private boolean isLoad = false;
    private boolean isToast = false;
    private RelativeLayout rela_Image;
    private ImageView img_Clear;
    SessionUser sessionUser;
    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;

    private LayDuLieuBinhLuan_Presenter layDuLieuBinhLuan_presenter;
    private GuiBinhLuan_Presenter guiBinhLuan_presenter;
    private String path_ImageComment = "";
    private byte[] dataImage; //Data Image To Sent
    private ProgressDialog progressDialog;

    public Activity_ChiTietBinhLuan() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                        final ProgressDialog progressDialog = new ProgressDialog(Activity_ChiTietBinhLuan.this);
                        progressDialog.setMessage(getResources().getString(R.string.thongbao1));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                UserDTO userDTO = new UserDTO(tokenID, task.getResult().getUser().getUid(), task.getResult().getUser().getDisplayName(), "https://graph.facebook.com/" + fbID + "/picture?access_token=" + tokenID, task.getResult().getUser().getPhoneNumber(), FirebaseInstanceId.getInstance().getToken(), fbID);
                                sessionUser.createSessionUser(userDTO);

                                SessionNotification sessionNotification = new SessionNotification(getBaseContext());
                                sessionNotification.createSessionNotificatione(Common.KEY_NOTIFICATION.KEY_ON);
                                Fragment_CaiDat_Nav.txt_NhanThongBao.setText("ON");

                                Picasso.with(getBaseContext()).load(sessionUser.getUserDTO().getUrlavatar()).into(MainActivity.profile_image);
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
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.thongbao2), Toast.LENGTH_SHORT).show();

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

        setContentView(R.layout.activity__chi_tiet_binh_luan);

        initView();
        initRecyclerView();
        sessionUser = new SessionUser(getBaseContext());

        Tiny.getInstance().init(getApplication());

        if (sessionUser.getUserDTO().getToken() == null || sessionUser.getUserDTO().getToken().isEmpty()) {
            final Dialog dialog = new Dialog(Activity_ChiTietBinhLuan.this);
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
            dialog.setContentView(R.layout.dialog_login);
            dialog.setTitle("");
            dialog.setCanceledOnTouchOutside(false);
            Button btn_co = (Button) dialog.findViewById(R.id.btn_codangnhap);
            Button btn_khong = (Button) dialog.findViewById(R.id.btn_khongdangnhap);
            btn_co.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logInWithReadPermissions(Activity_ChiTietBinhLuan.this, asList("email", "public_profile"));
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
        }


        Intent intent = getIntent();
        idQuanAn = intent.getStringExtra(Common.KEY_CODE.IDQUANAN);


        materialMenu.setColor(Color.WHITE);
        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        materialMenu.setOnClickListener(this);

        isLoad = true;

        guiBinhLuan_presenter = new GuiBinhLuan_Presenter();
        layDuLieuBinhLuan_presenter = new LayDuLieuBinhLuan_Presenter();
        layDuLieuBinhLuan_presenter.receiveHandle(this, NUM_OF_ITEM, idQuanAn, false, "", listBinhLuan.size(), sessionUser.getUserDTO().getId());


        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_BINHLUANS).child(idQuanAn + "").addChildEventListener(this);
        //realTimeYeuThich();

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(Activity_ChiTietBinhLuan.this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(Activity_ChiTietBinhLuan.this, config);


    }

    private void initView() {
        materialMenu = (MaterialMenuView) findViewById(R.id.meterialMenu);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        progress_bar = (FrameLayout) findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.VISIBLE);

        progressBar_LoadMore = (ProgressBar) findViewById(R.id.progressBar_LoadMore);
        progressBar_LoadMore.setVisibility(View.GONE);

        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgThuVien = (ImageView) findViewById(R.id.imgThuVien);
        imgThuVien.setOnClickListener(this);

        imgComment = (ImageView) findViewById(R.id.imgComment);

        imgGui = (ImageView) findViewById(R.id.imgGui);
        imgGui.setOnClickListener(this);

        edtNoiDung = (EditText) findViewById(R.id.edtNoiDung);

        rela_Image = (RelativeLayout) findViewById(R.id.rela_Image);
        img_Clear = (ImageView) findViewById(R.id.img_Clear);
        img_Clear.setOnClickListener(this);
    }

    private int previousTotal = 0; // ting so item tinh tu lan load truoc
    private int previousTotalOld = 0;
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    private int firstVisibleItem, visibleItemCount, totalItemCount;

    private void initRecyclerView() {
        // set true if your RecyclerView is finite and has fixed size
        recyclerView.setHasFixedSize(false);


        listBinhLuan = new ArrayList<>();


        //
        adapter_recyclerView_chiTietBinhLuan = new Adapter_RecyclerView_ChiTietBinhLuan(getBaseContext(), listBinhLuan, this, this);
        recyclerView.setAdapter(adapter_recyclerView_chiTietBinhLuan);

        final LinearLayoutManagerWithSmoothScroller linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(getBaseContext());
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(linearLayoutManager);
        }
       /* int spaceInPixels = 5;
        recyclerView.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));
*/

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (isLoadMore || isLoad)
                    return;
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                if (loading) {
                    // van dang load
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotalOld = previousTotal;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    // gan het danh sach, load tiep item moi

                    loading = true;
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        if (isLoadMore)
            return;
        isLoadMore = true;
        progressBar_LoadMore.setVisibility(View.VISIBLE);
        layDuLieuBinhLuan_presenter.receiveHandle(this, NUM_OF_ITEM + 1, idQuanAn, true, listBinhLuan.get(listBinhLuan.size() - 1).getIdbinhluan() + "", listBinhLuan.size(), sessionUser.getUserDTO().getId());

    }

    private boolean isGui = false;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.img_Clear: {
                imgComment.setImageDrawable(null);
                rela_Image.setVisibility(View.GONE);
                path_ImageComment = "";
                break;
            }
            case R.id.imgGui: {
                progressDialog = new ProgressDialog(Activity_ChiTietBinhLuan.this);
                progressDialog.setMessage(getResources().getString(R.string.thongbao1));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (sessionUser.getUserDTO().getToken() == null || sessionUser.getUserDTO().getToken().isEmpty()) {
                    final Dialog dialog = new Dialog(Activity_ChiTietBinhLuan.this);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                    dialog.setContentView(R.layout.dialog_login);
                    dialog.setTitle("");
                    dialog.setCanceledOnTouchOutside(false);
                    Button btn_co = (Button) dialog.findViewById(R.id.btn_codangnhap);
                    Button btn_khong = (Button) dialog.findViewById(R.id.btn_khongdangnhap);
                    btn_co.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginManager.getInstance().logInWithReadPermissions(Activity_ChiTietBinhLuan.this, asList("email", "public_profile"));
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


                if (edtNoiDung.getText().toString().trim().equals("") && rela_Image.getVisibility() == View.GONE || isGui)
                    return;


                isGui = true;


                if (rela_Image.getVisibility() == View.GONE || imgComment.getDrawable() == null) {

                    String key = new SimpleDateFormat("yyyyMMddHHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date()) + sessionUser.getUserDTO().getId();

                    guiBinhLuan_presenter.receiveHandle(this, "null", key, sessionUser, idQuanAn, edtNoiDung.getText().toString().trim(), null);

                } else {

                    imgComment.setDrawingCacheEnabled(true);
                    imgComment.buildDrawingCache();
                  /*  Bitmap bitmap = imgComment.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();*/

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date());
                    final String urlimage = timeStamp + sessionUser.getUserDTO().getId();

                    String key = new SimpleDateFormat("yyyyMMddHHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date()) + sessionUser.getUserDTO().getId();

                    guiBinhLuan_presenter.receiveHandle(this, urlimage, key, sessionUser, idQuanAn, edtNoiDung.getText().toString().trim(), dataImage);


                }

                break;
            }
            case R.id.imgCamera: {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

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

                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        // Truyền vào intent và Key để xác định kết quả trả về
                        startActivityForResult(intent, CODE_ACTION_IMAGE_CAPTURE);
                        return;
                    }
                    boolean READ = false;
                    boolean WRITE = false;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        READ = true;
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    // Truyền vào intent và Key để xác định kết quả trả về
                    startActivityForResult(intent, CODE_ACTION_IMAGE_CAPTURE);
                }

                break;
            }
            case R.id.imgThuVien: {

                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);

                    } else {

                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, CHOOSE_IMAGE);
                    }
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, CHOOSE_IMAGE);
                }
                break;
            }
            case R.id.meterialMenu: {
                InputMethodManager keyboard = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(edtNoiDung.getWindowToken(), 0);
                finish();
                break;
            }
        }
    }

    private File getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());
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

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private File fileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

    private String imagePick(Intent intent) {

        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            fileTemp = new File(Environment.getExternalStorageDirectory(), "" + seconds + TEMP_PHOTO_FILE_NAME);
        } else {

            fileTemp = new File(getFilesDir(), "" + seconds + TEMP_PHOTO_FILE_NAME);
        }

        ContentResolver resolver = getContentResolver();
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

    private void startCropImage(String filePath) {
        try {

            int aspectX, aspectY;
            aspectX = 2;
            aspectY = 2;
            Intent intentCropImage = new Intent(this, CropImage.class);
            intentCropImage.putExtra(CropImage.IMAGE_PATH, filePath);
            intentCropImage.putExtra(CropImage.SCALE, true);
            intentCropImage.putExtra(CropImage.ASPECT_X, aspectX);
            intentCropImage.putExtra(CropImage.ASPECT_Y, aspectY);
            // startActivity(intentCropImage);

            intentCropImage.putExtra("i", 0); // 0 là thứ tự hình ảnh
            startActivityForResult(intentCropImage, CROP_IMAGE);
        } catch (Exception e) {
            Log.e("caca", "" + e.getMessage());
        }


    }

    public Bitmap readBitmapAndScale(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //Chỉ đọc thông tin ảnh, không đọc dữ liệu
        BitmapFactory.decodeFile(path, options); //Đọc thông tin ảnh
        //options.inSampleSize = calculateInSampleSize(options, image_Result1.getWidth(), image_Result1.getHeight());; //Scale bitmap xuống 4 lần
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false; //Cho phép đọc dữ liệu ảnh ảnh
        return BitmapFactory.decodeFile(path, options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_READ_EXTERNAL_STORAGE: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, CHOOSE_IMAGE);
                }
                break;
            }
            case CODE_READ_WRITE_EXTERNAL_STORAGE: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                } else
                    return;
                break;
            }
            case CODE_READ_EXTERNAL_STORAGE_CAMERA: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                } else
                    return;
                break;
            }
            case CODE_WRITE_EXTERNAL_STORAGE_CAMERA: {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                } else
                    return;
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String filePath = imagePick(data);
                    //startCropImage(filePath);
                    this.path_ImageComment = filePath;

                    Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                    Tiny.getInstance().source(filePath).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                        @Override
                        public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                            //return the compressed file path and bitmap object
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            dataImage = baos.toByteArray();

                            rela_Image.setVisibility(View.VISIBLE);
                            imgComment.setImageBitmap(bitmap);

                        }
                    });
                }
            }

        } else if (requestCode == CROP_IMAGE) {
            if (resultCode == RESULT_OK) {
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                }
                //setStringPath(path);
                this.path_ImageComment = path;
                Bitmap bm = readBitmapAndScale(path);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                dataImage = baos.toByteArray();

                rela_Image.setVisibility(View.VISIBLE);
                imgComment.setImageBitmap(bm);

            }
        } else if (requestCode == CODE_ACTION_IMAGE_CAPTURE) {

            if (resultCode == Activity.RESULT_OK) {
                if (fileUri == null)
                    return;
                Uri imageUri = Uri.parse(fileUri.getPath());
                File file = new File(imageUri.getPath());

                this.path_ImageComment = fileUri.getPath();
                //startCropImage(file.getPath());
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(file.getPath()).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                    @Override
                    public void callback(boolean isSuccess, Bitmap bitmap, String outfile, Throwable t) {
                        //return the compressed file path and bitmap object
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        dataImage = baos.toByteArray();

                        rela_Image.setVisibility(View.VISIBLE);
                        imgComment.setImageBitmap(bitmap);

                    }
                });

            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (isLoad) {
            return;
        }


        final BinhLuanDTO binhLuanDTO = dataSnapshot.getValue(BinhLuanDTO.class);
        binhLuanDTO.setIdbinhluan(dataSnapshot.getKey());
        binhLuanDTO.setNew(true);
        binhLuanDTO.setIdquanan(idQuanAn + "");
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_THANHVIENS).child(binhLuanDTO.getUserid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserDTO itemUser = dataSnapshot.getValue(UserDTO.class);
                        binhLuanDTO.setUserDTO(itemUser);

                        if (binhLuanDTO.getUrlimage().equals("null")) {
                            if (!binhLuanDTO.getUserid().equals(sessionUser.getUserDTO().getId()) && isToast && binhLuanDTO.getIdquanan().equals(idQuanAn))
                                Toast.makeText(getBaseContext(), binhLuanDTO.getUserDTO().getHoten() + " " + getResources().getString(R.string.dabinhluan), Toast.LENGTH_SHORT).show();

                            adapter_recyclerView_chiTietBinhLuan.addData(binhLuanDTO);
                            recyclerView.smoothScrollToPosition(0);

                        } else {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(Common.KEY_CODE.HINHQUANAN).child(binhLuanDTO.getUrlimage());
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    if (!binhLuanDTO.getUserid().equals(sessionUser.getUserDTO().getId()) && isToast && binhLuanDTO.getIdquanan().equals(idQuanAn))
                                        Toast.makeText(getBaseContext(), binhLuanDTO.getUserDTO().getHoten() + " " + getResources().getString(R.string.dabinhluan), Toast.LENGTH_SHORT).show();
                                    binhLuanDTO.setUrlimage(uri.toString());

                                    adapter_recyclerView_chiTietBinhLuan.addData(binhLuanDTO);

                                    recyclerView.smoothScrollToPosition(0);
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        isToast = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isToast = false;
    }

    @Override
    public void onSuccessLayDuLieuBinhLuan(List<BinhLuanDTO> binhLuanDTOs, boolean isMore) {
        adapter_recyclerView_chiTietBinhLuan.appendData(binhLuanDTOs);
        if (isMore) {
            progressBar_LoadMore.setVisibility(View.GONE);
            isLoad = false;
            isLoadMore = false;
        } else {
            progress_bar.setVisibility(View.GONE);
            isLoad = false;
        }
    }

    @Override
    public void onFailedLayDuLieuBinhLuan(String message, boolean isMore) {
        if (isMore) {
            progressBar_LoadMore.setVisibility(View.GONE);
            isLoad = false;
            isLoadMore = false;
        } else {
            progress_bar.setVisibility(View.GONE);
            isLoad = false;
        }

        // Gán lại biến đếm trước khi loadMore
        this.previousTotal = previousTotalOld;
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmpty(String message, boolean isMore) {
        if (isMore) {
            progressBar_LoadMore.setVisibility(View.GONE);
            isLoad = false;
        } else {
            progress_bar.setVisibility(View.GONE);
            isLoad = false;
        }
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccessGuiBinhLuan() {
        InputMethodManager keyboard = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(edtNoiDung.getWindowToken(), 0);
        edtNoiDung.setText("");
        imgComment.setImageDrawable(null);
        rela_Image.setVisibility(View.GONE);
        isGui = false;
        progressDialog.dismiss();

    }

    @Override
    public void onFailedGuiBinhLuan(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    @Override
    public void onSuccessYeuThichBinhLuan(boolean isYeuThich, String idBinhLuan) {

        for (int i = 0; i < listBinhLuan.size(); i++) {
            if (listBinhLuan.get(i).getIdbinhluan().equals(idBinhLuan)) {
                listBinhLuan.get(i).setYeuThich(isYeuThich);
                if (isYeuThich) {
                    listBinhLuan.get(i).setLuotthich(Long.parseLong(listBinhLuan.get(i).getLuotthich()) + 1 + "");
                } else {
                    listBinhLuan.get(i).setLuotthich(Long.parseLong(listBinhLuan.get(i).getLuotthich()) - 1 + "");
                }
                adapter_recyclerView_chiTietBinhLuan.notifyItemChanged(i);
                break;
            }
        }

    }

    @Override
    public void onFailedYeuThichBinhLuan(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void realTimeYeuThich() {
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_YEUTHICHBINHLUANS).child(idQuanAn).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (isLoad || dataSnapshot.hasChild(sessionUser.getUserDTO().getId())) {
                    return;
                }


                for (int i = 0; i < listBinhLuan.size(); i++) {
                    if (listBinhLuan.get(i).getIdbinhluan().equals(dataSnapshot.getKey())) {
                        listBinhLuan.get(i).setYeuThich(true);
                        listBinhLuan.get(i).setLuotthich(Long.parseLong(listBinhLuan.get(i).getLuotthich()) + 1 + "");

                        adapter_recyclerView_chiTietBinhLuan.notifyItemChanged(i);

                        break;
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (isLoad || dataSnapshot.hasChild(sessionUser.getUserDTO().getId())) {
                    return;
                }
                for (int i = 0; i < listBinhLuan.size(); i++) {
                    if (listBinhLuan.get(i).getIdbinhluan().equals(dataSnapshot.getKey())) {
                        listBinhLuan.get(i).setYeuThich(false);

                        listBinhLuan.get(i).setLuotthich(Long.parseLong(listBinhLuan.get(i).getLuotthich()) - 1 + "");

                        adapter_recyclerView_chiTietBinhLuan.notifyItemChanged(i);

                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
