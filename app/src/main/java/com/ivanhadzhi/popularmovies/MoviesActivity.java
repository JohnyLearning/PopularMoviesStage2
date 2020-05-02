package com.ivanhadzhi.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.network.data.Movie;
import com.ivanhadzhi.popularmovies.viewmodel.MoviesViewModel;

import java.util.List;

import static com.ivanhadzhi.popularmovies.MovieDetailActivity.MOVIE_BUNDLE_PARAM;
import static com.ivanhadzhi.popularmovies.model.SortBy.POPULAR;
import static com.ivanhadzhi.popularmovies.model.SortBy.TOP_RATED;

public class MoviesActivity extends AppCompatActivity {

    private MoviesAdapter moviesAdapter;
    private RecyclerView moviesContainer;
    private MoviesViewModel moviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        moviesContainer = findViewById(R.id.rv_movies);
        int numberOfItemsPerRow = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 5 : 3;
        moviesContainer.setLayoutManager(new GridLayoutManager(this, numberOfItemsPerRow));
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesAdapter = new MoviesAdapter(this);
        moviesAdapter.setClickListener(movie -> {
            Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MOVIE_BUNDLE_PARAM, movie);
            startActivity(movieDetailIntent);
        });
        moviesContainer.setAdapter(moviesAdapter);
        setActionBarTitle(loadSortBy());
        loadMovies(loadSortBy());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (moviesAdapter != null && moviesAdapter.getItemCount() == 0) {
            ;
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
        moviesViewModel.getMovies(sortBy).observe(MoviesActivity.this, movies -> setupUI(movies));
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
        moviesAdapter.addMovies(movies);
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
