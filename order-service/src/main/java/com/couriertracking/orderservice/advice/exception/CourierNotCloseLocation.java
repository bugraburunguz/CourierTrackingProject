package com.couriertracking.orderservice.advice.exception;

import static com.couriertracking.orderservice.advice.constant.ErrorCodes.COURIER_NOT_CLOSE_TO_LOCATION;

public class CourierNotCloseLocation extends CourierTrackingRuntimeException {

    public CourierNotCloseLocation() {
        super(COURIER_NOT_CLOSE_TO_LOCATION, "Courier not close to location");
    }

}