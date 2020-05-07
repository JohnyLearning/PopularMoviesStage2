package com.ivanhadzhi.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.ivanhadzhi.popularmovies.databinding.ActivityMoviesBinding;
import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.model.OnError;
import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.viewmodel.MoviesViewModel;

import java.util.List;

import static com.ivanhadzhi.popularmovies.MovieDetailActivity.FAVORITE_BUNDLE_PARAM;
import static com.ivanhadzhi.popularmovies.MovieDetailActivity.MOVIE_BUNDLE_PARAM;
import static com.ivanhadzhi.popularmovies.model.SortBy.FAVORITES;
import static com.ivanhadzhi.popularmovies.model.SortBy.POPULAR;
import static com.ivanhadzhi.popularmovies.model.SortBy.TOP_RATED;

public class MoviesActivity extends BaseActivity {

    private MoviesAdapter moviesAdapter;
    private MoviesViewModel moviesViewModel;

    private ActivityMoviesBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        int numberOfItemsPerRow = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 5 : 3;
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesAdapter = new MoviesAdapter(this, numberOfItemsPerRow);
        moviesAdapter.setClickListener((movie, favorite) -> {
            Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MOVIE_BUNDLE_PARAM, movie);
            movieDetailIntent.putExtra(FAVORITE_BUNDLE_PARAM, favorite);
            startActivity(movieDetailIntent);
        });
        dataBinding.rvMovies.setAdapter(moviesAdapter);
        dataBinding.rvMovies.setLayoutManager(new GridLayoutManager(this, numberOfItemsPerRow));
        setActionBarTitle(loadSortBy());
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMovies(loadSortBy(), this);
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
                loadMovies(POPULAR, this);
                break;
            case R.id.sort_by_top_rated:
                loadMovies(TOP_RATED, this);
                sortBy = TOP_RATED;
                break;
            case R.id.show_favorites:
                loadMovies(FAVORITES, this);
                sortBy = FAVORITES;
                break;
        }
        setActionBarTitle(sortBy);
        saveSortBy(sortBy);
        return true;
    }

    private void loadMovies(final SortBy sortBy, final OnError onError) {
        moviesViewModel.getMovies(POPULAR, onError).removeObservers(MoviesActivity.this);
        moviesViewModel.getMovies(TOP_RATED, onError).removeObservers(MoviesActivity.this);
        moviesViewModel.getMovies(FAVORITES, onError).removeObservers(MoviesActivity.this);
        moviesViewModel.getMovies(sortBy, onError).observe(MoviesActivity.this, movies -> {
            if (sortBy == FAVORITES && movies == null || movies.size() == 0) {
                dataBinding.rvMovies.setVisibility(View.GONE);
                dataBinding.noFavoritesFound.setVisibility(View.VISIBLE);
            } else {
                dataBinding.rvMovies.setVisibility(View.VISIBLE);
                dataBinding.noFavoritesFound.setVisibility(View.GONE);
                setupUI(movies);
            }
        });
    }

    private void setActionBarTitle(final SortBy sortBy) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (sortBy == TOP_RATED) {
                actionBar.setTitle(R.string.top_rated_movies_title);
            } else if (sortBy == FAVORITES) {
                actionBar.setTitle(R.string.favorite_movies_title);
            } else {
                actionBar.setTitle(R.string.popular_movies_title);
            }
        }
    }

    private void setupUI(List<Movie> movies) {
        moviesAdapter.addMovies(movies);
    }

    private void saveSortBy(final SortBy sortBy) {
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
