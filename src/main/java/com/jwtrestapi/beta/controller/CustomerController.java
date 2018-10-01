package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.exception.ResourceNotFoundException;
import com.jwtrestapi.beta.model.Customer;
import com.jwtrestapi.beta.model.CustomerProfileDet;
import com.jwtrestapi.beta.model.Enum.JenisKelaminEnum;
import com.jwtrestapi.beta.payload.CustomerPayload;
import com.jwtrestapi.beta.payload.request.CustomerRequest;
import com.jwtrestapi.beta.payload.response.ApiResponse;
import com.jwtrestapi.beta.payload.response.CustomerResponseAllRetrive;
import com.jwtrestapi.beta.payload.response.CustomerResponseDataOne;
import com.jwtrestapi.beta.payload.response.CustomerResponseDataOneWithPayload;
import com.jwtrestapi.beta.repository.CustomerProfileDetRepository;
import com.jwtrestapi.beta.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/customer")
@RestController
public class CustomerController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    CustomerRepository customerRepository;

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

    @GetMapping("/allRetriveOld")
    public List<Customer> getAllCustomerRetrive() {
        List<Customer> customers = customerRepository.findAll();
//        if(customers==null){
//            return new ResponseEntity(new ApiResponse(false,"Data Not Found"),HttpStatus.BAD_REQUEST);
//        }
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

    @PutMapping("/update/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable(value = "customerId") Long customerId,
                                             @RequestBody CustomerRequest customerRequest)  {

        if(customerRequest.getNama()==null||customerRequest.getEmail()==null){
            return new ResponseEntity(new ApiResponse(false,"Payload Validasi Fail!"),HttpStatus.BAD_REQUEST);
        }

        return customerRepository.findById(customerId).map(customer -> {
            CustomerProfileDet customerProfileDet = customer.getCustomerProfileDet();
            JenisKelaminEnum gender ;
            String tempJenisKelaminRequest = customerRequest.getJenisKelamin()!=null ? customerRequest.getJenisKelamin():customerProfileDet.getJenisKelamin().toString();
            if (tempJenisKelaminRequest.equalsIgnoreCase("L")||tempJenisKelaminRequest.equals(JenisKelaminEnum.LAKI_LAKI)) {
                gender = JenisKelaminEnum.LAKI_LAKI;
            } else {
                gender = JenisKelaminEnum.PEREMPUAN;
            }
            customer.setId(customer.getId());
            customer.setNama(customerRequest.getNama());
            customer.setEmail(customerRequest.getEmail());

            Date tempDate = null;
            try {
                tempDate = customerRequest.getTanggalLahir() !=null ? sdf.parse(customerRequest.getTanggalLahir()):customerProfileDet.getTanggalLahir();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            customerProfileDet.setKota(customerRequest.getKota()!=null ? customerRequest.getKota():customerProfileDet.getKota());
            customerProfileDet.setTanggalLahir(tempDate);
            customerProfileDet.setNegara(customerRequest.getNegara()!=null? customerRequest.getNegara():customerProfileDet.getNegara());
            customerProfileDet.setJenisKelamin(gender);
            customerProfileDet.setNoKtp(customerRequest.getNoKtp()!=null? customerRequest.getNoKtp():customerProfileDet.getNoKtp());
            customerProfileDet.setCustomer(customer);

            customerRepository.save(customer);
            return ResponseEntity.ok(new ApiResponse(true, "Success update Customer!"));

        }).orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", customerId));
    }


}
