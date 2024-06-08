package com.couriertracking.courierservice.service.impl;

import com.couriertracking.courierservice.model.haversine.HaversineStrategy;

import static com.couriertracking.courierservice.advice.constant.CourierServiceConstant.EARTH_RADIUS;

public class HaversineImpl implements HaversineStrategy {
    @Override
    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
