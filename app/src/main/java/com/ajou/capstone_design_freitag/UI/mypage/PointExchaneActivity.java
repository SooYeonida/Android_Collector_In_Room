package com.ajou.capstone_design_freitag.UI.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;
import com.ajou.capstone_design_freitag.R;
import com.ajou.capstone_design_freitag.WebViewActivity;

import java.lang.ref.WeakReference;

public class PointExchaneActivity extends AppCompatActivity {
    private static final int REGISTER_OPENBANKING_REQUEST_CODE = 100;

    TextView pointExchangeMaximum;
    TextView pointExchangeAmount;
    Button pointExchangeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_exchange);

        Intent intent = getIntent();
        int userPoint = intent.getIntExtra("point", 0);

        pointExchangeMaximum = findViewById(R.id.point_exchange_maximum);
        pointExchangeMaximum.setText(String.valueOf(userPoint));
        pointExchangeAmount = findViewById(R.id.point_exchange_amount);
        pointExchangeAmount.setHint(String.valueOf(userPoint));
        pointExchangeButton = findViewById(R.id.point_exchange_button);
        pointExchangeButton.setOnClickListener(view -> {
            PointExchangeTask pointExchangeTask = new PointExchangeTask(this);
            pointExchangeTask.execute();
        });
    }

    static class PointExchangeTask extends AsyncTask<Void, Void, Integer> {
        WeakReference<PointExchaneActivity> activityReference;

        PointExchangeTask(PointExchaneActivity activity) {
            activityReference = new WeakReference<>(activity);
        }

        PointExchaneActivity getActivity() {
            PointExchaneActivity activity = activityReference.get();
            if(activity == null || activity.isFinishing()) {
                return null;
            }
            return activity;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            PointExchaneActivity activity = getActivity();
            if(activity == null) {
                return null;
            }

            TextView pointExchangeAmount = activity.pointExchangeAmount;
            int amount = 0;
            if(!pointExchangeAmount.getText().toString().equals("")) {
                amount = Integer.parseInt(pointExchangeAmount.getText().toString());
                Integer result;
                try {
                    result = RESTAPI.getInstance().exchange(amount);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            PointExchaneActivity activity = getActivity();
            if(activity == null) {
                return;
            }

            if(result == null){
                return;
            }

            if(result == RESTAPI.TRANSACTION_SUCCESS) {
                Toast.makeText(activity, "포인트가 성공적으로 환전되었습니다.", Toast.LENGTH_LONG).show();
                activity.setResult(RESULT_OK);
                activity.finish();
            } else if(result == RESTAPI.TRANSACTION_FAIL) {
                Toast.makeText(activity, "포인트 환전이 실패했습니다.", Toast.LENGTH_LONG).show();
            } else if(result == RESTAPI.TRANSACTION_NOT_REGISTERED_ACCOUNT_FAIL) {
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
