package com.ajou.capstone_design_freitag;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ajou.capstone_design_freitag.API.RESTAPI;

public class MainActivity extends AppCompatActivity {
    private View layout_login;
    private View layout_register;

    private EditText logindID;
    private EditText loginPassword;

    private EditText registerID;
    private EditText registerPassword;
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

        logindID = findViewById(R.id.loginID);
        loginPassword = findViewById(R.id.loginPassword);

        registerID = findViewById(R.id.registerID);
        registerPassword = findViewById(R.id.registerPassword);
        registerName = findViewById(R.id.registerName);
        registerPhone = findViewById(R.id.registerPhone);
        registerEmail = findViewById(R.id.registerEmail);
        registerAffiliation = findViewById(R.id.registerAffiliation);
    }

    public void register(final View view) {
        final String userID = registerID.getText().toString();
        final String userPassword = registerPassword.getText().toString();
        final String userName = registerName.getText().toString();
        final String userPhone = registerPhone.getText().toString();
        final String userEmail = registerEmail.getText().toString();
        final String userAffiliation = registerAffiliation.getText().toString();

        AsyncTask<String, Void, Boolean> singupTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... registerInfos) {
                boolean result = RESTAPI.getInstance().signup(registerInfos[0],
                        registerInfos[1],
                        registerInfos[2],
                        registerInfos[3],
                        registerInfos[4],
                        registerInfos[5]);
                return new Boolean(result);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("회원 가입 성공!");
                    goToLogin(view);
                } else {
                    showToast("회원 가입 실패...");
                }
            }

            private void showToast(final String text)
            {
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        singupTask.execute(userID, userPassword, userName, userPhone, userEmail, userAffiliation);
    }

    public void login(final View view) {
        final String userID = logindID.getText().toString();
        final String userPassword = loginPassword.getText().toString();

        AsyncTask<String, Void, Boolean> singupTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... registerInfos) {
                boolean result = RESTAPI.getInstance().login(registerInfos[0], registerInfos[1]);
                return new Boolean(result);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    showToast("로그인 성공!");
                    goToLogin(view);
                } else {
                    showToast("로그인 실패...");
               }
            }

            private void showToast(final String text)
            {
                MainActivity.this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        singupTask.execute(userID, userPassword);
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
}
