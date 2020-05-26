package com.ajou.capstone_design_freitag.ui.plus;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.ajou.capstone_design_freitag.R;

public class PlusFragment extends Fragment  {

    ProjectMakeFragment projectMakeFragment = new ProjectMakeFragment();
    PayFragment payFragment = new PayFragment();
    CreationTypeFragment creationTypeFragment = new CreationTypeFragment();

    FragmentTransaction fragmentTransaction;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plus, container, false);
        replaceFragment(4);
        return view;
    }


    public void replaceFragment(int index) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (index == 0) {
            if (!projectMakeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_plus, ProjectMakeFragment.newInstance("collection"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if(index == 1){
            if (!projectMakeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_plus, ProjectMakeFragment.newInstance("labelling"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if(index == 2){
            if (!projectMakeFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_plus, ProjectMakeFragment.newInstance("both"));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 3) {
            if (!payFragment.isAdded()) {
                fragmentTransaction.replace(R.id.fragment_plus, payFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if(index == 4){
            if(!creationTypeFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_plus, creationTypeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

    }



}
