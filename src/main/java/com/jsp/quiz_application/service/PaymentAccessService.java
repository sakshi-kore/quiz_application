package com.jsp.quiz_application.service;

import com.jsp.quiz_application.entity.Payment;
import com.jsp.quiz_application.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentAccessService {

    @Autowired
    private PaymentRepository paymentRepository;

    public boolean hasPaid(Long userId, Long quizId){

        List<Payment> payments = paymentRepository.findAll();

        for(Payment payment : payments){

            if(payment.getUser().getId().equals(userId)
                    && payment.getQuiz().getId().equals(quizId)
                    && "SUCCESS".equals(payment.getStatus())){

                return true;
            }
        }

        return false;
    }
}