package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    View layout_login;
    View layout_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout_login = findViewById(R.id.layout_login);
        layout_register = findViewById(R.id.layout_register);
    }

    public void register(View view) {
        //RESTful API 때려
    }

    public void login(View view) {
        //RESTful API 때려
    }

    public void goToLogin(View view) {
        layout_login.setVisibility(View.VISIBLE);
        layout_register.setVisibility(View.GONE);
    }

    public void goToRegister(View view) {
        layout_login.setVisibility(View.GONE);
        layout_register.setVisibility(View.VISIBLE);
    }
}
