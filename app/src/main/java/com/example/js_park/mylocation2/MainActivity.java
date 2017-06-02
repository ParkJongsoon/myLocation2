package com.example.js_park.mylocation2;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callFragment();
    }

    private void callFragment()
    {
        FragmentTransaction mtrans = getSupportFragmentManager().beginTransaction();
        MyMap mMyMap = new MyMap();
        mtrans.replace(R.id.fragContainer, mMyMap);
        mtrans.commit();
    }
}
