package com.ajou.capstone_design_freitag.Work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.ClassDto;
import com.ajou.capstone_design_freitag.UI.dto.Problem;
import com.ajou.capstone_design_freitag.UI.dto.ProblemWithClass;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BoundingBoxActivity extends AppCompatActivity {

    static Project project;

    List<ProblemWithClass> problemWithClassList = new ArrayList<>();

    private CustomViewPager viewPager;
    private BoundingBoxPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounding_box);
        Intent intent = getIntent();
        project = intent.getParcelableExtra("project");
        Project.getProjectinstance().setBucketName(project.getBucketName());
        Project.getProjectinstance().setProjectId(project.getProjectId());
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        getProblem();
    }

    public void getProblem() {
        BoundingBoxActivity.GetProblemTask getProblemTask = new BoundingBoxActivity.GetProblemTask(this);
        getProblemTask.execute();
    }

    static class GetProblemTask extends AsyncTask<Void, Void, String> {
        private WeakReference<BoundingBoxActivity> activityReference;

        ViewPager viewPager;
        BoundingBoxPagerAdapter pagerAdapter;
        List<ProblemWithClass> problemWithClassList;

        GetProblemTask(BoundingBoxActivity context) {
            activityReference = new WeakReference<>(context);
            viewPager = context.viewPager;
            pagerAdapter = context.pagerAdapter;
            problemWithClassList = context.problemWithClassList;
        }

        @Override
        protected String doInBackground(Void... info) {
            String result;
            try {
                result = RESTAPI.getInstance().getBoundingBoxProblems();
                System.out.println("바운딩박스 문제: " + result);
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

            final BoundingBoxActivity activity = getActivity();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pagerAdapter = new BoundingBoxPagerAdapter(activity, problemWithClassList,project);
                    viewPager.setAdapter(pagerAdapter);
                }
            });
        }

        public void jsonParse(String list) throws JSONException {
            JSONArray jsonArray = new JSONArray(list);
            for (int i = 0; i < jsonArray.length(); i++) {
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
                problem.setObjectName(problemJsonObject.getString("objectName"));
                problemWithClass.setProblem(problem);

                for (int j = 0; j < classNameList.length(); j++) {
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

        private BoundingBoxActivity getActivity() {
            BoundingBoxActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return activity;
        }
    }

    public void mOnClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.btn_previous://이전버튼 클릭
                position = viewPager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                viewPager.setCurrentItem(position - 1, true);
                break;
            case R.id.btn_next://다음버튼 클릭
                position = viewPager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                viewPager.setCurrentItem(position + 1, true);
                break;
        }
    }

}
