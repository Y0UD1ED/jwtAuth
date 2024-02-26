package com.example.jwtAuth.dao;

import com.example.jwtAuth.models.QuestModule;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class QuestModuleDAO {
    private final JdbcTemplate jdbcTemplate;

    public QuestModuleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<QuestModule> findByModuleId(Integer moduleId){
        String sql="SELECT * FROM quest_module WHERE module_id=(?)";
        return jdbcTemplate.query(sql,(rs,rowNum)->{
            QuestModule questModule=new QuestModule();
            questModule.setModuleId(moduleId);
            questModule.setQuestId(rs.getInt("id"));
            questModule.setQuestId(rs.getInt("quest_modules.id"));
            questModule.setQuestPosition(rs.getInt("order"));
            questModule.setQuest(rs.getString("quest"));
            return questModule;
        }).stream().toList();
    }
    public void save(QuestModule questModule){
        String sql="INSERT INTO quest_modules (module_id,order,quest) VALUES (?,?,?)";
        jdbcTemplate.update(sql,questModule.getModuleId(),questModule.getQuestPosition(),questModule.getQuest());
    }

    public void delete(Integer moduleId){
        String sql="DELETE FROM quest_modules WHERE module_id=(?)";
        jdbcTemplate.update(sql,moduleId);
    }
}
