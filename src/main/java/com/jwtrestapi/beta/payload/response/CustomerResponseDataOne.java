package com.jwtrestapi.beta.payload.response;

import com.jwtrestapi.beta.model.Customer;

public class CustomerResponseDataOne {

    private boolean success;

    private Customer data;

    public CustomerResponseDataOne() {
    }


    public CustomerResponseDataOne(boolean success, Customer data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Customer getData() {
        return data;
    }

    public void setData(Customer data) {
        this.data = data;
    }
}
