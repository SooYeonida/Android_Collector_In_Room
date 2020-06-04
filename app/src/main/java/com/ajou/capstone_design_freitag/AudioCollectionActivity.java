package com.ajou.capstone_design_freitag;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.Audio.AudioTrimmerActivity;
import com.ajou.capstone_design_freitag.ui.dto.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class AudioCollectionActivity extends AppCompatActivity {
    private static final int COLLECTION_AUDIO_REQUEST_CODE =100;
    private static final int ADD_AUDIO = 1001;
    private static final int REQUEST_ID_PERMISSIONS = 1;

    TextView projectName;
    TextView wayContent;
    TextView conditionContent;
    TextView requester;
    TextView classlistview;
    Button select;
    Button record;
    Button upload;
    Button work_done;
    Button delete;
    TextView dataURI;
    Context context;
    Project project;
    String classname;

    ImageView play;
    ImageView pause;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    boolean isPlaying = true;
    ProgressUpdate progressUpdate;

    RadioGroup class_list;

    List<InputStream> inputStreamList = new ArrayList<>();
    List<String> fileNameList = new ArrayList<>();
    List<String> classList = new ArrayList<>();

    Map<String,Integer> class_count = new HashMap<>();

    File file = new File("/data/data/com.ajou.capstone_design_freitag/files/project_example.mp3");
    OutputStream outputStream = new FileOutputStream(file);

    public AudioCollectionActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_collection);

        Intent intent = getIntent();
        project = intent.getParcelableExtra("project"); //리스트에서 사용자가 선택한 프로젝트 정보 받아옴
        Project.getProjectinstance().setBucketName(project.getBucketName());
        Project.getProjectinstance().setProjectId(project.getProjectId());

        context = getApplicationContext();

        projectName = findViewById(R.id.audio_collection_project_name);
        wayContent = findViewById(R.id.audio_collection_way_content);
        conditionContent = findViewById(R.id.audio_collection_condition_content);
        select = findViewById(R.id.collection_audio_select);
        record = findViewById(R.id.collection_audio_record);
        work_done = findViewById(R.id.work_done_audio);
        upload = findViewById(R.id.collection_upload_audio);
        delete = findViewById(R.id.delete_audio_file);
        delete.setVisibility(View.GONE);
        dataURI = findViewById(R.id.collection_audio_uri);
        class_list = findViewById(R.id.radioGroup_class_list_audio);
        requester = findViewById(R.id.audio_collection_work_requester);
        classlistview = findViewById(R.id.audio_classlist_project_detail);

        //radiobutton 동적생성
        for(int i=0;i<project.getClass_list().size();i++){
            RadioButton radioButton = new RadioButton(context);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton.setLayoutParams(param);
            radioButton.setText(project.getClass_list().get(i));
            radioButton.setId(i);
            class_list.addView(radioButton);
        }

        //라디오버튼 리스너
        class_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i=0;i<project.getClass_list().size();i++){
                    if(i == checkedId){
                        classname =  project.getClass_list().get(i);
                    }
                }
            }
        });

        //예시 음악 재생
        mediaPlayer = new MediaPlayer();
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        pause.setVisibility(View.GONE);
        seekBar = (SeekBar)findViewById(R.id.seekbar);

        try {
            getExampleData(); //예시데이터 불러옴 - 재생준비
        } catch (IOException e) {
            e.printStackTrace();
        }

        projectName.setText(project.getProjectName());
        wayContent.setText(project.getWayContent());
        conditionContent.setText(project.getConditionContent());
        requester.setText(project.getUserId());
        classlistview.setText(project.getClass_list().toString());


        select.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                select_audio_collection_data(v);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    startActivityForResult(new Intent(AudioCollectionActivity.this, AudioTrimmerActivity.class), ADD_AUDIO);
                    overridePendingTransition(0, 0);
                } else {
                    requestStoragePermission();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class_count.put(classname,inputStreamList.size());//클래스에 해당하는 데이타 개수 몇개인지 저장.
                classList.add(classname);
                System.out.println(classname+":"+inputStreamList.size());

                dataURI.setText("");
                Toast.makeText(getApplicationContext(),"오디오 등록 완료",Toast.LENGTH_LONG).show();

            }
        });

        work_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputStreamList.size()==0){
                    Toast.makeText(getApplicationContext(),"데이터를 업로드 하셔야 합니다.",Toast.LENGTH_LONG).show();
                }
                else {
                    upload_audio_data(inputStreamList,fileNameList,classname);
                    Toast.makeText(context, "작업 완료",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputStreamList.remove(inputStreamList.size()-1);//젤 최근꺼 삭제
                fileNameList.remove(fileNameList.size()-1);
                StringTokenizer stringTokenizer = new StringTokenizer(dataURI.getText().toString(),"\n");
                dataURI.setText("");
                while(stringTokenizer.countTokens()!=1){
                    dataURI.setText(dataURI.getText() + "\n" + stringTokenizer.nextToken());
                }
            }
        });

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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                if(seekBar.getProgress()>0&&play.getVisibility()==View.GONE){
                    mediaPlayer.start();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                readyToPlay();
            }
        });
    }

    //예시데이터 재생
    private void getExampleData() throws IOException {
        AsyncTask<Object, Void, Boolean> downloadExampleTask = new AsyncTask<Object, Void, Boolean>() {
            protected Boolean doInBackground(Object... dataInfos) {
                Boolean result = RESTAPI.getInstance().downloadObject((String)dataInfos[0],(String)dataInfos[1],(OutputStream)dataInfos[2]);
                return result;
            }
            @Override
            protected void onPostExecute(Boolean result) {
                if(!result){
                    System.out.println("예시 다운로드 실패");
                }
                else
                {
                    System.out.println("예시 다운로드 성공");
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    readyToPlay();
                    progressUpdate = new ProgressUpdate();
                    progressUpdate.start();
                }
            }

        };
        downloadExampleTask.execute(project.getBucketName(),project.getExampleContent(),outputStream);
    }

    public void readyToPlay() {
        try {
            seekBar.setProgress(0);
            Uri uri = Uri.fromFile(file);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
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

    class ProgressUpdate extends Thread{
        @Override
        public void run() {
            while(isPlaying){
                try {
                    Thread.sleep(500);
                    if(mediaPlayer!=null){
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                } catch (Exception e) {
                    Log.e("ProgressUpdate",e.getMessage());
                }

            }
        }
    }

    //오디오 트리머 퍼미션
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(AudioCollectionActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO},
                REQUEST_ID_PERMISSIONS);
    }

    private boolean checkStoragePermission() {
        return (ActivityCompat.checkSelfPermission(AudioCollectionActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(AudioCollectionActivity.this,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AudioCollectionActivity.this, "Permission granted, Click again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //직접 녹음 & 업로드
    public void save_user_record_audio_data(String path) throws IOException {
        if (checkStoragePermission()) {
            startActivityForResult(new Intent(AudioCollectionActivity.this, AudioTrimmerActivity.class), ADD_AUDIO);
            overridePendingTransition(0, 0);
        } else {
            requestStoragePermission();
        }
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        inputStreamList.add(inputStream);
        final String fileName = file.getName();
        dataURI.setText(dataURI.getText() + "\n" + fileName);
        fileNameList.add(fileName);

        delete.setVisibility(View.VISIBLE);
    }

    //파일 선택 & 업로드
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void select_audio_collection_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("file/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, COLLECTION_AUDIO_REQUEST_CODE);
        delete.setVisibility(View.VISIBLE);
    }

    public void upload_audio_data(List<InputStream> inputStreams, List<String> filenames, String classname){
        AsyncTask<Object, Void, Boolean> uploadUserAudioTask = new AsyncTask<Object, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... info) {
                try {
                    boolean result = RESTAPI.getInstance().collectionWork((List<InputStream>)info[0],(List<String>)info[1],"audio/mp3",(String)info[2]);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Boolean(false);
                }
            }
            @Override
            protected void onPostExecute(Boolean result) {
                if(result){
                    Toast.makeText(context, "수집 작업 오디오 업로드 완료",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(context, "수집 작업 오디오 업로드 실패",Toast.LENGTH_LONG).show();
                }
            }
        };
        uploadUserAudioTask.execute(inputStreams,filenames,classname);
    }

    public String getFileNameToUri(Uri data) {
        String[] proj = //{MediaStore.Audio.Media.TITLE};
         {MediaStore.Files.FileColumns.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String audioPath = cursor.getString(column_index);
        String audioName = audioPath.substring(audioPath.lastIndexOf("/") + 1);
        return audioName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COLLECTION_AUDIO_REQUEST_CODE) {
            List<Uri> uriList = new ArrayList<>();
            uriList.add(data.getData());
            System.out.println("urlList size:" + uriList.size());
            for (int i = 0; i < uriList.size(); i++) {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(uriList.get(i));
                    inputStreamList.add(inputStream);
                    String fileName = getFileNameToUri(uriList.get(i));
                    dataURI.setText(dataURI.getText() + "\n" + fileName);
                    fileNameList.add(fileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == ADD_AUDIO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String path = data.getExtras().getString("INTENT_AUDIO_FILE");
                    try {
                        save_user_record_audio_data(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(AudioCollectionActivity.this, "Audio stored at " + path, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}