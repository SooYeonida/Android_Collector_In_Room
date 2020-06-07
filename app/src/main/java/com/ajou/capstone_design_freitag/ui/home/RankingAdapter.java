package com.ajou.capstone_design_freitag.ui.home;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.ui.dto.User;

import java.util.ArrayList;

public class RankingAdapter extends BaseAdapter {

    private ArrayList<User> rankingArrayList = new ArrayList<User>();

    public RankingAdapter(ArrayList<User> rankingList)
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
            convertView = inflater.inflate(R.layout.layout_user_info_ranking,parent,false);
        }

        ImageView userIconView = (ImageView) convertView.findViewById(R.id.user_image);
        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name);
        TextView userPointView = (TextView) convertView.findViewById(R.id.user_point);

        User ranking = rankingArrayList.get(position);

        userIconView.setImageDrawable(ranking.getUserIcon());
        userNameView.setText(ranking.getUserID());
        userPointView.setText(ranking.getTotalPoint());

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

    public void addItem(User user){
        rankingArrayList.add(user);
    }

    public void removeAll(){rankingArrayList.removeAll(rankingArrayList);}

}