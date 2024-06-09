package com.couriertracking.orderservice.service;

import com.couriertracking.couriermodel.response.CourierResponse;
import com.couriertracking.ordermodel.request.OrderRequest;
import com.couriertracking.ordermodel.response.OrderResponse;
import com.couriertracking.orderservice.advice.exception.CourierNotFoundException;
import com.couriertracking.orderservice.advice.exception.CustomerNotFoundException;
import com.couriertracking.orderservice.advice.exception.OrderNotFoundException;
import com.couriertracking.orderservice.client.CourierClient;
import com.couriertracking.orderservice.client.StoreClient;
import com.couriertracking.orderservice.converter.OrderConverter;
import com.couriertracking.orderservice.persistance.entity.CourierEntity;
import com.couriertracking.orderservice.persistance.entity.CustomerEntity;
import com.couriertracking.orderservice.persistance.entity.OrderEntity;
import com.couriertracking.orderservice.persistance.entity.StoreEntity;
import com.couriertracking.orderservice.persistance.repository.CourierRepository;
import com.couriertracking.orderservice.persistance.repository.CustomerRepository;
import com.couriertracking.orderservice.persistance.repository.OrderRepository;
import com.couriertracking.storemodel.resonse.StoreResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StoreClient storeClient;
    private final CourierClient courierClient;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);

        StoreResponse nearestStore = storeClient.getNearestStore(customer.getLatitude(), customer.getLongitude());
        CourierResponse nearestAvailableCourier = courierClient.getNearestAvailableCourier(nearestStore.getLat(), nearestStore.getLng());

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setId(nearestStore.getId());
        CourierEntity courierEntity = new CourierEntity();
        courierEntity.setId(nearestAvailableCourier.getId());

        OrderEntity order = OrderConverter.toOrderEntity(customer, courierEntity, storeEntity);
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