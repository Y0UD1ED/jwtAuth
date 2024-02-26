package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.BaseUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BaseUserDAO {
    private final JdbcTemplate jdbcTemplate;

    public BaseUserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public BaseUser show(Integer id) {
        String sql = "SELECT * FROM users WHERE id =?";
        return jdbcTemplate.query(sql,(rs -> {
            return new BaseUser(rs.getInt("id"),rs.getString("login"),rs.getString("password"));
        }),id);
    }

    public BaseUser show(String login) {
        String sql = "SELECT * FROM users WHERE login =?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            return new BaseUser(rs.getInt("id"), rs.getString("login"), rs.getString("password"));
        },login).stream().findFirst().orElse(null);
    }

}
