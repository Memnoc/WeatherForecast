package com.smartdroidesign.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    final String BASE_URL =  "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD = "/?lat=40.925471&lon=9.4944273";
    final String URL_UNITS = "&units=metric";
    final String API_TOKEN = "&APPID=3a834efe29e097a21b0d01f86439e5ee";
// 40.925471,9.4944273


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        final String url = BASE_URL + URL_COORD + URL_UNITS + API_TOKEN;

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("FUN", "RES: " + response.toString());

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.v("FUN", "Err: " + error.getLocalizedMessage());

            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);


    }
}
