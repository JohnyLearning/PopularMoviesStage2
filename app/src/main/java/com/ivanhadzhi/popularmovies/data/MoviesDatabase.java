package com.ivanhadzhi.popularmovies.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ivanhadzhi.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MoviesDatabase extends RoomDatabase {

    private static MoviesDatabase instance;

    public static MoviesDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (MoviesDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            MoviesDatabase.class, "popularmovies")
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract MovieDao movieDao();

}
