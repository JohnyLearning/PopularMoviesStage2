package com.ivanhadzhi.popularmovies.network;

import android.os.AsyncTask;
import android.util.Log;

import com.ivanhadzhi.popularmovies.BuildConfig;
import com.ivanhadzhi.popularmovies.network.data.Movie;
import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MoviesListTask extends AsyncTask<String, Void, List<Movie>> {

    public static final String TAG = MoviesListTask.class.getCanonicalName();

    private OnSuccess successCallback;
    private OnError errorCallback;
    private SortBy sortBy;

    @FunctionalInterface
    public interface OnSuccess {
        void execute(List<Movie> movies);
    }

    @FunctionalInterface
    public interface OnError {
        void error(Throwable e);
    }

    public MoviesListTask(OnSuccess successCallback,
                          OnError errorCallback,
                          SortBy sortBy) {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        this.sortBy = sortBy;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        return fetchMovies();
    }

    private List<Movie> fetchMovies() {
        URL url = NetworkUtils.getURL(sortBy);
        try {
            String response = NetworkUtils.getResponseFromHttpUrl(url);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, response);
            }
            JSONObject initial = new JSONObject(response);
            JSONArray results = initial.getJSONArray("results");
            ArrayList<Movie> movies = new ArrayList<>();
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObject = results.getJSONObject(i);
                if (movieObject != null && movieObject.getString("poster_path") != "null") {
                    movies.add(new Movie(
                            movieObject.getString("original_title"),
                            movieObject.getString("title"),
                            movieObject.getString("id"),
                            movieObject.getString("poster_path"),
                            movieObject.getString("overview"),
                            movieObject.getDouble("vote_average"),
                            movieObject.getString("release_date")));
                }
            }
            return movies;
        } catch (IOException | JSONException ie) {
            executeError(ie);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (successCallback != null) {
            successCallback.execute(movies);
        }
    }

    private void executeError(Throwable throwable) {
        Log.e(TAG, throwable.getLocalizedMessage(), throwable);
        if (errorCallback != null) {
            errorCallback.error(throwable);
        }
    }

}
