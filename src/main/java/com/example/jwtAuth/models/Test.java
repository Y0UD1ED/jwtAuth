package com.example.jwtAuth.models;

import java.util.List;
import java.util.Map;


public class Test {

    Integer testId;
    Integer questModuleId;

    String quest;
    Integer questPosition;
    Map<Integer,Option> optionList;

    public Integer getQuestModuleId() {
        return questModuleId;
    }

    public void setQuestModuleId(Integer questModuleId) {
        this.questModuleId = questModuleId;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public Map<Integer, Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(Map<Integer, Option> optionList) {
        this.optionList = optionList;
    }

    public Integer getQuestPosition() {
        return questPosition;
    }

    public void setQuestPosition(Integer questPosition) {
        this.questPosition = questPosition;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }
}
