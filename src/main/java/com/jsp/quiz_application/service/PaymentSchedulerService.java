package com.jsp.quiz_application.service;

import com.jsp.quiz_application.entity.Payment;
import com.jsp.quiz_application.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentSchedulerService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Scheduled(fixedRate =300000) // Every 5 minutes
    public void checkPendingPayments() {

        List<Payment> payments = paymentRepository.findByStatus("PENDING");

        for (Payment payment : payments) {
            payment.setStatus("EXPIRED");
            paymentRepository.save(payment);
        }

        System.out.println("Checked and updated pending payments");
    }
    @Scheduled(fixedRate = 300000) // every 5 minute
    public void retryFailedPayments() {

        List<Payment> failedPayments = paymentRepository.findByStatus("FAILED");

        for (Payment payment : failedPayments) {
            payment.setStatus("RETRY");
            paymentRepository.save(payment);
        }

        System.out.println("Retry payment process started");
    }

}