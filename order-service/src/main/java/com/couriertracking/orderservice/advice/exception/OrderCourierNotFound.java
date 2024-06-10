package com.couriertracking.orderservice.advice.exception;

import static com.couriertracking.orderservice.advice.constant.ErrorCodes.ORDER_COURIER_NOT_FOUND;

public class OrderCourierNotFound extends CourierTrackingRuntimeException {

    public OrderCourierNotFound() {
        super(ORDER_COURIER_NOT_FOUND, "Order courier not found!");
    }

}