package com.example.jwtAuth.dao;



import com.example.jwtAuth.dtos.CourseResponse;
import com.example.jwtAuth.dtos.UsersCourseDto;
import com.example.jwtAuth.models.Direction;
import com.example.jwtAuth.models.UsersCourses;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersCoursesDAO {

    private final JdbcTemplate jdbcTemplate;

    public UsersCoursesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UsersCourseDto> getAllUserCoursesByLevelAndUserId(int levelId, int userId){
        /*String sql="SELECT courses.id,courses.name course_name,courses.direction_id,directions.name direction_name, users_passing.scores, courses.scores" +
                "FROM users_courses JOIN users ON users_courses.user_id=users.id " +
                "JOIN courses ON users_courses.course_id=courses.id " +
                "JOIN directions ON courses.direction_id=directions.id " +
                "WHERE users_courses.user_id=? AND courses.level_id=?";

         */
        String sql="SELECT courses.id, courses.name course_name, COALESCE(SUM(quest_modules.scores),0) course_score, courses.direction_id, directions.name direction_name,COALESCE(SUM(quest_modules_passing.scores),0) user_scores\n" +
                "FROM users_courses JOIN users ON users_courses.user_id=users.id\n" +
                "JOIN courses ON users_courses.course_id=courses.id\n" +
                "JOIN directions ON courses.direction_id=directions.id\n" +
                "LEFT JOIN quest_modules ON courses.id=quest_modules.course_id\n" +
                "LEFT JOIN quest_modules_passing ON quest_modules_passing.user_id=users.id AND quest_modules_passing.quest_module_id=quest_modules.id\n" +
                "WHERE users_courses.user_id=? AND courses.level_id=?\n" +
                "GROUP BY courses.id,courses.direction_id,directions.name";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            UsersCourseDto usersCourseDto = new UsersCourseDto();
            usersCourseDto.setCourseId(rs.getInt("id"));
            usersCourseDto.setCourseName(rs.getString("course_name"));
            usersCourseDto.setScore(rs.getInt("user_scores"));
            usersCourseDto.setTotalScore(rs.getInt("course_score"));
            usersCourseDto.setDirection(new Direction(rs.getInt("direction_id"),rs.getString("direction_name")));
            return usersCourseDto;
        },userId,levelId);
    }

    public CourseResponse getUserCourseByCourseId(int courseId,int userId){
        String sql="SELECT courses.id course_id,courses.name course_name, info_modules.id info_modules_id FROM users_courses " +
                "JOIN courses ON users_courses.course_id=courses.id " +
                "JOIN users ON users_courses.user_id=users.id " +
                "JOIN info_modules ON courses.id=info_modules.course_id " +
                "WHERE users_courses.course_id=? AND users_courses.user_id=?";
        return jdbcTemplate.queryForObject(sql,(rs,rowNum) -> {
            CourseResponse courseResponse = new CourseResponse();
            courseResponse.setCourseId(rs.getInt("course_id"));
            courseResponse.setCourseName(rs.getString("course_name"));
            courseResponse.setInfoModuleId(rs.getInt("info_modules_id"));
            return courseResponse;
        },courseId,userId);
    }
    public List<UsersCourses> getPassingByUserId(int userId){
        String sql="SELECT * FROM passing WHERE user_id=?";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            UsersCourses usersCourses=new UsersCourses();
            usersCourses.setCourseId(rs.getInt("course_id"));
            usersCourses.setUserId(rs.getInt("user_id"));
            return usersCourses;
        },userId);
    }

    public List<UsersCourses> getPassingByCourseId(int courseId){
        String sql="SELECT * FROM passing WHERE course_id=?";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            UsersCourses usersCourses=new UsersCourses();
            usersCourses.setCourseId(rs.getInt("course_id"));
            usersCourses.setUserId(rs.getInt("user_id"));
            return usersCourses;
        },courseId);
    }
    public void save(UsersCourses usersCourses){
        String sql="INSERT INTO passing(course_id,user_id,scores) VALUES(?,?,0)";
        jdbcTemplate.update(sql,usersCourses.getCourseId(),usersCourses.getUserId());
    }

}
