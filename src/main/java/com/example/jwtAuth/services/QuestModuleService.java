package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.dao.QuestModulePassingDAO;
import com.example.jwtAuth.dtos.TestDto;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.QuestModule;
import com.example.jwtAuth.models.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestModuleService {
    private final QuestModuleDAO questModuleDAO;

    private final TestsService testsService;

    private final QuestModulePassingDAO testPassingDAO;

    public QuestModuleService(QuestModuleDAO testDAO, QuestModulePassingDAO testPassingDAO, TestsService testsService) {
        this.questModuleDAO = testDAO;
        this.testPassingDAO = testPassingDAO;
        this.testsService = testsService;
    }
    public QuestModule findQuestModuleById(Integer id) {
        QuestModule questModule = questModuleDAO.getQuestModulesById(id);
        questModule.setTests(testsService.findByQuestModuleId(id));
        return questModule;
    }
    public Map<String, Object> findQuestModulesIdAndTotalScoresByCourseId(Integer courseId) {
        Map<String, Object> result = new HashMap<>();
        List<QuestModule> questModules = questModuleDAO.getQuestModulesByCourseId(courseId);
        List<Integer> questModulesId=new ArrayList<>();
        for (QuestModule questModule : questModules) {
            questModulesId.add(questModule.getQuestModuleId());
        }
        result.put("questModuleId", questModulesId);
        result.put("totalScores", getTotalScoresForTest(questModules));
        return result;
    }

    public Integer getTotalScoresForTest(List<QuestModule> questModules) {
        return questModules.stream().mapToInt(QuestModule::getScores).sum();
    }

    public Integer getUserScoreForCourse(Integer courseId) {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) jwtAuthentication.getPrincipal()).getId();
        return testPassingDAO.getUserScoreForTestByCourseId(courseId, userId);
    }

    public void saveQuestModule(QuestModule questModule) {
        Integer id=questModuleDAO.addQuestModule(questModule);
        for(Test test:questModule.getTests()){
            test.setQuestModuleId(id);
        }
        testsService.save(questModule.getTests());
    }


}
