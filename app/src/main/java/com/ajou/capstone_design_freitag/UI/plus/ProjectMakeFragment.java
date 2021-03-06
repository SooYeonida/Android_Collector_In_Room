package com.ajou.capstone_design_freitag.UI.plus;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProjectMakeFragment extends Fragment {

    // Intent용 REQUEST CODE 정의
    private static final int EXAMPLE_IMAGE_REQUEST_CODE = 100;
    private static final int LABELLING_IMAGE_REQUEST_CODE = 101;
    private static final int LOGIN_REQUEST_CODE = 102;
    private static final int EXAMPLE_AUDIO_REQUEST_CODE =103;
    private static final int EXAMPLE_TEXT_REQUEST_CODE = 104;

    // 업로드용 데이터 URI 임시 저장
    private Uri exampleDataUri;
    private ArrayList<Uri> labellingDataUris = new ArrayList<>();;

    // View들
    private TextView workTypeTextView;

    private LinearLayout collectionTypeLinearLayout;
    private RadioGroup collectionTypeRadioGroup;
    private RadioButton collectionImageRadioButton;
    private RadioButton collectionAudioRadioButton;
    private RadioButton collectionTextRadioButton;

    private LinearLayout labellingTypeLinearLayout;
    private RadioGroup labellingTypeRadioGroup;
    private RadioButton labellingBoundingboxRadioButton;
    private RadioButton labellingClassificationRadioButton;

    private LinearLayout textUploadWayLinearLayout;
    private RadioGroup textUploadWayRadioGroup;
    private RadioButton userInputExampleTextRadioButton;
    private RadioButton uploadTextRadioButton;

    private LinearLayout uploadExampleDataLinearLayout;
    private Button uploadExampleDataButton;

    private LinearLayout userInputExampleTextLinearLayout;

    private LinearLayout classLinearLayout;
    private Button createNewClassButton;
    private int classNum = 0;
    private List<EditText> classListEditText = new ArrayList<EditText>();

    private LinearLayout uploadLabellingDataLinearLayout;
    private Button uploadLabellingDataButton;

    private EditText projectName;
    private EditText totalDataEditText;
    private EditText subjectEditText;
    private EditText descriptionEditText;
    private EditText wayEditText;
    private EditText conditionEditText;

    private ImageView example_image;
    private LinearLayout example_audio_layout;

    private ImageView play;
    private ImageView pause;
    private SeekBar seekBar;
    MediaPlayer mediaPlayer;
    boolean isPlaying = true;
    ProgressUpdate progressUpdate;

    private Button makeButton;

    private String worktype;
    private String datatype;

    List<String> classList = new ArrayList<>();

    private Context context;

    private LinearLayout setLayout;
    private Button add;
    int editTextNum=0;
    private List<EditText> questionList = new ArrayList<>();
    private List<EditText> answerList = new ArrayList<>();

    static AppCompatDialog progressDialog;

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

        example_image = view.findViewById(R.id.make_example_image);
        example_audio_layout = view.findViewById(R.id.make_example_audio);
        example_audio_layout.setVisibility(View.GONE);
        play = view.findViewById(R.id.make_play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.VISIBLE);
                play.setVisibility(View.GONE);
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                mediaPlayer.start();
            }
        });
        pause = view.findViewById(R.id.make_pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.GONE);
                play.setVisibility(View.VISIBLE);
                mediaPlayer.pause();
            }
        });
        seekBar = view.findViewById(R.id.make_seekbar);
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

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                readyToPlay();
            }
        });

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
                    case R.id.collectionImageRadioButton:
                        datatype = "image";
                        example_image.setVisibility(View.GONE);
                        example_audio_layout.setVisibility(View.GONE);
                        textUploadWayLinearLayout.setVisibility(View.GONE);
                        uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                        userInputExampleTextLinearLayout.setVisibility(View.GONE);
                        break;
                    case R.id.collectionTextRadioButton:
                        datatype = "text";
                        example_image.setVisibility(View.GONE);
                        example_audio_layout.setVisibility(View.GONE);
                        textUploadWayRadioGroup.clearCheck();
                        textUploadWayLinearLayout.setVisibility(View.VISIBLE);
                        uploadExampleDataLinearLayout.setVisibility(View.GONE);
                        userInputExampleTextLinearLayout.setVisibility(View.GONE);
                        break;
                    case R.id.collectionAudioRadioButton:
                        datatype = "audio";
                        example_image.setVisibility(View.GONE);
                        example_audio_layout.setVisibility(View.GONE);
                        textUploadWayLinearLayout.setVisibility(View.GONE);
                        uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                        userInputExampleTextLinearLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        labellingTypeLinearLayout = view.findViewById(R.id.labelingTypeLinearLayout);
        labellingTypeRadioGroup = view.findViewById(R.id.labelingTypeRadioGroup);
        labellingClassificationRadioButton = view.findViewById(R.id.labelingClassificationRadioButton);
        labellingBoundingboxRadioButton = view.findViewById(R.id.labelingBoundingboxRadioButton);
        labellingTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.labelingClassificationRadioButton:
                        datatype = "classification";
                        break;
                    case R.id.labelingBoundingboxRadioButton:
                        datatype = "boundingBox";
                        break;
                }
            }
        });

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

        classLinearLayout = view.findViewById(R.id.classLinearLayout);
        createNewClassButton = view.findViewById(R.id.createNewClassButton);
        createNewClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewClass(view);
            }
        });

        uploadLabellingDataLinearLayout = view.findViewById(R.id.uploadLabelingDataLinearLayout);
        uploadLabellingDataButton = view.findViewById(R.id.uploadLabelingDataButton);
        uploadLabellingDataButton.setOnClickListener(new View.OnClickListener() {
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

        setLayout = view.findViewById(R.id.question_and_answer_make);
        add = view.findViewById(R.id.make_example_text_user);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView question = new TextView(context);
                question.setLayoutParams(params);
                question.setText("질문: ");
                setLayout.addView(question);
                editTextNum++;

                EditText questionEditText = new EditText(context);
                questionEditText.setLayoutParams(params);
                questionEditText.setId(editTextNum);
                setLayout.addView(questionEditText);
                questionList.add(questionEditText);
                editTextNum++;

                TextView answer = new TextView(context);
                answer.setLayoutParams(params);
                answer.setText("답: ");
                setLayout.addView(answer);
                editTextNum++;

                EditText answerEditText = new EditText(context);
                answerEditText.setLayoutParams(params);
                answerEditText.setId(editTextNum);
                setLayout.addView(answerEditText);
                answerList.add(answerEditText);
                editTextNum++;
            }
        });

    }

    private void setAttributeByWorkType() {
        switch (worktype) {
            case "collection":
                workTypeTextView.setText("수집");
                collectionTypeLinearLayout.setVisibility(View.VISIBLE);
                labellingTypeLinearLayout.setVisibility(View.GONE);
                uploadExampleDataLinearLayout.setVisibility(View.GONE);
                uploadLabellingDataLinearLayout.setVisibility(View.GONE);
                break;
            case "labelling":
                workTypeTextView.setText("라벨링");
                collectionTypeLinearLayout.setVisibility(View.GONE);
                labellingTypeLinearLayout.setVisibility(View.VISIBLE);
                uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                uploadLabellingDataLinearLayout.setVisibility(View.VISIBLE);
                break;
            case "both":
                workTypeTextView.setText("수집+라벨링");
                collectionTypeLinearLayout.setVisibility(View.VISIBLE);
                collectionImageRadioButton.setChecked(true);
                collectionImageRadioButton.setEnabled(false);
                collectionTextRadioButton.setEnabled(false);
                collectionAudioRadioButton.setEnabled(false);
                labellingTypeLinearLayout.setVisibility(View.VISIBLE);
                uploadExampleDataLinearLayout.setVisibility(View.VISIBLE);
                uploadLabellingDataLinearLayout.setVisibility(View.GONE);
                break;
        }
        textUploadWayLinearLayout.setVisibility(View.GONE);
        userInputExampleTextLinearLayout.setVisibility(View.GONE);
    }

    private void makeProject(View view){
        MakeProjectTask makeProjectTask = new MakeProjectTask(this);
        makeProjectTask.execute();
        progressON(getActivity(),"Loading..");
    }

    private void createNewClass(View view) { //프로젝트 생성할때 클래스 입력
        EditText input = new EditText(getActivity().getApplicationContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(param);
        input.setHint("class");
        classListEditText.add(input);
        classLinearLayout.addView(input);
        classNum++;
    }

    private void selectExampleData(View view) {
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, EXAMPLE_IMAGE_REQUEST_CODE);
        example_image.setVisibility(View.VISIBLE);
    }

    private void selectExampleAudio() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setDataAndType(MediaStore.Downloads.EXTERNAL_CONTENT_URI, "audio/*");
        startActivityForResult(intent, EXAMPLE_AUDIO_REQUEST_CODE);
    }

    private void selectExampleText() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setDataAndType(MediaStore.Downloads.EXTERNAL_CONTENT_URI, "text/*");
        startActivityForResult(intent, EXAMPLE_TEXT_REQUEST_CODE);
    }

    private void selectLabellingImages(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, LABELLING_IMAGE_REQUEST_CODE);
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

    public void readyToPlay() {
        try {
            seekBar.setProgress(0);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, exampleDataUri);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == EXAMPLE_AUDIO_REQUEST_CODE || requestCode == EXAMPLE_TEXT_REQUEST_CODE || requestCode == EXAMPLE_IMAGE_REQUEST_CODE){
                exampleDataUri = data.getData();
                if (requestCode == EXAMPLE_IMAGE_REQUEST_CODE){
                    InputStream inputStream = null;
                    try {
                        inputStream = context.getContentResolver().openInputStream(exampleDataUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    example_image.setImageBitmap(bitmap);
                }
                if (requestCode == EXAMPLE_AUDIO_REQUEST_CODE){
                    example_audio_layout.setVisibility(View.VISIBLE);
                    readyToPlay();
                    progressUpdate = new ProgressUpdate();
                    progressUpdate.start();
                }
            } else if(requestCode == LABELLING_IMAGE_REQUEST_CODE) {
                ClipData clipDatas = data.getClipData();
                for (int i = 0; i < clipDatas.getItemCount(); i++) {
                    labellingDataUris.add(clipDatas.getItemAt(i).getUri());
                }
            } else if(requestCode == LOGIN_REQUEST_CODE) {
                ((MainActivity)getActivity()).loginSuccess();
            }
        } else {
            if(requestCode == LOGIN_REQUEST_CODE) {
                Toast.makeText(context, "로그인이 필요합니다.",Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).goToHome();
            }
        }
    }

    private Uri getExampleDataUri() throws Exception {
        if(collectionTextRadioButton.isChecked()) {
//            String exampleText = userInputExampleTextEditText.getText().toString();
//            String dirPath = getContext().getCacheDir().getAbsolutePath() + "/freitag";
//            File dir = new File(dirPath);
//            if(!dir.exists()) {
//                dir.mkdir();
//            }
//            String fileName = dirPath + "/example.txt";
//            FileOutputStream fileOutputStream = new FileOutputStream(new File(fileName));
//            fileOutputStream.write(exampleText.getBytes());
//            fileOutputStream.close();

            JSONObject userInput = new JSONObject();
            JSONArray setArray = new JSONArray();

            for(int i=0;i<questionList.size();i++) {
                JSONObject setObject = new JSONObject();
                setObject.put("question",questionList.get(i).getText().toString());
                setObject.put("answer",answerList.get(i).getText().toString());
                setArray.add(setObject);
            }
            userInput.put("set",setArray);
            String dirPath = context.getCacheDir().getAbsolutePath() + "/freitag";
            File dir = new File(dirPath);
            if(!dir.exists()) {
                dir.mkdir();
            }
            String fileName = "example.txt";
            String filePath= dirPath + "/"+ fileName;

            FileWriter file = new FileWriter(filePath);
            file.write(userInput.toJSONString());
            file.flush();
            file.close();

            return Uri.fromFile(new File(filePath));
        }
        return exampleDataUri;
    }

    private static class MakeProjectTask extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<ProjectMakeFragment> fragmentReference;

        private String projectName;
        private String worktype;
        private String datatype;
        private String totalData;
        private String subject;
        private String description;
        private String way;
        private String condition;
        private ArrayList<String> classList;
        private Uri exampleDataUri;
        private ArrayList<Uri> labellingDataUris;

        MakeProjectTask(ProjectMakeFragment context) {
            fragmentReference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            getParameters();
            System.out.println("getParameters pass");
            if(validation()) {
                System.out.println("validation pass");
                if(sendProjectInfo()) {
                    System.out.println("create project pass");
                    if(sendClassInfo()) {
                        System.out.println("create class pass");
                        if(sendExampleData()) {
                            System.out.println("upload example data pass");
                            progressOFF();
                            if(worktype.equals("labelling")) {
                                if(sendLabellingData()) {
                                    System.out.println("upload labelling datas pass");
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
            progressOFF();
            return false;
        }

        private boolean sendLabellingData() {
            ProjectMakeFragment fragment = getFragment();
            if(fragment == null) {
                return false;
            }
            Context context = fragment.getContext();

            try {
                int labellingDataNum = labellingDataUris.size();
                InputStream[] inputStreams = new InputStream[labellingDataNum];
                String[] contentTypes = new String[labellingDataNum];
                String[] fileNames = new String[labellingDataNum];
                for(int i = 0; i < labellingDataNum; i++) {
                    inputStreams[i] = context.getContentResolver().openInputStream(labellingDataUris.get(i));
                    fileNames[i] = getFileNameFromUri(labellingDataUris.get(i));
                    contentTypes[i] = context.getContentResolver().getType(labellingDataUris.get(i));
                }
                return RESTAPI.getInstance().uploadLabellingFiles(inputStreams, fileNames, contentTypes);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private String getFileNameFromUri(Uri uri) {
            ProjectMakeFragment fragment = getFragment();
            if(fragment == null)
                return null;

            String result = null;

            if (uri.getScheme().equals("content")) {
                String[] proj = { MediaStore.Files.FileColumns.DISPLAY_NAME };
                try (Cursor cursor = fragment.getContext().getContentResolver().query(uri, proj, null, null, null)) {
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

        private boolean sendExampleData() {
            ProjectMakeFragment fragment = getFragment();
            if(fragment == null) {
                return false;
            }
            Context context = fragment.getContext();

            try {
                InputStream inputStream = context.getContentResolver().openInputStream(exampleDataUri);
                String fileName = getFileNameFromUri(exampleDataUri);
                String contentType = context.getContentResolver().getType(exampleDataUri);
                return RESTAPI.getInstance().uploadExampleFile(inputStream, fileName, contentType, worktype);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean sendClassInfo() {
            return RESTAPI.getInstance().createClass(classList);
        }

        private boolean sendProjectInfo() {
            return RESTAPI.getInstance().makeProject(projectName, worktype, datatype, subject, way, condition, description, totalData);
        }

        private boolean validation() {
            if(projectName.isEmpty() || datatype.isEmpty() || totalData.isEmpty() || subject.isEmpty() || description.isEmpty() || way.isEmpty() || condition.isEmpty()) {
                System.out.println("not enough project infomation");
                return false;
            }
            if(classList.size() == 0) {
                System.out.println("not entered classes");
                return false;
            }
            if(exampleDataUri == null) {
                System.out.println("not selected example");
                return false;
            }
            if(worktype.equals("labelling")) {
                if(Integer.parseInt(totalData) != labellingDataUris.size()) {
                    System.out.println("not match labelling number");
                    System.out.println("totalData : " + Integer.parseInt(totalData));
                    System.out.println("labellingDataUris.size() : " + labellingDataUris.size());
                    return false;
                }
            }

            return true;
        }

        private void getParameters() {
            ProjectMakeFragment fragment = getFragment();
            if (fragment == null) {
                return;
            }

            projectName = fragment.projectName.getText().toString();
            worktype = fragment.worktype;
            datatype = fragment.datatype;
            totalData = fragment.totalDataEditText.getText().toString();
            subject = fragment.subjectEditText.getText().toString();
            description = fragment.descriptionEditText.getText().toString();
            way = fragment.wayEditText.getText().toString();
            condition = fragment.conditionEditText.getText().toString();
            classList = new ArrayList<>();
            for(int i = 0; i < fragment.classListEditText.size(); i++) {
                classList.add(fragment.classListEditText.get(i).getText().toString());
            }
            try {
                exampleDataUri = fragment.getExampleDataUri();
            } catch (Exception e) {
                e.printStackTrace();
                exampleDataUri = null;
            }
            labellingDataUris = fragment.labellingDataUris;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            ProjectMakeFragment fragment = getFragment();
            if (fragment == null) {
                return;
            }

            if(result) {
                ((PlusFragment)fragment.getParentFragment()).replaceFragment(3);
            } else {
                Toast.makeText(fragment.context, "프로젝트 생성이 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        }

        private ProjectMakeFragment getFragment() {
            ProjectMakeFragment fragment = fragmentReference.get();
            if (fragment == null || fragment.isRemoving()) {
                return null;
            }
            return fragment;
        }
    }

    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.layout_dialog);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }


    }

    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public static void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}