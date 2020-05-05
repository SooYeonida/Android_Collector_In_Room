package com.ajou.capstone_design_freitag.ui.mypage;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.R;

public class UserInfoFragment extends Fragment  {

    Button changeUserInfo;
    Button point;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_info, container, false);
        changeUserInfo = (Button)view.findViewById(R.id.change_user_info_btn);
        point = (Button) view.findViewById(R.id.exchange_point_btn);

        changeUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserInfo(v);
            }
        });
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangePoint(v);
            }
        });

        return view;
    }

    public void changeUserInfo(View view){
        //rest api
    }

    public void exchangePoint(View view){
        //rest api

    }
}
