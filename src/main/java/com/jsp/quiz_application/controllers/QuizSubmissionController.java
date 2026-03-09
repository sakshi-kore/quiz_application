package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.QuizSubmitRequest;
import com.jsp.quiz_application.entity.*;
import com.jsp.quiz_application.repository.*;
import com.jsp.quiz_application.service.EmailService;
import com.jsp.quiz_application.service.PdfService;
import com.jsp.quiz_application.service.QuizFailureAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/result")
public class QuizSubmissionController {
    @Autowired
    private QuizFailureAlertService alertService;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private EmailService emailService;

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
    public ResponseEntity<String> submitQuiz(@RequestBody QuizSubmitRequest request) throws Exception {
        try {
        System.out.println("UserId: " + request.getUserId());
        System.out.println("QuizId: " + request.getQuizId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + request.getQuizId()));



        int totalAttempted = 0;
        int correct = 0;
        int wrong = 0;

            UserQuiz lastUserQuiz = null;

            for (Map.Entry<Long, String> entry : request.getAnswers().entrySet()) {

                Long questionId = entry.getKey();
                String submittedAnswer = entry.getValue();

                Question question = questionRepository.findById(questionId).orElse(null);

                if (question == null) {
                    throw new RuntimeException("Question not found with id: " + questionId);
                }

                UserQuiz userQuiz = new UserQuiz();
                userQuiz.setUser(user);
                userQuiz.setQuiz(quiz);
                userQuiz.setQuestion(question);
                userQuiz.setAnswer(submittedAnswer);

                userQuizRepository.save(userQuiz);

                lastUserQuiz = userQuiz;   // ⭐ IMPORTANT

                totalAttempted++;

                if (question.getCorrectAnswer().equalsIgnoreCase(submittedAnswer)) {
                    correct++;
                } else {
                    wrong++;
                }
            }


        UserResult result = new UserResult();
        result.setQuiz(quiz);
        result.setUserQuiz(lastUserQuiz);
        result.setTotalAttempted(totalAttempted);
        result.setCorrectAnswer(correct);
        result.setWrongAnswer(wrong);

        userResultRepository.save(result);
        byte[] pdf = pdfService.generateResultPdf(user, result);
        String html =
                "<html>" +
                        "<body style='font-family:Arial;background:#f4f6f8;padding:40px'>" +

                        "<table align='center' width='600' style='background:white;border-radius:10px;padding:30px;text-align:center'>" +

                        "<tr>" +
                        "<td style='background:#1976d2;color:white;padding:20px;font-size:22px'>" +
                        "<b>QUIZ APPLICATION</b>" +
                        "</td>" +
                        "</tr>" +

                        "<tr>" +
                        "<td style='padding:30px'>" +

                        "<h2>Quiz Completed</h2>" +

                        "<p>Hello " + user.getUserName() + ",</p>" +

                        "<p>Your quiz has been completed successfully.</p>" +

                        "<p>Total Attempted: " + totalAttempted + "</p>" +
                        "<p>Correct Answers: " + correct + "</p>" +
                        "<p>Wrong Answers: " + wrong + "</p>" +

                        "<p>Your detailed result is attached in PDF.</p>" +

                        "</td>" +
                        "</tr>" +

                        "</table>" +
                        "</body>" +
                        "</html>";
        emailService.sendEmailWithAttachment(
                user.getEmail(),
                "Thank You Your Quiz Was Completed And Here is Your Quiz Result",
                html,
                pdf,
                "quiz-result.pdf"
        );
        return ResponseEntity.ok(request.toString());
    }catch (Exception e) {
            System.out.println("Quiz Failed: " + e.getMessage());

            User user = userRepository.findById(request.getUserId()).orElse(null);

            if (user != null) {
                alertService.sendFailureAlert(
                        user,
                        request.getQuizId(),
                        e.getMessage()
                );
            }

            return ResponseEntity.internalServerError()
                    .body("Quiz failed. Admin notified.");
        }
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