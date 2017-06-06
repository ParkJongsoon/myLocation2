package com.example.js_park.mylocation2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

    public GetMarkerThread(String url,Context context,GoogleMap gmap)
    {
        _url = url;
        _context = context;
        _map = gmap;
    }

    //region GetMarkerThread
    public void run()
    {
        map = new Map();
        try
        {
//            final String output = request(_url);
            _handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    getMarker(_url);
                    Log.d("return","test_이따 주석 푸셈 그리고 출력 변수 output 변경");
                }
            });
        }catch (Exception e)
        {
            Log.d("run",e.toString());
        }
    }

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
                Log.d("onPostExecute", result);
                _getData = result;
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
            //JSONArray에 가져온 getData 삽입
            JSONArray jsonArray = new JSONArray(_getData);

            for (int i = 0; i < jsonArray.length(); i++)
            {
                //JSONArray 만큼 반복
                JSONObject _object = jsonArray.getJSONObject(i);

                String name = _object.getString(marker_name);
                String latitude = _object.getString(marker_latitude);
                String longitude = _object.getString(marker_longitude);
                LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                Map markingMap = new Map();
                markingMap.selectDestination(latLng,_map);

                Marker marker = new Marker();
                marker.set_name(name);
                marker.set_latitude(latitude);
                marker.set_lonitude(longitude);

                _marker.add(marker);
                Log.d("count_i_is",String.valueOf(i));
            }
        }
        catch (JSONException e)
        {
            Log.d("showList_error",e.toString());
        }
    }
    //endregion
}
