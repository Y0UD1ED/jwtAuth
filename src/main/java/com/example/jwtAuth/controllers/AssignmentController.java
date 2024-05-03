package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.PassingDto;
import com.example.jwtAuth.mappers.AssignmentMapper;
import com.example.jwtAuth.models.UsersCourses;
import com.example.jwtAuth.services.QuestModulesPassingService;
import com.example.jwtAuth.services.AssignmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("setCourse")
public class AssignmentController {
    private final QuestModulesPassingService modulePassingService;
    private final AssignmentService assignmentService;
    private final AssignmentMapper assignmentMapper;

    public AssignmentController(QuestModulesPassingService modulePassingService, AssignmentService assignmentService, AssignmentMapper assignmentMapper) {
        this.modulePassingService = modulePassingService;
        this.assignmentService = assignmentService;
        this.assignmentMapper = assignmentMapper;
    }

    @PostMapping("/forOneUser")
    public void setCourseForOneUser(@RequestBody PassingDto passingDto) {
        UsersCourses usersCourses = assignmentMapper.passingDtoToPassing(passingDto);
        assignmentService.setCourseForOneUser(usersCourses);
    }

    @PostMapping("/byLevel")
    public void setCourseByLevel(@RequestBody PassingDto passingDto) {
        UsersCourses usersCourses = assignmentMapper.passingDtoToPassing(passingDto);
        assignmentService.setCourseForUserByLevelId(usersCourses, passingDto.getLevelId());
    }

    @PostMapping("/byDirection")
    public void setCourseByDirection(@RequestBody PassingDto passingDto) {
        UsersCourses usersCourses = assignmentMapper.passingDtoToPassing(passingDto);
        assignmentService.setCourseForUserByDirectionId(usersCourses, passingDto.getDirectionId());
    }

    @PostMapping("/forAllUsers")
    public void setCourseForAllUsers(@RequestBody PassingDto passingDto) {
        UsersCourses usersCourses = assignmentMapper.passingDtoToPassing(passingDto);
        assignmentService.setCourseForAllUsers(usersCourses);
    }

    @PostMapping("/byDirectionAndLevel")
    public void setCourseByDirectionAndLevel(@RequestBody PassingDto passingDto) {
        UsersCourses usersCourses = assignmentMapper.passingDtoToPassing(passingDto);
        assignmentService.setCourseForUserByDirectionIdAndLevelId(usersCourses, passingDto.getDirectionId(), passingDto.getLevelId());
    }



}
