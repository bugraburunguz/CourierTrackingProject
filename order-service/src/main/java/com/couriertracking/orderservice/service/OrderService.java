package com.couriertracking.orderservice.service;

import com.couriertracking.ordermodel.request.OrderRequest;
import com.couriertracking.ordermodel.response.OrderResponse;
import com.couriertracking.orderservice.advice.exception.CourierNotFoundException;
import com.couriertracking.orderservice.advice.exception.CustomerNotFoundException;
import com.couriertracking.orderservice.advice.exception.OrderNotFoundException;
import com.couriertracking.orderservice.converter.OrderConverter;
import com.couriertracking.orderservice.persistance.entity.CourierEntity;
import com.couriertracking.orderservice.persistance.entity.CustomerEntity;
import com.couriertracking.orderservice.persistance.entity.OrderEntity;
import com.couriertracking.orderservice.persistance.repository.CourierRepository;
import com.couriertracking.orderservice.persistance.repository.CustomerRepository;
import com.couriertracking.orderservice.persistance.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CourierRepository courierRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);
        CourierEntity courier = courierRepository.findById(request.getCourierId())
                .orElseThrow(CourierNotFoundException::new);

        OrderEntity order = OrderConverter.toOrderEntity( customer, courier);
        OrderEntity savedOrder = orderRepository.save(order);
        return OrderConverter.toOrderResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);
        return OrderConverter.toOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return OrderConverter.toOrderResponseList(orderRepository.findAll());
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}