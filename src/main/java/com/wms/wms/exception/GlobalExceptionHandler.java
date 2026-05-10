package com.wms.wms.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wms.wms.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ✅ Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(ResourceNotFoundException ex) {

        logger.error("Resource not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                        "Resource not found",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    // ✅ No Bin Available
    @ExceptionHandler(NoBinAvailableException.class)
    public ResponseEntity<ApiResponse<String>> handleNoBin(NoBinAvailableException ex) {

        logger.error("No bin available: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(
                        "No storage bin available",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    // 🔥 WEEK 3 IMPORTANT
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<String>> handleStock(InsufficientStockException ex) {

        logger.error("Stock issue: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(
                        "Insufficient stock",
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    // ✅ Catch-all (safe version)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception ex) {

        logger.error("Unexpected error occurred: ", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                        "Internal server error",
                        "Something went wrong. Please try again.",
                        LocalDateTime.now()
                ));
    }
}