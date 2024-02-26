package com.example.jwtAuth.models;

public class InfoModule{
    Integer moduleId;
    Integer infoId;
    Integer contentPosition;
    String contentType;
    String content;

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public Integer getContentPosition() {
        return contentPosition;
    }

    public void setContentPosition(Integer contentPosition) {
        this.contentPosition = contentPosition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}
