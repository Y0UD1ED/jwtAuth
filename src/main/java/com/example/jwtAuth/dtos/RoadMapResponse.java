package com.example.jwtAuth.dtos;

import java.util.List;

public class RoadMapResponse {
    String direction;
    List<CourseMapDtoDel> courses;
    Integer passing;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<CourseMapDtoDel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseMapDtoDel> courses) {
        this.courses = courses;
    }

    public Integer getPassing() {
        return passing;
    }

    public void setPassing(Integer passing) {
        this.passing = passing;
    }
}
