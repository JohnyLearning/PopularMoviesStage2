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

import android.net.Uri;
import android.util.Log;

import com.ivanhadzhi.popularmovies.model.ImageSize;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * These utilities will be used to communicate with the movies database servers.
 */
public final class NetworkUtils {

    private NetworkUtils() {
    }

    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";

    public static URL getImageURL(ImageSize size, String posterPath) {
        Uri imageUri = Uri.parse(BASE_IMAGE_URL + size + posterPath).buildUpon().build();
        try {
            return new URL(imageUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
            return null;
        }
    }

}