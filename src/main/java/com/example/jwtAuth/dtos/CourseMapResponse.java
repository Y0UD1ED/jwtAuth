package com.example.jwtAuth.dtos;

import java.util.List;

public class CourseMapResponse {

    Integer userScores;
    List<ShortCourseDto> courses;

    public Integer getUserScores() {
        return userScores;
    }

    public void setUserScores(Integer userScores) {
        this.userScores = userScores;
    }

    public List<ShortCourseDto> getCourses() {
        return courses;
    }

    public void setCourses(List<ShortCourseDto> courses) {
        this.courses = courses;
    }
}
