package com.example.jwtAuth.dao;

import com.example.jwtAuth.dtos.ShortCourseDto;
import com.example.jwtAuth.models.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CourseDAO {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CourseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("courses").usingGeneratedKeyColumns("id");
    }

    public List<Course> getAllCourses(){
        String sql="SELECT * FROM courses";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            Course course=new Course();
            course.setId(rs.getInt("id"));
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setLevel(new Level(rs.getInt("level_id")));
            course.setDirection(new Direction(rs.getInt("direction_id")));
            return course;
    });
    }

    public Course getCourseById(Integer id){
        String sql="SELECT * FROM courses WHERE id=(?)";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            Course course=new Course();
            course.setId(id);
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setLevel(new Level(rs.getInt("level_id")));
            course.setDirection(new Direction(rs.getInt("direction_id")));
            return course;
        },id).stream().findFirst().orElse(null);
    }

    public List<UserCourse> getUserCoursesByLevel(Integer id, int levelId) {
        String sql="SELECT courses.id course_id,courses.name course_name, courses.direction_id, directions.name direction\n" +
                "FROM users_courses\n" +
                "JOIN courses ON users_courses.course_id=courses.id\n" +
                "JOIN directions ON courses.direction_id=directions.id\n" +
                "WHERE courses.level_id=? AND users_courses.user_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserCourse userCourse=new UserCourse();
            userCourse.setId(rs.getInt("course_id"));
            userCourse.setName(rs.getString("course_name"));
            userCourse.setDirection(new Direction(rs.getString("direction")));
            return userCourse;
        },levelId,id);
    }
    public Integer addCourse(Course course, Integer authorId){
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", course.getName());
        parameters.put("description", course.getDescription());
        parameters.put("author_id", authorId);
        parameters.put("direction_id", course.getDirection().getId());
        parameters.put("level_id", course.getLevel().getId());
        //String sql="INSERT INTO courses (name,description,author_id,direction_id,level_id) VALUES (?,?,?,?,?)";
        //jdbcTemplate.update(sql,course.getName(),course.getDescription(),authorId,course.getDirection().getId(),course.getLevel().getId());
        return (Integer) simpleJdbcInsert.executeAndReturnKey(parameters);
    }


    public void addUserCourse(Integer userId, int courseId) {
        String sql="INSERT INTO users_courses (user_id,course_id) VALUES (?,?)";
        jdbcTemplate.update(sql,userId,courseId);
    }
    public void updateCourse(Course course){
        String sql="UPDATE courses SET name=(?), SET description=(?) WHERE id=(?)";
        jdbcTemplate.update(sql,course.getName(),course.getDescription(),course.getId());
    }


    public List<Course> getUserCoursesByLevelAndDirection(Integer level, Integer direction, Integer userId) {
        String sql="SELECT courses.id,courses.name course_name\n" +
                "FROM courses "+
                "JOIN users_courses ON users_courses.course_id=courses.id\n" +
                "WHERE users_courses.user_id=? AND courses.level_id=? AND courses.direction_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Course course=new Course();
            course.setId(rs.getInt("id"));
            course.setName(rs.getString("course_name"));
            return course;
        },userId,level,direction);
    }


    public List<ShortCourseDto> getUserCourses(Integer userId) {
        String sql="SELECT courses.id,courses.name\n" +
                "FROM courses\n" +
                "JOIN users_courses ON users_courses.course_id=courses.id\n" +
                "WHERE users_courses.user_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ShortCourseDto shortCourseDto=new ShortCourseDto();
            shortCourseDto.setId(rs.getInt("id"));
            shortCourseDto.setName(rs.getString("name"));
            return shortCourseDto;
        },userId);
    }
}
