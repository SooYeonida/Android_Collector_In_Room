package com.ajou.capstone_design_freitag.ui.plus;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

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

public class ProjectMakeFragment extends Fragment {

    // Intent용 REQUEST CODE 정의
    private static final int EXAMPLE_IMAGE_REQUEST_CODE = 100;
    private static final int LABELING_IMAGE_REQUEST_CODE = 101;
    private static final int LOGIN_REQUEST_CODE = 102;
    private static final int EXAMPLE_AUDIO_REQUEST_CODE =103;
    private static final int EXAMPLE_TEXT_REQUEST_CODE = 104;

    // 업로드용 데이터 URI 임시 저장
    private Uri exampleDataUri;
    private ArrayList<Uri> labelingDataUris = new ArrayList<>();;

    // View들
    private TextView workTypeTextView;

    private LinearLayout collectionTypeLinearLayout;
    private RadioGroup collectionTypeRadioGroup;
    private RadioButton collectionImageRadioButton;
    private RadioButton collectionAudioRadioButton;
    private RadioButton collectionTextRadioButton;

    private LinearLayout labelingTypeLinearLayout;
    private RadioGroup labelingTypeRadioGroup;
    private RadioButton labelingBoundingboxRadioButton;
    private RadioButton labelingClassificationRadioButton;

    private LinearLayout textUploadWayLinearLayout;
    private RadioGroup textUploadWayRadioGroup;
    private RadioButton userInputExampleTextRadioButton;
    private RadioButton uploadTextRadioButton;

    private LinearLayout uploadExampleDataLinearLayout;
    private Button uploadExampleDataButton;

    private LinearLayout userInputExampleTextLinearLayout;
    private EditText userInputExampleTextEditText;

    private LinearLayout classLinearLayout;
    private Button createNewClassButton;
    private int classNum = 0;
    private List<EditText> classListEditText = new ArrayList<EditText>();

    private LinearLayout uploadLabelingDataLinearLayout;
    private Button uploadLabelingDataButton;

    private EditText projectName;
    private EditText totalDataEditText;
    private EditText subjectEditText;
    private EditText descriptionEditText;
    private EditText wayEditText;
    private EditText conditionEditText;

    private Button makeButton;

    private String worktype;

    List<String> classList = new ArrayList<>();

    private Context context;

    public static ProjectMakeFragment newInstance(String worktype) {
        ProjectMakeFragment fragment = new ProjectMakeFragment();
        Bundle args = new Bundle();
        args.putString("worktype", worktype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //토큰 받아오는데 null이면 로그인
        if(RESTAPI.getInstance().getToken()==null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);
        }
        if (getArguments() != null) {
            worktype = getArguments().getString("worktype");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_make, container, false);

        findViews(view);
        setAttributeByWorkType();

        context = container.getContext();

        return view;
    }

