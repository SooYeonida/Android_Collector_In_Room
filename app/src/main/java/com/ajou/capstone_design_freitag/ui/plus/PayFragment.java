package com.ajou.capstone_design_freitag.ui.plus;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.LoginActivity;
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;

public class PayFragment extends Fragment {

     String cost;
     String projectName = null;

     TextView project_name;
     TextView project_cost;
     Button pay_point;
     Button pay_account;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Project projectinstance = Project.getProjectinstance();
        cost =  Integer.toString(projectinstance.getCost());
        projectName = projectinstance.getProjectName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_pay, container, false);

        project_name = (TextView) view.findViewById(R.id.pay_project_name);
        project_cost = (TextView) view.findViewById(R.id.pay_project_cost);
        pay_point = (Button) view.findViewById(R.id.pay_point_btn);
        pay_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point(v);
            }
        });
        pay_account = (Button) view.findViewById(R.id.pay_account_btn);
        pay_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account(v);
            }
        });
        project_name.setText(projectName);
        project_cost.setText(cost);
        return  view;

    }

    public void point(View view){

                AsyncTask<Void, Void,Boolean > pointTask = new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        boolean result = false;
                        try {
                            result = RESTAPI.getInstance().pointPayment();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
            protected void onPostExecute(Boolean result) {
                if(result){
                    ((MainActivity)getActivity()).goToHome();
                    Toast.makeText(getActivity(), "결제 성공", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "포인트 결제 실패. 다른 결제 수단을 선택하쇼", Toast.LENGTH_SHORT).show();
                }
            }


        };
        pointTask.execute();
    }


    public void account(View view){

    }
}
