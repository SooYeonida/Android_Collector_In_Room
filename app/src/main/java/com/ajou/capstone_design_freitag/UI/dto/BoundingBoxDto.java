package com.ajou.capstone_design_freitag.UI.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class BoundingBoxDto implements Parcelable {

    private int boxId;
    private int problemId;
    private String className;
    private String coordinates;

    public BoundingBoxDto(Parcel in) {
        boxId = in.readInt();
        problemId = in.readInt();
        className = in.readString();
        coordinates = in.readString();
    }

    public static final Creator<BoundingBoxDto> CREATOR = new Creator<BoundingBoxDto>() {
        @Override
        public BoundingBoxDto createFromParcel(Parcel in) {
            return new BoundingBoxDto(in);
        }

        @Override
        public BoundingBoxDto[] newArray(int size) {
            return new BoundingBoxDto[size];
        }
    };

    public BoundingBoxDto() {

    }

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(boxId);
        dest.writeInt(problemId);
        dest.writeString(className);
        dest.writeString(coordinates);
    }
}
