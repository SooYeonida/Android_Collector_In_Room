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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class PlusFragment extends Fragment implements View.OnClickListener{
    private static final int EXAMPLE_PICTURE_IMAGE_REQUEST_CODE = 100;
    private static final int LABELING_PICTURE_IMAGE_REQUEST_CODE = 101;
    private static final int LOGIN_REQUEST_CODE = 102;

    private ArrayList<Uri> examplePictures;
    private ArrayList<Uri> labelingPictures;
    //라벨링일 경우 사용자가 클래스 입력?

    Button make_button;
    Button example_data_button;
    Button labelling_data_button;

    TextView examplePicturesURI;
    private Context context;

    private RadioGroup work;
    private RadioGroup data;
    private RadioButton collection;
    private RadioButton labelling;
    private RadioButton image;
    private RadioButton audio;
    private RadioButton text;
    private EditText project_name;
    private EditText project_subject;
    private EditText description;
    private EditText way_content;
    private EditText condition_content;

    String worktype = null;
    String datatype = null;
    Project project = new Project();

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

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){
        examplePictures = new ArrayList<>();
        labelingPictures = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_plus,container,false);
        make_button = (Button) view.findViewById(R.id.make_button);
        example_data_button = (Button) view.findViewById(R.id.example_data_selection);
        labelling_data_button = (Button) view.findViewById(R.id.labelling_data_selection);

        work = view.findViewById(R.id.radioGroup_work);
        collection = view.findViewById(R.id.data_collection);
        labelling = view.findViewById(R.id.labelling);
        work.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               if(checkedId == R.id.data_collection)
                        worktype = collection.getText().toString();
                if(checkedId == R.id.labelling)
                        worktype = labelling.getText().toString();
            }
        });

        data = view.findViewById(R.id.radioGroup_data);
        image = view.findViewById(R.id.image_btn);
        audio = view.findViewById(R.id.audio_btn);
        text = view.findViewById(R.id.text_btn);
        data.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.image_btn)
                        datatype = image.getText().toString();
                if(checkedId == R.id.audio_btn)
                        datatype = audio.getText().toString();
                if(checkedId == R.id.text_btn)
                        datatype = text.getText().toString();
                }
        });

        project_name = view.findViewById(R.id.projectName);
        project_subject = view.findViewById(R.id.subject);
        description = view.findViewById(R.id.description);
        way_content = view.findViewById(R.id.waycontent);
        condition_content = view.findViewById(R.id.condition_content);

        make_button.setOnClickListener(this);
        example_data_button.setOnClickListener(this);
        labelling_data_button.setOnClickListener(this);

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
        }
    }

    public void make_project(final View view){
    }

    public void upload_example_data(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
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
                                        boolean result = RESTAPI.getInstance().uploadExampleFile(inputStream);
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
