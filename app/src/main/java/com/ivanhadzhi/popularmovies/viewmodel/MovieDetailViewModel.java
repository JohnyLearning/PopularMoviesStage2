package com.ivanhadzhi.popularmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ivanhadzhi.popularmovies.model.Review;
import com.ivanhadzhi.popularmovies.model.Trailer;
import com.ivanhadzhi.popularmovies.network.MovieDbService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    @FunctionalInterface
    public interface OnError {
        void error(Throwable throwable);
    }

    private MutableLiveData<List<Trailer>> trailers;
    private MutableLiveData<List<Review>> reviews;

    private MovieDbService movieDbService;

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDbService = new MovieDbService();
    }

    public LiveData<List<Trailer>> getTrailers(String movieId, OnError onErrorCallback) {
        if (trailers == null) {
            trailers = new MutableLiveData<>();
            fetchTrailers(movieId, onErrorCallback);
        }
        return trailers;
    }

    public LiveData<List<Review>> getReviews(String movieId, OnError onErrorCallback) {
        if (reviews == null) {
            reviews = new MutableLiveData<>();
            fetchReviews(movieId, onErrorCallback);
        }
        return reviews;
    }

    private void fetchTrailers(String movieId, final OnError onErrorCallback) {
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
                }, error -> {
                    if (onErrorCallback != null) {
                        onErrorCallback.error(error);
                    }
                });
    }

    private void fetchReviews(String movieId, final OnError onErrorCallback) {
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
                }, error -> {
                    if (onErrorCallback != null) {
                        onErrorCallback.error(error);
                    }
                });
    }

}
