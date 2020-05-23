package com.ajou.capstone_design_freitag.ui.plus;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;


public class Project implements Parcelable {
    private static Project projectinstance = null;

    public Project() {

    }

    public static Project getProjectinstance(){
        if(projectinstance == null){
            projectinstance = new Project();
        }
        return projectinstance;
    }

    protected Project(Parcel in) {
        projectId = in.readString();
        userId = in.readString();
        projectName = in.readString();
        bucketName = in.readString();
        status = in.readString();
        workType = in.readString();
        dataType = in.readString();
        subject = in.readString();
        difficulty = in.readInt();
        wayContent = in.readString();
        conditionContent = in.readString();
        exampleContent = in.readString();
        description = in.readString();
        totalData = in.readInt();
        progressData = in.readInt();
        cost = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(projectId);
        dest.writeString(userId);
        dest.writeString(projectName);
        dest.writeString(bucketName);
        dest.writeString(status);
        dest.writeString(workType);
        dest.writeString(dataType);
        dest.writeString(subject);
        dest.writeInt(difficulty);
        dest.writeString(wayContent);
        dest.writeString(conditionContent);
        dest.writeString(exampleContent);
        dest.writeString(description);
        dest.writeInt(totalData);
        dest.writeInt(progressData);
        dest.writeInt(cost);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    private Drawable projectIcon; //빼도됨
    private String projectId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
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
