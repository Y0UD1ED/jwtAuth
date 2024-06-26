package com.example.jwtAuth.dtos;

import java.util.List;

public class LeaderboardDto {
    Integer userId;
    String firstName;
    String lastName;
    List<ShortCourseDto> usersCourses;
    Integer score;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ShortCourseDto> getUsersCourses() {
        return usersCourses;
    }

    public void setUsersCourses(List<ShortCourseDto> usersCourses) {
        this.usersCourses = usersCourses;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
