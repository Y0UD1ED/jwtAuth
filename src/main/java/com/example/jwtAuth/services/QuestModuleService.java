package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.models.QuestModule;
import com.example.jwtAuth.models.Test;
import org.springframework.stereotype.Service;

@Service
public class QuestModuleService {
    private final QuestModuleDAO questModuleDAO;

    private final TestsService testsService;


    public QuestModuleService(QuestModuleDAO testDAO, TestsService testsService) {
        this.questModuleDAO = testDAO;
        this.testsService = testsService;
    }
    public QuestModule findQuestModuleById(Integer id) {
        QuestModule questModule = questModuleDAO.getQuestModuleById(id);
        questModule.setTests(testsService.findByQuestModuleId(id));
        return questModule;
    }


    public void saveQuestModule(QuestModule questModule,Integer courseId) {
        Integer id=questModuleDAO.addQuestModule(questModule,courseId);
        for(Test test:questModule.getTests()){
            test.setQuestModuleId(id);
        }
        testsService.save(questModule.getTests());
    }




}
