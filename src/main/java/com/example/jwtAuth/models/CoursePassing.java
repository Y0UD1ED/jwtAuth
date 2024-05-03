package com.example.jwtAuth.models;

public class CoursePassing {
    Integer userId;
    String secondName;
    String firstName;

    Integer userScores;

    Integer totalScores;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getUserScores() {
        return userScores;
    }

    public void setUserScores(Integer userScores) {
        this.userScores = userScores;
    }

    public Integer getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(Integer totalScores) {
        this.totalScores = totalScores;
    }
}
