package com.ajou.capstone_design_freitag.UI.home;

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

import com.ajou.capstone_design_freitag.UI.dto.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter_list1 = new RankingAdapter(rankingArrayList1);
        adpater_list2 = new RankingAdapter(rankingArrayList2);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        rankingPoint();
        View view = inflater.inflate(R.layout.fragment_home_menu,container,false);
        work_start = view.findViewById(R.id.banner1);
        create_project = view.findViewById(R.id.banner2);

        point = view.findViewById(R.id.ranking_list_total_point);
        accuracy = view.findViewById(R.id.ranking_list_accuracy);

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

    public void rankingPoint(){
        AsyncTask<Void, Void, String> rankingPointTask = new AsyncTask<Void, Void, String>() {
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

            @Override
            protected void onPostExecute(String result){
                try {
                    if(result!=null){
                        jsonParse(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        rankingPointTask.execute();
    }

    public void rankingAccuracy(final View view){

    }

    public void jsonParse(String list) throws JSONException {
        List<User> ranking = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(list);
        for(int i=0;i<jsonArray.length();i++){
            User user = new User();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            user.setUserID(jsonObject.getString("userId"));
            user.setTotalPoint(jsonObject.getString("totalPoint"));
            ranking.add(user);
        }
        adapter_list1.removeAll();
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
            adapter_list1.addItem(ranking.get(i));
        }
        point.setAdapter(adapter_list1);
        setListViewHeightBasedOnChildren(point);
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
