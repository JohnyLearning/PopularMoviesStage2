package com.ivanhadzhi.popularmovies;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ivanhadzhi.popularmovies.data.MovieDao;
import com.ivanhadzhi.popularmovies.data.MoviesDatabase;
import com.ivanhadzhi.popularmovies.databinding.ActivityMovieDetailBinding;
import com.ivanhadzhi.popularmovies.model.ImageSize;
import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;
import com.ivanhadzhi.popularmovies.viewmodel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.view.View.GONE;

public class MovieDetailActivity extends BaseActivity {

    public static final String MOVIE_BUNDLE_PARAM = "movie";
    public static final String FAVORITE_BUNDLE_PARAM = "favorite";

    private MovieDetailViewModel movieDetailViewModel;

    private ActivityMovieDetailBinding dataBinding;

    private Executor executor;

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
        bindData(movie, getIntent().getBooleanExtra(FAVORITE_BUNDLE_PARAM, false));
        bindTrailers(movie.getMovieId());
        bindReviews(movie.getMovieId());
        executor = Executors.newFixedThreadPool(2);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindData(Movie movie, boolean favorite) {
        Picasso.get()
                .load(NetworkUtils.getImageURL(ImageSize.w500, movie.getPosterPath()).toString())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(dataBinding.moviePoster);
        dataBinding.movieTitle.setText(movie.getOriginalTitle());
        dataBinding.movieUserRating.setText(String.format("%.1f/%d", movie.getUserRating(), 10));
        dataBinding.movieSynopsis.setText(movie.getOverview());
        setFavoriteImage(favorite);
        dataBinding.favoriteImage.setOnClickListener(view -> {
            markFavorite(movie);
        });
        if (movie.getReleaseDate() != null) {
            DateTime dateTime = new DateTime(movie.getReleaseDate());
            dataBinding.movieReleaseDate.setText(dateTime.toString("d MMM, yyyy"));
        }
    }

    private void setFavoriteImage(boolean favorite) {
        if (favorite) {
            dataBinding.favoriteImage.setImageResource(R.drawable.favorite_selected);
        } else {
            dataBinding.favoriteImage.setImageResource(R.drawable.favorite);
        }
    }

    private void markFavorite(Movie movie) {
        MovieDao dao = MoviesDatabase.getInstance(this).movieDao();
        boolean favorite = getFavorite();
        if (favorite) {
            executor.execute(() -> {
                dao.delete(movie);
            });
        } else {
            executor.execute(() -> {
                dao.insert(movie);
            });
        }
        updateFavorite(!favorite);
        setFavoriteImage(!favorite);
    }

    private void updateFavorite(boolean favorite) {
        if (getIntent() != null) {
            getIntent().putExtra(FAVORITE_BUNDLE_PARAM, favorite);
        }
    }

    private boolean getFavorite() {
        boolean favorite = false;
        if (getIntent() != null) {
            favorite = getIntent().getBooleanExtra(FAVORITE_BUNDLE_PARAM, false);
        }
        return favorite;
    }

    private void bindTrailers(String movieId) {
        RecyclerView trailersView = dataBinding.rvTrailers;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        trailersView.setLayoutManager(layoutManager);
        movieDetailViewModel.getTrailers(movieId, this)
                .observe(MovieDetailActivity.this, trailers -> {
                    if (trailers != null && trailers.size() > 0) {
                        TrailerAdapter adapter = new TrailerAdapter(this);
                        adapter.addTrailers(trailers);
                        dataBinding.rvTrailers.setAdapter(adapter);
                        setTrailersVisibility(View.VISIBLE);
                    } else {
                        setTrailersVisibility(GONE);
                    }
                });
    }

    private void setTrailersVisibility(int visibility) {
        dataBinding.trailersTitle.setVisibility(visibility);
        dataBinding.trailersDivider.setVisibility(visibility);
        dataBinding.rvTrailers.setVisibility(visibility);
    }

    private void bindReviews(String movieId) {
        RecyclerView reviewsView = dataBinding.rvReviews;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        reviewsView.setLayoutManager(layoutManager);
        movieDetailViewModel.getReviews(movieId, this)
                .observe(MovieDetailActivity.this, reviews -> {
                    if (reviews != null && reviews.size() > 0) {
                        ReviewAdapter adapter = new ReviewAdapter(this);
                        adapter.addReviews(reviews);
                        dataBinding.rvReviews.setAdapter(adapter);
                        setReviewsVisibility(View.VISIBLE);
                    } else {
                        setReviewsVisibility(GONE);
                    }
                });
    }

    private void setReviewsVisibility(int visibility) {
        dataBinding.reviewsTitle.setVisibility(visibility);
        dataBinding.rvReviewsDivider.setVisibility(visibility);
        dataBinding.rvReviews.setVisibility(visibility);
    }

}
