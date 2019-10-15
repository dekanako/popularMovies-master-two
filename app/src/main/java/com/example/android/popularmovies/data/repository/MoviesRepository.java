package com.example.android.popularmovies.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.MovieApiService;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieContainer;
import com.example.android.popularmovies.data.Room.AppDBRoom;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
public class MoviesRepository {

    private AppDBRoom mRoom;
    private MovieApiService mMovieApiService;

    @Inject
    public MoviesRepository(AppDBRoom room, MovieApiService movieApiService) {
        mRoom = room;
        mMovieApiService = movieApiService;
    }


    public LiveData<List<Movie>>getPopularMovies(String page){
        final MutableLiveData<List<Movie>> moviesLiveDate = new MutableLiveData<>();
        mMovieApiService.getMovies(MovieApiService.POPULAR_PATH,MovieApiService.API_KEY,"1",MovieApiService.ENG_LANG_RESULT).enqueue(new Callback<MovieContainer>() {
            @Override
            public void onResponse(@NonNull Call<MovieContainer> call, @NonNull Response<MovieContainer> response) {
                Timber.d("RESPONSE MESSAGE :%s", response.message());
                Timber.d("RESPONSE CODE :%s", response.code());

                moviesLiveDate.setValue(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieContainer> call, Throwable t) {
                Timber.e(t);
            }
        });
        return moviesLiveDate;
    }


    public LiveData<List<Movie>>getTopRatedMovies(String page){
        final MutableLiveData<List<Movie>> moviesLiveDate = new MutableLiveData<>();
        mMovieApiService.getMovies(MovieApiService.TOP_RATED_PATH,MovieApiService.API_KEY,"1",MovieApiService.ENG_LANG_RESULT).enqueue(new Callback<MovieContainer>() {
            @Override
            public void onResponse(Call<MovieContainer> call, Response<MovieContainer> response) {
                Timber.d("RESPONSE MESSAGE :%s", response.message());
                Timber.d("RESPONSE CODE :%s", response.code());

                moviesLiveDate.setValue(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieContainer> call, Throwable t) {
                Timber.e(t);
            }
        });
        return moviesLiveDate;
    }

    public LiveData<List<Movie>>getFavouriteMovies(){
        return mRoom.dao().getAllMovie();
    }
}
