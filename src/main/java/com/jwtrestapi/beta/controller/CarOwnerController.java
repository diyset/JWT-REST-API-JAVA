package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.model.TblOwnerCar;
import com.jwtrestapi.beta.payload.response.ResponseDataApi;
import com.jwtrestapi.beta.repository.TblCarRepository;
import com.jwtrestapi.beta.repository.TblOwnerCarDetRepository;
import com.jwtrestapi.beta.repository.TblOwnerCarRepository;
import com.jwtrestapi.beta.service.CarOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ownercar")
public class CarOwnerController {
    @Autowired
    TblCarRepository tblCarRepository;

    @Autowired
    CarOwnerService carOwnerService;

    @Autowired
    TblOwnerCarDetRepository tblOwnerCarDetRepository;

    @Autowired
    TblOwnerCarRepository tblOwnerCarRepository;


    @GetMapping("/allRetrive")
    public ResponseEntity<?> getAllRetriveOwner() {
        List<TblOwnerCar> tblOwnerCars = (List<TblOwnerCar>) tblOwnerCarRepository.findAll();


        return ResponseEntity.ok(new ResponseDataApi("Ok", true, tblOwnerCars));
    }
}
