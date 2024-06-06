package com.couriertracking.courierservice.advice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class CourierTrackingRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int code;
    private final HttpStatus status;

    public CourierTrackingRuntimeException(int code, String message) {
        this(BAD_REQUEST, code, message);
    }

    public CourierTrackingRuntimeException(HttpStatus status, int code, String message) {
        super(message);
        this.code = code;
        this.status = status;
    }

}
