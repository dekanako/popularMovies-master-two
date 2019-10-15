package com.example.android.popularmovies;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.repository.MoviesRepository;
import com.example.android.popularmovies.di.MoviesApplication;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivityViewModel extends AndroidViewModel implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    MoviesRepository mRepository;

    private LiveData<List<Movie>> mMovies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        ((MoviesApplication)application).getAppComponent().injectViewModel(this);
        assignListOfMovies();
    }

    private void assignListOfMovies() {
        String queryType = QueryPreferences.getStoredTypeOfQuery(getApplication());

        if (queryType.equals(getApplication().getString(R.string.popular))){
            mMovies = getPopularMovies("1");
        }else if(queryType.equals(getApplication().getString(R.string.top_rated))){
            mMovies = getTopRatedMovies("1");
        }else if (queryType.equals(getApplication().getString(R.string.favourites))){
            mMovies = getFavMovies();
        }
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Timber.d("CHANGE CHANGE");
    }
}
