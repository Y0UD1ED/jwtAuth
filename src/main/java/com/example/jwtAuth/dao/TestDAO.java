package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TestDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public TestDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tests").usingGeneratedKeyColumns("id");
    }


    public List<Test> getByQuestModuleId(Integer questModuleId){
        String sql="SELECT * FROM tests WHERE quest_module_id=(?) ORDER BY position";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            Test test =new Test();
            test.setTestId(rs.getInt("id"));
            test.setQuestModuleId(rs.getInt("quest_module_id"));
            test.setQuestPosition(rs.getInt("position"));
            test.setQuest(rs.getString("quest"));
            return test;
        },questModuleId).stream().toList();
    }
    public Integer addTest(Test test){
        Map<String,Object> params=new HashMap<>();
        params.put("quest_module_id",test.getQuestModuleId());
        params.put("position",test.getQuestPosition());
        params.put("quest",test.getQuest());
        return (Integer) insert.executeAndReturnKey(params);
    }

    public void deleteTest(Integer moduleId){
        String sql="DELETE FROM tests WHERE quest_module_id=(?)";
        jdbcTemplate.update(sql,moduleId);
    }
}
