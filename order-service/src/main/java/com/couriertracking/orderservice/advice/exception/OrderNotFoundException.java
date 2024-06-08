package com.couriertracking.orderservice.advice.exception;

import static com.couriertracking.orderservice.advice.constant.ErrorCodes.ORDER_NOT_FOUND;

public class OrderNotFoundException extends CourierTrackingRuntimeException {

    public OrderNotFoundException() {
        super(ORDER_NOT_FOUND, "Customer not found!");
    }

}
