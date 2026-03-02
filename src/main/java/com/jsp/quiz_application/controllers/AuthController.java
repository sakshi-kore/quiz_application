package com.jsp.quiz_application.controllers;

import com.jsp.quiz_application.entity.AuthResponse;
import com.jsp.quiz_application.entity.User;
import com.jsp.quiz_application.jwts.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public AuthResponse login(@RequestBody User user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUserName(),
                        user.getPassword()
                )
        );

        String token = jwtUtil.generateToken(user.getUserName());

        return new AuthResponse(
                token,
                user.getUserName(),
                jwtUtil.extractIssuedAt(token),
                jwtUtil.extractExpiration(token)
        );
    }
}
