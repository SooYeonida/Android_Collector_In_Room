package com.ajou.capstone_design_freitag.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.dto.User;

import java.util.ArrayList;
import java.util.List;


public class HomeMenuFragment extends Fragment {

    RankingAdapter adapter_list1;
    RankingAdapter adpater_list2;

    ArrayList<User> rankingArrayList1 = new ArrayList<User>();
    ArrayList<User> rankingArrayList2 = new ArrayList<User>();

    ListView point;
    ListView accuracy;

    Button work_start;
    Button create_project;

    HomeFragment homeFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home_menu,container,false);

        work_start = view.findViewById(R.id.banner1);
        create_project = view.findViewById(R.id.banner2);

        adapter_list1 = new RankingAdapter(rankingArrayList1);
        adpater_list2 = new RankingAdapter(rankingArrayList2);

        point = view.findViewById(R.id.ranking_list_total_point);
        accuracy = view.findViewById(R.id.ranking_list_accuracy);

        rankingPoint(view);
        //rankingAccuracy(view);
        adapter_list1.notifyDataSetChanged();

        work_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment = (HomeFragment) getParentFragment();
             homeFragment.replaceFragment(1);
            }
        });
        create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment = (HomeFragment) getParentFragment();
             homeFragment.replaceFragment(2);
            }
        });

        return view;
    }
    public void rankingPoint(final View view){
        AsyncTask<Void, Void, List<User>> rankingPointTask = new AsyncTask<Void, Void, List<User>>() {
            @Override
            protected List<User> doInBackground(Void... usernfos) {
                List<User> result = new ArrayList<User>();
                try {
                    result = RESTAPI.getInstance().rankingPoint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<User> result){
                for(int i=0;i<result.size();i++){
                    if(i==0) {
                       result.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking1));
                    }
                    else if(i==1){
                        result.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking2));
                    }
                    else if(i==2){
                        result.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking3));
                    }
                    else{
                        result.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.user));
                    }
                    adapter_list1.addItem(result.get(i));
                }
                point.setAdapter(adapter_list1);
                setListViewHeightBasedOnChildren(point);
            }
        };
        rankingPointTask.execute();
    }

    public void rankingAccuracy(final View view){

    }


    public static void setListViewHeightBasedOnChildren(@NonNull ListView listView) {
        RankingAdapter rankingAdapter = (RankingAdapter) listView.getAdapter();

        int totalHeight = 0;
        for (int i = 0; i < rankingAdapter.getCount(); i++) {
            View listItem = rankingAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (rankingAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
