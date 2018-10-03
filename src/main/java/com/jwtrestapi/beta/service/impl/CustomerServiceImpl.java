package com.jwtrestapi.beta.service.impl;

import com.google.gson.Gson;
import com.jwtrestapi.beta.exception.ResourceNotFoundException;
import com.jwtrestapi.beta.model.Customer;
import com.jwtrestapi.beta.model.CustomerProfileDet;
import com.jwtrestapi.beta.model.Enum.JenisKelaminEnum;
import com.jwtrestapi.beta.payload.CustomerPayload;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.CustomerRequest;
import com.jwtrestapi.beta.repository.CustomerProfileDetRepository;
import com.jwtrestapi.beta.repository.CustomerRepository;
import com.jwtrestapi.beta.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
    Gson gson = new Gson();
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerProfileDetRepository customerProfileDetRepository;

    @Override
    public List<Customer> getAllList() {
        LOGGER.info("****Start Service GetAllList");
        List<Customer> customers = customerRepository.findAll();
        LOGGER.info("****End Service GetAllList");
        return customers;
    }

    @Override
    public CustomerPayload getOneCustomer(Long customerId) {
        LOGGER.info("****Start Service GetOneCustomer : " + customerId);
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            LOGGER.error("Error : " + new ResourceNotFoundException("Customer", "customer", customerId));
            LOGGER.info("****End Service GetOneCustomer");
            return new ResourceNotFoundException("Customer", "customer", customerId);
        });
        CustomerPayload customerPayload = new CustomerPayload();
        customerPayload.setNama(customer.getNama());
        customerPayload.setEmail(customer.getEmail());
        customerPayload.setCustomerProfileDet(customer.getCustomerProfileDet());
        customerPayload.setCustomerId(customer.getId());
        LOGGER.info("****End Service GetOneCustomer");
        return customerPayload;
    }

    @Override
    public ResponseService createCustomer(CustomerRequest customerRequest) {
        LOGGER.info("****Start Service CreateCustomer");
        JenisKelaminEnum gender;
        Customer customer = new Customer();
        CustomerProfileDet customerProfileDet = new CustomerProfileDet();


        if (customerRepository.existsCustomerByEmail(customerRequest.getEmail())) {
            LOGGER.info("Existing Customer By Email");
            LOGGER.info("****End Service CreateCustomer");
            return new ResponseService(false, "Exist Email Customer!");
        }
        if (customerProfileDetRepository.existsCustomerProfileDetByNoHp(customerRequest.getNoHp())) {
            LOGGER.info("Existing Customer By No Hp");
            LOGGER.info("****End Service CreateCustomer");
            return new ResponseService(false, "Exist No Hp Customer!");
        }
        if (customerProfileDetRepository.existsCustomerProfileDetByNoKtp(customerRequest.getNoKtp())) {
            LOGGER.info("Existing Customer By No Ktp");
            LOGGER.info("****End Service CreateCustomer");
            return new ResponseService(false, "Exist No Ktp Customer!");
        }

        customer.setEmail(customerRequest.getEmail());
        customer.setNama(customerRequest.getNama());
        customerProfileDet.setNoKtp(customerRequest.getNoKtp());
        if (customerRequest.getJenisKelamin().equalsIgnoreCase("L")) {
            gender = JenisKelaminEnum.LAKI_LAKI;
        } else {
            gender = JenisKelaminEnum.PEREMPUAN;
        }
        customerProfileDet.setJenisKelamin(gender);
        customerProfileDet.setNegara(customerRequest.getNegara());
        try {
            customerProfileDet.setTanggalLahir(sdf.parse(customerRequest.getTanggalLahir()));
        } catch (ParseException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        customerProfileDet.setNoHp(customerRequest.getNoHp());
        customerProfileDet.setKota(customerRequest.getKota());
        customerProfileDet.setKodePos(customerRequest.getKodePos());
        customerProfileDet.setAlamat(customerRequest.getAlamat());
        customer.setCustomerProfileDet(customerProfileDet);
        customerProfileDet.setCustomer(customer);
        customerRepository.save(customer);
        LOGGER.info("Data Request : " + gson.toJson(customerRequest));
        LOGGER.info("****End Service Create Customer");
        return new ResponseService(true, "Successfully Create Customer!");
    }

    @Override
    public ResponseService deleteCustomer(Long customerId) {
        LOGGER.info("****Start Service Delete Customer");
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            LOGGER.error("Error : " + new ResourceNotFoundException("Customer", "customerId", customerId));
            LOGGER.info("****End Service Delete Customer");
            return new ResourceNotFoundException("Customer", "customerId", customerId);
        });
        customerRepository.delete(customer);
        LOGGER.info("****End Service Delete Customer Id : " + customerId);
        return new ResponseService(true, "Successfully Deleted Id " + customerId + " (" + customer.getNama() + ")");
    }

    @Override
    public ResponseService updateCustomer(Long customerId, CustomerRequest customerRequest) {
        return null;
    }


}
