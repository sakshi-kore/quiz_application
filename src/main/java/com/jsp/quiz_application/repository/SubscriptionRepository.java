package com.jsp.quiz_application.repository;

import com.jsp.quiz_application.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findBySubscriptionId(String subscriptionId);

}