package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.exception.ResourceNotFoundException;
import com.jwtrestapi.beta.model.Customer;
import com.jwtrestapi.beta.model.CustomerProfileDet;
import com.jwtrestapi.beta.model.Enum.JenisKelaminEnum;
import com.jwtrestapi.beta.payload.CustomerPayload;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.CustomerRequest;
import com.jwtrestapi.beta.payload.response.*;
import com.jwtrestapi.beta.repository.CustomerProfileDetRepository;
import com.jwtrestapi.beta.repository.CustomerRepository;
import com.jwtrestapi.beta.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/api/customer")
@RestController
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerProfileDetRepository customerProfileDetRepository;


    @PostMapping("/create")
    public ResponseEntity<?> createCustomerAndDetProfile(@Valid @RequestBody CustomerRequest customerRequest) throws ParseException {

        if (customerRepository.existsCustomerByEmail(customerRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email is Existing"), HttpStatus.BAD_REQUEST);
        }

        if (customerProfileDetRepository.existsCustomerProfileDetByNoKtp(customerRequest.getNoKtp())) {
            return new ResponseEntity(new ApiResponse(false, "No Ktp is Existing!"), HttpStatus.BAD_REQUEST);
        }
        JenisKelaminEnum gender;
        if (customerRequest.getJenisKelamin().equalsIgnoreCase("L")) {
            gender = JenisKelaminEnum.LAKI_LAKI;
        } else {
            gender = JenisKelaminEnum.PEREMPUAN;
        }

        Customer customer = new Customer();
        customer.setNama(customerRequest.getNama());
        customer.setEmail(customerRequest.getEmail());

        CustomerProfileDet customerProfileDet = new CustomerProfileDet(customerRequest.getAlamat(),
                customerRequest.getKota(), customerRequest.getNegara(), sdf.parse(customerRequest.getTanggalLahir()),
                gender, customerRequest.getNoHp(), customerRequest.getKodePos(), customerRequest.getNoKtp(), customer);

        customer.setCustomerProfileDet(customerProfileDet);
        customerProfileDet.setCustomer(customer);


        customerRepository.save(customer);
        return ResponseEntity.ok(new ApiResponse(true, "successfully created Customer!"));

    }

    @GetMapping("/customerss/{customerId}")
    public ResponseEntity<?> getOneCustomerResponse(@PathVariable(value = "customerId") Long customerId) {
        LOGGER.info("****Start GetOneCustomerController");
        try {
            CustomerPayload customerPayload = customerService.getOneCustomer(customerId);
            return ResponseEntity.ok(new ResponseDataApi("success", true, customerPayload));
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.info("***End GetOneCustomerController");
            return new ResponseEntity(new ResponseDataApi(e.getMessage(), false, null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createResponse")
    public ResponseEntity<?> createCustomerResponse(@RequestBody CustomerRequest customerRequest) {
        LOGGER.info("****Start CreateCustomerController");
        try {
            ResponseService responseService = customerService.createCustomer(customerRequest);
            if (responseService.getSuccess()) {
                return ResponseEntity.ok(new ResponseDataApi(responseService.getData(), true, customerRequest));

            }
            return new ResponseEntity(new ResponseDataApi(responseService.getData(), false, null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.info("****End CreateCustomerController");
            return new ResponseEntity(new ResponseDataApi(e.getLocalizedMessage(), false, "error"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/customerss")
    public ResponseEntity<?> getAllCustomerResponse() {
        LOGGER.info("****Start Get All Customer");
        try {
            List<Customer> customers = customerService.getAllList();
            if (customers.size() == 0) {
                LOGGER.error("Data Not Found : " + customers.size());
                LOGGER.info("****End Get All Customer");
                return new ResponseEntity(new ResponseDataApi("Data Not Found", false, null), HttpStatus.NOT_FOUND);
            }
            LOGGER.info("****End Get All Customer");
            return ResponseEntity.ok(new ResponseDataApi("success", true, customers));

        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.info("****End Get All Customer");
            return new ResponseEntity(new ResponseDataApi(e.getLocalizedMessage(), false, null), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/allRetriveOld")
    public List<Customer> getAllCustomerRetrive() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    @GetMapping("/allRetrive")
    public ResponseEntity<CustomerResponseAllRetrive> getAllCustomerResponseRetrive() {
        List<Customer> customers = customerRepository.findAll();
        if (customers == null) {
            return new ResponseEntity(new ApiResponse(false, "Data Not Found"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new CustomerResponseAllRetrive(true, customers));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDataOne> getOneCustomer(@PathVariable(value = "customerId") Long customerId) {
        return customerRepository.findById(customerId).map(customer -> {
            return ResponseEntity.ok(new CustomerResponseDataOne(true, customer));
        }).orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId));
    }

    @GetMapping("/demo/{customerId}")
    public ResponseEntity<CustomerResponseDataOneWithPayload> getOneCustomerWithPayload(@PathVariable(value = "customerId") Long customerId) {

        return customerRepository.findById(customerId).map(customer -> {
            CustomerPayload customerPayload = new CustomerPayload(customer.getId(), customer.getNama(),
                    customer.getEmail(), customer.getCustomerProfileDet());
            return ResponseEntity.ok(new CustomerResponseDataOneWithPayload(true, customerPayload));
        }).orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId));
    }

    @PostMapping("/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(value = "customerId") Long customerId) {
        LOGGER.info("****Start Delete Customer Id " + customerId);
        try {
            ResponseService responseService = customerService.deleteCustomer(customerId);
            if (responseService.getSuccess()) {
                LOGGER.info("***End Delete Customer Controller");
                return ResponseEntity.ok(new ResponseDataApi(responseService.getData(), true, "success"));
            }
            LOGGER.info("***End Delete Customer Controller");
            return new ResponseEntity(new ResponseDataApi("Data Not Found!", false, null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
            LOGGER.info("***End Delete Customer Controller");
            return new ResponseEntity(new ResponseDataApi("Error", false, e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable(value = "customerId") Long customerId,
                                            @RequestBody CustomerRequest customerRequest) {

        if (customerRequest.getNama() == null || customerRequest.getEmail() == null) {
            return new ResponseEntity(new ApiResponse(false, "Payload Validasi Fail!"), HttpStatus.BAD_REQUEST);
        }

        return customerRepository.findById(customerId).map(customer -> {
            CustomerProfileDet customerProfileDet = customer.getCustomerProfileDet();
            JenisKelaminEnum gender;
            String tempJenisKelaminRequest = customerRequest.getJenisKelamin() != null ? customerRequest.getJenisKelamin() : customerProfileDet.getJenisKelamin().toString();
            if (tempJenisKelaminRequest.equalsIgnoreCase("L") || tempJenisKelaminRequest.equals(JenisKelaminEnum.LAKI_LAKI)) {
                gender = JenisKelaminEnum.LAKI_LAKI;
            } else {
                gender = JenisKelaminEnum.PEREMPUAN;
            }
            customer.setId(customer.getId());
            customer.setNama(customerRequest.getNama());
            customer.setEmail(customerRequest.getEmail());

            Date tempDate = null;
            try {
                tempDate = customerRequest.getTanggalLahir() != null ? sdf.parse(customerRequest.getTanggalLahir()) : customerProfileDet.getTanggalLahir();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            customerProfileDet.setKota(customerRequest.getKota() != null ? customerRequest.getKota() : customerProfileDet.getKota());
            customerProfileDet.setTanggalLahir(tempDate);
            customerProfileDet.setNegara(customerRequest.getNegara() != null ? customerRequest.getNegara() : customerProfileDet.getNegara());
            customerProfileDet.setJenisKelamin(gender);
            customerProfileDet.setNoKtp(customerRequest.getNoKtp() != null ? customerRequest.getNoKtp() : customerProfileDet.getNoKtp());
            customerProfileDet.setCustomer(customer);

            customerRepository.save(customer);
            return ResponseEntity.ok(new ApiResponse(true, "Success update Customer!"));

        }).orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId));
    }


}
