package com.example.android.popularmovies.data.repository;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android.popularmovies.MovieApiService;
import com.example.android.popularmovies.QueryPreferences;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieContainer;
import com.example.android.popularmovies.data.Room.AppDBRoom;
import com.example.android.popularmovies.data.Room.AppExecutors;

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


    public MutableLiveData<List<Movie>>getPopularMovies(String page){

        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();

        mMovieApiService.getMovies(MovieApiService.POPULAR_PATH,MovieApiService.API_KEY,"1",MovieApiService.ENG_LANG_RESULT).enqueue(new Callback<MovieContainer>() {
            @Override
            public void onResponse(@NonNull Call<MovieContainer> call, @NonNull Response<MovieContainer> response) {
                mutableLiveData.setValue(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieContainer> call, Throwable t) {
                Timber.e(t);
            }
        });


        return mutableLiveData;
    }

    public MutableLiveData<List<Movie>>getTopRatedMovies(String page){
        MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();
        mMovieApiService.getMovies(MovieApiService.TOP_RATED_PATH,MovieApiService.API_KEY,"1",MovieApiService.ENG_LANG_RESULT).enqueue(new Callback<MovieContainer>() {
            @Override
            public void onResponse(Call<MovieContainer> call, Response<MovieContainer> response) {
                mutableLiveData.setValue(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieContainer> call, Throwable t) {
                Timber.e(t);
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<Movie>>getFavouriteMovies(){
        return mRoom.dao().getAllMovie();
    }

    public LiveData<List<Movie>> getListOfMoviesWithRespectToUserPreferences(Application application) {

        Timber.d("ON OF THEM IS " + QueryPreferences.getStoredTypeOfQuery(application));

        if (application.getString(R.string.popular).equals(QueryPreferences.getStoredTypeOfQuery(application))){
            return getPopularMovies("1");
        }else if (application.getString(R.string.top_rated).equals(QueryPreferences.getStoredTypeOfQuery(application))){
            return getTopRatedMovies("1");
        }else {
            return getFavouriteMovies();
        }
    }

    public LiveData<Movie>getFavouriteMovie(int movieId){
        return mRoom.dao().getMovie(movieId);
    }

    public void insertFavoriteMovie(Movie movie) {
        try {
            AppExecutors.getInstance().diskIO().execute(()-> mRoom.dao().insertMovie(movie) );
        }catch (SQLiteConstraintException e){
            Timber.e(e);
        }

    }

    public void removeFromFavourites(Movie movie) {
        try {
            AppExecutors.getInstance().diskIO().execute(()-> mRoom.dao().deleteMovieFromFavourites(movie));
        }catch (SQLiteConstraintException e){
            Timber.e(e);
        }
    }

    public LiveData<Movie> getMovieWithItsTrailer(int movieId) {
        MutableLiveData<Movie>mutableLiveData = new MutableLiveData<>();

        mMovieApiService.getMovieById(movieId,
                MovieApiService.API_KEY,
                MovieApiService.ENG_LANG_RESULT,
                MovieApiService.VIDEO_APPEND).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) { Timber.e(t); }
        });
        return mutableLiveData;
    }
}
