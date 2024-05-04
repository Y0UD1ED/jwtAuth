package com.example.jwtAuth.dtos;

import java.util.List;

public class CourseResponse {
    Integer id;
    String name;
    String description;

    Integer infoModuleIds;

    List<Integer> questModuleIds;

    Integer totalScore;
    Integer userScore;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getInfoModuleIds() {
        return infoModuleIds;
    }

    public void setInfoModuleIds(Integer infoModuleIds) {
        this.infoModuleIds = infoModuleIds;
    }

    public List<Integer> getQuestModuleIds() {
        return questModuleIds;
    }

    public void setQuestModuleIds(List<Integer> questModuleIds) {
        this.questModuleIds = questModuleIds;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }
}
