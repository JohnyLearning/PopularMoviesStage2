package com.ivanhadzhi.popularmovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ivanhadzhi.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static final String DB_NAME = "movies";

    private static MoviesDatabase instance;

    public static MoviesDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (MoviesDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            MoviesDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            // TODO: remove before submitting
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract MovieDao movieDao();

}
