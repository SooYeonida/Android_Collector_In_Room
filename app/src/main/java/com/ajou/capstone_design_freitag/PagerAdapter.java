package com.ajou.capstone_design_freitag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.ui.dto.ProblemWithClass;

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

//오디오가 되긴되는데 프로그레스바 안움직이고 중단버튼도 안뜸 글고 한번만 재생 아마 뷰페이저로 뷰미리 만들어놓고 재생해서 그런 것 같음
public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private Context mContext;
    private List<ProblemWithClass> problemList;
    String file_extension;
    File file;
    OutputStream outputStream;

    ImageView imageView;
    LinearLayout audiolayout;
    ImageView play;
    ImageView pause;

    List<String> problemId = new ArrayList<>();
    MediaPlayer mediaPlayer = new MediaPlayer();
    List<String> answer = new ArrayList<>();

    List<StringBuffer> classAnswers = new ArrayList<>();

    int currenPage;

    public PagerAdapter(Context context, List<ProblemWithClass> problemWithClassList){
         mContext = context;
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
            view = inflater.inflate(R.layout.layout_prepare_problem, container, false);
            container.addView(view);
            return view;
        }
        else {
            View view = null;
            if (mContext != null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_problem_set, container, false);

                TextView work_num = view.findViewById(R.id.classification_work_num);
                TextView problem_num = view.findViewById(R.id.classification_problem_num);
                imageView = view.findViewById(R.id.classification_image);
                imageView.setVisibility(View.GONE);

                play = view.findViewById(R.id.classification_play);
                pause = view.findViewById(R.id.classification_pause);
                audiolayout = view.findViewById(R.id.classification_audio_layout);
                audiolayout.setVisibility(View.GONE);

                LinearLayout classList = view.findViewById(R.id.classification_checkbox_group);
                for (int i = 0; i < problemList.get(position-1).getClassNameList().size()+1; i++) {
                    final CheckBox checkBox = new CheckBox(mContext);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    checkBox.setLayoutParams(param);
                    checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currenPage = position;
                            answer.add(checkBox.getText().toString());
                        }
                    });
                    if (i==problemList.get(position-1).getClassNameList().size()){
                        checkBox.setText("없음");
                    }
                    else{
                        checkBox.setText(problemList.get(position-1).getClassNameList().get(i).getClassName());
                    }
                    checkBox.setId(i);
                    classList.addView(checkBox);
                }

                work_num.setText(Integer.toString(position));
                problem_num.setText(Integer.toString(problemList.get(position-1).getProblem().getProblemId()));

                file_extension = FilenameUtils.getExtension(problemList.get(position-1).getProblem().getObjectName());
                file = new File("/data/data/com.ajou.capstone_design_freitag/files/project_classification." + file_extension);
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //content type 받아와도 그게 어디 포함하는건지 구분해야되서 우선 일케해놓음
                if (file_extension.equals("jpg") || file_extension.equals("png") || file_extension.equals("jpeg")) { //흠 고쳐야하는뎅
                        imageView.setVisibility(View.VISIBLE);
                        audiolayout.setVisibility(View.GONE);
                        getClassificationData(position, "이미지");
                } else if (file_extension.equals("mp3") || file_extension.equals("wav") || file_extension.equals("m4a")) { //흠
                        imageView.setVisibility(View.GONE);
                        audiolayout.setVisibility(View.VISIBLE);
                        getClassificationData(position, "음성");
                }
            }
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pause.setVisibility(View.VISIBLE);
                    play.setVisibility(View.GONE);
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                    mediaPlayer.start();
                }
            });

            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pause.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    mediaPlayer.pause();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    readyToPlay();
                }
            });
            container.addView(view);

            Button next = view.findViewById(R.id.classification_upload);
            Button done = view.findViewById(R.id.classification_done);
            done.setVisibility(View.GONE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadUserAnswer(answer);
                }
            });
            if (position ==5){
                done.setVisibility(View.VISIBLE);
            }
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classificationWorkDone();
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

public void uploadUserAnswer(List<String> answers) {

        //확인해보기
    if(classAnswers.contains(problemList.get(currenPage-1).getProblem().getProblemId())){
        classAnswers.remove(problemList.get(currenPage-1).getProblem().getProblemId());
    }
    if(problemId.contains(problemList.get(currenPage-1).getProblem().getProblemId())){
        problemId.remove(problemList.get(currenPage-1).getProblem().getProblemId());
    }

    StringBuffer stringBuffer = new StringBuffer();
    for(int i=0;i<answers.size();i++){
       stringBuffer.append(String.format("%s",answers.get(i)));
       if(i!=answers.size()-1){
           stringBuffer.append("&");
       }
    }
    classAnswers.add(stringBuffer);
    problemId.add(Integer.toString(problemList.get(currenPage-1).getProblem().getProblemId()));
    answers.removeAll(answers);
    Toast.makeText(mContext, "작업 등록 성공", Toast.LENGTH_LONG).show();
}

public void classificationWorkDone(){
    AsyncTask<Void,Void,Boolean> classificationTask = new AsyncTask<Void, Void, Boolean>() {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean result = null;
            try {
                result = RESTAPI.getInstance().labellingWork(classAnswers,problemId);
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
        }
    };
    classificationTask.execute();
}

    public void getClassificationData(int position, final String dataType) {
        AsyncTask<Object, Void, Boolean> downloadExampleTask = new AsyncTask<Object, Void, Boolean>() {
            protected Boolean doInBackground(Object... dataInfos) {
                Boolean result = RESTAPI.getInstance().downloadObject((String)dataInfos[0],(String)dataInfos[1],(OutputStream)dataInfos[2]);
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
                    if(dataType.equals("이미지")) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(bitmap);
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(dataType.equals("음성")){
                        readyToPlay();
                    }
                }
            }

        };
        downloadExampleTask.execute(problemList.get(position-1).getProblem().getBucketName(),problemList.get(position-1).getProblem().getObjectName(),outputStream);
    }

    public void readyToPlay() {
        try {
            Uri uri = Uri.fromFile(file);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mContext, uri);
            mediaPlayer.prepare();
            if(mediaPlayer.isPlaying()){
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
            }else{
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
            }
        }
        catch (Exception e) {
            Log.e("SimplePlayer", e.getMessage());
        }
    }


}
