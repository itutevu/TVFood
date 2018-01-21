package com.example.user.tvfood.Common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

/**
 * Created by THANH on 5/9/2017.
 */

public class GetCurrentLocation_GoogleAPI implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double  mLatitude , mLongitude;
    private String currentQuan,currentTinhThanh;
    public Context getContext() {
        return context;
    }

    public String getCurrentQuan() {
        return currentQuan;

    }

    public void setCurrentQuan(String currentQuan) {
        this.currentQuan = currentQuan;
    }

    public String getCurrentTinhThanh() {
        return currentTinhThanh;
    }

    public void setCurrentTinhThanh(String currentTinhThanh) {
        this.currentTinhThanh = currentTinhThanh;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public GetCurrentLocation_GoogleAPI(Context context) {
        this.context = context;
        init();

    }
    public void init()
    {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        onStart();
    }
    public boolean isLatLog()
    {
        if(getmLatitude()==-9999&&getmLongitude()==-9999)
            return false;
        return true;
    }

    public void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {

                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                String tinhThanh=returnedAddress.getAddressLine(returnedAddress.getMaxAddressLineIndex()-1);
                String quanHuyen=returnedAddress.getAddressLine(returnedAddress.getMaxAddressLineIndex()-2);

                setCurrentTinhThanh(tinhThanh);
                setCurrentQuan(quanHuyen);


            } else {
                //Toast.makeText(getContext(), "Không có kết quả!", Toast.LENGTH_SHORT).show();
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getContext(), "Không thể lấy được địa chỉ!", Toast.LENGTH_SHORT).show();
            //Log.w("My Current loction address", "Canont get Address!");
        }

    }
    private void onStart() {
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    private void onStop() {
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(!checkGPS())
        {
            setmLatitude(-9999);
            setmLongitude(-9999);
            return;
        }
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            setmLatitude(-9999);
            setmLongitude(-9999);
            return;
        }


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            setmLatitude(mLastLocation.getLatitude());
            setmLongitude(mLastLocation.getLongitude());
            getCompleteAddressString(mLatitude,mLongitude);
            //Does this log?
        } else {
            Toast.makeText(getContext(), "Không lấy được vị trí hiện tại", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public boolean checkGPS()
    {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        return gps_enabled;
    }


}
