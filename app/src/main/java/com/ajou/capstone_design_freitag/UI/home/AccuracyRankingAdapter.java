package com.ajou.capstone_design_freitag.UI.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.User;

import java.util.ArrayList;
import java.util.List;

public class AccuracyRankingAdapter extends BaseAdapter {

    private ArrayList<User> accuracyArrayList = new ArrayList<User>();

    public AccuracyRankingAdapter(ArrayList<User> rankingList){
        if(rankingList ==null){
            accuracyArrayList = new ArrayList<>();
        }else{
            accuracyArrayList = rankingList;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_user_info_accuracy_ranking,parent,false);
        }

        ImageView userIconView = (ImageView) convertView.findViewById(R.id.user_image_accuracy);
        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name_accuracy);
        TextView userAccuracy = (TextView) convertView.findViewById(R.id.user_accuracy);
        TextView userNumOfProblems = convertView.findViewById(R.id.user_num_problem_accuracy);

        User ranking = accuracyArrayList.get(position);

        userIconView.setImageDrawable(ranking.getUserIcon());
        userNameView.setText(ranking.getUserID());
        userAccuracy.setText(Float.toString(ranking.getAccuracy()*100)+"%");
        userNumOfProblems.setText(ranking.getNumOfProblems());

        return convertView;
    }

    public void addList(List<User> user){
        accuracyArrayList= (ArrayList<User>) user;
    }
    public void removeAll(){
        accuracyArrayList.removeAll(accuracyArrayList);
    }
    @Override
    public int getCount() {
        return accuracyArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return accuracyArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
