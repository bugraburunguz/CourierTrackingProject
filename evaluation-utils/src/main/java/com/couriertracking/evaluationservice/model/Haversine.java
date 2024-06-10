package com.couriertracking.evaluationservice.model;

public interface Haversine {
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}