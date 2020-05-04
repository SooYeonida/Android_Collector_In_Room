package com.ajou.capstone_design_freitag.ui.plus;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ajou.capstone_design_freitag.R;

public class PlusFragment extends Fragment implements View.OnClickListener{
   //라벨링일 경우 사용자가 클래스 입력?

    Button make_button;
    Button example_data_button;
    Button labelling_data_button;
    private Context context;

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_plus,container,false);
        make_button = (Button) view.findViewById(R.id.make_button);
        example_data_button = (Button) view.findViewById(R.id.example_data_selection);
        labelling_data_button = (Button) view.findViewById(R.id.labelling_data_selection);

        make_button.setOnClickListener(this);
        example_data_button.setOnClickListener(this);
        labelling_data_button.setOnClickListener(this);

        context = container.getContext();
        return view;
    }

    @Override
    public void onClick(View view){
    switch (view.getId()){
        case R.id.make_button:
            make_project(view);
        case R.id.example_data_selection:
            upload_example_data(view);
        case R.id.labelling_data_selection:
            upload_labelling_data(view);
    }

    }

    public void make_project(View view){
        //RESTful API
        Toast.makeText(context,"프로젝트 생성 완료",Toast.LENGTH_LONG).show();
    }

    public void upload_example_data(View view){
        //RESTful API
        Toast.makeText(context,"예시 이미지 데이터 업로드",Toast.LENGTH_LONG).show();
    }

    public void upload_labelling_data(View view){
        //RESTful API
        Toast.makeText(context,"라벨링 데이터 업로드",Toast.LENGTH_LONG).show();
    }


}
