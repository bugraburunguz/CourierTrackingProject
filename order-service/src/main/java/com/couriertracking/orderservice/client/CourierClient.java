package com.couriertracking.orderservice.client;

import com.couriertracking.couriermodel.response.CourierResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "courier-service", url = "${feign.client.config.courier-service.url}")
public interface CourierClient {

    @GetMapping("/api/couriers/nearest-available")
    CourierResponse getNearestAvailableCourier(@RequestParam double lat, @RequestParam double lng);
}