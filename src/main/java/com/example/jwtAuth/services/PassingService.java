package com.example.jwtAuth.services;

import com.example.jwtAuth.authentications.JwtAuthentication;
import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.dao.TestDAO;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.dtos.PassedTestDto;
import com.example.jwtAuth.models.ExtendUserDetails;
import com.example.jwtAuth.models.QuestModule;
import com.example.jwtAuth.models.Test;
import com.example.jwtAuth.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PassingService {
    private final QuestModuleDAO questModuleDAO;

    private final TestsService testService;
    private final UserDAO userDAO;


    public PassingService(QuestModuleDAO questModuleDAO, UserDAO userDAO, TestsService testService) {
        this.questModuleDAO = questModuleDAO;
        this.userDAO = userDAO;
        this.testService = testService;
    }
    public PassedTestDto passQuestModule(Integer moduleId, Map<Integer,Integer> answers) throws AuthenticationException {
        JwtAuthentication authentication= (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = ((ExtendUserDetails) authentication.getPrincipal()).getId();
        User user=userDAO.getUserById(userId);
        Integer count=questModuleDAO.getUserTestCount(moduleId,userId);
        QuestModule questModule=questModuleDAO.getQuestModuleById(moduleId);
        questModule.setTests(testService.findByQuestModuleId(moduleId));
        Date date=new Date();
        PassedTestDto passedTestDto=new PassedTestDto();
        if(count>=questModule.getTrialCount()){
            throw new AuthenticationException("You have already passed this quest module");
        }

        if(date.after(questModule.getEndDate())){
            throw new AuthenticationException("Time has expired");
        }

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
        passedTestDto.setPassed(true);
        passedTestDto.setCorrectAnswers(correctAnswers);
        passedTestDto.setTotalScores(usersScore);
        if(usersScore<=questModule.getPassingScores()){
            passedTestDto.setPassed(false);
        }
        if(count==0){
            userDAO.updateUserBalance(userId,user.getBalance()+usersScore);
        }

        return passedTestDto;


    }

}
