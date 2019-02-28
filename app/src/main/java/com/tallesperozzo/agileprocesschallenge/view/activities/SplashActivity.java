package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.utils.Constants;

/*
 * SplashActivity
 *
 * Goes to BeerListActivity after 2 seconds
 *
 * Created by Talles Perozzo
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), BeerListActivity.class);
                startActivity(i);
                finish();
            }
        }, Constants.SPLASH_TIME_OUT);

    }
}
