package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Option;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class OptionDAO {
    private final JdbcTemplate jdbcTemplate;

    public OptionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Option> findByQuestId(Integer questId){
        String sql="SELECT * FROM options WHERE quest_id=(?)";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            Option option=new Option();
            option.setOption(rs.getString("option"));
            option.setId(rs.getInt("id"));
            option.setCorrect(rs.getBoolean("correct"));
            return option;
        }).stream().toList();
    }

    public void save(Option option){
        String sql="INSERT INTO options(id,option,correct) VALUES(?,?,?)";
        jdbcTemplate.update(sql,option.getId(),option.getOption(),option.getCorrect());
    }

    public void delete(Integer questId){
        String sql="DELETE FROM options WHERE quest_id=(?)";
        jdbcTemplate.update(sql,questId);
    }
}
