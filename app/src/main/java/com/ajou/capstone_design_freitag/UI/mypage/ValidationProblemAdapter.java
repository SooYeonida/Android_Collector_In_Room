package com.ajou.capstone_design_freitag.UI.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import java.util.ArrayList;

public class ValidationProblemAdapter extends BaseAdapter {

    private ArrayList<Project> projectArrayList = new ArrayList<>();
    //클래스바꿔야할듯

    public ValidationProblemAdapter(ArrayList<Project> projectList){
        if(projectArrayList == null){
            projectArrayList = new ArrayList<>();
        }
        else{
            projectArrayList = projectList;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_validated_problem_list,parent,false);
        }
        TextView problemNum = convertView.findViewById(R.id.validation_problem_num);

        return null;
    }


    @Override
    public int getCount() {
        return projectArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return projectArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(Project project){

        project.setProjectIcon(project.getProjectIcon());
        project.setProjectName(project.getProjectName());
        project.setWorkType(project.getWorkType());
        project.setDataType(project.getDataType());

        projectArrayList.add(project);
    }
}
