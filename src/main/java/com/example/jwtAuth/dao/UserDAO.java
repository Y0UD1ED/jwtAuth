package com.example.jwtAuth.dao;


import com.example.jwtAuth.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User show(Integer id) {
        String sql = "SELECT * FROM users WHERE id =?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            User user = new User();
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
        },id).stream().findFirst().orElse(null);
    }

    public User show(String login) {
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
            user.setCompany(rs.getString("company"));
            user.setDoB(rs.getString("dob"));
            user.setDoW(rs.getString("dow"));
            user.setPhone(rs.getString("phone"));
            user.setPhoto(rs.getString("photo"));
            user.setCurrentLevel(rs.getString("current_level"));
            user.setNextLevel(rs.getString("next_level"));
            user.setScores(rs.getInt("scores"));
            return user;
        },login).stream().findAny().orElse(null);
    }

    public boolean check(String login) {
        String sql="SELECT COUNT(*) FROM users WHERE login =?";
        return jdbcTemplate.queryForObject(sql,Integer.class,login)>0;
    }
    public void save(User user) {
        String sql="INSERT INTO users (login,password,first_name,second_name,middle_name) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getFirstName(),user.getSecondName(),user.getMiddleName());
    }

    public void update(User user) {
        String sql = "UPDATE users SET first_name=?,second_name=?,middle_name=?,city=?,company=?,dob=?,dow=?,phone=? WHERE id=?";
        jdbcTemplate.update(sql, user.getFirstName(), user.getSecondName(), user.getMiddleName(), user.getCity(), user.getCompany(), user.getDoB(), user.getDoW(), user.getPhone(), user.getId());
    }
    public Integer getUserId(String login) {
        String sql="SELECT id FROM users WHERE login =?";
        return jdbcTemplate.queryForObject(sql,Integer.class,login);
    }
    public void fullSave(User user) {
        String sql="INSERT INTO users (login,password,first_name,second_name,middle_name,city,company,dob,dow,phone,photo,current_level,next_level,scores) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,user.getUsername(),user.getPassword(),user.getFirstName(),user.getSecondName(),user.getMiddleName(),user.getCity(),user.getCompany(),user.getDoB(),user.getDoW(),user.getPhone(),user.getPhoto(),user.getCurrentLevel(),user.getNextLevel(),user.getScores());
    }
}
