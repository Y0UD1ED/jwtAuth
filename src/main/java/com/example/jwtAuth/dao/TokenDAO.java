package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class TokenDAO {
    private final JdbcTemplate jdbcTemplate;


    public TokenDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Token show(Integer id){
        String sql="SELECT * from tokens WHERE user_id=?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {Token token=new Token();
                                                    token.setToken(rs.getString("refresh_token"));
                                                    token.setUser_id(rs.getInt("user_id"));
                                                    return token;},id).stream().findAny().orElse(null);
    }
    public void save(Token token) {
        String sql = "INSERT INTO tokens (user_id,refresh_token) VALUES (?,?)";
        jdbcTemplate.update(sql, token.getUser_id(),token.getToken());
    }

    public void update(Token token) {
        String sql = "UPDATE tokens SET refresh_token = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, token.getToken(), token.getUser_id());
    }

    public void delete(Integer userId) {
        String sql = "DELETE FROM tokens WHERE user_id =?";
        jdbcTemplate.update(sql, userId);
    }


}
