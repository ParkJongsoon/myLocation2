package com.example.js_park.mylocation2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by db2 on 2017-06-06.
 */

public class GetMarkerThread extends Thread
{
    String _url;
    Context _context;
    GoogleMap _map;
    Handler _handler = new Handler();
    Map map;
    String _getData;
    private static final String TAG_RESULTS = "result";
    private static final String marker_name = "marker_name";
    private static final String marker_latitude = "marker_latitude";
    private static final String marker_longitude = "marker_longitude";
    JSONArray _jsonArray = null;

    public GetMarkerThread(String url,Context context,GoogleMap gmap)
    {
        _url = url;
        _context = context;
        _map = gmap;
    }

    //region GetMarkerThread
    public void run()
    {
        map = new Map(_context,_map);
        try
        {
//            final String output = request(_url);
            getMarker(_url);
            _handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.d("return","test_이따 주석 푸셈 그리고 출력 변수 output 변경");
                }
            });
        }catch (Exception e)
        {
            Log.d("run",e.toString());
        }
    }

    private String request(String url)
    {
        StringBuilder output = new StringBuilder();
        try
        {
            URL inputUrl = new URL(url);
            //URL 객체를 이용(url)하여 HTTPURLConnetion 객체 생성
            HttpURLConnection conn = (HttpURLConnection)inputUrl.openConnection();

            if(conn!=null)
            {
                conn.setConnectTimeout(10000); //ms이므로 10초
                conn.setRequestMethod("GET"); //요청하는 메소드는 가져와야 하므로 getegetegetegetet겟잇뷰티
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //서버 접속하여 요청 -> 여기서 내부적으로 웹서버에 페이지를 요청하는 과정 수행
                int resCode = conn.getResponseCode();

                //응답결과를 읽기위한 스트림 객체
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                String line = null; //line
                //라인별로 반복하여 읽기위해 while 반복문 사용
                while(true)
                {
                    //반복되는 동안 한 줄씩 읽어 결과를 문자열에 추가
                    line = reader.readLine();
                    if(line==null)
                    {
                        break;
                    }
                    output.append(line+"\n");
                }
                reader.close();
                conn.disconnect();
                }
        }
        catch (Exception ex)
        {
            Log.d("request_exception_is",ex.toString());
        }
        return output.toString();
    }
    //endregion

    //region AsyncTask

    public void getMarker(String url)
    {
        class GetMarkerAsyncTask extends AsyncTask<String, Void,String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String uri = params[0];
                StringBuilder sb = new StringBuilder();

                BufferedReader bufferedReader = null;
                try
                {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();


                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null)
                    {
                        sb.append(json + "\n");
                        Log.d("test",sb.toString());
                    }

                } catch (Exception e)
                {
                    Log.d("Exception_error",e.toString());
                }
                return sb.toString().trim();
            }

            @Override
            protected void onPostExecute(String result)
            {
                _getData = result;
                Log.d("onPostExecute", result);
                showList();
            }
        }
        //endregion
        GetMarkerAsyncTask _getMarker = new GetMarkerAsyncTask();
        _getMarker.execute(url);
    }

    //region showList
    protected void showList()
    {
        ArrayList<Marker> _marker = new ArrayList<>();
        try
        {
            JSONObject jsonObj = new JSONObject(_getData);
            _jsonArray = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < _jsonArray.length(); i++)
            {
                Marker marker = new Marker();

                JSONObject c = _jsonArray.getJSONObject(i);

                String name = c.getString(marker_name);
                String latitude = c.getString(marker_latitude);
                String longitude = c.getString(marker_longitude);

                LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                Map markingMap = new Map(_context,_map);
                markingMap.selectDestination(latLng,_map);

//                marker.set_name(name);
//                marker.set_latitude(latitude);
//                marker.set_lonitude(longitude);
//
//                _marker.add(marker);
            }
        }
        catch (JSONException e)
        {
            Log.d("showList_error",e.toString());
        }
    }
    //endregion
}
