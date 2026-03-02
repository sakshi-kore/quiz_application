package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.QuizSubmitRequest;
import com.jsp.quiz_application.entity.*;
import com.jsp.quiz_application.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/result")
public class QuizSubmissionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserQuizRepository userQuizRepository;

    @Autowired
    private UserResultRepository userResultRepository;


    @PostMapping("/submit")
    public ResponseEntity<String> submitQuiz(@RequestBody QuizSubmitRequest request) {
        System.out.println("UserId: " + request.getUserId());
        System.out.println("QuizId: " + request.getQuizId());


        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + request.getQuizId()));



        int totalAttempted = 0;
        int correct = 0;
        int wrong = 0;

        for (Map.Entry<Long, String> entry : request.getAnswers().entrySet()) {

            Long questionId = entry.getKey();
            String submittedAnswer = entry.getValue();

            Question question = questionRepository.findById(questionId).orElse(null);
            if (question == null) {
                return ResponseEntity.badRequest()
                        .body("Question not found with id: " + questionId);
            }



            UserQuiz userQuiz = new UserQuiz();
            userQuiz.setUser(user);
            userQuiz.setQuiz(quiz);
            userQuiz.setQuestion(question);
            userQuiz.setAnswer(submittedAnswer);

            userQuizRepository.save(userQuiz);

            totalAttempted++;

            if (question.getCorrectAnswer().equalsIgnoreCase(submittedAnswer)) {
                correct++;
            } else {
                wrong++;
            }
        }


        UserResult result = new UserResult();
        result.setQuiz(quiz);
        result.setTotalAttempted(totalAttempted);
        result.setCorrectAnswer(correct);
        result.setWrongAnswer(wrong);

        userResultRepository.save(result);

        return ResponseEntity.ok(request.toString());
    }


    @GetMapping("/quiz/{quizId}")
    public UserResult getResult(@PathVariable Long quizId) {

        return userResultRepository.findAll()
                .stream()
                .filter(r -> r.getQuiz().getId().equals(quizId))
                .findFirst()
                .orElseThrow();
    }
}