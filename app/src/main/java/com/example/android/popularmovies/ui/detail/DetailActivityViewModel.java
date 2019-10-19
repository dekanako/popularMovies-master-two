package com.example.android.popularmovies.ui.detail;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.repository.MoviesRepository;
import com.example.android.popularmovies.di.MoviesApplication;

import javax.inject.Inject;


public class DetailActivityViewModel extends AndroidViewModel {

    private LiveData<Movie> mMovieInDatabase;
    private LiveData<Movie> mDetailedMovie;
    @Inject MoviesRepository mRepository;

    public DetailActivityViewModel(Application application, int movieId) {
        super(application);
        ((MoviesApplication)application).getAppComponent().injectDetailActivityViewModel(this);
        mMovieInDatabase = mRepository.getFavouriteMovie(movieId);
        mDetailedMovie = mRepository.getMovieWithItsTrailer(movieId);
    }

    public LiveData<Movie> getMovieInDatabase() {
        return mMovieInDatabase;
    }

    public LiveData<Movie> getDetailedMovie() {
        return mDetailedMovie;
    }

    public void insertMovie(Movie movie) {
        mRepository.insertFavoriteMovie(movie);
    }

    public void removeFromFavourites(Movie movie) {
        mRepository.removeFromFavourites(movie);
    }
}
