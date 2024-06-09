package com.couriertracking.orderservice.client;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.orderservice.persistance.entity.CourierEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "courier-service", url = "${feign.client.config.courier-service.url}")
public interface CourierServiceClient {
    @GetMapping("/location/nearest-courier")
    CourierLocationResponse findNearestCourier(@RequestParam double latitude, @RequestParam double longitude);

    @GetMapping("/{id}/total-distance")
    double calculateTotalDistance(@PathVariable Long id);

    @PutMapping("/{id}/update-status")
    void updateStatus(@PathVariable Long id, @RequestParam CourierStatus status);

    @GetMapping("/{id}")
    CourierResponse getCourierById(@PathVariable Long id);

    @GetMapping("/location/{courierId}")
    CourierLocationResponse getCourierLocationById(@PathVariable Long courierId);

}