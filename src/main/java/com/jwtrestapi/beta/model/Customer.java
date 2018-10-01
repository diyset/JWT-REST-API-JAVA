package com.jwtrestapi.beta.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jwtrestapi.beta.model.audit.DateAudit;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@Table(name = "customers")
public class Customer extends DateAudit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Customer() {
    }


}
