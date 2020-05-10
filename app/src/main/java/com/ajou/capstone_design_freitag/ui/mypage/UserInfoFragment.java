package com.ajou.capstone_design_freitag.ui.mypage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.home.User;

public class UserInfoFragment extends Fragment  {

    Button changeUserInfo;
    Button point;
    private Context context;
    private User user = new User();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_info, container, false);

        TextView userEmail = (TextView) view.findViewById(R.id.user_email_mypage);
        TextView userName = (TextView) view.findViewById(R.id.user_name_mypage);
        TextView userLevel = (TextView) view.findViewById(R.id.user_level_mypage);
        TextView userPoint = (TextView) view.findViewById(R.id.user_point_mypage);

        //임의로 유저 데이터 생성
        addUser("nahyn858@ajou.ac.kr","나효니","starter",1599);

        userEmail.setText(user.getEmail());
        userName.setText(user.getName());
        userLevel.setText(user.getLevel());
        userPoint.setText(Integer.toString(user.getPoint()));


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


    public void addUser(String user_email,String user_name,String user_level,int user_point){
        User u = new User();
        u.setEmail(user_email);
        u.setName(user_name);
        u.setPoint(user_point);
        u.setLevel(user_level);

        user = u;
    }

    public void changeUserInfo(View view){
        //rest api
    }

    public void exchangePoint(View view){
        //rest api

    }
}
