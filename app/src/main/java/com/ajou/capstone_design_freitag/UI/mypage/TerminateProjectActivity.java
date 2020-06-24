package com.ajou.capstone_design_freitag.UI.mypage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.UI.dto.Project;
import com.ajou.capstone_design_freitag.WebViewActivity;

import java.lang.ref.WeakReference;

public class TerminateProjectActivity extends AppCompatActivity {
    private static final int REGISTER_OPENBANKING_REQUEST_CODE = 100;

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
            PointPaymentTask pointPaymentTask = new PointPaymentTask(this);
            pointPaymentTask.execute();
        });
        finalPaymentByAccount = findViewById(R.id.final_payment_by_account);
        finalPaymentByAccount.setOnClickListener(view -> {
            AccountPaymentTask accountPaymentTask = new AccountPaymentTask(this);
            accountPaymentTask.execute();
        });
    }

    static class TerminateProjectTask extends AsyncTask<Void,Void,Integer> {
        private WeakReference<TerminateProjectActivity> activityReference;
        Project project;

        TerminateProjectTask(TerminateProjectActivity context) {
            activityReference = new WeakReference<>(context);
            project = getActivity().project;
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
        GetFinalCostTask(TerminateProjectActivity context) {
            super(context);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
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

    static class PointPaymentTask extends TerminateProjectTask {
        PointPaymentTask(TerminateProjectActivity context) {
            super(context);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Integer result = null;
            try {
                result = RESTAPI.getInstance().terminatePoint(project.getProjectId());
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

            if(result == RESTAPI.PAYMENT_SUCCESS) {
                Toast.makeText(activity, "프로젝트가 성공적으로 종료되었습니다.", Toast.LENGTH_LONG).show();
                activity.setResult(RESULT_OK);
                activity.finish();
            } else {
                Toast.makeText(activity, "프로젝트 종료가 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    static class AccountPaymentTask extends TerminateProjectTask {
        AccountPaymentTask(TerminateProjectActivity context) {
            super(context);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            Integer result = null;
            try {
                result = RESTAPI.getInstance().terminateAccount(project.getProjectId());
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

            if(result == null){
                return;
            }

            if(result == RESTAPI.PAYMENT_SUCCESS) {
                Toast.makeText(activity, "프로젝트가 성공적으로 종료되었습니다.", Toast.LENGTH_LONG).show();
                activity.setResult(RESULT_OK);
                activity.finish();
            } else if(result == RESTAPI.PAYMENT_FAIL) {
                Toast.makeText(activity, "프로젝트 종료가 실패했습니다.", Toast.LENGTH_LONG).show();
            } else if(result == RESTAPI.PAYMENT_NOT_REGISTERED_ACCOUNT_FAIL) {
                Toast.makeText(activity, "계좌등록이 필요합니다.", Toast.LENGTH_LONG).show();
                String url = RESTAPI.getInstance().getRegisterOpenBankingURL();
                Intent intent = new Intent(activity.getApplicationContext(), WebViewActivity.class);
                intent.putExtra("URL", url);
                activity.startActivityForResult(intent, REGISTER_OPENBANKING_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_OPENBANKING_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "오픈뱅킹 등록에 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

}
