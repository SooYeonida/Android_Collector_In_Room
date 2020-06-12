package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.ui.dto.Project;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageCollectionActivity extends AppCompatActivity {

    private static final int COLLECTION_IMAGE_REQUEST_CODE = 100;
    private static final int COLLECTION_IMAGE_CAPTURE_REQUEST_CODE = 101;

    TextView projectName;
    TextView wayContent;
    TextView conditionContent;
    ImageView exampleContent;
    TextView requester;
    TextView classListView;
    TextView dataURI;
    RadioGroup selectClass;
    Button select;
    Button capture;
    Button upload;
    Context context;
    Project project;
    String classname;

    List<InputStream> inputStreamList = new ArrayList<>();
    List<String> fileNameList = new ArrayList<>();
    List<String> classList = new ArrayList<>();

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

        context = getApplicationContext();

        projectName = findViewById(R.id.image_collection_project_name);
        wayContent = findViewById(R.id.image_collection_way_content);
        conditionContent = findViewById(R.id.image_collection_condition_content);
        dataURI = findViewById(R.id.collection_image_uri);
        select = findViewById(R.id.collection_image_select);
        capture = findViewById(R.id.collection_image_capture);
        upload = findViewById(R.id.collection_image_upload);
        exampleContent = findViewById(R.id.work_example_content_image);
        selectClass = findViewById(R.id.radioGroup_class_list_image);

        requester = findViewById(R.id.image_collection_work_requester);
        classListView = findViewById(R.id.image_classlist_project_detail);

        for(int i=0;i<project.getClass_list().size();i++){
            RadioButton radioButton = new RadioButton(context);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton.setLayoutParams(param);
            radioButton.setText(project.getClass_list().get(i));
            radioButton.setId(i);
            selectClass.addView(radioButton);
        }

            getExampleData();

        projectName.setText(project.getProjectName());
        wayContent.setText(project.getWayContent());
        conditionContent.setText(project.getConditionContent());

        requester.setText(project.getUserId());
        classListView.setText(project.getClass_list().toString());

        //라디오버튼 리스너
        selectClass.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i=0;i<project.getClass_list().size();i++){
                    if(i == checkedId){
                        classname =  project.getClass_list().get(i);
                    }
                }
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_collection_image_data(v);
            }
        });

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture_collection_image_data(v);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataURI.setText("");
                if (inputStreamList.size()==0){
                    Toast.makeText(getApplicationContext(),"데이터를 업로드 하셔야 합니다.",Toast.LENGTH_LONG).show();
                }
                else {
                    upload_image_data(inputStreamList,fileNameList,classname);

                    Toast.makeText(context, "작업 완료",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getExampleData(){
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

    public void select_collection_image_data(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, COLLECTION_IMAGE_REQUEST_CODE);
    }

    public void capture_collection_image_data(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, COLLECTION_IMAGE_CAPTURE_REQUEST_CODE);
    }

    public void upload_image_data(List<InputStream> inputStreams, List<String> filenames, String classname){
            try {
                AsyncTask<Object, Void, Boolean> uploadImageTask = new AsyncTask<Object, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Object... info) {
                        try {

                            boolean result = RESTAPI.getInstance().collectionWork((List<InputStream>) info[0], (List<String>) info[1], "image/jpeg",(String)info[2]);
                            return new Boolean(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return new Boolean(false);
                        }
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        if (result) {
                            Toast.makeText(getApplicationContext(), "수집 작업 이미지 업로드 성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),PopupActivity.class);
                            intent.putExtra("type","image");
                            intent.putExtra("project", project);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "수집 작업 이미지 업로드 실패", Toast.LENGTH_LONG).show();
                        }
                    }

                };
                uploadImageTask.execute(inputStreams,filenames,classname);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public String getFileNameToUri(Uri uri) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            String[] proj = { MediaStore.Files.FileColumns.DISPLAY_NAME };
            try (Cursor cursor = getContentResolver().query(uri, proj, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                }
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == COLLECTION_IMAGE_REQUEST_CODE) {
                ClipData clipData = data.getClipData();
                classList.add(classname);
                System.out.println(classname + ":" + clipData.getItemCount());

                for (int i = 0; i < clipData.getItemCount(); i++) {
                    if (requestCode == COLLECTION_IMAGE_REQUEST_CODE) {
                        try {
                            Uri uri = clipData.getItemAt(i).getUri();
                            addFileToList(uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (requestCode == COLLECTION_IMAGE_CAPTURE_REQUEST_CODE) {
                try {
                    String dirPath = getCacheDir().getAbsolutePath() + "/freitag";
                    File dir = new File(dirPath);
                    if(!dir.exists()) {
                        dir.mkdir();
                    }
                    String filePath = dirPath + "/" + UUID.randomUUID() + ".jpg";
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));

                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.close();

                    addFileToList(Uri.fromFile(new File(filePath)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addFileToList(Uri uri) throws FileNotFoundException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        inputStreamList.add(inputStream);
        String fileName = getFileNameToUri(uri);
        dataURI.setText(dataURI.getText() + "\n" + fileName);
        fileNameList.add(fileName);
    }
}