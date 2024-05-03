package com.example.jwtAuth.dao;


import com.example.jwtAuth.models.Direction;
import com.example.jwtAuth.models.Level;
import com.example.jwtAuth.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isUserExist(String login) {
        String sql="SELECT COUNT(*) FROM users WHERE login =?";
        return jdbcTemplate.queryForObject(sql,Integer.class,login)>0;
    }

    public User getUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login =?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setSecondName(rs.getString("second_name"));
            user.setMiddleName(rs.getString("middle_name"));
            user.setCity(rs.getString("city"));
            user.setJob(rs.getString("job"));
            user.setDoB(rs.getString("dob"));
            user.setDoW(rs.getString("dow"));
            user.setPhone(rs.getString("phone"));
            user.setPhoto(rs.getString("photo"));
            user.setCurrentLevel(new Level(rs.getInt("current_level")));
            user.setDirection(new Direction(rs.getInt("direction_id")));
            user.setScores(rs.getInt("scores"));
            return user;
        },login).stream().findAny().orElse(null);
    }

    public List<Integer> getAllUsersId() {
        String sql = "SELECT id FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"));
    }

    public List<Integer> getAllUsersIdByDirection(Integer id) {
        String sql = "SELECT id FROM users WHERE direction_id =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"),id);
    }

    public List<Integer> getAllUsersIdByLevel(Integer id) {
        String sql = "SELECT id FROM users WHERE current_level =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), id);
    }

    public List<Integer> getAllUsersIdByLevelAndDirection(Integer id, Integer id2) {
        String sql = "SELECT id FROM users WHERE current_level =? AND direction_id =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("id"), id, id2);
    }

    public Integer getUserId(String login) {
        String sql="SELECT id FROM users WHERE login =?";
        return jdbcTemplate.queryForObject(sql,Integer.class,login);
    }

    public Integer getUserBalance(Integer id) {
        String sql="SELECT scores FROM users WHERE id =?";
        return jdbcTemplate.queryForObject(sql,Integer.class,id);
    }

    public void addUser(User user) {
        String sql="INSERT INTO users (login,password,first_name,scores) VALUES (?,?,?,0)";
        jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getFirstName());
    }

    public void addFullUser(User user) {
        String sql="INSERT INTO users (login,password,first_name,second_name,middle_name,city,job,dob,dow,phone,photo,current_level,direction_id,scores) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,0)";
        jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getFirstName(),user.getSecondName(),user.getMiddleName(),user.getCity(),user.getJob(),user.getDoB(),user.getDoW(),user.getPhone(),user.getPhoto(),user.getCurrentLevel().getId(),user.getDirection().getId());
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET first_name=?,second_name=?,middle_name=?,city=?,job=?,dob=?,dow=?,phone=?,login=? WHERE id=?";
        jdbcTemplate.update(sql, user.getFirstName(), user.getSecondName(), user.getMiddleName(), user.getCity(), user.getJob(), user.getDoB(), user.getDoW(), user.getPhone(),user.getUsername(), user.getId());
    }

    public void updateUserBalance(Integer id, Integer balance) {
        String sql = "UPDATE users SET scores=? WHERE id=?";
        jdbcTemplate.update(sql, balance, id);

    }



}
