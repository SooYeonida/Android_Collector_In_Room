package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {
    private ViewFlipper login_view_flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_view_flipper = findViewById(R.id.login_view_flipper);
    }

    public void register(View view) {
        //RESTful API 때려
    }

    public void login(View view) {
        //RESTful API 때려
    }

    public void goToLogin(View view) {
        login_view_flipper.setDisplayedChild(2);
    }

    public void goToRegister(View view) {
        login_view_flipper.setDisplayedChild(1);
    }
}
