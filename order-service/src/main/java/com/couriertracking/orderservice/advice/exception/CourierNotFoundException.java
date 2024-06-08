package com.couriertracking.orderservice.advice.exception;

import static com.couriertracking.orderservice.advice.constant.ErrorCodes.COURIER_NOT_FOUND;

public class CourierNotFoundException extends CourierTrackingRuntimeException {

    public CourierNotFoundException() {
        super(COURIER_NOT_FOUND, "Courier not found!");
    }

}
