package com.jwtrestapi.beta.payload.response;

import com.jwtrestapi.beta.model.Customer;
import lombok.Data;

import java.util.List;
@Data
public class CustomerResponseAllRetrive {

   private Boolean success;
   private List<Customer> data;

    public CustomerResponseAllRetrive() {
    }

    public CustomerResponseAllRetrive(Boolean success, List<Customer> data) {
        this.success = success;
        this.data = data;
    }
}
