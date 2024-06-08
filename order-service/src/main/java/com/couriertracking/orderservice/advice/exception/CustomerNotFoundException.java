package com.couriertracking.orderservice.advice.exception;


import static com.couriertracking.orderservice.advice.constant.ErrorCodes.CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends CourierTrackingRuntimeException {

    public CustomerNotFoundException() {
        super(CUSTOMER_NOT_FOUND, "Customer not found!");
    }

}
