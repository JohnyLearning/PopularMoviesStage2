package com.ivanhadzhi.popularmovies.network;

import com.ivanhadzhi.popularmovies.BuildConfig;
import com.ivanhadzhi.popularmovies.network.data.MoviesListResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieDbService {

    private MovieDbApi movieDbApi;

    public MovieDbService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        this.movieDbApi = retrofit.create(MovieDbApi.class);;
    }

    public Single<MoviesListResponse> getPopularMovieList() {
        return movieDbApi.getPopularMovies(BuildConfig.MOVIE_DB_KEY);
    }

}
