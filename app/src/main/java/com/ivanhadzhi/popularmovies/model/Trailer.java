package com.ivanhadzhi.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    @SerializedName("id")
    private String trailerId;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private TrailerSize size;

    @SerializedName("type")
    private TrailerType type;

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
