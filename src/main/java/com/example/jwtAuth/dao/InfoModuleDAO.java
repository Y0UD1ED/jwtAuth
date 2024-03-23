package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.InfoModule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InfoModuleDAO {
    private final JdbcTemplate jdbcTemplate;

    public InfoModuleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

   public List<InfoModule> findByCourseId(Integer courseId){
        String sql="SELECT * FROM info_modules WHERE course_id=(?) ORDER BY position";
        return jdbcTemplate.query(sql,(rs, rowNum) -> {
            InfoModule infoModule= new InfoModule();
            infoModule.setInfoId(rs.getInt("id"));
            infoModule.setCourseId(courseId);
            infoModule.setContent(rs.getString("content"));
            infoModule.setContentType(rs.getString("type"));
            infoModule.setContentPosition(rs.getInt("position"));
            return infoModule;
        },courseId).stream().toList();
   }

   public void save(InfoModule infoModule){
        String sql="INSERT INTO info_modules (course_id, position, content,type) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql,infoModule.getCourseId(), infoModule.getContentPosition(),infoModule.getContent(),infoModule.getContentType());

   }

    public void update(InfoModule infoModule){
        String sql="UPDATE info_modules SET course_id=?, content=?,type=? ,order=? WHERE id=(?)";
        jdbcTemplate.update(sql,infoModule.getCourseId(),infoModule.getContent(),infoModule.getContentType(),infoModule.getContentPosition(),infoModule.getCourseId());
    }


   public void delete(Integer courseId){
        String sql="DELETE FROM info_module WHERE course_id=(?)";
        jdbcTemplate.update(sql,courseId);
   }
}
