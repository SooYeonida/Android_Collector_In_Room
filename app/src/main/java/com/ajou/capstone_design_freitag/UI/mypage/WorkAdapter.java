package com.ajou.capstone_design_freitag.UI.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.WorkHistory;

import java.util.ArrayList;

public class WorkAdapter extends BaseAdapter {

    private ArrayList<WorkHistory> workHistoryArrayList = new ArrayList<>();

    public WorkAdapter(ArrayList<WorkHistory> workList){
        if(workHistoryArrayList==null){
            workHistoryArrayList = new ArrayList<>();
        }
        else{
            workHistoryArrayList = workList;
        }
    }

    @Override
    public int getCount() {
        return workHistoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return workHistoryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_work_info,parent,false);
        }

        ImageView projectTypeView = (ImageView) convertView.findViewById(R.id.project_icon_workhistory);
        TextView projectName = (TextView) convertView.findViewById(R.id.project_name_workhistory);
        TextView projectRequester = (TextView) convertView.findViewById(R.id.project_requester_workhistory);
        TextView workType = (TextView) convertView.findViewById(R.id.work_type_workhistory);
        TextView dataType = (TextView) convertView.findViewById(R.id.data_type_workhistory);
        TextView status = (TextView) convertView.findViewById(R.id.status_workhistory);
        WorkHistory workHistory = workHistoryArrayList.get(position);

        projectTypeView.setImageDrawable(workHistory.getProjectIcon());
        projectName.setText(workHistory.getProjectName());
        projectRequester.setText(workHistory.getProjectRequester());
        workType.setText(workHistory.getProjectWorkType());
        dataType.setText(workHistory.getProjectDataType());
        status.setText(workHistory.getProjectStatus());

        return convertView;
    }

    public void addItem(WorkHistory workHistory){

        workHistory.setProjectIcon(workHistory.getProjectIcon());
        workHistory.setProjectName(workHistory.getProjectName());
        workHistory.setProjectRequester(workHistory.getProjectRequester());
        workHistory.setProjectWorkType(workHistory.getProjectWorkType());
        workHistory.setProjectDataType(workHistory.getProjectDataType());
        workHistory.setProjectStatus(workHistory.getProjectStatus());

        workHistoryArrayList.add(workHistory);
    }
}
