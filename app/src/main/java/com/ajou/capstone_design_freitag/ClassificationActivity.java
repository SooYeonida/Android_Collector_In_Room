package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.ui.dto.ClassDto;
import com.ajou.capstone_design_freitag.ui.dto.Problem;
import com.ajou.capstone_design_freitag.ui.dto.ProblemWithClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassificationActivity extends AppCompatActivity {
    List<ProblemWithClass> problemWithClassList = new ArrayList<>();

    private ViewPager viewPager ;
    private PagerAdapter pagerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        getProblem();
        viewPager = (ViewPager) findViewById(R.id.viewPager) ;
    }

    public void getProblem(){
        AsyncTask<Void, Void, String> getProblemTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... info) {
                String result;
                try {
                    result = RESTAPI.getInstance().provideClassificationProblems("classification");
                    System.out.println("분류 문제: "+result);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    jsonParse(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pagerAdapter = new PagerAdapter(getApplicationContext(),problemWithClassList) ;
                        viewPager.setAdapter(pagerAdapter) ;
                    }
                });
            }
        };
        getProblemTask.execute();
    }

    public void jsonParse(String list) throws JSONException {
        JSONArray jsonArray = new JSONArray(list);
        for(int i=0;i<jsonArray.length();i++){
            ProblemWithClass problemWithClass = new ProblemWithClass();
            JSONObject jsonObject;
            JSONObject problemJsonObject;
            JSONArray classNameList;

            jsonObject = jsonArray.getJSONObject(i);
            problemJsonObject = jsonObject.getJSONObject("problemDto");
            classNameList = jsonObject.getJSONArray("classNameList");

            Problem problem = new Problem();
            List<ClassDto> classDtoList = new ArrayList<>();

            problem.setProblemId(problemJsonObject.getInt("problemId"));
            problem.setProjectId(problemJsonObject.getInt("projectId"));
            problem.setBucketName(problemJsonObject.getString("bucketName"));
            problem.setFinalAnswer(problemJsonObject.getString("finalAnswer"));
            problem.setObjectName(problemJsonObject.getString("objectName"));
            problem.setReferenceId(problemJsonObject.getInt("referenceId"));
            problem.setUserId(problemJsonObject.getString("userId"));
            problem.setValidationStatus(problemJsonObject.getString("validationStatus"));
            problemWithClass.setProblem(problem);

            for(int j=0;j<classNameList.length();j++){
                JSONObject className;
                className = classNameList.getJSONObject(j);
                ClassDto classDto = new ClassDto();
                classDto.setProjectId(Integer.parseInt(className.getString("projectId")));
                classDto.setClassName(className.getString("className"));
                classDtoList.add(classDto);
            }
            problemWithClass.setClassNameList(classDtoList);

            problemWithClassList.add(problemWithClass);
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        Log.d("onPostCreate", "onDestroy");
    }

}