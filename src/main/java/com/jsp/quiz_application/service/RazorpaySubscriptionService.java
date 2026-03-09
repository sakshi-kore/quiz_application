package com.jsp.quiz_application.service;

import com.razorpay.RazorpayClient;
import com.razorpay.Subscription;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpaySubscriptionService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public Subscription createSubscription(String planId) throws Exception {

        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

        JSONObject request = new JSONObject();
        request.put("plan_id", planId);
        request.put("total_count", 12);
        request.put("customer_notify", 1);

        return razorpay.subscriptions.create(request);
    }
}