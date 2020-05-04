package com.ivanhadzhi.popularmovies;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // start movies activity

        startActivity(new Intent(SplashActivity.this, MoviesActivity.class));

        // close splash

        finish();

    }
}
