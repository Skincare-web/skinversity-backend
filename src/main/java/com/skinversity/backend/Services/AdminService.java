package com.skinversity.backend.Services;

import com.skinversity.backend.Exceptions.UserAlreadyExists;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.Requests.EmailRequest;
import com.skinversity.backend.Requests.RegistrationRequest;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

import static com.skinversity.backend.Enumerators.Roles.ADMIN;


@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    Dotenv dotenv = Dotenv.configure().load();
    private final String adminPassword = dotenv.get("ADMIN_PASSWORD");

    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void enrollAdmin(RegistrationRequest request) {
        Optional<Users> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExists("This email is linked to another user");
        }

        Users newAdmin = new Users();
        newAdmin.setEmail(request.getEmail());
        newAdmin.setFullName(request.getName());
        newAdmin.setPassword(passwordEncoder.encode(adminPassword));
        newAdmin.setRole(ADMIN);
        newAdmin.setCreatedAt(LocalDateTime.now());
        userRepository.save(newAdmin);

        String message = "Your have successfully been enrolled as an administrator. " +
                "\nPlease sign in with your email and this generated password: " + adminPassword;
        String subject = "SkinVersity Administrator Credentials";
        String recipient = newAdmin.getEmail();
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setSubject(subject);
        emailRequest.setRecipient(recipient);
        emailRequest.setBodyText(message);
        emailService.sendEmail(emailRequest);


    }
}
