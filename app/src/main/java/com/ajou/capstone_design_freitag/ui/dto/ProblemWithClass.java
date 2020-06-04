package com.ajou.capstone_design_freitag.ui.dto;


import java.util.List;

public class ProblemWithClass {

    private Problem problem;
    private List<ClassDto> classNameList;

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

}
