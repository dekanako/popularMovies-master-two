package com.example.android.popularmovies.di.modules;



import com.example.android.popularmovies.MovieApiService;
import com.example.android.popularmovies.Room.AppDBRoom;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public abstract class NetworkModules {

    @Singleton
    @Provides
    public static Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(MovieApiService.BASE_MOVIE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public static MovieApiService provideMovieApiService(Retrofit retrofit){
        return retrofit.create(MovieApiService.class);
    }


}
