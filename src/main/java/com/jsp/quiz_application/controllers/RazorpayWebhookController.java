package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.Payment;
import com.jsp.quiz_application.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class RazorpayWebhookController {

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/razorpay")
    public String handleWebhook(@RequestBody String payload,
                                @RequestHeader("X-Razorpay-Signature") String signature) {

        try {

            JSONObject json = new JSONObject(payload);

            String event = json.getString("event");

            if (event.equals("payment.captured")) {

                JSONObject paymentEntity = json
                        .getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

                String orderId = paymentEntity.getString("order_id");
                String paymentId = paymentEntity.getString("id");

                Payment payment = paymentRepository
                        .findAll()
                        .stream()
                        .filter(p -> orderId.equals(p.getOrderId()))
                        .findFirst()
                        .orElseThrow();

                payment.setPaymentId(paymentId);
                payment.setStatus("SUCCESS");

                paymentRepository.save(payment);
            }

            return "Webhook processed";

        } catch (Exception e) {
            return "Webhook failed";
        }
    }
}