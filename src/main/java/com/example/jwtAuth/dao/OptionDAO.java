package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Option;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OptionDAO {
    private final JdbcTemplate jdbcTemplate;

    public OptionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<Integer,Option> getByQuestId(Integer questId){
        String sql="SELECT * FROM options WHERE quest_id=?";
        HashMap<Integer,Option> results = new HashMap<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,questId);
        for(Map<String, Object> row:rows){
            results.put((Integer) row.get("id"),new Option(row.get("option").toString(),row.get("correct").toString().equals("true")));
        }
        return results;
    }

    public void addOption(Option option){
        String sql="INSERT INTO options(option,correct,quest_id) VALUES(?,?,?)";
        jdbcTemplate.update(sql,option.getOption(),option.getCorrect(),option.getQuestionId());
    }

    public void deleteOption(Integer questId){
        String sql="DELETE FROM options WHERE quest_id=(?)";
        jdbcTemplate.update(sql,questId);
    }
}
