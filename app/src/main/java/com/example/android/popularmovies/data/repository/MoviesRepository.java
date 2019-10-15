package com.example.android.popularmovies.data.repository;

import com.example.android.popularmovies.MovieApiService;
import com.example.android.popularmovies.Room.AppDBRoom;

import javax.inject.Inject;

public class MoviesRepository {

    private MovieApiService mMovieApiService;
    private AppDBRoom mDBRoom;

    @Inject
    public MoviesRepository(MovieApiService movieApiService, AppDBRoom DBRoom) {
        mMovieApiService = movieApiService;
        mDBRoom = DBRoom;
    }


}
