package com.ajou.capstone_design_freitag.UI.mypage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.User;

public class CheckPasswordFragment extends Fragment {

    MyPageFragment myPageFragment;

    Button password;
    EditText user_input;
    String user_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User userinstance = User.getUserinstance();
        user_password = userinstance.getUserPwd();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_check_password, container, false);

       user_input = view.findViewById(R.id.password_before_update);

       password = (Button) view.findViewById(R.id.password_before_update_btn);
       password.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               checkPassword(v);
           }
       });

       return view;
    }

    public void checkPassword(View view){
        String user_input_pwd = RESTAPI.SHA256(user_input.getText().toString());
        myPageFragment = (MyPageFragment) getParentFragment();

        if(!user_password.equals(user_input_pwd)){
            Toast.makeText(getContext(),"비밀번호가 틀렸습니다",Toast.LENGTH_LONG).show();
            myPageFragment.replaceFragment(0);
        }
        else{
            myPageFragment.replaceFragment(2);
        }
    }
}
