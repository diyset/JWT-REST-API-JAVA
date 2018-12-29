package com.jwtrestapi.beta.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwtrestapi.beta.model.Enum.JenisKelaminEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "customer_profile_det")
public class CustomerProfileDet implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;


    public CustomerProfileDet() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public JenisKelaminEnum getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(JenisKelaminEnum jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getKodePos() {
        return kodePos;
    }

    public void setKodePos(String kodePos) {
        this.kodePos = kodePos;
    }

    public String getNoKtp() {
        return noKtp;
    }

    public void setNoKtp(String noKtp) {
        this.noKtp = noKtp;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "CustomerProfileDet{" +
                "id=" + id +
                ", alamat:'" + alamat + '\'' +
                ", kota:'" + kota + '\'' +
                ", negara:'" + negara + '\'' +
                ", tanggalLahir:" + tanggalLahir +
                ", jenisKelamin:" + jenisKelamin +
                ", noHp:'" + noHp + '\'' +
                ", kodePos:'" + kodePos + '\'' +
                ", noKtp:'" + noKtp + '\'' +
                '}';
    }
}
