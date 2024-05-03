package com.example.jwtAuth.models;

public class Option {
    String option;
    Boolean correct;
    Integer questionId;

    public Option() {
    }
    public Option(String option, Boolean correct) {
        this.option = option;
        this.correct = correct;
    }

    public String getOption() {
        return this.option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
