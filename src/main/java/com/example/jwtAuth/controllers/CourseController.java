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
        Course course1 = new Course();
        course1.setName(course.getName());
        course1.setLevel(new Level(course.getLevelId()));
        course1.setDirection(new Direction(course.getDirectionId()));
        course1.setDescription(course.getDescription());
        Integer courseId=courseService.save(course1);
        /*InfoModule courseInfoModule = new InfoModule();
        courseInfoModule.setCourseId(courseId);
        courseInfoModule.setContentType("text");
        courseInfoModule.setContentPosition(1);
        courseInfoModule.setContent(course.getInfoModuleText());
        infoModuleService.saveOneInfoModule(courseInfoModule);*/
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


    @PostMapping("/addInfoModule")
    public void addInfoModule(@RequestBody List<InfoModule> infoModule) {
        infoModuleService.save(infoModule);
    }

    /*@GetMapping("/getCourses/{id}/quest_module")
    public ResponseEntity<?> getCourseQuestModule(@PathVariable(value = "id") Integer id) {
        return ResponseEntity.ok(testsService.findByQuestModuleId(id));

    }*/

    @PostMapping("/addQuestModule")
    public void addQuestModule(@RequestBody QuestModule questModule) {
        questModuleService.saveQuestModule(questModule);
    }

    @PostMapping("/questModule/{id}/pass")
    public void passQuestModule(@PathVariable(value = "id") Integer id, @RequestBody Map<Integer,Integer> answers) {
        questModulesPassingService.passQuestModule(id,answers);
    }
}
