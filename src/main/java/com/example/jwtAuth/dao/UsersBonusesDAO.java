package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Bonus;
import com.example.jwtAuth.models.UsersBonuses;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsersBonusesDAO {

    private final JdbcTemplate jdbcTemplate;

    public UsersBonusesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Integer> getUsersBonuses(Integer userId) {
        String sql = "SELECT bonus_id FROM users_bonuses WHERE user_id =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return rs.getInt("bonus_id");
        }, userId);
    }
    public void setBonus(Integer userId, Integer bonusId, Integer count) {
        String sql = "INSERT INTO users_bonuses (user_id, bonus_id, count) VALUES (?,?,?)";
        jdbcTemplate.update(sql, userId, bonusId, count);
    }

    public void updateBonus(Integer userId, Integer bonusId, Integer count) {
        String sql = "UPDATE users_bonuses SET count =? WHERE user_id =? AND bonus_id =?";
        jdbcTemplate.update(sql, count, userId, bonusId);
    }

    public List<UsersBonuses> show(Integer userId) {
        String sql = "SELECT * FROM users_bonuses WHERE user_id =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UsersBonuses usersBonuses=new UsersBonuses();
            usersBonuses.setUserId(rs.getInt("user_id"));
            usersBonuses.setBonusId(rs.getInt("bonus_id"));
            usersBonuses.setCount(rs.getInt("count"));
            return usersBonuses;
        },userId);
    }

   public boolean check(Integer userId, Integer bonusId) {
        String sql = "SELECT COUNT(*) FROM users_bonuses WHERE user_id =? AND bonus_id =?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, bonusId) > 0;

    }

}
