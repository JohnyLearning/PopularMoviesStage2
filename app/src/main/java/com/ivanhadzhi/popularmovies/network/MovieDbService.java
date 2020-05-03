package com.ivanhadzhi.popularmovies.network;

import com.ivanhadzhi.popularmovies.BuildConfig;
import com.ivanhadzhi.popularmovies.model.SortBy;
import com.ivanhadzhi.popularmovies.network.data.MoviesListResponse;
import com.ivanhadzhi.popularmovies.network.data.ReviewsListResponse;
import com.ivanhadzhi.popularmovies.network.data.Trailer;
import com.ivanhadzhi.popularmovies.network.data.TrailersListResponse;

import java.io.IOException;

import io.reactivex.Single;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// TODO: consider using ServiceGenerator
public class MovieDbService {

    /**
     * To prospective reviewers: please replace the value of the API_KEY with your own Movie DB api key
     */
    private static final String MOVIE_DB_API_KEY = BuildConfig.MOVIE_DB_KEY;

    private MovieDbApi movieDbApi;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(new Interceptor() {
                // reference: https://futurestud.io/tutorials/retrofit-2-how-to-add-query-parameters-to-every-request
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    HttpUrl originalHttpUrl = original.url();

                    HttpUrl url = originalHttpUrl.newBuilder()
                            .addQueryParameter("api_key", MOVIE_DB_API_KEY)
                            .build();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .url(url);

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            })
            .build();

    public MovieDbService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        this.movieDbApi = retrofit.create(MovieDbApi.class);;
    }

    public Single<MoviesListResponse> getMovieList(SortBy sortBy) {
        if (sortBy == SortBy.TOP_RATED) {
            return movieDbApi.getTopRatedMovies();
        } else {
            return movieDbApi.getPopularMovies();
        }
    }

    public Single<TrailersListResponse> getTrailers(String movieId) {
        // TODO: handle onError
        return movieDbApi.getTrailers(movieId);
    }

    public Single<ReviewsListResponse> getReviews(String movieId) {
        // TODO: handle onError
        return movieDbApi.getReviews(movieId);
    }

}
