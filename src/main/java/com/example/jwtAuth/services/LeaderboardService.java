package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.dtos.LeaderboardDto;
import com.example.jwtAuth.dtos.ShortCourseDto;
import com.example.jwtAuth.dtos.StatiscticDto;
import com.example.jwtAuth.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardService {


    private final AssignmentService assignmentService;
    private final UserDAO userDAO;

    public LeaderboardService(AssignmentService assignmentService, UserDAO userDAO) {
        this.assignmentService = assignmentService;
        this.userDAO = userDAO;
    }


    public List<LeaderboardDto> getLeaderboardByLevelAndDirection(Integer level, Integer direction) {
        List<User> users=userDAO.getAllUsersByLevelAndDirection(level, direction);
        List<LeaderboardDto> leaderboardDtos = new ArrayList<>();
        for(User user:users){
            LeaderboardDto leaderboardDto = new LeaderboardDto();
            leaderboardDto.setUserId(user.getId());
            leaderboardDto.setFirstName(user.getFirstName());
            leaderboardDto.setLastName(user.getLastName());
            List<ShortCourseDto> shortCourseDtos = assignmentService.getAssignedCoursesForUserByLevelAndDirection(level, direction, user.getId());
            leaderboardDto.setUsersCourses(shortCourseDtos);
            leaderboardDto.setScore(assignmentService.calculateCoursesPassingForUser(user.getId(),shortCourseDtos));
            leaderboardDtos.add(leaderboardDto);
        }
        leaderboardDtos.sort(this::compare);
        return leaderboardDtos;

    }

    public List<StatiscticDto> getAllLeaderboard() {
        List<User> users=userDAO.getAllUsers();
        List<StatiscticDto> statiscticDtos = new ArrayList<>();
        for(User user:users){
            StatiscticDto statiscticDto = new StatiscticDto();
            statiscticDto.setUserId(user.getId());
            statiscticDto.setFirstName(user.getFirstName());
            statiscticDto.setLastName(user.getLastName());
            statiscticDto.setMiddleName(user.getMiddleName());
            statiscticDto.setLevel(user.getCurrentLevel().getName());
            List<ShortCourseDto> coursesForUser= assignmentService.getAssignedCoursesForUser(user.getId());
            statiscticDto.setScores(assignmentService.calculateCoursesPassingForUser(user.getId(),coursesForUser));
            statiscticDtos.add(statiscticDto);
        }

        return  statiscticDtos;

    }

    private int compare(LeaderboardDto l1, LeaderboardDto l2) {
        return Integer.compare(l1.getScore(), l2.getScore());
    }
}
