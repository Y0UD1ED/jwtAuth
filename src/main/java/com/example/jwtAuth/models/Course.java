package com.example.jwtAuth.models;

import java.util.List;

public class Course {
    Integer id;

    String name;
    String description;

    Level level;
    Direction direction;

    InfoModule infoModule;

    List<QuestModule> questModules;

    public Course() {
    }

    public Course(String name, String description, Level level, Direction direction) {
        this.name = name;
        this.description = description;
        this.level = level;
        this.direction = direction;
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

    public List<QuestModule> getQuestModules() {
        return questModules;
    }

    public void setQuestModules(List<QuestModule> questModules) {
        this.questModules = questModules;
    }
}
