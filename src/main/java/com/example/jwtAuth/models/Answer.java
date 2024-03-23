package com.example.jwtAuth.models;

public class Answer {
    User user;

    QuestModule questModule;

    Option option;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public QuestModule getQuestModule() {
        return questModule;
    }

    public void setQuestModule(QuestModule questModule) {
        this.questModule = questModule;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }
}
