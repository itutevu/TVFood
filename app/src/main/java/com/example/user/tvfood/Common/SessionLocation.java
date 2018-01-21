package com.example.user.tvfood.Common;

/**
 * Created by USER on 13/08/2017.
 */

public class SessionLocation {
    private double latitude;
    private double longitude;
    private static SessionLocation instance;

    public SessionLocation() {

    }

    public static SessionLocation getIntance()
    {
        if(instance==null)
            instance=new SessionLocation();
        return instance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
