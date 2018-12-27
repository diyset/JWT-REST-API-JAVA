package com.jwtrestapi.beta.repository;

import com.jwtrestapi.beta.model.TblCar;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TblCarRepository extends CrudRepository<TblCar, Long> {

    List<TblCar> findByBrand(String brand);

    List<TblCar> findByColor(String color);

    List<TblCar> findByYear(int year);

    List<TblCar> findByBrandOrColor(String brand, String color);

    List<TblCar> findByModelOrColor(String model,String collor);
}
