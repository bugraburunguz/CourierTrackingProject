package com.couriertracking.customerservice.advice.exception;


import static com.couriertracking.customerservice.advice.constant.ErrorCodes.CUSTOMER_NOT_FOUND;

public class CustomerNotFoundException extends CourierTrackingRuntimeException {

    public CustomerNotFoundException() {
        super(CUSTOMER_NOT_FOUND, "Customer not found!");
    }

}
