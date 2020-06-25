package com.ajou.capstone_design_freitag.UI.mypage;
import android.content.Intent;
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
import com.ajou.capstone_design_freitag.UI.dto.User;

import org.json.JSONException;

import static android.app.Activity.RESULT_OK;

public class UserInfoFragment extends Fragment {
    private static final int POINT_EXCHANGE_REQUEST_CODE = 100;

    Button update;
    Button exchange;
    MyPageFragment myPageFragment;

    TextView userName;
    TextView userEmail;
    TextView userPhoneNumber;
    TextView userAffiliation;
    TextView userAccuracy;
    TextView userPoint;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        userName = (TextView) view.findViewById(R.id.user_name_mypage);
        userEmail = (TextView) view.findViewById(R.id.user_email_mypage);
        userPhoneNumber = (TextView) view.findViewById(R.id.user_phone_mypage);
        userAffiliation = (TextView) view.findViewById(R.id.user_affiliation_mypage);
        userAccuracy = (TextView) view.findViewById(R.id.user_accuracy_mypage);
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
                userAccuracy.setText(String.format("%.2f %%", user.getAccuracy()*100));
                userPoint.setText(String.valueOf(user.getPoint())); //포인트 안받아와서 일단은 이렇게 해놓음
            }

        };
        mypageTask.execute(User.getUserinstance().getUserID());
    }


    public void updateUserInfo(View view){
        myPageFragment = (MyPageFragment) getParentFragment();
        myPageFragment.replaceFragment(1);
    }

    public void exchangePoint(View view){
        Intent intent = new Intent(this.getContext(), PointExchaneActivity.class);
        intent.putExtra("point", Integer.parseInt(userPoint.getText().toString()));
        startActivityForResult(intent, POINT_EXCHANGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == POINT_EXCHANGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getMyPage(null);
            }
        }
    }

}
