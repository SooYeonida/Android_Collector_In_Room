package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ajou.capstone_design_freitag.Work.AudioCollectionActivity;
import com.ajou.capstone_design_freitag.Work.ImageCollectionActivity;
import com.ajou.capstone_design_freitag.Work.TextCollectionActivity;
import com.ajou.capstone_design_freitag.UI.dto.Project;

public class PopupActivity extends AppCompatActivity {

    String type;
    Project project;
    Button yes;
    Button no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_popup);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        project = intent.getParcelableExtra("project"); //리스트에서 사용자가 선택한 프로젝트 정보 받아옴
        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
    }

    public void yes(View view){
        if(type.equals("image")) {
            Intent intent = new Intent(getApplicationContext(), ImageCollectionActivity.class);
            intent.putExtra("project", project);
            startActivity(intent);
        }
        else if(type.equals("audio")){
            Intent intent = new Intent(getApplicationContext(), AudioCollectionActivity.class);
            intent.putExtra("project", project);
            startActivity(intent);
        }
        else if(type.equals("text")){
            Intent intent = new Intent(getApplicationContext(), TextCollectionActivity.class);
            intent.putExtra("project", project);
            startActivity(intent);
        }
    }
    public void no(View view){
        finish();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
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
