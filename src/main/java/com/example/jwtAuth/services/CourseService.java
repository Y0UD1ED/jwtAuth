package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.CourseDAO;
import com.example.jwtAuth.dao.DirectionDAO;
import com.example.jwtAuth.dao.LevelDAO;
import com.example.jwtAuth.models.Course;
import com.example.jwtAuth.models.Direction;
import com.example.jwtAuth.models.ExtendUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseDAO courseDAO;
    private final LevelDAO levelDAO;
    private final DirectionDAO directionDAO;

    public CourseService(CourseDAO courseDAO, LevelDAO levelDAO, DirectionDAO directionDAO) {
        this.courseDAO = courseDAO;
        this.levelDAO = levelDAO;
        this.directionDAO = directionDAO;
    }

    public List<Course> findAll(){
        List<Course> list=courseDAO.findAll();
        for(Course course:list){
            course.setDirection(directionDAO.findById(course.getDirection().getId()));
            course.setLevel(levelDAO.findById(course.getLevel().getId()));
        }
        return list;
    }
    public Course findById(Integer id){
        Course course=courseDAO.findById(id);
        course.setDirection(directionDAO.findById(course.getDirection().getId()));
        course.setLevel(levelDAO.findById(course.getLevel().getId()));
        return course;
    }

    public void save(Course course){
        JwtAuthentication jwtAuthentication=(JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId=((ExtendUserDetails)jwtAuthentication.getPrincipal()).getId();
        courseDAO.save(course,userId);
    }
}
