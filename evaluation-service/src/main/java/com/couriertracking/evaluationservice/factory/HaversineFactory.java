package com.couriertracking.evaluationservice.factory;

import com.couriertracking.evaluationservice.model.Haversine;
import com.couriertracking.evaluationservice.model.impl.HaversineImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.logging.Level;
public class HaversineFactory {
    private HaversineFactory() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static Haversine createHaversine() {
        return new HaversineImpl();
    }

}