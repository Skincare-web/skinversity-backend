package com.skinversity.backend.Services;

import com.skinversity.backend.Enumerators.Roles;
import com.skinversity.backend.Exceptions.UserAlreadyExists;
import com.skinversity.backend.Exceptions.UserNotFoundException;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.Requests.EmailRequest;
import com.skinversity.backend.Requests.LoginRequest;
import com.skinversity.backend.Requests.RegistrationRequest;
import com.skinversity.backend.Requests.ResetPassword;
import com.skinversity.backend.ServiceInterfaces.UserServiceInterface;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, JWTService jwtService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void registerUser(RegistrationRequest registrationRequest) {
        Optional<Users> user = userRepository.findByEmail(registrationRequest.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExists("An account has been registered with this email.");
        }
        Users newUser = new Users();
        newUser.setEmail(registrationRequest.getEmail());
        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setAddress(registrationRequest.getAddress());
        newUser.setPhone(registrationRequest.getPhone());
        newUser.setRole(Roles.CUSTOMER);
        newUser.setCreatedAt(LocalDateTime.now());
        userRepository.save(newUser);.

        //send email to newly registered customer to confirm registration
        sendEmail(registrationRequest, emailService);

    }

    private static void sendEmail(RegistrationRequest registrationRequest, EmailService emailService) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setSubject("Account Creation with Skinversity");
        emailRequest.setRecipient(registrationRequest.getEmail());
        emailRequest.setBodyText("Hello <b>"
        + registrationRequest.getName() +"</b>" + " your account with Skinversity has been successfully registered.");
        emailService.sendEmail(emailRequest);
    }

    @Override
    public void loginUser(LoginRequest registrationRequest, HttpServletResponse httpServletResponse) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    registrationRequest.getEmail(), registrationRequest.getPassword()
            ));
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

            Users user = userPrincipal.getUser();
            String accessToken = jwtAccessToken(registrationRequest.getEmail(), user.getRole());

            Cookie cookie = new Cookie("accessToken", accessToken);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            cookie.setSecure(true);
            httpServletResponse.addCookie(cookie);

//        }catch(BadCredentialsException e){
//            throw new BadCredentialsException("Invalid username or password");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void resetPassword(ResetPassword resetPassword) {
        Optional<Users> user = userRepository.findByEmail(resetPassword.getEmail());
        if (user.isPresent()) {
            if(user.get().getPassword().equals(passwordEncoder.encode(resetPassword.getOldPassword()))) {
                user.get().setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
                userRepository.save(user.get());
            }else{
                throw new BadCredentialsException("Invalid password");
            }
        }else{
            throw new UserNotFoundException("User not found");
        }
    }

    private String jwtAccessToken(String email, Roles role) {
        return jwtService.generateAccessToken(email, role);
    }
}
