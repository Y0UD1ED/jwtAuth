package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.CourseDAO;
import com.example.jwtAuth.dao.DirectionDAO;
import com.example.jwtAuth.dao.LevelDAO;
import com.example.jwtAuth.dao.UsersCoursesDAO;
import com.example.jwtAuth.dtos.*;
import com.example.jwtAuth.models.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CourseService {
    private final CourseDAO courseDAO;

    private final InfoModuleService infoModuleService;
    private final LevelDAO levelDAO;
    private final DirectionDAO directionDAO;

    private  final UsersCoursesDAO usersCoursesDAO;
    private final AssignmentService assignmentService;

    public CourseService(CourseDAO courseDAO, LevelDAO levelDAO, DirectionDAO directionDAO, AssignmentService assignmentService, UsersCoursesDAO usersCoursesDAO, InfoModuleService infoModuleService) {
        this.courseDAO = courseDAO;
        this.levelDAO = levelDAO;
        this.directionDAO = directionDAO;
        this.assignmentService=assignmentService;
        this.usersCoursesDAO=usersCoursesDAO;
        this.infoModuleService=infoModuleService;
    }

    public List<Course> findAll(){
        List<Course> list=courseDAO.getAllCourses();
        for(Course course:list){
            course.setDirection(directionDAO.getDirectionById(course.getDirection().getId()));
            course.setLevel(levelDAO.getById(course.getLevel().getId()));
        }
        return list;
    }


    public CourseResponse findUserCourseById(Integer id){
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        CourseResponse courseResponse=usersCoursesDAO.getUserCourseByCourseId(id,userId);
        return courseResponse;
    }


    public List<RoadMapResponse> findAllCoursePassingByLevelAndUserId(Integer level){
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        List<UsersCourseDto> usersCourseDtoList= usersCoursesDAO.getAllUserCoursesByLevelAndUserId(level, userId);
        HashMap<String,List<CourseMapDtoDel>> map=new HashMap<>();
        for(UsersCourseDto usersCourseDto:usersCourseDtoList){
            String key=usersCourseDto.getDirection().getName();
            List<CourseMapDtoDel> current = map.get(key);
            if (current == null) {
                current = new ArrayList<CourseMapDtoDel>();
                map.put(key, current);
            }
            CourseMapDtoDel courseMapDtoDel =new CourseMapDtoDel();
            courseMapDtoDel.setCourseName(usersCourseDto.getCourseName());
            courseMapDtoDel.setScore(usersCourseDto.getScore());
            courseMapDtoDel.setCourseId(usersCourseDto.getCourseId());
            courseMapDtoDel.setTotalScore(usersCourseDto.getTotalScore());
            current.add(courseMapDtoDel);
        }
        List<RoadMapResponse> roadMapResponses=new ArrayList<RoadMapResponse>();
        for(String key:map.keySet()){
            RoadMapResponse roadMapResponse=new RoadMapResponse();
            roadMapResponse.setDirection(key);
            roadMapResponse.setCourses(map.get(key));
            Integer user_scores=0;
            Integer total_scores=0;
            for(CourseMapDtoDel courseMapDtoDel :map.get(key)){
                user_scores+= courseMapDtoDel.getScore();
                total_scores+= courseMapDtoDel.getTotalScore();
            }
            if(total_scores==0)
                roadMapResponse.setPassing(100);
            else{
                roadMapResponse.setPassing((Integer) user_scores/total_scores*100);

            }
            roadMapResponses.add(roadMapResponse);
        }

        return roadMapResponses;
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


}
