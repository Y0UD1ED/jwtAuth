package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.CourseRequest;
import com.example.jwtAuth.dtos.CourseResponse;
import com.example.jwtAuth.models.*;
import com.example.jwtAuth.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CourseController {

    private final CourseService courseService;

    private final QuestModuleService questModuleService;

    private final QuestModulesPassingService questModulesPassingService;

    private final InfoModuleService infoModuleService;
    private final TestsService testsService;

    public CourseController(CourseService courseService, InfoModuleService infoModuleService, TestsService testsService, QuestModuleService questModuleService, QuestModulesPassingService questModulesPassingService) {
        this.courseService = courseService;
        this.infoModuleService = infoModuleService;
        this.testsService = testsService;
        this.questModuleService = questModuleService;
        this.questModulesPassingService = questModulesPassingService;
    }

    @PostMapping("/addCourse")
    public void addCourse(@RequestBody CourseRequest course) {
       courseService.addNewCourse(course);
    }

    @GetMapping("/getCoursesByLevel/{levelId}")
    public ResponseEntity<?> getCourses(@PathVariable(value = "levelId") Integer levelId) {
        return ResponseEntity.ok(courseService.findAllCoursePassingByLevelAndUserId(levelId));
    }
    @GetMapping("/getCourses/{id}")
    public ResponseEntity<?> getCourse(@PathVariable(value = "id") Integer id) {
        CourseResponse courseResponse=courseService.findUserCourseById(id);
        Map<String,Object> map= questModuleService.findQuestModulesIdAndTotalScoresByCourseId(id);
        courseResponse.setQuestModuleIds((List<Integer>) map.get("testsId"));
        courseResponse.setTotalScore((Integer) map.get("totalScore"));
        courseResponse.setScores(questModuleService.getUserScoreForCourse(id));
        return ResponseEntity.ok(courseResponse);

    }

    @GetMapping("/getCourses/{id}/info_module")
    public ResponseEntity<?> getCourseInfoModule(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(infoModuleService.findByCourseId(id));
    }

    @GetMapping("/getQuestModule/{questModuleId}")
    public ResponseEntity<?> getCourseQuestModule(@PathVariable(value = "questModuleId") Integer questModuleId) {
        return ResponseEntity.ok(questModuleService.findQuestModuleById(questModuleId));
    }

    @PostMapping("/addQuestModule")
    public void addQuestModule(@RequestBody QuestModule questModule) {
        questModuleService.saveQuestModule(questModule);
    }

    @PostMapping("/questModule/{id}/pass")
    public void passQuestModule(@PathVariable(value = "id") Integer id, @RequestBody Map<Integer,Integer> answers) {
        questModulesPassingService.passQuestModule(id,answers);
    }
}
