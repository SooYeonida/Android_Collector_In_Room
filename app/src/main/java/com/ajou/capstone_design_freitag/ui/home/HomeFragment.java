package com.ajou.capstone_design_freitag.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ajou.capstone_design_freitag.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    RankingAdapter adapter_list1;
    RankingAdapter adpater_list2;

    ArrayList<Ranking> rankingArrayList1 = new ArrayList<Ranking>();
    ArrayList<Ranking> rankingArrayList2 = new ArrayList<Ranking>();

    Comparator<Ranking> comparator_point = new Comparator<Ranking>() {
        @Override
        public int compare(Ranking o1, Ranking o2) {
            return (o2.getTotalPoint()-o1.getTotalPoint());
        }
    };

    Comparator<Ranking> comparator_accuracy = new Comparator<Ranking>() {
        @Override
        public int compare(Ranking o1, Ranking o2) {
            return (o2.getAccuracy()-o1.getAccuracy());
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        adapter_list1 = new RankingAdapter(rankingArrayList1);
        adpater_list2 = new RankingAdapter(rankingArrayList2);

        ListView listView1 = view.findViewById(R.id.ranking_list_total_point);
        ListView listView2 = view.findViewById(R.id.ranking_list_accuracy);
        listView1.setAdapter(adapter_list1);
        listView2.setAdapter(adpater_list2);

        //임시로 랭킹 유저 추가 디비 연결하고 가져와야함
        adapter_list1.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user1),"nabong",1000,10);
        adapter_list1.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user2),"pury",2000,5);
        adapter_list1.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user3),"sooyeon",3000,7);
        adapter_list1.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user4),"woney",4000,2);
        adapter_list1.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user5),"merong",5000,11);
        adapter_list1.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user6),"dddd",6000,6);

        adpater_list2.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user1),"nabong",1000,10);
        adpater_list2.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user2),"pury",2000,5);
        adpater_list2.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user3),"sooyeon",3000,7);
        adpater_list2.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user4),"woney",4000,2);
        adpater_list2.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user5),"merong",5000,11);
        adpater_list2.addItem(ContextCompat.getDrawable(getActivity(),R.drawable.user6),"dddd",6000,6);

        Collections.sort(rankingArrayList1,comparator_point);
        Collections.sort(rankingArrayList2,comparator_accuracy);
        adapter_list1.notifyDataSetChanged();
        adpater_list2.notifyDataSetChanged();

        return view;

    }



}
