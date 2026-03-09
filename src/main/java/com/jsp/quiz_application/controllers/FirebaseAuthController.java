package com.jsp.quiz_application.controllers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.jsp.quiz_application.entity.AuthResponse;
import com.jsp.quiz_application.entity.FirebaseRequest;
import com.jsp.quiz_application.entity.User;
import com.jsp.quiz_application.jwts.JwtUtil;
import com.jsp.quiz_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/firebase")
public class FirebaseAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody FirebaseRequest request) throws Exception {
        System.out.println("Firebase Token Received:");
        System.out.println(request.getToken());

        FirebaseToken decodedToken =
                FirebaseAuth.getInstance().verifyIdToken(request.getToken());

        String email = decodedToken.getEmail();
        String name = decodedToken.getName();


        User user = userRepository.findByUserName(name).orElse(null);

        if(user == null){

            user = new User();
            user.setUserName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("google_login_user"));


            user.setActive(true);

            userRepository.save(user);
        }

        String jwt = jwtUtil.generateToken(user.getUserName());

        return new AuthResponse(
                jwt,
                user.getUserName(),
                jwtUtil.extractIssuedAt(jwt),
                jwtUtil.extractExpiration(jwt)
        );

    }
}