package com.ivanhadzhi.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import com.ivanhadzhi.popularmovies.model.TrailerSize;
import com.ivanhadzhi.popularmovies.model.TrailerType;

public class Review {

    @SerializedName("id")
    private String trailerId;

    @SerializedName("author")
    private String key;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

}
