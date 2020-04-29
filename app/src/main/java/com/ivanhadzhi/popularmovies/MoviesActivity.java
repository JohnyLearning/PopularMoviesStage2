package com.ivanhadzhi.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ivanhadzhi.popularmovies.network.MoviesListTask;

import static com.ivanhadzhi.popularmovies.network.MoviesListTask.SortByParam.mostPopular;

public class MoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        MoviesListTask task = new MoviesListTask(response -> {}, error -> {}, mostPopular);
        task.execute();
    }
}
