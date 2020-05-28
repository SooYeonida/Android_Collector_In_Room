package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.ui.plus.Project;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageCollectionActivity extends AppCompatActivity {

    private static final int COLLECTION_IMAGE_REQUEST_CODE = 100;

    TextView projectName;
    TextView wayContent;
    TextView conditionContent;
    ImageView exampleContent;
    TextView dataURI;
    Button upload;
    Button work_done;
    Context context;
    Project project;

    List<InputStream> inputStreamList = new ArrayList<>();
    List<String> fileNameList = new ArrayList<>();

    File file = new File("/data/data/com.ajou.capstone_design_freitag/files/project_example.jpg");
    OutputStream outputStream = new FileOutputStream(file);

    public ImageCollectionActivity() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_collection);
        Intent intent = getIntent();
        project = intent.getParcelableExtra("project"); //리스트에서 사용자가 선택한 프로젝트 정보 받아옴
        Project.getProjectinstance().setBucketName(project.getBucketName());
        Project.getProjectinstance().setProjectId(project.getProjectId());
        projectName = findViewById(R.id.image_collection_project_name);
        wayContent = findViewById(R.id.image_collection_way_content);
        conditionContent = findViewById(R.id.image_collection_condition_content);
        dataURI = findViewById(R.id.collection_image_uri);
        upload = findViewById(R.id.collection_image_upload);
        work_done = findViewById(R.id.work_done_image);
        exampleContent = findViewById(R.id.work_example_content_image);

        try {
            getExampleData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        projectName.setText(project.getProjectName());
        wayContent.setText(project.getWayContent());
        conditionContent.setText(project.getConditionContent());

        context = getApplicationContext();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_collection_image_data(v);
            }
        });
        work_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputStreamList.size()==0){
                    Toast.makeText(getApplicationContext(),"데이터를 업로드 하셔야 합니다.",Toast.LENGTH_LONG).show();
                }
                else {
                    user_work_done();
                    Toast.makeText(context, "작업 완료",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

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
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    exampleContent.setImageBitmap(bitmap);
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        downloadExampleTask.execute(project.getBucketName(),project.getExampleContent(),outputStream);

    }

    public void upload_collection_image_data(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, COLLECTION_IMAGE_REQUEST_CODE);
    }

    public void user_work_done(){
        try {
            AsyncTask<Object, Void, Boolean> uploadImageTask = new AsyncTask<Object, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Object... info) {
                    try {

                        boolean result = RESTAPI.getInstance().collection_work((List<InputStream>)info[0],(List<String>)info[1],"image/jpeg");
                        return new Boolean(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new Boolean(false);
                    }
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    if(result){
                        Toast.makeText(getApplicationContext(),"수집 작업 이미지 업로드 성공",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"수집 작업 이미지 업로드 실패",Toast.LENGTH_LONG).show();
                    }
                }

            };
            uploadImageTask.execute(inputStreamList,fileNameList);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        if (requestCode == COLLECTION_IMAGE_REQUEST_CODE) {
            ClipData clipData = data.getClipData();
            for (int i = 0; i < clipData.getItemCount(); i++) {
                if (requestCode == COLLECTION_IMAGE_REQUEST_CODE) {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(clipData.getItemAt(i).getUri());
                        inputStreamList.add(inputStream);
                        String fileName = getFileNameToUri(clipData.getItemAt(i).getUri());
                        dataURI.setText(dataURI.getText() +"\n" + fileName);
                        fileNameList.add(fileName);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}