package com.ajou.capstone_design_freitag.Work;

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

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ClassificationPagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    // LayoutInflater 서비스 사용을 위한 Context 참조 저장.
    private ClassificationActivity classificationActivity;
    private static Context mContext;
    private static List<ProblemWithClass> problemList;

    //이미지
    static ImageView imageView;
    //오디오
    LinearLayout audiolayout;
    static TextView textView;
    static ImageView play;
    static ImageView pause;
    static MediaPlayer mediaPlayer = new MediaPlayer();

    //바운딩박스
    static TextView conditionView;
    static CustomView boundingBoxView;
    static LinearLayout boundingBoxQuestionLayout;

    //답제출
    StringBuffer answer;
    List<String> problemId = new ArrayList<>();
    List<StringBuffer> classAnswers = new ArrayList<>();
    static int currentPage;

    List<InputStream> inputStreams;
    List<String> extension;
    List<Uri> uris;

    public ClassificationPagerAdapter(ClassificationActivity classificationActivity, List<ProblemWithClass> problemWithClassList,List<InputStream> inputStreamList,
                                      List<String> fileExtensionList,List<Uri> uriList){
        this.classificationActivity = classificationActivity;
        mContext = classificationActivity.getApplicationContext();
        inputStreams = inputStreamList;
        extension = fileExtensionList;
        uris = uriList;
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
            view = inflater.inflate(R.layout.layout_start_classification_problem, container, false);
            container.addView(view);
            return view;
        }
        else {
            View view = null;
            if (mContext != null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_classification_problem_set, container, false);

                TextView work_num = view.findViewById(R.id.classification_work_num);
                TextView problem_num = view.findViewById(R.id.classification_problem_num);
                imageView = view.findViewById(R.id.classification_image);
                imageView.setVisibility(View.GONE);

                play = view.findViewById(R.id.classification_play);
                pause = view.findViewById(R.id.classification_pause);
                audiolayout = view.findViewById(R.id.classification_audio_layout);
                audiolayout.setVisibility(View.GONE);

                textView = view.findViewById(R.id.classification_text);
                textView.setVisibility(View.GONE);

                TextView conditionText = view.findViewById(R.id.conditionText);
                conditionText.setVisibility(View.GONE);
                conditionView = view.findViewById(R.id.conditionView);
                conditionView.setVisibility(View.GONE);
                boundingBoxView = view.findViewById(R.id.boundingBoxView);
                boundingBoxView.setVisibility(View.GONE);
                boundingBoxQuestionLayout = view.findViewById(R.id.boundingQuestionLayout);
                boundingBoxQuestionLayout.setVisibility(View.GONE);
                TextView labelTextView = view.findViewById(R.id.labelTextView);

                RadioGroup classList = view.findViewById(R.id.classification_radio_group);
                RadioGroup classList2 = view.findViewById(R.id.classification_radio_group2);
                for (int i = 0; i < problemList.get(position-1).getClassNameList().size()+1; i++) {
                    final RadioButton radioButton = new RadioButton(mContext);
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    radioButton.setLayoutParams(param);
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentPage = position;
                            answer = new StringBuffer();
                            answer.append(radioButton.getText().toString());
                        }
                    });
                    if (i==problemList.get(position-1).getClassNameList().size()){
                        radioButton.setText("없음");
                    }
                    else{
                        radioButton.setText(problemList.get(position-1).getClassNameList().get(i).getClassName());
                    }
                        if(i<=5){
                            classList.addView(radioButton);
                        }
                        else{
                            classList2.addView(radioButton);
                        }
                }

                work_num.setText(Integer.toString(position));
                problem_num.setText(Integer.toString(problemList.get(position-1).getProblem().getProblemId()));

                if (extension.get(position-1).equals("jpg") || extension.get(position-1).equals("png") || extension.get(position-1).equals("jpeg")) {
                    if(problemList.get(position-1).getBoundingBoxList() == null) {
                        imageView.setVisibility(View.VISIBLE);
                        audiolayout.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStreams.get(position-1));
                      //  Bitmap resizeImgBitmap = Bitmap.createScaledBitmap(bitmap, 800,600, true);
                        imageView.setImageBitmap(bitmap);
                    }
                    else{
                        imageView.setVisibility(View.GONE);
                        audiolayout.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        classList.setVisibility(View.GONE);
                        conditionText.setVisibility(View.VISIBLE);
                        conditionView.setVisibility(View.VISIBLE);
                        conditionView.setText(problemList.get(position-1).getConditionContent());
                        boundingBoxView.setVisibility(View.VISIBLE);
                        boundingBoxQuestionLayout.setVisibility(View.VISIBLE);
                        labelTextView.setVisibility(View.GONE);
                        setBoundingBoxProblem(inputStreams.get(position-1),position);
                    }
                } else if (extension.get(position-1).equals("mp3") ||extension.get(position-1).equals("wav") || extension.get(position-1).equals("m4a")) {
                        imageView.setVisibility(View.GONE);
                        audiolayout.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                        readyToPlay(uris.get(position-1));
                } else if(extension.get(position-1).equals("txt")) {
                    imageView.setVisibility(View.GONE);
                    audiolayout.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    try {
                        jsonParse(inputStreams.get(position-1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                    readyToPlay(uris.get(position));
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
                    classificationActivity.finish();
                }
            });

            return view;
        }
    }

    private void setBoundingBoxProblem(InputStream inputStream,int position) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream).copy(Bitmap.Config.ARGB_8888,true);
        boundingBoxView.setBitmap(bitmap);
        List<String> labelList = new ArrayList<>();
        List<String> rectList = new ArrayList<>();

        for (int i = 0; i < problemList.get(position-1).getBoundingBoxList().size(); i++) {
            labelList.add(Integer.toString(problemList.get(position - 1).getBoundingBoxList().get(i).getBoxId()));
            String[] rect = problemList.get(position - 1).getBoundingBoxList().get(i).getCoordinates().split(" ");
            for(int j=0;j<rect.length;j++){
                rectList.add(rect[j]);
            }
        }
        boundingBoxView.setLabel(labelList);
        boundingBoxView.setRect(rectList);


        for (int i = 0; i < problemList.get(position-1).getBoundingBoxList().size(); i++) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            LinearLayout sentence1 = new LinearLayout(mContext);
            sentence1.setOrientation(LinearLayout.HORIZONTAL);
            sentence1.setLayoutParams(param);
            TextView q = new TextView(mContext);
            q.setText("Q. 위 사진 속 ");
            q.setLayoutParams(param);
            sentence1.addView(q);
            final TextView boxId = new TextView(mContext);
            boxId.setLayoutParams(param);
            boxId.setText(Integer.toString(problemList.get(position-1).getBoundingBoxList().get(i).getBoxId()));
            sentence1.addView(boxId);
            TextView q2 = new TextView(mContext);
            q2.setLayoutParams(param);
            q2.setText("번 바운딩 박스가 주어진 작업 조건에 맞게");
            sentence1.addView(q2);

            LinearLayout sentence2 = new LinearLayout(mContext);
            sentence2.setOrientation(LinearLayout.HORIZONTAL);
            sentence2.setLayoutParams(param);
            TextView label = new TextView(mContext);
            label.setText(problemList.get(position-1).getBoundingBoxList().get(i).getClassName());
            label.setLayoutParams(param);
            sentence2.addView(label);
            TextView q3 = new TextView(mContext);
            q3.setText("을 포함하고 있습니까?");
            q3.setLayoutParams(param);
            sentence2.addView(q3);

            boundingBoxQuestionLayout.addView(sentence1);
            boundingBoxQuestionLayout.addView(sentence2);

            RadioGroup answerRadioGroup = new RadioGroup(mContext);
            answerRadioGroup.setLayoutParams(param);
            answerRadioGroup.setOrientation(LinearLayout.HORIZONTAL);

            final RadioButton yes = new RadioButton(mContext);
            final RadioButton no = new RadioButton(mContext);
            yes.setLayoutParams(param);
            yes.setText("예");
            no.setLayoutParams(param);
            no.setText("아니오");

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPage = position;
                    answer = new StringBuffer();
                    answer.append(boxId.getText().toString());
                    answer.append(" ");
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            answerRadioGroup.addView(yes);
            answerRadioGroup.addView(no);
            boundingBoxQuestionLayout.addView(answerRadioGroup);
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

