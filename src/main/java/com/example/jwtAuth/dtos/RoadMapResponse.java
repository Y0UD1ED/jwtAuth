package com.example.jwtAuth.dtos;

import java.util.List;

public class RoadMapResponse {
    String direction;
    List<CourseMapDto> courses;
    Integer passing;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<CourseMapDto> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseMapDto> courses) {
        this.courses = courses;
    }

    public Integer getPassing() {
        return passing;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }
}
