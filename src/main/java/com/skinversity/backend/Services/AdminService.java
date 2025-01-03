package com.skinversity.backend.Services;

import com.skinversity.backend.Exceptions.UserAlreadyExists;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.Requests.EmailRequest;
import org.apache.commons.lang3.RandomStringUtils;
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


    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void enrollAdmin(String email) {
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new UserAlreadyExists("This email is linked to another user");
        }
        String tempPassword = RandomStringUtils.random(8);

        Users mewAdmin = new Users();
        mewAdmin.setEmail(email);
        mewAdmin.setPassword(passwordEncoder.encode(tempPassword));
        mewAdmin.setRole(ADMIN);
        mewAdmin.setCreatedAt(LocalDateTime.now());
        userRepository.save(mewAdmin);

        String message = "Your have successfully been enrolled as an administrator. " +
                "\nPlease sign in with your email and this generated password: <b>" + tempPassword + "</b>";
        String subject = "SkinVersity Administrator Credentials";
        String recipient = mewAdmin.getEmail();
        EmailRequest request = new EmailRequest();
        request.setSubject(subject);
        request.setRecipient(recipient);
        request.setBodyText(message);
        emailService.sendEmail(request);


    }
}
