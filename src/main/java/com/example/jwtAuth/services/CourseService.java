package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.CourseDAO;
import com.example.jwtAuth.models.Course;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public Course findById(Integer id){
        return courseDAO.findById(id);
    }
}
