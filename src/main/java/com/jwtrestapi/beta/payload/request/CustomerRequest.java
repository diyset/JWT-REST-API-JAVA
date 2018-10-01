package com.jwtrestapi.beta.payload.request;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CustomerRequest {

    @NotBlank(message = "Nama Is Not Null")
    @Size(min = 4, max = 60)
    private String nama;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 200)
    private String alamat;

    @NotBlank
    private String kota;
    @NotBlank
    private String negara;
    @Temporal(TemporalType.DATE)
    private String tanggalLahir;

    @NotBlank
    private String jenisKelamin;

    @Size(min = 12, max = 20, message = "No Hp Validate")
    private String noHp;
    @Size(min = 6, max = 12, message = "Kode Pos Validate")
    private String kodePos;

    @Size(min = 16, max = 16, message = "noKtp Validate")
    private String noKtp;

    public CustomerRequest() {
    }


}
