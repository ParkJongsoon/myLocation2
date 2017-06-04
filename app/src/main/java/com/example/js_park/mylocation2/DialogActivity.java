package com.example.js_park.mylocation2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class DialogActivity extends Dialog implements View.OnTouchListener
{

    private EditText inputName, inputLatitude, inputLongitude;
    private Button addOK, addCancel;
    private String markerName,markerLatitude,markerLongitude;
    protected static int key;



    public DialogActivity(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog); // 커스텀 다이얼로그 레이아웃

        inputName = (EditText) findViewById(R.id.markkerName);
        inputLatitude = (EditText) findViewById(R.id.markerLatitude);
        inputLongitude = (EditText) findViewById(R.id.markerLongitude);

        addOK = (Button) findViewById(R.id.addOK);
        addCancel = (Button) findViewById(R.id.addCancel);

        addOK.setOnTouchListener(this);
        addCancel.setOnTouchListener(this);
    }

    @Override
    protected void onStart() {
        inputLatitude.setText(String.valueOf(MyMap.touchLatLng.latitude));
        inputLongitude.setText(String.valueOf(MyMap.touchLatLng.longitude));
    }

    @Override // 터치 리스너
    public boolean onTouch(View v, MotionEvent event) {
        // 확인 버튼을 클릭하면 입력한 값을 적절히 설정한 후 다이얼로그를 닫음
        if (v == addOK) {
            key =1;

            markerName = inputName.getText().toString();
            markerLatitude = inputLatitude.getText().toString();
            markerLongitude = inputLongitude.getText().toString();
            //여기서 DB 작동해야함
            Log.d("test..",markerName+" "+markerLatitude+" "+markerLongitude);

            //db insert ...하 ㅠㅠ


            dismiss(); // 이후 MainActivity에서 구현해준 Dismiss 리스너가 작동함
        }

        // 취소 버튼을 클릭하면 단순히 다이얼로그를 닫음
        else if (v == addCancel) {
            key = 0; //key 값을 통해 구분을 해봤어요..
            cancel();
        }
        return false;
    }
}