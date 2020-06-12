package com.ajou.capstone_design_freitag.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.dto.Project;

import java.util.ArrayList;

public class ProjectAdapter  extends BaseAdapter {
    private ArrayList<Project> projectArrayList = new ArrayList<Project>();

    public ProjectAdapter(ArrayList<Project> projectList){
        if(projectList == null){
            projectArrayList = new ArrayList<Project>();
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
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_project_info_projectlist,parent,false);
        }

        ImageView projectTypeView = (ImageView) convertView.findViewById(R.id.project_icon);
        TextView projectName = (TextView) convertView.findViewById(R.id.project_name);
        TextView projectType = (TextView) convertView.findViewById(R.id.project_type);
        TextView workType = (TextView) convertView.findViewById(R.id.data_type);

        Project project = projectArrayList.get(position);

        projectTypeView.setImageDrawable(project.getProjectIcon());
        projectName.setText(project.getProjectName());
        switch (project.getWorkType()){
            case "collection":
                projectType.setText("수집");
                break;
            case "labelling":
                projectType.setText("라벨링");
                break;
        }
        switch (project.getDataType()){
            case "image":
                workType.setText("이미지");
                break;
            case "text":
                workType.setText("텍스트");
                break;
            case "audio":
                workType.setText("음성");
                break;
            case "boundingbox":
                workType.setText("바운딩박스");
                break;
            case "classification":
                workType.setText("분류");
                break;
        }

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
