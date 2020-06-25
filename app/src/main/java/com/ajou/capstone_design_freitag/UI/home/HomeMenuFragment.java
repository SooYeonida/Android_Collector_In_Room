package com.ajou.capstone_design_freitag.UI.home;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;

import com.ajou.capstone_design_freitag.UI.dto.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeMenuFragment extends Fragment {

    PointRankingAdapter pointAdapter;
    AccuracyRankingAdapter accuracyAdapter;

    ArrayList<User> pointRankingArrayList = new ArrayList<User>();
    ArrayList<User> accuracyRankingArrayList = new ArrayList<User>();

    ListView point;
    ListView accuracy;

    Button work_start;
    Button create_project;

    HomeFragment homeFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointAdapter = new PointRankingAdapter(pointRankingArrayList);
        accuracyAdapter = new AccuracyRankingAdapter(accuracyRankingArrayList);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        RankingPointTask rankingPointTask = new RankingPointTask();
        RankingAccuracyTask rankingAccuracyTask = new RankingAccuracyTask();
        rankingPointTask.execute();
        rankingAccuracyTask.execute();

        View view = inflater.inflate(R.layout.fragment_home_menu,container,false);
        work_start = view.findViewById(R.id.banner1);
        create_project = view.findViewById(R.id.banner2);

        point = view.findViewById(R.id.ranking_list_total_point);
        accuracy = view.findViewById(R.id.ranking_list_accuracy);

        pointAdapter.notifyDataSetChanged();
        accuracyAdapter.notifyDataSetChanged();

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

    public class RankingPointTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... infos) {
                try {
                    String result = RESTAPI.getInstance().rankingPoint();
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected void onPostExecute(String result){
                try {
                    if(result!=null){
                        List<User> ranking = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(result);
                        for(int i=0;i<jsonArray.length();i++){
                            User user = new User();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            user.setUserID(jsonObject.getString("userId"));
                            user.setTotalPoint(jsonObject.getString("totalPoint"));
                            user.setNumOfProblems(jsonObject.getString("numOfProblems"));
                            ranking.add(user);
                        }
                        pointAdapter.removeAll();
                     //   ranking.sort(Comparator.comparing(User::getTotalPoint).reversed().thenComparing(User::getNumOfProblems).reversed());
                        Collections.sort(ranking,new PointCompare());
                        for(int i=0;i<ranking.size();i++){
                            if(i==0) {
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking1));
                            }
                            else if(i==1){
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking2));
                            }
                            else if(i==2){
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking3));
                            }
                            else{
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.user));
                            }
                        }
                        pointAdapter.addList(ranking);
                        point.setAdapter(pointAdapter);
                        setListViewHeightBasedOnChildrenPoint(point);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    public class RankingAccuracyTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... infos) {
                try {
                    String result = RESTAPI.getInstance().rankingAccuracy();
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                try {
                    if(result!=null){
                        List<User> ranking = new ArrayList<>();
                        JSONArray jsonArray = new JSONArray(result);
                        for(int i=0;i<jsonArray.length();i++){
                            User user = new User();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            user.setUserID(jsonObject.getString("userId"));
                            user.setAccuracy(Float.parseFloat(jsonObject.getString("userAccuracy")));
                            user.setNumOfProblems(jsonObject.getString("numOfProblems"));
                            ranking.add(user);
                        }
                        accuracyAdapter.removeAll();
                        Collections.sort(ranking,new AccuracyCompare());
                        for(int i=0;i<ranking.size();i++){
                            if(i==0) {
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking1));
                            }
                            else if(i==1){
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking2));
                            }
                            else if(i==2){
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.ranking3));
                            }
                            else{
                                ranking.get(i).setUserIcon(ContextCompat.getDrawable(getContext(), R.drawable.user));
                            }
                        }
                        accuracyAdapter.addList(ranking);
                        accuracy.setAdapter(accuracyAdapter);
                        setListViewHeightBasedOnChildrenAccuracy(accuracy);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    public class PointCompare implements Comparator<User> {
        int ret = 0;
        @Override
        public int compare(User s1, User s2) {
            if(Integer.parseInt(s1.getTotalPoint()) < Integer.parseInt(s2.getTotalPoint())) {
                ret = 1;
            }
            if(Integer.parseInt(s1.getTotalPoint()) == Integer.parseInt(s2.getTotalPoint())) {
                if(s1.getNumOfProblems().compareTo(s2.getNumOfProblems()) == 0) {
                    ret = 0;
                } else if(s1.getNumOfProblems().compareTo(s2.getNumOfProblems()) < 0) {
                    ret = 1;
                } else if(s1.getNumOfProblems().compareTo(s2.getNumOfProblems()) > 0) {
                    ret = -1;
                }
            }
            if(Integer.parseInt(s1.getTotalPoint()) > Integer.parseInt(s2.getTotalPoint())) {
                ret = -1;
            }
            return ret;
        }
    }

    public class AccuracyCompare implements Comparator<User> {
        int ret = 0;
        @Override
        public int compare(User s1, User s2) {
            if(s1.getAccuracy() < s2.getAccuracy()) {
                ret = 1;
            }
            if(s1.getAccuracy() == s2.getAccuracy()) {
                if(s1.getNumOfProblems().compareTo(s2.getNumOfProblems()) == 0) {
                    ret = 0;
                } else if(s1.getNumOfProblems().compareTo(s2.getNumOfProblems()) < 0) {
                    ret = 1;
                } else if(s1.getNumOfProblems().compareTo(s2.getNumOfProblems()) > 0) {
                    ret = -1;
                }
            }
            if(s1.getAccuracy() > s2.getAccuracy()) {
                ret = -1;
            }
            return ret;
        }
    }

    public static void setListViewHeightBasedOnChildrenPoint(@NonNull ListView listView) {
        PointRankingAdapter rankingAdapter = (PointRankingAdapter) listView.getAdapter();

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
    public static void setListViewHeightBasedOnChildrenAccuracy(@NonNull ListView listView) {
        AccuracyRankingAdapter rankingAdapter = (AccuracyRankingAdapter) listView.getAdapter();

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
