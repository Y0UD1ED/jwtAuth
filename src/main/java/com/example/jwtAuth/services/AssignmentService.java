package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.UserDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {
    private final UserDAO userDAO;

    public AssignmentService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public void setCourseForUsers(List<Integer> userIds, int courseId) {
        for (Integer userId : userIds) {
            userDAO.setUserCourse(userId, courseId);
        }
    }
}

