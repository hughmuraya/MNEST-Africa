package com.mnestafrica.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mnestafrica.android.R;
import com.mnestafrica.android.dependancies.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import static com.mnestafrica.android.dependancies.AppController.TAG;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextInputLayout til_email;
    private TextInputEditText etxt_email;
    private MaterialButton reset_pass;
    private TextView sign_in;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reset Password");

        initialize();

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doResetPassword();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mint = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mint);

            }
        });


    }

    public void initialize(){

        til_email = (TextInputLayout) findViewById(R.id.til_email);
        etxt_email = (TextInputEditText) findViewById(R.id.etxt_email);
        reset_pass = (MaterialButton) findViewById(R.id.btn_reset_password);
        sign_in = (TextView) findViewById(R.id.tv_signin);




    }

    private void doResetPassword(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", etxt_email.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Constants.ENDPOINT+Constants.RESET_PASSWORD)
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



                        try {
                            boolean  status = response.has("success") && response.getBoolean("success");
                            String error = response.has("error") ? response.getString("error") : "";
                            String message = response.has("message") ? response.getString("message") : "";

                            if (status){


                                Intent mint = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                                mint.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mint);

                                Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();


                            }else if (!status){

                                Snackbar.make(findViewById(R.id.activity_reset_password), message, Snackbar.LENGTH_LONG).show();

                            }
                            else{

                                Snackbar.make(findViewById(R.id.activity_reset_password), error, Snackbar.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.e(TAG, String.valueOf(error.getErrorCode()));


                        if (error.getErrorCode() == 0){

                            Toast.makeText(ResetPasswordActivity.this, "An error occurred!", Toast.LENGTH_LONG).show();

                        }else if(error.getErrorBody().contains("Enter valid email")){

                            Snackbar.make(findViewById(R.id.activity_reset_password), "Enter valid email", Snackbar.LENGTH_LONG).show();


                        }
                        else{

                            Snackbar.make(findViewById(R.id.activity_reset_password), "" + error.getErrorBody(), Snackbar.LENGTH_LONG).show();

                        }

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


}