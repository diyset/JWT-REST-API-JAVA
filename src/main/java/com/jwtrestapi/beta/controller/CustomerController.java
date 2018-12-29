package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.model.Customer;
import com.jwtrestapi.beta.payload.CustomerPayload;
import com.jwtrestapi.beta.payload.ResponseService;
import com.jwtrestapi.beta.payload.request.CustomerRequest;
import com.jwtrestapi.beta.payload.response.*;
import com.jwtrestapi.beta.repository.CustomerProfileDetRepository;
import com.jwtrestapi.beta.repository.CustomerRepository;
import com.jwtrestapi.beta.service.CustomerService;
import com.jwtrestapi.beta.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/customer")
@RestController
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;

    /*
    Yang Lama Create Customer
    @PostMapping("/create")
    public ResponseEntity<?> createCustomerAndDetProfile(@Valid @RequestBody CustomerRequest customerRequest) throws ParseException {

        if (customerRepository.existsCustomerByEmail(customerRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email is Existing"), HttpStatus.BAD_REQUEST);
        }

        if (customerProfileDetRepository.existsCustomerProfileDetByNoKtp(customerRequest.getNoKtp())) {
            return new ResponseEntity(new ApiResponse(false, "No Ktp is Existing!"), HttpStatus.BAD_REQUEST);
        }
        JenisKelaminEnum gender;
        if (customerRequest.getJenisKelamin().equalsIgnoreCase(Constant.GENDER.LAKI_LAKI.getCode())) {
            gender = JenisKelaminEnum.LAKI_LAKI;
        } else if (customerRequest.getJenisKelamin().equalsIgnoreCase(Constant.GENDER.WANITA.getCode())) {
            gender = JenisKelaminEnum.PEREMPUAN;
        } else {
            gender = null;
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
    */

    @GetMapping("/getOne/{customerId}")
    public ResponseEntity<?> getOneCustomerResponse(@PathVariable(value = "customerId") Long customerId, @RequestHeader("Authorization") String token) {
        Class nameMethodClass = new Object() {}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_CONTROLLER.getDescription(nameMethodClass));
        LOGGER.info("Token : {}", token);
        try {
            CustomerPayload responseService = customerService.getOneCustomer(customerId);
            if(responseService.getCustomerId()!=null){
                LOGGER.info("Data : {}", responseService);
                LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(nameMethodClass));
                return ResponseEntity.ok(new ResponseDataApi(Constant.RESPONSE_SERVICE.SUCCESS.getDescription(), true, responseService));
            } else {
                LOGGER.info("Data : {}",Constant.RESPONSE_SERVICE.DATA_NOT_FOUND.getDescription());
                LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(nameMethodClass));
                return new ResponseEntity(new ResponseDataApi(Constant.RESPONSE_SERVICE.DATA_NOT_FOUND.getDescription(),false,null),HttpStatus.NOT_FOUND);
            }
//            return new ResponseEntity(new ResponseDataApi("success", true, responseService), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(nameMethodClass));
            return new ResponseEntity(new ResponseDataApi(e.getMessage(), false, null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createResponse")
    public ResponseEntity<?> createCustomerAndDet(@RequestBody CustomerRequest customerRequest, @RequestHeader("Authorization") String token) {
        Class classNameMethod = new Object(){}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_CONTROLLER.getDescription(classNameMethod));
        LOGGER.info("Token : {}", token);
        try {
            ResponseService responseService = customerService.createCustomer(customerRequest);
            if (responseService.getSuccess()) {
                LOGGER.info("Data : {}", customerRequest);
                LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
                return ResponseEntity.ok(new ResponseDataApi(Constant.RESPONSE_SERVICE.SUCCESS.getDescription(), true, customerRequest));
            }
            LOGGER.info("Data : {}", responseService);
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
            return new ResponseEntity(new ResponseDataApi(responseService.getData(), false, customerRequest), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error : {}",e.getMessage());
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
            return new ResponseEntity(new ResponseDataApi(e.getLocalizedMessage(), false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomer(@RequestHeader("Authorization") String token) {
        Class classNameMethod = new Object() {}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_CONTROLLER.getDescription(classNameMethod));
        LOGGER.info("Token : {}", token);
        try {
            List<CustomerPayload> customers = customerService.getAllListCustomer();
            if (customers.size() == 0) {
                LOGGER.error("Data Not Found : {}", customers.size());
                LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
                return new ResponseEntity(new ResponseDataApi(Constant.RESPONSE_SERVICE.DATA_NOT_FOUND.getDescription(), false, null), HttpStatus.NOT_FOUND);
            }
            LOGGER.info("Quantity Data : {}",customers.size());
            LOGGER.info("Data : {}", new ResponseDataApi(Constant.RESPONSE_SERVICE.SUCCESS.getDescription(),false,customers.size()+" unit"));
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
            return ResponseEntity.ok(new ResponseDataApi(Constant.RESPONSE_SERVICE.SUCCESS.getDescription(), true, customers));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            LOGGER.info(Constant.RESPONSE_SERVICE.ERROR.getDescription());
            return new ResponseEntity(new ResponseDataApi(e.getLocalizedMessage(), false, null), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable(value = "customerId") Long customerId) {
        Class classNameMethod = new Object() {}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_CONTROLLER.getDescription(classNameMethod));
        LOGGER.info("Customer Id : `{}`", customerId);
        try {
            ResponseService responseService = customerService.deleteCustomer(customerId);
            if (responseService.getSuccess()) {
                LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
                return ResponseEntity.ok(new ResponseDataApi(responseService.getData(), true, "success"));
            }
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
            return new ResponseEntity(new ResponseDataApi(Constant.RESPONSE_SERVICE.DATA_NOT_FOUND.getDescription(), false, null), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
            return new ResponseEntity(new ResponseDataApi(Constant.RESPONSE_SERVICE.ERROR.getDescription(), false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable(value = "customerId") Long customerId,
                                            @RequestBody CustomerRequest customerRequest) {
        Class classNameMethod = new Object(){}.getClass();
        LOGGER.info(Constant.INFO_SERVICE.START_CONTROLLER.getDescription(classNameMethod));
        try {
            ResponseService responseService = customerService.updateCustomer(customerId, customerRequest);
            if (!responseService.getSuccess()) {
                LOGGER.info("Error : {}", responseService.getData());
                LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
                return new ResponseEntity(new ResponseDataApi(responseService.getData(), false, null), HttpStatus.BAD_REQUEST);
            }
            LOGGER.info("Data : {}", customerRequest);
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
            return ResponseEntity.ok(new ResponseDataApi("success", true, responseService.getData()));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(Constant.RESPONSE_SERVICE.ERROR.getDescription()+"{}",e.getMessage());
            LOGGER.info(Constant.INFO_SERVICE.END_CONTROLLER.getDescription(classNameMethod));
            return new ResponseEntity(new ResponseDataApi(e.getLocalizedMessage(), false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
