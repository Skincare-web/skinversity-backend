package com.skinversity.backend.Services;

import com.skinversity.backend.Enumerators.PaymentMethod;
import com.skinversity.backend.Models.Payment;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Repositories.PaymentRepository;
import com.skinversity.backend.Repositories.UserRepository;
import com.skinversity.backend.Requests.PaymentRequest;
import com.skinversity.backend.Requests.PaymentResponse;
import com.skinversity.backend.ServiceInterfaces.PaymentServiceInterface;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.skinversity.backend.Enumerators.PaymentMethod.CASH;

@Service
public class PaymentService implements PaymentServiceInterface {
    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    Dotenv dotenv = Dotenv.configure().load();
    String secretKey = dotenv.get("PAYSTACK_SECRET");

    public PaymentService(RestTemplate restTemplate, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PaymentResponse processPayment(String userEmail, int totalAmount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setEmail(userEmail);
        paymentRequest.setAmount(totalAmount * 100);
        System.out.println(paymentRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<PaymentRequest> request = new HttpEntity<>(paymentRequest, headers);

        String url = "https://api.paystack.co/transaction/initialize";

        Users users = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UUID userId = users.getUserId();

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, PaymentResponse.class);

        System.out.println(response.getBody());


        return response.getBody();
    }

    @Override
    public void refundPayment(UUID paymentId) {

    }
}
