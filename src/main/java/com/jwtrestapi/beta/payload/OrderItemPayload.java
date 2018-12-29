package com.jwtrestapi.beta.payload;

import com.jwtrestapi.beta.model.Customer;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemPayload implements Serializable {

    private String itemName;

    private int quantity;

    private Customer customer;

    public OrderItemPayload() {
    }

    public OrderItemPayload(String itemName, int quantity, Customer customer) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "OrderItemPayload{" +
                "itemName:'" + itemName + '\'' +
                ", quantity:" + quantity +
                ", customer:" + customer +
                '}';
    }
}
