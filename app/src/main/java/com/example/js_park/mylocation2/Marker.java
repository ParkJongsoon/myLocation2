package com.example.js_park.mylocation2;

import android.util.Log;

/**
 * Created by js_park on 2017-06-05.
 */

public class Marker
{
    public String _marker_name;
    public String _marker_latitude;
    public String _marker_longitude;

    public String get_name() {
        return _marker_name;
    }

    public void set_name(String marker_name) {
        _marker_name = marker_name;
    }

    public String get_latitude() {
        return _marker_latitude;
    }

    public void set_latitude(String marker_latitude) {
        _marker_latitude = marker_latitude;
    }

    public String get_longitude() {
        return _marker_longitude;
    }

    public void set_lonitude(String marker_longitude) {
        this._marker_longitude = marker_longitude;
        Log.d("test...",_marker_latitude);
    }

    @Override
    public String toString() {
        return "Marker [marker_name=" + _marker_name + ", marker_latitude=" + _marker_latitude + ", marker_longitude="+ _marker_longitude + "]";
    }
}
