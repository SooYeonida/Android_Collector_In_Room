package com.ajou.capstone_design_freitag.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ajou.capstone_design_freitag.R;

public class SearchFragment extends Fragment {

    ProjectTypeFragment projectTypeFragment = new ProjectTypeFragment();
    CollectionDataTypeFragment dataTypeFragment = new CollectionDataTypeFragment();
    ProjectListFragment projectListFragment = new ProjectListFragment();
    LabellingTypeFragment labellingTypeFragment = new LabellingTypeFragment();


    FragmentTransaction fragmentTransaction;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);
        replaceFragment(0);
        return view;
    }

    public void replaceFragment(int index) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (index == 0) {
            if (!projectTypeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, projectTypeFragment);
                fragmentTransaction.commit();
            }
        } else if (index == 1) {
            if (!dataTypeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, dataTypeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else if (index == 2) {
            if (!labellingTypeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, labellingTypeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 3) { //이미지
            if (!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search,ProjectListFragment.newInstance("수집","이미지"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 4) { //텍스트
            if (!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, ProjectListFragment.newInstance("수집","텍스트"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 5) { //음성
            if (!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, ProjectListFragment.newInstance("수집","음성"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 6) { //바운딩박스
            if (!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, ProjectListFragment.newInstance("라벨링","바운딩박스"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 7) { //분류
            if (!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, ProjectListFragment.newInstance("라벨링","분류"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

}
