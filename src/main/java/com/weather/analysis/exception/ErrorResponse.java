package com.weather.analysis.exception;

public record ErrorResponse(
    int statusCode,
    String message,
    long timestamp,
    String details
) {}