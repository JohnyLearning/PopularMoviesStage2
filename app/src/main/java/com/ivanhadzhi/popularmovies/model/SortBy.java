package com.ivanhadzhi.popularmovies.model;

public enum SortBy {
    POPULAR("popular"),
    TOP_RATED("top_rated"),
    FAVORITES("favorites");

    private String sortByValue;

    SortBy(String value) {
        sortByValue = value;
    }

    @Override
    public String toString() {
        return sortByValue;
    }
}
