package com.ajou.capstone_design_freitag.ui.mypage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    int numoftabs;

    public PagerAdapter(FragmentManager fragmentManager, int numtab){
        super(fragmentManager);
        this.numoftabs = numtab;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0 :
                ProjectFragment projectFragment = new ProjectFragment();
                return projectFragment;
            case 1:
                ProgressWorkFragment progressWorkFragment = new ProgressWorkFragment();
                return progressWorkFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return numoftabs;
    }
}
