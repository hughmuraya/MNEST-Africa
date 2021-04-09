package com.mnestafrica.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mnestafrica.android.R;

/*CREATED BY Hugh*/

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    startActivity(new Intent(SplashScreenActivity.this, SignInActivity.class));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
}