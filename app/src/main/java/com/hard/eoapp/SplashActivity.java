package com.hard.eoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hard.eoapp.login.LoginActivity;
import com.hard.eoapp.utils.SessionPrefs;
import com.pixplicity.easyprefs.library.Prefs;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.getString(SessionPrefs.isLogin, "").matches("1")){
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}