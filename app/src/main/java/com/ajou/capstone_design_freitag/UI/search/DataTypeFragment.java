package com.ajou.capstone_design_freitag.UI.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.Work.ClassificationActivity;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;

import static android.app.Activity.RESULT_OK;

public class DataTypeFragment extends Fragment {
    private static final int LOGIN_REQUEST_CODE = 102;

    Button classfication_btn;
    Button boundingbox_btn;

    SearchFragment searchFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_data_type, container, false);
       classfication_btn = (Button)view.findViewById(R.id.classification_btn);
       boundingbox_btn = (Button) view.findViewById(R.id.boundingbox_btn);

       classfication_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(RESTAPI.getInstance().getToken()==null){
                   Intent intent = new Intent(getActivity(), LoginActivity.class);
                   startActivityForResult(intent, LOGIN_REQUEST_CODE);
               }
               else {
                   Intent intent = new Intent(getActivity(), ClassificationActivity.class);
                   startActivity(intent);
               }
           }
       });

       boundingbox_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               searchFragment = (SearchFragment)getParentFragment();
               searchFragment.replaceFragment(2);
           }
       });

       return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getActivity(), "로그인이 필요합니다.", Toast.LENGTH_LONG).show();
                MainActivity.goToHome();
            } else {
                MainActivity.loginSuccess();
            }
        }
    }
}
