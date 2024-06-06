package com.couriertracking.courierservice.advice.exception;

import static com.couriertracking.courierservice.advice.constant.ErrorCodes.COURIER_NOT_FOUND;

public class CourierNotFoundException extends CourierTrackingRuntimeException {

    public CourierNotFoundException() {
        super(COURIER_NOT_FOUND, "Courier not found!");
    }

}
