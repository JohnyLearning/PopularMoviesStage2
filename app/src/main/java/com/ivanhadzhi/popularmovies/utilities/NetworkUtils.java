/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ivanhadzhi.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.ivanhadzhi.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movies database servers.
 */
public final class NetworkUtils {

    private static final String API_KEY = "<YOUR_MOVIE_DB_API_KEY_HERE>";

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final String API_KEY_PARAM = "api_key";
    public static final String SORT_BY_PARAM = "sort_by";

    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */

    /* The format we want our API to return */
    private static final String format = "json";

    /* The query parameter allows us to provide a location string to the API */
    private static final String QUERY_PARAM = "q";

    public static URL getURL(Pair<String, String>[] params) {
        Uri.Builder moviesDbUriBuilder = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY);
        if (params != null && params.length > 0) {
            for (Pair<String, String> param: params) {
                moviesDbUriBuilder.appendQueryParameter(param.first, param.second);
            }
        }
        Uri moviesUri = moviesDbUriBuilder.build();

        try {
            URL moviesUrl = new URL(moviesUri.toString());
            Log.v(TAG, "URL: " + moviesUrl);
            return moviesUrl;
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}