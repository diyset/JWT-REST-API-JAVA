package com.jwtrestapi.beta.service.impl;

import com.jwtrestapi.beta.model.TblOwnerCar;
import com.jwtrestapi.beta.repository.TblOwnerCarRepository;
import com.jwtrestapi.beta.service.CarOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarOwnerServiceImpl implements CarOwnerService {

    @Autowired
    private TblOwnerCarRepository tblOwnerCarRepository;

    @Override
    public List findAll() {

        List<TblOwnerCar> tblOwnerCar = (List<TblOwnerCar>) tblOwnerCarRepository.findAll();

        return (List) tblOwnerCarRepository.findAll();
    }
}
