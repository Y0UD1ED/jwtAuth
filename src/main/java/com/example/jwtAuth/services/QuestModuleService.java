package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.OptionDAO;
import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.models.Option;
import com.example.jwtAuth.models.QuestModule;

import java.util.List;

public class QuestModuleService {
    private final QuestModuleDAO questModuleDAO;

    private final OptionDAO optionDAO;

    public QuestModuleService(QuestModuleDAO questModuleDAO,OptionDAO optionDAO) {
        this.questModuleDAO = questModuleDAO;
        this.optionDAO=optionDAO;
    }

    public List<QuestModule> findByModuleId(Integer moduleId){
        List<QuestModule> questModuleList=questModuleDAO.findByModuleId(moduleId);
        for (QuestModule quest: questModuleList) {
            quest.setOptionList(this.findOptionByQuestId(quest.getQuestId()));
        }
        return questModuleList;
    }

    private List<Option> findOptionByQuestId(Integer questId){
        return optionDAO.findByQuestId(questId);
    }
}
