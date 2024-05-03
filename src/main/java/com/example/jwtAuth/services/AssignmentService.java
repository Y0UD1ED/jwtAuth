package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.UsersCoursesDAO;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.models.UsersCourses;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {
    private final UsersCoursesDAO usersCoursesDAO;

    private final UserDAO userDAO;

    public AssignmentService(UsersCoursesDAO usersCoursesDAO, UserDAO userDAO) {
        this.usersCoursesDAO = usersCoursesDAO;
        this.userDAO = userDAO;
    }

    public void setCourseForOneUser(UsersCourses usersCourses) {
        usersCoursesDAO.save(usersCourses);
    }

    public void setCourseForAllUsers(UsersCourses usersCourses) {
        List<Integer> usersId=userDAO.getAllUsersId();
        for (Integer userId : usersId) {
            usersCourses.setUserId(userId);
            setCourseForOneUser(usersCourses);
        }
    }

    public void setCourseForUserByLevelId(UsersCourses usersCourses, int levelId) {
        List<Integer> usersId=userDAO.getAllUsersIdByLevel(levelId);
        for (Integer userId : usersId) {
            usersCourses.setUserId(userId);
            setCourseForOneUser(usersCourses);
        }
    }

    public void setCourseForUserByDirectionId(UsersCourses usersCourses, int directionId) {
        List<Integer> usersId=userDAO.getAllUsersIdByDirection(directionId);
        for (Integer userId : usersId) {
            usersCourses.setUserId(userId);
            setCourseForOneUser(usersCourses);
        }
    }

    public void setCourseForUserByDirectionIdAndLevelId(UsersCourses usersCourses, int directionId,int levelId) {
        List<Integer> usersId=userDAO.getAllUsersIdByLevelAndDirection(levelId,directionId);
        for (Integer userId : usersId) {
            usersCourses.setUserId(userId);
            setCourseForOneUser(usersCourses);
        }
    }



}
