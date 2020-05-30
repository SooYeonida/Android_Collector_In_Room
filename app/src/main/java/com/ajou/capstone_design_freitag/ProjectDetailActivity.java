package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.ui.plus.Project;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProjectDetailActivity extends AppCompatActivity {

    private static final int LOGIN_REQUEST_CODE = 102;
    Project project;

    ImageView projectIcon;
    TextView projectName;
    TextView dataType;
    TextView subject;
    TextView requester;
    LinearLayout className;
    TextView wayContent;
    TextView conditionContent;
    TextView classlist;
    CheckBox agree_check;
    TextView date;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RESTAPI instance = RESTAPI.getInstance();
        //토큰 받아오는데 null이면 로그인
        if(instance.getToken()==null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }

        setContentView(R.layout.activity_project_detail);

        Intent intent = getIntent();
        project = intent.getParcelableExtra("project"); //리스트에서 사용자가 선택한 프로젝트 정보 받아옴
        projectIcon = (ImageView)findViewById(R.id.project_icon_detail);
        projectName = (TextView)findViewById(R.id.work_name);
        dataType = (TextView)findViewById(R.id.work_data_type);
        subject = (TextView)findViewById(R.id.work_subject);
        requester = (TextView)findViewById(R.id.work_requester);
        className = (LinearLayout) findViewById(R.id.work_class_name);
        wayContent = (TextView)findViewById(R.id.work_waycontent);
        conditionContent = (TextView)findViewById(R.id.work_conditioncontent);
        classlist = (TextView)findViewById(R.id.classlist_project_detail);
        agree_check = findViewById(R.id.work_agree_check); //동의 안하면 작업 안넘어가게
        start = findViewById(R.id.work_start);

        if(project.getWorkType().equals("labelling")){
            className.setVisibility(View.GONE);
        }

        if(project.getWorkType().equals("collection")) {
            switch (project.getDataType()) {
                case ("image"):
                    projectIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_image_black_24dp));
                    break;
                case ("text"):
                    projectIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_text_black_24dp));
                    break;
                case ("audio"):
                    projectIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_voice_black_24dp));
                    break;
            }
        }
        else{
            projectIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_label_black_24dp));
        }

        projectName.setText(project.getProjectName());
        dataType.setText(project.getDataType());
        subject.setText(project.getSubject());
        requester.setText(project.getUserId());
        wayContent.setText(project.getWayContent());
        conditionContent.setText(project.getConditionContent());
        classlist.setText(project.getClass_list().toString());

        date = (TextView)findViewById(R.id.current_date);
        Date currenTime = Calendar.getInstance().getTime();
        date.setText(new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault()).format(currenTime));


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!agree_check.isChecked()){
                    Toast.makeText(getApplicationContext(), "정보 제공 동의를 해야 작업할 수 있습니다.",Toast.LENGTH_LONG).show();
                }else{
                    switch(project.getDataType()){
                        case("image"):
                            goToImageCollectionWork(v);
                            break;
                        case("text"):
                            goToTextCollectionWork(v);
                            break;
                        case ("audio"):
                            goToAudioCollectionWork(v);
                            break;
                    }

                }
            }
        });
    }

        private void goToImageCollectionWork(View v) {
        Intent intent = new Intent(getApplicationContext(),ImageCollectionActivity.class);
        intent.putExtra("project",project);
        startActivity(intent);
    }

    private void goToTextCollectionWork(View v) {
        Intent intent = new Intent(getApplicationContext(), TextCollectionActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }
    private void goToAudioCollectionWork(View v) {
        Intent intent = new Intent(getApplicationContext(), AudioCollectionActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getApplicationContext(), "로그인이 필요합니다.",Toast.LENGTH_LONG).show();
                ((MainActivity)getApplicationContext()).goToHome();
            }
        }
    }

}
