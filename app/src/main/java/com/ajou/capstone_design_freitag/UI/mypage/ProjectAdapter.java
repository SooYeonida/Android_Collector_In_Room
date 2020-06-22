package com.ajou.capstone_design_freitag.UI.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import java.util.ArrayList;

public class ProjectAdapter extends BaseAdapter {

    private ArrayList<Project> projectArrayList = new ArrayList<>();

    public ProjectAdapter(ArrayList<Project> projectList){
        if(projectArrayList == null){
            projectArrayList = new ArrayList<>();
        }
        else{
            projectArrayList = projectList;
        }
    }

    @Override
    public int getCount(){
        return projectArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_project_info_mypage,parent,false);
        }

        ImageView projectTypeView = (ImageView) convertView.findViewById(R.id.project_icon_mypage);
        TextView projectName = (TextView) convertView.findViewById(R.id.project_name_mypage);
        TextView projectType = (TextView) convertView.findViewById(R.id.project_type_mypage);
        TextView workType = (TextView) convertView.findViewById(R.id.data_type_mypage);

        Project project = projectArrayList.get(position);

        projectTypeView.setImageDrawable(project.getProjectIcon());
        projectName.setText(project.getProjectName());
        projectType.setText(project.getWorkType());
        workType.setText(project.getDataType());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return projectArrayList.get(position);
    }

    public void addItem(Project project){

        project.setProjectIcon(project.getProjectIcon());
        project.setProjectName(project.getProjectName());
        project.setWorkType(project.getWorkType());
        project.setDataType(project.getDataType());

        projectArrayList.add(project);
    }
}
