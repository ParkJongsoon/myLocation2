package com.example.js_park.mylocation2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MyMap extends Fragment
{
    private GoogleMap _map;
    private ViewGroup root_page;
    SupportMapFragment mapFragment;
    Map makingMapObject;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        makingMapObject = new Map(getActivity(), _map);


        root_page = (ViewGroup) inflater.inflate(R.layout.activity_my_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                _map = googleMap;
                LatLng initialization = new LatLng(37.555744, 126.970431);
                _map.animateCamera(CameraUpdateFactory.newLatLngZoom(initialization, 20));
                _map.setMyLocationEnabled(true);

                _map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
                {
                    @Override
                    public void onMapClick(LatLng latLng)
                    {
                        Toast.makeText(getActivity(),"현재 위치는 "+String.valueOf(latLng.latitude)+" "+String.valueOf(latLng.longitude)+"입니다.",Toast.LENGTH_SHORT).show();
                    }
                });

                _map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
                {
                    @Override
                    public void onMapLongClick(LatLng latLng)
                    {

                        makingMapObject.selectDestination(latLng,_map);
                    }
                });
            }
        });

        try
        {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return root_page;
    }
}


