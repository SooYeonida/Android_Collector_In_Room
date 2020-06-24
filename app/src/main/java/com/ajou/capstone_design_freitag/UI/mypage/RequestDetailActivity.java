package com.ajou.capstone_design_freitag.UI.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class RequestDetailActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    Project project;
    PieChart pieChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_detail);
        Intent intent = getIntent();
        project = intent.getParcelableExtra("project");

        TextView projectName1;
        projectName1 = findViewById(R.id.project_name_request_detail1);
        TextView projectName2;
        projectName2 = findViewById(R.id.project_name_request_detail2);
        ImageView dataTypeImage;
        dataTypeImage = findViewById(R.id.project_icon_request);
        TextView dataType;
        dataType = findViewById(R.id.work_data_type_request);
        TextView subject;
        subject = findViewById(R.id.work_subject_request);
        TextView requester;
        requester = findViewById(R.id.work_requester_request);
        TextView classList;
        classList = findViewById(R.id.classlist_project_detail_request);
        TextView wayContent;
        wayContent = findViewById(R.id.work_waycontent_request);
        TextView conditionContent;
        conditionContent = findViewById(R.id.work_conditioncontent_request);

        projectName1.setText(project.getProjectName());
        switch (project.getDataType()){
            case "image":
                dataType.setText("이미지");
                break;
            case "text":
                dataType.setText("텍스트");
                break;
            case "audio":
                dataType.setText("음성");
                break;
            case "boundingBox":
                dataType.setText("바운딩박스");
                break;
            case "classification":
                dataType.setText("분류");
                break;
        }
        if(project.getWorkType().equals("collection")) {
            switch (project.getDataType()) {
                case ("image"):
                    dataTypeImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_image_black_24dp));
                    break;
                case ("text"):
                    dataTypeImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_text_black_24dp));
                    break;
                case ("audio"):
                    dataTypeImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_voice_black_24dp));
                    break;
            }
        }
        else{
            dataTypeImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_label_black_24dp));
        }

        subject.setText(project.getSubject());
        requester.setText(project.getUserId());
        classList.setText(project.getClass_list().toString());
        wayContent.setText(project.getWayContent());
        conditionContent.setText(project.getConditionContent());

        pieChart = (PieChart)findViewById(R.id.piechart);
        projectName2.setText(project.getProjectName());

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        if(project.getProgressData()!=0){
            yValues.add(new PieEntry(project.getProgressData()-project.getValidationData(),"진행 중인 데이터"));
        }
        yValues.add(new PieEntry(project.getValidationData(),"검증 완료된 데이터"));
        yValues.add(new PieEntry(project.getTotalData()-project.getProgressData(),"진행 전 데이터"));


        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.LTGRAY);

        pieChart.setData(data);
        pieChart.setOnChartValueSelectedListener(this);

        Button end;
        end = findViewById(R.id.work_end);
        if(project.getStatus().equals("진행중") || project.getStatus().equals("검증대기") || project.getStatus().equals("검증완료")) {
            end.setVisibility(View.VISIBLE);
        } else {
            end.setVisibility(View.GONE);
        }
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TerminateProjectActivity.class);
                intent.putExtra("project",project);
                startActivity(intent);
            }
        });

        Button validatedDataDetail;
        validatedDataDetail = findViewById(R.id.validatedDataDetail);
        validatedDataDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheckValidatedDataActivity.class);
                intent.putExtra("project",project);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
        switch ((int) h.getX()){
            case 1:
                System.out.println("검증완료");
                Intent intent = new Intent(getApplicationContext(), CheckValidatedDataActivity .class);
                intent.putExtra("project",project);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onNothingSelected() {

    }

}
