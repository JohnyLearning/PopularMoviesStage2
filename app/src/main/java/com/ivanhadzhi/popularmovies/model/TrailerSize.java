package com.ivanhadzhi.popularmovies.model;

public enum TrailerSize {

    size360(360),
    size480(480),
    size720(720),
    size1080(1080);

    private int size;

    TrailerSize(int size) {
        this.size = size;
    }

    public int size() {
        return size;
    }

}
