package com.example.jwtAuth.services;

import com.example.jwtAuth.dao.OptionDAO;
import com.example.jwtAuth.dao.TestDAO;
import com.example.jwtAuth.models.Option;
import com.example.jwtAuth.models.Test;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TestsService {
    private final TestDAO testDAO;

    private final OptionDAO optionDAO;

    public TestsService(TestDAO questModuleDAO, OptionDAO optionDAO) {
        this.testDAO = questModuleDAO;
        this.optionDAO=optionDAO;
    }

    public List<Test> findByQuestModuleId(Integer moduleId){
        List<Test> testList = testDAO.getByQuestModuleId(moduleId);
        for (Test quest: testList) {
            quest.setOptionList(this.findOptionByQuestId(quest.getTestId()));
        }
        return testList;
    }

    public void save(List<Test> testList){
        for(Test quest: testList){
            Integer testId=testDAO.addTest(quest);
            for(Integer optionId:quest.getOptionList().keySet()){
                Option option = quest.getOptionList().get(optionId);
                optionDAO.addOption(option,testId);
            }

        }
    }
    private Map<Integer,Option> findOptionByQuestId(Integer questId){
        return optionDAO.getByQuestId(questId);
    }
}
