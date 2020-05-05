package com.ivanhadzhi.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import com.ivanhadzhi.popularmovies.model.TrailerSize;
import com.ivanhadzhi.popularmovies.model.TrailerType;

public class Review {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
