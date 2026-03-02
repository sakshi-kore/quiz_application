package com.jsp.quiz_application.repository;

import com.jsp.quiz_application.entity.UserResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResultRepository extends JpaRepository<UserResult, Long> {
}