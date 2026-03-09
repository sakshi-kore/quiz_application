package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.Subscription;
import com.jsp.quiz_application.entity.User;
import com.jsp.quiz_application.repository.SubscriptionRepository;
import com.jsp.quiz_application.repository.UserRepository;
import com.jsp.quiz_application.service.RazorpaySubscriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private RazorpaySubscriptionService razorpaySubscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public String createSubscription(@RequestParam Long userId,
                                     @RequestParam String planId) throws Exception {
        com.razorpay.Subscription razorSubscription =
                razorpaySubscriptionService.createSubscription(planId);

        User user = userRepository.findById(userId).orElseThrow();

        Subscription subscription = new Subscription();

        subscription.setSubscriptionId(razorSubscription.get("id"));
        subscription.setPlanId(planId);
        subscription.setStatus("CREATED");
        subscription.setUser(user);

        subscriptionRepository.save(subscription);

        return razorSubscription.toString();
    }
}