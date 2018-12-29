package com.jwtrestapi.beta.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jwtrestapi.beta.model.audit.DateAudit;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Data
@Table(name = "customers")
public class Customer extends DateAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 60)
    private String nama;


    @Email(message = "Email Format")
    @Column(unique = true)
    private String email;
    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            mappedBy = "customer")
    @JsonManagedReference
    private CustomerProfileDet customerProfileDet;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer")
    @JsonManagedReference
    private OrderItem orderItem;
    public Customer() {
    }

    public Customer(@NotBlank @Size(min = 4, max = 60) String nama, @Email(message = "Email Format") String email, CustomerProfileDet customerProfileDet) {
        this.nama = nama;
        this.email = email;
        this.customerProfileDet = customerProfileDet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerProfileDet getCustomerProfileDet() {
        return customerProfileDet;
    }

    public void setCustomerProfileDet(CustomerProfileDet customerProfileDet) {
        this.customerProfileDet = customerProfileDet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);

    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", nama:'" + nama + '\'' +
                ", email:'" + email + '\'' +
                ","+((customerProfileDet == null) ? null: customerProfileDet.toString()) +
                "}";
    }
}
