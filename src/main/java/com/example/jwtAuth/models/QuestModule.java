package com.example.jwtAuth.models;


import java.util.Date;
import java.util.List;

public class QuestModule {

    Integer id;
    Integer scores;

    Integer passingScores;

    Integer trialCount;
    Integer position;

    String name;

    List<Test> tests;

    Date startDate;
    Date endDate;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public Integer getPassingScores() {
        return passingScores;
    }

    public void setPassingScores(Integer passingScores) {
        this.passingScores = passingScores;
    }

    public Integer getTrialCount() {
        return trialCount;
    }

    public void setTrialCount(Integer trialCount) {
        this.trialCount = trialCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
