package com.example.user.tvfood.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.tvfood.Adapter.Adapter_List_TimQuanAn;
import com.example.user.tvfood.Adapter.Adapter_List_TrangChu;
import com.example.user.tvfood.Common.CalculationByDistance;
import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.LocaleUtils;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.Controller.MapsActivity_TimQuanAn_Controller;
import com.example.user.tvfood.Model.QuanAnDTO;
import com.example.user.tvfood.R;
import com.github.channguyen.rsv.RangeSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity_TimQuanAn extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> markerList = new ArrayList<>();
    private List<QuanAnDTO> quanAnDTOList = new ArrayList<>();
    private Circle circle;
    private LatLng latLngOld;
    public static int DISTANCE = 3; // 3km
    private ImageView imgBack;
    private ListView listView;
    private Adapter_List_TimQuanAn adapter_list_timQuanAn;
    public static TextView txt_KhongCoDuLieu;
    public static FrameLayout progressBar_Load;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    private LinearLayout linear;
    private RelativeLayout rela_Main, rela_Distance;
    private FrameLayout frameLayout;
    private ImageView img_icon;
    private int height = 0;

    private View mapView;
    private MapsActivity_TimQuanAn_Controller mapsActivity_timQuanAn_controller;
    private LatLng latLngCurrent;
    private RangeSliderView rsv_distance;

    private LatLng latLngTarget = new LatLng(0, 0);

    private RelativeLayout rela_Ads;


    public MapsActivity_TimQuanAn() {
        LocaleUtils.updateConfig(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__tim_quan_an);


        initView();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView();

        latLngCurrent = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
        latLngOld = latLngCurrent;

        mapsActivity_timQuanAn_controller = new MapsActivity_TimQuanAn_Controller();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                else
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });

        height = frameLayout.getHeight();

        slidingUpPanelLayout.setScrollableView(listView);
        slidingUpPanelLayout.setVerticalScrollBarEnabled(false);
        slidingUpPanelLayout.setAnchorPoint((float) 0.5);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;

        //ViewHelper.setTranslationY(rela_Main, -(height / 2) / 2);


        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset > 0.5f) {
                    return;
                }
                float headerTranslationY = panel.getHeight() - panel.getY();

                ViewPropertyAnimator.animate(rela_Main).cancel();
                ViewHelper.setTranslationY(rela_Main, -headerTranslationY / 2);

                if (slideOffset == 0f) {
                    rela_Distance.setVisibility(View.VISIBLE);
                    rsv_distance.setVisibility(View.VISIBLE);
                    //rela_Ads.setVisibility(View.VISIBLE);
                } else {
                    if (rela_Distance.getVisibility() == View.VISIBLE) {
                        rela_Distance.setVisibility(View.GONE);
                        //rela_Ads.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

        adapter_list_timQuanAn = new Adapter_List_TimQuanAn(getBaseContext(), quanAnDTOList);
        listView.setAdapter(adapter_list_timQuanAn);


        progressBar_Load.setVisibility(View.VISIBLE);


        rsv_distance.setOnSlideListener(new RangeSliderView.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                switch (index) {
                    case 0: {
                        changeCircleDistance(3);
                        break;
                    }
                    case 1: {
                        changeCircleDistance(4);
                        break;
                    }
                    case 2: {
                        changeCircleDistance(5);
                        break;
                    }
                    case 3: {
                        changeCircleDistance(8);
                        break;
                    }
                    case 4: {
                        changeCircleDistance(10);
                        break;
                    }
                }
            }
        });


        // --todo : ads google banner ---
        //MobileAds.initialize(getBaseContext(), Common.KEY_ADS.KEY_UNIT_ID);
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

    private void changeCircleDistance(int distance) {
        DISTANCE = distance;
        circle.remove();
        circle = mMap.addCircle(new CircleOptions()
                .center(latLngTarget)
                .radius(DISTANCE * 1000)
                .strokeWidth(3f)
                .strokeColor(getResources().getColor(R.color.colorActionBar))
                .fillColor(getResources().getColor(R.color.colorCircle)));

        progressBar_Load.setVisibility(View.VISIBLE);
        for (Marker marker : markerList)
            marker.remove();
        markerList.clear();
        quanAnDTOList.clear();
        adapter_list_timQuanAn.notifyDataSetChanged();

        mapsActivity_timQuanAn_controller.getDuLieuQuanAn(latLngTarget, mMap, quanAnDTOList, markerList, adapter_list_timQuanAn);

    }

    private void initView() {
        img_icon = findViewById(R.id.img_icon);

        rsv_distance = findViewById(R.id.rsv_distance);
        rela_Distance = findViewById(R.id.rela_Distance);
        //rela_Distance.setVisibility(View.GONE);

        rela_Main = findViewById(R.id.rela_Main);
        listView = findViewById(R.id.listView);
        frameLayout = findViewById(R.id.frameLayout);
        linear = findViewById(R.id.linear);
        imgBack = findViewById(R.id.imgBack);
        progressBar_Load = findViewById(R.id.progressBar_Load);
        txt_KhongCoDuLieu = findViewById(R.id.txt_KhongCoDuLieu);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout != null && (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                || (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED))) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED)
                finish();
        } else {
            super.onBackPressed();
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapsActivity_timQuanAn_controller.getDuLieuQuanAn(latLngCurrent, mMap, quanAnDTOList, markerList, adapter_list_timQuanAn);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 50, 70);
        }

        LatLng latLng = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        latLngTarget = new LatLng(SessionLocation.getIntance().getLatitude(), SessionLocation.getIntance().getLongitude());
        circle = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(DISTANCE * 1000)
                .strokeWidth(3f)
                .strokeColor(getResources().getColor(R.color.colorActionBar))
                .fillColor(getResources().getColor(R.color.colorCircle)));


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (img_icon.getVisibility() == View.GONE)
                    img_icon.setVisibility(View.VISIBLE);
            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                img_icon.setVisibility(View.GONE);
                LatLng latLng = mMap.getCameraPosition().target;
                latLngTarget = mMap.getCameraPosition().target;
                if (Double.parseDouble(CalculationByDistance.getKm(latLngOld, latLng)) <= DISTANCE) {
                    return;
                }
                progressBar_Load.setVisibility(View.VISIBLE);
                latLngOld = latLng;
                circle.remove();
                circle = mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .strokeWidth(3f)
                        .strokeColor(getResources().getColor(R.color.colorActionBar))
                        .radius(DISTANCE * 1000)
                        .fillColor(getResources().getColor(R.color.colorCircle)));


                for (Marker marker : markerList)
                    marker.remove();
                markerList.clear();
                quanAnDTOList.clear();
                adapter_list_timQuanAn.notifyDataSetChanged();

                mapsActivity_timQuanAn_controller.getDuLieuQuanAn(latLng, mMap, quanAnDTOList, markerList, adapter_list_timQuanAn);


            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getId().equals(marker.getId())) {
                        Adapter_List_TrangChu.setUrlImage("");
                        Intent intent = new Intent(MapsActivity_TimQuanAn.this, Activity_ChiTietQuanAn.class);
                        intent.putExtra(Common.KEY_CODE.IDQUANAN, quanAnDTOList.get(i).getIdquanan());
                        startActivity(intent);
                        return;

                    }
                }
            }
        });

        // Add a marker in Sydney and move the camera
    }


}
