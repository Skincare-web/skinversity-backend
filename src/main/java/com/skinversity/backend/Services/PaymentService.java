package com.skinversity.backend.Services;


import com.skinversity.backend.Models.Order;
import com.skinversity.backend.Models.Users;
import com.skinversity.backend.Repositories.OrderRepository;
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


import java.util.UUID;


@Service
public class PaymentService implements PaymentServiceInterface {
    private final RestTemplate restTemplate;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

  Dotenv dotenv = Dotenv.configure().load();
    private final String secretKey = dotenv.get("PAYSTACK_SECRET");

    public PaymentService(RestTemplate restTemplate,
                          PaymentRepository paymentRepository,
                          UserRepository userRepository,
                          OrderRepository orderRepository) {
        this.restTemplate = restTemplate;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
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

        Order order = orderRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        ResponseEntity<PaymentResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, PaymentResponse.class);


        String reference = response.getBody().getData().getReference();
        order.setReference(reference);
        orderRepository.save(order);

        System.out.println(response.getBody());


        return response.getBody();
    }

    @Override
    public void refundPayment(UUID paymentId) {
    }
}
