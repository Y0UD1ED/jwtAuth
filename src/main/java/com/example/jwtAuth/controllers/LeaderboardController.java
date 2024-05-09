package com.example.jwtAuth.controllers;

import com.example.jwtAuth.dtos.LeaderboardDto;
import com.example.jwtAuth.dtos.StatiscticDto;
import com.example.jwtAuth.models.CoursePassing;
import com.example.jwtAuth.services.LeaderboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /*@GetMapping("leaderboard/allUsers/byDirectionAndLevel/{direction}/{level}")
    public ResponseEntity<?> getPassingByDirectionAndLevel(@PathVariable(value = "direction") Integer direction, @PathVariable(value = "level") Integer level) {
        List<CoursePassing> passing = leaderboardService.findAllCoursePassingByLevelAndDirection(level, direction);
        return ResponseEntity.ok(passing);
    }*/

    @GetMapping("leaderboard/byDirectionAndLevel/{direction}/{level}")
    public ResponseEntity<?> getLeaderboardByDirectionAndLevel(@PathVariable(value = "direction") Integer direction, @PathVariable(value = "level") Integer level) {
        List<LeaderboardDto> leaderboard = leaderboardService.getLeaderboardByLevelAndDirection(level, direction);
        return ResponseEntity.ok(leaderboard);
    }


    @GetMapping("statistic/allUsers")
    public ResponseEntity<?> getStatisticForAllUsers() {
       List<StatiscticDto> stat=leaderboardService.getAllLeaderboard();
        return ResponseEntity.ok(stat);
    }


}
