package com.skinversity.backend.Controllers;

import com.skinversity.backend.Exceptions.UserAlreadyExists;
import com.skinversity.backend.Exceptions.UserNotFoundException;
import com.skinversity.backend.Requests.LoginRequest;
import com.skinversity.backend.Requests.RegistrationRequest;
import com.skinversity.backend.Requests.ResetPassword;
import com.skinversity.backend.Services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        try {
            userService.registerUser(registrationRequest);
        } catch (UserAlreadyExists e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        try{
            userService.loginUser(loginRequest,httpServletResponse);
            return new ResponseEntity<>("Login Successful", HttpStatus.OK);
        }catch(BadCredentialsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }catch(UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/change-password")
    public ResponseEntity<?> resetPassword(ResetPassword resetPassword) {
        try{
            userService.resetPassword(resetPassword);
        }catch(BadCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

        }catch (UserNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Your password has been reset successfully.", HttpStatus.OK);
    }
}

