package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.CourseDAO;
import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.dtos.CourseMapResponse;
import com.example.jwtAuth.dtos.CourseResponse;
import com.example.jwtAuth.dtos.ShortCourseDto;
import com.example.jwtAuth.models.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssignmentService {
    private final UserDAO userDAO;

    private final CourseDAO courseDAO;

    private final QuestModuleDAO questModuleDAO;

    public AssignmentService(UserDAO userDAO, CourseDAO courseDAO, QuestModuleDAO questModuleDAO) {
        this.userDAO = userDAO;
        this.courseDAO = courseDAO;
        this.questModuleDAO = questModuleDAO;
    }
    public void assignCourseForUsers(List<Integer> userIds, int courseId) {
        for (Integer userId : userIds) {
            courseDAO.addUserCourse(userId, courseId);
        }
    }

    /*public Map<String, CourseMapResponse> getAssignedCoursesForUserByLevel(int levelId) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        user.setId(((ExtendUserDetails) authentication.getPrincipal()).getId());
        user.setCourses(courseDAO.getUserCoursesByLevel(user.getId(), levelId));
        Map<String, Pair<Integer, Integer>> scores = new HashMap<>();
        Map<String, CourseMapResponse> courseMap = new HashMap<>();
        for (UserCourse userCourse : user.getCourses()) {
            Pair<Integer, Integer> courseScore = questModuleDAO.getUserQuestModuleScores(userCourse.getId(), user.getId());
            String direction = userCourse.getDirection().getName();
            CourseMapResponse course = courseMap.get(direction);
            if (course == null) {
                course = new CourseMapResponse();
                List<ShortCourseDto> newList= new ArrayList<>();
                course.setCourses(newList);
                scores.put(direction, new Pair<>(0, 0));
            }
            List<ShortCourseDto> shortCourses = course.getCourses();
            shortCourses.add(new ShortCourseDto(userCourse.getId(), userCourse.getName()));
            course.setCourses(shortCourses);
            courseMap.put(direction, course);

            Integer userScore = scores.get(direction).a + courseScore.a;
            Integer totalScore = scores.get(direction).b + courseScore.b;
            scores.put(direction, new Pair<>(userScore, totalScore));
        }
        for(String direction:scores.keySet()){
            if(scores.get(direction).b==0){
                courseMap.get(direction).setUserScores(100);
            }else {
                Integer userScore = scores.get(direction).a / scores.get(direction).b * 100;
                courseMap.get(direction).setUserScores(userScore);
            }
        }
        return courseMap;
    }*/
    public Map<String, CourseMapResponse> getAssignedCoursesForUserByLevel(int levelId) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        user.setId(((ExtendUserDetails) authentication.getPrincipal()).getId());
        user.setCourses(courseDAO.getUserCoursesByLevel(user.getId(), levelId));
        Map<String, CourseMapResponse> courseMap= unionCoursesByDirection(user);
        for(String direction:courseMap.keySet()){
            courseMap.get(direction).setUserScores(calculateScoresForDirection(courseMap.get(direction),user));
        }
        return courseMap;
    }

    public CourseResponse getOneAssignedCourseForUserByCourseId(int courseId) {
        CourseResponse courseResponse = new CourseResponse();
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        user.setId(((ExtendUserDetails) authentication.getPrincipal()).getId());
        UserCourse course = new UserCourse(courseDAO.getCourseById(courseId));
        course.setQuestModules(questModuleDAO.getUserQuestModules(courseId, user.getId()));
        courseResponse = CourseToCourseResponse(course);
        Pair<Integer,Integer> userScores = calculateUserScoresForCourse(course);
        courseResponse.setUserScore(userScores.a);
        courseResponse.setTotalScore(userScores.b);
        return courseResponse;
    }

    private Map<String,CourseMapResponse> unionCoursesByDirection(User user){
        Map<String, CourseMapResponse> courseMap = new HashMap<>();
        for (UserCourse userCourse : user.getCourses()) {
            String direction = userCourse.getDirection().getName();
            CourseMapResponse course = courseMap.get(direction);
            if (course == null) {
                course = new CourseMapResponse();
                List<ShortCourseDto> newList= new ArrayList<>();
                course.setCourses(newList);
            }
            List<ShortCourseDto> shortCourses = course.getCourses();
            shortCourses.add(new ShortCourseDto(userCourse.getId(), userCourse.getName()));
            course.setCourses(shortCourses);
            courseMap.put(direction, course);
        }
        return courseMap;
    }

    private Integer calculateScoresForDirection(CourseMapResponse courseMap,User user){
        Integer userScores=0;
        Integer totalScore=0;
        Integer result=0;
        List<ShortCourseDto> shortCourses = courseMap.getCourses();
        for(ShortCourseDto course:shortCourses) {
            Pair<Integer, Integer> courseScore = questModuleDAO.getUserQuestModuleScores(course.getId(), user.getId());
            userScores+=courseScore.a;
            totalScore+=courseScore.b;
        }
        if(totalScore==0){
            result=100;
        }else {
            result=userScores/totalScore * 100;
        }
       return result;
    }

    private CourseResponse CourseToCourseResponse(UserCourse course){
        CourseResponse courseResponse = new CourseResponse();
        List<Integer> questModuleIds = new ArrayList<>();
        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setDescription(course.getDescription());
        if(course.getInfoModule()!=null) {
            courseResponse.setInfoModuleIds(course.getInfoModule().getId());
        }
        for (UserQuestModule userQuestModule : course.getQuestModules()) {
            if(userQuestModule.getId()!=null) {
                questModuleIds.add(userQuestModule.getId());
            }
        }
        courseResponse.setQuestModuleIds(questModuleIds);
        return courseResponse;
    }
    private Pair<Integer,Integer> calculateUserScoresForCourse(UserCourse course){
       Integer userScore=0;
       Integer totalScore=0;
        for (UserQuestModule userQuestModule : course.getQuestModules()) {
            if(userQuestModule.getId()!=null) {
                userScore+=userQuestModule.getScores();
                totalScore += userQuestModule.getUserScore();

            }
        }
        return new Pair<Integer,Integer>(userScore,totalScore);
    }

    public List<ShortCourseDto> getAssignedCoursesForUserByLevelAndDirection(Integer level, Integer direction, Integer userId) {
        List<Course> courses = courseDAO.getUserCoursesByLevelAndDirection(level, direction, userId);
        List<ShortCourseDto> shortCourseDtos= new ArrayList<>();
        for(Course course:courses){
            ShortCourseDto shortCourseDto = new ShortCourseDto(course.getId(),course.getName());
            shortCourseDtos.add(shortCourseDto);
        }
        return shortCourseDtos;
    }

    public Integer calculateScoresForUserCourse(Integer id, List<ShortCourseDto> shortCourseDtos) {
        Integer userScores=0;
        Integer totalScore=0;
        Integer result=0;
        for(ShortCourseDto course:shortCourseDtos) {
            Pair<Integer, Integer> courseScore = questModuleDAO.getUserQuestModuleScores(course.getId(), id);
            userScores+=courseScore.a;
            totalScore+=courseScore.b;
        }
        if(totalScore==0){
            result=100;
        }else {
            result=userScores/totalScore * 100;
        }
        return result;
    }

   public List<ShortCourseDto> getAssignedCoursesForUser(Integer userId) {
       return courseDAO.getUserCourses(userId);
   }
}

