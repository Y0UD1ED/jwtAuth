package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.CoursePassingDAO;
import com.example.jwtAuth.models.CoursePassing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursePassingService {
    private final CoursePassingDAO coursePassingDAO;

    public CoursePassingService(CoursePassingDAO coursePassingDAO) {
        this.coursePassingDAO = coursePassingDAO;
    }
    public List<CoursePassing> findAllCoursePassingByLevelAndDirection(Integer level, Integer direction){
        List<CoursePassing> coursePassingList = coursePassingDAO.getAllCoursePassingByLevelAndDirection(level, direction);
        return coursePassingDAO.getAllCoursePassingByLevelAndDirection(level, direction);
    }


}
