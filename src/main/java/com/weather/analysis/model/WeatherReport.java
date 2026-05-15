package com.weather.analysis.model;

public record WeatherReport(
    String cityName, 
    double temperature, 
    int humidity, 
    long lastUpdated
) {}


