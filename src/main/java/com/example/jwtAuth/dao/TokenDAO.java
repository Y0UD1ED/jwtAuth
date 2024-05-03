package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Token;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenDAO {
    private final JdbcTemplate jdbcTemplate;


    public TokenDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isTokenExist(Integer userId){
        String sql="SELECT COUNT(*) from tokens WHERE user_id=?";
        return jdbcTemplate.queryForObject(sql, Integer.class,userId)>0;
    }

    public Token getTokenByUserId(Integer id){
        String sql="SELECT * from tokens WHERE user_id=?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {Token token=new Token();
                                                    token.setToken(rs.getString("refresh_token"));
                                                    token.setUser_id(rs.getInt("user_id"));
                                                    return token;},id).stream().findAny().orElse(null);
    }
    public void addToken(Token token) {
        String sql = "INSERT INTO tokens (user_id,refresh_token) VALUES (?,?)";
        jdbcTemplate.update(sql, token.getUser_id(),token.getToken());
    }

    public void updateToken(Token token) {
        String sql = "UPDATE tokens SET refresh_token = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, token.getToken(), token.getUser_id());
    }

    public void deleteToken(Integer userId) {
        String sql = "DELETE FROM tokens WHERE user_id =?";
        jdbcTemplate.update(sql, userId);
    }


}
