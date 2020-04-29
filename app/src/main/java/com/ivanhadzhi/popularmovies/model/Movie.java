package com.ivanhadzhi.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String originalTitle;
    private String title;
    private String movieId;
    private String posterPath;
    private String overview;
    private Double userRating;
    private String releaseDate;

    public Movie(String originalTitle, String title, String movieId, String posterPath, String overview, Double userRating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.title = title;
        this.movieId = movieId;
        this.posterPath = posterPath;
        this.overview = overview;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public Double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalTitle);
        dest.writeString(this.title);
        dest.writeString(this.movieId);
        dest.writeString(this.posterPath);
        dest.writeString(this.overview);
        dest.writeValue(this.userRating);
        dest.writeString(this.releaseDate);
    }

    protected Movie(Parcel in) {
        this.originalTitle = in.readString();
        this.title = in.readString();
        this.movieId = in.readString();
        this.posterPath = in.readString();
        this.overview = in.readString();
        this.userRating = (Double) in.readValue(Double.class.getClassLoader());
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
