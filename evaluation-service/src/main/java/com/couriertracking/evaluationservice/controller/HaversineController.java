package com.couriertracking.evaluationservice.controller;

import com.couriertracking.evaluationservice.factory.HaversineFactory;
import com.couriertracking.evaluationservice.model.Haversine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HaversineController {

    private final Haversine haversine;

    public HaversineController() {
        this.haversine = HaversineFactory.createHaversine();
    }

    @GetMapping("/calculate-distance")
    public double calculateDistance(
            @RequestParam double lat1,
            @RequestParam double lon1,
            @RequestParam double lat2,
            @RequestParam double lon2) {
        return haversine.calculateDistance(lat1, lon1, lat2, lon2);
    }
}