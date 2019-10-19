package com.example.android.popularmovies.ui.detail;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.ui.detail.DetailActivityViewModel;

import javax.inject.Inject;

public class DetailActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private int movieId;
    private Application mApplication;

    @Inject
    public DetailActivityViewModelFactory(Application application,int movieId) {
        this.movieId = movieId;
        this.mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(mApplication,movieId);
    }
}
