package com.ajou.capstone_design_freitag.UI.search;

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
    ProjectListFragment projectListFragment = new ProjectListFragment();
    DataTypeFragment dataTypeFragment = new DataTypeFragment();
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
        }
        else if (index == 1) { //리스트
            if (!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, ProjectListFragment.newInstance("수집"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 2) { //리스트
            if (!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, ProjectListFragment.newInstance("라벨링")); //바운딩 박스만
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 3){
            if (!dataTypeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, dataTypeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

}
