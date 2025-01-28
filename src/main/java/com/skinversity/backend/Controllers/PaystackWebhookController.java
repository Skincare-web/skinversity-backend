package com.skinversity.backend.Controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skinversity.backend.Models.Order;
import com.skinversity.backend.Repositories.OrderRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

import java.util.Optional;

import static com.skinversity.backend.Enumerators.OrderStatus.PAID;


@RestController
@RequestMapping("api/v1/paystack")
public class PaystackWebhookController {
    private final OrderRepository orderRepository;

    static Dotenv dotenv = Dotenv.configure().load();


    private static final String API_SECRET_KEY = dotenv.get("PAYSTACK_SECRET");

    public PaystackWebhookController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("x-paystack-signature") String signature) {
        try {
            //verify the signature
            if (!isValidSignature(payload, signature)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode payloadJson = mapper.readTree(payload);
            String reference = payloadJson.path("data").path("reference").asText();
            String transactionId = payloadJson.path("data").path("id").asText();

            System.out.println("Transaction reference: " + reference);

            Optional <Order> order = orderRepository.findByReference(reference);

            if(order.isPresent()) {
                Order orderObj = order.get();
                orderObj.setStatus(PAID);
                orderRepository.save(orderObj);
                System.out.println("Order status: " + orderObj.getStatus().toString());
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            //log the payload for debugging
            System.out.println("Received payload: " + payload);

            //handle specific events
            if (payload.contains("\"event\":\"charge.success\"")) {
                System.out.println("Transaction was successful. Update the database here.");
                //extract transaction details and update your database
            } else {
                System.out.println("Event not handled.");
            }
            return ResponseEntity.ok("Webhook handled successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    private boolean isValidSignature(String payload, String signature) {
        try {
            //create HMAC-SHA512 signature
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(API_SECRET_KEY.getBytes(), "HmacSHA512");
            sha512Hmac.init(secretKey);
            byte[] hashedPayload = sha512Hmac.doFinal(payload.getBytes());

            //convert the hashed payload to a hex string
            String expectedSignature = Hex.encodeHexString(hashedPayload);

            //compare the signature (case-sensitive)
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
