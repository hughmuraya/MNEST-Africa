package com.mnestafrica.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.fxn.stash.Stash;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mnestafrica.android.R;
import com.mnestafrica.android.dependancies.Constants;
import com.mnestafrica.android.models.auth;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mnestafrica.android.dependancies.AppController.TAG;

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

        Stash.init(this);

        AndroidNetworking.initialize(getApplicationContext());

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

                pDialog.show();
                doLoginRequest();

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

    private void doLoginRequest() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password.getText().toString());
            jsonObject.put("username", username.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Constants.ENDPOINT+ Constants.LOGIN)
                .addHeaders("Content-Type", "application.json")
                .addHeaders("Accept", "*/*")
                .addHeaders("Accept", "gzip, deflate, br")
                .addHeaders("Connection","keep-alive")
                .addJSONObjectBody(jsonObject) // posting json
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response

                        Log.e(TAG, response.toString());

                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog.cancel();
                        }

                        try {
                            boolean  status = response.has("success") && response.getBoolean("success");
                            String error = response.has("error") ? response.getString("error") : "";
                            String auth_token = response.has("auth_token") ? response.getString("auth_token") : "";

                            if (response.has("auth_token")){

                                auth newUser = new auth(auth_token);

                                Stash.put(Constants.AUTH_TOKEN, newUser);

                                Bundle bundle = new Bundle();
                                bundle.putString("auth", auth_token);

                                Intent mint = new Intent(SignInActivity.this, MainActivity.class);
                                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mint.putExtras(bundle);
                                startActivity(mint);

                                Toast.makeText(SignInActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();


                            }else if (!status){

                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.hide();
                                    pDialog.cancel();
                                }

                                Snackbar.make(findViewById(R.id.activity_sign_in), error, Snackbar.LENGTH_LONG).show();

                            }
                            else{

                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.hide();
                                    pDialog.cancel();
                                }

                                Snackbar.make(findViewById(R.id.activity_sign_in), error, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, String.valueOf(error.getErrorCode()));

                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.hide();
                            pDialog.cancel();
                        }

                        if (error.getErrorBody().contains("Unable to log in with provided credentials.")){

                            Snackbar.make(findViewById(R.id.activity_sign_in), "Invalid username or password." , Snackbar.LENGTH_LONG).show();

                            til_username.setError("Please confirm the username is correct!");
                            til_password.setError("Please confirm the password is correct!");

                        }
                        else if(error.getErrorCode() == 0 ){

                            Snackbar.make(findViewById(R.id.activity_sign_in), "Please try again later!" , Snackbar.LENGTH_LONG).show();

                        }
                        else if(error.getErrorCode() == 500 ){

                            Snackbar.make(findViewById(R.id.activity_sign_in), "Internal Server Error. Please try again later!" , Snackbar.LENGTH_LONG).show();

                        }

                        else {

                            Snackbar.make(findViewById(R.id.activity_sign_in), "" + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                        }


                    }
                });

    }

}