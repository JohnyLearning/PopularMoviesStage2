package com.ivanhadzhi.popularmovies.model;

public enum SortBy {
    POPULAR("popular"),
    TOP_RATED("top_rated");

    private String sortyByValue;

    private SortBy(String value) {
        sortyByValue = value;
    }

    String getSortByValue() {
        return sortyByValue;
    }

    @Override
    public String toString() {
        return sortyByValue;
    }
}
