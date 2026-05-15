package com.steamapi_hashmap;

import com.weather.analysis.WeatherAnalysisApplication; // Make sure this import matches your main class path
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WeatherAnalysisApplication.class) // <-- Hand-feed the class to the test
class WeatherForecastAssessmentApplicationTests {

    @Test
    void contextLoads() {
        // Test passes if the application context boots up successfully
    }
}