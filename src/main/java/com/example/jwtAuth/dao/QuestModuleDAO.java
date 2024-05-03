package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.QuestModule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QuestModuleDAO {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertQuestModule;

    public QuestModuleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertQuestModule = new SimpleJdbcInsert(jdbcTemplate).withTableName("quest_modules").usingGeneratedKeyColumns("id");
    }

    public QuestModule getQuestModulesById(int moduleId) {
        String sql="SELECT * FROM quest_modules WHERE id=?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            QuestModule questModule =new QuestModule();
            questModule.setQuestModuleId(rs.getInt("id"));
            questModule.setCourseId(rs.getInt("course_id"));
            questModule.setName(rs.getString("name"));
            questModule.setPosition(rs.getInt("position"));
            questModule.setScores(rs.getInt("scores"));
            return questModule;
        },moduleId).stream().findFirst().orElse(null);
    }

    public List<QuestModule> getQuestModulesByCourseId(int courseId) {
        String sql="SELECT * FROM quest_modules WHERE course_id=? ORDER BY position";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            QuestModule questModule =new QuestModule();
            questModule.setQuestModuleId(rs.getInt("id"));
            questModule.setCourseId(rs.getInt("course_id"));
            questModule.setName(rs.getString("test_name"));
            questModule.setPosition(rs.getInt("position"));
            return questModule;
        },courseId);
    }

    public Integer addQuestModule(QuestModule questModule) {
        //String sql="INSERT INTO quest_modules(course_id,name,position,scores) VALUES(?,?,?,?)";
        //jdbcTemplate.update(sql,questModule.getCourseId(),questModule.getName(),questModule.getPosition(),questModule.getScores());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", questModule.getName());
        parameters.put("scores", questModule.getScores());
        parameters.put("position", questModule.getPosition());
        parameters.put("course_id", questModule.getCourseId());
        return (Integer) insertQuestModule.executeAndReturnKey(parameters);
    }
}
