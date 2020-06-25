package com.ajou.capstone_design_freitag.Work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.BoundingBoxDto;
import com.ajou.capstone_design_freitag.UI.dto.ClassDto;
import com.ajou.capstone_design_freitag.UI.dto.Problem;
import com.ajou.capstone_design_freitag.UI.dto.ProblemWithClass;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ClassificationActivity extends AppCompatActivity {
    List<ProblemWithClass> problemWithClassList = new ArrayList<>();

    private CustomViewPager viewPager ;
    private ClassificationPagerAdapter pagerAdapter ;

    static File file;
    OutputStream outputStream;
    List<String> fileExtensionList = new ArrayList<>();
    List<InputStream> inputStreamList = new ArrayList<>();
    List<Uri> uriList = new ArrayList<>();

    AppCompatDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager) ;
        getProblem();
        progressON(this,"Loading..");
    }

    public void getProblem(){
        GetProblemTask getProblemTask = new GetProblemTask(this);
        getProblemTask.execute();
    }

    private class GetProblemTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<ClassificationActivity> activityReference;
        ViewPager viewPager ;
        ClassificationPagerAdapter pagerAdapter ;
        List<ProblemWithClass> problemWithClassList;

        GetProblemTask(ClassificationActivity context) {
            activityReference = new WeakReference<>(context);
            viewPager = context.viewPager;
            pagerAdapter = context.pagerAdapter;
            problemWithClassList = context.problemWithClassList;
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if(problemJsonParse(getClassificationProblems())) {
                    if (problemWithClassList.size() == 5) {
                        System.out.println("json parse pass");
                        for (int i = 0; i < problemWithClassList.size(); i++) {
                            if (getResult(problemWithClassList.get(i).getProblem().getBucketName(), problemWithClassList.get(i).getProblem().getObjectName(),i+1)) {
                                InputStream inputStream = new FileInputStream(file);
                                inputStreamList.add(inputStream);
                            }
                        }
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            final ClassificationActivity activity = getActivity();
            if (activity == null) {
                return;
            }
            progressOFF();
            pagerAdapter = new ClassificationPagerAdapter(activity, problemWithClassList,inputStreamList,fileExtensionList,uriList) ;
            viewPager.setAdapter(pagerAdapter) ;
        }

        private Boolean getResult(String bucketName,String objectName,int position) {
            String file_extension = FilenameUtils.getExtension(objectName);
            fileExtensionList.add(file_extension);
            file = new File("/data/data/com.ajou.capstone_design_freitag/files/project_classification"+position+"." + file_extension);
            uriList.add(Uri.fromFile(file));
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return RESTAPI.getInstance().downloadObject(bucketName,objectName,outputStream);
        }

        private String getClassificationProblems() throws Exception {
            String result = RESTAPI.getInstance().getClassificationProblems("classification");
            System.out.println("분류 문제: "+result);
            return result;
        }
        public boolean problemJsonParse(String list) throws JSONException {
            JSONArray jsonArray = new JSONArray(list);
            for(int i=0;i<jsonArray.length();i++){
                ProblemWithClass problemWithClass = new ProblemWithClass();
                JSONObject jsonObject;
                JSONObject problemJsonObject;
                JSONArray classNameList;
                JSONArray boundingList;

                jsonObject = jsonArray.getJSONObject(i);
                problemJsonObject = jsonObject.getJSONObject("problemDto");
                classNameList = jsonObject.getJSONArray("classNameList");

                List<BoundingBoxDto> boundingBoxDtoList = new ArrayList<>();
                if(!jsonObject.isNull("boundingBoxList")) {
                    boundingList = jsonObject.getJSONArray("boundingBoxList");
                    for (int j = 0; j < boundingList.length(); j++) {
                        JSONObject bounding;
                        bounding = boundingList.getJSONObject(j);
                        BoundingBoxDto boundingBoxDto = new BoundingBoxDto();
                        boundingBoxDto.setBoxId(Integer.parseInt(bounding.getString("boxId")));
                        boundingBoxDto.setProblemId(Integer.parseInt(bounding.getString("problemId")));
                        boundingBoxDto.setClassName(bounding.getString("className"));
                        boundingBoxDto.setCoordinates(bounding.getString("coordinates"));
                        boundingBoxDtoList.add(boundingBoxDto);
                    }
                    problemWithClass.setBoundingBoxList(boundingBoxDtoList);
                }
                problemWithClass.setConditionContent(jsonObject.getString("conditionContent"));

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
            return true;
        }

        private ClassificationActivity getActivity() {
            ClassificationActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return activity;
        }
    }

    public void mOnClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.btn_previous_classification://이전버튼 클릭
                position = viewPager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                //viewPager.setCurrentItem(position - 1, true);
                break;
            case R.id.btn_next_classification://다음버튼 클릭
                position = viewPager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                viewPager.setCurrentItem(position + 1, true);
                break;
        }
    }

    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.layout_dialog);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }


    }

    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}