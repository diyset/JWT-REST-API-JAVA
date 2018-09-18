package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.exception.AppException;
import com.jwtrestapi.beta.model.Role;
import com.jwtrestapi.beta.model.RoleName;
import com.jwtrestapi.beta.model.User;
import com.jwtrestapi.beta.payload.request.LoginRequest;
import com.jwtrestapi.beta.payload.request.SignUpRequest;
import com.jwtrestapi.beta.payload.response.ApiResponse;
import com.jwtrestapi.beta.payload.response.JwtAuthenticationResponse;
import com.jwtrestapi.beta.repository.RoleRepository;
import com.jwtrestapi.beta.repository.UserRepository;
import com.jwtrestapi.beta.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticatedUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
        if(userRepository.existsUserByEmail(signUpRequest.getEmail())){
            return new ResponseEntity(new ApiResponse(false,"Username is Already Taken!"),HttpStatus.NOT_ACCEPTABLE);
        }
        if(userRepository.existsUserByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false,"Email is Already Taken!"),HttpStatus.NOT_ACCEPTABLE);
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setUsername(signUpRequest.getUsername());
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(()->
                new AppException("User Role Not Set!"));
        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true,"User Registered success!"));

    }


}
