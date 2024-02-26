package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.BaseUser;
import com.example.jwtAuth.models.ExtendUser;
import jdk.jfr.Category;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExtendUserDAO {
    private final JdbcTemplate jdbcTemplate;

    public ExtendUserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ExtendUser show(Integer id) {
        String sql = "SELECT * FROM users WHERE id =?";
        return jdbcTemplate.query(sql,(rs -> {
            ExtendUser user = new ExtendUser();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setSecondName(rs.getString("second_name"));
            user.setMiddleName(rs.getString("middle_name"));
            user.setCity(rs.getString("city"));
            user.setCompany(rs.getString("company"));
            user.setDoB(rs.getString("dob"));
            user.setDoW(rs.getString("dow"));
            user.setPhone(rs.getString("phone"));
            user.setPhoto(rs.getString("photo"));
            user.setCurrentLevel(rs.getString("current_level"));
            user.setNextLevel(rs.getString("next_level"));
            user.setScores(rs.getInt("scores"));
            return user;
        }),id);
    }

    public ExtendUser show(String login) {
        String sql = "SELECT * FROM users WHERE login =?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            ExtendUser user = new ExtendUser();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setSecondName(rs.getString("second_name"));
            user.setMiddleName(rs.getString("middle_name"));
            user.setCity(rs.getString("city"));
            user.setCompany(rs.getString("company"));
            user.setDoB(rs.getString("dob"));
            user.setDoW(rs.getString("dow"));
            user.setPhone(rs.getString("phone"));
            user.setPhoto(rs.getString("photo"));
            user.setCurrentLevel(rs.getString("current_level"));
            user.setNextLevel(rs.getString("next_level"));
            user.setScores(rs.getInt("scores"));
            return user;
        },login).stream().findFirst().orElse(null);
    }
}
