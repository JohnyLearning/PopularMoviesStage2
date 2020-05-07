package com.ivanhadzhi.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesListResponse {

    @SerializedName("results")
    private List<Movie> movies;

    public MoviesListResponse(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

}
