package com.weather.analysis.controller;

import com.weather.analysis.model.WeatherReport;
import com.weather.analysis.service.WeatherService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/{city}")
    public WeatherReport updateWeather(@PathVariable String city) {
        return weatherService.fetchAndStore(city);
    }

    @GetMapping("/high-humidity")
    public List<WeatherReport> getHighHumidity(@RequestParam int level) {
        return weatherService.getHighHumidityCities(level);
    }
}