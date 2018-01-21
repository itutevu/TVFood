package com.example.user.tvfood.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.example.user.tvfood.Adapter.Adapter_List_BinhLuan_Detail;
import com.example.user.tvfood.Adapter.Adapter_List_TrangChu;
import com.example.user.tvfood.Adapter.Adapter_ViewPager_PhoBien;
import com.example.user.tvfood.Adapter.HorizontalPagerAdapter;
import com.example.user.tvfood.Common.CalculationByDistance;
import com.example.user.tvfood.Common.CalculationByTime;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.IsConnect;
import com.example.user.tvfood.Common.LocaleHelper;
import com.example.user.tvfood.Common.LocaleUtils;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.Common.SessionNotification;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.ExpandableHeightListView;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.Model.BinhLuanDTO;
import com.example.user.tvfood.Model.Comment_Detail;
import com.example.user.tvfood.Model.EventBoSuuTap;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.Model.UserDTO;
import com.example.user.tvfood.Model.detailDTO;
import com.example.user.tvfood.R;
import com.example.user.tvfood.UI.LayDuLieuBinhLuan.LayDuLieuBinhLuan_Presenter;
import com.example.user.tvfood.UI.LayDuLieuBinhLuan.LayDuLieuBinhLuan_ViewListener;
import com.example.user.tvfood.UI.LayDuLieuQuanAnByID.LayDuLieuQuanAnByID_Presenter;
import com.example.user.tvfood.UI.LayDuLieuQuanAnByID.LayDuLieuQuanAnByID_ViewListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressCustom;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static java.util.Arrays.asList;

public class Activity_ChiTietQuanAn extends AppCompatActivity implements View.OnClickListener, ObservableScrollViewCallbacks, LayDuLieuBinhLuan_ViewListener, LayDuLieuQuanAnByID_ViewListener {
    private static final String KEY_LOVE = "key_love";
    private final static int NUM_OF_ITEM_COMMENT = 5;
    private MaterialMenuView materialMenu;
    private Toolbar toolbar;
    private Button btn_XemBanDo;
    private ObservableScrollView scrollView;
    private FrameLayout progressBar_LoadBanner, progressBar_LoadListHinhAnh;

    private ExpandableHeightListView listView;
    public static ArrayList<Comment_Detail> commentDetails;
    private Adapter_List_BinhLuan_Detail adapter_list_binhLuan_detail;
    private ImageView imgBanner, img_Pin, img_XacNhan;
    private MaterialRatingBar rtbDanhGia;
    private TextView txt_CamXuc1, txt_CamXuc2, txt_CamXuc3, txt_CamXuc4, txt_CamXuc5;
    private TextView txt_DanhGia, txt_DiemDanhGia, txt_DiemDanhGia2, txt_SoHinhAnh, txt_TenCuaHang, txt_SoBinhLuan, txt_GioMoCua, txtMoCua, txtDongCua, txt_KhoangCach, txt_DiaChi, txt_TenQuan, txtXemTatCaBinhLuan;
    private HorizontalInfiniteCycleViewPager horizontalInfiniteCycleViewPager;
    private HorizontalPagerAdapter horizontalPagerAdapter;
    private List<detailDTO> detailDTOs;
    private RelativeLayout rela_Main;
    private List<BinhLuanDTO> listBinhLuan = new ArrayList<>();


    private String idQuanAn = "";

    private QuanAnDTO quanAnDTO;

    SessionUser sessionUser;
    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;

    private Button btn_LienHe;
    private ViewPager viewPager;
    private Adapter_ViewPager_PhoBien adapter_viewPager_phoBien;
    private ImageView img1, img2;
    private boolean isLoad_DanhGia = true;

