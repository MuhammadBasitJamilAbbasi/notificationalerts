package com.bj.georgehousetrust.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.bj.georgehousetrust.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
               checkSharedPreferences();
                finish();
            }
        }, 5000 );
    }




    private void checkSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("credentionls", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("name", null);

        if (userId != null) {
            // User ID exists in SharedPreferences, go to home screen
            startActivity(new Intent(this, Homescreen.class));
            finish();
        } else {
            // User ID doesn't exist in SharedPreferences, go to sign-in screen
            startActivity(new Intent(this, signin.class));
            finish();
        }
    }
}