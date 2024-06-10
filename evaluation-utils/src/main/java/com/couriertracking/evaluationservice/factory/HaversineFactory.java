package com.couriertracking.evaluationservice.factory;

import com.couriertracking.evaluationservice.model.Haversine;
import com.couriertracking.evaluationservice.model.impl.HaversineImpl;

public class HaversineFactory {
    private HaversineFactory() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static Haversine createHaversine() {
        return new HaversineImpl();
    }

}