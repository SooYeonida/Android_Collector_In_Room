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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.Audio.AudioTrimmerActivity;
import com.ajou.capstone_design_freitag.ui.plus.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AudioCollectionActivity extends AppCompatActivity {
    private static final int COLLECTION_AUDIO_REQUEST_CODE =100;
    private static final int ADD_AUDIO = 1001;
    private static final int REQUEST_ID_PERMISSIONS = 1;

    TextView projectName;
    TextView wayContent;
    TextView conditionContent;
    ImageView exampleContent;//오디오 파일은 예시 어케 보여주지? 재생하는거 만들어야하남

    Button upload;
    Button record;
    TextView dataURI;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_collection);

        Intent intent = getIntent();
        Project project = intent.getParcelableExtra("project"); //리스트에서 사용자가 선택한 프로젝트 정보 받아옴

        projectName = findViewById(R.id.audio_collection_project_name);
        wayContent = findViewById(R.id.audio_collection_way_content);
        conditionContent = findViewById(R.id.audio_collection_condition_content);
        upload = findViewById(R.id.collection_audio_upload);
        record = findViewById(R.id.collection_audio_record);
        dataURI = findViewById(R.id.collection_audio_uri);

        projectName.setText(project.getProjectName());
        wayContent.setText(project.getWayContent());
        conditionContent.setText(project.getConditionContent());

        context = getApplicationContext();

        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
             upload_audio_collection_data(v);
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

    }

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

    public void upload_user_record_audio_data(String path){
        if (checkStoragePermission()) {
            startActivityForResult(new Intent(AudioCollectionActivity.this, AudioTrimmerActivity.class), ADD_AUDIO);
            overridePendingTransition(0, 0);
        } else {
            requestStoragePermission();
        }
        File file = new File(path);
        final String fileName = file.getName();
        dataURI.setText(dataURI.getText() + "\n" + fileName);
        AsyncTask<File, Void, Boolean> uploadUserAudioTask = new AsyncTask<File, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(File... files) {
                try {
                    InputStream inputStream = new FileInputStream(files[0]);
                    //boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream,"example.txt", "text/plain");
                    boolean result = true; //다른걸로 나중에 바꿔야함
                    inputStream.close();
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Boolean(false);
                }
            }
            @Override
            protected void onPostExecute(Boolean result) {
                if(!result){
                    Toast.makeText(context, "음성 수집 데이터 업로드 실",Toast.LENGTH_LONG).show();
                }
            }
        };
        uploadUserAudioTask.execute(file);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void upload_audio_collection_data(View view){ //파일 선택하면 바로 올라가는게 아니라 편집 화면 뜨게해야함
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("file/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, COLLECTION_AUDIO_REQUEST_CODE);
    }

    public String getFileNameToUri(Uri data) {
        String[] proj = {MediaStore.Files.FileColumns.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        return imgName;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COLLECTION_AUDIO_REQUEST_CODE) {
            Uri uri = data.getData();
            final String fileName = getFileNameToUri(data.getData());//이름
            dataURI.setText(dataURI.getText() + "\n" + fileName);
            //uri
            AsyncTask<Uri, Void, Boolean> uploadAudioTask = new AsyncTask<Uri, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Uri... uris) {
                    try {
                        //여기서 파일을 트리머 액티비티로 보내야함.
                        if (checkStoragePermission()) {
                            startActivityForResult(new Intent(AudioCollectionActivity.this, AudioTrimmerActivity.class), ADD_AUDIO);
                            overridePendingTransition(0, 0);
                        } else {
                            requestStoragePermission();
                        }
                        InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                       // boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream, fileName, "audio/mp3");
                        boolean result = true;
                        return new Boolean(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new Boolean(false);
                    }
                }
            };
            uploadAudioTask.execute(uri);
        }
        else if (requestCode == ADD_AUDIO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String path = data.getExtras().getString("INTENT_AUDIO_FILE");
                    upload_user_record_audio_data(path);
                    Toast.makeText(AudioCollectionActivity.this, "Audio stored at " + path, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
