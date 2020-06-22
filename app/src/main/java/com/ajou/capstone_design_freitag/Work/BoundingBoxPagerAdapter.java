package com.ajou.capstone_design_freitag.Work;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.ProblemWithClass;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class BoundingBoxPagerAdapter  extends androidx.viewpager.widget.PagerAdapter  {

    private BoundingBoxActivity boundingBoxActivity;
    private static Context mContext;
    private List<ProblemWithClass> problemList;
    String file_extension;
    static File file;
    OutputStream outputStream;
    static List<String> problemId = new ArrayList<>();
    static List<StringBuffer> classAnswers = new ArrayList<>();
    int currenPage;

    static TextView projectName;
    static TextView wayContent;
    static TextView conditionContent;
    static TextView requester;
    static TextView classListView;
    static ImageView exampleContent;
    static CustomView customView;

    Project project;

    String label=null;
    static Bitmap bitmap=null;

    public BoundingBoxPagerAdapter(BoundingBoxActivity boundingBoxActivity, List<ProblemWithClass> problemWithClassList,Project projectInfo){
        this.boundingBoxActivity = boundingBoxActivity;
        mContext = boundingBoxActivity.getApplicationContext();
        project = projectInfo;
        if(problemWithClassList==null){
            problemList = new ArrayList<>();
        }
        else{
            problemList = problemWithClassList;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (position==0){ //문제 시작 전 설명 화면
            View view;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_start_boundingbox_problem, container, false);

            projectName = view.findViewById(R.id.boundingbox_project_name);
            wayContent = view.findViewById(R.id.boundingbox_way_content);
            conditionContent = view.findViewById(R.id.boundingbox_condition_content);
            requester = view.findViewById(R.id.boundingbox_requester);
            classListView = view.findViewById(R.id.boundingbox_classlist);
            exampleContent = view.findViewById(R.id.boundingbox_example_content);
            projectName.setText(project.getProjectName());
            wayContent.setText(project.getWayContent());
            conditionContent.setText(project.getConditionContent());
            requester.setText(project.getUserId());
            classListView.setText(project.getClass_list().toString());

            BoundingBoxPagerAdapter.DownloadDataTask downloadDataTask = new BoundingBoxPagerAdapter.DownloadDataTask();
            downloadDataTask.execute(project.getBucketName(),project.getExampleContent(),outputStream,"예시");

            container.addView(view);
            return view;
        }
        else {
            View view = null;
            if (mContext != null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_boundingbox_problem_set, container, false);

                TextView work_num = view.findViewById(R.id.boundingbox_work_num);

                //custom view initialize
                customView = new CustomView(mContext);
                customView = (CustomView)view.findViewById(R.id.customView);

                RadioGroup classList = view.findViewById(R.id.boundingbox_radio_group);
                for (int i = 0; i < problemList.get(position-1).getClassNameList().size()+1; i++) {
                    final RadioButton radioButton = new RadioButton(mContext);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    radioButton.setLayoutParams(param);
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currenPage = position;
                            label = radioButton.getText().toString();
                            customView.setLabel(label);
                        }
                    });
                    if (i==problemList.get(position-1).getClassNameList().size()){
                        radioButton.setText("없음");
                    }
                    else{
                        radioButton.setText(problemList.get(position-1).getClassNameList().get(i).getClassName());
                    }
                    radioButton.setId(i);
                    classList.addView(radioButton);
                }

                work_num.setText(Integer.toString(position));

                file_extension = FilenameUtils.getExtension(problemList.get(position-1).getProblem().getObjectName());
                file = new File("/data/data/com.ajou.capstone_design_freitag/files/project_boundingbox." + file_extension);
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                getBoundingBoxData(position, "바운딩박스");
            }
            container.addView(view);

            Button next = view.findViewById(R.id.boundingbox_upload);
            Button done = view.findViewById(R.id.boundingbox_done);
            done.setVisibility(View.GONE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadUserAnswer(label);
                }
            });
            if (position ==5){
                done.setVisibility(View.VISIBLE);
            }
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classificationWorkDone();
                    boundingBoxActivity.finish();
                }
            });
            return view;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }

    public void uploadUserAnswer(String answer) {
        if(problemId.contains(Integer.toString(problemList.get(currenPage-1).getProblem().getProblemId()))){
            problemId.remove(Integer.toString(problemList.get(currenPage-1).getProblem().getProblemId()));
            classAnswers.remove(currenPage-1);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.format("%s",answer));
        classAnswers.add(stringBuffer);
        problemId.add(Integer.toString(problemList.get(currenPage-1).getProblem().getProblemId()));
        Toast.makeText(mContext, "작업 등록 성공", Toast.LENGTH_LONG).show();
    }

    public void classificationWorkDone(){
        BoundingBoxPagerAdapter.BoundingBoxTask boundingBoxTask = new BoundingBoxPagerAdapter.BoundingBoxTask();
        boundingBoxTask.execute();
    }

    private static class BoundingBoxTask extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean result = null;
            try {
                for (int i = 0; i < classAnswers.size(); i++) {
                    System.out.println(problemId.get(i)+": "+classAnswers.get(i));
                }
                result = RESTAPI.getInstance().ClassificationWork(classAnswers,problemId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            if(result !=true){
                Toast.makeText(mContext, "분류 작업 실패", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(mContext, "분류 작업 완료", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void getBoundingBoxData(int position, final String dataType) {
        BoundingBoxPagerAdapter.DownloadDataTask downloadDataTask = new BoundingBoxPagerAdapter.DownloadDataTask();
        downloadDataTask.execute(problemList.get(position-1).getProblem().getBucketName(),problemList.get(position-1).getProblem().getObjectName(),outputStream,dataType);
    }

    private static class DownloadDataTask extends AsyncTask<Object, Void, Boolean>{
        String dataType;
        protected Boolean doInBackground(Object... dataInfos) {
            Boolean result = RESTAPI.getInstance().downloadObject((String)dataInfos[0],(String)dataInfos[1],(OutputStream)dataInfos[2]);
            dataType = (String)dataInfos[3];
            return result;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(!result){
                System.out.println("문제 데이터 다운로드 실패");
            }
            else
            {
                System.out.println("문제 데이터 다운로드 성공");
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if(dataType.equals("바운딩박스")) {
                    bitmap = BitmapFactory.decodeStream(inputStream).copy(Bitmap.Config.ARGB_8888, true);
                    //다운받은 그림으로 캔버스 설정
                    customView.setBitmap(bitmap);
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(dataType.equals("예시")){
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    exampleContent.setImageBitmap(bitmap);
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}