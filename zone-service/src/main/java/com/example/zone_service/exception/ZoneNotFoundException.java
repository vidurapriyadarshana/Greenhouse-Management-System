package com.example.zone_service.exception;

public class ZoneNotFoundException extends RuntimeException {
    public ZoneNotFoundException(String message) {
        super(message);
    }
}
