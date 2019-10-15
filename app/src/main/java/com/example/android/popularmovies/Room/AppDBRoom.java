package com.example.android.popularmovies.Room;

import android.content.Context;



import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.popularmovies.data.Movie;

@Database(entities = Movie.class,exportSchema = false,version = 1)
public abstract class AppDBRoom extends RoomDatabase {



    public abstract MovieDAO dao();




}
