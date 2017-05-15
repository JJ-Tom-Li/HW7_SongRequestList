package com.example.no_1.hw7_songrequestlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername,etPassword;
    Button btnLogin;
    TextView tvRegister;
    SharedPreferences preference;
    String username="",passwd="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*find view*/
        findView();

        /*get username and password*/
        preference = getSharedPreferences("UserFile",MODE_PRIVATE);
        /*textView click listener*/
        tvRegister.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=etUsername.getText().toString();
                passwd=etPassword.getText().toString();
                preference.edit()
                        .putString(username,passwd)
                        .commit();
                Toast.makeText(LoginActivity.this,"創建帳號成功!",Toast.LENGTH_LONG)
                    .show();
            }
        });

        /*登入*/
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=etUsername.getText().toString();
                passwd=etPassword.getText().toString();
                if(passwd.equals(preference.getString(username,"UNKNOWN")))
                {
                    Toast.makeText(LoginActivity.this,"登入成功!",Toast.LENGTH_LONG)
                        .show();
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"帳號或密碼有誤!",Toast.LENGTH_LONG)
                        .show();
                }
            }
        });
    }

    public void findView()
    {
        etUsername=(EditText)findViewById(R.id.editTextUsername);
        etPassword=(EditText)findViewById(R.id.editTextPassword);
        btnLogin=(Button)findViewById(R.id.buttonLogin);
        tvRegister=(TextView)findViewById(R.id.textViewRegister);
    }
}
