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
import com.jwtrestapi.beta.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private Class classNameMethod = new Object(){}.getClass();
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImpl.class);
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
    public List<CustomerPayload> getAllListCustomer() {
        Class nameMethod = new Object(){}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_SERVICE.getDescription(nameMethod));
        List<Customer> customers = customerRepository.findAll();
        LOGGER.info("Data : {}",customers);
        List<CustomerPayload> customerPayloads = new ArrayList<>();
        for (Customer customer:customers) {
            customerPayloads.add(new CustomerPayload(customer.getId(),customer.getNama(),customer.getEmail(),customer.getCustomerProfileDet()));
        }
        LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(nameMethod));
        return customerPayloads;
    }

    @Override
    public CustomerPayload getOneCustomer(Long customerId) {
        Class nameMethod = new Object(){}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_SERVICE.getDescription(nameMethod));
        LOGGER.info("Customer Id : `{}`",customerId);
        /*
        Yang lama belom dipake lagi

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            LOGGER.error("Error : {}", new ResourceNotFoundException("Customer", "customerId", customerId));
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(nameMethod));
            return new ResourceNotFoundException("Customer", "customerId", customerId);
        });
          */
        Customer customer = customerRepository.findById(customerId).orElse(new Customer());
        if(null==customer){
            LOGGER.error("Error : {}", new ResourceNotFoundException("Customer", "customerId", customerId));
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(nameMethod));
            return new CustomerPayload();
        }
        CustomerPayload customerPayload = new CustomerPayload();
        customerPayload.setNama(customer.getNama());
        customerPayload.setEmail(customer.getEmail());
        customerPayload.setCustomerProfileDet(customer.getCustomerProfileDet());
        customerPayload.setCustomerId(customer.getId());

        LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(nameMethod));
        return customerPayload;
    }

    @Override
    public ResponseService createCustomer(CustomerRequest customerRequest) {
        Class className = new Object(){}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_SERVICE.getDescription(className));
        JenisKelaminEnum gender;
        Customer customer = new Customer();
        CustomerProfileDet customerProfileDet = new CustomerProfileDet();

//        if (!datePattern.matches(customerRequest.getTanggalLahir())) {
        /*
         * Validasi Create Customer Service
         * ---------------------------------
         */
        if (!Constant.isDatePattern(customerRequest.getTanggalLahir())) {
            LOGGER.error(Constant.MESSAGE_VALIDATE.DATE_FORMAT.getDescription(customerRequest.getTanggalLahir()));
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(className));
            return new ResponseService(false, Constant.MESSAGE_VALIDATE.DATE_FORMAT.getDescription(customerRequest.getTanggalLahir()));
        }
        if (customerRepository.existsCustomerByEmail(customerRequest.getEmail())) {
            LOGGER.error("Existing Customer By Email");
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(className));
            return new ResponseService(false, "Exist Email Customer!");
        }
        if (customerProfileDetRepository.existsCustomerProfileDetByNoHp(customerRequest.getNoHp())) {
            LOGGER.error("Existing Customer By No Hp");
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(className));
            return new ResponseService(false, "Exist No Hp Customer!");
        }
        if (customerProfileDetRepository.existsCustomerProfileDetByNoKtp(customerRequest.getNoKtp())) {
            LOGGER.error("Existing Customer By No Ktp");
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(className));
            return new ResponseService(false, "Exist No Ktp Customer!");
        }
        /*
            ------------------------
         */

        /*
        Persist DB
         */
        customer.setEmail(customerRequest.getEmail());
        customer.setNama(customerRequest.getNama());
        customerProfileDet.setNoKtp(customerRequest.getNoKtp());
        if (customerRequest.getJenisKelamin().equalsIgnoreCase(Constant.GENDER.LAKI_LAKI.getCode())) {
            gender = JenisKelaminEnum.LAKI_LAKI;
        } else {
            gender = JenisKelaminEnum.PEREMPUAN;
        }
        customerProfileDet.setJenisKelamin(gender);
        customerProfileDet.setNegara(customerRequest.getNegara());
        try {
            customerProfileDet.setTanggalLahir(Constant.dateFormat.parse(customerRequest.getTanggalLahir()));
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(className));
            return new ResponseService(false,e.getMessage());
        }
        customerProfileDet.setNoHp(customerRequest.getNoHp());
        customerProfileDet.setKota(customerRequest.getKota());
        customerProfileDet.setKodePos(customerRequest.getKodePos());
        customerProfileDet.setAlamat(customerRequest.getAlamat());
        customerProfileDet.setCustomer(customer);
        customer.setCustomerProfileDet(customerProfileDet);
        customerRepository.save(customer);
