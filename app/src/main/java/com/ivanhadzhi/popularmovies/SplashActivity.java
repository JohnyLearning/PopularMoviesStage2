package com.ivanhadzhi.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try { Thread.sleep(3000); } catch (Exception ex) {};
        // start movies activity

        startActivity(new Intent(SplashActivity.this, MoviesActivity.class));

        // close splash

        finish();

    }
}
