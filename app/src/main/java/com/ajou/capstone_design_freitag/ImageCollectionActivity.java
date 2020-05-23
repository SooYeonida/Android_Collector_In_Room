package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.plus.Project;

public class ImageCollectionActivity extends AppCompatActivity {

    TextView projectName;
    TextView wayContent;
    TextView conditionContent;
    ImageView exampleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_collection);
        Intent intent = getIntent();
        Project project = intent.getParcelableExtra("project"); //리스트에서 사용자가 선택한 프로젝트 정보 받아옴

        projectName = findViewById(R.id.image_collection_project_name);
        wayContent = findViewById(R.id.image_collection_way_content);
        conditionContent = findViewById(R.id.image_collection_condition_content);
        projectName.setText(project.getProjectName());
        wayContent.setText(project.getWayContent());
        conditionContent.setText(project.getConditionContent());

    }
}
