package com.example.android.popularmovies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.repository.MoviesRepository;
import com.example.android.popularmovies.di.MoviesApplication;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends AndroidViewModel {

    @Inject
    MoviesRepository mRepository;

    private LiveData<List<Movie>> mMovies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        ((MoviesApplication)application).getAppComponent().injectViewModel(this);
        mMovies = getFavMovies();
    }

    public LiveData<List<Movie>>getFavMovies(){
        return mRepository.getFavouriteMovies();
    }

    public LiveData<List<Movie>>getPopularMovies(String page){
        return mRepository.getPopularMovies(page);
    }

    public LiveData<List<Movie>>getTopRatedMovies(String page){
        return mRepository.getTopRatedMovies(page);
    }
}
