package com.ivanhadzhi.popularmovies.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.network.MovieDbService;
import com.ivanhadzhi.popularmovies.model.Movie;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoviesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> topRatedMovies;
    private MovieDbService movieDbService;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        movieDbService = new MovieDbService();
    }

    public LiveData<List<Movie>> getMovies(SortBy sortBy) {
        if (sortBy == SortBy.TOP_RATED) {
            return getTopRatedMovies();
        } else {
            return getPopularMovies();
        }
    }

    private LiveData<List<Movie>> getPopularMovies() {
        if (popularMovies == null) {
            popularMovies = new MutableLiveData<>();
            fetchMovies(SortBy.POPULAR);
        }
        return popularMovies;
    }

    private LiveData<List<Movie>> getTopRatedMovies() {
        if (topRatedMovies == null) {
            topRatedMovies = new MutableLiveData<>();
            fetchMovies(SortBy.TOP_RATED);
        }
        return topRatedMovies;
    }

    private void fetchMovies(SortBy sortBy) {
        movieDbService.getMovieList(sortBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<Movie> movies = response.getMovies();
                    if (sortBy == SortBy.TOP_RATED) {
                        List<Movie> liveMovies = topRatedMovies.getValue();
                        if (liveMovies == null || liveMovies.isEmpty()) {
                            topRatedMovies.setValue(movies);
                        } else {
                            liveMovies.addAll(movies);
                            topRatedMovies.setValue(liveMovies);
                        }
                    } else {
                        List<Movie> liveMovies = popularMovies.getValue();
                        if (liveMovies == null || liveMovies.isEmpty()) {
                            popularMovies.setValue(movies);
                        } else {
                            liveMovies.addAll(movies);
                            popularMovies.setValue(liveMovies);
                        }
                    }
                });
    }

}
