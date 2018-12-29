package com.jwtrestapi.beta.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jwtrestapi.beta.model.audit.DateAudit;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_item")
@Data
public class OrderItem extends DateAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String itemName;
    private int quantity;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

    public OrderItem() {
    }

    public OrderItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public OrderItem(String itemName, int quantity, Customer customer) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id:" + id +
                ", itemName:'" + itemName + '\'' +
                ", quantity:" + quantity +
                '}';
    }
}
