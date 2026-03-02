package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.Quiz;
import com.jsp.quiz_application.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {



        @Autowired
        private QuizRepository quizRepository;

        @PostMapping("/create")
        public Quiz createQuiz(@RequestBody Quiz quiz) {
            quiz.setCreatedAt(LocalDateTime.now());
            quiz.setUpdatedAt(LocalDateTime.now());
            return quizRepository.save(quiz);
        }

        @GetMapping("/all")
        public List<Quiz> getAll() {
            return quizRepository.findAll();
        }
    }

