package com.ivanhadzhi.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ivanhadzhi.popularmovies.network.MovieDbService;
import com.ivanhadzhi.popularmovies.network.data.Review;
import com.ivanhadzhi.popularmovies.network.data.Trailer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    private MutableLiveData<List<Trailer>> trailers;
    private MutableLiveData<List<Review>> reviews;

    private MovieDbService movieDbService;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDbService = new MovieDbService();
    }

    public LiveData<List<Trailer>> getTrailers(String movieId) {
        if (trailers == null) {
            trailers = new MutableLiveData<>();
            fetchTrailers(movieId);
        }
        return trailers;
    }

    public LiveData<List<Review>> getReviews(String movieId) {
        if (reviews == null) {
            reviews = new MutableLiveData<>();
            fetchReviews(movieId);
        }
        return reviews;
    }

    private void fetchTrailers(String movieId) {
        movieDbService.getTrailers(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<Trailer> freshTrailers = response.getTrailers();
                    List<Trailer> liveTrailers = trailers.getValue();
                    if (liveTrailers == null || liveTrailers.isEmpty()) {
                        trailers.setValue(freshTrailers);
                    } else {
                        liveTrailers.addAll(freshTrailers);
                        trailers.setValue(liveTrailers);
                    }
                });
    }

    private void fetchReviews(String movieId) {
        movieDbService.getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<Review> freshReviews = response.getReviews();
                    List<Review> liveReviews = reviews.getValue();
                    if (liveReviews == null || liveReviews.isEmpty()) {
                        reviews.setValue(freshReviews);
                    } else {
                        liveReviews.addAll(freshReviews);
                        reviews.setValue(liveReviews);
                    }
                });
    }

}
