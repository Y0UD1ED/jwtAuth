package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.CourseDAO;
import com.example.jwtAuth.models.Course;
import com.example.jwtAuth.models.ExtendUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }

    public Course findById(Integer id){
        return courseDAO.findById(id);
    }

    public void save(Course course){
        JwtAuthentication jwtAuthentication=(JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId=((ExtendUserDetails)jwtAuthentication.getPrincipal()).getId();
        courseDAO.save(course,userId);
    }
}
