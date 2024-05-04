package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.CourseDAO;
import com.example.jwtAuth.dtos.*;
import com.example.jwtAuth.models.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseDAO courseDAO;

    private final InfoModuleService infoModuleService;

    private final AssignmentService assignmentService;

    public CourseService(CourseDAO courseDAO, AssignmentService assignmentService, InfoModuleService infoModuleService) {
        this.courseDAO = courseDAO;
        this.assignmentService=assignmentService;
        this.infoModuleService=infoModuleService;
    }


    public void addNewCourse(CourseRequest course) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        Course newCourse = new Course(course.getName(),course.getDescription(),new Level(course.getLevelId()),new Direction(course.getDirectionId()));
        Integer courseId=courseDAO.addCourse(newCourse, userId);
        InfoModule infoModule = new InfoModule();
        infoModule.setContent("text");
        infoModule.setContentPosition(1);
        infoModule.setContent(course.getInfoModuleText());
        infoModuleService.save(infoModule, courseId);
        assignmentService.setCourseForUsers(course.getUserIds(),courseId);

    }


    public Course findByCourseId(int courseId) {
        return courseDAO.getCourseById(courseId);
    }
}
