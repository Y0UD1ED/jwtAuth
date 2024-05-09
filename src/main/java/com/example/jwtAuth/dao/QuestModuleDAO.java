package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.QuestModule;
import com.example.jwtAuth.models.UserQuestModule;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public QuestModule getQuestModuleById(int moduleId) {
        String sql="SELECT * FROM quest_modules WHERE id=?";
        return jdbcTemplate.query(sql,(rs,rowNum) -> {
            QuestModule questModule =new QuestModule();
            questModule.setId(rs.getInt("id"));
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
            questModule.setId(rs.getInt("id"));
            questModule.setName(rs.getString("test_name"));
            questModule.setPosition(rs.getInt("position"));
            return questModule;
        },courseId);
    }

    public Integer addQuestModule(QuestModule questModule,Integer courseId) {
        //String sql="INSERT INTO quest_modules(course_id,name,position,scores) VALUES(?,?,?,?)";
        //jdbcTemplate.update(sql,questModule.getCourseId(),questModule.getName(),questModule.getPosition(),questModule.getScores());
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", questModule.getName());
        parameters.put("scores", questModule.getScores());
        parameters.put("position", questModule.getPosition());
        parameters.put("course_id", courseId);
        return (Integer) insertQuestModule.executeAndReturnKey(parameters);
    }
    public Pair<Integer,Integer> getUserQuestModuleScores(Integer courseId, Integer userId) {
        try {
            String sql = "SELECT SUM(qm.scores)  total_scores,COALESCE(SUM(qmp.scores),0)user_scores\n" +
                    "FROM quest_modules as qm\n" +
                    "LEFT JOIN quest_modules_passing as qmp ON qmp.quest_module_id=qm.id AND qmp.user_id=?\n" +
                    "WHERE qm.course_id=?\n" +
                    "GROUP BY qm.id";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                            new Pair<>(rs.getInt("user_scores"), rs.getInt("total_scores"))
                    , userId, courseId);
        } catch (EmptyResultDataAccessException e) {
            return new Pair<>(0, 0);
        }
    }

    public boolean isUserPassedTest(Integer moduleId, Integer userId) {
        String sql="SELECT COUNT(*) FROM quest_modules_passing WHERE quest_module_id=? AND user_id=?";
        return jdbcTemplate.queryForObject(sql,Integer.class,moduleId,userId)>0;
    }

    public void addUserQuestModule(Integer userId, Integer moduleId, Integer score) {
        String sql="INSERT INTO quest_modules_passing (user_id,quest_module_id,scores) VALUES (?,?,?)";
        jdbcTemplate.update(sql,userId,moduleId,score);
    }

    public List<UserQuestModule> getUserQuestModules(Integer courseId, Integer userId) {
        String sql="SELECT qm.id,qm.name, qm.scores total_scores,qmp.scores user_scores \n" +
                "FROM quest_modules as qm\n" +
                "JOIN quest_modules_passing as qmp ON qmp.quest_module_id=qm.id AND qmp.user_id=?\n" +
                "WHERE qm.course_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserQuestModule userQuestModule=new UserQuestModule();
            userQuestModule.setId(rs.getInt("id"));
            userQuestModule.setName(rs.getString("name"));
            userQuestModule.setScores(rs.getInt("total_scores"));
            userQuestModule.setUserScore(rs.getInt("user_scores"));
            return userQuestModule;
        },userId,courseId);
    }

}