public void uploadUserAnswer(StringBuffer answer) {
        if(problemId.contains(Integer.toString(problemList.get(currentPage -1).getProblem().getProblemId()))){
            problemId.remove(Integer.toString(problemList.get(currentPage -1).getProblem().getProblemId()));
            classAnswers.remove(currentPage -1);
        }
    classAnswers.add(answer);
    System.out.println("현재까지 classAnswer: "+classAnswers.toString());
    problemId.add(Integer.toString(problemList.get(currentPage -1).getProblem().getProblemId()));
    Toast.makeText(mContext, "작업 등록 성공", Toast.LENGTH_LONG).show();
}

public void classificationWorkDone(){
    ClassificationTask classificationTask = new ClassificationTask();
    classificationTask.execute();
}

private class ClassificationTask extends AsyncTask<Void,Void,Boolean>{
    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean result = null;
        try {
            System.out.println("분류 답:"+classAnswers.toString());
            System.out.println("분류 문제아이디: "+problemId.toString());
            result = RESTAPI.getInstance().classificationWork(classAnswers,problemId);
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

    public static void jsonParse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuffer strBuffer = new StringBuffer();
        while(true){
            try {
                if (!((line=reader.readLine())!=null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            strBuffer.append(line+"\n");
        }
        reader.close();
        inputStream.close();
        JSONParser jsonParser = new JSONParser();
        String textData = "";
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(strBuffer.toString());
            JSONArray setArray = (JSONArray) jsonObject.get("set");
            for(int i=0;i<setArray.size();i++){
                JSONObject set = (JSONObject)setArray.get(i);
                textData+="질문:"+set.get("question")+"\n";
                textData+="답:"+set.get("answer")+"\n";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textView.setText(textData);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readyToPlay(Uri uri) {
        try {
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
