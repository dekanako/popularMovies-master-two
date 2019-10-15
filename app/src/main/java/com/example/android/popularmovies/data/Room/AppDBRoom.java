package com.example.android.popularmovies.data.Room;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.android.popularmovies.data.Movie;

@Database(entities = Movie.class,exportSchema = false,version = 1)
public abstract class AppDBRoom extends RoomDatabase {

    public abstract MovieDAO dao();

}
