package com.example.sensor_service.exception;

import com.example.sensor_service.common.CommonResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<CommonResponse<Void>> handleExternalApiException(ExternalApiException ex) {
        logger.error("External API error: {}", ex.getMessage());
        CommonResponse<Void> response = new CommonResponse<>(
            502, "BAD_GATEWAY", ex.getMessage(), null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(TelemetryFetchException.class)
    public ResponseEntity<CommonResponse<Void>> handleTelemetryFetchException(TelemetryFetchException ex) {
        logger.error("Telemetry fetch error: {}", ex.getMessage());
        CommonResponse<Void> response = new CommonResponse<>(
            502, "BAD_GATEWAY", ex.getMessage(), null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleGenericException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        CommonResponse<Void> response = new CommonResponse<>(
            500, "INTERNAL_SERVER_ERROR", "An unexpected error occurred", null
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
