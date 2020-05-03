package com.ivanhadzhi.popularmovies.network.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersListResponse {

    @SerializedName("results") private List<Trailer> trailers;

    public TrailersListResponse(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
