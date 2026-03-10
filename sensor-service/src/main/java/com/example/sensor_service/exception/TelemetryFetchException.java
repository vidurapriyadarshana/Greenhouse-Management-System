package com.example.sensor_service.exception;

public class TelemetryFetchException extends RuntimeException {
    public TelemetryFetchException(String message) {
        super(message);
    }
}
