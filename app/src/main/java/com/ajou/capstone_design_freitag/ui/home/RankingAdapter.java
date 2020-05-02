package com.ajou.capstone_design_freitag.ui.home;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajou.capstone_design_freitag.R;

import java.util.ArrayList;

public class RankingAdapter extends BaseAdapter {

    private ArrayList<Ranking> rankingArrayList = new ArrayList<Ranking>();

    public RankingAdapter(ArrayList<Ranking> rankingList)
    {
        if (rankingList == null) {
            rankingArrayList = new ArrayList<Ranking>() ;
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
            convertView = inflater.inflate(R.layout.user_info_ranking,parent,false);
        }

        ImageView userIconView = (ImageView) convertView.findViewById(R.id.user_image);
        TextView userNameView = (TextView) convertView.findViewById(R.id.user_name);
        TextView userPointView = (TextView) convertView.findViewById(R.id.user_point);
        TextView userTotalTaskView = (TextView) convertView.findViewById(R.id.total_task);

        Ranking ranking = rankingArrayList.get(position);

        userIconView.setImageDrawable(ranking.getUserIcon());
        userNameView.setText(ranking.getUserName());
        userPointView.setText(Integer.toString(ranking.getTotalPoint()));
        userTotalTaskView.setText(Integer.toString(ranking.getAccuracy()));

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

    public void addItem(Drawable user_icon, String user_name, int user_point, int user_accuracy){
        Ranking ranking = new Ranking();

        ranking.setUserIcon(user_icon);
        ranking.setUserName(user_name);
        ranking.setTotalPoint(user_point);
        ranking.setAccuracy(user_accuracy);

        rankingArrayList.add(ranking);
    }


}