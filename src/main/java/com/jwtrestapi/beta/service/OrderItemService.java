package com.jwtrestapi.beta.service;

import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.OrderItemRequest;

public interface OrderItemService {

    ResponseService crateOrder(OrderItemRequest orderItemRequest);
}
