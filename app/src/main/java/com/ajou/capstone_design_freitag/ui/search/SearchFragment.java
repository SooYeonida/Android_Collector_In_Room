package com.ajou.capstone_design_freitag.ui.search;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ajou.capstone_design_freitag.R;

//주제 검색기능 넣어야 할 것 같음 -> 검색바

public class SearchFragment extends Fragment  {

    ProjectTypeFragment projectTypeFragment = new ProjectTypeFragment();
    DataTypeFragment dataTypeFragment = new DataTypeFragment();
    ProjectListFragment projectListFragment = new ProjectListFragment();

    FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_search,container,false);
        replaceFragment(0);
        return view;
    }

    public void replaceFragment(int index){
        fragmentTransaction = getChildFragmentManager().beginTransaction();

        if(index==0){
            if(!projectTypeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, projectTypeFragment);
                fragmentTransaction.commit();
            }
        }
        else if(index==1){
            if(!dataTypeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, dataTypeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if(index==2){
            if(!projectListFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_search, projectListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

    }

}
