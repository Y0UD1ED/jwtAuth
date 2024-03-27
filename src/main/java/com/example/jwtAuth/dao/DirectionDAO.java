package com.example.jwtAuth.dao;


import com.example.jwtAuth.models.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DirectionDAO {
    private final JdbcTemplate jdbcTemplate;

    public DirectionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Direction findById(int id) {
        String sql = "SELECT * FROM directions WHERE id =?";
        return jdbcTemplate.query(sql,(rs, rowNum) -> {
            Direction direction = new Direction();
            direction.setId(rs.getInt("id"));
            direction.setName(rs.getString("name"));
            return direction;
        }, id).stream().findAny().orElse(null);
    }

    public void save(Direction direction) {
        String sql = "INSERT INTO directions (name) VALUES (?)";
        jdbcTemplate.update(sql, direction.getName());
    }

    public void update(Direction direction) {
        String sql = "UPDATE directions SET name =? WHERE id =?";
        jdbcTemplate.update(sql, direction.getName(), direction.getId());
    }

    public void delete(int id){
        String sql = "DELETE FROM directions WHERE id =?";
        jdbcTemplate.update(sql, id);
    }
}
