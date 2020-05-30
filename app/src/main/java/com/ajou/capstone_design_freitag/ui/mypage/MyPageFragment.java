package com.ajou.capstone_design_freitag.ui.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment extends Fragment {
    private static final int LOGIN_REQUEST_CODE = 102;

    UserInfoFragment userInfoFragment = new UserInfoFragment();
    MyPageUpdateFragment myPageUpdateFragment = new MyPageUpdateFragment();
    CheckPasswordFragment passwordFragment = new CheckPasswordFragment();
    CompleteWorkListFragment completeWorkListFragment = new CompleteWorkListFragment();
    RequestProjectListFragment requestProjectListFragment = new RequestProjectListFragment();
    MyPageMenuFragment myPageMenuFragment = new MyPageMenuFragment();

    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESTAPI instance = RESTAPI.getInstance();
        //토큰 받아오는데 null이면 로그인
        if(instance.getToken()==null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        replaceFragment(0);
        return view;
    }

    public void replaceFragment(int index) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();

        if(index==0){
            if(!userInfoFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_my_page, myPageMenuFragment);
                fragmentTransaction.commit();
            }
        }
        else if(index == 1){
            if(!passwordFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_my_page,passwordFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index == 2) {
            if(!myPageUpdateFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_my_page,myPageUpdateFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index ==3){
            if(!userInfoFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_my_page,userInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index ==4){
            if(!completeWorkListFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_my_page,completeWorkListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        else if (index==5){
            if(!requestProjectListFragment.isAdded()){
                fragmentTransaction.replace(R.id.fragment_my_page,requestProjectListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getContext(), "로그인이 필요합니다.",Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).goToHome();
            } else {
                ((MainActivity)getActivity()).loginSuccess();
            }
        }
    }

}