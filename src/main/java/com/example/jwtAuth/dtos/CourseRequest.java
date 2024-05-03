package com.example.jwtAuth.dtos;

import java.util.List;

public class CourseRequest {
    Integer directionId;
    Integer levelId;
    String name;
    String infoModuleText;

    String description;

    List<Integer> userIds;

    public Integer getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Integer directionId) {
        this.directionId = directionId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getInfoModuleText() {
        return infoModuleText;
    }

    public void setInfoModuleText(String infoModuleText) {
        this.infoModuleText = infoModuleText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}

