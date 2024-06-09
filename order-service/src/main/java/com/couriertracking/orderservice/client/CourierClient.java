package com.couriertracking.orderservice.client;

import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.orderservice.persistance.entity.CourierEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "courier-service", url = "${feign.client.config.courier-service.url}")
public interface CourierClient {
    @GetMapping("/nearest")
    CourierEntity findNearestAvailableCourier(@RequestParam Double storeLat, @RequestParam Double storeLon);

    @GetMapping("/{id}/total-distance")
    double calculateTotalDistance(@PathVariable Long id);
}