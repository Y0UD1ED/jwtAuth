package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.QuestModulePassingDAO;
import com.example.jwtAuth.dtos.TestDto;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.QuestModulePassing;
import com.example.jwtAuth.models.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuestModulesPassingService {
    private final QuestModulePassingDAO questModulePassingDAO;

    private final TestsService testsService;

    public QuestModulesPassingService(QuestModulePassingDAO modulePassingDAO, TestsService testsService) {
        this.questModulePassingDAO = modulePassingDAO;
        this.testsService = testsService;
    }

    public List<QuestModulePassing> findByUserId(Integer userId) {
        return questModulePassingDAO.getQuestModulePassingByUserId(userId);
    }

    public List<QuestModulePassing> findByCourseId(Integer courseId) {
        return questModulePassingDAO.getQuestModulePassingByCourseId(courseId);
    }

    private QuestModulePassing findByModuleId(Integer moduleId) {
        QuestModulePassing modulePassing = questModulePassingDAO.getModulePassingModuleId(moduleId);
        return modulePassing;
    }

    public void save(QuestModulePassing modulePassing) {
        if(findByModuleId(modulePassing.getQuestModuleId()) == null){
            questModulePassingDAO.addQuestModulePassing(modulePassing);
        }

    }

    public void passQuestModule(Integer moduleId, Map<Integer,Integer> answers) {
        JwtAuthentication authentication= (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) authentication.getPrincipal()).getId();
        boolean check=questModulePassingDAO.isUserPassedTest(moduleId,userId);
        if(!check){
            List<Test> testList = testsService.findByQuestModuleId(moduleId);
            Integer usersScore=0;
            Integer totalScore=100;
            Integer totalQuestions=testList.size();
            Integer correctAnswers=0;
            for(Test test:testList){
                Integer questionId=test.getTestId();
                Integer questionAnswer=answers.get(questionId);
                if(test.getOptionList().get(questionAnswer).getCorrect()){
                    correctAnswers++;
                }
            }
            usersScore=correctAnswers*100/totalQuestions;
            QuestModulePassing modulePassing=new QuestModulePassing();
            modulePassing.setScore(usersScore);
            modulePassing.setQuestModuleId(moduleId);
            modulePassing.setUserId(userId);
            questModulePassingDAO.addQuestModulePassing(modulePassing);
        }
    }

}
