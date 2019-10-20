package com.example.android.popularmovies.ui;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.popularmovies.ui.detail.DetailActivityViewModel;
import com.example.android.popularmovies.ui.review.ReviewViewModel;

import javax.inject.Inject;

import timber.log.Timber;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private int movieId;
    private Application mApplication;

    @Inject
    public ViewModelFactory(Application application, int movieId) {
        this.movieId = movieId;
        this.mApplication = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        String viewModelType = modelClass.getSimpleName();
        if (viewModelType.equals(DetailActivityViewModel.class.getSimpleName())){
            return (T) new DetailActivityViewModel(mApplication,movieId);
        }else {
            return (T) new ReviewViewModel(mApplication,movieId);
        }
    }


}
