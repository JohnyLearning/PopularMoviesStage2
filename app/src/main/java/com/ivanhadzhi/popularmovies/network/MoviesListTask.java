package com.ivanhadzhi.popularmovies.network;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.ivanhadzhi.popularmovies.BuildConfig;
import com.ivanhadzhi.popularmovies.model.Movie;
import com.ivanhadzhi.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MoviesListTask extends AsyncTask<String, Void, List<Movie>> {

    public static final String TAG = MoviesListTask.class.getCanonicalName();

    private OnSuccess successCallback;
    private OnError errorCallback;
    private SortByParam sortBy;

    public enum SortByParam {

        mostPopular("popular.desc"),
        topRated("vote_average.desc");

        private String paramValue;

        SortByParam(String paramValue) {
            this.paramValue = paramValue;
        }

    }

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
                          SortByParam sortBy) {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        this.sortBy = sortBy;
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        return fetchMovies();
    }

    private List<Movie> fetchMovies() {
        URL url = NetworkUtils.getURL(new Pair[]{new Pair(NetworkUtils.SORT_BY_PARAM, sortBy.paramValue)});
        try {
            String response = NetworkUtils.getResponseFromHttpUrl(url);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, response);
            }
        } catch (IOException ie) {
            executeError(ie);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
    }

    private void executeError(Throwable throwable) {
        Log.e(TAG, throwable.getLocalizedMessage(), throwable);
        if (errorCallback != null) {
            errorCallback.error(throwable);
        }
    }

}
