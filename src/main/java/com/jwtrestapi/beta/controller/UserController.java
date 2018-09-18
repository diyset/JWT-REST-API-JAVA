package com.jwtrestapi.beta.controller;

import com.jwtrestapi.beta.exception.ResourceNotFoundException;
import com.jwtrestapi.beta.model.User;
import com.jwtrestapi.beta.payload.UserIdentityAvailability;
import com.jwtrestapi.beta.payload.UserProfile;
import com.jwtrestapi.beta.payload.UserSummary;
import com.jwtrestapi.beta.repository.UserRepository;
import com.jwtrestapi.beta.security.CurrentUser;
import com.jwtrestapi.beta.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        UserSummary userSummary = new UserSummary();

        userSummary.setId(userPrincipal.getId());
        userSummary.setName(userPrincipal.getName());
        userSummary.setUsername(userPrincipal.getUsername());

        return userSummary;
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email){
        Boolean isAvailable = !userRepository.existsUserByEmail(email);

        return new UserIdentityAvailability(isAvailable);
    }
    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username){
        Boolean isAvailable = !userRepository.existsUserByUsername(username);

        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {

        User user = userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User","username",username));

        UserProfile userProfile = new UserProfile();
        userProfile.setId(user.getId());
        userProfile.setJoinedAt(user.getCreatedAt());
        userProfile.setName(user.getName());
        userProfile.setUsername(user.getUsername());
        return userProfile;
    }
}
