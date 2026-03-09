package com.jsp.quiz_application.service;

import com.jsp.quiz_application.entity.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class DeepSeekQuestionService {

    @Value("${deepseek.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Question generateQuestion(String topic) {

        String url = "https://api.deepseek.com/chat/completions";

        Map<String, Object> body = Map.of(
                "model", "deepseek-chat",
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content",
                                "Generate a Java MCQ question with 4 options and correct answer in JSON format. Topic: " + topic
                        )
                ),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        String questionText = "Default Java Question";

        try {

            Map response = restTemplate.postForObject(url, request, Map.class);

            List<Map> choices = (List<Map>) response.get("choices");
            Map firstChoice = choices.get(0);
            Map message = (Map) firstChoice.get("message");

            questionText = (String) message.get("content");

        } catch (Exception e) {
            System.out.println("DeepSeek API failed: " + e.getMessage());
        }

        Question q = new Question();

        q.setTitle(topic);
        q.setDescription(questionText);

        q.setOptionA("Option A");
        q.setOptionB("Option B");
        q.setOptionC("Option C");
        q.setOptionD("Option D");

        q.setCorrectAnswer("Option A");

        q.setCountry("India");
        q.setState("Maharashtra");
        q.setCity("Pune");

        return q;
    }
}