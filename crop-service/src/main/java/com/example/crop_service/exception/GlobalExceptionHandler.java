package com.example.crop_service.exception;

import com.example.crop_service.common.CommonResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(CropNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleNotFoundException(CropNotFoundException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        CommonResponse<Void> response = new CommonResponse<>(
            404, "NOT_FOUND", ex.getMessage(), null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<CommonResponse<Void>> handleBadRequestException(InvalidStatusTransitionException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        CommonResponse<Void> response = new CommonResponse<>(
            400, "BAD_REQUEST", ex.getMessage(), null
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
