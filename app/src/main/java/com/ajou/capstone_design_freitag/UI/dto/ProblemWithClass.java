package com.ajou.capstone_design_freitag.UI.dto;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ProblemWithClass implements Parcelable {

    private Problem problem;
    private List<ClassDto> classNameList;
    private List<BoundingBoxDto> boundingBoxList;
    private String conditionContent;

    public ProblemWithClass(Parcel in) {
        problem = in.readParcelable(Problem.class.getClassLoader());
        classNameList = in.createTypedArrayList(ClassDto.CREATOR);
        boundingBoxList = in.createTypedArrayList(BoundingBoxDto.CREATOR);
        conditionContent = in.readString();
    }

    public static final Creator<ProblemWithClass> CREATOR = new Creator<ProblemWithClass>() {
        @Override
        public ProblemWithClass createFromParcel(Parcel in) {
            return new ProblemWithClass(in);
        }

        @Override
        public ProblemWithClass[] newArray(int size) {
            return new ProblemWithClass[size];
        }
    };

    public ProblemWithClass() {

    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public List<ClassDto> getClassNameList() {
        return classNameList;
    }

    public void setClassNameList(List<ClassDto> classNameList) {
        this.classNameList = classNameList;
    }

    public List<BoundingBoxDto> getBoundingBoxList() {
        return boundingBoxList;
    }

    public void setBoundingBoxList(List<BoundingBoxDto> boundingBoxList) {
        this.boundingBoxList = boundingBoxList;
    }

    public String getConditionContent() {
        return conditionContent;
    }

    public void setConditionContent(String conditionContent) {
        this.conditionContent = conditionContent;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(problem, flags);
        dest.writeTypedList(classNameList);
        dest.writeTypedList(boundingBoxList);
        dest.writeString(conditionContent);
    }
}
