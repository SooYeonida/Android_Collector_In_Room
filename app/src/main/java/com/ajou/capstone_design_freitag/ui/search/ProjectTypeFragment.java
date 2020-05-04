package com.ajou.capstone_design_freitag.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.R;

public class ProjectTypeFragment extends Fragment {

    Button data_collection_btn;
    Button data_labelling_btn;

    SearchFragment searchFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_project_type,container,false);
        data_collection_btn = (Button) view.findViewById(R.id.data_collection_btn);
        data_labelling_btn = (Button) view.findViewById(R.id.data_labelling_btn);

        data_collection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("수집버튼호출");
                goToDataType(v);
            }
        });
        data_labelling_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("라벨링버튼호출");
                goToProjectList(v);
            }
        });

        return view;
    }

    public void goToDataType(View view){
        searchFragment = (SearchFragment)getParentFragment();
        searchFragment.replaceFragment(1);
    }
    public void goToProjectList(View view){
        searchFragment = (SearchFragment)getParentFragment();
        searchFragment.replaceFragment(2);
    }
}
