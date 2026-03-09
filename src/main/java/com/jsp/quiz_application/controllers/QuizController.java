package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.Quiz;
import com.jsp.quiz_application.entity.User;
import com.jsp.quiz_application.repository.QuizRepository;
import com.jsp.quiz_application.repository.UserRepository;
import com.jsp.quiz_application.service.EmailService;
import com.jsp.quiz_application.service.PaymentAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/quiz")
public class QuizController {

        @Autowired
        private PaymentAccessService paymentAccessService;
        @Autowired
        private EmailService emailService;
        @Autowired
        private QuizRepository quizRepository;
        @Autowired
        private UserRepository userRepository;

        @PostMapping("/create")
        public Quiz createQuiz(@RequestBody Quiz quiz, @RequestParam Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            quiz.setCreatedAt(LocalDateTime.now());
            quiz.setUpdatedAt(LocalDateTime.now());
            quiz.setCreatedBy(user);
            quiz.setUpdatedBy(user);
            String html =
                    "<html>" +
                            "<body style='font-family:Arial;background:#f4f6f8;padding:40px'>" +

                            "<table align='center' width='600' style='background:white;border-radius:10px;padding:30px;text-align:center'>" +

                            "<tr>" +
                            "<td style='background:#1976d2;color:white;padding:20px;font-size:22px;border-radius:8px'>" +
                            "<b>QUIZ APPLICATION</b><br>" +
                            "<span style='font-size:12px'>Test your knowledge</span>" +
                            "</td>" +
                            "</tr>" +

                            "<tr>" +
                            "<td style='padding:30px'>" +
                            "<h2>Quiz Started</h2>" +
                            "<p>Hello " + user.getUserName() + ",</p>" +

                            "<p>Your quiz has started successfully.</p>" +

                            "<p>All the best for your quiz!</p>" +

                            "<a href='http://localhost:8081/quiz/start' " +
                            "style='background:#1976d2;color:white;padding:12px 25px;text-decoration:none;border-radius:6px'>" +
                            "Start Quiz" +
                            "</a>" +

                            "</td>" +
                            "</tr>" +

                            "</table>" +
                            "</body>" +
                            "</html>";
            emailService.sendEmail(
                    user.getEmail(),
                    "Quiz Started",
                    "Your quiz has started. Best of luck!"
            );
            return quizRepository.save(quiz);
        }

        @GetMapping("/all")
        public List<Quiz> getAll() {
            return quizRepository.findAll();
        }
    @GetMapping("/start")
    public String startQuiz(@RequestParam Long userId,
                            @RequestParam Long quizId){

        boolean hasPaid = paymentAccessService.hasPaid(userId, quizId);

        if(!hasPaid){
            throw new RuntimeException("Payment required to access quiz");
        }

        return "Quiz Access Granted";
    }
    }

