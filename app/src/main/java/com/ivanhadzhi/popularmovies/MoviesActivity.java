package com.ivanhadzhi.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.network.MoviesListTask;

import java.util.List;

import static com.ivanhadzhi.popularmovies.MovieDetailActivity.MOVIE_BUNDLE_PARAM;
import static com.ivanhadzhi.popularmovies.network.MoviesListTask.SortByParam.mostPopular;

public class MoviesActivity extends AppCompatActivity {

    private MoviesAdapter moviesAdapter;
    private RecyclerView moviesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        moviesContainer = findViewById(R.id.rv_movies);
        moviesContainer.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    protected void onStart() {
        super.onStart();
        MoviesListTask task = new MoviesListTask(movies -> {
            setupUI(movies);
        }, error -> {}, mostPopular);
        task.execute();
    }

    private void setupUI(List<Movie> movies) {
        moviesAdapter = new MoviesAdapter(this, movies);
        moviesAdapter.setClickListener(movie -> {
            // TODO: call movie detail activity
            Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MOVIE_BUNDLE_PARAM, movie);
            startActivity(movieDetailIntent);

        });
        moviesContainer.setAdapter(moviesAdapter);
    }
}
