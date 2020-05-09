package com.ajou.capstone_design_freitag.ui.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.plus.Project;

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
        projectName.setText(project.getName());
        projectType.setText(project.getWorkType());
        workType.setText(project.getWorkType());

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

    public ArrayList<Project> sort(String projecttype, String worktype){
        ArrayList<Project> list = new ArrayList<Project>();
        for(Project element:projectArrayList){
            if(element.getWorkType().equals(projecttype)&&element.getWorkType().equals(worktype)){
                list.add(element);
            }
        }
        return list;
    }

    public void addItem(Drawable project_icon, String project_name,String project_type,String project_data_type){
        Project project = new Project();

        project.setProjectIcon(project_icon);
        project.setName(project_name);
        project.setWorkType(project_type);
        project.setWorkType(project_data_type);

        projectArrayList.add(project);
    }

}
