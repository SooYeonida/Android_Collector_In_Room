package com.ajou.capstone_design_freitag.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.R;

public class CollectionDataTypeFragment extends Fragment {

    Button data_image_btn;
    Button data_text_btn;
    Button data_voice_btn;
    View view;
    SearchFragment searchFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_collection_data_type,container,false);
        data_image_btn = (Button) view.findViewById(R.id.data_image_btn);
        data_text_btn = (Button) view.findViewById(R.id.data_text_btn);
        data_voice_btn = (Button) view.findViewById(R.id.data_voice_btn);

        data_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment = (SearchFragment)getParentFragment();
                searchFragment.replaceFragment(3);
            }
        });
        data_text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment = (SearchFragment)getParentFragment();
                searchFragment.replaceFragment(4);
            }
        });
        data_voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment = (SearchFragment)getParentFragment();
                searchFragment.replaceFragment(5);
            }
        });

        return view;
    }

}
