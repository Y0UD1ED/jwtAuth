package com.example.jwtAuth.models;

public class QuestModulePassing {
    Integer questModuleId;

    Integer userId;

    Integer score;

    public Integer getQuestModuleId() {
        return questModuleId;
    }

    public void setQuestModuleId(Integer questModuleId) {
        this.questModuleId = questModuleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
