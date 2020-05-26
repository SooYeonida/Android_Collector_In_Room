package com.ajou.capstone_design_freitag.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.plus.CreationTypeFragment;
import com.ajou.capstone_design_freitag.ui.search.ProjectTypeFragment;

public class HomeFragment extends Fragment {


    HomeMenuFragment homeMenuFragment = new HomeMenuFragment();
    ProjectTypeFragment projectTypeFragment = new ProjectTypeFragment();
    CreationTypeFragment creationTypeFragment = new CreationTypeFragment();
    FragmentTransaction fragmentTransaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        replaceFragment(0);
        return view;
    }

    public void replaceFragment(int index) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();

        if(index==0){
            if(!homeMenuFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_home, homeMenuFragment);
                fragmentTransaction.commit();
            }
        }
        else if(index == 1){
            if(!projectTypeFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_home,projectTypeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 2) {
            if(!creationTypeFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_home,creationTypeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

    }


}
