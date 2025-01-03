package com.skinversity.backend.Services;

import com.skinversity.backend.Requests.EmailRequest;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSenderImpl mailSender;

    static Dotenv dotenv = Dotenv.configure().load();

    private static final String appEmail = dotenv.get("MAIL_USERNAME");

    public EmailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    //method to take in the message, the receiver
    // throws an email not found exception and return a boolean when successful or not

    public void sendEmail(EmailRequest request) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(appEmail);
            helper.setTo(request.getRecipient());
            helper.setSubject(request.getSubject());
            helper.setText(request.getBodyText());
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
