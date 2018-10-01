package com.jwtrestapi.beta.payload.response;

import com.jwtrestapi.beta.payload.CustomerPayload;
import lombok.Data;

@Data
public class CustomerResponseDataOneWithPayload {

    private boolean success;

    private CustomerPayload data;

    public CustomerResponseDataOneWithPayload(boolean success, CustomerPayload data) {
        this.success = success;
        this.data = data;
    }
}