    private Handler handler_Pin;
    Handler.Callback callback_Pin = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            boolean check = message.getData().getBoolean(KEY_LOVE);
            if (check) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.huyluuquananthanhcong), Toast.LENGTH_SHORT).show();

                img_Pin.setImageDrawable(getResources().getDrawable(R.drawable.icon_pin));
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.luuquananthanhcong), Toast.LENGTH_SHORT).show();

                img_Pin.setImageDrawable(getResources().getDrawable(R.drawable.icon_pin_hover));
            }
            return false;
        }
    };

    private boolean isClickDanhGia = false;
    // Yêu thích
    private boolean isClickYeuThich = true;
    private boolean isYeuThich = true;
    private ImageView img_YeuThich, img_KhongYeuThich;
    private ViewGroup btnLike;
    private View viewYeuThich;
    private TextView txt_YeuThich;
    private ACProgressCustom dialog_Load;

    private LayDuLieuBinhLuan_Presenter layDuLieuBinhLuan_presenter;
    private LayDuLieuQuanAnByID_Presenter layDuLieuQuanAnByID_presenter;

    private RelativeLayout rela_Ads;

    public Activity_ChiTietQuanAn() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__chi_tiet_quan_an);

        firebaseAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        isClickYeuThich = true;
                        final String tokenID = loginResult.getAccessToken().getToken();
                        final String fbID = loginResult.getAccessToken().getUserId();
                        AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
                        MainActivity.relaLogin.setVisibility(View.GONE);
                        MainActivity.btn_Logout.setVisibility(View.VISIBLE);
                        MainActivity.rela_content.setVisibility(View.VISIBLE);
                        dialog_Load.show();

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                UserDTO userDTO = new UserDTO(tokenID, task.getResult().getUser().getUid(), task.getResult().getUser().getDisplayName(), "https://graph.facebook.com/" + fbID + "/picture?access_token=" + tokenID, task.getResult().getUser().getPhoneNumber(), FirebaseInstanceId.getInstance().getToken(), fbID);
                                sessionUser.createSessionUser(userDTO);

                                SessionNotification sessionNotification = new SessionNotification(getBaseContext());
                                sessionNotification.createSessionNotificatione(Common.KEY_NOTIFICATION.KEY_ON);
                                Fragment_CaiDat_Nav.txt_NhanThongBao.setText("ON");

                                if (sessionUser.getUserDTO().getId() != null && !sessionUser.getUserDTO().getId().isEmpty()) {
                                    MainActivity.firebaseDatabase.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(Common.KEY_CODE.NODE_GHIMS).child(sessionUser.getUserDTO().getId() + "").child(idQuanAn).getValue(String.class) != null) {
                                                img_Pin.setImageDrawable(getResources().getDrawable(R.drawable.icon_pin_hover));
                                            }
                                            if (dataSnapshot.child(Common.KEY_CODE.NODE_LUOTTHICHS).child(idQuanAn).child(sessionUser.getUserDTO().getId() + "").getValue(String.class) != null) {
                                                isYeuThich = true;

                                                // animation for button like
                                                TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                                                img_KhongYeuThich.setVisibility(false ? View.VISIBLE : View.GONE);

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        int colorFrom = getResources().getColor(R.color.colorBottomBar);
                                                        int colorTo = getResources().getColor(R.color.colorWhite);
                                                        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                                                        colorAnimation.setDuration(500); // milliseconds
                                                        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                                            @Override
                                                            public void onAnimationUpdate(ValueAnimator animator) {
                                                                btnLike.setBackgroundColor((int) animator.getAnimatedValue());
                                                            }

                                                        });
                                                        colorAnimation.addListener(new AnimatorListenerAdapter() {
                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {
                                                                super.onAnimationEnd(animation);
                                                                viewYeuThich.setVisibility(View.VISIBLE);
                                                                TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                                                                txt_YeuThich.setVisibility(View.VISIBLE);
                                                                img_YeuThich.setVisibility(View.VISIBLE);

                                                                isClickYeuThich = false;
                                                            }
                                                        });
                                                        colorAnimation.start();
                                                    }
                                                }, 400);


                                            } else {
                                                isYeuThich = false;
                                                isClickYeuThich = false;
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    if (!sessionUser.getUserDTO().getId().isEmpty() && sessionUser.getUserDTO().getId() != null) {

                                        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_DANHGIAS).child(idQuanAn).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(sessionUser.getUserDTO().getId())) {
                                                    String rating = dataSnapshot.child(sessionUser.getUserDTO().getId()).getValue(String.class);
                                                    rtbDanhGia.setRating(Float.parseFloat(rating));
                                                    rtbDanhGia.setOnTouchListener(new View.OnTouchListener() {
                                                        @Override
                                                        public boolean onTouch(View view, MotionEvent motionEvent) {
                                                            return true;
                                                        }
                                                    });
                                                    img_XacNhan.setVisibility(View.GONE);
                                                    txt_DanhGia.setVisibility(View.VISIBLE);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }
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
                                dialog_Load.hide();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        dialog_Load.hide();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Facebook Login", exception.toString());
                        dialog_Load.hide();
                    }
                }
        );

        dialog_Load = new ACProgressCustom.Builder(Activity_ChiTietQuanAn.this)
                .speed(25)
                .sizeRatio(0.6f)
                .useImages(R.drawable.load01, R.drawable.load02, R.drawable.load03, R.drawable.load04, R.drawable.load05, R.drawable.load06, R.drawable.load07,
                        R.drawable.load08, R.drawable.load09, R.drawable.load10, R.drawable.load11, R.drawable.load12)
                .build();
        dialog_Load.show();

        initView();
        supportPostponeEnterTransition();


        if (!Adapter_List_TrangChu.getUrlImage().isEmpty()) {
            Picasso.with(this)
                    .load(Adapter_List_TrangChu.getUrlImage())
                    .into(imgBanner, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }

        sessionUser = new SessionUser(getBaseContext());
        if (sessionUser.getUserDTO().getId() == null || sessionUser.getUserDTO().getId().isEmpty()) {
            isYeuThich = false;
            viewYeuThich.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
            txt_YeuThich.setVisibility(View.GONE);
            img_YeuThich.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int colorFrom = getResources().getColor(R.color.colorWhite);
                    int colorTo = getResources().getColor(R.color.colorBottomBar);
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(500); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            btnLike.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                            img_KhongYeuThich.setVisibility(true ? View.VISIBLE : View.GONE);
                        }
                    });
                    colorAnimation.start();
                }
            }, 400);
        }
        handler_Pin = new Handler(callback_Pin);
        setCommentDetails();

        materialMenu.setColor(Color.WHITE);
        materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        materialMenu.setOnClickListener(this);

        detailDTOs = new ArrayList<>();

        Intent intent = getIntent();
        idQuanAn = intent.getStringExtra(Common.KEY_CODE.IDQUANAN);


        adapter_viewPager_phoBien = new Adapter_ViewPager_PhoBien(getBaseContext(), detailDTOs);
        viewPager.setAdapter(adapter_viewPager_phoBien);


        //Lấy dữ liệu bình luận
        layDuLieuBinhLuan_presenter = new LayDuLieuBinhLuan_Presenter();
        layDuLieuBinhLuan_presenter.receiveHandle(this, NUM_OF_ITEM_COMMENT, idQuanAn, false, "", listBinhLuan.size(), sessionUser.getUserDTO().getToken());

        // Lấy dữ liệu chi tiết quán ăn
        layDuLieuQuanAnByID_presenter = new LayDuLieuQuanAnByID_Presenter();
        layDuLieuQuanAnByID_presenter.receiveHandle(this, idQuanAn, sessionUser);
        //RealTime đánh giá
        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_DANHGIAS).child(idQuanAn).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (isLoad_DanhGia)
                    return;
                quanAnDTO.setSl_danhgia(quanAnDTO.getSl_danhgia() + 1);
                quanAnDTO.setDiemdanhgia(String.valueOf(Float.parseFloat(quanAnDTO.getDiemdanhgia()) + 2 * Float.parseFloat(dataSnapshot.getValue(String.class))));

                float diemdanhgia = (float) Math.round(((Float.parseFloat(quanAnDTO.getDiemdanhgia()) / quanAnDTO.getSl_danhgia()) / 1) * 10) / 10;
                if (diemdanhgia < 5f) {
                    txt_DiemDanhGia.setVisibility(View.GONE);
                    txt_DiemDanhGia2.setVisibility(View.VISIBLE);
                    txt_DiemDanhGia2.setText(String.valueOf(diemdanhgia));
                } else {
                    txt_DiemDanhGia2.setVisibility(View.GONE);
                    txt_DiemDanhGia.setVisibility(View.VISIBLE);
                    txt_DiemDanhGia.setText(String.valueOf(diemdanhgia));
                }

                if (Float.parseFloat(dataSnapshot.getValue(String.class)) * 2 <= 2f) {
                    quanAnDTO.setSl_camxuc1(quanAnDTO.getSl_camxuc1() + 1);
                    txt_CamXuc1.setText(String.valueOf(quanAnDTO.getSl_camxuc1()));
                } else {
                    if (Float.parseFloat(dataSnapshot.getValue(String.class)) * 2 <= 4f) {
                        quanAnDTO.setSl_camxuc2(quanAnDTO.getSl_camxuc2() + 1);
                        txt_CamXuc2.setText(String.valueOf(quanAnDTO.getSl_camxuc2()));
                    } else {
                        if (Float.parseFloat(dataSnapshot.getValue(String.class)) * 2 <= 6f) {
                            quanAnDTO.setSl_camxuc3(quanAnDTO.getSl_camxuc3() + 1);
                            txt_CamXuc3.setText(String.valueOf(quanAnDTO.getSl_camxuc3()));
                        } else {
                            if (Float.parseFloat(dataSnapshot.getValue(String.class)) * 2 <= 8f) {
                                quanAnDTO.setSl_camxuc4(quanAnDTO.getSl_camxuc4() + 1);
                                txt_CamXuc4.setText(String.valueOf(quanAnDTO.getSl_camxuc4()));
                            } else {
                                quanAnDTO.setSl_camxuc5(quanAnDTO.getSl_camxuc5() + 1);
                                txt_CamXuc5.setText(String.valueOf(quanAnDTO.getSl_camxuc5()));
                            }
                        }
                    }
                }
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
        });


        // --todo : ads google banner ---
        //MobileAds.initialize(getBaseContext(), Common.KEY_ADS.KEY_UNIT_ID);
        rela_Ads = (RelativeLayout) findViewById(R.id.rela_Ads);
        final AdView adView = new AdView(getBaseContext());
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdUnitId(Common.KEY_ADS.KEY_ID_BANNER);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                rela_Ads.removeView(adView);
                rela_Ads.addView(adView);
            }
        });

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        // --todo : ads google banner ---
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    private void setDataDetail(QuanAnDTO dataDetail) {
        if (Adapter_List_TrangChu.getUrlImage().isEmpty()) {
            if (dataDetail.getHinhquanan().size() != 0)
                Picasso.with(getBaseContext()).load(dataDetail.getHinhquanan().get(0)).into(imgBanner);
        }
        txt_TenCuaHang.setText(dataDetail.getTenquanan());
        txt_TenQuan.setText(dataDetail.getTenquanan());
        txt_TenQuan.setVisibility(View.INVISIBLE);
        txt_SoBinhLuan.setText(String.valueOf(dataDetail.getSobinhluan()));
        txt_GioMoCua.setText(dataDetail.getGiomocua() + " - " + dataDetail.getGiodongcua());
        if (CalculationByTime.SoSanhTime(dataDetail.getGiomocua()) && !CalculationByTime.SoSanhTime(dataDetail.getGiodongcua())) {
            txtDongCua.setVisibility(View.GONE);
            txtMoCua.setVisibility(View.VISIBLE);
        } else {
            txtDongCua.setVisibility(View.VISIBLE);
            txtMoCua.setVisibility(View.GONE);
        }
        if (SessionLocation.getIntance().getLatitude() != -999) {
            LatLng latLngCurrent = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
            LatLng latLngQuanAn = new LatLng(dataDetail.getLatitude(), dataDetail.getLongitude());
            String km = CalculationByDistance.getKm(latLngCurrent, latLngQuanAn);
            txt_KhoangCach.setText(km + " km");
        } else
            txt_KhoangCach.setText("... km");

        txt_DiaChi.setText(dataDetail.getDiachi());
        txt_SoHinhAnh.setText(String.valueOf(dataDetail.getSohinhanh()));


        float diemdanhgia = (float) Math.round(((Float.parseFloat(dataDetail.getDiemdanhgia()) / dataDetail.getSl_danhgia()) / 1) * 10) / 10;
        if (diemdanhgia < 5f) {
            txt_DiemDanhGia.setVisibility(View.GONE);
            txt_DiemDanhGia2.setVisibility(View.VISIBLE);
            txt_DiemDanhGia2.setText(String.valueOf(diemdanhgia));
        } else {
            txt_DiemDanhGia2.setVisibility(View.GONE);
            txt_DiemDanhGia.setVisibility(View.VISIBLE);
            txt_DiemDanhGia.setText(String.valueOf(diemdanhgia));
        }

        txt_CamXuc1.setText(String.valueOf(dataDetail.getSl_camxuc1()));
        txt_CamXuc2.setText(String.valueOf(dataDetail.getSl_camxuc2()));
        txt_CamXuc3.setText(String.valueOf(dataDetail.getSl_camxuc3()));
        txt_CamXuc4.setText(String.valueOf(dataDetail.getSl_camxuc4()));
        txt_CamXuc5.setText(String.valueOf(dataDetail.getSl_camxuc5()));

        isClickYeuThich = false;
        scrollView.scrollTo(0, 0);


    }


    private void setCommentDetails() {
        adapter_list_binhLuan_detail = new Adapter_List_BinhLuan_Detail(getBaseContext(), listBinhLuan, this);
        listView.setExpanded(true);
        listView.setAdapter(adapter_list_binhLuan_detail);
    }


    private void initView() {
        rela_Main = (RelativeLayout) findViewById(R.id.rela_Main);
        btn_LienHe = (Button) findViewById(R.id.btn_LienHe);
        btn_LienHe.setOnClickListener(this);
        txt_CamXuc1 = (TextView) findViewById(R.id.txt_CamXuc1);
        txt_CamXuc2 = (TextView) findViewById(R.id.txt_CamXuc2);
        txt_CamXuc3 = (TextView) findViewById(R.id.txt_CamXuc3);
        txt_CamXuc4 = (TextView) findViewById(R.id.txt_CamXuc4);
        txt_CamXuc5 = (TextView) findViewById(R.id.txt_CamXuc5);
        txt_DiemDanhGia2 = (TextView) findViewById(R.id.txt_DiemDanhGia2);
        txt_DiemDanhGia = (TextView) findViewById(R.id.txt_DiemDanhGia);
        txt_DanhGia = (TextView) findViewById(R.id.txt_DanhGia);
        txt_SoHinhAnh = (TextView) findViewById(R.id.txt_SoHinhAnh);
        progressBar_LoadListHinhAnh = (FrameLayout) findViewById(R.id.progressBar_LoadListHinhAnh);
        progressBar_LoadBanner = (FrameLayout) findViewById(R.id.progressBar_LoadBanner);


        rtbDanhGia = (MaterialRatingBar) findViewById(R.id.rtbDanhGia);

        img_XacNhan = (ImageView) findViewById(R.id.img_XacNhan);
        //img_XacNhan.setVisibility(View.INVISIBLE);
        img_XacNhan.setOnClickListener(this);

        img_Pin = (ImageView) findViewById(R.id.img_Pin);
        img_Pin.setOnClickListener(this);
        txt_TenQuan = (TextView) findViewById(R.id.txt_TenQuan);
        txt_DiaChi = (TextView) findViewById(R.id.txt_DiaChi);
        txt_KhoangCach = (TextView) findViewById(R.id.txt_KhoangCach);
        txtDongCua = (TextView) findViewById(R.id.txtDongCua);
        txtMoCua = (TextView) findViewById(R.id.txtMoCua);
        txt_GioMoCua = (TextView) findViewById(R.id.txt_GioMoCua);
        txt_SoBinhLuan = (TextView) findViewById(R.id.txt_SoBinhLuan);
        txt_TenCuaHang = (TextView) findViewById(R.id.txt_TenCuaHang);
        imgBanner = (ImageView) findViewById(R.id.img_Image);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        scrollView.setScrollViewCallbacks(this);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_XemBanDo = (Button) findViewById(R.id.btn_XemBanDo);
        btn_XemBanDo.setOnClickListener(this);
        txtXemTatCaBinhLuan = (TextView) findViewById(R.id.txtXemTatCaBinhLuan);
        txtXemTatCaBinhLuan.setOnClickListener(this);

        materialMenu = (MaterialMenuView) findViewById(R.id.meterialMenu);

        listView = (ExpandableHeightListView) findViewById(R.id.listView);

        horizontalInfiniteCycleViewPager = (HorizontalInfiniteCycleViewPager) findViewById(R.id.horizontalView);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        int pagerPadding = 120;
        viewPager.setClipToPadding(false);
        viewPager.setPadding(pagerPadding, 0, pagerPadding, 0);
        img1 = (ImageView) findViewById(R.id.img_01);
        img2 = (ImageView) findViewById(R.id.img_02);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);

        // Yêu thích
        btnLike = (ViewGroup) findViewById(R.id.linear_Like);
        btnLike.setOnClickListener(this);
        txt_YeuThich = (TextView) findViewById(R.id.txt_YeuThich);
        img_YeuThich = (ImageView) findViewById(R.id.img_YeuThich);
        viewYeuThich = findViewById(R.id.viewYeuThich);
        img_KhongYeuThich = (ImageView) findViewById(R.id.img_KhongYeuThich);


    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_LienHe: {
                if (quanAnDTO.getSodienthoai().isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.khongcothongtinlienhe), Snackbar.LENGTH_LONG).show();
                    return;
                }
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(quanAnDTO.getSodienthoai().trim())));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(callIntent);
                break;
            }
            case R.id.img_XacNhan: {
                if (!IsConnect.getInstance().isConnect()) {
                    Snackbar.make(rela_Main, getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (sessionUser.getUserDTO().getToken() == null || sessionUser.getUserDTO().getToken().isEmpty()) {
                    final Dialog dialog = new Dialog(Activity_ChiTietQuanAn.this);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                    dialog.setContentView(R.layout.dialog_login);
                    dialog.setTitle("");
                    dialog.setCanceledOnTouchOutside(false);
                    Button btn_co = (Button) dialog.findViewById(R.id.btn_codangnhap);
                    Button btn_khong = (Button) dialog.findViewById(R.id.btn_khongdangnhap);
                    btn_co.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginManager.getInstance().logInWithReadPermissions(Activity_ChiTietQuanAn.this, asList("email", "public_profile"));
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
                if (isClickDanhGia)
                    return;
                isClickDanhGia = true;

                final Dialog dialog = new Dialog(Activity_ChiTietQuanAn.this);
                //dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                dialog.setContentView(R.layout.dialog_danhgia);
                dialog.setTitle("");
                dialog.setCanceledOnTouchOutside(true);
                TextView txt = dialog.findViewById(R.id.txt);
                Button btn_xacnhan = (Button) dialog.findViewById(R.id.btn_xacnhan);
                Button btn_thoat = (Button) dialog.findViewById(R.id.btn_thoat);
                txt.setText(getResources().getString(R.string.banmuondanhgia) + " " + String.valueOf(rtbDanhGia.getRating()) + " " + getResources().getString(R.string.diemcho) + " " + txt_TenCuaHang.getText().toString());
                btn_xacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_DANHGIAS).child(idQuanAn).child(sessionUser.getUserDTO().getId()).runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                if (mutableData.getValue(String.class) == null) {
                                    mutableData.setValue(String.valueOf(rtbDanhGia.getRating()));
                                }
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                txt_DanhGia.setVisibility(View.VISIBLE);
                                img_XacNhan.setVisibility(View.GONE);
                                dialog.dismiss();
                                makeText(getResources().getString(R.string.danhgiathanhcong));
                                isClickDanhGia = false;
                            }
                        });
                    }
                });
                btn_thoat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isClickDanhGia = false;
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            }
            case R.id.img_01: {
                int currentPoi = viewPager.getCurrentItem();
                if (currentPoi != 0)
                    viewPager.setCurrentItem(currentPoi - 1);
                break;
            }
            case R.id.img_02: {
                int n = detailDTOs.size();
                int currentPoi = viewPager.getCurrentItem();
                if (currentPoi != n - 1)
                    viewPager.setCurrentItem(currentPoi + 1);
                break;
            }
            case R.id.img_Pin: {
                if (!IsConnect.getInstance().isConnect()) {
                    Snackbar.make(rela_Main, getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (sessionUser.getUserDTO().getToken() == null || sessionUser.getUserDTO().getToken().isEmpty()) {
                    final Dialog dialog = new Dialog(Activity_ChiTietQuanAn.this);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                    dialog.setContentView(R.layout.dialog_login);
                    dialog.setTitle("");
                    dialog.setCanceledOnTouchOutside(false);
                    Button btn_co = (Button) dialog.findViewById(R.id.btn_codangnhap);
                    Button btn_khong = (Button) dialog.findViewById(R.id.btn_khongdangnhap);
                    btn_co.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginManager.getInstance().logInWithReadPermissions(Activity_ChiTietQuanAn.this, asList("email", "public_profile"));
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

                final Message message = new Message();

                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_GHIMS).child(sessionUser.getUserDTO().getId() + "").child(idQuanAn).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        Bundle bundle = new Bundle();
                        String result = mutableData.getValue(String.class);
                        if (result == null) {
                            String key = new SimpleDateFormat("yyyyMMddHHmmss", Locale.SIMPLIFIED_CHINESE).format(new Date());
                            mutableData.setValue(key);
                            bundle.putBoolean(KEY_LOVE, false);
                            message.setData(bundle);
                            return Transaction.success(mutableData);
                        }
                        mutableData.setValue(null);
                        bundle.putBoolean(KEY_LOVE, true);
                        message.setData(bundle);

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        handler_Pin.sendMessage(message);
                    }
                });
                break;
            }
            case R.id.linear_Like: {
                if (!IsConnect.getInstance().isConnect()) {
                    Snackbar.make(rela_Main, getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (isClickYeuThich)
                    return;
                isClickYeuThich = true;
                if (sessionUser.getUserDTO().getToken() == null || sessionUser.getUserDTO().getToken().isEmpty()) {
                    final Dialog dialog = new Dialog(Activity_ChiTietQuanAn.this);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
                    dialog.setContentView(R.layout.dialog_login);
                    dialog.setTitle("");
                    dialog.setCanceledOnTouchOutside(false);
                    Button btn_co = (Button) dialog.findViewById(R.id.btn_codangnhap);
                    Button btn_khong = (Button) dialog.findViewById(R.id.btn_khongdangnhap);
                    btn_co.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LoginManager.getInstance().logInWithReadPermissions(Activity_ChiTietQuanAn.this, asList("email", "public_profile"));
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
                final Message message = new Message();

                MainActivity.firebaseDatabase.getReference().child(Common.KEY_CODE.NODE_LUOTTHICHS).child(idQuanAn).child(sessionUser.getUserDTO().getId() + "").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        String result = mutableData.getValue(String.class);
                        if (result == null) {
                            mutableData.setValue("1");
                            isYeuThich = true;
                            return Transaction.success(mutableData);
                        }
                        mutableData.setValue(null);
                        isYeuThich = false;

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        if (isYeuThich) {
                            // animation for button like
                            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                            img_KhongYeuThich.setVisibility(false ? View.VISIBLE : View.GONE);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int colorFrom = getResources().getColor(R.color.colorBottomBar);
                                    int colorTo = getResources().getColor(R.color.colorWhite);
                                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                                    colorAnimation.setDuration(500); // milliseconds
                                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animator) {
                                            btnLike.setBackgroundColor((int) animator.getAnimatedValue());
                                        }

                                    });
                                    colorAnimation.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            viewYeuThich.setVisibility(View.VISIBLE);
                                            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                                            txt_YeuThich.setVisibility(View.VISIBLE);
                                            img_YeuThich.setVisibility(View.VISIBLE);

                                            isClickYeuThich = false;
                                        }
                                    });
                                    colorAnimation.start();
                                }
                            }, 400);

                        } else {
                            // animation for button like
                            viewYeuThich.setVisibility(View.GONE);
                            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                            txt_YeuThich.setVisibility(View.GONE);
                            img_YeuThich.setVisibility(View.GONE);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int colorFrom = getResources().getColor(R.color.colorWhite);
                                    int colorTo = getResources().getColor(R.color.colorBottomBar);
                                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                                    colorAnimation.setDuration(500); // milliseconds
                                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                        @Override
                                        public void onAnimationUpdate(ValueAnimator animator) {
                                            btnLike.setBackgroundColor((int) animator.getAnimatedValue());
                                        }

                                    });
                                    colorAnimation.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                                            img_KhongYeuThich.setVisibility(true ? View.VISIBLE : View.GONE);
                                            isClickYeuThich = false;
                                        }
                                    });
                                    colorAnimation.start();
                                }
                            }, 400);
                        }
                    }
                });
                break;
            }
            case R.id.txtXemTatCaBinhLuan: {
                if (!IsConnect.getInstance().isConnect()) {
                    Snackbar.make(rela_Main, getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(Activity_ChiTietQuanAn.this, Activity_ChiTietBinhLuan.class);
                intent.putExtra(Common.KEY_CODE.IDQUANAN, idQuanAn);
                startActivityForResult(intent, 999);

                break;
            }
            case R.id.btn_XemBanDo: {
                if (!IsConnect.getInstance().isConnect()) {
                    Snackbar.make(rela_Main, getResources().getString(R.string.khongcoketnoiinternet), Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!checkGPS()) {
                    showSettingsAlert();
                    return;
                }
                Intent intent = new Intent(Activity_ChiTietQuanAn.this, MapsActivity.class);
                intent.putExtra(Common.KEY_CODE.LATITUDE, quanAnDTO.getLatitude());
                intent.putExtra(Common.KEY_CODE.LONGITUDE, quanAnDTO.getLongitude());
                startActivity(intent);
                break;
            }
            case R.id.meterialMenu: {
                finish();
                //supportFinishAfterTransition();
                break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }


    private void makeText(String text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Activity_ChiTietQuanAn.this);

        // Setting Dialog Title
        alertDialog.setTitle("Cài đặt GPS");
        alertDialog.setCancelable(false);

        // Setting Dialog Message
        alertDialog.setMessage("GPS không hoạt động. Bạn có muốn đi đến cài đặt?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Cài đặt", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 101);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
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


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewHelper.setTranslationY(imgBanner, scrollY / 2);

        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
        int diff = (view.getBottom() - (scrollView.getHeight() + scrollY));


        if (scrollY >= imgBanner.getHeight() + txt_TenCuaHang.getHeight() + 20 && txt_TenQuan.getVisibility() == View.INVISIBLE) {
            txt_TenQuan.setVisibility(View.VISIBLE);
        }

        if (scrollY < imgBanner.getHeight() + txt_TenCuaHang.getHeight() + 20 && txt_TenQuan.getVisibility() == View.VISIBLE) {
            txt_TenQuan.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 999)
        //dialog_Load.dismiss();*/
    }

    @Override
    public void onSuccessLayDuLieuBinhLuan(List<BinhLuanDTO> binhLuanDTOs, boolean isMore) {

        listBinhLuan.addAll(binhLuanDTOs);
        adapter_list_binhLuan_detail.notifyDataSetChanged();
        scrollView.scrollVerticallyTo(0);

    }

    @Override
    public void onFailedLayDuLieuBinhLuan(String message, boolean isMore) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();

        scrollView.scrollVerticallyTo(0);
    }

    @Override
    public void onEmpty(String message, boolean isMore) {

        scrollView.scrollVerticallyTo(0);
    }

    @Override
    public void onSuccess_LayDuLieuQuanAnByID(QuanAnDTO quanAnDTO, boolean isYeuThich, boolean isPin, boolean isRating, String rating) {

        this.quanAnDTO = new QuanAnDTO(quanAnDTO);
        for (int j = 0; j < quanAnDTO.getHinhquanan().size(); j++) {
            detailDTOs.add(new detailDTO(quanAnDTO.getHinhquanan().get(j), getResources().getString(R.string.hinhanh) + " " + (j + 1)));

        }
        adapter_viewPager_phoBien.notifyDataSetChanged();
        scrollView.scrollTo(0, 0);
        Picasso.with(getBaseContext()).load(quanAnDTO.getHinhquanan().get(0)).into(imgBanner);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });


        //Thông tin chi tiết quán ăn
        setDataDetail(quanAnDTO);

        //Thông tin đánh giá
        if (isRating) {
            rtbDanhGia.setRating(Float.parseFloat(rating));
            rtbDanhGia.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
            img_XacNhan.setVisibility(View.GONE);
            txt_DanhGia.setVisibility(View.VISIBLE);
        }

        // Thông tin Yêu Thích và Pin
        if (isPin) {

            img_Pin.setImageDrawable(getResources().getDrawable(R.drawable.icon_pin_hover));
        }
        if (isYeuThich) {
            this.isYeuThich = true;

        } else {
            this.isYeuThich = false;
            viewYeuThich.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
            txt_YeuThich.setVisibility(View.GONE);
            img_YeuThich.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int colorFrom = getResources().getColor(R.color.colorWhite);
                    int colorTo = getResources().getColor(R.color.colorBottomBar);
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(500); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            btnLike.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            TransitionManager.beginDelayedTransition(btnLike, new Slide(Gravity.BOTTOM));
                            img_KhongYeuThich.setVisibility(true ? View.VISIBLE : View.GONE);
                            scrollView.scrollVerticallyTo(0);
                        }
                    });
                    colorAnimation.start();
                }
            }, 400);
        }

        scrollView.scrollVerticallyTo(0);
        isLoad_DanhGia = false;
        dialog_Load.dismiss();
    }

    @Override
    public void onFailed_LayDuLieuQuanAnByID(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();

        isLoad_DanhGia = false;
    }
}
