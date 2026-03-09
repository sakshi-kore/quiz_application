package com.jsp.quiz_application.repository;

import com.jsp.quiz_application.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Payment findByOrderId(String orderId);
    List<Payment> findByStatus(String status);

}