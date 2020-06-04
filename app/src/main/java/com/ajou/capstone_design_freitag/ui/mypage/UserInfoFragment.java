package com.ajou.capstone_design_freitag.ui.mypage;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.dto.User;

import org.json.JSONException;

public class UserInfoFragment extends Fragment {

    Button update;
    Button exchange;
    MyPageFragment myPageFragment;

    TextView userName;
    TextView userEmail;
    TextView userPhoneNumber;
    TextView userAffiliation;
    TextView userLevel;
    TextView userPoint;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        userName = (TextView) view.findViewById(R.id.user_name_mypage);
        userEmail = (TextView) view.findViewById(R.id.user_email_mypage);
        userPhoneNumber = (TextView) view.findViewById(R.id.user_phone_mypage);
        userAffiliation = (TextView) view.findViewById(R.id.user_affiliation_mypage);
        userLevel = (TextView) view.findViewById(R.id.user_level_mypage);
        userPoint = (TextView) view.findViewById(R.id.user_point_mypage);
        update = (Button)view.findViewById(R.id.update_user_info_btn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo(v);
            }
        });
        exchange = (Button) view.findViewById(R.id.exchange_point_btn);
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangePoint(v);
            }
        });

        RESTAPI instance = RESTAPI.getInstance();
        if(instance.getToken()!=null){
            getMyPage(view);
        }
        return view;
    }

    public void getMyPage(final View view){
        AsyncTask<String, Void,User> mypageTask = new AsyncTask<String, Void, User>() {
            protected User doInBackground(String... UserInfos) {
                User user = null;
                try {
                    user = RESTAPI.getInstance().mypage(UserInfos[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return user;
            }
            @Override
            protected void onPostExecute(User user) {
                userName.setText(user.getName());
                userEmail.setText(user.getEmail());
                userPhoneNumber.setText(user.getPhonenumber());
                userAffiliation.setText(user.getAffiliation());
                userLevel.setText(user.getLevel());
                userPoint.setText("0"); //포인트 안받아와서 일단은 이렇게 해놓음
            }

        };
        mypageTask.execute(User.getUserinstance().getUserID());
    }


    public void updateUserInfo(View view){
        myPageFragment = (MyPageFragment) getParentFragment();
        myPageFragment.replaceFragment(1);
    }

    public void exchangePoint(View view){

    }

}
