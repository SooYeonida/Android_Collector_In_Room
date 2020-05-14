package com.ajou.capstone_design_freitag.ui.plus;

import android.graphics.drawable.Drawable;

public class Project {
    private static Project projectinstance = null;

    public static Project getProjectinstance(){
        if(projectinstance == null){
            projectinstance = new Project();
        }
        return projectinstance;
    }

    private Drawable projectIcon; //빼도됨
    private int projectId;
    private String userId;
    private String projectName;
    private String bucketName;
    private String status;
    private String workType; //collection, boundingBox, classification
    private String dataType; //image, audio, text
    private String subject;
    private int difficulty;
    private String wayContent;
    private String conditionContent;
    private String exampleContent;
    private String description;
    private int totalData;
    private int progressData;
    private int cost;


    public Drawable getProjectIcon() {
        return projectIcon;
    }

    public void setProjectIcon(Drawable projectIcon) {
        this.projectIcon = projectIcon;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getWayContent() {
        return wayContent;
    }

    public void setWayContent(String wayContent) {
        this.wayContent = wayContent;
    }

    public String getConditionContent() {
        return conditionContent;
    }

    public void setConditionContent(String conditionContent) {
        this.conditionContent = conditionContent;
    }

    public String getExampleContent() {
        return exampleContent;
    }

    public void setExampleContent(String exampleContent) {
        this.exampleContent = exampleContent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getProgressData() {
        return progressData;
    }

    public void setProgressData(int progressData) {
        this.progressData = progressData;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
