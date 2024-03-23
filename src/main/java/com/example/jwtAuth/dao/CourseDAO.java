package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Course;
import jdk.jfr.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CourseDAO {

    private final JdbcTemplate jdbcTemplate;

    public CourseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Course findById(Integer id){
        String sql="SELECT * FROM courses WHERE id=(?)";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            Course course=new Course();
            course.setId(id);
            course.setName(rs.getString("name"));
            course.setDescription(rs.getString("description"));
            course.setScores(rs.getInt("scores"));
            return course;
        },id).stream().findFirst().orElse(null);
    }

    public void update(Course course){
        String sql="UPDATE courses SET name=(?), SET description=(?) WHERE id=(?)";
        jdbcTemplate.update(sql,course.getName(),course.getDescription(),course.getId());
    }

    public void save(Course course,Integer authorId){
        String sql="INSERT INTO courses (name,description,scores,author_id) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,course.getName(),course.getDescription(),course.getScores(),authorId);
    }

}
