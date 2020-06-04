package com.ajou.capstone_design_freitag.ui.mypage;

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
import com.ajou.capstone_design_freitag.ui.dto.WorkHistory;

import java.util.ArrayList;
import java.util.List;

public class CompleteWorkListFragment extends Fragment {

    ArrayList<WorkHistory> workHistoryArrayList = new ArrayList<>();
    WorkAdapter workAdapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_complete_work_list, container, false);

        workAdapter = new WorkAdapter(workHistoryArrayList);
        listView = view.findViewById(R.id.work_list_mypage);

        workList(view);
        workAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }

    public void workList(final View view){
        AsyncTask<Void, Void, List<WorkHistory>> collectionListTask = new AsyncTask<Void, Void, List<WorkHistory>>() {
            @Override
            protected List<WorkHistory> doInBackground(Void... info) {
                List<WorkHistory> result = new ArrayList<WorkHistory>();
                try {
                    result = RESTAPI.getInstance().workHistory();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<WorkHistory> result){
                for(int i=0;i<result.size();i++){
                    if(result.get(i).getProjectWorkType().equals("collection")) {
                        switch (result.get(i).getProjectDataType()) {
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
                    workAdapter.addItem(result.get(i));
                }
                listView.setAdapter(workAdapter);
                setListViewHeightBasedOnChildren(listView);
            }
        };
        collectionListTask.execute();
    }

    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        WorkAdapter workAdapter = (WorkAdapter) listView.getAdapter();

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
