package com.ajou.capstone_design_freitag.ui.mypage;

import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ajou.capstone_design_freitag.R;
import com.google.android.material.tabs.TabLayout;

public class MyPageFragment extends Fragment {

    UserInfoFragment userInfoFragment = new UserInfoFragment();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("완료한 작업 목록"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        //어뎁터 부분
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(),1);
        viewPager.setAdapter(pagerAdapter);

        //탭 선택 이벤트처리
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        return view;
    }



}
