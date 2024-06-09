package com.couriertracking.orderservice.controller;

import com.couriertracking.ordermodel.request.OrderRequest;
import com.couriertracking.ordermodel.response.OrderResponse;
import com.couriertracking.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PutMapping("/{orderId}/{courierId}/deliver-order")
    public void deliverOrder(@PathVariable Long orderId, @PathVariable Long courierId) {
        orderService.deliverOrder(orderId, courierId);
    }
}