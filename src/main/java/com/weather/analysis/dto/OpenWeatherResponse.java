 
package com.weather.analysis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenWeatherResponse(
    String name,
    Main main,
    List<Weather> weather
) {
    public record Main(
        double temp,
        int humidity
    ) {}

    public record Weather(
        String description
    ) {}
}