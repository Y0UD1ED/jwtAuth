package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.CourseDAO;
import com.example.jwtAuth.dao.DirectionDAO;
import com.example.jwtAuth.dao.LevelDAO;
import com.example.jwtAuth.dao.UsersCoursesDAO;
import com.example.jwtAuth.dtos.CourseMapDto;
import com.example.jwtAuth.dtos.CourseResponse;
import com.example.jwtAuth.dtos.RoadMapResponse;
import com.example.jwtAuth.dtos.UsersCourseDto;
import com.example.jwtAuth.models.Course;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.UsersCourses;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CourseService {
    private final CourseDAO courseDAO;
    private final LevelDAO levelDAO;
    private final DirectionDAO directionDAO;

    private  final UsersCoursesDAO usersCoursesDAO;
    private final AssignmentService assignmentService;

    public CourseService(CourseDAO courseDAO, LevelDAO levelDAO, DirectionDAO directionDAO, AssignmentService assignmentService, UsersCoursesDAO usersCoursesDAO) {
        this.courseDAO = courseDAO;
        this.levelDAO = levelDAO;
        this.directionDAO = directionDAO;
        this.assignmentService=assignmentService;
        this.usersCoursesDAO=usersCoursesDAO;
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
        HashMap<String,List<CourseMapDto>> map=new HashMap<>();
        for(UsersCourseDto usersCourseDto:usersCourseDtoList){
            String key=usersCourseDto.getDirection().getName();
            List<CourseMapDto> current = map.get(key);
            if (current == null) {
                current = new ArrayList<CourseMapDto>();
                map.put(key, current);
            }
            CourseMapDto courseMapDto=new CourseMapDto();
            courseMapDto.setCourseName(usersCourseDto.getCourseName());
            courseMapDto.setScore(usersCourseDto.getScore());
            courseMapDto.setCourseId(usersCourseDto.getCourseId());
            courseMapDto.setTotalScore(usersCourseDto.getTotalScore());
            current.add(courseMapDto);
        }
        List<RoadMapResponse> roadMapResponses=new ArrayList<RoadMapResponse>();
        for(String key:map.keySet()){
            RoadMapResponse roadMapResponse=new RoadMapResponse();
            roadMapResponse.setDirection(key);
            roadMapResponse.setCourses(map.get(key));
            Integer user_scores=0;
            Integer total_scores=0;
            for(CourseMapDto courseMapDto:map.get(key)){
                user_scores+=courseMapDto.getScore();
                total_scores+=courseMapDto.getTotalScore();
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

    public Integer save(Course course) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        course.setId(courseDAO.addCourse(course, userId));
        /*UsersCourses usersCourses = new UsersCourses();
        usersCourses.setCourseId(course.getId());
        assignmentService.setCourseForUserByDirectionIdAndLevelId(usersCourses, course.getDirection().getId(), course.getLevel().getId());
        */
        return course.getId();
    }


}
