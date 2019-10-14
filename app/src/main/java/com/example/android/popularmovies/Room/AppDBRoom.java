package com.example.android.popularmovies.Room;

import android.app.Application;
import android.content.Context;

import com.example.android.popularmovies.Model.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Movie.class,exportSchema = false,version = 1)
public abstract class AppDBRoom extends RoomDatabase {

    public static final String MOVIE_DB = "MovieDB";

    public abstract MovieDAO dao();

    private static AppDBRoom sInstance;

    public static AppDBRoom getInstance(Context context)
    {
        synchronized (AppDBRoom.class)
        {
            if (sInstance == null)
            {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDBRoom.class, MOVIE_DB)

                        .build();
            }
            return sInstance;
        }
    }

}
