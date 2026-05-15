package com.weather.analysis.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle our custom 404 City Not Found Exception
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCityNotFound(CityNotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            System.currentTimeMillis(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handle external API/Integration issues (502 Bad Gateway)
    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<ErrorResponse> handleWeatherApiError(WeatherApiException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_GATEWAY.value(),
            ex.getMessage(),
            System.currentTimeMillis(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    // Catch-all for any unhandled global server exceptions (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred on the server.",
            System.currentTimeMillis(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}