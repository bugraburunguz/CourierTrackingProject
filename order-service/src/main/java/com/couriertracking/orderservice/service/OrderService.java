package com.couriertracking.orderservice.service;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.evaluationservice.factory.HaversineFactory;
import com.couriertracking.evaluationservice.model.Haversine;
import com.couriertracking.ordermodel.enums.OrderStatus;
import com.couriertracking.ordermodel.request.OrderRequest;
import com.couriertracking.ordermodel.response.OrderResponse;
import com.couriertracking.orderservice.advice.exception.*;
import com.couriertracking.orderservice.client.CourierServiceClient;
import com.couriertracking.orderservice.client.StoreServiceClient;
import com.couriertracking.orderservice.converter.OrderConverter;
import com.couriertracking.orderservice.persistance.entity.*;
import com.couriertracking.orderservice.persistance.repository.CustomerRepository;
import com.couriertracking.orderservice.persistance.repository.OrderRepository;
import com.couriertracking.storemodel.resonse.StoreResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StoreServiceClient storeServiceClient;
    private final CourierServiceClient courierServiceClient;
    private final Haversine haversine = HaversineFactory.createHaversine();

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);

        StoreResponse nearestStore = storeServiceClient.findNearestStore(customer.getLatitude(), customer.getLongitude());
        CourierLocationResponse nearestAvailableCourier = courierServiceClient.findNearestCourier(nearestStore.getLat(), nearestStore.getLng());

        OrderEntity order = OrderConverter.toOrderEntity(customer, nearestAvailableCourier.getId(), nearestStore.getId());
        order.setStatus(OrderStatus.PENDING);
        OrderEntity savedOrder = orderRepository.save(order);

        courierServiceClient.updateStatus(nearestAvailableCourier.getId(), CourierStatus.BUSY);

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

    @Transactional
    public void deliverOrder(Long orderId, Long courierId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        Long courier = order.getCourier().getId();
        if (courier == null) {
            throw new OrderCourierNotFound();
        }

        if (!Objects.equals(courier, courierId)) {
            throw new CourierNotFoundException();
        }

        CourierLocationResponse courierLocation = courierServiceClient.getCourierLocationById(courier);

        double distanceToCustomer = haversine.calculateDistance(
                courierLocation.getLatitude(), courierLocation.getLongitude(),
                order.getCustomer().getLatitude(), order.getCustomer().getLongitude()
        );

        if (distanceToCustomer > 0.005) {
            throw new CourierNotCloseLocation();
        }
        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);
        courierServiceClient.updateStatus(courier, CourierStatus.AVAILABLE);
        log.info("Order delivered. order id: {} courier id: {}", orderId, courier);
    }
}