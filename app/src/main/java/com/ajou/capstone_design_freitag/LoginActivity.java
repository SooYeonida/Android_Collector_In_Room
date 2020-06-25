package com.ajou.capstone_design_freitag;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ajou.capstone_design_freitag.API.RESTAPI;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {
    private static final int REGISTER_OPENBANKING_REQUEST_CODE = 100;

    private View layout_login;
    private View layout_register;
    private View layout_register_openbanking;

    private EditText logindID;
    private EditText loginPassword;

    private EditText registerID;
    private EditText registerPassword;
    private EditText registerPasswordSecond;
    private EditText registerName;
    private EditText registerPhone;
    private EditText registerEmail;
    private EditText registerAffiliation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout_login = findViewById(R.id.layout_login);
        layout_register = findViewById(R.id.layout_register);
        layout_register_openbanking = findViewById(R.id.layout_register_openbanking);

        logindID = findViewById(R.id.loginID);
        loginPassword = findViewById(R.id.loginPassword);

        registerID = findViewById(R.id.registerID);
        registerPassword = findViewById(R.id.registerPassword);
        registerPasswordSecond = findViewById(R.id.registerPasswordSecond);
        registerName = findViewById(R.id.registerName);
        registerPhone = findViewById(R.id.registerPhone);
        registerEmail = findViewById(R.id.registerEmail);
        registerAffiliation = findViewById(R.id.registerAffiliation);
    }

    public void login(final View view) {
        new LoginTask(this).execute();
    }

    public void register(final View view) {
        new SignupTask(this).execute();
    }

    public void registerOpenBanking(final View view) {
        String url = RESTAPI.getInstance().getRegisterOpenBankingURL();
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("URL", url);
        startActivityForResult(intent, REGISTER_OPENBANKING_REQUEST_CODE);
    }

    public void goToLogin(View view) {
        logindID.setText("");
        loginPassword.setText("");

        layout_login.setVisibility(View.VISIBLE);
        layout_register.setVisibility(View.GONE);
        layout_register_openbanking.setVisibility(View.GONE);
    }

    public void goToRegister(View view) {
        registerID.setText("");
        registerPassword.setText("");
        registerPasswordSecond.setText("");
        registerName.setText("");
        registerPhone.setText("");
        registerEmail.setText("");
        registerAffiliation.setText("");

        layout_login.setVisibility(View.GONE);
        layout_register.setVisibility(View.VISIBLE);
        layout_register_openbanking.setVisibility(View.GONE);
    }

    private void goToRegisterOpenBanking() {
        layout_login.setVisibility(View.GONE);
        layout_register.setVisibility(View.GONE);
        layout_register_openbanking.setVisibility(View.VISIBLE);
    }

    private static class IDManageTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<LoginActivity> activityReference;

        @Override
        public Integer doInBackground(Void... voids) {
            return 0;
        }

        public IDManageTask(LoginActivity context) {
            activityReference = new WeakReference<>(context);
        }

        LoginActivity getActivity() {
            LoginActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            return activity;
        }

        void showToast(final String text)
        {
            final LoginActivity activity = getActivity();
            if(activity == null)
                return;

            activity.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private static class LoginTask extends IDManageTask {
        private String userID;
        private String userPassword;

        public LoginTask(LoginActivity context) {
            super(context);
        }

        @Override
        public Integer doInBackground(Void... voids) {
            getParameters();
            return RESTAPI.getInstance().login(userID, userPassword);
        }

        private void getParameters() {
            final LoginActivity activity = getActivity();
            if(activity == null)
                return;

            userID = activity.logindID.getText().toString();
            userPassword = activity.loginPassword.getText().toString();
        }

        @Override
        public void onPostExecute(Integer result) {
            final LoginActivity activity = getActivity();
            if(activity == null)
                return;

            switch (result) {
                case RESTAPI.LOGIN_SUCCESS_WITH_REWARD:
                    showToast("로그인 보상이 지급되었습니다!");
                    System.out.println("로그인 보상이 지급되었습니다!");
                case RESTAPI.LOGIN_SUCCESS:
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", result);
                    activity.setResult(RESULT_OK, returnIntent);
                    activity.finish();
                    break;
                case RESTAPI.LOGIN_FAIL:
                    showToast("아이디 또는 비밀번호를 잘못 입력했습니다.");
                    break;
            }
        }
    }

    private static class SignupTask extends IDManageTask {
        private String userID;
        private String userPassword;
        private String userPasswordSecond;
        private String userName;
        private String userPhone;
        private String userEmail;
        private String userAffiliation;

        public SignupTask(LoginActivity context) {
            super(context);
        }

        @Override
        public Integer doInBackground(Void... voids) {
            getParameters();
            if(validation()) {
                return RESTAPI.getInstance().signup(userID, userPassword, userName, userPhone, userEmail, userAffiliation);
            } else {
                return RESTAPI.REGISTER_VALIDATION_FAIL;
            }
        }

        private boolean validation() {
            return userPassword.equals(userPasswordSecond);
        }

        private void getParameters() {
            final LoginActivity activity = getActivity();
            if(activity == null)
                return;

            userID = activity.registerID.getText().toString();
            userPassword = activity.registerPassword.getText().toString();
            userPasswordSecond = activity.registerPasswordSecond.getText().toString();
            userName = activity.registerName.getText().toString();
            userPhone = activity.registerPhone.getText().toString();
            userEmail = activity.registerEmail.getText().toString();
            userAffiliation = activity.registerAffiliation.getText().toString();
        }

        @Override
        public void onPostExecute(Integer result) {
            final LoginActivity activity = getActivity();
            if(activity == null)
                return;

            switch (result) {
                case RESTAPI.REGISTER_SUCCESS:
                    activity.goToRegisterOpenBanking();
                    break;
                case RESTAPI.REGISTER_VALIDATION_FAIL:
                    showToast("패스워드를 다르게 입력했습니다.");
                    break;
                case RESTAPI.REGISTER_FAIL:
                    showToast("회원 가입에 실패했습니다.");
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_OPENBANKING_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                goToLogin(null);
            } else {
                Toast.makeText(this, "오픈뱅킹 등록에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
