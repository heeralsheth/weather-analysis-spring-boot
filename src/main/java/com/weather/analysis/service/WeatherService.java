package com.weather.analysis.service;

import com.weather.analysis.model.WeatherReport;
import com.weather.analysis.dto.OpenWeatherResponse;
import com.weather.analysis.exception.CityNotFoundException;
import com.weather.analysis.exception.WeatherApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final Map<String, WeatherReport> weatherCache = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.url:https://api.openweathermap.org/data/2.5/weather}")
    private String apiUrl;

    // The :fallback text tells Spring to use this string if the property is missing
    @Value("${weather.api.key:MOCK_API_KEY_PLACEHOLDER}")
    private String apiKey;
    
    private static final long EXPIRATION_TIME_MS = 15 * 60 * 1000;

    // 1. Fetch from API and Store in Cache
    public WeatherReport fetchAndStore(String city) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
        
        try {
            OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);
            
            if (response == null) {
                throw new CityNotFoundException("City '" + city + "' could not be found or returned empty data.");
            }
            
            WeatherReport report = new WeatherReport(
                response.name(),
                response.main().temp(),
                response.main().humidity(),
                System.currentTimeMillis()
            );
            
            weatherCache.put(city.toLowerCase(), report);
            return report;
            
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            throw new CityNotFoundException("City '" + city + "' not found by the external weather provider.");
        } catch (Exception e) {
            throw new WeatherApiException("Failed to communicate with external weather service infrastructure.", e);
        }
    }

    // 2. Stream API Analysis for High Humidity
    public List<WeatherReport> getHighHumidityCities(int threshold) {
        return weatherCache.values().stream()
                .filter(report -> report.humidity() > threshold)
                .toList();
    }

    // 3. Background Cache Eviction via Stream API
    @Scheduled(fixedRate = 60000)
    public void evictStaleWeatherData() {
        long currentTime = System.currentTimeMillis();

        log.info("Starting scheduled cache eviction scan. Current cache size: {}", weatherCache.size());

        weatherCache.entrySet().stream()
            .filter(entry -> (currentTime - entry.getValue().lastUpdated()) > EXPIRATION_TIME_MS)
            .map(Map.Entry::getKey)
            .toList() 
            .forEach(cityKey -> {
                weatherCache.remove(cityKey);
                log.info("Evicted stale weather data for city: {}", cityKey);
            });

        log.info("Cache eviction scan complete. Updated cache size: {}", weatherCache.size());
    }
}