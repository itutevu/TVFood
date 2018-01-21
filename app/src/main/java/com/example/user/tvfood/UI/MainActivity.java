package com.example.user.tvfood.UI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.DataSearch;
import com.example.user.tvfood.Common.INetworkChange;
import com.example.user.tvfood.Common.IsConnect;
import com.example.user.tvfood.Common.LocaleHelper;
import com.example.user.tvfood.Common.LocaleUtils;
import com.example.user.tvfood.Common.NetworkChangeReceiver;
import com.example.user.tvfood.Common.SessionNotification;
import com.example.user.tvfood.Common.SessionUser;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.MaterialSearchView.MaterialSearchView;
import com.example.user.tvfood.MaterialSearchView.onSearchListener;
import com.example.user.tvfood.MaterialSearchView.onSimpleSearchActionsListener;
import com.example.user.tvfood.Model.DataSearch_Model;
import com.example.user.tvfood.Model.EventBoSuuTap;
import com.example.user.tvfood.Model.UserDTO;
import com.example.user.tvfood.NonSwipeableViewPager;
import com.example.user.tvfood.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.gigamole.library.PulseView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, INetworkChange, onSimpleSearchActionsListener, onSearchListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private DrawerLayout drawerLayout;
    private boolean direction;
    private MaterialMenuView materialMenuView;
    public static View view;
    public static FirebaseDatabase firebaseDatabase;
    private LoginButton btnLoginFB;
    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;
    public static CircleImageView profile_image;
    public static Button btn_Logout;
    public static RelativeLayout rela_content;
    public static AppBarLayout myAppbar;

    private TabLayout tabLayout;
    public static NonSwipeableViewPager viewPager;
    public static TextView txtTabOld, txt_Name, txt_SDT;
    public static RelativeLayout relaLogin;
    LayoutInflater inflater;
    private TextView txt_ChiaSeUngDung, txt_DanhGia, txt_GioiThieu;
    private RelativeLayout rela_Main;
    public static TextView txt_New;
    private PulseView pulseView;
    SessionUser sessionUser;
    SessionNotification sessionNotification;
    public static Fragment_BoSuuTap_Nav fragment_boSuuTap_nav = new Fragment_BoSuuTap_Nav();
    public static boolean isFirst = true;
    private BroadcastReceiver networkChangeReceive;

    //Search
    private WindowManager mWindowManager;
    private MaterialSearchView mSearchView;
    private Toolbar mToolbar;
    private boolean mSearchViewAdded = false;
    private boolean searchActive = false;
    private MenuItem searchItem;

    private RelativeLayout rela_Ads;

    public MainActivity() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        List<DataSearch_Model> dataSearch_model = DataSearch.getInstance().getDataSearch_models();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.signOut();
        setContentView(R.layout.activity_main);


        // Xử lý đăng nhập
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String tokenID = loginResult.getAccessToken().getToken();
                final String fbID = loginResult.getAccessToken().getUserId();
                AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
                relaLogin.setVisibility(View.GONE);
                rela_content.setVisibility(View.VISIBLE);
                btn_Logout.setVisibility(View.VISIBLE);
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
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

                        if (viewPager.getCurrentItem() == 2) {
                            isFirst = false;
                            GlobalBus.getBus().post(new EventBoSuuTap("1"));
                            //fragment_boSuuTap_nav.getDataGhim(fragment_boSuuTap_nav.NUM_OF_ITEM);
                        } else
                            isFirst = true;

                        Picasso.with(getBaseContext()).load(sessionUser.getUserDTO().getUrlavatar()).into(profile_image);
                        txt_Name.setText(sessionUser.getUserDTO().getHoten());

                        if (sessionUser.getUserDTO().getSdt().equals(""))
                            txt_SDT.setText(getResources().getString(R.string.activity_main_chuacosodienthoai));
                        else
                            txt_SDT.setText(sessionUser.getUserDTO().getSdt());

                        if (sessionUser.getUserDTO().getId().equals("")) {
                            relaLogin.setVisibility(View.VISIBLE);
                            rela_content.setVisibility(View.GONE);
                            btn_Logout.setVisibility(View.GONE);
                            makeText(getResources().getString(R.string.thongbao2));
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
            public void onError(FacebookException error) {

            }
        });


        initView();
        //Check Internet
        if (!IsConnect.getInstance().isConnectInternet(getBaseContext())) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.dialog_connect);
            dialog.setTitle("");
            dialog.setCanceledOnTouchOutside(false);
            TextView txt_ThuLai = (TextView) dialog.findViewById(R.id.txt_ThuLai);

            txt_ThuLai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            dialog.show();
            return;
        }
        isFirst = true;
        sessionNotification = new SessionNotification(getBaseContext());
        sessionUser = new SessionUser(getBaseContext());
        createViewPager(viewPager);
        initTabLayout();

        setOnClick();

        setDrawerListener();


        if (!sessionUser.getUserDTO().getToken().equals("")) {
            Picasso.with(getBaseContext()).load(sessionUser.getUserDTO().getUrlavatar()).into(profile_image);
            txt_Name.setText(sessionUser.getUserDTO().getHoten());
            if (sessionUser.getUserDTO().getSdt().equals(""))
                txt_SDT.setText(getResources().getString(R.string.activity_main_chuacosodienthoai));
            else
                txt_SDT.setText(sessionUser.getUserDTO().getSdt());
            relaLogin.setVisibility(View.GONE);
            rela_content.setVisibility(View.VISIBLE);
            btn_Logout.setVisibility(View.VISIBLE);
        }

        networkChangeReceive = new NetworkChangeReceiver(this);
        registerNetworkBroadcastForNougat();

        // Search
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mSearchView = new MaterialSearchView(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setHintText("Search");


        if (mToolbar != null) {
            // Delay adding SearchView until Toolbar has finished loading
            mToolbar.post(new Runnable() {
                @Override
                public void run() {
                    if (!mSearchViewAdded && mWindowManager != null) {
                        mWindowManager.addView(mSearchView,
                                MaterialSearchView.getSearchViewLayoutParams(MainActivity.this));
                        mSearchViewAdded = true;
                    }
                }
            });
        }
        // End Search


        // --todo : ads google banner ---
        MobileAds.initialize(getBaseContext(), Common.KEY_ADS.KEY_UNIT_ID);
        rela_Ads = (RelativeLayout) findViewById(R.id.rela_Ads);
        final AdView adView = new AdView(getBaseContext());
        adView.setAdSize(AdSize.BANNER);
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

    //Search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        searchItem = menu.findItem(R.id.search);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mSearchView.display();
                openKeyboard();
                return true;
            }
        });
        if (searchActive)
            mSearchView.display();
        return true;
    }

    private void openKeyboard() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                mSearchView.getSearchView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }

    @Override
    public void onSearch(String query) {

    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onCancelSearch() {
        searchActive = false;
        mSearchView.hide();
    }

    @Override
    public void onItemClicked(DataSearch_Model item) {
        Intent intent = new Intent(getBaseContext(), Activity_ChiTietQuanAn.class);
        intent.putExtra(Common.KEY_CODE.IDQUANAN, item.getIdQuanAn());
        startActivity(intent);
    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {
        Toast.makeText(getBaseContext(), localizedMessage, Toast.LENGTH_SHORT).show();
    }

    //End Search
    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            registerReceiver(networkChangeReceive, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkChangeReceive, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkChangeReceive);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterNetworkChanges();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisConnect() {
        Snackbar.make(rela_Main, getResources().getString(R.string.matketnoiinternet), Snackbar.LENGTH_LONG).show();
    }

    private void initView() {
        pulseView = (PulseView) findViewById(R.id.pulseView);
        pulseView.setOnClickListener(this);
        pulseView.startPulse();

        txt_GioiThieu = (TextView) findViewById(R.id.txt_GioiThieu);
        txt_GioiThieu.setOnClickListener(this);

        rela_Main = (RelativeLayout) findViewById(R.id.rela_Main);
        txt_DanhGia = (TextView) findViewById(R.id.txt_DanhGia);
        txt_DanhGia.setOnClickListener(this);

        txt_ChiaSeUngDung = (TextView) findViewById(R.id.txt_ChiaSeUngDung);
        txt_ChiaSeUngDung.setOnClickListener(this);


        myAppbar = (AppBarLayout) findViewById(R.id.myAppbar);
        inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        txt_Name = (TextView) findViewById(R.id.txt_ProfileName);
        txt_SDT = (TextView) findViewById(R.id.txt_SDT);

        btnLoginFB = (LoginButton) findViewById(R.id.connectWithFbButton);
        btnLoginFB.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setSelectedTabIndicatorHeight(0);

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        //view = findViewById(R.id.header);

        materialMenuView = (MaterialMenuView) findViewById(R.id.meterialMenu);
        materialMenuView.setColor(Color.WHITE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.parseColor("#66000000"));

        btn_Logout = (Button) findViewById(R.id.btn_Logout);
        btn_Logout.setOnClickListener(this);
        btn_Logout.setVisibility(View.GONE);

        relaLogin = (RelativeLayout) findViewById(R.id.relaLogin);
        relaLogin.setVisibility(View.VISIBLE);

        rela_content = (RelativeLayout) findViewById(R.id.rela_content);
        rela_content.setVisibility(View.GONE);

    }


    private void createViewPager(ViewPager viewPager) {
        // bỏ PageTransformer
        viewPager.setPageTransformer(false, null);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment_TrangChu_Nav(), getResources().getString(R.string.activity_main_trangchu));
        adapter.addFrag(new Fragment_DangQuanAn_Nav(), getResources().getString(R.string.activity_main_danganh));
        adapter.addFrag(fragment_boSuuTap_nav, getResources().getString(R.string.activity_main_bosuutap));
        adapter.addFrag(new Fragment_CaiDat_Nav(), getResources().getString(R.string.activity_main_caidat));
        adapter.addFrag(new Fragment_ThongBao_Nav(), getResources().getString(R.string.activity_main_chiase));
        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                return;
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    MainActivity.txt_New.setVisibility(View.GONE);
                    MainActivity.txt_New.setText("0");
                }
                if (position != 0) {
                    //MainActivity.myAppbar.setActivated(false);
                    MainActivity.myAppbar.setExpanded(true, true);

                   /* View child = myAppbar.getChildAt(0);
                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) child.getLayoutParams();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);*/
                } else {
                    //MainActivity.myAppbar.setActivated(true);

                   /* View child = myAppbar.getChildAt(0);
                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) child.getLayoutParams();
                    params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);*/
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);


        View v1 = inflater.inflate(R.layout.custom_tab_layout, null);
        TextView txtTabName1 = (TextView) v1.findViewById(R.id.txtTabName);
        ImageView imgIcon1 = (ImageView) v1.findViewById(R.id.imgIcon);
        txtTabName1.setText(getResources().getString(R.string.activity_main_trangchu));
        txtTabName1.setTextColor(Color.parseColor("#FFFFFF"));
        txtTabOld = txtTabName1;
        imgIcon1.setImageResource(R.drawable.ic_home_white);
        tabLayout.getTabAt(0).setCustomView(v1);

        View v2 = inflater.inflate(R.layout.custom_tab_layout, null);
        TextView txtTabName2 = (TextView) v2.findViewById(R.id.txtTabName);
        ImageView imgIcon2 = (ImageView) v2.findViewById(R.id.imgIcon);
        txtTabName2.setText(getResources().getString(R.string.activity_main_danganh));
        txtTabName2.setTextColor(Color.parseColor("#FFFFFF"));
        txtTabName2.setVisibility(View.GONE);
        imgIcon2.setImageResource(R.drawable.ic_menu_camera_white);
        tabLayout.getTabAt(1).setCustomView(v2);

        View v3 = inflater.inflate(R.layout.custom_tab_layout, null);
        TextView txtTabName3 = (TextView) v3.findViewById(R.id.txtTabName);
        ImageView imgIcon3 = (ImageView) v3.findViewById(R.id.imgIcon);
        txtTabName3.setText(getResources().getString(R.string.activity_main_bosuutap));
        txtTabName3.setTextColor(Color.parseColor("#FFFFFF"));
        txtTabName3.setVisibility(View.GONE);
        imgIcon3.setImageResource(R.drawable.ic_menu_gallery_white);
        tabLayout.getTabAt(2).setCustomView(v3);

        View v4 = inflater.inflate(R.layout.custom_tab_layout, null);
        TextView txtTabName4 = (TextView) v4.findViewById(R.id.txtTabName);
        ImageView imgIcon4 = (ImageView) v4.findViewById(R.id.imgIcon);
        txtTabName4.setText(getResources().getString(R.string.activity_main_caidat));
        txtTabName4.setTextColor(Color.parseColor("#FFFFFF"));
        txtTabName4.setVisibility(View.GONE);
        imgIcon4.setImageResource(R.drawable.ic_menu_manage_white);
        tabLayout.getTabAt(3).setCustomView(v4);

        View v5 = inflater.inflate(R.layout.custom_tab_layout, null);
        TextView txtTabName5 = v5.findViewById(R.id.txtTabName);
        ImageView imgIcon5 = v5.findViewById(R.id.imgIcon);
        TextView txt_New = v5.findViewById(R.id.txt_New);
        txt_New.setVisibility(View.GONE);
        txt_New.setText("0");
        this.txt_New = txt_New;

        txtTabName5.setText(getResources().getString(R.string.activity_main_chiase));
        txtTabName5.setTextColor(Color.parseColor("#FFFFFF"));
        txtTabName5.setVisibility(View.GONE);
        imgIcon5.setImageResource(R.drawable.ic_notifications_24dp);
        tabLayout.getTabAt(4).setCustomView(v5);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int poi = tab.getPosition();
                if (isFirst && poi == 2 && sessionUser.getUserDTO().getId() != null && !sessionUser.getUserDTO().getId().isEmpty()) {
                    isFirst = false;
                    GlobalBus.getBus().post(new EventBoSuuTap("2"));
                    //fragment_boSuuTap_nav.getDataGhim(fragment_boSuuTap_nav.NUM_OF_ITEM);
                }
                //Toast.makeText(getBaseContext(), poi + "", Toast.LENGTH_SHORT).show();
                View v = tab.getCustomView();
                ImageView imgIcon = (ImageView) v.findViewById(R.id.imgIcon);
                TextView txtTabName = (TextView) v.findViewById(R.id.txtTabName);

                txtTabOld.setVisibility(View.GONE);
                txtTabName.setVisibility(View.VISIBLE);
                txtTabOld = txtTabName;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setDrawerListener() {
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenuView.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        direction ? 2 - slideOffset : slideOffset);

            }

            @Override
            public void onDrawerOpened(android.view.View drawerView) {
                direction = true;

            }

            @Override
            public void onDrawerClosed(android.view.View drawerView) {
                direction = false;
            }
        });
    }


    private void makeText(String text) {
        Toast.makeText(getBaseContext(), text + "", Toast.LENGTH_SHORT).show();
    }

    private void setOnClick() {
        materialMenuView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.txt_GioiThieu: {
                Intent intent = new Intent(MainActivity.this, Activity_About.class);
                startActivity(intent);
                break;
            }
            case R.id.txt_DanhGia: {
                //Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Uri uri = Uri.parse("market://details?id=" + "net.flychicken.gcg");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            //Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                            Uri.parse("http://play.google.com/store/apps/details?id=" + "net.flychicken.gcg")));
                }
                break;
            }
            case R.id.txt_ChiaSeUngDung: {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=net.flychicken.gcg"))
                        .build();
                ShareDialog.show(MainActivity.this, content);
                break;
            }
            case R.id.btn_Logout: {
                GlobalBus.getBus().post(new EventBoSuuTap("3"));
                //fragment_boSuuTap_nav.quanAnDTOs.clear();
                //fragment_boSuuTap_nav.adapter_boSuuTap.notifyDataSetChanged();
                isFirst = false;
                sessionNotification.clearData();
                Fragment_CaiDat_Nav.txt_NhanThongBao.setText("OFF");
                sessionUser.clearData();
                relaLogin.setVisibility(View.VISIBLE);
                rela_content.setVisibility(View.GONE);
                btn_Logout.setVisibility(View.GONE);
                LoginManager.getInstance().logOut();
                firebaseAuth.signOut();
                profile_image.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
                txt_Name.setText(getResources().getString(R.string.activity_main_hoten));
                txt_SDT.setText(getResources().getString(R.string.activity_main_sodienthoai));

                break;
            }
            case R.id.pulseView: {
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
                break;
            }
            case R.id.connectWithFbButton: {
                btnLoginFB.setReadPermissions("email", "public_profile");
                btnLoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        final String tokenID = loginResult.getAccessToken().getToken();
                        final String fbID = loginResult.getAccessToken().getUserId();
                        AuthCredential authCredential = FacebookAuthProvider.getCredential(tokenID);
                        relaLogin.setVisibility(View.GONE);
                        rela_content.setVisibility(View.VISIBLE);
                        btn_Logout.setVisibility(View.VISIBLE);
                        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                        progressDialog.setMessage(getResources().getString(R.string.thongbao1));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                UserDTO userDTO = new UserDTO(tokenID, task.getResult().getUser().getUid(), task.getResult().getUser().getDisplayName(), "https://graph.facebook.com/" + fbID + "/picture?access_token=" + tokenID, task.getResult().getUser().getPhoneNumber(), FirebaseInstanceId.getInstance().getToken(), fbID);
                                sessionUser.createSessionUser(userDTO);

                                if (viewPager.getCurrentItem() == 2) {
                                    isFirst = false;
                                    GlobalBus.getBus().post(new EventBoSuuTap("1"));
                                    //fragment_boSuuTap_nav.getDataGhim(fragment_boSuuTap_nav.NUM_OF_ITEM);
                                } else
                                    isFirst = true;

                                Picasso.with(getBaseContext()).load(sessionUser.getUserDTO().getUrlavatar()).into(profile_image);
                                txt_Name.setText(sessionUser.getUserDTO().getHoten());

                                if (sessionUser.getUserDTO().getSdt().equals(""))
                                    txt_SDT.setText(getResources().getString(R.string.activity_main_chuacosodienthoai));
                                else
                                    txt_SDT.setText(sessionUser.getUserDTO().getSdt());

                                if (sessionUser.getUserDTO().getId().equals("")) {
                                    relaLogin.setVisibility(View.VISIBLE);
                                    rela_content.setVisibility(View.GONE);
                                    btn_Logout.setVisibility(View.GONE);
                                    makeText(getResources().getString(R.string.thongbao2));
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
                    public void onError(FacebookException error) {

                    }
                });
                break;
            }
            case R.id.meterialMenu: {
                if (!direction)
                    drawerLayout.openDrawer(GravityCompat.START);
                else
                    drawerLayout.closeDrawers();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshDrawerState() {
        this.direction = drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        refreshDrawerState();
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private static class NoPageTransformer implements ViewPager.PageTransformer {
        public void transformPage(View view, float position) {
            if (position < 0) {
                view.setScrollX((int) ((float) (view.getWidth()) * position));
            } else if (position > 0) {
                view.setScrollX(-(int) ((float) (view.getWidth()) * -position));
            } else {
                view.setScrollX(0);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.getWindow().getAttributes().windowAnimations = R.style.AnimationDialog;
            dialog.setContentView(R.layout.dialog_exit);
            dialog.setTitle("");
            dialog.setCanceledOnTouchOutside(false);
            Button btn_co = (Button) dialog.findViewById(R.id.btn_co);
            Button btn_khong = (Button) dialog.findViewById(R.id.btn_khong);
            btn_co.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    AppExit();

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
        return false;
    }

    public void AppExit() {

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);

    }
}
