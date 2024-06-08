package com.couriertracking.evaluationservice.factory;

import com.couriertracking.evaluationservice.model.Haversine;
import com.couriertracking.evaluationservice.model.impl.HaversineImpl;

public class HaversineFactory {
    public static Haversine createHaversine() {
        return new HaversineImpl();
    }
}