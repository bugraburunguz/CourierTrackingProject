package com.couriertracking.storeservice.advice.exception;

import static com.couriertracking.storeservice.advice.constant.ErrorCodes.STORE_NOT_FOUND;

public class StoreNotFoundException extends CourierTrackingRuntimeException {

    public StoreNotFoundException() {
        super(STORE_NOT_FOUND, "Store not found!");
    }

}
