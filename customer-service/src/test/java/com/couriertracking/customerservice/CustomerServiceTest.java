package com.couriertracking.customerservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.couriertracking.customermodel.request.CustomerRequest;
import com.couriertracking.customermodel.response.CustomerResponse;
import com.couriertracking.customerservice.advice.exception.CustomerNotFoundException;
import com.couriertracking.customerservice.converter.CustomerConverter;
import com.couriertracking.customerservice.persistance.entity.CustomerEntity;
import com.couriertracking.customerservice.persistance.repository.CustomerRepository;
import com.couriertracking.customerservice.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CustomerRequest request = new CustomerRequest("John", "Doe", 40.748817, -73.985428);
        CustomerEntity entity = CustomerConverter.toCustomerEntity(request);

        customerService.createCustomer(request);

        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testUpdateCustomer() {
        Long id = 1L;
        CustomerRequest request = new CustomerRequest("John", "Doe", 40.748817, -73.985428);
        CustomerEntity entity = new CustomerEntity();
        entity.setId(id);
        entity.setFirstName("Jane");
        entity.setLastName("Smith");

        when(customerRepository.findById(id)).thenReturn(Optional.of(entity));
        when(customerRepository.save(any(CustomerEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponse response = customerService.updateCustomer(id, request);

        assertNotNull(response);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        Long id = 1L;
        CustomerRequest request = new CustomerRequest("John", "Doe", 40.748817, -73.985428);

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.updateCustomer(id, request);
        });

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void testGetCustomerById() {
        Long id = 1L;
        CustomerEntity entity = new CustomerEntity();
        entity.setId(id);
        entity.setFirstName("John");
        entity.setLastName("Doe");

        when(customerRepository.findById(id)).thenReturn(Optional.of(entity));

        CustomerResponse response = customerService.getCustomerById(id);

        assertNotNull(response);
        assertEquals(entity.getFirstName(), response.getFirstName());
        assertEquals(entity.getLastName(), response.getLastName());
        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void testGetCustomerById_NotFound() {
        Long id = 1L;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> {
            customerService.getCustomerById(id);
        });

        verify(customerRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllCustomers() {
        CustomerEntity entity1 = new CustomerEntity();
        entity1.setId(1L);
        entity1.setFirstName("John");
        entity1.setLastName("Doe");

        CustomerEntity entity2 = new CustomerEntity();
        entity2.setId(2L);
        entity2.setFirstName("Jane");
        entity2.setLastName("Smith");

        when(customerRepository.findAll()).thenReturn(List.of(entity1, entity2));

        List<CustomerResponse> responses = customerService.getAllCustomers();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testDeleteCustomer() {
        Long id = 1L;

        customerService.deleteCustomer(id);

        verify(customerRepository, times(1)).deleteById(id);
    }
}
