package com.couriertracking.courierservice.model.haversine;

public class HaversineContext {
    private HaversineStrategy strategy;

    public HaversineContext(HaversineStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(HaversineStrategy strategy) {
        this.strategy = strategy;
    }

    public double executeStrategy(double lat1, double lon1, double lat2, double lon2) {
        return strategy.calculate(lat1, lon1, lat2, lon2);
    }
}