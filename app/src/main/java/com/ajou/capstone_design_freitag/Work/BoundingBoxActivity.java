package com.ajou.capstone_design_freitag.Work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.ClassDto;
import com.ajou.capstone_design_freitag.UI.dto.Problem;
import com.ajou.capstone_design_freitag.UI.dto.ProblemWithClass;
import com.ajou.capstone_design_freitag.UI.dto.Project;
import com.theartofdev.edmodo.cropper.CropImage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BoundingBoxActivity extends AppCompatActivity {

    static Project project;

    static List<ProblemWithClass> problemWithClassList = new ArrayList<>();

    private CustomViewPager viewPager;
    private BoundingBoxPagerAdapter pagerAdapter;
    private static String preLabel;
    private static String labelName;
    private static String problemId;
    private static List<StringBuffer> coordinate = new ArrayList<>();
    private static List<String> classList = new ArrayList<>();

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
                    pagerAdapter = new BoundingBoxPagerAdapter(activity, problemWithClassList, project, new BoundingBoxPagerAdapter.OnRadioCheckedChanged() {
                        @Override
                        public void onRadioCheckedChanged(String label,int problem) {
                            labelName = label;
                            problemId = Integer.toString(problem);
                        }
                    }, new BoundingBoxPagerAdapter.RegisterListener() {
                        @Override
                        public void clickBtn() {
                            BoundingBoxTask boundingBoxTask = new BoundingBoxTask(getActivity());
                            boundingBoxTask.execute();
                        }
                    });
                    viewPager.setAdapter(pagerAdapter);
                }
            });
        }

        private static class BoundingBoxTask extends AsyncTask<Void,Void,Boolean>{
            private WeakReference<BoundingBoxActivity> activityReference;

            @Override
            protected Boolean doInBackground(Void... voids) {
                Boolean result = null;

                try {
                    result = RESTAPI.getInstance().boundingBoxWork(coordinate,problemId,classList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            }
            public BoundingBoxTask(BoundingBoxActivity context) {
                activityReference = new WeakReference<>(context);
            }

            BoundingBoxActivity getActivity() {
                BoundingBoxActivity activity = activityReference.get();
                if (activity == null || activity.isFinishing()) {
                    return null;
                }
                return activity;
            }

            @Override
            protected void onPostExecute(Boolean result){
                final BoundingBoxActivity activity = getActivity();
                if(result !=true){
                   activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(activity, "바운딩박스 작업 실패", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else{
                    activity.runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(activity, "바운딩박스 작업 성공", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                classList.clear();
                coordinate.clear();
            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of CropImageActivity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Toast.makeText(
                        this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG)
                        .show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CropImage.BOUNDING_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                float[] result = data.getFloatArrayExtra("rect");
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(result[0]);
                stringBuffer.append(" ");
                stringBuffer.append(result[1]);
                stringBuffer.append(" ");
                stringBuffer.append(result[4]);
                stringBuffer.append(" ");
                stringBuffer.append(result[5]);

                    classList.add(labelName);
                    coordinate.add(stringBuffer);

            }
        }
    }
}
