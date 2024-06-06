package com.couriertracking.couriermodel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CourierStatus {

    AVAILABLE(1),
    BUSY(2),
    OFFLINE(3);

    private int status;
    public static int of(int status) {
        return Arrays.stream(CourierStatus.values())
                .filter(courierStatus -> courierStatus.getStatus() == status)
                .findFirst()
                .map(CourierStatus::getStatus)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status: " + status));
    }
}