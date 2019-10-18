package com.example.android.popularmovies;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.repository.MoviesRepository;
import com.example.android.popularmovies.di.MoviesApplication;

import javax.inject.Inject;

import timber.log.Timber;

public class DetailActivityViewModel extends AndroidViewModel {

    private LiveData<Movie> mMovie;
    @Inject MoviesRepository mRepository;

    public DetailActivityViewModel(Application application, int movie) {
        super(application);
        ((MoviesApplication)application).getAppComponent().injectDetailActivityViewModel(this);
        mMovie = mRepository.getFavouriteMovie(movie);
        Timber.d(" Movie Id is %s", movie);
    }

    public LiveData<Movie> getMovie() {
        return mMovie;
    }

    public void insertMovie(Movie movie) {
        mRepository.insertFavoriteMovie(movie);
    }

    public void removeFromFavourites(Movie movie) {
        mRepository.removeFromFavourites(movie);
    }
}
