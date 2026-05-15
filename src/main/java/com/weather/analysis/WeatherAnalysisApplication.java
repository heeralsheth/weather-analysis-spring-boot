package com.weather.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Enables the background task scheduler
public class WeatherAnalysisApplication {
    public static void main(String[]String[] args) {
        SpringApplication.run(WeatherAnalysisApplication.class, args);
    }
}