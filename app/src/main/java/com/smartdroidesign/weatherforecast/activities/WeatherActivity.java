package com.smartdroidesign.weatherforecast.activities;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.smartdroidesign.weatherforecast.R;
import com.smartdroidesign.weatherforecast.model.DailyWeatherReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    final String BASE_URL =  "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_COORD = "/?lat=";//40.925471&lon=9.4944273";
    final String URL_UNITS = "&units=metric";
    final String API_TOKEN = "&APPID=3a834efe29e097a21b0d01f86439e5ee";

    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSION_LOCATION = 111;
    private ArrayList<DailyWeatherReport> weatherReportList = new ArrayList<>();

    private ImageView weatherMain;
    private ImageView weatherMini;
    private TextView dateTxt;
    private TextView currentTempTxt;
    private TextView minTempTxt;
    private TextView locationTxt;
    private TextView conditionTxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        weatherMain = findViewById(R.id.weather_main);
        weatherMini = findViewById(R.id.weather_logo);
        dateTxt = findViewById(R.id.dateTxt);
        currentTempTxt = findViewById(R.id.maxTempTxt);
        minTempTxt = findViewById(R.id.minTempTxt);
        locationTxt = findViewById(R.id.locationTxt);
        conditionTxt = findViewById(R.id.conditionTxt);


        mGoogleApiClient  =new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }



    public void downloadWeatherData(Location location){
        final String fullCords = URL_COORD + location.getLatitude() + "&lon=" + location.getLongitude();
        final String url = BASE_URL + fullCords + URL_UNITS + API_TOKEN;
        // "https://swapi.co/api/people/1/" StarWars API
        // "https://pokeapi.co/api/v2/pokemon/" Pokemon API

        Log.v("FUN2", "URL: " + url);


        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("FUN", "RES: " + response.toString());

                try {
                    JSONObject city = response.getJSONObject("city");
                    String cityName = city.getString("name");
                    String country = city.getString("country");

                    JSONArray list = response.getJSONArray("list");

                    for (int i = 0; i < 5; i++) {
                        JSONObject obj = list.getJSONObject(i);
                        JSONObject main = obj.getJSONObject("main");
                        Double currentTemp = main.getDouble("temp");
                        Double maxTemp = main.getDouble("temp_max");
                        Double minTemp = main.getDouble("temp_min");


                        JSONArray weatherArr = obj.getJSONArray("weather");
                        JSONObject weather =  weatherArr.getJSONObject(0);
                        String weatherType = weather.getString("main");

                        String rawDate = obj.getString("dt_txt");

                        DailyWeatherReport report = new DailyWeatherReport(cityName, country, currentTemp.intValue(), maxTemp.intValue(), minTemp.intValue(), weatherType, rawDate);
                        weatherReportList.add(report);

                        Log.v("JSON", "Printing from class: " + report.getWeather());

                    }


                    Log.v("JSON", "Name: " + cityName + " - " + "country: " + country);

                } catch (JSONException e){

                    Log.v("JSON", "EXC: " + e.getLocalizedMessage());
                }

                updateUI();

            }





        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("FUN", "Err: " + error.getLocalizedMessage());

            }
        });

        Volley.newRequestQueue(this).add(jsonRequest);

    }

//    private ImageView weatherMain;
//    private ImageView weatherMini;
//    private TextView dateTxt;
//    private TextView currentTempTxt;
//    private TextView minTempTxt;
//    private TextView locationTxt;
//    private TextView conditionTxt;

    public void updateUI(){
        if(weatherReportList.size() > 0){
            DailyWeatherReport report = weatherReportList.get(0);

            switch (report.getWeather()) {
                case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                    weatherMini.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                    weatherMain.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    weatherMini.setImageDrawable(getResources().getDrawable(R.drawable.rainy));
                    weatherMain.setImageDrawable(getResources().getDrawable(R.drawable.rainy));
                    break;
                default:
                    weatherMini.setImageDrawable(getResources().getDrawable(R.drawable.sunny));
                    weatherMain.setImageDrawable(getResources().getDrawable(R.drawable.sunny));

            }

            dateTxt.setText("Today, May 1st");
            currentTempTxt.setText(Integer.toString(report.getCurrentTemp()));
            minTempTxt.setText(Integer.toString(report.getMinTemp()));
            locationTxt.setText(report.getCityName() + ", " + report.getCountry());
            conditionTxt.setText(report.getWeather());

        }

    }

    @Override
    public void onLocationChanged(Location location) {
        downloadWeatherData(location);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
        } else {
            startLocationServices();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    public void startLocationServices(){
        try {
            LocationRequest req = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,req,this);
        } catch (SecurityException exception) {
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationServices();
                } else {
                    // show a pop up dialog stating permission has been denied
                    Toast.makeText(this, "I cannot run your location yo", Toast.LENGTH_LONG).show();

                }
            }
        }
    }


}