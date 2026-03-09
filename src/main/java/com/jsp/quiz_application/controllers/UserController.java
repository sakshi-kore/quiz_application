package com.jsp.quiz_application.controllers;



import com.jsp.quiz_application.entity.User;
import com.jsp.quiz_application.repository.UserRepository;
import com.jsp.quiz_application.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static liquibase.command.core.init.InitProjectUtil.FileCreationResultEnum.created;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@RequestBody User user) throws Exception {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedBy(user);
        user.setUpdatedBy(user);
        user.setActive(true);

        User savedUser = userRepository.save(user);
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
                        "<h2>Welcome to Quiz Application</h2>" +
                        "<p>Dear " + user.getUserName() + ",</p>" +

                        "<p>Your Quiz account has been created successfully.</p>" +

                        "<p>Please click the button below to start your quiz.</p>" +

                        "<br>" +

                        "<a href='http://localhost:8081/quiz/all' " +
                        "style='background:#1976d2;color:white;padding:12px 25px;" +
                        "text-decoration:none;border-radius:6px;font-size:16px'>" +
                        "Start Quiz" +
                        "</a>" +

                        "<br><br>" +

                        "<p style='font-size:12px;color:gray'>" +
                        "This email was sent automatically from Quiz Application." +
                        "</p>" +

                        "</td>" +
                        "</tr>" +

                        "</table>" +

                        "</body>" +
                        "</html>";
        emailService.sendHtmlEmail(
                user.getEmail(),
                "Welcome to Quiz Application",
                html
        );

        return savedUser;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
