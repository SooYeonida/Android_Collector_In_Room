package com.ajou.capstone_design_freitag.ui.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ajou.capstone_design_freitag.ClassificationActivity;
import com.ajou.capstone_design_freitag.ProjectDetailActivity;
import com.ajou.capstone_design_freitag.R;

public class DataTypeFragment extends Fragment {

    Button classfication_btn;
    Button boundingbox_btn;

    SearchFragment searchFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_data_type, container, false);
       classfication_btn = (Button)view.findViewById(R.id.classification_btn);
       boundingbox_btn = (Button) view.findViewById(R.id.boundingbox_btn);

       classfication_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), ClassificationActivity.class);
               startActivity(intent);
           }
       });

       boundingbox_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               searchFragment = (SearchFragment)getParentFragment();
               searchFragment.replaceFragment(2);
           }
       });

       return view;
    }
}
