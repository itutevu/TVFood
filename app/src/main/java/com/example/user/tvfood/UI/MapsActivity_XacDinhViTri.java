package com.example.user.tvfood.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.tvfood.Common.Common;
import com.example.user.tvfood.Common.SessionLocation;
import com.example.user.tvfood.GlobalBus;
import com.example.user.tvfood.Model.EventSentLatLong;
import com.example.user.tvfood.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

public class MapsActivity_XacDinhViTri extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final int RESULT = 555;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Marker marker;

    private double latSelected = 0;
    private double longSelected = 0;
    private FloatingActionButton button_Complete;

    private void setLatLongSelected(LatLng latLongSelected) {
        latSelected = latLongSelected.latitude;
        longSelected = latLongSelected.longitude;
    }

    private ImageView img_Search, img_Back, img_Icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__xac_dinh_vi_tri);

        initView();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initView() {
        img_Icon = findViewById(R.id.img_Icon);
        img_Icon.setVisibility(View.GONE);

        button_Complete = findViewById(R.id.button_Complete);
        button_Complete.setOnClickListener(this);

        img_Search = findViewById(R.id.img_Search);
        img_Search.setOnClickListener(this);

        img_Back = findViewById(R.id.img_Back);
        img_Back.setOnClickListener(this);
    }

    private void addMarker(LatLng latLng, String title) {
        if (marker != null)
            marker.remove();

        MarkerOptions markerOptions = new MarkerOptions()
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.green_marker))
                .position(latLng);

        marker = mMap.addMarker(markerOptions);
        marker.showInfoWindow();


    }

    public String getCompleteAddressString(LatLng latLng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
               /* StringBuilder strReturnedAddress = new StringBuilder("");
                StringBuilder diadiem = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    if (i != returnedAddress.getMaxAddressLineIndex() - 1)
                        diadiem.append(returnedAddress.getAddressLine(i)).append(", ");
                    else
                        diadiem.append(returnedAddress.getAddressLine(i));
                }*/
                if (returnedAddress.getMaxAddressLineIndex() >= 0)
                    strAdd = returnedAddress.getAddressLine(0);
                //Toast.makeText(getBaseContext(), returnedAddress.getAddressLine(0), Toast.LENGTH_SHORT).show();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
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
        // Add a marker in Sydney and move the camera


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String title = getCompleteAddressString(latLng);
                setLatLongSelected(latLng);
                addMarker(latLng, title);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (img_Icon.getVisibility() == View.GONE)
                    img_Icon.setVisibility(View.VISIBLE);
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                img_Icon.setVisibility(View.GONE);
                LatLng latLng = mMap.getCameraPosition().target;

                String title = getCompleteAddressString(latLng);
                setLatLongSelected(latLng);
                addMarker(latLng, title);
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_Complete: {
                GlobalBus.getBus().post(new EventSentLatLong(latSelected, longSelected));
                /*Intent intent = new Intent();
                intent.putExtra(Common.KEY_INTENT.KEY_LATITUDE, latSelected);
                intent.putExtra(Common.KEY_INTENT.KEY_LONGITUDE, longSelected);
                setResult(RESULT, intent);*/
                finish();
                break;
            }
            case R.id.img_Back: {

                finish();
                break;
            }
            case R.id.img_Search: {

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);


                String title = getCompleteAddressString(place.getLatLng());
                setLatLongSelected(place.getLatLng());
                addMarker(place.getLatLng(), title);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                //Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        LatLng latlog1 = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

        String title = getCompleteAddressString(latlog1);
        addMarker(latlog1, title);

        setLatLongSelected(latlog1);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlog1, 15));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
