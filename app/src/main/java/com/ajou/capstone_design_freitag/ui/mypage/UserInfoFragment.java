package com.ajou.capstone_design_freitag.ui.mypage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.home.User;

import org.json.JSONException;
import org.json.JSONObject;


public class UserInfoFragment extends Fragment  {

    Button changeUserInfo;
    Button point;
    private User user = new User();
    View view;

    TextView userName;
    TextView userEmail;
    TextView userPhoneNumber;
    TextView userAffiliation;
    TextView userLevel;
    TextView userPoint;

    String user_id;
    JSONObject jsonObject = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESTAPI instance = RESTAPI.getInstance();
        user_id = instance.getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_info, container, false);

        userName = (TextView) view.findViewById(R.id.user_name_mypage);
        userEmail = (TextView) view.findViewById(R.id.user_email_mypage);
        userPhoneNumber = (TextView) view.findViewById(R.id.user_phone_mypage);
        userAffiliation = (TextView) view.findViewById(R.id.user_affiliation_mypage);
        userLevel = (TextView) view.findViewById(R.id.user_level_mypage);
        userPoint = (TextView) view.findViewById(R.id.user_point_mypage);
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

        RESTAPI instance = RESTAPI.getInstance();
        //토큰 받아오는데 null아니면 (로그인 한 상태면 진행)
        if(instance.getToken()!=null){
            getMyPage(view);
        }

        return view;
    }

    public void getMyPage(final View view){
        AsyncTask<String, Void,String> mypageTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... UserInfos) {
                String result = RESTAPI.getInstance().mypage(UserInfos[0]);
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                try {
                    jsonObject = new JSONObject(result);
                    user.setName(jsonObject.getString("userName"));
                    user.setEmail(jsonObject.getString("userEmail"));
                    user.setAccount(jsonObject.getString("userAccount"));
                    user.setBank(jsonObject.getInt("userBank"));
                    user.setPhonenumber(jsonObject.getString("userPhone"));
                    user.setAffiliation(jsonObject.getString("userAffiliation"));
                    user.setUserID(jsonObject.getString("username")); //이거 고침
                    //level임의로
                    user.setLevel("starter");

                    userName.setText(user.getName());
                    userEmail.setText(user.getEmail());
                    userPhoneNumber.setText(user.getPhonenumber());
                    userAffiliation.setText(user.getAffiliation());
                    userLevel.setText(user.getLevel());
                    userPoint.setText(Integer.toString(user.getBank()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        mypageTask.execute(user_id);
    }


    public void changeUserInfo(View view){
        //rest api
    }

    public void exchangePoint(View view){
        //rest api

    }
}
