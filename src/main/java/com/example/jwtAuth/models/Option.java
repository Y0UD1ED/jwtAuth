package com.example.jwtAuth.models;

public class Option {
    String option;
    Boolean correct;


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


}
