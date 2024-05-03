package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dao.CoursePassingDAO;
import com.example.jwtAuth.models.CoursePassing;
import com.example.jwtAuth.services.CoursePassingService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/passing")
public class PassingController {
    private final CoursePassingService coursePassingService;

    public PassingController(CoursePassingService coursePassingService) {
        this.coursePassingService = coursePassingService;
    }

    @GetMapping("/allUsers/byDirectionAndLevel/{direction}/{level}")
    public ResponseEntity<?> getPassingByDirectionAndLevel(@PathVariable(value = "direction") Integer direction, @PathVariable(value = "level") Integer level) {
        List<CoursePassing> passing = coursePassingService.findAllCoursePassingByLevelAndDirection(level, direction);
        return ResponseEntity.ok(passing);
    }

}
