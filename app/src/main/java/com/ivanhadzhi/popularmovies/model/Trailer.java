package com.ivanhadzhi.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import com.ivanhadzhi.popularmovies.model.TrailerSize;
import com.ivanhadzhi.popularmovies.model.TrailerType;

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

}
