package com.example.jwtAuth.dtos;

public class PassedTestDto {
    Integer correctAnswers;
    Integer totalScores;

    Boolean isPassed;

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(Integer totalScores) {
        this.totalScores = totalScores;
    }

    public Boolean getPassed() {
        return isPassed;
    }

    public void setPassed(Boolean passed) {
        isPassed = passed;
    }
}
