package com.ajou.capstone_design_freitag.ui.plus;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;

import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ProjectMakeFragment extends Fragment implements View.OnClickListener {

    private static final int EXAMPLE_PICTURE_IMAGE_REQUEST_CODE = 100;
    private static final int LABELING_PICTURE_IMAGE_REQUEST_CODE = 101;
    private static final int LOGIN_REQUEST_CODE = 102;

    private ArrayList<Uri> examplePictures;
    private ArrayList<Uri> labelingPictures;

    LinearLayout layout_plus;
    Button make_button;
    Button example_data_button;
    Button labelling_data_button;
    Button class_input;

    TextView examplePicturesURI;
    private Context context;

    PlusFragment plusFragment;

    int id_class = 0;
    private RadioGroup work;
    private RadioGroup labelling_work;
    private RadioGroup collection_data;
    private RadioButton collection;
    private RadioButton labelling;
    private RadioButton image;
    private RadioButton audio;
    private RadioButton text;
    private RadioButton boundingbox;
    private RadioButton classification;

    private EditText project_name;
    private EditText project_subject;
    private EditText description;
    private EditText way_content;
    private EditText condition_content;
    private EditText total_data;

    LinearLayout collection_data_type;
    LinearLayout labelling_work_type;

    String worktype = null;
    String datatype = null;

    public ProjectMakeFragment() {
        // Required empty public constructor
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        examplePictures = new ArrayList<>();
        labelingPictures = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_project_make, container, false);
        layout_plus = (LinearLayout)view.findViewById(R.id.linearlayout_in_plus);

        make_button = (Button) view.findViewById(R.id.make_button);
        example_data_button = (Button) view.findViewById(R.id.example_data_selection);
        labelling_data_button = (Button) view.findViewById(R.id.labelling_data_selection);
        class_input = (Button) view.findViewById(R.id.class_input);

        collection_data_type = view.findViewById(R.id.type_collection);
        labelling_work_type = view.findViewById(R.id.type_labelling);

        work = view.findViewById(R.id.radioGroup_work);
        collection = view.findViewById(R.id.data_collection);
        labelling = view.findViewById(R.id.labelling);
        work.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case(R.id.data_collection):
                        worktype = collection.getText().toString();
                        collection_data_type.setVisibility(View.VISIBLE);
                        labelling_work_type.setVisibility(View.GONE);
                        break;
                    case(R.id.labelling):
                        worktype = labelling.getText().toString();
                        labelling_work_type.setVisibility(View.VISIBLE);
                        collection_data_type.setVisibility(View.GONE);
                        break;
                }
            }
        });

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
                        break;
                    case(R.id.audio_btn):
                        datatype = audio.getText().toString();
                        break;
                    case( R.id.text_btn):
                        datatype = text.getText().toString();
                        break;
                }
            }
        });

        project_name = view.findViewById(R.id.projectName);
        project_subject = view.findViewById(R.id.subject);
        description = view.findViewById(R.id.description);
        way_content = view.findViewById(R.id.waycontent);
        condition_content = view.findViewById(R.id.condition_content);
        total_data = view.findViewById(R.id.total_data);

        make_button.setOnClickListener(this);
        example_data_button.setOnClickListener(this);
        labelling_data_button.setOnClickListener(this);
        class_input.setOnClickListener(this);

        examplePicturesURI = (TextView) view.findViewById(R.id.examplePicturesURI);

        context = container.getContext();

        return view;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.make_button:
                make_project(view);
                break;
            case R.id.example_data_selection:
                upload_example_data(view);
                break;
            case R.id.labelling_data_selection:
                upload_labelling_data(view);
                break;
            case R.id.class_input:
                input_class(view);
                break;
        }
    }

    private void input_class(View view) { //프로젝트 생성할때 클래스 입력
        EditText input = new EditText(getActivity().getApplicationContext());
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(param);
        input.setHint("class");
        input.setId(id_class);
        id_class++;
        layout_plus.addView(input);
    }

    public void make_project(final View view){
        final String projectName = project_name.getText().toString();
        final String projectSubject = project_subject.getText().toString();
        final String wayContent = way_content.getText().toString();
        final String conditionContent = condition_content.getText().toString();
        final String projectDescription = description.getText().toString();
        final String projectTotalData = total_data.getText().toString();

        if(projectName.equals("") || projectSubject.equals("")|| wayContent.equals("")||conditionContent.equals("")||projectDescription.equals("") ||projectTotalData.equals("")){
            Toast.makeText(context, "빈칸없이 입력하세요.",Toast.LENGTH_LONG).show();
            return;
        }

        AsyncTask<String, Void,Boolean > projectTask = new AsyncTask<String, Void, Boolean>() {
            protected Boolean doInBackground(String... userInfos) {
                boolean result = RESTAPI.getInstance().makeProject(userInfos[0],
                        userInfos[1],userInfos[2],userInfos[3],userInfos[4],userInfos[5]
                        ,userInfos[6],userInfos[7]);
                return result;
            }
            @Override
            protected void onPostExecute(Boolean result) {
                if(result){
                    plusFragment = (PlusFragment)getParentFragment();
                    plusFragment.replaceFragment(1);
                }
                else{
                    Toast.makeText(getActivity(), "프로젝트 생성 실패", Toast.LENGTH_SHORT).show();
                }
            }

        };

            projectTask.execute(projectName, worktype, datatype, projectSubject, wayContent, conditionContent, projectDescription, projectTotalData);
    }

    public void upload_example_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_PICTURE_IMAGE_REQUEST_CODE);
    }

    public void upload_labelling_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, EXAMPLE_PICTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EXAMPLE_PICTURE_IMAGE_REQUEST_CODE || requestCode == LABELING_PICTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    if (requestCode == EXAMPLE_PICTURE_IMAGE_REQUEST_CODE) {
                        examplePictures.add(clipData.getItemAt(i).getUri());
                        examplePicturesURI.setText(examplePicturesURI.getText() + "\n" + clipData.getItemAt(i).getUri());
                        try {
                            AsyncTask<Uri, Void, Boolean> loginTask = new AsyncTask<Uri, Void, Boolean>() {
                                @Override
                                protected Boolean doInBackground(Uri... uris) {
                                    try {
                                        InputStream inputStream = context.getContentResolver().openInputStream(uris[0]);
                                        String contentType = context.getContentResolver().getType(uris[0]);
                                        boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream, contentType);
                                        return new Boolean(result);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return new Boolean(false);
                                    }
                                }
                            };
                            loginTask.execute(clipData.getItemAt(i).getUri());
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
