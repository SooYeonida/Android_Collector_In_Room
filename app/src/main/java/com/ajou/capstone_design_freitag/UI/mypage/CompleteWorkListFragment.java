package com.ajou.capstone_design_freitag.UI.mypage;

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
import com.ajou.capstone_design_freitag.UI.dto.WorkHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CompleteWorkListFragment extends Fragment {

    ArrayList<WorkHistory> workHistoryArrayList = new ArrayList<>();
    CompleteWorkAdapter workAdapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_complete_work_list, container, false);

        workAdapter = new CompleteWorkAdapter(workHistoryArrayList);
        listView = view.findViewById(R.id.work_list_mypage);
        workAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    public void workList(){
        AsyncTask<Void, Void, String> collectionListTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... info) {
                try {
                  String result = RESTAPI.getInstance().workHistory();
                  return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                try {
                    if(result!=null) {
                        jsonParse(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        collectionListTask.execute();
    }

    public void jsonParse(String list) throws JSONException {
                List<WorkHistory> workHistoryList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(list);
                for(int i=0;i<jsonArray.length();i++){
                    WorkHistory work = new WorkHistory();
                    JSONObject jsonObject;
                    jsonObject = jsonArray.getJSONObject(i);
                    work.setProjectRequester(jsonObject.getString("projectRequester"));
                    work.setProjectName(jsonObject.getString("projectName"));
                    work.setProjectWorkType(jsonObject.getString("projectWorkType"));
                    work.setProjectDataType(jsonObject.getString("projectDataType"));
                    work.setProjectStatus(jsonObject.getString("projectStatus"));
                    work.setProblemId(Integer.parseInt(jsonObject.getString("problemId")));
                    workHistoryList.add(work);
                }
        for(int i=0;i<workHistoryList.size();i++){
            if(workHistoryList.get(i).getProjectWorkType().equals("collection")) {
                switch (workHistoryList.get(i).getProjectDataType()) {
                    case ("image"):
                        workHistoryList.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_image_black_24dp));
                        break;
                    case ("text"):
                        workHistoryList.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_text_black_24dp));
                        break;
                    case ("audio"):
                        workHistoryList.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_voice_black_24dp));
                        break;
                }
            }
            else{
                workHistoryList.get(i).setProjectIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_label_black_24dp));
            }
            workAdapter.addItem(workHistoryList.get(i));
        }
        listView.setAdapter(workAdapter);
        setListViewHeightBasedOnChildren(listView);
    }

    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        CompleteWorkAdapter workAdapter = (CompleteWorkAdapter) listView.getAdapter();

        int totalHeight = 0;
        for (int i = 0; i < workAdapter.getCount(); i++) {
            View listItem = workAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (workAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
