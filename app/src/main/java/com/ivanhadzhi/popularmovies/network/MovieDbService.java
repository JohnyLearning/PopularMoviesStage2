package com.ivanhadzhi.popularmovies.network;

import com.ivanhadzhi.popularmovies.BuildConfig;
import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.network.data.MoviesListResponse;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieDbService {

    /**
     * To prospective reviewers: please replace the value of the API_KEY with your own Movie DB api key
     */
    private static final String MOVIE_DB_API_KEY = BuildConfig.MOVIE_DB_KEY;

    private MovieDbApi movieDbApi;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

    public MovieDbService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        this.movieDbApi = retrofit.create(MovieDbApi.class);;
    }

    public Single<MoviesListResponse> getMovieList(SortBy sortBy) {
        if (sortBy == SortBy.TOP_RATED) {
            return movieDbApi.getTopRatedMovies(MOVIE_DB_API_KEY);
        } else {
            return movieDbApi.getPopularMovies(MOVIE_DB_API_KEY);
        }
    }

}
