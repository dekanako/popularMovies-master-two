package com.example.android.popularmovies;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.repository.MoviesRepository;
import com.example.android.popularmovies.di.MoviesApplication;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> listOfMovies;
    @Inject
    MoviesRepository mRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        ((MoviesApplication)getApplication()).getAppComponent().injectViewModel(this);

        listOfMovies = mRepository.getListOfMoviesWithRespectToUserPreferences(getApplication());
    }

    public void refreshForNewData(){
        listOfMovies = mRepository.getListOfMoviesWithRespectToUserPreferences(getApplication());
    }
    public LiveData<List<Movie>> getListOfMovies() {
        return listOfMovies;
    }
}