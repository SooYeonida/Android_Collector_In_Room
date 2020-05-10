package com.ajou.capstone_design_freitag.ui.search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ajou.capstone_design_freitag.R;

public class LabellingTypeFragment extends Fragment {

    ProjectListFragment projectListFragment = new ProjectListFragment();

    Button bounding_box_btn;
    Button classification_btn;
    View view;

    SearchFragment searchFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_labelling_type, container, false);
        bounding_box_btn = (Button) view.findViewById(R.id.bounding_box_btn);
        classification_btn = (Button) view.findViewById(R.id.classification_btn);

        bounding_box_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment = (SearchFragment)getParentFragment();
                searchFragment.replaceFragment(6);
            }
        });
        classification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment = (SearchFragment)getParentFragment();
                searchFragment.replaceFragment(7);
            }
        });
        return view;
    }

}
