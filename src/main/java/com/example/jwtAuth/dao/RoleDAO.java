package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleDAO {


    private final JdbcTemplate jdbcTemplate;

    public RoleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Role> getUsersRoles(Integer id) {
        String sql = "select * from users_roles join roles on roles.id=users_roles.role_id where user_id=(?)";
        return jdbcTemplate.query(sql, (rs,rowNum) -> {
            Role role=new Role();
            role.setAuthority(rs.getString("name"));
            role.setId(rs.getInt("id"));
            return role;
        },id);
    }

    public void setRole(Integer userId, Integer roleId) {
        String sql="INSERT INTO users_roles (user_id, role_id) VALUES (?,?)";
        jdbcTemplate.update(sql,userId,roleId);
    }

    public void setUserRole(Integer userId){
        String sql="INSERT INTO users_roles (user_id,role_id)" +
                "SELECT (?),roles.id FROM roles " +
                "WHERE roles.name='ROLE_USER'";
        jdbcTemplate.update(sql,userId);
    }

}
