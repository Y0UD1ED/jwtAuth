package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.OptionDAO;
import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.models.Option;
import com.example.jwtAuth.models.QuestModule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestModuleService {
    private final QuestModuleDAO questModuleDAO;

    private final OptionDAO optionDAO;

    public QuestModuleService(QuestModuleDAO questModuleDAO,OptionDAO optionDAO) {
        this.questModuleDAO = questModuleDAO;
        this.optionDAO=optionDAO;
    }

    public List<QuestModule> findByCourseId(Integer moduleId){
        List<QuestModule> questModuleList=questModuleDAO.findByCourseId(moduleId);
        for (QuestModule quest: questModuleList) {
            quest.setOptionList(this.findOptionByQuestId(quest.getQuestId()));
        }
        return questModuleList;
    }

    public void save(List<QuestModule> questModuleList){
        for(QuestModule quest:questModuleList){
            questModuleDAO.save(quest);
            for(Option option:quest.getOptionList()){
                optionDAO.save(option,3);
            }

        }
    }
    private List<Option> findOptionByQuestId(Integer questId){
        return optionDAO.findByQuestId(questId);
    }
}
