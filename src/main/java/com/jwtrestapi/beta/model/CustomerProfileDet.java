package com.jwtrestapi.beta.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jwtrestapi.beta.model.Enum.JenisKelaminEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "customer_profile_det")
public class CustomerProfileDet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 200)
    private String alamat;

    @NotBlank
    private String kota;
    @NotBlank
    private String negara;

    @Temporal(TemporalType.DATE)
    private Date tanggalLahir;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    @NotNull
    private JenisKelaminEnum jenisKelamin;

    @Size(min = 12, max = 20)
    @Column(unique = true)
    private String noHp;

    @Size(min = 6, max = 12)
    private String kodePos;

    @Column(unique = true)
    @Size(min = 16, max = 16)
    private String noKtp;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;



    public CustomerProfileDet(@NotBlank @Size(min = 5, max = 200) String alamat, @NotBlank String kota, @NotBlank String negara, Date tanggalLahir, @NotBlank JenisKelaminEnum jenisKelamin, @Size(min = 12, max = 20) String noHp, @Size(min = 6, max = 12) String kodePos, @Size(min = 16, max = 16) String noKtp, Customer customer) {
        this.alamat = alamat;
        this.kota = kota;
        this.negara = negara;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.noHp = noHp;
        this.kodePos = kodePos;
        this.noKtp = noKtp;
        this.customer = customer;
    }
}
