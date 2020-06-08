package com.ajou.capstone_design_freitag.ui.mypage;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.dto.User;

public class MyPageUpdateFragment extends Fragment{

    Button update;
    EditText user_name;
    EditText user_phone;
    EditText user_email;
    EditText user_affiliation;

    User user;

    public static MyPageUpdateFragment newInstance(){
        return new MyPageUpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User userinstance = User.getUserinstance();
        user = userinstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page_update, container, false);

        update = (Button) view.findViewById(R.id.update_button);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(v);
            }
        });

        user_name = view.findViewById(R.id.update_user_name);
        user_phone = view.findViewById(R.id.update_user_phone);
        user_email = view.findViewById(R.id.update_user_email);
        user_affiliation = view.findViewById(R.id.update_user_affiliation);

        return view;
    }

    public void update(final View view){
        String userName = user_name.getText().toString();
        String userPhone = user_phone.getText().toString();
        String userEmail = user_email.getText().toString();
        String userAffiliation = user_affiliation.getText().toString();

        if(userName.getBytes().length==0){
            userName = user.getName();
        }
        if(userPhone.getBytes().length==0){
            userPhone = user.getPhonenumber();
        }
        if(userEmail.getBytes().length==0){
            userEmail = user.getEmail();
        }
        if(userAffiliation.getBytes().length==0){
            userAffiliation = user.getAffiliation();
        }

        AsyncTask<String, Void, Boolean> updateTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... userInfos) {
                boolean result = RESTAPI.getInstance().update(userInfos[0],
                        userInfos[1],
                        userInfos[2],
                        userInfos[3]);
                return new Boolean(result);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Toast.makeText(getContext(),"수정 완료",Toast.LENGTH_LONG).show();
                    ((MainActivity)getActivity()).goToHome();
                } else {
                    Toast.makeText(getContext(),"수정 실패",Toast.LENGTH_LONG).show();
                    ((MainActivity)getActivity()).goToHome();
                }
            }
        };
        updateTask.execute(userName,userPhone,userEmail,userAffiliation);
    }

}
