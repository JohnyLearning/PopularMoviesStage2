package com.ivanhadzhi.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.network.MovieDbService;
import com.ivanhadzhi.popularmovies.network.data.Movie;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoviesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> popularMovies;
    private MovieDbService movieDbService;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        movieDbService = new MovieDbService();
    }
    public LiveData<List<Movie>> getPopularMovies() {
        if (popularMovies == null) {
            popularMovies = new MutableLiveData<>();
            fetchMovies(SortBy.POPULAR);
        }
        return popularMovies;
    }

    private void fetchMovies(SortBy sortBy) {
        movieDbService.getPopularMovieList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<Movie> movies = response.getMovies();
                    List<Movie> liveMovies = popularMovies.getValue();
                    if (liveMovies == null || liveMovies.isEmpty()) {
                        popularMovies.setValue(movies);
                    } else {
                        liveMovies.addAll(movies);
                        popularMovies.setValue(liveMovies);
                    }
                });
    }

}
