package com.jwtrestapi.beta.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TblOwnerCarDet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nik;
    private String noHp;
    private String pekerjaan;
    private String alamat;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_car_id")
    private TblOwnerCar tblOwnerCar;

    public TblOwnerCarDet() {
    }

    public TblOwnerCarDet(String nik, String noHp, String pekerjaan, String alamat) {
        this.nik = nik;
        this.noHp = noHp;
        this.pekerjaan = pekerjaan;
        this.alamat = alamat;
    }

    public TblOwnerCarDet(String nik, String noHp, String pekerjaan, String alamat, TblOwnerCar tblOwnerCar) {
        this.nik = nik;
        this.noHp = noHp;
        this.pekerjaan = pekerjaan;
        this.alamat = alamat;
        this.tblOwnerCar = tblOwnerCar;
    }

    @Override
    public String toString() {
        return "TblOwnerCarDet{" +
                "id:" + id +
                ", nik:" + nik + '\'' +
                ", noHp:'" + noHp + '\'' +
                ", pekerjaan:'" + pekerjaan + '\'' +
                ", alamat:'" + alamat + '\'' +
                '}';
    }
}
