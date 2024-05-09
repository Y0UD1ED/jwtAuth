package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.QuestModule;
import com.example.jwtAuth.models.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Map;

@Service
public class PassingService {
    private final QuestModuleDAO questModuleDAO;

    public PassingService(QuestModuleDAO questModuleDAO) {
        this.questModuleDAO = questModuleDAO;
    }
    public Integer passQuestModule(Integer moduleId, Map<Integer,Integer> answers) throws AuthenticationException {
        JwtAuthentication authentication= (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) authentication.getPrincipal()).getId();
        boolean check=questModuleDAO.isUserPassedTest(moduleId,userId);
        if(!check){
            QuestModule questModule=questModuleDAO.getQuestModuleById(moduleId);
            List<Test> testList=questModule.getTests();
            Integer usersScore=0;
            Integer totalScore=questModule.getScores();
            Integer totalQuestions=testList.size();
            Integer correctAnswers=0;
            for(Test test:testList){
                Integer questionId=test.getTestId();
                Integer questionAnswer=answers.get(questionId);
                if(test.getOptionList().get(questionAnswer).getCorrect()){
                    correctAnswers++;
                }
            }
            usersScore=correctAnswers*totalScore/totalQuestions;
            questModuleDAO.addUserQuestModule(userId,moduleId,usersScore);
            return correctAnswers;
        }
        throw new AuthenticationException("You have already passed this quest module");
    }

}
