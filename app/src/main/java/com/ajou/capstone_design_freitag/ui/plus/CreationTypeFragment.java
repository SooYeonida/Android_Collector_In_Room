package com.ajou.capstone_design_freitag.ui.plus;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ajou.capstone_design_freitag.R;

public class CreationTypeFragment extends Fragment {

    Button creation_collection;
    Button creation_labelling;
    Button creation_colla;

    PlusFragment plusFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation_type, container, false);

        creation_collection = view.findViewById(R.id.creation_collection_project);
        creation_labelling = view.findViewById(R.id.creation_labelling_project);
//        creation_colla = view.findViewById(R.id.creation_colla_project);

        creation_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusFragment = (PlusFragment)getParentFragment();
                plusFragment.replaceFragment(0);

            }
        });
        creation_labelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusFragment = (PlusFragment)getParentFragment();
                plusFragment.replaceFragment(1);
            }
        });
//        creation_colla.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                plusFragment = (PlusFragment)getParentFragment();
//                plusFragment.replaceFragment(2);
//            }
//        });

        return view;
    }
}
