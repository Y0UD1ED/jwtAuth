package com.example.jwtAuth.dao;


import com.example.jwtAuth.models.*;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");
    }

    public boolean isUserExist(String login) {
        String sql="SELECT COUNT(*) FROM users WHERE login =?";
        return jdbcTemplate.queryForObject(sql,Integer.class,login)>0;
    }

    public boolean isUserExist(Integer id) {
        String sql="SELECT COUNT(*) FROM users WHERE id =?";
        return jdbcTemplate.queryForObject(sql,Integer.class,id)>0;
    }

    public User getUserByLogin(String login) {
        String sql = "SELECT id,login,password FROM users WHERE login =?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            return user;
        },login).stream().findAny().orElse(null);
    }

    public List<Role> getUsersRoles(Integer userId){
        String sql = "select * from users_roles join roles on roles.id=users_roles.role_id where user_id=(?)";
        return jdbcTemplate.query(sql, (rs,rowNum) -> {
            Role role=new Role();
            role.setAuthority(rs.getString("name"));
            role.setId(rs.getInt("id"));
            return role;
        },userId);
    }




    public List<User> getAllUsersByLevelAndDirection(Integer levelId, Integer directionId) {
        String sql = "SELECT DISTINCT users.id,first_name,last_name FROM users "+
                "JOIN users_courses ON users.id=users_courses.user_id "+
                "JOIN courses ON users_courses.course_id=courses.id " +
                "WHERE courses.level_id =? AND courses.direction_id =?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            return user;
        }, levelId, directionId);
    }


    public Integer addUser(User user) {
        HashMap<String,Object> params=new HashMap<>();
        params.put("login",user.getLogin());
        params.put("password",user.getPassword());
        params.put("first_name",user.getFirstName());
        params.put("balance",user.getBalance());
        return insertUser.executeAndReturnKey(params).intValue();
    }

    public void setUserRole(Integer userId){
        String sql="INSERT INTO users_roles (user_id,role_id)" +
                "SELECT (?),roles.id FROM roles " +
                "WHERE roles.name='ROLE_USER'";
        jdbcTemplate.update(sql,userId);
    }
    public Integer addFullUser(User user) {
        HashMap<String,Object> params=new HashMap<>();
        params.put("login",user.getLogin());
        params.put("password",user.getPassword());
        params.put("first_name",user.getFirstName());
        params.put("last_name",user.getLastName());
        params.put("middle_name",user.getMiddleName());
        params.put("city",user.getCity());
        params.put("job",user.getJob());
        params.put("dob",user.getDoB());
        params.put("dow",user.getDoW());
        params.put("phone",user.getPhone());
        params.put("current_level",user.getCurrentLevel().getId());
        params.put("direction_id",user.getDirection().getId());
        params.put("balance",user.getBalance());
        return insertUser.executeAndReturnKey(params).intValue();
    }


    public void updateUser(User user) {
        String sql = "UPDATE users SET first_name=?,last_name=?,middle_name=?,city=?,job=?,dob=?,dow=?,phone=?,login=? WHERE id=?";
        jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getCity(), user.getJob(), user.getDoB(), user.getDoW(), user.getPhone(),user.getLogin(), user.getId());
    }

    public void updateUserBalance(Integer id, Integer balance) {
        String sql = "UPDATE users SET balance=? WHERE id=?";
        jdbcTemplate.update(sql, balance, id);

    }


    public User getUserById(Integer userId) {
        String sql = "SELECT * FROM users WHERE id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setMiddleName(rs.getString("middle_name"));
            user.setCity(rs.getString("city"));
            user.setJob(rs.getString("job"));
            user.setDoB(rs.getString("dob"));
            user.setDoW(rs.getString("dow"));
            user.setPhone(rs.getString("phone"));
            user.setBalance(rs.getInt("balance"));
            user.setCurrentLevel(new Level(rs.getInt("current_level")));
            user.setDirection(new Direction(rs.getInt("direction_id")));
            return user;
        }, userId);
    }


    public List<User> getAllUsers() {
        String sql="SELECT users.id,first_name,last_name,middle_name, levels.name FROM users " +
                "JOIN levels ON levels.id=users.current_level "+
                "JOIN users_roles ON users.id=users_roles.user_id " +
                "WHERE users_roles.role_id = 1";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setMiddleName(rs.getString("middle_name"));
            user.setCurrentLevel(new Level(0,rs.getString("name")));
            return user;
    });
    }
}
