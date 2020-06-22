package com.ajou.capstone_design_freitag.UI.plus;

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
import com.ajou.capstone_design_freitag.MainActivity;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import java.lang.ref.WeakReference;

public class PayFragment extends Fragment {

     private String cost;
     private String projectName = null;

     private TextView project_name;
     private TextView project_cost;
     private Button pay_point;
     private Button pay_account;

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

        project_name = view.findViewById(R.id.pay_project_name);
        project_cost = view.findViewById(R.id.pay_project_cost);
        pay_point = view.findViewById(R.id.pay_point_btn);
        pay_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point(v);
            }
        });
        pay_account = view.findViewById(R.id.pay_account_btn);
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

    private void point(View view) {
        PayTask pointPayTask = new PayTask(this, "point");
        pointPayTask.execute();
    }


    private void account(View view) {
        PayTask pointPayTask = new PayTask(this, "account");
        pointPayTask.execute();
    }

    private static class PayTask extends AsyncTask<Void, Void,Boolean > {
        private WeakReference<PayFragment> fragmentReference;
        private final String method;

        public PayTask(PayFragment context, String method) {
            fragmentReference = new WeakReference<>(context);
            this.method = method;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean result = false;
            try {
                result = RESTAPI.getInstance().payment(method);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            PayFragment fragment = getFragment();
            if(fragment == null)
                return;

            if(result) {
                ((MainActivity)fragment.getActivity()).goToHome();
                Toast.makeText(fragment.getActivity(), "결제 성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(fragment.getActivity(), "결제에 실패했습니다. 다른 결제 수단을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

        private PayFragment getFragment() {
            PayFragment fragment = fragmentReference.get();
            if (fragment == null || fragment.isRemoving()) {
                return null;
            }
            return fragment;
        }
    };
}
