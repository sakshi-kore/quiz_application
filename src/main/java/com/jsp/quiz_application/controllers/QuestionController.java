package com.jsp.quiz_application.controllers;



import com.jsp.quiz_application.entity.Question;
import com.jsp.quiz_application.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/create")
    public Question createQuestion(@RequestBody Question question) {
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        return questionRepository.save(question);
    }

    @GetMapping("/all")
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }
}