package com.ajou.capstone_design_freitag.ui.dto;

import android.graphics.drawable.Drawable;

public class WorkHistory {

    private Drawable projectIcon; //빼도됨
    private String projectRequester;    // 작업자가 참여한 프로젝트의 의뢰자 이름
    private String projectName;         // 작업자가 참여한 프로젝트의 이름
    private String projectWorkType;     // 작업자가 참여한 프로젝트의 종류(수집/라벨링)
    private String projectDataType;     // 작업자가 참여한 프로젝트의 데이터 종류(이미지, 음성, 텍스트) 혹은 라벨링 종류(분류, 바운딩박스)
    private String projectStatus;       // 작업자가 참여한 프로젝트의 진행 정도
    private int problemId;              // 작업자가 작업한 문제의 번호

    public String getProjectRequester() {
        return projectRequester;
    }

    public void setProjectRequester(String projectRequester) {
        this.projectRequester = projectRequester;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectWorkType() {
        return projectWorkType;
    }

    public void setProjectWorkType(String projectWorkType) {
        this.projectWorkType = projectWorkType;
    }

    public String getProjectDataType() {
        return projectDataType;
    }

    public void setProjectDataType(String projectDataType) {
        this.projectDataType = projectDataType;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }
    public Drawable getProjectIcon() {
        return projectIcon;
    }

    public void setProjectIcon(Drawable projectIcon) {
        this.projectIcon = projectIcon;
    }

}
