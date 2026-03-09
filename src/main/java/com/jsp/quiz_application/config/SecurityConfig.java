package com.jsp.quiz_application.config;
import com.jsp.quiz_application.service.UserService;
import com.jsp.quiz_application.jwts.JwtFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;
    @Autowired
    private UserService userService;

    public SecurityConfig(JwtFilter jwtFilter, UserService userService) {
        this.jwtFilter = jwtFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configure(http))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // allow HTML pages
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/chatbot.html",
                                "/payment.html",
                                "/quiz.html",
                                "/css/**",
                                "/js/**"
                        ).permitAll()

                        // WebSocket endpoints
                        .requestMatchers("/chat/**").permitAll()
                        .requestMatchers("/app/**").permitAll()
                        .requestMatchers("/topic/**").permitAll()
                        .requestMatchers("/user/**").permitAll()

                        // your APIs
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/firebase/**").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/question/**").permitAll()
                        .requestMatchers("/quiz/create").permitAll()
                        .requestMatchers("/quiz/all").permitAll()
                        .requestMatchers("/result/**").permitAll()
                        .requestMatchers("/payment/**").permitAll()
                        .requestMatchers("/webhook/**").permitAll()
                        .requestMatchers("/subscription/**").permitAll()

                        .anyRequest().authenticated()
                )
                .userDetailsService(userService)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}