package com.example.android.popularmovies.di;

import android.app.Application;


import com.example.android.popularmovies.di.components.AppComponent;
import com.example.android.popularmovies.di.components.DaggerAppComponent;
import com.example.android.popularmovies.di.modules.AppModule;

import javax.inject.Inject;


public class MoviesApplication extends Application {

    @Inject
    AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
