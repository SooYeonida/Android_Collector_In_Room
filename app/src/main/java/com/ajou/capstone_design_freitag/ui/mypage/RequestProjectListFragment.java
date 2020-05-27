package com.ajou.capstone_design_freitag.ui.mypage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.plus.Project;

import java.util.ArrayList;


public class RequestProjectListFragment extends Fragment {

    ArrayList<Project> projectArrayList = new ArrayList<>();
    ProjectAdapter projectAdapter;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_project_list, container, false);

        projectAdapter = new ProjectAdapter(projectArrayList);
        listView  = view.findViewById(R.id.project_list_mypage);

        projectList(view);
        projectAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        return view;
    }

    public void projectList(final View view){
        //projectAdapter.addItem();
    }

    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        com.ajou.capstone_design_freitag.ui.search.ProjectAdapter projectAdapter = (com.ajou.capstone_design_freitag.ui.search.ProjectAdapter) listView.getAdapter();

        int totalHeight = 0;
        for (int i = 0; i < projectAdapter.getCount(); i++) {
            View listItem = projectAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (projectAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
