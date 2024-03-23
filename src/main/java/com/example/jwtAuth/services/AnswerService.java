package com.example.jwtAuth.services;


import com.example.jwtAuth.dao.OptionDAO;
import com.example.jwtAuth.dao.QuestModuleDAO;
import com.example.jwtAuth.dao.UserDAO;
import com.example.jwtAuth.models.Answer;


import java.util.List;

public class AnswerService {
    private final UserDAO userDAO;
    private final QuestModuleDAO questModuleDAO;

    private final OptionDAO optionDAO;

    public AnswerService(UserDAO userDAO, QuestModuleDAO questModuleDAO, OptionDAO optionDAO) {
        this.userDAO = userDAO;
        this.questModuleDAO = questModuleDAO;
        this.optionDAO = optionDAO;
    }

    public List<Answer> findByUserIdAndModuleId(Integer userId,Integer moduleId){
        return null;
    }
}
