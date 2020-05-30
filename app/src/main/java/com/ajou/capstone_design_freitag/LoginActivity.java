package com.ajou.capstone_design_freitag;

import android.app.Activity;
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
        RESTAPI.getInstance().registerOpenBanking(this);
    }

    public void goToLogin(View view) {
        logindID.setText("");
        loginPassword.setText("");

        layout_login.setVisibility(View.VISIBLE);
        layout_register.setVisibility(View.GONE);
    }

    public void goToRegister(View view) {
        registerID.setText("");
        registerPassword.setText("");
        registerName.setText("");
        registerPhone.setText("");
        registerEmail.setText("");
        registerAffiliation.setText("");

        layout_login.setVisibility(View.GONE);
        layout_register.setVisibility(View.VISIBLE);
    }

    private void goToRegisterOpenBanking() {
        layout_register.setVisibility(View.GONE);
        layout_register_openbanking.setVisibility(View.VISIBLE);
    }

    private static class IDManageTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<LoginActivity> activityReference;

        @Override
        public Boolean doInBackground(Void... voids) {
            return true;
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
        public Boolean doInBackground(Void... voids) {
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
        public void onPostExecute(Boolean result) {
            final LoginActivity activity = getActivity();
            if(activity == null)
                return;

            if(result) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                activity.setResult(RESULT_OK, returnIntent);
                activity.finish();
            } else {
                showToast("아이디 또는 비밀번호를 잘못 입력했습니다.");
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
        public Boolean doInBackground(Void... voids) {
            getParameters();
            if(validation()) {
                return RESTAPI.getInstance().signup(userID, userPassword, userName, userPhone, userEmail, userAffiliation);
            } else {
                return false;
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
        public void onPostExecute(Boolean result) {
            final LoginActivity activity = getActivity();
            if(activity == null)
                return;

            if (result) {
                activity.goToRegisterOpenBanking();
            } else {
                showToast("회원 가입에 실패했습니다.");
            }
        }
    }
}