    private void findViews(View view) {
        workTypeTextView = view.findViewById(R.id.workTypeTextView);

        collectionTypeLinearLayout = view.findViewById(R.id.collectionTypeLinearLayout);
        collectionTypeRadioGroup = view.findViewById(R.id.collectionTypeRadioGroup);
        collectionImageRadioButton = view.findViewById(R.id.collectionImageRadioButton);
        collectionAudioRadioButton = view.findViewById(R.id.collectionAudioRadioButton);
        collectionTextRadioButton = view.findViewById(R.id.collectionTextRadioButton);
        collectionTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                exampleDataUri = null;
                switch (checkedId){
                    case(R.id.collectionImageRadioButton):
                    case(R.id.collectionAudioRadioButton):
                        textUploadWayLinearLayout.setVisibility(View.GONE);
                        uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                        userInputExampleTextLinearLayout.setVisibility(View.GONE);
                        break;
                    case( R.id.collectionTextRadioButton):
                        textUploadWayRadioGroup.clearCheck();
                        textUploadWayLinearLayout.setVisibility(View.VISIBLE);
                        uploadExampleDataLinearLayout.setVisibility(View.GONE);
                        userInputExampleTextLinearLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        labelingTypeLinearLayout = view.findViewById(R.id.labelingTypeLinearLayout);
        labelingTypeRadioGroup = view.findViewById(R.id.labelingTypeRadioGroup);
        labelingBoundingboxRadioButton = view.findViewById(R.id.labelingBoundingboxRadioButton);
        labelingClassificationRadioButton = view.findViewById(R.id.labelingClassificationRadioButton);

        textUploadWayLinearLayout = view.findViewById(R.id.textUploadWayLinearLayout);
        textUploadWayRadioGroup = view.findViewById(R.id.textUploadWayRadioGroup);
        userInputExampleTextRadioButton = view.findViewById(R.id.userInputExampleTextRadioButton);
        uploadTextRadioButton = view.findViewById(R.id.uploadTextRadioButton);
        textUploadWayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case(R.id.userInputExampleTextRadioButton):
                        userInputExampleTextLinearLayout.setVisibility(View.VISIBLE);
                        uploadExampleDataLinearLayout.setVisibility(View.GONE);
                        break;
                    case(R.id.uploadTextRadioButton):
                        uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                        userInputExampleTextLinearLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        uploadExampleDataLinearLayout = view.findViewById(R.id.uploadExampleDataLinearLayout);
        uploadExampleDataButton = view.findViewById(R.id.uploadExampleDataButton);
        uploadExampleDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectExampleData(view);
            }
        });

        userInputExampleTextLinearLayout = view.findViewById(R.id.userInputExampleTextLinearLayout);
        userInputExampleTextEditText = view.findViewById(R.id.userInputExampleTextEditText);

        classLinearLayout = view.findViewById(R.id.classLinearLayout);
        createNewClassButton = view.findViewById(R.id.createNewClassButton);
        createNewClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewClass(view);
            }
        });

        uploadLabelingDataLinearLayout = view.findViewById(R.id.uploadLabelingDataLinearLayout);
        uploadLabelingDataButton = view.findViewById(R.id.uploadLabelingDataButton);
        uploadLabelingDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLabellingImages(view);
            }
        });

        projectName = view.findViewById(R.id.projectName);
        totalDataEditText = view.findViewById(R.id.totalDataEditText);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        wayEditText = view.findViewById(R.id.wayEditText);
        conditionEditText = view.findViewById(R.id.conditionEditText);

        makeButton = view.findViewById(R.id.makeButton);
        makeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeProject(view);
            }
        });
    }

    private void setAttributeByWorkType() {
        switch (worktype) {
            case "collection":
                workTypeTextView.setText("수집");
                collectionTypeLinearLayout.setVisibility(View.VISIBLE);
                labelingTypeLinearLayout.setVisibility(View.GONE);
                uploadExampleDataLinearLayout.setVisibility(View.GONE);
                uploadLabelingDataLinearLayout.setVisibility(View.GONE);
                break;
            case "labelling":
                workTypeTextView.setText("라벨링");
                collectionTypeLinearLayout.setVisibility(View.GONE);
                labelingTypeLinearLayout.setVisibility(View.VISIBLE);
                uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                uploadLabelingDataLinearLayout.setVisibility(View.VISIBLE);
                break;
            case "both":
                workTypeTextView.setText("수집+라벨링");
                collectionTypeLinearLayout.setVisibility(View.VISIBLE);
                collectionImageRadioButton.setChecked(true);
                collectionImageRadioButton.setEnabled(false);
                collectionTextRadioButton.setEnabled(false);
                collectionAudioRadioButton.setEnabled(false);
                labelingTypeLinearLayout.setVisibility(View.VISIBLE);
                uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                uploadLabelingDataLinearLayout.setVisibility(View.GONE);
                break;
        }
        textUploadWayLinearLayout.setVisibility(View.GONE);
        userInputExampleTextLinearLayout.setVisibility(View.GONE);
    }

    public void makeProject(View view){
        final String projectName = this.projectName.getText().toString();
        final String projectSubject = subjectEditText.getText().toString();
        final String wayContent = wayEditText.getText().toString();
        final String conditionContent = conditionEditText.getText().toString();
        final String projectDescription = descriptionEditText.getText().toString();
        final String projectTotalData = totalDataEditText.getText().toString();

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
        projectTask.execute(projectName, worktype, projectSubject, wayContent, conditionContent, projectDescription, projectTotalData);
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
        for(int i = 0; i< classListEditText.size(); i++){
            classList.add(classListEditText.get(i).getText().toString());
        }
        classTask.execute(classList);
    }

    public void createNewClass(View view) { //프로젝트 생성할때 클래스 입력
        EditText input = new EditText(getActivity().getApplicationContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(param);
        input.setHint("class");
        input.setId(classNum);
        classListEditText.add(input);

        classLinearLayout.addView(input);
        classNum++;
    }

    public void upload_user_text_example(View view) throws Exception {
        final String exampleContent = userInputExampleTextEditText.getText().toString();
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

    public void selectExampleData(View view) {
        if(worktype.equals("collection")) {
            switch (collectionTypeRadioGroup.getCheckedRadioButtonId()) {
                case R.id.collectionImageRadioButton:
                    selectExampleImage();
                    break;
                case R.id.collectionTextRadioButton:
                    selectExampleText();
                    break;
                case R.id.collectionAudioRadioButton:
                    selectExampleAudio();
                    break;
            }
        } else {
            selectExampleImage();
        }
    }

    private void selectExampleImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_IMAGE_REQUEST_CODE);
    }

    private void selectExampleAudio() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Audio.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_AUDIO_REQUEST_CODE);
    }

    private void selectExampleText() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("file/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setData(MediaStore.Downloads.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_TEXT_REQUEST_CODE);
    }

    public void selectLabellingImages(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_IMAGE_REQUEST_CODE);
    }

    public String getFileNameFromUri(Uri uri) {
        String[] proj = { MediaStore.Files.FileColumns.DISPLAY_NAME };
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        String name = path.substring(path.lastIndexOf("/")+1);
        cursor.close();
        return name;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == EXAMPLE_AUDIO_REQUEST_CODE || requestCode == EXAMPLE_TEXT_REQUEST_CODE || requestCode == EXAMPLE_IMAGE_REQUEST_CODE){
                exampleDataUri = data.getData();
                System.out.println(exampleDataUri);
            }
            if(requestCode == LABELING_IMAGE_REQUEST_CODE) {
                ClipData clipDatas = data.getClipData();
                for (int i = 0; i < clipDatas.getItemCount(); i++) {
                    labelingDataUris.add(clipDatas.getItemAt(i).getUri());
                }
            }
        } else {
            if(requestCode == LOGIN_REQUEST_CODE) {
                Toast.makeText(context, "로그인이 필요합니다.",Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).goToHome();
            }
        }
    }
}