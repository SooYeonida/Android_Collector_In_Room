package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.ui.plus.Project;

import java.io.InputStream;
import java.util.ArrayList;

public class ImageCollectionActivity extends AppCompatActivity {

    private static final int COLLECTION_IMAGE_REQUEST_CODE = 100;
    private ArrayList<Uri> collectionPictures;

    TextView projectName;
    TextView wayContent;
    TextView conditionContent;
    ImageView exampleContent;
    TextView dataURI;
    Button upload;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_collection);
        collectionPictures = new ArrayList<>();
        Intent intent = getIntent();
        Project project = intent.getParcelableExtra("project"); //리스트에서 사용자가 선택한 프로젝트 정보 받아옴

        projectName = findViewById(R.id.image_collection_project_name);
        wayContent = findViewById(R.id.image_collection_way_content);
        conditionContent = findViewById(R.id.image_collection_condition_content);
        dataURI = findViewById(R.id.collection_image_uri);
        upload = findViewById(R.id.collection_image_upload);

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
    }

    public void upload_collection_image_data(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, COLLECTION_IMAGE_REQUEST_CODE);
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
                    collectionPictures.add(clipData.getItemAt(i).getUri());
                    final String fileName = getFileNameToUri(data.getData());
                    dataURI.setText(dataURI.getText() + "\n" + fileName);
                    //clipData.getItemAt(i).getUri()
                    try {
                        AsyncTask<Uri, Void, Boolean> uploadImageTask = new AsyncTask<Uri, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Uri... uris) {
                                try {
                                    InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                                    //boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream,fileName,"image/jpeg");
                                    boolean result = true; //이거말고 수집 작업 데이터 업로드하는걸 불러야겠
                                    return new Boolean(result);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    return new Boolean(false);
                                }
                            }
                        };
                        uploadImageTask.execute(clipData.getItemAt(i).getUri());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}