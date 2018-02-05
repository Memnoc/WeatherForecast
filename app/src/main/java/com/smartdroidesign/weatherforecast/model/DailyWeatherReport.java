package com.smartdroidesign.weatherforecast.model;

/**
 * Created by mstara on 05/02/2018.
 */

public class DailyWeatherReport {

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

    public String rawDateToPretty (String rawDate){
        // convert raw date into formatted date
        return "May 1st";
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
