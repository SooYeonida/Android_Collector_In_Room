package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.Work.AudioCollectionActivity;
import com.ajou.capstone_design_freitag.Work.ImageCollectionActivity;
import com.ajou.capstone_design_freitag.Work.TextCollectionActivity;
import com.ajou.capstone_design_freitag.UI.dto.Project;

public class PopupActivity extends AppCompatActivity {

    TextView popupText;

    String msg;
    Button yes;
    Button no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup);
        Intent intent = getIntent();
        msg = intent.getStringExtra("msg");
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);

        popupText = findViewById(R.id.popup_text);
        popupText.setText(msg);
    }

    public void yes(View view){
        setResult(RESULT_OK);
        finish();
    }

    public void no(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        return;
    }
}
