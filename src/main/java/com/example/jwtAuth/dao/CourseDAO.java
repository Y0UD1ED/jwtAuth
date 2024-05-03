package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Course;
import com.example.jwtAuth.models.Direction;
import com.example.jwtAuth.models.InfoModule;
import com.example.jwtAuth.models.Level;
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


    public void updateCourse(Course course){
        String sql="UPDATE courses SET name=(?), SET description=(?) WHERE id=(?)";
        jdbcTemplate.update(sql,course.getName(),course.getDescription(),course.getId());
    }




}
