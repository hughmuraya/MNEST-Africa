package com.mnestafrica.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mnestafrica.android.R;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout til_username;
    private TextInputEditText username;
    private TextInputLayout til_password;
    private TextInputEditText password;
    private TextView forgot_password;
    private Button btn_sign_in;
    private TextView sign_up;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();

        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setTitle("Signing In...");
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mint = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mint);

            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                pDialog.show();
                Intent mint = new Intent(SignInActivity.this, MainActivity.class);
                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mint);

            }
        });

        /*sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mint = new Intent(SignInActivity.this, SignUpActivity.class);
                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mint);

            }
        });*/


    }

    public void initialize() {
        til_username = findViewById(R.id.til_username);
        til_password = findViewById(R.id.til_password);
        username = findViewById(R.id.etxt_username);
        password = findViewById(R.id.etxt_password);
        forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        sign_up = (TextView) findViewById(R.id.tv_sign_up);
    }
}