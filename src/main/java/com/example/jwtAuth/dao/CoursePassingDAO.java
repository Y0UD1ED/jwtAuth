package com.example.jwtAuth.dao;


import com.example.jwtAuth.models.CoursePassing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CoursePassingDAO {
    private final JdbcTemplate jdbcTemplate;

    public CoursePassingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CoursePassing> getAllCoursePassingByLevelAndDirection(int levelId, int directionId){
        String sql="SELECT users.second_name,users.first_name, users_courses.user_id,COALESCE(SUM(quest_modules_passing.scores),0) user_scores,COALESCE(SUM(quest_modules.scores),0) total_scores FROM users_courses \n" +
                "JOIN courses ON users_courses.course_id=courses.id\n" +
                "JOIN users ON users_courses.user_id=users.id\n" +
                "LEFT JOIN quest_modules ON quest_modules.course_id=courses.id \n" +
                "LEFT JOIN quest_modules_passing ON quest_modules_passing.quest_module_id=quest_modules.id AND quest_modules_passing.user_id=users.id\n" +
                "WHERE courses.level_id=? AND courses.direction_id=?\n" +
                "GROUP BY (users_courses.user_id,users.second_name,users.first_name)";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            CoursePassing coursePassing = new CoursePassing();
            coursePassing.setSecondName(rs.getString("second_name"));
            coursePassing.setFirstName(rs.getString("first_name"));
            coursePassing.setUserId(rs.getInt("user_id"));
            coursePassing.setUserScores(rs.getInt("user_scores"));
            coursePassing.setTotalScores(rs.getInt("total_scores"));
            return coursePassing;
        },levelId,directionId);
    }


}
