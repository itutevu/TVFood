package com.example.user.tvfood.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.DataSearch;
import com.example.user.tvfood.Common.INetworkChange;
import com.example.user.tvfood.Common.IsConnect;
import com.example.user.tvfood.Common.LocaleHelper;
import com.example.user.tvfood.Common.LocaleUtils;
import com.example.user.tvfood.Common.NetworkChangeReceiver;
import com.example.user.tvfood.Common.SessionLanguage;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.Model.DataSearch_Model;
import com.example.user.tvfood.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    Handler handler = new Handler();
    private ImageView imageView;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 999;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private SessionLanguage sessionLanguage;
    private BroadcastReceiver networkChangeReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        registerNetworkBroadcastForNougat();

        //Check Internet
        if (!IsConnect.getInstance().isConnectInternet(getBaseContext())) {
            final Dialog dialog = new Dialog(SplashScreen.this);
            dialog.setContentView(R.layout.dialog_connect);
            dialog.setTitle("");
            dialog.setCanceledOnTouchOutside(false);
            TextView txt_ThuLai = (TextView) dialog.findViewById(R.id.txt_ThuLai);

            txt_ThuLai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(SplashScreen.this, SplashScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            dialog.show();
            return;
        }


        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();

            if (!intent.hasExtra("title") && !intent.hasExtra("body")) {
                initView();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setVisibility(View.VISIBLE);
                        makeAnimation(imageView, R.anim.splash_animation2);
                    }
                }, 500);

                getDataSearch();
                return;
            }
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("body");

            createNotification(title, content);
        } else {
            initView();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageView.setVisibility(View.VISIBLE);
                    makeAnimation(imageView, R.anim.splash_animation2);
                }
            }, 500);

            getDataSearch();

        }
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void getDataSearch() {
        FirebaseDatabase.getInstance().getReference().child(Common.KEY_CODE.NODE_QUANANS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DataSearch_Model> dataSearch_models = new ArrayList<>();
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    DataSearch_Model dataSearch_model = item.getValue(DataSearch_Model.class);
                    assert dataSearch_model != null;
                    dataSearch_model.setIdQuanAn(item.getKey());
                    dataSearch_models.add(dataSearch_model);
                }
                DataSearch.getInstance().setDataSearch_models(dataSearch_models);
                init();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {

        sessionLanguage = new SessionLanguage(getBaseContext());


        //Thiết lập ngôn ngữ
        int key_language = sessionLanguage.getKeyLanguage();
        switch (key_language) {
            case Common.KEY_LANGUAGE.KEY_VN: {
                setLocale("vi");
                break;
            }
            case Common.KEY_LANGUAGE.KEY_EN: {
                setLocale("en");
                break;
            }
        }


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (Build.VERSION.SDK_INT >= 23) {
            initPermission();
        } else {
            splashScreen_Controller();

        }

        if (!checkGPS()) {
            SessionLocation.getIntance().setLatitude(-9999);
            SessionLocation.getIntance().setLongitude(-9999);
        }
    }

    private void createNotification(String title, String content) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

    }

    private boolean checkPlayServices(Context context) {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode)) {
                api.makeGooglePlayServicesAvailable(SplashScreen.this);
                //api.getErrorDialog((SplashScreen.this), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getBaseContext(), "This device is not supported.", Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
            }
            return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public void setLocale(String localeCode) {
        String localeCodeLowerCase = localeCode.toLowerCase();

        Resources resources = getApplicationContext().getResources();
        Configuration overrideConfiguration = resources.getConfiguration();
        Locale overrideLocale = new Locale(localeCodeLowerCase);

        LocaleUtils.setLocale(overrideLocale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            overrideConfiguration.setLocale(overrideLocale);
        } else {
            overrideConfiguration.locale = overrideLocale;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getApplicationContext().createConfigurationContext(overrideConfiguration);
        } else {
            resources.updateConfiguration(overrideConfiguration, null);
        }
    }

    /*private void setLocale(String lang) {
        //LocaleHelper.setLocale(getBaseContext(), lang);

        *//*Locale locale = new Locale(lang);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, displayMetrics);
        }*//*


    }*/

    public boolean checkGPS() {
        LocationManager lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        return gps_enabled;
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.img);
        imageView.setVisibility(View.INVISIBLE);
    }

    private void splashScreen_Controller() {
        googleApiClient.connect();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
            }
        }, 1000);


    }

    private void makeAnimation(View view, int animationID) {
        Animation animation = AnimationUtils.loadAnimation(this, animationID);
        view.startAnimation(animation);
    }

    public void initPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        //ACCESS_FINE_LOCATION
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionsNeeded.add("Read CACCESS FINE LOCATION");
        }
        //ACCESS_COARSE_LOCATION
        if (!addPermission(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionsNeeded.add("Read ACCESS COARSE LOCATION");
        }


        if (permissionsList.size() > 0) {

            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                if (Build.VERSION.SDK_INT >= 23) {
                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }

            return;
        }

        boolean check = checkPlayServices(getBaseContext());
        if (check)
            splashScreen_Controller();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                //perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
                // perms.put(android.Manifest.permission.WRITE_SETTINGS, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                // Check for ACCESS_FINE_LOCATION

                /*if (perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                    // && perms.get(android.Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED

                        )*/

                if (perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    splashScreen_Controller();
                } else {
                    // Permission Denied
                    Toast.makeText(SplashScreen.this, getResources().getString(R.string.vuilongcapduquyen), Toast.LENGTH_SHORT).show();
                    initPermission();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                SessionLocation.getIntance().setLatitude(-9999);
                SessionLocation.getIntance().setLongitude(-9999);
                return;
            }
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            SessionLocation.getIntance().setLatitude(location.getLatitude());
            SessionLocation.getIntance().setLongitude(location.getLongitude());
        }

    }


}
