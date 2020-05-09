package com.ajou.capstone_design_freitag.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.plus.Project;

import java.util.ArrayList;

public class ProjectListFragment extends Fragment {
//프로젝트 설명이런것도 추가해야함.

    ProjectAdapter projectAdapter;
    ProjectAdapter sort;
    private static final String param1 = "projecttype";
    private static final String param2 = "worktype";
    private String project;
    private String work;

    ArrayList<Project> projectArrayList = new ArrayList<Project>();

    public static ProjectListFragment newInstance(String projecttype, String worktype) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putString(param1, projecttype);
        args.putString(param2, worktype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            project = getArguments().getString(param1);
            work = getArguments().getString(param2);
        }

    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_project_list,container,false);

        projectAdapter = new ProjectAdapter(projectArrayList);
        ListView listView = view.findViewById(R.id.project_list);

        //프로젝트 임시로 생성
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_image_black_24dp),"강아지 사진 수집 프로젝트","수집","이미지");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_text_black_24dp),"김나현 사진 수집 프로젝트","수집","이미지");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_label_black_24dp),"욕하는 김나현 라벨링 프로젝트","라벨링","바운딩박스");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_voice_black_24dp),"과제하는 김나현 사진 수집 프로젝트","수집","이미지");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_voice_black_24dp),"김나현 노래 수집 프로젝트","수집","음성");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_text_black_24dp),"김나현 카톡 메시지 수집 프로젝트","수집","텍스트");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_image_black_24dp),"고양이 사진 수집 프로젝트","수집","이미지");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_label_black_24dp),"자동차 라벨링 프로젝트","라벨링","분류");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_label_black_24dp),"신호등 라벨링 프로젝트","라벨링","분류");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_text_black_24dp),"상황에 따른 대답 수집 프로젝트","수집","텍스트");
        projectAdapter.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.ic_voice_black_24dp),"신생아 울음소리 수집 프로젝트","수집","음성");

        projectAdapter.notifyDataSetChanged();

        sort = new ProjectAdapter(projectAdapter.sort(project,work));
        listView.setAdapter(sort);

        setListViewHeightBasedOnChildren(listView);

        return view;
    }


    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        ProjectAdapter projectAdapter = (ProjectAdapter) listView.getAdapter();

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
