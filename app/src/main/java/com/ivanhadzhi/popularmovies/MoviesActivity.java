package com.ivanhadzhi.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ivanhadzhi.popularmovies.databinding.ActivityMoviesBinding;
import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.viewmodel.MoviesViewModel;

import java.util.List;

import static com.ivanhadzhi.popularmovies.MovieDetailActivity.MOVIE_BUNDLE_PARAM;
import static com.ivanhadzhi.popularmovies.model.SortBy.FAVORITES;
import static com.ivanhadzhi.popularmovies.model.SortBy.POPULAR;
import static com.ivanhadzhi.popularmovies.model.SortBy.TOP_RATED;

    public class MoviesActivity extends BaseActivity {

    private MoviesAdapter moviesAdapter;
    private RecyclerView moviesContainer;
    private MoviesViewModel moviesViewModel;

    private ActivityMoviesBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        int numberOfItemsPerRow = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 5 : 3;
        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        moviesAdapter = new MoviesAdapter(this, numberOfItemsPerRow);
        moviesAdapter.setClickListener(movie -> {
            Intent movieDetailIntent = new Intent(this, MovieDetailActivity.class);
            movieDetailIntent.putExtra(MOVIE_BUNDLE_PARAM, movie);
            startActivity(movieDetailIntent);
        });
        dataBinding.rvMovies.setAdapter(moviesAdapter);
        dataBinding.rvMovies.setLayoutManager(new GridLayoutManager(this, numberOfItemsPerRow));
        setActionBarTitle(loadSortBy());
        loadMovies(loadSortBy());
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
            case R.id.show_favorites:
                loadMovies(FAVORITES);
                sortBy = FAVORITES;
                break;
        }
        setActionBarTitle(sortBy);
        saveSortBy(sortBy);
        return true;
    }

    private void loadMovies(SortBy sortBy) {
        moviesViewModel.getMovies(sortBy).observe(MoviesActivity.this, movies -> setupUI(movies));
    }

    private void loadFavorites() {
        moviesViewModel.getMovies(FAVORITES).observe(MoviesActivity.this, movies -> setupUI(movies));
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