//        customerProfileDetRepository.save(customerProfileDet);
        LOGGER.info("Data Request : " + customerRequest);
        LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(className));
        return new ResponseService(true, customerRequest.toString());
    }

    @Override
    public ResponseService deleteCustomer(Long customerId) {
        LOGGER.info(Constant.INFO_SERVICE.START_SERVICE.getDescription(this.getClass()));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            LOGGER.error("Error : {}", new ResourceNotFoundException("Customer", "customerId", customerId));
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(this.getClass()));
            return new ResourceNotFoundException("Customer", "customerId", customerId);
        });
        customerRepository.delete(customer);
        LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(this.getClass()));
        return new ResponseService(true, customer.toString());
    }

    @Override
    public ResponseService updateCustomer(Long customerId, CustomerRequest customerRequest) {
        LOGGER.info(Constant.INFO_SERVICE.START_SERVICE.getDescription(classNameMethod));
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            LOGGER.error("Error : {}", new ResourceNotFoundException("Customer", "customerId", customerId));
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(classNameMethod));
            return new ResourceNotFoundException("Customer", "customerId", customerId);
        });
        if (!Constant.isDatePattern(customerRequest.getTanggalLahir())) {
            return new ResponseService(false, "Invalid Date Format (dd-MM-yyyy)" + customerRequest.getTanggalLahir());
        }
        CustomerProfileDet customerProfileDet = customer.getCustomerProfileDet();
        String temptJenisKelaminRequest = customerRequest.getJenisKelamin() != null ? customerRequest.getJenisKelamin()
                : customerProfileDet.getJenisKelamin().toString();
        if (temptJenisKelaminRequest.equalsIgnoreCase("L") || temptJenisKelaminRequest.equals(JenisKelaminEnum.LAKI_LAKI)) {
            customerProfileDet.setJenisKelamin(JenisKelaminEnum.LAKI_LAKI);
        } else {
            customerProfileDet.setJenisKelamin(JenisKelaminEnum.PEREMPUAN);
        }
        customerProfileDet.setAlamat(customerRequest.getAlamat() != null ? customerRequest.getAlamat() : customer.getCustomerProfileDet().getAlamat());
        customer.setNama(customerRequest.getNama());
        customer.setEmail(customerRequest.getEmail());
        customerProfileDet.setKodePos(customerRequest.getKota() != null ? customerRequest.getKota() : customer.getCustomerProfileDet().getKota());
        customerProfileDet.setNegara(customerRequest.getNegara() != null ? customerRequest.getNegara() : customer.getCustomerProfileDet().getNegara());
        customerProfileDet.setNoKtp(customerRequest.getNoKtp() != null ? customerRequest.getNoKtp() : customer.getCustomerProfileDet().getNoKtp());
        Date tempDate;
        try {
            tempDate = customerRequest.getTanggalLahir() != null ? Constant.dateFormat.parse(customerRequest.getTanggalLahir()) : customerProfileDet.getTanggalLahir();
            customerProfileDet.setTanggalLahir(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
            LOGGER.info("Error : {}",e.getMessage());
            LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(classNameMethod));
        }
        customerProfileDet.setCustomer(customer);
        customerRepository.save(customer);
        LOGGER.info(Constant.INFO_SERVICE.END_SERVICE.getDescription(classNameMethod));
        return new ResponseService(true, customer.toString());
    }

}
