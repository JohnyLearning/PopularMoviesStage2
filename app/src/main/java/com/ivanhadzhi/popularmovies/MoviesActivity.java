package com.ivanhadzhi.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.network.MoviesListTask;

import java.util.List;

import static com.ivanhadzhi.popularmovies.MovieDetailActivity.MOVIE_BUNDLE_PARAM;
import static com.ivanhadzhi.popularmovies.model.SortBy.POPULAR;
import static com.ivanhadzhi.popularmovies.model.SortBy.TOP_RATED;

public class MoviesActivity extends AppCompatActivity {

    private MoviesAdapter moviesAdapter;
    private RecyclerView moviesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        moviesContainer = findViewById(R.id.rv_movies);
        moviesContainer.setLayoutManager(new GridLayoutManager(this, 3));
        setActionBarTitle(loadSortBy());
        loadMovies(loadSortBy());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (moviesAdapter != null && moviesAdapter.getItemCount() == 0) {
            loadMovies(loadSortBy());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SortBy sortBy = POPULAR;
        switch (item.getItemId()) {
            case R.id.sort_by_popular:
                loadMovies(POPULAR);
                break;
            case R.id.sort_by_top_rated:
                loadMovies(TOP_RATED);
                sortBy = TOP_RATED;
                break;
        }
        setActionBarTitle(sortBy);
        saveSortBy(sortBy);
        return true;
    }

    private void loadMovies(SortBy sortBy) {
        MoviesListTask task = new MoviesListTask(movies -> {
            setupUI(movies);
        }, error -> {
        }, sortBy);
        task.execute();
    }

    private void setActionBarTitle(SortBy sortBy) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (sortBy == TOP_RATED) {
                actionBar.setTitle(R.string.top_rated_movies_title);
            } else {
                actionBar.setTitle(R.string.popular_movies_title);
            }
        }
    }

    private void setupUI(List<Movie> movies) {
        moviesAdapter = new MoviesAdapter(this, movies);
        moviesAdapter.setClickListener(movie -> {
            Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MOVIE_BUNDLE_PARAM, movie);
            startActivity(movieDetailIntent);
        });
        moviesContainer.setAdapter(moviesAdapter);
    }

    private void saveSortBy(SortBy sortBy) {
        if (getIntent() != null) {
            getIntent().putExtra("sortBy", sortBy.ordinal());
        }
    }

    private SortBy loadSortBy() {
        SortBy sortBy = POPULAR;
        if (getIntent() != null) {
            sortBy = SortBy.values()[getIntent().getIntExtra("sortBy", sortBy.ordinal())];
        }
        return sortBy;
    }
}
