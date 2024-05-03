package com.example.jwtAuth.models;


import java.util.List;

public class QuestModule {

    Integer questModuleId;
    Integer courseId;
    Integer scores;
    Integer position;

    String name;

    List<Test> tests;
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getScores() {
        return scores;
    }

    public void setScores(Integer scores) {
        this.scores = scores;
    }


    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuestModuleId() {
        return questModuleId;
    }

    public void setQuestModuleId(Integer questModuleId) {
        this.questModuleId = questModuleId;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}
