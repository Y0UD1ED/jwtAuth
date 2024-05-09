package com.example.jwtAuth.controllers;

import com.example.jwtAuth.services.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentController {
    private  final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }
    @GetMapping("/getAssignmentCoursesByLevel/{levelId}")
    public ResponseEntity<?> getCourseMap(@PathVariable("levelId") int levelId){
        return ResponseEntity.ok(assignmentService.getAssignedCoursesForUserByLevel(levelId));
    }

    @GetMapping("/getAssignmentCourse/{id}")
    public ResponseEntity<?> getCourse(@PathVariable("id") int courseId){
        return ResponseEntity.ok(assignmentService.getOneAssignedCourseForUserByCourseId(courseId));
    }
}
