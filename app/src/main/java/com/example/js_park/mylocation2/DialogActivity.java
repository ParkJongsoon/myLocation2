package com.example.js_park.mylocation2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DialogActivity extends Dialog implements View.OnTouchListener {
    private EditText inputName, inputLatitude, inputLongitude;
    private Button addOK, addCancel;
    protected static String markerName, markerLatitude, markerLongitude;
    protected static int key;
    protected Context _context;
    Marker marker = new Marker();

    public DialogActivity(@NonNull Context context) {
        super(context);
        _context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            key=1;
            marker.set_name(inputName.getText().toString());
            marker.set_latitude(inputLatitude.getText().toString());
            marker.set_lonitude(inputLongitude.getText().toString());
            //여기서 DB 작동해야함
            Log.d("test..", marker.get_name() + " " + marker.get_latitude() + " " + marker.get_longitude());
            MyPostAsyncTask _postSync = new MyPostAsyncTask();
            _postSync.execute();
            dismiss();
        }

        // 취소 버튼을 클릭하면 단순히 다이얼로그를 닫음
        else if (v == addCancel) {
            key = 0; //key 값을 통해 구분을 해봤어요..
            cancel();
        }
        return false;
    }

    public class MyPostAsyncTask extends AsyncTask<Void,Void,Void> {
        //순서[onPreExecute()] -> [doInBackground()] -> [onPostExecute()]

        @Override
        protected Void doInBackground(Void... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://118.91.118.27:52273/test");
            try {
                // 아래처럼 적절히 응용해서 데이터형식을 넣으시고
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("marker_name", marker.get_name()));
                nameValuePairs.add(new BasicNameValuePair("marker_latitude", marker.get_latitude()));
                nameValuePairs.add(new BasicNameValuePair("marker_longitude", marker.get_longitude()));
                Log.d("async_test", nameValuePairs.toString());
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //HTTP Post 요청 실행
                HttpResponse response = httpclient.execute(httppost);
            }
            catch (ClientProtocolException e)
            {
                Log.d("ClientProtocol_error",e.toString());
            }
            catch (IOException e) {
                Log.d("post_err", e.toString());
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}