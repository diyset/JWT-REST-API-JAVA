package com.jwtrestapi.beta.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class OrderItemRequest implements Serializable {

    @NotBlank
    @Size(min = 5, max = 255)
    private String itemName;
    @Size(min = 1)
    @NotBlank
    private int quantity;
    @NotBlank
    private Long idCustomer;

    public OrderItemRequest() {
    }

    public OrderItemRequest(@NotBlank @Size(min = 5, max = 255) String itemName, @Size(min = 1) @NotBlank int quantity, @NotBlank Long idCustomer) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.idCustomer = idCustomer;
    }

    @Override
    public String toString() {
        return "OrderItemRequest{" +
                "itemName:'" + itemName + '\'' +
                ", quantity:" + quantity +
                ", idCustomer:" + idCustomer +
                '}';
    }
}
