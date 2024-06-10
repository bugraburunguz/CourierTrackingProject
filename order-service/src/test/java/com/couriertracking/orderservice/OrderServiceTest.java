package com.couriertracking.orderservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.couriertracking.couriermodel.enums.CourierStatus;
import com.couriertracking.couriermodel.response.CourierLocationResponse;
import com.couriertracking.ordermodel.enums.OrderStatus;
import com.couriertracking.ordermodel.request.OrderRequest;
import com.couriertracking.ordermodel.response.OrderResponse;
import com.couriertracking.orderservice.advice.exception.*;
import com.couriertracking.orderservice.client.CourierServiceClient;
import com.couriertracking.orderservice.client.StoreServiceClient;
import com.couriertracking.orderservice.persistance.entity.*;
import com.couriertracking.orderservice.persistance.repository.CustomerRepository;
import com.couriertracking.orderservice.persistance.repository.OrderRepository;
import com.couriertracking.orderservice.service.OrderService;
import com.couriertracking.storemodel.resonse.StoreResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StoreServiceClient storeServiceClient;

    @Mock
    private CourierServiceClient courierServiceClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        OrderRequest request = new OrderRequest(1L);
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setLatitude(40.748817);
        customer.setLongitude(-73.985428);

        StoreResponse storeResponse = new StoreResponse();
        storeResponse.setId(1L);
        storeResponse.setLat(40.748817);
        storeResponse.setLng(-73.985428);

        CourierLocationResponse courierLocationResponse = new CourierLocationResponse();
        courierLocationResponse.setId(1L);
        courierLocationResponse.setLatitude(40.748817);
        courierLocationResponse.setLongitude(-73.985428);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setCustomer(customer);
        orderEntity.setCourier(new CourierEntity());
        orderEntity.setStore(new StoreEntity());
        orderEntity.setStatus(OrderStatus.PENDING);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(storeServiceClient.findNearestStore(anyDouble(), anyDouble())).thenReturn(storeResponse);
        when(courierServiceClient.findNearestCourier(anyDouble(), anyDouble())).thenReturn(courierLocationResponse);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        OrderResponse orderResponse = orderService.createOrder(request);

        assertNotNull(orderResponse);
        assertEquals(OrderStatus.PENDING.getStatus(), orderResponse.getOrderStatus());
        verify(customerRepository, times(1)).findById(1L);
        verify(storeServiceClient, times(1)).findNearestStore(anyDouble(), anyDouble());
        verify(courierServiceClient, times(1)).findNearestCourier(anyDouble(), anyDouble());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(courierServiceClient, times(1)).updateStatus(courierLocationResponse.getId(), CourierStatus.BUSY);
    }

    @Test
    void testCreateOrder_CustomerNotFound() {
        OrderRequest request = new OrderRequest(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> orderService.createOrder(request));

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderById() {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        CourierEntity courier = new CourierEntity();
        courier.setId(1L);

        StoreEntity store = new StoreEntity();
        store.setId(1L);

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setCustomer(customer);
        orderEntity.setCourier(courier);
        orderEntity.setStore(store);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(orderEntity));

        OrderResponse response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(orderEntity.getId(), response.getId());
        verify(orderRepository, times(1)).findById(1L);
    }


    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllOrders() {
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(1L);

        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(2L);

        CourierEntity courier1 = new CourierEntity();
        courier1.setId(1L);

        CourierEntity courier2 = new CourierEntity();
        courier2.setId(2L);

        StoreEntity store1 = new StoreEntity();
        store1.setId(1L);

        StoreEntity store2 = new StoreEntity();
        store2.setId(2L);

        OrderEntity order1 = new OrderEntity();
        order1.setId(1L);
        order1.setStatus(OrderStatus.PENDING);
        order1.setCustomer(customer1);
        order1.setCourier(courier1);
        order1.setStore(store1);

        OrderEntity order2 = new OrderEntity();
        order2.setId(2L);
        order2.setStatus(OrderStatus.DELIVERED);
        order2.setCustomer(customer2);
        order2.setCourier(courier2);
        order2.setStore(store2);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<OrderResponse> responses = orderService.getAllOrders();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testDeleteOrder() {
        Long orderId = 1L;

        orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
