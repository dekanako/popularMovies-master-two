package com.example.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieContainer {
    @SerializedName("results")
    private List<Movie> mMovieList;

    public MovieContainer(List<Movie> movieList) {
        mMovieList = movieList;
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(List<Movie> movieList) {
        mMovieList = movieList;
    }
}
