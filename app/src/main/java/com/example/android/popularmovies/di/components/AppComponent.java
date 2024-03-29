package com.example.android.popularmovies.di.components;


import android.app.Application;

import com.example.android.popularmovies.ui.detail.DetailActivity;
import com.example.android.popularmovies.ui.detail.DetailActivityViewModel;
import com.example.android.popularmovies.ui.main.MainActivity;
import com.example.android.popularmovies.ui.main.MainActivityViewModel;
import com.example.android.popularmovies.data.Room.AppDBRoom;
import com.example.android.popularmovies.data.Room.MovieDAO;
import com.example.android.popularmovies.di.MoviesApplication;
import com.example.android.popularmovies.di.modules.AppModule;
import com.example.android.popularmovies.di.modules.NetworkModules;
import com.example.android.popularmovies.di.modules.RoomModule;
import com.example.android.popularmovies.ui.review.ReviewViewModel;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModules.class, AppModule.class, RoomModule.class})
public interface AppComponent {

    void inject(MoviesApplication moviesApplication);
    
    AppDBRoom roomDatabase();

    Application application();

    MovieDAO provideMoviesDao();

    void injectActivity(MainActivity mainActivity);

    void injectDetailActivity(DetailActivity detailActivity);

    void injectViewModel(MainActivityViewModel mainActivityViewModel);

    void injectDetailActivityViewModel(DetailActivityViewModel detailActivityViewModel);

    void injectReviewViewModel(ReviewViewModel reviewViewModel);
}
