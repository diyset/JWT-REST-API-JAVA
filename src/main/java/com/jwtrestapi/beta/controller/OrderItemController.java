package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.OrderItemRequest;
import com.jwtrestapi.beta.payload.response.ResponseDataApi;
import com.jwtrestapi.beta.service.OrderItemService;
import com.jwtrestapi.beta.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orderitem")
public class OrderItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemController.class);

    @Autowired
    OrderItemService orderItemService;


    @PostMapping("")
    public ResponseEntity<?> createOrderItem(@RequestBody OrderItemRequest orderItemRequest, @RequestHeader("Authorization") String token){
        try {
            ResponseService responseService = orderItemService.crateOrder(orderItemRequest);
            if(responseService.getSuccess()){
                LOGGER.info("Data : {}",orderItemRequest);
                return ResponseEntity.ok(new ResponseDataApi(Constant.RESPONSE_SERVICE.SUCCESS.getDescription(),true, orderItemRequest));
            }
            LOGGER.info("Data : {}",responseService);
            return new ResponseEntity(new ResponseDataApi(responseService.getData(),false,orderItemRequest),HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.error("Error : {}",e.getMessage());
            return new ResponseEntity(new ResponseDataApi(e.getMessage(),false,null),HttpStatus.BAD_REQUEST);
        }
    }

}
