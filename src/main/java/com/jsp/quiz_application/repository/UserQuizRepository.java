package com.jsp.quiz_application.repository;



import com.jsp.quiz_application.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
}
