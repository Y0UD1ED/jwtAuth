package com.example.jwtAuth.models;

import org.springframework.stereotype.Component;

import java.util.List;


public class QuestModule{

    Integer courseId;
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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
