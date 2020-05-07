package com.ivanhadzhi.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsListResponse {

    @SerializedName("results")
    private List<Review> reviews;

    public ReviewsListResponse(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
