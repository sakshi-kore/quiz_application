package com.jsp.quiz_application.entity;

import jakarta.persistence.*;

@Entity
public class UserResult extends Audit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_quiz_id")
    private UserQuiz userQuiz;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    public UserQuiz getUserQuiz() {
        return userQuiz;
    }

    public void setUserQuiz(UserQuiz userQuiz) {
        this.userQuiz = userQuiz;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Integer getTotalAttempted() {
        return totalAttempted;
    }

    public void setTotalAttempted(Integer totalAttempted) {
        this.totalAttempted = totalAttempted;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(Integer correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getWrongAnswer() {
        return wrongAnswer;
    }

    public void setWrongAnswer(Integer wrongAnswer) {
        this.wrongAnswer = wrongAnswer;
    }

    private Integer totalAttempted;
    private Integer correctAnswer;
    private Integer wrongAnswer;



}
