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

    ArrayList<User> rankingArrayList1 = new ArrayList<User>();
    ArrayList<User> rankingArrayList2 = new ArrayList<User>();

    Comparator<User> comparator_point = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
            return (o2.getTotalPoint()-o1.getTotalPoint());
        }
    };

    Comparator<User> comparator_accuracy = new Comparator<User>() {
        @Override
        public int compare(User o1, User o2) {
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

        setListViewHeightBasedOnChildren(listView1);
        setListViewHeightBasedOnChildren(listView2);

        return view;
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
