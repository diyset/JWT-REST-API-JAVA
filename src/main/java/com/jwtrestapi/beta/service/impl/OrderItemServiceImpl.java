package com.jwtrestapi.beta.service.impl;

import com.jwtrestapi.beta.model.Customer;
import com.jwtrestapi.beta.model.OrderItem;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.OrderItemRequest;
import com.jwtrestapi.beta.repository.CustomerRepository;
import com.jwtrestapi.beta.repository.OrderItemRepository;
import com.jwtrestapi.beta.service.OrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {


    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemServiceImpl.class);
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Override
    public ResponseService crateOrder(OrderItemRequest orderItemRequest) {
        Customer customer = customerRepository.findById(orderItemRequest.getIdCustomer()).orElse(new Customer());
        if (customer.getId() == null) {
            LOGGER.error("Data Not Found : id `{}`", orderItemRequest.getIdCustomer());
            return new ResponseService(false, "Data Not Found : id " + orderItemRequest.getIdCustomer());
        }
        OrderItem orderItem = new OrderItem(orderItemRequest.getItemName(),orderItemRequest.getQuantity(),customer);
        orderItemRepository.save(orderItem);
        LOGGER.info("Success Save : {}", orderItem);
        return new ResponseService(true, orderItem.toString());
    }
}
