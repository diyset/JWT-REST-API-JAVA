package com.jwtrestapi.beta.repository;

import com.jwtrestapi.beta.model.CustomerProfileDet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProfileDetRepository extends JpaRepository<CustomerProfileDet, Long> {

    Boolean existsCustomerProfileDetByNoKtp(String noKtp);
}
