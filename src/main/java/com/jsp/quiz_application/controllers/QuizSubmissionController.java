package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.QuizSubmitRequest;
import com.jsp.quiz_application.entity.*;
import com.jsp.quiz_application.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String submitQuiz(@RequestBody QuizSubmitRequest request) {

        User user = userRepository.findById(request.getUserId()).orElseThrow();
        Quiz quiz = quizRepository.findById(request.getQuizId()).orElseThrow();

        int totalAttempted = 0;
        int correct = 0;
        int wrong = 0;

        for (Map.Entry<Long, String> entry : request.getAnswers().entrySet()) {

            Long questionId = entry.getKey();
            String submittedAnswer = entry.getValue();

            Question question = questionRepository.findById(questionId).orElseThrow();


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

        return "Quiz Submitted Successfully!";
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