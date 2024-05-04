package com.example.jwtAuth.models;

import java.util.List;

public class UserCourse{
    Integer id;

    String name;
    String description;

    Level level;
    Direction direction;

    InfoModule infoModule;

    List<UserQuestModule> questModules;


    public UserCourse() {
    }

    public UserCourse(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.description = course.getDescription();
        this.level = course.getLevel();
        this.direction = course.getDirection();
        this.infoModule = course.getInfoModule();
    }

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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public InfoModule getInfoModule() {
        return infoModule;
    }

    public void setInfoModule(InfoModule infoModule) {
        this.infoModule = infoModule;
    }

    public List<UserQuestModule> getQuestModules() {
        return questModules;
    }

    public void setQuestModules(List<UserQuestModule> questModules) {
        this.questModules = questModules;
    }
}
