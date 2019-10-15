package com.example.android.popularmovies.Room;

import com.example.android.popularmovies.data.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDAO
{
  @Query("SELECT * FROM movie")
  LiveData<List<Movie>>getAllMovie();

  @Insert
  void insertMovie(Movie movie);

  @Query("SELECT * FROM movie WHERE dbMovieId = :passedId")
  Movie getMovie(int passedId);

  @Delete
  void deleteMovieFromFavourites(Movie movie);


}
