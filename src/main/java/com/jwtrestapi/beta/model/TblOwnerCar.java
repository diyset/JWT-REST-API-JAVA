package com.jwtrestapi.beta.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_owner_car")
@Data
public class TblOwnerCar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ownerId;

    private String firstname, lastname;

    private String email;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblOwnerCar", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<TblCar> cars = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tblOwnerCar", fetch = FetchType.LAZY)
    @JsonIgnore
    private TblOwnerCarDet ownerCarDet;

    public TblOwnerCar() {
    }

    public TblOwnerCar(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Set<TblCar> getCars() {
        return cars;
    }

    public void setCars(Set<TblCar> cars) {
        this.cars = cars;
    }

    public String toRetriveAll() {
        return "TblOwnerCar{" +
                "ownerId=" + ownerId +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", cars='" + cars + '\'' +
                ", ownerCarDet=" + ownerCarDet +
                '}';
    }

    @Override
    public String toString() {
        return "{TblOwnerCar :{" +
                "ownerId:" + ownerId +
                ", firstname:'" + firstname + '\'' +
                ", lastname:'" + lastname + '\'' +
                ", email:'" + email + '\'' +
                ", tblCar:" + cars.toString() +
                "," + ownerCarDet.toString() +
                '}' +
                '}';
    }
}
