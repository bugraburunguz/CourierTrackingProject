package com.couriertracking.customerservice.service;

import com.couriertracking.customermodel.request.CustomerRequest;
import com.couriertracking.customermodel.response.CustomerResponse;
import com.couriertracking.customerservice.advice.exception.CustomerNotFoundException;
import com.couriertracking.customerservice.converter.CustomerConverter;
import com.couriertracking.customerservice.persistance.entity.CustomerEntity;
import com.couriertracking.customerservice.persistance.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public void createCustomer(CustomerRequest request) {
        CustomerEntity entity = CustomerConverter.toCustomerEntity(request);
        customerRepository.save(entity);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setLatitude(request.getLatitude());
        entity.setLongitude(request.getLongitude());
        CustomerEntity updatedEntity = customerRepository.save(entity);
        return CustomerConverter.toCustomerResponse(updatedEntity);
    }

    public CustomerResponse getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(CustomerNotFoundException::new);
        return CustomerConverter.toCustomerResponse(entity);
    }

    public List<CustomerResponse> getAllCustomers() {
        return CustomerConverter.toCustomerResponseList(customerRepository.findAll());
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}