package com.ivanhadzhi.popularmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.ivanhadzhi.popularmovies.databinding.ActivityMovieDetailBinding;
import com.ivanhadzhi.popularmovies.model.ImageSize;
import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.model.Trailer;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;
import com.ivanhadzhi.popularmovies.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.List;

public class MovieDetailActivity extends BaseActivity {

    public static final String MOVIE_BUNDLE_PARAM = "movie";
    private TextView title;
    private ImageView poster;
    private TextView userRating;
    private TextView synopsis;
    private TextView releaseDate;

    private MovieDetailViewModel movieDetailViewModel;

    private ActivityMovieDetailBinding dataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        movieDetailViewModel = ViewModelProviders.of(this).get(MovieDetailViewModel.class);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.movie_details_title);
        }
        Movie movie = getIntent().getParcelableExtra(MOVIE_BUNDLE_PARAM);
        bindData(movie);
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

    private void bindData(Movie movie) {
        Picasso.get().load(NetworkUtils.getImageURL(ImageSize.w500, movie.getPosterPath()).toString()).into(dataBinding.moviePoster);
        dataBinding.movieTitle.setText(movie.getOriginalTitle());
        dataBinding.movieUserRating.setText(String.format("%.1f/%d", movie.getUserRating(), 10));
        dataBinding.movieSynopsis.setText(movie.getOverview());
        if (movie.getReleaseDate() != null) {
            DateTime dateTime = new DateTime(movie.getReleaseDate());
            dataBinding.movieReleaseDate.setText(dateTime.toString("d MMM, yyyy"));
        }
    }

    private void setupTrailers(List<Trailer> trailers) {
        Log.d("RAG", String.valueOf(trailers.size()));
    }


}
