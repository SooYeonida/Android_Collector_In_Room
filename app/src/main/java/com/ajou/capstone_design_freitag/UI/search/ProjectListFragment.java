package com.ajou.capstone_design_freitag.UI.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.Work.ProjectDetailActivity;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProjectListFragment extends Fragment {
    private static final int LOGIN_REQUEST_CODE = 102;

    ArrayList<Project> projectArrayList = new ArrayList<Project>();
    ProjectAdapter projectAdapter;
    ListView listView;

    String button_result;
    String workType = "";
    String dataType = "";
    String subject = "";

    LinearLayout datatype;

    EditText search_subject;
    RadioGroup group_datatype;
    RadioButton image;
    RadioButton text;
    RadioButton audio;
    Button searchButton;

    public static ProjectListFragment newInstance(String worktype) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putString("worktype", worktype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            button_result = getArguments().getString("worktype");
        }
    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_project_list,container,false);

        projectAdapter = new ProjectAdapter(projectArrayList);
        listView = view.findViewById(R.id.project_list);

        datatype = view.findViewById(R.id.radiogroup_datatype);
        searchButton = view.findViewById(R.id.project_search);

        search_subject = view.findViewById(R.id.search_subject);
        group_datatype = view.findViewById(R.id.radioGroup_datatype);
        image = view.findViewById(R.id.data_type_image);
        text = view.findViewById(R.id.data_type_text);
        audio = view.findViewById(R.id.data_type_audio);
        View line = view.findViewById(R.id.search_line);
        if(button_result.equals("수집")){//수집 버튼 누르면
            workType = "collection";
            projectList(view);
        }
        else{//라벨링
            datatype.setVisibility(View.GONE);
            search_subject.setVisibility(View.GONE);
            searchButton.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            workType = "labelling";
            projectList(view);
        }

        subject = search_subject.getText().toString();

        group_datatype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case(R.id.data_type_image):
                        dataType = "image";
                        break;
                    case(R.id.data_type_text):
                        dataType = "text";
                        break;
                    case(R.id.data_type_audio):
                        dataType = "audio";
                        break;
                }
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectList(v);
            }
        });

        projectAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RESTAPI instance = RESTAPI.getInstance();

                //토큰 받아오는데 null이면 로그인
                if(instance.getToken()==null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, LOGIN_REQUEST_CODE);
                } else {
                    Project project = (Project) projectAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
                    intent.putExtra("project", project);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    public void projectList(final View view){
        AsyncTask<String, Void, List<Project>> collectionListTask = new AsyncTask<String, Void, List<Project>>() {
            @Override
            protected List<Project> doInBackground(String... projectinfos) {
                List<Project> result = new ArrayList<Project>();
                try {
                    result = RESTAPI.getInstance().projectList(projectinfos[0],projectinfos[1],projectinfos[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Project> result){
                projectArrayList.clear();
                for(int i=0;i<result.size();i++){
                    if(result.get(i).getWorkType().equals("collection")) {
                        switch (result.get(i).getDataType()) {
                            case ("image"):
                                result.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_image_black_24dp));
                                break;
                            case ("text"):
                                result.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_text_black_24dp));
                                break;
                            case ("audio"):
                                result.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_voice_black_24dp));
                                break;
                        }
                    }
                    else{ //labelling
                        result.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_label_black_24dp));
                    }
                    if(!result.get(i).getDataType().equals("classification")){
                        projectAdapter.addItem(result.get(i));
                    }
                }
                listView.setAdapter(projectAdapter); //projectAdapter
                setListViewHeightBasedOnChildren(listView);
            }
        };
        collectionListTask.execute(workType,dataType,subject);
    }


    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        ProjectAdapter projectAdapter = (ProjectAdapter) listView.getAdapter();

        int totalHeight = 0;
        for (int i = 0; i < projectAdapter.getCount(); i++) {
            View listItem = projectAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (projectAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getContext(), "로그인이 필요합니다.",Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).goToHome();
            } else {
                ((MainActivity)getActivity()).loginSuccess();
            }
        }
    }

}
