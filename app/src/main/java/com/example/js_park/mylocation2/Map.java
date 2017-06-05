package com.example.js_park.mylocation2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by db2 on 2017-06-02.
 */

public class Map
{
    Context _context;
    GoogleMap _map;
    LatLng _myCenter;

    public Map(Context context, GoogleMap gmap)
    {
        _context= context;
        _map = gmap;
    }

    protected void requestMyLocation()
    {
        LocationManager manager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,new LocationListener()
                    {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                        }
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,new LocationListener()
                    {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    }
            );
        } catch(SecurityException e) {
            e.printStackTrace();
            Log.d("error",e.toString());
        }
    }

    private void showCurrentLocation(Location location)
    {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        _map.animateCamera(CameraUpdateFactory. newLatLngZoom(curPoint, 15));
        checkLocation(curPoint.latitude,curPoint.longitude);
    }

    protected void selectDestination(LatLng latLng,GoogleMap googleMap)
    {
        _myCenter = latLng;
        googleMap.addMarker(new MarkerOptions().position(_myCenter).
                snippet("Lat:"+ _myCenter.latitude+ "Lng:" + _myCenter.longitude)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    private void checkLocation(double x, double y)
    {
        Log.d("checkLocation","This_is_check_Location");
        double tempLatitude1 = _myCenter.latitude+0.000150;
        double tempLatitude2 = _myCenter.latitude-0.000150;

        double tempLonittude1 = _myCenter.latitude+0.000150;
        double tempLonittude2 = _myCenter.latitude-0.000150;

        if(x<tempLatitude1||x>tempLatitude2)
        {
            Log.d("checkLocation",String.valueOf(tempLatitude1)+" "+String.valueOf(tempLatitude2)+" MyLocation_is :"+String.valueOf(x));
            if(y<tempLonittude1||y>tempLonittude2)
            {
                Log.d("checkLocation",String.valueOf(tempLonittude1)+" "+String.valueOf(tempLonittude2)+" MyLocation_is :"+String.valueOf(y));
                Toast.makeText(_context,"HELLO!!!!!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
