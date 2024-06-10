package com.couriertracking.evaluationservice;

import com.couriertracking.evaluationservice.model.Haversine;
import com.couriertracking.evaluationservice.model.impl.HaversineImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HaversineImplTest {

    private Haversine haversine;

    @BeforeEach
    public void setUp() {
        haversine = new HaversineImpl();
    }

    @Test
    void testCalculateDistance() {
        double lat1 = 40.748817; // New York
        double lon1 = -73.985428;
        double lat2 = 34.052235; // Los Angeles
        double lon2 = -118.243683;

        double distance = haversine.calculateDistance(lat1, lon1, lat2, lon2);

        assertEquals(3937.2169108840662, distance, 0.1); // Expected distance between New York and Los Angeles in kilometers
    }

    @Test
    void testCalculateDistance_SameLocation() {
        double lat = 40.748817; // New York
        double lon = -73.985428;

        double distance = haversine.calculateDistance(lat, lon, lat, lon);

        assertEquals(0, distance);
    }

    @Test
    void testCalculateDistance_NorthPoleToSouthPole() {
        double lat1 = 90.0; // North Pole
        double lon1 = 0.0;
        double lat2 = -90.0; // South Pole
        double lon2 = 0.0;

        double distance = haversine.calculateDistance(lat1, lon1, lat2, lon2);

        assertEquals(20015.09, distance, 0.1); // Expected distance between North Pole and South Pole in kilometers
    }
}
