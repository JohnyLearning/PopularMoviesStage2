package com.ivanhadzhi.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanhadzhi.popularmovies.model.ImageSize;
import com.ivanhadzhi.popularmovies.network.data.Movie;
import com.ivanhadzhi.popularmovies.network.data.Trailer;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;
import com.ivanhadzhi.popularmovies.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_BUNDLE_PARAM = "movie";
    private TextView title;
    private ImageView poster;
    private TextView userRating;
    private TextView synopsis;
    private TextView releaseDate;

    private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        // TODO: refactor this method and split into a couple smaller private methods
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.movie_details_title);
        }
        Movie movie = getIntent().getParcelableExtra(MOVIE_BUNDLE_PARAM);
        poster = findViewById(R.id.movie_poster);
        Picasso.get().load(NetworkUtils.getImageURL(ImageSize.w500, movie.getPosterPath()).toString()).into(poster);
        title = findViewById(R.id.movie_title);
        title.setText(movie.getOriginalTitle());
        userRating = findViewById(R.id.movie_user_rating);
        userRating.setText(String.valueOf(movie.getUserRating()));
        synopsis = findViewById(R.id.movie_synopsis);
        synopsis.setText(movie.getOverview());
        releaseDate = findViewById(R.id.movie_release_date);
        if (movie.getReleaseDate() != null) {
            DateTime dateTime = new DateTime(movie.getReleaseDate());
            releaseDate.setText(dateTime.toString("d MMM, yyyy"));
        }
        movieDetailViewModel.getTrailers(movie.getMovieId()).observe(MovieDetailActivity.this, trailers -> {setupTrailers(trailers);});
        movieDetailViewModel.getReviews(movie.getMovieId()).observe(MovieDetailActivity.this, reviews -> {});
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupTrailers(List<Trailer> trailers) {
        Log.d("RAG", String.valueOf(trailers.size()));
    }


}
