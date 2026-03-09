package com.jsp.quiz_application.service;

import com.jsp.quiz_application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QuizFailureAlertService {

    @Autowired
    private EmailService emailService;

    @Value("${admin.email}")
    private String adminEmail;

    public void sendFailureAlert(User user, Long quizId, String reason) {

        String subject = "⚠ Quiz Failure Alert - Immediate Attention Required";

        String html =
                "<html>" +
                        "<body style='font-family:Arial;background:#f4f6f8;padding:40px'>" +

                        "<table align='center' width='600' style='background:white;border-radius:10px;padding:30px'>" +

                        "<tr>" +
                        "<td style='background:#d32f2f;color:white;padding:20px;font-size:22px;text-align:center'>" +
                        "<b>QUIZ SYSTEM ALERT</b>" +
                        "</td>" +
                        "</tr>" +

                        "<tr>" +
                        "<td style='padding:30px'>" +

                        "<h3 style='color:#d32f2f'>Quiz Failure Detected</h3>" +

                        "<p><b>Username:</b> " + user.getUserName() + "</p>" +
                        "<p><b>Email:</b> " + user.getEmail() + "</p>" +
                        "<p><b>Quiz ID:</b> " + quizId + "</p>" +
                        "<p><b>Failure Reason:</b> " + reason + "</p>" +
                        "<p><b>Timestamp:</b> " + java.time.LocalDateTime.now() + "</p>" +

                        "<hr>" +

                        "<p style='font-size:12px;color:gray'>" +
                        "This alert was generated automatically by the Quiz Application Monitoring System." +
                        "</p>" +

                        "</td>" +
                        "</tr>" +

                        "</table>" +
                        "</body>" +
                        "</html>";

        try {
            emailService.sendHtmlEmail(adminEmail, subject, html);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}