package com.jwtrestapi.beta.payload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwtrestapi.beta.model.CustomerProfileDet;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerPayload implements Serializable {

    private Long customerId;

    private String nama;

    private String email;
//    @JsonIgnore
    private CustomerProfileDet customerProfileDet;

    public CustomerPayload() {
    }

    public CustomerPayload(Long customerId, String nama, String email, CustomerProfileDet customerProfileDet) {
        this.customerId = customerId;
        this.nama = nama;
        this.email = email;
        this.customerProfileDet = customerProfileDet;
    }

    public CustomerPayload(CustomerPayload customerPayload) {
    }
}
