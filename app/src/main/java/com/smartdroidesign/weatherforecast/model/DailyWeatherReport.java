package com.smartdroidesign.weatherforecast.model;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by mstara on 05/02/2018.
 */

public class DailyWeatherReport {

    public static final String WEATHER_TYPE_CLOUDS = "Clouds";
    public static final String WEATHER_TYPE_CLEAR = "Clear";
    public static final String WEATHER_TYPE_RAIN = "Rain";
    public static final String WEATHER_TYPE_WIND = "Wind";
    public static final String WEATHER_TYPE_SNOW = "Snow";

    private String cityName;
    private  String country;
    private int currentTemp;
    private int minTemp;
    private int maxTemp;
    private String weather;
    private String formattedDate;

    public DailyWeatherReport(String cityName, String country, int currentTemp, int minTemp, int maxTemp, String weather, String rawDate) {
        this.cityName = cityName;
        this.country = country;
        this.currentTemp = currentTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.weather = weather;
        this.formattedDate = rawDateToPretty(rawDate);
    }

    // converting time from the epoch format in the end, much easier

    private String rawDateToPretty (String rawDate) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM, dd", Locale.UK);
        Long dateLong = 1000*Long.parseLong(rawDate);
        //int dateLong = Integer.parseInt(rawDate);
        return format.format(dateLong);
    }




    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public String getWeather() {
        return weather;
    }

    public String getFormattedDate() {
        return formattedDate;
    }
}
