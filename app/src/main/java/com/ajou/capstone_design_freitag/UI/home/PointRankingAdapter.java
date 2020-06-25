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

public class PointRankingAdapter extends BaseAdapter {

    private ArrayList<User> rankingArrayList = new ArrayList<User>();

    public PointRankingAdapter(ArrayList<User> rankingList)
    {
        if (rankingList == null) {
            rankingArrayList = new ArrayList<User>() ;
        } else {
            rankingArrayList = rankingList ;
        }
    }

    //개수리턴
    @Override
    public int getCount(){
        return rankingArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_user_info_point_ranking,parent,false);
        }

        ImageView userIconView = (ImageView) convertView.findViewById(R.id.user_image);
        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name);
        TextView userPointView = (TextView) convertView.findViewById(R.id.user_point);
        TextView userNumOfProblems = convertView.findViewById(R.id.user_num_problem);

        User ranking = rankingArrayList.get(position);

        userIconView.setImageDrawable(ranking.getUserIcon());
        userNameView.setText(ranking.getUserID());
        userPointView.setText(ranking.getTotalPoint());
        userNumOfProblems.setText(ranking.getNumOfProblems());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return rankingArrayList.get(position);
    }

    public void addList(List<User> user){
        rankingArrayList= (ArrayList<User>) user;
    }

    public void removeAll(){rankingArrayList.removeAll(rankingArrayList);}

}