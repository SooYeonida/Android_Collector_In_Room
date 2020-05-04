package com.ajou.capstone_design_freitag.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.R;

public class ProjectListFragment extends Fragment {

    private Context context;
    View view;

    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_project_list,container,false);
        return view;
    }
}
