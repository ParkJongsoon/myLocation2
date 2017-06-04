package com.example.js_park.mylocation2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.js_park.mylocation2.DialogActivity.markerLatitude;
import static com.example.js_park.mylocation2.DialogActivity.markerLongitude;
import static com.example.js_park.mylocation2.DialogActivity.markerName;

public class MyMap extends Fragment
{
    GoogleMap _map;
    ViewGroup root_page;
    SupportMapFragment mapFragment;
    Map makingMapObject;
    DialogActivity _dialogActivity;
    static LatLng touchLatLng;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        makingMapObject = new Map(getActivity(), _map);
        root_page = (ViewGroup) inflater.inflate(R.layout.activity_my_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        _dialogActivity = new DialogActivity(getActivity());
        _dialogActivity.setTitle("마커 추가");

        //그거 머냐 다이얼로그가 사라지면?!
        _dialogActivity.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(_dialogActivity.key ==1)
                {
                    selectMap();
                    Toast.makeText(getActivity(),"마커를 추가하였습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        _dialogActivity.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                Toast.makeText(getActivity(), "마커를 추가하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

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
                        touchLatLng = latLng;
                        showDialog(touchLatLng);
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

    public void selectMap()
    {
        makingMapObject.selectDestination(touchLatLng,_map);
    }

    public void showDialog(LatLng latLng)
    {
        Log.d("제발 please....",String.valueOf(latLng));
        _dialogActivity.show();
    }
}


