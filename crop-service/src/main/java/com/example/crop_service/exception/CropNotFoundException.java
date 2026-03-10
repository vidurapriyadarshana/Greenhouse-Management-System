package com.example.crop_service.exception;

public class CropNotFoundException extends RuntimeException {
    public CropNotFoundException(String message) {
        super(message);
    }
}
