package com.jsp.quiz_application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Question extends Audit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String correctAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptionA(String optionA) {
        this.option_a = optionA;
    }

    public void setOptionB(String optionB) {
        this.option_b = optionB;
    }

    public void setOptionC(String optionC) {
        this.option_c = optionC;
    }

    public void setOptionD(String optionD) {
        this.option_d = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
