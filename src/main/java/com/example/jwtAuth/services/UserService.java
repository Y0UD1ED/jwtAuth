package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.dtos.CourseMapResponse;
import com.example.jwtAuth.dtos.CourseResponse;
import com.example.jwtAuth.dtos.ShortCourseDto;
import com.example.jwtAuth.models.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    private final BonusesService bonusesService;
    private final CourseService courseService;

    private final QuestModuleService questModuleService;


    public UserService(UserDAO userDAO, CourseService courseService, QuestModuleService questModuleService, BonusesService bonusesService) {
        this.userDAO = userDAO;
        this.courseService = courseService;
        this.questModuleService = questModuleService;
        this.bonusesService = bonusesService;
    }

    public User findByUsername(String username) {
        User user = userDAO.getUserByLogin(username);
        user.setRoles(userDAO.getUsersRoles(user.getId()));
        return user;
    }

    public void createUser(User user) throws AuthenticationException {
        boolean check = userDAO.isUserExist(user.getLogin());
        if (check) {
            throw new AuthenticationException(String.format("User '%s' already exists", user.getLogin()));
        }
        user.setBalance(0);
        Integer userId = userDAO.addUser(user);
        userDAO.setUserRole(userId);

    }

    public void createFullUser(User user) {
        boolean check = userDAO.isUserExist(user.getLogin());
        if (check) {
            throw new UsernameNotFoundException(String.format("User '%s' already exists", user.getLogin()));
        }
        Integer userId = userDAO.addFullUser(user);
        userDAO.setUserRole(userId);
    }

    public User updateUser(User userUpdated) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        ExtendUserDetails user = (ExtendUserDetails) authentication.getPrincipal();
        boolean check = userDAO.isUserExist(user.getId());
        if (!check) {
            throw new UsernameNotFoundException(String.format("User '%s' not founded", user.getUsername()));
        }
        userDAO.updateUser(userUpdated);
        return userUpdated;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean check = userDAO.isUserExist(username);
        if (!check) {
            throw new UsernameNotFoundException(String.format("User '%s' not founded", username));
        }
        User user = findByUsername(username);
        return new ExtendUserDetails(user.getId(), user.getLogin(), user.getPassword(), user.getRoles());
    }

    private Collection<? extends GrantedAuthority> mapRolesToAth(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).toList();
    }


    public Map<String, CourseMapResponse> getCourseMap(int levelId) {
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        user.setId(((ExtendUserDetails) authentication.getPrincipal()).getId());
        user.setCourses(userDAO.getUserCoursesByLevel(user.getId(), levelId));
        Map<String, Pair<Integer, Integer>> scores = new HashMap<>();
        Map<String, CourseMapResponse> courseMap = new HashMap<>();
        for (UserCourse userCourse : user.getCourses()) {
            Pair<Integer, Integer> courseScore = userDAO.getUserQuestModuleScores(userCourse.getId(), user.getId());
            String direction = userCourse.getDirection().getName();
            CourseMapResponse course = courseMap.get(direction);
            if (course == null) {
                course = new CourseMapResponse();
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
        return courseMap;
    }


    public CourseResponse getUserCourse(int courseId) {
        CourseResponse courseResponse = new CourseResponse();
        JwtAuthentication authentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        user.setId(((ExtendUserDetails) authentication.getPrincipal()).getId());
        UserCourse course = new UserCourse(courseService.findByCourseId(courseId));
        course.setQuestModules(userDAO.getUserQuestModules(courseId, user.getId()));
        List<Integer> questModuleIds = new ArrayList<>();
        Integer totalScore = 0;
        Integer userScore = 0;
        courseResponse.setId(course.getId());
        courseResponse.setName(course.getName());
        courseResponse.setDescription(course.getDescription());
        courseResponse.setInfoModuleIds(course.getInfoModule().getInfoId());
        for (UserQuestModule userQuestModule : course.getQuestModules()) {
            questModuleIds.add(userQuestModule.getQuestModuleId());
            totalScore += userQuestModule.getScores();
            userScore += userQuestModule.getUserScore();
        }
        courseResponse.setQuestModuleIds(questModuleIds);
        courseResponse.setTotalScore(totalScore);
        courseResponse.setUserScore(userScore);
        return courseResponse;
    }

    public Integer passQuestModule(Integer moduleId, Map<Integer,Integer> answers) throws AuthenticationException {
        JwtAuthentication authentication= (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) authentication.getPrincipal()).getId();
        boolean check=userDAO.isUserPassedTest(moduleId,userId);
        if(!check){
            QuestModule questModule=questModuleService.findQuestModuleById(moduleId);
            List<Test> testList=questModule.getTests();
            Integer usersScore=0;
            Integer totalScore=questModule.getScores();
            Integer totalQuestions=testList.size();
            Integer correctAnswers=0;
            for(Test test:testList){
                Integer questionId=test.getTestId();
                Integer questionAnswer=answers.get(questionId);
                if(test.getOptionList().get(questionAnswer).getCorrect()){
                    correctAnswers++;
                }
            }
            usersScore=correctAnswers*totalScore/totalQuestions;
            userDAO.addUserQuestModule(userId,moduleId,usersScore);
            return correctAnswers;
        }
        throw new AuthenticationException("You have already passed this quest module");
    }

    public void buyBonus(int bonusId) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        User user=userDAO.getUserById(userId);
        Bonus bonus = bonusesService.getBonusById(bonusId);
        Integer price=bonus.getPrice();
        Integer balance=user.getBalance();
        if(balance<price) {
            throw new RuntimeException("Not enough money");
        }
        user.setBalance(balance-price);
        userDAO.updateUser(user);
        userDAO.addBonus(userId,bonusId);
    }

    public List<Bonus> getMyBonuses() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        List<Bonus> bonuses = userDAO.getUserBonuses(userId);
        //mapper
        return bonuses;
    }
}
