package com.ajou.capstone_design_freitag.UI.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassDto implements Parcelable {
    private int projectId;
    private String className;

    public ClassDto(Parcel in) {
        projectId = in.readInt();
        className = in.readString();
    }

    public static final Creator<ClassDto> CREATOR = new Creator<ClassDto>() {
        @Override
        public ClassDto createFromParcel(Parcel in) {
            return new ClassDto(in);
        }

        @Override
        public ClassDto[] newArray(int size) {
            return new ClassDto[size];
        }
    };

    public ClassDto() {

    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(projectId);
        dest.writeString(className);
    }
}
