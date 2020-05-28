package com.ajou.capstone_design_freitag.ui.plus;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProjectMakeFragment extends Fragment implements View.OnClickListener {

    private static final int EXAMPLE_PICTURE_IMAGE_REQUEST_CODE = 100;
    private static final int LABELING_PICTURE_IMAGE_REQUEST_CODE = 101;
    private static final int LOGIN_REQUEST_CODE = 102;
    private static final int EXAMPLE_AUDIO_REQUEST_CODE =103;
    private static final int EXAMPLE_TEXT_REQUEST_CODE = 104;

    private ArrayList<Uri> examplePictures;
    private ArrayList<Uri> labelingPictures;

    LinearLayout layout_plus;
    LinearLayout example_content_layout;
    LinearLayout example_data_upload;
    LinearLayout text_upload_way;
    LinearLayout labelling_upload;

    Button make_button;
    Button text_example_data_button;
    Button example_data_button;
    Button labelling_data_button;
    Button class_input;

    TextView exampleURI;
    private Context context;

    PlusFragment plusFragment;

    int id_class = 0;
    private RadioGroup labelling_work;
    private RadioGroup collection_data;
    private RadioGroup text_example;
    private RadioButton image;
    private RadioButton audio;
    private RadioButton text;
    private RadioButton boundingbox;
    private RadioButton classification;
    private RadioButton user_input_text;
    private RadioButton text_file_upload;

    private EditText project_name;
    private EditText project_subject;
    private EditText description;
    private EditText way_content;
    private EditText example_content;
    private EditText condition_content;
    private EditText total_data;
    private TextView worktype_text;

    LinearLayout collection_data_type;
    LinearLayout labelling_work_type;

    String worktype = null;
    String datatype = null;
    String button_result = null;


    List<EditText> addclass = new ArrayList<EditText>();
    List<String> classList = new ArrayList<String>();

    public static ProjectMakeFragment newInstance(String worktype) {
        ProjectMakeFragment fragment = new ProjectMakeFragment();
        Bundle args = new Bundle();
        args.putString("worktype",worktype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESTAPI instance = RESTAPI.getInstance();
        //토큰 받아오는데 null이면 로그인
        if(instance.getToken()==null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
        if (getArguments() != null) {
            button_result = getArguments().getString("worktype");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        examplePictures = new ArrayList<>();
        labelingPictures = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_project_make, container, false);
        layout_plus = (LinearLayout)view.findViewById(R.id.linearlayout_in_plus);
        example_content_layout = (LinearLayout)view.findViewById(R.id.example_content_layout);
        example_content_layout.setVisibility(View.GONE);
        example_data_upload = (LinearLayout)view.findViewById(R.id.example_data_upload);
        example_data_upload.setVisibility(View.GONE);
        text_upload_way = (LinearLayout)view.findViewById(R.id.text_upload_way);
        text_upload_way.setVisibility(View.GONE);
        labelling_upload = (LinearLayout)view.findViewById(R.id.labelling_upload);

        make_button = (Button) view.findViewById(R.id.make_button);
        text_example_data_button = (Button)view.findViewById(R.id.text_example_data_input);
        example_data_button = (Button) view.findViewById(R.id.example_data_selection);
        labelling_data_button = (Button) view.findViewById(R.id.labelling_data_selection);
        class_input = (Button) view.findViewById(R.id.class_input);

        collection_data_type = view.findViewById(R.id.type_collection);
        labelling_work_type = view.findViewById(R.id.type_labelling);
      
        worktype_text = view.findViewById(R.id.work_type_creation);

        if(button_result.equals("collection")){
            worktype_text.setText("수집");
            worktype = button_result;
            collection_data_type.setVisibility(View.VISIBLE);
            labelling_work_type.setVisibility(View.GONE);
            labelling_upload.setVisibility(View.GONE);

        }
        else if(button_result.equals("labelling")){
            worktype_text.setText("라벨링");
            worktype = button_result;
            labelling_work_type.setVisibility(View.VISIBLE);
            collection_data_type.setVisibility(View.GONE);
            example_data_upload.setVisibility(View.VISIBLE);
        }
        else if(button_result.equals("both")){
            worktype_text.setText("수집+라벨링");
            //worktype = button_result;
            labelling_work_type.setVisibility(View.GONE);
            collection_data_type.setVisibility(View.VISIBLE);
        }

        labelling_work = view.findViewById(R.id.radioGroup_labelling);
        boundingbox = view.findViewById(R.id.boundingbox);
        classification = view.findViewById(R.id.classification);
        labelling_work.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case(R.id.boundingbox):
                        datatype = boundingbox.getText().toString();
                        break;
                    case(R.id.classification):
                        datatype = classification.getText().toString();
                }
            }
        });

        collection_data = view.findViewById(R.id.radioGroup_data);
        image = view.findViewById(R.id.image_btn);
        audio = view.findViewById(R.id.audio_btn);
        text = view.findViewById(R.id.text_btn);
        collection_data.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case(R.id.image_btn):
                        datatype = image.getText().toString();
                        example_content_layout.setVisibility(View.GONE);
                        example_data_upload.setVisibility(View.VISIBLE);
                        text_upload_way.setVisibility(View.GONE);
                        break;
                    case(R.id.audio_btn):
                        datatype = audio.getText().toString();
                        example_content_layout.setVisibility(View.GONE);
                        example_data_upload.setVisibility(View.VISIBLE);
                        text_upload_way.setVisibility(View.GONE);
                        break;
                    case( R.id.text_btn):
                        datatype = text.getText().toString();
                        text_upload_way.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        text_example = view.findViewById(R.id.radioGroup_text_example);
        user_input_text = view.findViewById(R.id.user_input_text);
        text_file_upload = view.findViewById(R.id.text_file_upload);
        text_example.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case(R.id.user_input_text):
                        example_content_layout.setVisibility(View.VISIBLE);
                        example_data_upload.setVisibility(View.GONE);
                        break;
                    case(R.id.text_file_upload):
                        example_data_upload.setVisibility(View.VISIBLE);
                        example_content_layout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        project_name = view.findViewById(R.id.projectName);
        project_subject = view.findViewById(R.id.subject);
        description = view.findViewById(R.id.description);
        way_content = view.findViewById(R.id.waycontent);
        example_content = view.findViewById(R.id.examplecontent);
        condition_content = view.findViewById(R.id.condition_content);
        total_data = view.findViewById(R.id.total_data);

        make_button.setOnClickListener(this);
        text_example_data_button.setOnClickListener(this);
        example_data_button.setOnClickListener(this);
        labelling_data_button.setOnClickListener(this);
        class_input.setOnClickListener(this);

        exampleURI = (TextView) view.findViewById(R.id.examplePicturesURI);

        context = container.getContext();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.make_button:
                if(project_name.getText().toString().equals("") || project_subject.getText().toString().equals("")|| way_content.getText().toString().equals("")||condition_content.getText().toString().equals("")||description.getText().toString().equals("") ||total_data.getText().toString().equals("")){
                    Toast.makeText(context, "빈칸없이 입력하세요.",Toast.LENGTH_LONG).show();
                    return;
                }
                plusFragment = (PlusFragment)getParentFragment();
                plusFragment.replaceFragment(3);
                break;
            case R.id.text_example_data_input:
                make_project(view);
                createClass(view);
                try {
                    upload_user_text_example(view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.example_data_selection:
                make_project(view);
                createClass(view);
                if(datatype.equals("이미지")){
                    upload_image_example_data(view);
                }
                else if(datatype.equals("텍스트")){
                  upload_text_example_data(view);
                }
                else if(datatype.equals("음성")){
                    upload_audio_example_data(view);
                }
                break;
            case R.id.labelling_data_selection:
                upload_labelling_data(view);
                break;
            case R.id.class_input:
                input_class(view);
                break;
        }
    }

    public void make_project(final View view){
        final String projectName = project_name.getText().toString();
        final String projectSubject = project_subject.getText().toString();
        final String wayContent = way_content.getText().toString();
        final String conditionContent = condition_content.getText().toString();
        final String projectDescription = description.getText().toString();
        final String projectTotalData = total_data.getText().toString();

        AsyncTask<String, Void,Boolean> projectTask = new AsyncTask<String, Void, Boolean>() {
            protected Boolean doInBackground(String... userInfos) {
                Boolean result = RESTAPI.getInstance().makeProject(userInfos[0],
                        userInfos[1],userInfos[2],userInfos[3],userInfos[4],userInfos[5]
                        ,userInfos[6],userInfos[7]);
                return result;
            }
            @Override
            protected void onPostExecute(Boolean result) {
               if(!result){
                    Toast.makeText(getActivity(), "프로젝트 생성 실패", Toast.LENGTH_SHORT).show();
                }
            }
        };
        projectTask.execute(projectName, worktype, datatype, projectSubject, wayContent, conditionContent, projectDescription, projectTotalData);
    }

    public void createClass(final View view) {
        AsyncTask<Object, Void,Boolean> classTask = new AsyncTask<Object, Void, Boolean>() {
            protected Boolean doInBackground(Object... classInfos) {
                Boolean result = null;
                try {
                    result = RESTAPI.getInstance().createClass((List<String>) classInfos[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

        };
        for(int i=0;i<addclass.size();i++){
            classList.add(addclass.get(i).getText().toString());
        }
        classTask.execute(classList);

    }

    private void input_class(View view) { //프로젝트 생성할때 클래스 입력
        EditText input = new EditText(getActivity().getApplicationContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(param);
        input.setHint("class");
        input.setId(id_class);
        addclass.add(input);

        layout_plus.addView(input);
        id_class++;
    }

    public void upload_user_text_example(View view) throws Exception {
        final String exampleContent = example_content.getText().toString();
        String dirPath = getContext().getExternalFilesDir(null).getAbsolutePath() + "/text_ex_file";
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdir();
        }

        String FileName = "example.txt";
        FileOutputStream fileOutputStream = getContext().openFileOutput(FileName,Context.MODE_PRIVATE);
        fileOutputStream.write(exampleContent.getBytes());
        fileOutputStream.close();
        File file = new File("/data/data/com.ajou.capstone_design_freitag/files/example.txt");

        AsyncTask<File, Void, Boolean> uploadUserTextTask = new AsyncTask<File, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(File... files) {
                try {
                    InputStream inputStream = new FileInputStream(files[0]);
                    boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream,"example.txt", "text/plain");
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
                    Toast.makeText(context, "사용자 입력 예시 텍스트파일 업로드 실패",Toast.LENGTH_LONG).show();
                }
            }
        };
        uploadUserTextTask.execute(file);
    }

    public void upload_image_example_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_PICTURE_IMAGE_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void upload_audio_example_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("file/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, EXAMPLE_AUDIO_REQUEST_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void upload_text_example_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("file/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, EXAMPLE_TEXT_REQUEST_CODE);
    }

    public void upload_labelling_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_PICTURE_IMAGE_REQUEST_CODE);
    }

    public String getFileNameToUri(Uri data) { String[] proj = { MediaStore.Files.FileColumns.DISPLAY_NAME};
    Cursor cursor = context.getContentResolver().query(data, proj, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
    cursor.moveToFirst();
    String imgPath = cursor.getString(column_index);
    String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
    return imgName; }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EXAMPLE_AUDIO_REQUEST_CODE){
            Uri uri = data.getData();
            final String fileName = getFileNameToUri(data.getData());//이름
            exampleURI.setText(exampleURI.getText() + "\n" + uri);
            AsyncTask<Uri, Void, Boolean> uploadAudioTask = new AsyncTask<Uri, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Uri... uris) {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                        boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream,fileName,"audio/mp3");
                        return new Boolean(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new Boolean(false);
                    }
                }
            };
            uploadAudioTask.execute(uri);
        }
        else if(requestCode == EXAMPLE_TEXT_REQUEST_CODE){
            Uri uri = data.getData();
            final String fileName = getFileNameToUri(data.getData());//이름
            exampleURI.setText(exampleURI.getText() + "\n" + uri);
            AsyncTask<Uri, Void, Boolean> uploadTextTask = new AsyncTask<Uri, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Uri... uris) {
                    try {
                        InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                        boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream,fileName,"text/plain");
                        return new Boolean(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new Boolean(false);
                    }
                }
            };
            uploadTextTask.execute(uri);
        }
        else if(requestCode == EXAMPLE_PICTURE_IMAGE_REQUEST_CODE || requestCode == LABELING_PICTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    if (requestCode == EXAMPLE_PICTURE_IMAGE_REQUEST_CODE) {
                        examplePictures.add(clipData.getItemAt(i).getUri());
                        final String fileName = getFileNameToUri(data.getData());//이름
                        exampleURI.setText(exampleURI.getText() + "\n" + fileName);
                        //clipData.getItemAt(i).getUri()
                        try {
                            AsyncTask<Uri, Void, Boolean> uploadImageTask = new AsyncTask<Uri, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(Uri... uris) {
                                    try {
                                        InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                                        boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream,fileName,"image/jpeg");
                                        //String contentType = context.getContentResolver().getType(uris[0]);
                                        //boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream, contentType);
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
                    } else if (requestCode == LABELING_PICTURE_IMAGE_REQUEST_CODE) {
                        labelingPictures.add(clipData.getItemAt(i).getUri());
                    }
                }
            }
        } else if(requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(context, "로그인이 필요합니다.",Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).goToHome();
            }
        }
    }
}
