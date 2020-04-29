package com.ivanhadzhi.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanhadzhi.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_BUNDLE_PARAM = "movie";
    private TextView title;
    private ImageView poster;
    private TextView userRating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Movie movie = getIntent().getParcelableExtra(MOVIE_BUNDLE_PARAM);
        // TODO: move this url to network utils
        poster = findViewById(R.id.movie_poster);
        Picasso.get().load("http://image.tmdb.org/t/p/w500" + movie.getPosterPath()).into(poster);
        title = findViewById(R.id.movie_title);
        title.setText(movie.getOriginalTitle());
        userRating = findViewById(R.id.movie_user_rating);
        userRating.setText(String.valueOf(movie.getUserRating()));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
