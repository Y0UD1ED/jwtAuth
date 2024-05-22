package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.CourseRequest;
import com.example.jwtAuth.models.*;
import com.example.jwtAuth.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CourseController {

    private final CourseService courseService;

    private final QuestModuleService questModuleService;

    private final InfoModuleService infoModuleService;

    public CourseController(CourseService courseService, InfoModuleService infoModuleService, QuestModuleService questModuleService) {
        this.courseService = courseService;
        this.infoModuleService = infoModuleService;
        this.questModuleService = questModuleService;

    }

    @PostMapping("/addCourse")
    public void addCourse(@RequestBody CourseRequest course) {
       courseService.addNewCourse(course);
    }

    @GetMapping("/getCourses/{id}/info_module")
    public ResponseEntity<?> getCourseInfoModule(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(infoModuleService.findByCourseId(id));
    }

    @GetMapping("/getQuestModule/{questModuleId}")
    public ResponseEntity<?> getCourseQuestModule(@PathVariable(value = "questModuleId") Integer questModuleId) {
        return ResponseEntity.ok(questModuleService.findQuestModuleById(questModuleId));
    }

    @PostMapping("/addQuestModule/{courseId}")
    public void addQuestModule(@RequestBody QuestModule questModule, @PathVariable(value = "courseId") Integer courseId) {
        questModuleService.saveQuestModule(questModule,courseId);
    }


}
