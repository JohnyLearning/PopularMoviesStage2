package com.ivanhadzhi.popularmovies.network;

import com.ivanhadzhi.popularmovies.network.data.MoviesListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbApi {

    @GET("movie/popular")
    Single<MoviesListResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Single<MoviesListResponse> getTopRatedMovies(@Query("api_key") String apiKey);

}
