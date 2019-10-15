package com.example.android.popularmovies.di.modules;

import android.app.Application;

import androidx.room.Room;

import com.example.android.popularmovies.data.Room.AppDBRoom;
import com.example.android.popularmovies.data.Room.MovieDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RoomModule {


    @Singleton
    @Provides
    public static AppDBRoom provideMovieDatabase(Application application){
        return Room.databaseBuilder(application, AppDBRoom.class, "demo-db")
                .build();
    }

    @Singleton
    @Provides
    public static MovieDAO providesProductDao(AppDBRoom appDBRoom) {
        return appDBRoom.dao();
    }

}
