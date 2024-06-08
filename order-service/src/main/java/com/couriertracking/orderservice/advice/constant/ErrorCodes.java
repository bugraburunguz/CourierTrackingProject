package com.couriertracking.orderservice.advice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodes {
    public static final int COURIER_NOT_FOUND = 109000;
    public static final int CUSTOMER_NOT_FOUND = 109001;
    public static final int ORDER_NOT_FOUND = 109002;

}
