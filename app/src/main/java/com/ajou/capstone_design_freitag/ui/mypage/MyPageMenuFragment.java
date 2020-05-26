package com.ajou.capstone_design_freitag.ui.mypage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ajou.capstone_design_freitag.R;

public class MyPageMenuFragment extends Fragment {

    Button user_info;
    Button request_project_list;
    Button complete_work_list;

    MyPageFragment myPageFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page_menu, container, false);

        user_info = view.findViewById(R.id.user_info_btn);
        request_project_list = view.findViewById(R.id.request_project_list);
        complete_work_list = view.findViewById(R.id.complete_work_list);

        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPageFragment = (MyPageFragment)getParentFragment();
                myPageFragment.replaceFragment(3);
            }
        });

        request_project_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPageFragment = (MyPageFragment)getParentFragment();
                myPageFragment.replaceFragment(4);
            }
        });

        complete_work_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPageFragment = (MyPageFragment)getParentFragment();
                myPageFragment.replaceFragment(5);
            }
        });
        return view;
    }
}
