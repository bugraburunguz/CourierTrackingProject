package com.couriertracking.customerservice.advice;

import com.couriertracking.customerservice.advice.exception.CourierTrackingRuntimeException;
import com.couriertracking.customerservice.model.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CourierTrackingAdviceController {

    @ExceptionHandler(CourierTrackingRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleCourierTrackingRuntimeException(CourierTrackingRuntimeException ex) {
        log.error("CourierTrackingRuntimeException: {} - {}", ex.getCode(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus().value(), ex.getMessage(), "/courier-tracking-error/" + ex.getCode());
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", "/internal-server-error");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}