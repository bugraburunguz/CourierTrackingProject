package com.couriertracking.courierservice.model.haversine;

public interface HaversineStrategy {
    double calculate(double lat1, double lon1, double lat2, double lon2);
}

