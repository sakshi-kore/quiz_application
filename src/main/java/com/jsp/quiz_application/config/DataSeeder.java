package com.jsp.quiz_application.config;

import com.jsp.quiz_application.entity.Question;
import com.jsp.quiz_application.repository.QuestionRepository;
import com.jsp.quiz_application.service.DeepSeekQuestionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedQuestions(
            QuestionRepository questionRepository,
            DeepSeekQuestionService deepSeekQuestionService) {

        return args -> {

            if (questionRepository.count() == 0) {

                for (int i = 0; i < 5; i++) {

                    Question q = deepSeekQuestionService.generateQuestion("Java");

                    questionRepository.save(q);
                }

                System.out.println("DeepSeek Questions Seeded Successfully");

            }

        };
    }
}