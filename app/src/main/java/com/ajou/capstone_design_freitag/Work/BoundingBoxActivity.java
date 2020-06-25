package com.ajou.capstone_design_freitag.Work;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.ajou.capstone_design_freitag.UI.dto.Project;
import com.theartofdev.edmodo.cropper.CropImage;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundingBoxActivity extends AppCompatActivity {
    static Project project;

    static List<ProblemWithClass> problemWithClassList = new ArrayList<>();

    private CustomViewPager viewPager;
    private BoundingBoxPagerAdapter pagerAdapter;

    private String label = null;
    private int problemId;
    private List<BoundingBoxDto> finalAnswer = new ArrayList<>();
    private String coordinate = null;

    String file_extension;
    static File file;
    OutputStream outputStream;

    List<Bitmap> bitmapList = new ArrayList<>();
    List<Problem> problemList = new ArrayList<>();
    Map<Integer,Uri> positionUri = new HashMap<>();

    AppCompatDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounding_box);
        Intent intent = getIntent();
        project = intent.getParcelableExtra("project");
        Problem problem = new Problem();
        problem.setBucketName(project.getBucketName());
        problem.setObjectName(project.getExampleContent());
        problemList.add(problem);
        Project.getProjectinstance().setBucketName(project.getBucketName());
        Project.getProjectinstance().setProjectId(project.getProjectId());
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        getProblem();
        progressON(this,"Loading..");
    }

    public void getProblem() {
        BoundingBoxActivity.GetProblemTask getProblemTask = new BoundingBoxActivity.GetProblemTask(this);
        getProblemTask.execute();

    }

    private class GetProblemTask extends AsyncTask<Void, Void, Boolean> {
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
        protected Boolean doInBackground(Void... params) {
            try {
                if(problemJsonParse(getBoundingBoxProblems())) {
                    if (problemWithClassList.size() == 5) {
                        System.out.println("json parse pass");
                        if (getResult(project.getBucketName(), project.getExampleContent(),0)) {
                            InputStream inputStream = new FileInputStream(file);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            bitmapList.add(bitmap);
                            System.out.println("example pass");
                            for (int i = 0; i < problemWithClassList.size(); i++) {
                                if (getResult(problemWithClassList.get(i).getProblem().getBucketName(), problemWithClassList.get(i).getProblem().getObjectName(),i+1)) {
                                    InputStream inputStream2 = new FileInputStream(file);
                                    positionUri.put(i+1, Uri.fromFile(file));
                                    Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream2);
                                    bitmapList.add(bitmap2);
                                }
                            }
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            final BoundingBoxActivity activity = getActivity();
            if (activity == null) {
                return;
            }
                    progressOFF();
                    pagerAdapter = new BoundingBoxPagerAdapter(activity, problemWithClassList,bitmapList, project,positionUri, new BoundingBoxPagerAdapter.OnRadioCheckedChanged() {
                        @Override
                        public void onRadioCheckedChanged(String labelName,int problem) {
                            label = labelName;
                            System.out.println("label:"+label);
                            problemId = problem;
                        }
                    }, new BoundingBoxPagerAdapter.RegisterListener() {
                        @Override
                        public void clickBtn() {
                            //완료버튼 눌렀을 경우
                            BoundingBoxTask boundingBoxTask = new BoundingBoxTask();
                            boundingBoxTask.execute();
                        }
                    });
                    viewPager.setAdapter(pagerAdapter);
        }

        private Boolean getResult(String bucketName,String objectName,int position) {
            file_extension = FilenameUtils.getExtension(objectName);
            file = new File("/data/data/com.ajou.capstone_design_freitag/files/project_classification"+position+"." + file_extension);
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return RESTAPI.getInstance().downloadObject(bucketName,objectName,outputStream);
        }


        private String getBoundingBoxProblems() throws Exception {
            String result = RESTAPI.getInstance().getBoundingBoxProblems();
            System.out.println("바운딩 문제: "+result);
            return result;
        }

        public boolean problemJsonParse(String list) throws JSONException {
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
            return true;
        }

        private BoundingBoxActivity getActivity() {
            BoundingBoxActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return activity;
        }

    }

    private class BoundingBoxTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean result = null;
            try {
                for(int i=0;i<finalAnswer.size();i++) {
                    result = RESTAPI.getInstance().boundingBoxWork(finalAnswer.get(i).getCoordinates(),finalAnswer.get(i).getProblemId(),finalAnswer.get(i).getClassName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result !=true){
                System.out.println("바운딩 작업 실패");
            }
            else{
                System.out.println("바운딩 작업 성공");
                finish();
            }
        }
    }


    public void mOnClick(View v) {
        int position;
        switch (v.getId()) {
            case R.id.btn_previous://이전버튼 클릭
                position = viewPager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                //viewPager.setCurrentItem(position - 1, true);
                break;
            case R.id.btn_next://다음버튼 클릭
                position = viewPager.getCurrentItem();//현재 보여지는 아이템의 위치를 리턴
                viewPager.setCurrentItem(position + 1, true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.BOUNDING_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                float[] result = data.getFloatArrayExtra("rect");
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(result[0]/800);
                stringBuffer.append(" ");
                stringBuffer.append(result[1]/600);
                stringBuffer.append(" ");
                stringBuffer.append(result[4]/800);
                stringBuffer.append(" ");
                stringBuffer.append(result[5]/600);
                BoundingBoxDto boundingBoxDto = new BoundingBoxDto();
                boundingBoxDto.setClassName(label);
                boundingBoxDto.setProblemId(problemId);
                boundingBoxDto.setCoordinates(stringBuffer.toString());
                finalAnswer.add(boundingBoxDto);
            }
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
