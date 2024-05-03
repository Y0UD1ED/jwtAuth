package com.example.jwtAuth.dtos;

import java.util.List;

public class CourseResponse {
    Integer courseId;
    String courseName;
    Integer infoModuleId;
    List<Integer> questModuleIds;

    Integer scores;

    Integer totalScore;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getInfoModuleId() {
        return infoModuleId;
    }

    public void setInfoModuleId(Integer infoModuleId) {
        this.infoModuleId = infoModuleId;
    }

    public List<Integer> getQuestModuleIds() {
        return questModuleIds;
    }

    public void setQuestModuleIds(List<Integer> questModuleIds) {
        this.questModuleIds = questModuleIds;
    }

    public Integer getScores() {
        return scores;
    }

    public void setScores(Integer scores) {
        this.scores = scores;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
}
