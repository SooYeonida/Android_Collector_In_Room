package com.ajou.capstone_design_freitag.UI.mypage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;

import java.lang.ref.WeakReference;

public class TerminateProjectActivity extends AppCompatActivity {
    private Project project;
    private TextView finalPaymentProjectName;
    private TextView finalPaymentIfTerminate;
    private TextView finalPaymentCost;
    private TextView finalPaymentPayOrRefund;
    private Button finalPaymentByPoint;
    private Button finalPaymentByAccount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_validated_data);
        Intent intent = getIntent();
        project = intent.getParcelableExtra("project");

        setContentView(R.layout.activity_final_payment);

        finalPaymentProjectName = findViewById(R.id.final_payment_project_name);
        finalPaymentIfTerminate = findViewById(R.id.final_payment_if_terminate);
        finalPaymentCost = findViewById(R.id.final_payment_cost);
        finalPaymentPayOrRefund = findViewById(R.id.final_payment_pay_or_refund);

        GetFinalCostTask getFinalCostTask = new GetFinalCostTask(this);
        getFinalCostTask.execute();

        finalPaymentByPoint = findViewById(R.id.final_payment_by_point);
        finalPaymentByPoint.setOnClickListener(view -> {
            ;
        });
        finalPaymentByAccount = findViewById(R.id.final_payment_by_account);
        finalPaymentByAccount.setOnClickListener(view -> {
            ;
        });
    }

    static class TerminateProjectTask extends AsyncTask<Void,Void,Integer> {
        private WeakReference<TerminateProjectActivity> activityReference;

        TerminateProjectTask(TerminateProjectActivity context) {
            activityReference = new WeakReference<>(context);
        }

        TerminateProjectActivity getActivity() {
            TerminateProjectActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return activity;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return null;
        }
    }

    static class GetFinalCostTask extends TerminateProjectTask {
        Project project;

        GetFinalCostTask(TerminateProjectActivity context) {
            super(context);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            TerminateProjectActivity activity = getActivity();
            if(activity == null) {
                return null;
            } else {
                project = activity.project;
            }

            Integer result = null;
            try {
                result = RESTAPI.getInstance().terminateProject(project.getProjectId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            TerminateProjectActivity activity = getActivity();
            if(activity == null) {
                return;
            }

            if(result == null) {
                activity.finish();
            } else {
                TextView finalPaymentProjectName = activity.finalPaymentProjectName;
                TextView finalPaymentIfTerminate = activity.finalPaymentIfTerminate;
                TextView finalPaymentCost = activity.finalPaymentCost;
                TextView finalPaymentPayOrRefund = activity.finalPaymentPayOrRefund;

                finalPaymentProjectName.setText(project.getProjectName());
                if(result >= 0) {
                    finalPaymentIfTerminate.setText("를 종료하려면");
                    finalPaymentCost.setText(result + "원");
                    finalPaymentPayOrRefund.setText("을 추가로 결제해야 합니다.");
                } else {
                    finalPaymentIfTerminate.setText("를 종료하면");
                    finalPaymentCost.setText(-result + "원");
                    finalPaymentPayOrRefund.setText("이 환급됩니다.");
                }
            }
        }
    }

}
