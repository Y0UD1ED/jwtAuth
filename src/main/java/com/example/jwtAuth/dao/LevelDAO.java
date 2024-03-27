package com.example.jwtAuth.dao;


import com.example.jwtAuth.models.Level;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LevelDAO {
    private final JdbcTemplate jdbcTemplate;

    public LevelDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Level findById(int id) {
        String sql = "SELECT * FROM levels WHERE id =?";
        return jdbcTemplate.query(sql, (rs,rowNum) ->{
            Level level=new Level();
            level.setId(rs.getInt("id"));
            level.setName(rs.getString("name"));
            return level;
        },id).stream().findAny().orElse(null);
    }

    public void addLevel(Level level) {
        String sql = "INSERT INTO levels (name) VALUES (?)";
        jdbcTemplate.update(sql, level.getName());
    }

    public void updateLevel(Level level) {
        String sql = "UPDATE levels SET name =? WHERE id =?";
        jdbcTemplate.update(sql, level.getName(), level.getId());
    }
    public void deleteLevel(int id) {
        String sql = "DELETE FROM levels WHERE id =?";
        jdbcTemplate.update(sql, id);
    }
}
