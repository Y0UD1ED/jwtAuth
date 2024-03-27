package com.example.jwtAuth.controllers;

import com.example.jwtAuth.models.Course;
import com.example.jwtAuth.models.InfoModule;
import com.example.jwtAuth.models.QuestModule;
import com.example.jwtAuth.services.CourseService;
import com.example.jwtAuth.services.InfoModuleService;
import com.example.jwtAuth.services.QuestModuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {

    private final CourseService courseService;

    private final InfoModuleService infoModuleService;
    private final QuestModuleService questModuleService;

    public CourseController(CourseService courseService, InfoModuleService infoModuleService, QuestModuleService questModuleService) {
        this.courseService = courseService;
        this.infoModuleService = infoModuleService;
        this.questModuleService = questModuleService;
    }

    @PostMapping("/addCourse")
    public void addCourse(@RequestBody Course course) {
        courseService.save(course);
    }

    @GetMapping("/getCourses")
    public ResponseEntity<?> getCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }
    @GetMapping("/getCourses/{id}")
    public ResponseEntity<?> getCourse(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @GetMapping("/getCourses/{id}/info_module")
    public ResponseEntity<?> getCourseInfoModule(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(infoModuleService.findByCourseId(id));
    }


    @PostMapping("/addInfoModule")
    public void addInfoModule(@RequestBody List<InfoModule> infoModule) {
        infoModuleService.save(infoModule);
    }

    @GetMapping("/getCourses/{id}/quest_module")
    public ResponseEntity<?> getCourseQuestModule(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(questModuleService.findByCourseId(id));

    }

    @PostMapping("/addQuestModule")
    public void addQuestModule(@RequestBody List<QuestModule> questModule) {
        questModuleService.save(questModule);
    }
}
