package com.example.jwtAuth.dao;


import com.example.jwtAuth.models.*;
import org.antlr.v4.runtime.misc.Pair;
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
        String sql = "UPDATE users SET first_name=?,second_name=?,middle_name=?,city=?,job=?,dob=?,dow=?,phone=?,login=? WHERE id=?";
        jdbcTemplate.update(sql, user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getCity(), user.getJob(), user.getDoB(), user.getDoW(), user.getPhone(),user.getLogin(), user.getId());
    }

    public void updateUserBalance(Integer id, Integer balance) {
        String sql = "UPDATE users SET scores=? WHERE id=?";
        jdbcTemplate.update(sql, balance, id);

    }


    public void setUserCourse(Integer userId, int courseId) {
        String sql="INSERT INTO users_courses (user_id,course_id) VALUES (?,?)";
        jdbcTemplate.update(sql,userId,courseId);
    }

    public List<UserCourse> getUserCoursesByLevel(Integer id, int levelId) {
        String sql="SELECT courses.id course_id,courses.name course_name, courses.direction_id, directions.name direction, quest_modules.scores total_scores,qmp.scores user_scores\n" +
                "FROM users_courses\n" +
                "JOIN courses ON users_courses.course_id=courses.id\n" +
                "JOIN directions ON courses.direction_id=directions.id\n" +
                "WHERE courses.level_id=? AND users_courses.user_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserCourse userCourse=new UserCourse();
            userCourse.setId(rs.getInt("course_id"));
            userCourse.setName(rs.getString("course_name"));
            userCourse.setDirection(new Direction(rs.getString("direction")));
            return userCourse;
        },id,levelId);
    }

    public List<UserQuestModule> getUserQuestModules(Integer courseId, Integer userId) {
        String sql="SELECT qm.scores total_scores,qmp.scores user_scores \n" +
                "FROM quest_modules as qm\n" +
                "JOIN quest_modules_passing as qmp ON qmp.quest_module_id=qm.id AND qmp.user_id=?\n" +
                "WHERE qm.course_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserQuestModule userQuestModule=new UserQuestModule();
            userQuestModule.setScores(rs.getInt("total_scores"));
            userQuestModule.setUserScore(rs.getInt("user_scores"));
            return userQuestModule;
        },userId,courseId);
    }

    public Pair<Integer,Integer> getUserQuestModuleScores(Integer courseId, Integer userId) {
        String sql="SELECT SUM(qm.scores)  total_scores,COALESCE(SUM(qmp.scores),0)user_scores\n" +
                "FROM quest_modules as qm\n" +
                "LEFT JOIN quest_modules_passing as qmp ON qmp.quest_module_id=qm.id AND qmp.user_id=?\n" +
                "WHERE qm.course_id=?\n" +
                "GROUP BY qm.id";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new Pair<>(rs.getInt("user_scores"),rs.getInt("total_scores"));
        },userId,courseId);
    }
    }

}
