package com.example.jwtAuth.models;

public class Answer {
    BaseUser user;

    QuestModule questModule;

    Option option;

    public BaseUser getUser() {
        return user;
    }

    public void setUser(BaseUser user) {
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
