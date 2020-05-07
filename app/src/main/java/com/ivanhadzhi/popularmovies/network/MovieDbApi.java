package com.ivanhadzhi.popularmovies.network;

import com.ivanhadzhi.popularmovies.model.MoviesListResponse;
import com.ivanhadzhi.popularmovies.model.ReviewsListResponse;
import com.ivanhadzhi.popularmovies.model.TrailersListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface MovieDbApi {

    @GET("movie/popular")
    Single<MoviesListResponse> getPopularMovies();

    @GET("movie/top_rated")
    Single<MoviesListResponse> getTopRatedMovies();

    @GET("movie/{movie_id}/videos")
    Single<TrailersListResponse> getTrailers(@Path("movie_id") String movieId);

    @GET("movie/{movie_id}/reviews")
    Single<ReviewsListResponse> getReviews(@Path("movie_id") String movieId);

}
