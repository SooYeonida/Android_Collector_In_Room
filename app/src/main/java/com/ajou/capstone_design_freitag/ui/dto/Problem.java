package com.ajou.capstone_design_freitag.ui.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class Problem implements Parcelable {
    private int projectId;
    private int referenceId;
    private String bucketName;
    private String objectName;
    private String finalAnswer;
    private String validationStatus;
    private String userId;

    private int problemId;

    public Problem(Parcel in) {
        projectId = in.readInt();
        referenceId = in.readInt();
        bucketName = in.readString();
        objectName = in.readString();
        finalAnswer = in.readString();
        validationStatus = in.readString();
        userId = in.readString();
        problemId = in.readInt();
    }

    public Problem() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(projectId);
        dest.writeInt(referenceId);
        dest.writeString(bucketName);
        dest.writeString(objectName);
        dest.writeString(finalAnswer);
        dest.writeString(validationStatus);
        dest.writeString(userId);
        dest.writeInt(problemId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Problem> CREATOR = new Creator<Problem>() {
        @Override
        public Problem createFromParcel(Parcel in) {
            return new Problem(in);
        }

        @Override
        public Problem[] newArray(int size) {
            return new Problem[size];
        }
    };

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getFinalAnswer() {
        return finalAnswer;
    }

    public void setFinalAnswer(String finalAnswer) {
        this.finalAnswer = finalAnswer;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
