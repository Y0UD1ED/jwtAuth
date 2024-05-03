package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.QuestModulePassing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestModulePassingDAO {
    private final JdbcTemplate jdbcTemplate;

    public QuestModulePassingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isUserPassedTest(int questModuleId, int userId) {
        String sql="SELECT COUNT(quest_modules_passing.user_id) FROM quest_modules_passing\n" +
                "WHERE quest_modules_passing.quest_module_id=? AND quest_modules_passing.user_id=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,questModuleId,userId)>0;
    }
    public Integer getUserScoreForTestByCourseId(int courseId, int userId) {
        String sql="SELECT SUM(test_passing.scores) FROM test_passing\n" +
                "JOIN test ON test_passing.test_id=test.id\n" +
                "WHERE test.course_id=? AND test_passing.user_id=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,courseId,userId);
    }

    public List<QuestModulePassing> getQuestModulePassingByUserId(int userId) {
        String sql="SELECT * FROM quest_modules_passing WHERE user_id=?";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            QuestModulePassing modulePassing=new QuestModulePassing();
            modulePassing.setQuestModuleId(rs.getInt("quest_module_id"));
            modulePassing.setUserId(rs.getInt("user_id"));
            modulePassing.setScore(rs.getInt("scores"));
            return modulePassing;
        },userId);
    }

    public List<QuestModulePassing> getQuestModulePassingByCourseId(int courseId) {
        String sql="SELECT * FROM quest_modules_passing WHERE course_id=?";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            QuestModulePassing modulePassing=new QuestModulePassing();
            modulePassing.setQuestModuleId(rs.getInt("quest_module_id"));
            modulePassing.setUserId(rs.getInt("user_id"));
            modulePassing.setScore(rs.getInt("scores"));
            return modulePassing;
        },courseId);
    }

    public QuestModulePassing getModulePassingModuleId(int moduleId) {
        String sql="SELECT * FROM quest_modules_passing WHERE AND module_id=?";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            QuestModulePassing modulePassing=new QuestModulePassing();
            modulePassing.setQuestModuleId(rs.getInt("quest_module_id"));
            modulePassing.setUserId(rs.getInt("user_id"));
            modulePassing.setScore(rs.getInt("scores"));
            return modulePassing;
        },moduleId).stream().findFirst().orElse(null);
    }

    public void addQuestModulePassing(QuestModulePassing modulePassing) {
        String sql="INSERT INTO quest_modules_passing (quest_module_id,user_id,scores) VALUES (?,?,?)";
        jdbcTemplate.update(sql,modulePassing.getQuestModuleId(),modulePassing.getUserId(),modulePassing.getScore());
    }
}
