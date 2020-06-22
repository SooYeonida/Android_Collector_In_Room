package com.ajou.capstone_design_freitag.UI.mypage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import java.util.ArrayList;
import java.util.List;


public class RequestProjectListFragment extends Fragment {

    ArrayList<Project> projectArrayList = new ArrayList<>();
    ProjectAdapter projectAdapter;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_project_list, container, false);

        projectAdapter = new ProjectAdapter(projectArrayList);
        listView  = view.findViewById(R.id.project_list_mypage);

        projectList(view);
        projectAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project = (Project) projectAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), RequestDetailActivity.class);
                intent.putExtra("project", project);
                startActivity(intent);
            }
        });
        return view;
    }

    public void projectList(final View view){
        AsyncTask<Void, Void, List<Project>> collectionListTask = new AsyncTask<Void, Void, List<Project>>() {
            @Override
            protected List<Project> doInBackground(Void... info) {
                List<Project> result = new ArrayList<Project>();
                try {
                    result = RESTAPI.getInstance().requestProjectList();
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
                    else{
                        result.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_label_black_24dp));
                    }
                    projectAdapter.addItem(result.get(i));
                }
                listView.setAdapter(projectAdapter); //projectAdapter
                setListViewHeightBasedOnChildren(listView);
            }
        };
        collectionListTask.execute();
    }

    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        com.ajou.capstone_design_freitag.UI.mypage.ProjectAdapter projectAdapter = (com.ajou.capstone_design_freitag.UI.mypage.ProjectAdapter) listView.getAdapter();

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

}
