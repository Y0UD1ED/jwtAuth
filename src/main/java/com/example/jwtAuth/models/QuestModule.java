package com.example.jwtAuth.models;

import java.util.List;

public class QuestModule{

    Integer moduleId;
    Integer questId;

    String quest;
    Integer questPosition;
    List<Option> optionList;

    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public String getQuest() {
        return quest;
    }

    public void setQuest(String quest) {
        this.quest = quest;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public Integer getQuestPosition() {
        return questPosition;
    }

    public void setQuestPosition(Integer questPosition) {
        this.questPosition = questPosition;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}
