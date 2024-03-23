package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.QuestModule;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestModuleDAO {
    private final JdbcTemplate jdbcTemplate;

    public QuestModuleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<QuestModule> findByCourseId(Integer courseId){
        String sql="SELECT * FROM quest_modules WHERE course_id=(?) ORDER BY position";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            QuestModule questModule=new QuestModule();
            questModule.setCourseId(courseId);
            questModule.setQuestId(rs.getInt("id"));
            questModule.setQuestPosition(rs.getInt("position"));
            questModule.setQuest(rs.getString("quest"));
            return questModule;
        },courseId).stream().toList();
    }
    public void save(QuestModule questModule){
        String sql="INSERT INTO quest_modules (course_id,position,quest) VALUES (?,?,?)";
        jdbcTemplate.update(sql,questModule.getCourseId(),questModule.getQuestPosition(),questModule.getQuest());
    }

    public void delete(Integer moduleId){
        String sql="DELETE FROM quest_modules WHERE course_id=(?)";
        jdbcTemplate.update(sql,moduleId);
    }
}
