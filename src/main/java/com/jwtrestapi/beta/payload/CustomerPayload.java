package com.jwtrestapi.beta.payload;

import com.jwtrestapi.beta.model.CustomerProfileDet;
import lombok.Data;

@Data
public class CustomerPayload {

    private Long customerId;

    private String nama;

    private String email;

    private CustomerProfileDet customerProfileDet;


    public CustomerPayload(Long customerId, String nama, String email, CustomerProfileDet customerProfileDet) {
        this.customerId = customerId;
        this.nama = nama;
        this.email = email;
        this.customerProfileDet = customerProfileDet;
    }
}
