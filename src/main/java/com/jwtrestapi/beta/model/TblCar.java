package com.jwtrestapi.beta.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tbl_car")
@Data
public class TblCar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String brand, model, color, registerNumber;

    private int year;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_car_id")
    @JsonBackReference
    private TblOwnerCar tblOwnerCar;

    public TblCar() {
    }

    public TblCar(String brand, String model, String color, String registerNumber, int year, BigDecimal price, TblOwnerCar ownerCar) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.registerNumber = registerNumber;
        this.year = year;
        this.price = price;
        this.tblOwnerCar = ownerCar;
    }

    public TblOwnerCar getOwnerCar() {
        return tblOwnerCar;
    }

    public void setOwnerCar(TblOwnerCar tblOwnerCar) {
        this.tblOwnerCar = tblOwnerCar;
    }

    public String toRetrive(){
        return "TblCar{" +
                "id :" +id +
                ", brand='"+brand+'\''+
                ", model='"+model+'\''+
                ", color='"+color+'\''+
                ", registerNumber='"+registerNumber+'\''+
                ", year='"+year+'\''+
                '}';
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", brand:'" + brand + '\'' +
                ", model:'" + model + '\'' +
                ", color:'" + color + '\'' +
                ", registerNumber:'" + registerNumber + '\'' +
                ", year:" + year +
                ", price:" + price +
                '}';
    }


}
