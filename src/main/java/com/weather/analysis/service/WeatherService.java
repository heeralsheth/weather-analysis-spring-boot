package com.weather.analysis.service;

import com.weather.analysis.model.WeatherReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);
    private final Map<String, WeatherReport> weatherCache = new ConcurrentHashMap<>();
    
    // Define entry expiration time (e.g., 15 minutes in milliseconds)
    private static final long EXPIRATION_TIME_MS = 15 * 60 * 1000;

    // ... (keep your existing fetchAndStore and getHighHumidityCities methods)

    /**
     * Background task to evict stale cache entries.
     * fixedRate = 60000 means this runs every 60,000 milliseconds (1 minute).
     */
    @Scheduled(fixedRate = 60000)
    public void evictStaleWeatherData() {
        long currentTime = System.currentTimeMillis();

        log.info("Starting scheduled cache eviction scan. Current cache size: {}", weatherCache.size());

        // Use Stream API to identify keys where data has expired
        weatherCache.entrySet().stream()
            .filter(entry -> (currentTime - entry.getValue().lastUpdated()) > EXPIRATION_TIME_MS)
            .map(Map.Entry::getKey)
            // Collect to a list first to avoid ConcurrentModificationException during stream execution
            .toList() 
            .forEach(cityKey -> {
                weatherCache.remove(cityKey);
                log.info("Evicted stale weather data for city: {}", cityKey);
            });

        log.info("Cache eviction scan complete. Updated cache size: {}", weatherCache.size());
    }
}