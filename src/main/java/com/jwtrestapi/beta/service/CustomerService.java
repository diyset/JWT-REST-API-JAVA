package com.jwtrestapi.beta.service;

import com.jwtrestapi.beta.model.Customer;
import com.jwtrestapi.beta.payload.CustomerPayload;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.CustomerRequest;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllList();

    List<CustomerPayload> getAllListCustomer();

    CustomerPayload getOneCustomer(Long customerId);

    ResponseService createCustomer(CustomerRequest customerRequest);

    ResponseService deleteCustomer(Long customerId);

    ResponseService updateCustomer(Long customerId, CustomerRequest customerRequest);
}
