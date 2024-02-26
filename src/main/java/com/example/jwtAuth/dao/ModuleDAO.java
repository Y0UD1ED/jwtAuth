package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Module;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ModuleDAO {

    private final JdbcTemplate jdbcTemplate;

    public ModuleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Module findById(Integer id){
        String sql="SELECT * FROM modules WHERE id=(?)";
        return jdbcTemplate.query(sql,(rs)->{
            Module module=new Module();
            module.setId(id);
            module.setName(rs.getString("name"));
            module.setScores(rs.getInt("scores"));
            module.setOrder(rs.getInt("order"));
            module.setDescription(rs.getString("description"));
            return module;
        },id);
    }

    public List<Module> findByCourseId(Integer id){
        String sql="SELECT * FROM module WHERE course_id=(?)";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            Module module=new Module();
            module.setId(rs.getInt("id"));
            module.setName(rs.getString("name"));
            module.setScores(rs.getInt("scores"));
            module.setOrder(rs.getInt("order"));
            module.setDescription(rs.getString("description"));
            return module;
        },id).stream().toList();
    }

    public void update(Module module){
        String sql="UPDATE modules SET course_id=(?),SET order=(?),SET name=(?),SET description=(?),SET scores=(?) WHERE id=(?)";
        jdbcTemplate.update(sql,module.getCourse_id(),module.getOrder(),module.getName(),module.getDescription(),module.getDescription(),module.getId());

    }

    public void save(Module module){
        String sql="INSERT INTO modules (course_id,order,name,description,scores) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sql,module.getCourse_id(), module.getOrder(),module.getName(),module.getDescription(),module.getScores());
    }

    public void delete(Integer course_id){
        String sql="DELETE FROM modules WHERE course_id=(?)";
        jdbcTemplate.update(sql,course_id);
    }
}
