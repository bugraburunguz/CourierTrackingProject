package com.couriertracking.ordermodel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING(1),
    DELIVERED(2),
    CANCELLED(3);

    private int status;

    public static int of(int status) {
        return Arrays.stream(OrderStatus.values())
                .filter(orderstatus -> orderstatus.getStatus() == status)
                .findFirst()
                .map(OrderStatus::getStatus)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + status));
    }
}