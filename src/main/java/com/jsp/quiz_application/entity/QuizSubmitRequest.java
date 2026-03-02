package com.jsp.quiz_application.entity;

import java.util.Map;

public class QuizSubmitRequest {

    private Long userId;
    private Long quizId;


    private Map<Long, String> answers;

    public Long getUserId() {
        return userId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Map<Long, String> getAnswers() {
        return answers;
    }
}