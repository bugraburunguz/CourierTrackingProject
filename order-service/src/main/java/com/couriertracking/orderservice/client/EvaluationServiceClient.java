package com.couriertracking.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "evaluation-service", url = "${feign.client.config.evaluation-service.url}")
public interface EvaluationServiceClient {
    @GetMapping("/calculate-distance")
    double calculateDistance(@RequestParam double lat1, @RequestParam double lon1, @RequestParam double lat2, @RequestParam double lon2);
}
